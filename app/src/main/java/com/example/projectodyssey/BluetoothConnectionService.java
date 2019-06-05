package com.example.projectodyssey;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;


    String myPublicKey = "0x015Bbab8756B37d8D14e7ff7f915650b37161f39";
    Credentials credentials = Credentials.create("629054BB24F430E96C6BFFC58F186371695BC3BFC695E76CEF54DAFCA460BC0C");

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;
    boolean isIncompatibleDevice;
    public String currentMessage;
    public String currentLocation;
    boolean justcancelled;
    boolean unlock;
    String privateKey;
    String proverPublicKey;


    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        isIncompatibleDevice = true;
        justcancelled = false;
        unlock = false;
        currentMessage = "Unavailable";
        currentLocation = "Unavailable";
        mContext = context;
        start();
    }


    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // the local server socket

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread () {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.d(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "run: AcceptThread running.");

            BluetoothSocket socket = null;

            try{
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start...");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");
            }catch (IOException e){
                Log.d(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            // talk about this in 3rd Video
            if (socket != null) {
                connected(socket, mmDevice);
            }

            Log.i(TAG, "END mAcceptThread");
        }

        public void cancel () {
            Log.d(TAG, "cancel: Cancelling AcceptThread.");
            try {
                mmServerSocket.close();
            }catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread failed. " + e.getMessage());
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread.");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice

            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID:" + MY_UUID_INSECURE);
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
                unlock = true;
            }

            mmSocket = tmp;
            Log.d(TAG, "CHECK HERE: " + tmp);
            // Always cancel discovery because it will slow down connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
                isIncompatibleDevice = false;
                Log.e(TAG, "Device " + mmDevice.getName() + "is compatible, so FALSE!");
                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                Log.e(TAG, "mConnectThread: run: Unable to connect in socket: " + e.getMessage());
                try {

                    mmSocket.close();

                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket: " + e1.getMessage());
                    unlock = true;
                }

                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
                Log.d(TAG, "isIncompatibleDevice BECOMES TRUE.");
                isIncompatibleDevice = true;
            }
            // will talk about this in the 3rd video

            connected(mmSocket, mmDevice);
        }

        public void cancel () {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                unlock = true;
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in ConnectThread failed. " + e.getMessage());
            }
        }
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread trying to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    public synchronized void start2() {
        Log.d(TAG, "start");

        mInsecureAcceptThread.cancel();
        mInsecureAcceptThread = new AcceptThread();
        mInsecureAcceptThread.start();

    }

    /**
     * AcceptThread starts and sits waiting for a connection
     * Then ConnectThread starts and attempts to make a connection with
     * the other devices' AcceptThread
     **/
     public int startClient (BluetoothDevice device, UUID uuid) {
         Log.d(TAG, "startClient: Started.");

         // initprogress dialog
         mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth", "Please wait...", true);

         mConnectThread = new ConnectThread(device, uuid);
         mConnectThread.start();

         return 0;
     }

     public void cancel () {
         if (mConnectedThread != null)
         mConnectedThread.cancel();
     }


    /**
     * Finally the ConnectedThread which is responsible for maintaining
     * the BTConnection, sending the data, receiving incoming data through
     * IO streams
     **/
    private class ConnectedThread extends Thread {
          private final BluetoothSocket mmSocket;
          private final InputStream mmInStream;
          private final OutputStream mmOutStream;
          private KeyPair currentKeyPair;

         public ConnectedThread(BluetoothSocket socket) {
             Log.d(TAG, "ConnectedThread: Starting.");

             mmSocket = socket;
             InputStream tmpIn = null;
             OutputStream tmpOut = null;

             // dismiss the ProgressDialog when connection is established
             try {
                 mProgressDialog.dismiss();
             }catch (NullPointerException e) {
                 e.printStackTrace();
             }


             try {
                 tmpIn = mmSocket.getInputStream();
                 tmpOut = mmSocket.getOutputStream();
             } catch (IOException e) {
                 Log.e(TAG, "ConnectedThread: Socket failure: " + e.getMessage());
             }

             mmInStream = tmpIn;
             mmOutStream = tmpOut;
         }

        public void run() {
            // Buffer store for the stream
            byte [] buffer = new byte[1024];
            // Bytes returned from read()
            int bytes;

            unlock = true;



            // Keep listening to the input stream until an exception occurs
            while (true) {
                // Read from the input stream
                try {
                    Log.d(TAG,"mmInStream is: " + mmInStream.available());
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                    currentMessage = incomingMessage;

                    if (currentMessage.startsWith("POL request: ")) respondPoL2(currentMessage);
                    else if (currentMessage.startsWith("My Public Key is:")) respondPublicKey(currentMessage);
                    else if (currentMessage.startsWith("PoL Request: ")) respondPoL(currentMessage);

                } catch (IOException e) {
                    Log.e(TAG, "read: Error reading inputStream: " + e.getMessage());
                    try {

                        mmInStream.close();
                        mmSocket.close();
                        unlock = false;

                        if (!justcancelled) {
                            //
                            //mBluetoothAdapter.disable();
                            //Thread.sleep(1000);
                            //mBluetoothAdapter.enable();
                            justcancelled = false;
                            start2();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    break;
                }


            }
            //Log.d(TAG, "hey");
            //start();

        }

        public void respondPoL2 (String message){
            Log.d(TAG, "Responding to PoL request...");
            String coordinates = message.replace("POL request: ", "");

            // GET coordinates X, Y
            Log.d(TAG, "coordinates: " + coordinates + ", currentLocation: " + currentLocation);
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(coordinates);
            Matcher m2 = p.matcher(currentLocation);
            int[] requesterXY = new int[4];
            int[] myXY = new int[4];
            int i = 0;

            while(m.find()) {
                String temp = m.group();
                if (temp.length() > 4) temp = temp.substring(0, 4);
                requesterXY[i] = Integer.parseInt(temp);
                Log.d(TAG, requesterXY[i++] + "\n");
            }

            i = 0;
            while(m2.find()) {
                String temp = m2.group();
                if (temp.length() > 4) temp = temp.substring(0, 4);
                myXY[i] = Integer.parseInt(temp);
                Log.d(TAG, myXY[i++] + "\n");
            }

            String resPOL = "POL response: " + myXY[0] + "." + myXY[1] + ", " + myXY[2] + "." + myXY[3];

            byte [] bytes2 = resPOL.getBytes(Charset.defaultCharset());
            write(bytes2);


        }

        public void respondPoL (String message){
            // Verify
            Log.d(TAG, "Responding to PoL request...");
            String encryptedMessage = message.replace("PoL Request: ", "");
            Log.d(TAG, "Encrypted Message: " + encryptedMessage);
            String coordinates = decryptRSAToString(encryptedMessage, privateKey);
            Log.d(TAG, "Decrypted Message: " + coordinates);
            String resPoL = coordsToString( coordinates );


            // Check if coords are cool
            // Send to contract

            byte [] bytes2 = resPoL.getBytes(Charset.defaultCharset());
            write(bytes2);


        }

        public String coordsToString(String coordinates) {
             // GET coordinates X, Y
            Log.d(TAG, "coordinates: " + coordinates + ", currentLocation: " + currentLocation);
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(coordinates);
            Matcher m2 = p.matcher(currentLocation);
            int[] requesterXY = new int[4];
            int[] myXY = new int[4];
            int i = 0;

            while(m.find()) {
                String temp = m.group();
                if (temp.length() > 4) temp = temp.substring(0, 4);
                requesterXY[i] = Integer.parseInt(temp);
                Log.d(TAG, requesterXY[i++] + "\n");
            }

            i = 0;
            while(m2.find()) {
                String temp = m2.group();
                if (temp.length() > 4) temp = temp.substring(0, 4);
                myXY[i] = Integer.parseInt(temp);
                Log.d(TAG, myXY[i++] + "\n");
            }

            String resPOL = "POL response: " + myXY[0] + "." + myXY[1] + ", " + myXY[2] + "." + myXY[3];
            return resPOL;
        }

        public void respondPublicKey (String message){
            Log.d(TAG, "Responding to Public Key request...");
            proverPublicKey = message.replace("My Public Key is: ", "");

            // CHECK IF PUBLIC KEY IS LEGIT
            //Sign.SignatureData signature = Sign.signMessage(pro)
            currentKeyPair = getKeyPair();
            PublicKey publicKey = currentKeyPair.getPublic();
            PrivateKey privateKeyTemp = currentKeyPair.getPrivate();
            byte[] privateKeyBytes = privateKeyTemp.getEncoded();
            String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));
            privateKey = privateKeyBytesBase64;

            byte[] publicKeyBytes = publicKey.getEncoded();
            String publicKeyBytesBase64 = new String( Base64.encode(publicKeyBytes, Base64.DEFAULT));
            String response = "" + publicKeyBytesBase64;

            byte [] bytes2 = response.getBytes(Charset.defaultCharset());
            write(bytes2);


        }

        public KeyPair getKeyPair() {
            KeyPair kp = null;
            try {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                kp = kpg.generateKeyPair();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return kp;
        }

        public String encryptRSAToString(String clearText, String publicKey) {
            String encryptedBase64 = "";
            try {
                KeyFactory keyFac = KeyFactory.getInstance("RSA");
                KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
                Key key = keyFac.generatePublic(keySpec);

                // get an RSA cipher object and print the provider
                final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
                // encrypt the plain text using the public key
                cipher.init(Cipher.ENCRYPT_MODE, key);

                byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));
                encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return encryptedBase64.replaceAll("(\\r|\\n)", "");
        }

        public String decryptRSAToString(String encryptedBase64, String privateKey) {

            String decryptedString = "";
            try {
                KeyFactory keyFac = KeyFactory.getInstance("RSA");
                KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
                Key key = keyFac.generatePrivate(keySpec);

                // get an RSA cipher object and print the provider
                final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
                // encrypt the plain text using the public key
                cipher.init(Cipher.DECRYPT_MODE, key);

                byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                decryptedString = new String(decryptedBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return decryptedString;
        }


        /**
         * Call this from the MainActivity to send data to the remote device.
         **/
        public void write (byte [] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to outputstream: " + e.getMessage());
            }
        }


        /**
         * Call this from the MainActivity to cancel the connection.
         **/
        public void cancel () {
            try {
                justcancelled = true;
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmInStream.close();
                mmOutStream.close();
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in ConnectedThread failed. " + e.getMessage());
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronised manner
     *
     * @param out The Bytes to write
     * @see ConnectedThread#write(byte[])
     **/
    public void write(byte [] out) {
        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        // Perform the write
        mConnectedThread.write(out);

    }



    public String read () {
        if (currentMessage != null) return currentMessage;
        else return null;
    }



}
