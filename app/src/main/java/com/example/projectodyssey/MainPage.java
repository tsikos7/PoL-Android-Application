package com.example.projectodyssey;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Base64;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.bluetooth.BluetoothAdapter.STATE_OFF;
import static android.bluetooth.BluetoothAdapter.STATE_ON;

public class MainPage extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainPage";
    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
    // BLOCKCHAIN CONTRACT STUFF
    String contractAddress = "0xa4B84814dC87C487e583cfd6d20390f263d4787A";
    String greeterAddress = "0x504f2bD1E6E75809E98Ab887fE73bC98dF3bfBD5";
    String url = "https://rinkeby.infura.io/v3/671362fca54b42b0a7c7f3c3126dc47b";
    Web3j web3j = Web3j.build(new InfuraHttpService(url));

    BigInteger gasLimit = BigInteger.valueOf(6700000L);
    BigInteger gasPrice = BigInteger.valueOf(22_000_000_000L);

    Credentials credentials = Credentials.create("629054BB24F430E96C6BFFC58F186371695BC3BFC695E76CEF54DAFCA460BC0C");
    String myPublicKey = "0x015Bbab8756B37d8D14e7ff7f915650b37161f39";

    private static SecretKeySpec secretKey;
    private static byte[] key;

    Future<TransactionReceipt> transactionReceipt;

    TransactionManager fastRawTxMgr =new FastRawTransactionManager(web3j, credentials);
    Greeter greeter;
    Transaction transaction;


    // BLUETOOTH STUFF
    Button btnDebug;
    Button btnRequestPoL;
    Button btnONOFF;
    Button btnStartConnection;
    private TextView t;
    Button btnSendEther;
    EditText etSend;
    EditText textAmountToSend;
    Spinner spinner;
    String destination;
    String witnessKey;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnectionService mBluetoothConnection;
    BluetoothDevice mBTDevice;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private String currentLocation = "Unavailable";

    private LocationManager locationManager;
    private LocationListener listener;


    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    ListView lvNewDevices;

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // BLOCKCHAIN CONTRACT STUFF
        transaction = Transaction.load(contractAddress, web3j, fastRawTxMgr, gasPrice, gasLimit);


        btnSendEther = (Button) findViewById(R.id.sendEther);
        textAmountToSend = (EditText) findViewById(R.id.textAmount);

        spinner = (Spinner) findViewById(R.id.destAddresses);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.addresses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // BLUETOOTH STUFF

        //btnDebug = (Button) findViewById(R.id.Debugging);
//        btnDebug.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenDebugging(null);
//
//            }
//
//        });
        btnRequestPoL = (Button) findViewById(R.id.RequestPoL);
        btnONOFF = (Button) findViewById(R.id.btnONOFF);
        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);
        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        t = (TextView) findViewById(R.id.textView);

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        mBTDevices = new ArrayList<>();

        lvNewDevices.setOnItemClickListener(MainPage.this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                //Log.d(TAG, "RequestLocation: Location Changed... " + locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

                //t.setText("\n " + location.getLongitude() + " " + location.getLatitude());
                //Log.d(TAG, "THIS: " + currentLocation + " - " + mBluetoothConnection.currentLocation);
                double temp1 = location.getLatitude();
                double temp2 = location.getLongitude();
                DecimalFormat df = new DecimalFormat("#.####");

                df.setRoundingMode( RoundingMode.FLOOR);

                double lat = new Double(df.format(temp1));
                double lon = new Double(df.format(temp2));


                currentLocation = lon + ", " + lat;

                if (mBluetoothConnection != null)mBluetoothConnection.currentLocation = currentLocation;


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //Log.d(TAG, "RequestLocation: Status Changed... ");
            }

            @Override
            public void onProviderEnabled(String s) {
                //Log.d(TAG, "RequestLocation: Provider is Enabled... ");
            }

            @Override
            public void onProviderDisabled(String s) {
                //Log.d(TAG, "RequestLocation: Provider is disabled... ");
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
                Log.d(TAG, "RequestLocation: Provider has been enabled... ");
            }
        };

        activateLocationProvider();
        String destination = spinner.getSelectedItem().toString();
        Log.d( TAG, "AAAAAA" + destination );
        enableBT();
        btnEnableDisable_Discoverable();

        mBluetoothConnection = new BluetoothConnectionService(MainPage.this);
        btnDiscover(null);

        //changeGreeting();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                activateLocationProvider();
                break;
            default:
                break;
        }
    }

    void activateLocationProvider() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        //noinspection MissingPermission
        Log.d(TAG, "RequestLocation: Requesting Location Updates... ");
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }


    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state){
                    case STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };




    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null) mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    mBTDevice = mDevice;
                }
                //case2: creating a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };

    /**
     * Create Method for starting connection
     * Remember the connection fail and the app will crash if you have not paired yet
     */
    public void startConnection () {
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     **/
    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG,"startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device, uuid);

    }


    public void enableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableBT: BT is already enabled.");
        }
    }


    public void btnEnableDisable_Discoverable() {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);

    }

    public void btnDiscover(View view) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){
            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();

        if (mBluetoothConnection != null)
            mBluetoothConnection.cancel();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection = new BluetoothConnectionService(MainPage.this);
            startConnection();
        }
    }

    public void connectListEach () {
        mBluetoothAdapter.cancelDiscovery();
        int numOfPaired = 0;


        for (BluetoothDevice device : mBTDevices) {
            Log.e(TAG, "Device #" + numOfPaired++);
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();

            Log.d(TAG, "connectListEach: deviceName = " + deviceName);
            Log.d(TAG, "connectListEach: deviceAddress = " + deviceAddress);

            if (deviceName == null) continue;
            //if (!deviceName.equals("Black MLS") && !deviceName.equals("[TV] UE65JS9000")) continue;

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                Log.d(TAG, "Trying to pair with " + deviceName);
                device.createBond();

                mBTDevice = device;
                mBluetoothConnection = new BluetoothConnectionService(MainPage.this);

            }
            Log.d(TAG, "STARTING startConnection...");
            startConnection();
            Log.d(TAG, "ENDING startConnection...");

            while (true) {
                if (mBluetoothConnection.unlock) break;
            }


            Log.d(TAG, "STARTING requestPoL...");
            requestPoL(null);
            Log.d(TAG, "ENDING requestPoL...");

            if (mBluetoothConnection != null)
                mBluetoothConnection.cancel();


        }
    }



    public void requestPoL(View view) {
        Log.d(TAG, "Requesting Proof-of-Location...");
        t.clearComposingText();
        if (mBTDevice == null) return;
        Log.e(TAG, "Compatibility: " + mBluetoothConnection.isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection.isIncompatibleDevice) {
            if (!currentLocation.equals("Unavailable")) {
                String reqPOL = "POL request: " + currentLocation;
                byte[] bytes = reqPOL.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);

                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String temp = "" + reqPOL + "\n" + getCurrentMessage();

                        Log.d(TAG, temp);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                t.setText(temp);

                            }
                        });

                    }
                }).start();
            }
            else ;//resultDisplay.setText("Failed! Location unavailable...");
        } else {
            Log.d(TAG, "Device "

                    + mBTDevice + " is incompatible... Can't write here!");
        }
    }


    public void sendRequestPublicKey (View view) {
        Log.d(TAG, "Sending and requesting Public Key...");
        //t.clearComposingText();
        if (mBTDevice == null) return;
        Log.e(TAG, "Compatibility: " + mBluetoothConnection.isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection.isIncompatibleDevice) {
            if (true) {
                String reqPOL = "My Public Key is: " + myPublicKey;
                byte[] bytes = reqPOL.getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);

                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        witnessKey = getCurrentMessage();
                        System.out.println(witnessKey);
                        String temp = "" + reqPOL + "\nHis Public key is: " + witnessKey.substring(0, 10);

                        Log.d(TAG, temp);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                t.setText(temp);

                            }
                        });
                    }
                }).start();
                //resultDisplay.setText("Success!");
            }
            else ;//resultDisplay.setText("Failed! Location unavailable...");
        } else {
            Log.d(TAG, "Device "

                    + mBTDevice + " is incompatible... Can't write here!");
        }
    }









    public void sendLocation (View view) {

        //t.clearComposingText();
        if (mBTDevice == null) return;
        Log.e(TAG, "Compatibility: " + mBluetoothConnection.isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection.isIncompatibleDevice) {
            if (!currentLocation.equals("Unavailable")) {
                String locationmessage = currentLocation;

                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Encrypting Message...");
                        String encrypted = "PoL Request: " + encryptRSAToString(locationmessage, witnessKey);
                        Log.d(TAG, "Finished encrypting...");

                        Log.d(TAG, "Encrypted data: " + encrypted);

                        Log.d(TAG, "Signing Message...");
                        byte[] bytes = encrypted.getBytes(Charset.defaultCharset());
                        String prefix = PERSONAL_MESSAGE_PREFIX + encrypted.length();
                        byte[] msgHash = Hash.sha3((prefix + encrypted).getBytes());

                        Sign.SignatureData signature = Sign.signMessage(bytes, credentials.getEcKeyPair(), true);
                        String v = ":" + new String( new byte[] {signature.getV()});
                        String r = ":" + new String(signature.getR());
                        String s = ":" + new String(signature.getS());
                        bytes = (encrypted + r + s + v).getBytes(Charset.defaultCharset());



                        Log.d(TAG, "Encrypted data R: " + "0x" + Keys.getAddress(new BigInteger(1, signature.getR())));
                        Log.d(TAG, "Encrypted data S: " + "0x" + Keys.getAddress(new BigInteger(1, signature.getS())));
                        Log.d(TAG, "Encrypted data V: " + "0x" + Keys.getAddress(BigInteger.valueOf(signature.getV())));
                        Log.d(TAG, "Finished signing...");

                        String pubKey = null;
                        try {
                            pubKey = Sign.signedMessageToKey(bytes, signature).toString(16);
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        }
                        String signerAddress = "0x" + Keys.getAddress(pubKey);

                        Log.e(TAG, "My Publickey: " + myPublicKey + "\nPublickey: " + signerAddress);

                        mBluetoothConnection.write(bytes);


                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String temp = "" + encrypted + "\n" + getCurrentMessage();

                        Log.d(TAG, temp);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                t.setText(temp);

                            }
                        });
                    }
                }).start();
                //resultDisplay.setText("Success!");
            }
            else ;//resultDisplay.setText("Failed! Location unavailable...");
        } else {
            Log.d(TAG, "Device "

                    + mBTDevice + " is incompatible... Can't write here!");
        }
    }


    public void OpenDebugging(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void RequestPoL(View view) {


        Log.d(TAG, "STARTING connectListEach...");
        connectListEach();
        Log.d(TAG, "ENDING connectListEach...");
    }

    public String getCurrentMessage() {
        return mBluetoothConnection.currentMessage;
    }

    public void dothis(View view) {
        //CHECKSTYLE:OFF
        String signature = "0x2c6401216c9031b9a6fb8cbfccab4fcec6c951cdf40e2320108d1856eb532250576865fbcd452bcdc4c57321b619ed7a9cfd38bd973c3e1e0243ac2777fe9d5b1b";
//        String signature = "0x0993fdb1eee17965ce2ad068292f0d99280a0539686e47f7ce3358c6a6c52f8b8e3914f43a25c446";
//        0x3bc843a917d6c19c487c1d0c660cdd61389ce2a7651ee3171bcc212ffddca164
//        0x0993fdb1eee17965ce2ad068292f0d99280a0539
        //CHECKSTYLE:ON
        String address = "0x31b26e43651e9371c88af3d36c14cfd938baf4fd";
        String message = "v0G9u7huK4mJb2K1";

        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3( (prefix + message).getBytes() );

        byte[] signatureBytes = Numeric.hexStringToByteArray( signature );
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd = new Sign.SignatureData(
                v,
                (byte[]) Arrays.copyOfRange( signatureBytes, 0, 32 ),
                (byte[]) Arrays.copyOfRange( signatureBytes, 32, 64 ) );

        String addressRecovered = null;
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature( new BigInteger( 1, sd.getR() ), new BigInteger( 1, sd.getS() ) ),
                    msgHash );

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress( publicKey );

                if (addressRecovered.equals( address )) {
                    Log.e(TAG, "My Publickey: " + address + "\nPublickey: " + addressRecovered);
                    match = true;
                    break;
                }
            }
        }
    }

    public String encryptMessage (String plaintext) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {

        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Plaintext message: " + plaintext);

                // generate a new public/private key pair to test with (note. you should only do this once and keep them!)
                KeyPair kp = getKeyPair();
                Log.d(TAG, "HERE");
                PublicKey publicKey = kp.getPublic();
                byte[] publicKeyBytes = publicKey.getEncoded();
                String publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));

                PrivateKey privateKey = kp.getPrivate();
                byte[] privateKeyBytes = privateKey.getEncoded();
                String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));

                // test encryption
                String encrypted = encryptRSAToString(plaintext, publicKeyBytesBase64);
                String encrypted2 = encryptRSAToString(plaintext, publicKeyBytesBase64);
                Log.d(TAG, "Encrypted message 1: " + encrypted);
                Log.d(TAG, "Encrypted message 2: " + encrypted2);

                // test decryption
                String decrypted = decryptRSAToString(encrypted, privateKeyBytesBase64);
                String decrypted2 = decryptRSAToString(encrypted2, privateKeyBytesBase64);
                Log.d(TAG, "Decrypted message 1: " + decrypted);
                Log.d(TAG, "Decrypted message 2: " + decrypted2);

            }
        }).start();



        return "";
    }


    public static KeyPair getKeyPair() {
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

    public static String encryptRSAToString(String clearText, String publicKey) {
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

    public static String decryptRSAToString(String encryptedBase64, String privateKey) {

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



    public void transferToContract(View view) {
        if (destination.equals("Select Destination")) return;
        Log.d( TAG, "Amount: " + BigInteger.valueOf(Integer.parseInt(textAmountToSend.getText().toString())) );
        new Thread( new Runnable() {
            String destination = spinner.getSelectedItem().toString();

            BigInteger amount = BigInteger.valueOf(Integer.parseInt(textAmountToSend.getText().toString()));
            @Override
            public void run() {
                BigInteger etherUnit = new BigInteger( "1000000000000000" );
                BigInteger amountEther = amount.multiply(etherUnit);
                Future<TransactionReceipt> isTransferred = transaction.transferToContract(destination, amount, amountEther).sendAsync();
                TransactionReceipt transactionReceipt  = null;
                try {
                    transactionReceipt = isTransferred.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (transactionReceipt != null) {
                    Log.d(TAG, "Amount " + amount + " transferred to contract successfully");
                    Log.d(TAG, "Gas used: " + transactionReceipt.getGasUsed());
                }

            }
        }).start();
    }

    public void confirmTransaction(View view) {
        new Thread( new Runnable() {

            @Override
            public void run() {
                Future<TransactionReceipt> isTransferred = transaction.confirmTransaction().sendAsync();
                TransactionReceipt transactionReceipt  = null;
                try {
                    transactionReceipt = isTransferred.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (transactionReceipt != null) {
                    Log.d(TAG, "PoL approved... Transaction confirmed successfully");
                    Log.d(TAG, "Gas used: " + transactionReceipt.getGasUsed());
                }

            }
        }).start();
    }

}
