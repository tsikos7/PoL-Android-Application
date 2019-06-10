package com.example.projectodyssey;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import java.io.IOException;
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

import com.kenai.jffi.Main;

import java.util.List;
import java.util.Set;
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
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    // BLOCKCHAIN CONTRACT STUFF
    String contractAddress = "0x2a68c1D388dd59644957970E8Fb6d25d6751fCA4";
    String url = "https://rinkeby.infura.io/v3/671362fca54b42b0a7c7f3c3126dc47b";
    Web3j web3j = Web3j.build(new InfuraHttpService(url));

    BigInteger gasLimit = BigInteger.valueOf(6700000L);
    BigInteger gasPrice = BigInteger.valueOf(22_000_000_000L);

    String[] myPrivateKeys;
    String myPrivateKey = "EF41EE70F8F4724A5CFAC21E64E572E5BDD25102B96325B4F12009D52167121B";
    Credentials credentials = Credentials.create("EF41EE70F8F4724A5CFAC21E64E572E5BDD25102B96325B4F12009D52167121B");
    String myPublicKey = "0x3E877f2f819E1feec88336c4d75966637e21C6cC";

    FastRawTransactionManager fastRawTxMgr = new FastRawTransactionManager(web3j, credentials);
    Transaction transaction;


    ProgressDialog mProgressDialog;

    // BLUETOOTH STUFF
    private TextView t;
    EditText textAmountToSend;
    Spinner mySpinner, spinner;
    String destination;
    String witnessKey;
    boolean colors[] = new boolean[100];


    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnectionService [] mBluetoothConnection = new BluetoothConnectionService[8];
    BluetoothDevice mBTDevice;


    private String currentLocation = "Unavailable";
    private LocationManager locationManager;
    private LocationListener listener;


    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public ArrayList<BluetoothDevice> mBondedDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        //unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
        mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        // All the buttons and the visuals for the .xml layout

        textAmountToSend = (EditText) findViewById(R.id.textAmount);

        mySpinner = (Spinner) findViewById(R.id.myaddresses);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.myaddresses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.destAddresses);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.destaddresses, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        t = (TextView) findViewById(R.id.textView);

        mBTDevices = new ArrayList<>();

        lvNewDevices.setOnItemClickListener(MainPage.this);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view, int i, long l) {
                // When dropdown is pressed change the address settings and redeploy contract with right keys

                myPrivateKeys = MainPage.this.getResources().getStringArray(R.array.privateKeys);
                myPublicKey = mySpinner.getSelectedItem().toString();
                myPrivateKey = myPrivateKeys[mySpinner.getSelectedItemPosition()];
                credentials = Credentials.create(myPrivateKey);
                fastRawTxMgr = new FastRawTransactionManager(web3j, credentials);
                transaction = Transaction.load(contractAddress, web3j, fastRawTxMgr, gasPrice, gasLimit);

                mBluetoothConnection[0].credentials = Credentials.create(myPrivateKey);
                mBluetoothConnection[0].fastRawTxMgr = new FastRawTransactionManager(web3j, credentials);
                mBluetoothConnection[0].transaction = Transaction.load(contractAddress, web3j, fastRawTxMgr, gasPrice, gasLimit);
                Log.d(TAG, myPublicKey);
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });

        // Initialize various settings
        myPrivateKeys = MainPage.this.getResources().getStringArray(R.array.privateKeys);
        myPublicKey = mySpinner.getSelectedItem().toString();
        myPrivateKey = myPrivateKeys[mySpinner.getSelectedItemPosition()];
        destination = spinner.getSelectedItem().toString();

        // Initialize credentials and deploy the smart contract
        credentials = Credentials.create(myPrivateKey);
        fastRawTxMgr = new FastRawTransactionManager(web3j, credentials);
        transaction = Transaction.load(contractAddress, web3j, fastRawTxMgr, gasPrice, gasLimit);

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get bonded devices into array mBondedDevices
        // From mBondedDevices we will show the eligible for communication devices
        // to the user to see and pick
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mBondedDevices.add(device);
            }
        }

        // LocationManager and LocationListener to listen and register any changes to the location
        // and update location to the currentLocation string
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                // Get Latitude and Longitude
                double temp1 = location.getLatitude();
                double temp2 = location.getLongitude();
                DecimalFormat df = new DecimalFormat("#.####");

                df.setRoundingMode( RoundingMode.FLOOR);

                double lat = new Double(df.format(temp1));
                double lon = new Double(df.format(temp2));

                // assign to currentLocation
                currentLocation = lon + ", " + lat;

                // inform BluetoothConnectionService of the new connection
                if (mBluetoothConnection[0] != null) mBluetoothConnection[0].currentLocation = currentLocation;
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

        // Enable location provider so that the app has access to the phone's location
        // Enable Bluetooth when opening the app
        // Enable Discoverability as well (Depending on the android OS infinite discoverability will not be allowed)
        activateLocationProvider();
        enableBT();
        btnEnableDisable_Discoverable();

        // Start discovering other devices
        mBluetoothConnection[0] = new BluetoothConnectionService(MainPage.this);
        btnDiscover(null);
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

    /**
     * Function to activate location provider. After calling the device will have access to the devices location.
     * locationManager will request location updates from the GPS_PROVIDER of the phone when availlable.
     * In cases where it is not availlable, such as indoors, the app will request updates from its
     * NETWORK_PROVIDER.
     */
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
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

    /**
     * Broadcast Receiver for changes made to bluetooth states such as
     * turning BT on and off
     */
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
     * Broadcast Receiver for changes made to bluetooth states such as
     * turning Discoverability mode on/off or expire.
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
     * Broadcast Receiver for listing devices that are paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null) {
                    for (BluetoothDevice dev : mBondedDevices) {
                        if (dev.getAddress().equals(device.getAddress())) mBTDevices.add( device );
                    }
                }
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
     * Starts BT connection with another device
     * Remember the connection will fail and the app will crash if you have not paired yet
     */
    public void startConnection () {
        Log.d(TAG,"startBTConnection: Initializing RFCOM Bluetooth Connection.");
        mBluetoothConnection[0].startClient(mBTDevice, MY_UUID_INSECURE);
    }


    /**
     * Enables Bluetooth if not already enabled
     */
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

    /**
     * Enables Discoverability of device if already disabled, or disables
     * Discoverability of device if already enabled.
     */
    public void btnEnableDisable_Discoverable() {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for unlimited seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter( BluetoothAdapter.ACTION_SCAN_MODE_CHANGED );
        registerReceiver(mBroadcastReceiver2,intentFilter);

    }

    /**
     * Starts discovering for disoverable devices with Bluetooth enabled
     * @param view
     */
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

    /**
     * When a device of the ListView is clicked, change the color for the User to see
     * and save which device was clicked (with colors array)
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        if (colors[i]) {
            view.setBackgroundColor(Color.WHITE);
            colors[i] = false;
        }
        else {
            view.setBackgroundColor(Color.GREEN);
            colors[i] = true;
        }
    }

    // CHECK THESE
    @SuppressLint("HandlerLeak")
    Handler stopProgressDialogTx = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler startProgressDialogTx = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog = ProgressDialog.show(MainPage.this, "Applying for Transaction", "Please wait...", true);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler stopProgressDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler startProgressDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog = ProgressDialog.show(MainPage.this, "Connecting Bluetooth", "Please wait...", true);
        }
    };

    /**
     * Method that connects to the already Paired BT device that the User has clicked
     * @param i: the item position of the device that was picked from the User
     */
    public void connectBondedDeviceBluetooth (int i) {
        // first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();

        // If already connected with a device, cancel connection.
        if (mBluetoothConnection[0] != null)
            mBluetoothConnection[0].cancel();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBondedDevices.get(i).getName();
        String deviceAddress = mBondedDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        // create the bond.
        // NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection[0] = new BluetoothConnectionService(MainPage.this);
            startConnection();
        }
    }

    /**
     * Sends public Key to the connected device. This is the address to the Ethereum blockchain
     * It will be used to digitally sign the device's location later
     * Connected device will respond with freshly generated RSA public Key
     * This key will be used to hide the location from malicious MITM users that don't have the private key
     * @param view
     */
    public void sendRequestPublicKey (View view) {
        Log.d(TAG, "Sending and requesting Public Key...");
        // If not connected to a BT device stop
        if (mBTDevice == null) return;

        // If device is compatible (only if connection has been successful) send key
        Log.d(TAG, "Compatibility: " + mBluetoothConnection[0].isIncompatibleDevice + " for Device: " + mBTDevice.getName());
        if (!mBluetoothConnection[0].isIncompatibleDevice) {
            // Create string with public key and its identifier "My Public Key is: "
            // When another device receives a message that starts with this identifier
            // It will know that a public key is needed
            String reqPOL = "My Public Key is: " + myPublicKey;
            byte[] bytes = reqPOL.getBytes(Charset.defaultCharset());
            mBluetoothConnection[0].write(bytes);

            // CHECK HERE
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


                }
            }).start();
        } else {
            Log.d(TAG, "Device " + mBTDevice + " is incompatible... Can't write here!");
        }
    }

    /**
     * Sends location to the connected device, after encrypting it with the RSA key that was previously
     * received, and after signing it with the private key corresponding to its public key (Ethereum Address)
     * @param view
     */
    public void sendLocation (View view) {
        // If not connected to a BT device stop
        if (mBTDevice == null) return;


        // If device is compatible (only if connection has been successful) send location
        Log.d(TAG, "Compatibility: " + mBluetoothConnection[0].isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection[0].isIncompatibleDevice) {
            if (!currentLocation.equals("Unavailable")) {
                String locationmessage = currentLocation;

                Thread tSendLocation = new Thread( new Runnable() {
                    @Override
                    public void run() {
                        // encrypts the message with the key received previously from the witness
                        Log.d(TAG, "Encrypting Message with key: " + witnessKey);
                        String encrypted = "PoL Request: " + encryptRSAToString(locationmessage, witnessKey);
                        Log.d(TAG, "Finished encrypting...");

                        // Sign the message with the privateKey of the Device
                        Log.d(TAG, "Signing Message...");
                        byte[] bytes = encrypted.getBytes(Charset.defaultCharset());
                        String prefix = PERSONAL_MESSAGE_PREFIX + encrypted.length();
                        byte[] msgHash = Hash.sha3((prefix + encrypted).getBytes());

                        // Create the signature data with the message (bytes),
                        // the private and public key (credentials),
                        // and with sha3 hashing enabled
                        Sign.SignatureData signature = Sign.signMessage(bytes, credentials.getEcKeyPair(), true);
                        byte [] v = new byte[] {signature.getV()};
                        byte [] r = signature.getR();
                        byte [] s = signature.getS();

                        byte[] bytes2 = new byte[r.length + s.length + v.length + encrypted.getBytes().length];

                        System.arraycopy(encrypted.getBytes(), 0, bytes2, 0, encrypted.getBytes().length);
                        System.arraycopy(r, 0, bytes2, encrypted.getBytes().length, r.length);
                        System.arraycopy(s, 0, bytes2, encrypted.getBytes().length + r.length, s.length);
                        System.arraycopy(v, 0, bytes2, encrypted.getBytes().length + r.length + s.length, v.length);

                        Log.d(TAG, "Finished signing...");

                        // CHECK HERE
                        String pubKey = null;
                        try {
                            pubKey = Sign.signedMessageToKey(bytes, signature).toString(16);
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        }
                        String signerAddress = "0x" + Keys.getAddress(pubKey);
                        Log.d(TAG, "My Publickey: " + myPublicKey + "\nPublickey: " + signerAddress);


                        mBluetoothConnection[0].write(bytes2);

                        // CHECK HERE
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String temp = "" + encrypted + "\n" + getCurrentMessage();

                        Log.d(TAG, temp);

                    }
                });
                tSendLocation.start();

                try {
                    tSendLocation.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d(TAG, "Device " + mBTDevice + " is incompatible... Can't write here!");
        }
    }

    /**
     * Get the last received message from the BluetoothConnectionService
     * @return
     */
    public String getCurrentMessage() {
        return mBluetoothConnection[0].currentMessage;
    }

    /**
     * Method to encrypt the text with the publicKey received from the Bluetooth Witness device
     * It uses RSA encryption, making it impossible for other users to decrypt
     */
     static String encryptRSAToString(String clearText, String publicKey) {
        Log.d(TAG, "ClearText: " + clearText);
        Log.d(TAG, "publicKey: " + publicKey);
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

    /**
     * Method to initiate the transaction procedure. Makes call to the Ethereum smart contract
     * and sends the funds issued by the User to the contract,
     * with all the necessary information (destination, amount, number of witnesses' proofs, etc
     * @param numOfProofs
     */
    public void transferToContract(int numOfProofs) {
        myPublicKey = mySpinner.getSelectedItem().toString();
        myPrivateKey = myPrivateKeys[mySpinner.getSelectedItemPosition()];
        destination = spinner.getSelectedItem().toString();

        if (destination.equals("Select Destination")) return;
        Log.d(TAG, "Application Transaction for Address: " + destination);
        Log.d(TAG, "Amount: " + BigInteger.valueOf(Integer.parseInt(textAmountToSend.getText().toString())) );
        Log.d(TAG, "Number of Proofs: " + numOfProofs);
        Thread t = new Thread( new Runnable() {
            String destination = spinner.getSelectedItem().toString();

            BigInteger amount = BigInteger.valueOf(Integer.parseInt(textAmountToSend.getText().toString()));
            @Override
            public void run() {
                BigInteger etherUnit = new BigInteger( "1000000000000000" );
                BigInteger amountEther = amount.multiply(etherUnit);

                BigInteger numProofs = new BigInteger( String.valueOf( numOfProofs ) );
                TransactionReceipt transactionReceipt  = null;
                try {
                    transactionReceipt = transaction.transferToContract(destination, amount, numProofs, amountEther).send();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (transactionReceipt != null) {
                    Log.d(TAG, "Amount " + amount + " transferred to contract successfully");
                    Log.d(TAG, "Gas used: " + transactionReceipt.getGasUsed());
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that is called by pressing the Apply For Transaction button
     * Uses threads to count the witnesses, make the transaction and show UI loading Dialog appropriately
     * without interfering with the UI negatively
     * @param view
     */
    public void applyTransaction(View view) {
        Thread tProcedure = new Thread( new Runnable() {
            @Override
            public void run() {
                int i = 0;
                int numOfProofs = 0;
                for (boolean isClicked : colors){
                    if (isClicked) numOfProofs++;
                    i++;
                }

                int finalNumOfProofs = numOfProofs;

                Thread tMakeTransaction = new Thread( new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Making transaction...");
                        startProgressDialogTx.sendEmptyMessage(0);
                        transferToContract(finalNumOfProofs);
                        stopProgressDialogTx.sendEmptyMessage(0);
                    }
                });
                if (finalNumOfProofs > 0) {
                    fastRawTxMgr.setNonce( BigInteger.valueOf( -1 ) );

                    tMakeTransaction.start();
                    try {
                        tMakeTransaction.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } );
        tProcedure.start();
    }

    /**
     * Method to connect with the Witnesses' devices, exchange keys, and send location of the Device
     * for them to prove its location
     * Uses threads to show UI loading Dialog appropriately without interfering with the UI negatively
     * @param view
     */
    public void confirmPoL(View view) {
        Thread tProcedure = new Thread( new Runnable() {
            @Override
            public void run() {
                int i = 0;


                for (boolean isClicked : colors){
                    int finalI = i;
                    Thread forEachDevice = new Thread( new Runnable() {
                        @Override
                        public void run() {
                            if (isClicked) {
                                Log.e(TAG, "[Started]Connecting to device: " + mBondedDevices.get( finalI ).getName());
                                connectBondedDeviceBluetooth( finalI );


                                Thread tExchangeKeys = new Thread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, "[SOS]Exchanging Keys...");
                                        startProgressDialog.sendEmptyMessage(0);
                                        sendRequestPublicKey(null);
                                        stopProgressDialog.sendEmptyMessage(0);
                                        Log.e(TAG, "[SOS]Finished Exchanging Keys...");
                                    }
                                });

                                tExchangeKeys.start();
                                try {
                                    tExchangeKeys.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }



                                Thread tSendLocation = new Thread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, "[SOS]Sending Location...");
                                        startProgressDialog.sendEmptyMessage(0);
                                        boolean sentinel = true;
                                        String cur = getCurrentMessage();
                                        Log.d(TAG, cur);
                                        while (sentinel) {
                                            try {
                                                Thread.sleep(3000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            if (!cur.equals(getCurrentMessage())) sentinel = false;
                                        }
                                        witnessKey = getCurrentMessage();
                                        Log.d(TAG, witnessKey);
                                        sendLocation(null);
                                        stopProgressDialog.sendEmptyMessage(0);
                                        Log.e(TAG, "[SOS]Finished Sending Location...");
                                    }
                                });

                                tSendLocation.start();
                                try {
                                    tSendLocation.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Log.e(TAG, "[Finished]Device " + mBondedDevices.get( finalI ).getName() + "just finished");

                            }
                        }
                    } );
                    forEachDevice.start();
                    try {
                        forEachDevice.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }


            }
        } );
        tProcedure.start();

    }
}
