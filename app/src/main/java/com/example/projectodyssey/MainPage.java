package com.example.projectodyssey;

import android.Manifest;
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
    // BLOCKCHAIN CONTRACT STUFF
    String contractAddress = "0x47B710157f13d499dC15aaC9acC9eaf1e93b8885";
//    String contractAddress = "0x4C74B73c928E13016609d1b6788Ad7eb174ab6A2";
//    String contractAddress = "0x3C0aD1e7B0a24174241b72EFdd415e13A8177402";
    String url = "https://rinkeby.infura.io/v3/671362fca54b42b0a7c7f3c3126dc47b";

    Web3j web3j = Web3j.build(new InfuraHttpService(url));

    BigInteger gasLimit = BigInteger.valueOf(6700000L);
    BigInteger gasPrice = BigInteger.valueOf(22_000_000_000L);

    String[] myPrivateKeys;
    String myPrivateKey = "629054BB24F430E96C6BFFC58F186371695BC3BFC695E76CEF54DAFCA460BC0C";
    Credentials credentials = Credentials.create("629054BB24F430E96C6BFFC58F186371695BC3BFC695E76CEF54DAFCA460BC0C");
//    Credentials credentials = Credentials.create("8A6A1F416B7A6756BC89021AED2239F6F1EC3B165E81317382E256BA199A2F5D");
//    Credentials credentials = Credentials.create("368512A07C38EDA260BA631315D92EB93D271477607D5C4677D345054F85506E");
    String myPublicKey = "0x015Bbab8756B37d8D14e7ff7f915650b37161f39";

    FastRawTransactionManager fastRawTxMgr = new FastRawTransactionManager(web3j, credentials);
    Transaction transaction;
    ProgressDialog mProgressDialog;

    // BLUETOOTH STUFF
    Button btnRequestPoL;
    Button btnONOFF;
    Button btnStartConnection;
    private TextView t;
    EditText textAmountToSend;
    Spinner mySpinner, spinner;
    String destination;
    String witnessKey;
    boolean colors[] = new boolean[100];


    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnectionService [] mBluetoothConnection = new BluetoothConnectionService[8];
    BluetoothDevice mBTDevice;
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

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

        // BLOCKCHAIN CONTRACT STUFF




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

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view, int i, long l) {
                myPrivateKeys = MainPage.this.getResources().getStringArray(R.array.privateKeys);

                myPublicKey = mySpinner.getSelectedItem().toString();
                myPrivateKey = myPrivateKeys[mySpinner.getSelectedItemPosition()];
                credentials = Credentials.create(myPrivateKey);
                fastRawTxMgr = new FastRawTransactionManager(web3j, credentials);
                transaction = Transaction.load(contractAddress, web3j, fastRawTxMgr, gasPrice, gasLimit);
                Log.d(TAG, myPublicKey);
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });
        myPrivateKeys = MainPage.this.getResources().getStringArray(R.array.privateKeys);

        myPublicKey = mySpinner.getSelectedItem().toString();
        myPrivateKey = myPrivateKeys[mySpinner.getSelectedItemPosition()];
        credentials = Credentials.create(myPrivateKey);
        fastRawTxMgr = new FastRawTransactionManager(web3j, credentials);
        transaction = Transaction.load(contractAddress, web3j, fastRawTxMgr, gasPrice, gasLimit);


        destination = spinner.getSelectedItem().toString();


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


        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        Log.d(TAG, "Bonded Devices:");

        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mBondedDevices.add(device);
            }
        }

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

                if (mBluetoothConnection[0] != null)mBluetoothConnection[0].currentLocation = currentLocation;


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





        enableBT();
        btnEnableDisable_Discoverable();

        mBluetoothConnection[0] = new BluetoothConnectionService(MainPage.this);
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

        mBluetoothConnection[0].startClient(device, uuid);

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
        if (colors[i]) {
            view.setBackgroundColor(Color.WHITE);
            colors[i] = false;
        }
        else {
            view.setBackgroundColor(Color.GREEN);
            colors[i] = true;
        }
        //lvNewDevices.setTextColor( Color.BLACK/*or whatever RGB suites good contrast*/);
//        mBluetoothAdapter.cancelDiscovery();
//
//        if (mBluetoothConnection[0] != null)
//            mBluetoothConnection[0].cancel();
//
//        Log.d(TAG, "onItemClick: You Clicked on a device.");
//        String deviceName = mBTDevices.get(i).getName();
//        String deviceAddress = mBTDevices.get(i).getAddress();
//
//        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
//        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);
//
//        //create the bond.
//        //NOTE: Requires API 17+? I think this is JellyBean
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
//            Log.d(TAG, "Trying to pair with " + deviceName);
//            mBTDevices.get(i).createBond();
//
//            mBTDevice = mBTDevices.get(i);
//            mBluetoothConnection[0] = new BluetoothConnectionService(MainPage.this);
//            startConnection();
//        }
    }

    Handler stopProgressDialogTx = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
        }
    };

    Handler startProgressDialogTx = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog = ProgressDialog.show(MainPage.this, "Applying for Transaction", "Please wait...", true);
        }
    };

    Handler stopProgressDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
        }
    };

    Handler startProgressDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog = ProgressDialog.show(MainPage.this, "Connecting Bluetooth", "Please wait...", true);
        }
    };

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Log.e(TAG, "Item Clicked");
//        connectDevice(i);
//    }

    public void connectDevice (int i) {
        //connectDeviceBluetooth( i );
        Thread t = new Thread( new Runnable() {
            @Override
            public void run() {
                startProgressDialog.sendEmptyMessage(0);
                connectDeviceBluetooth(i);
                stopProgressDialog.sendEmptyMessage(0);
            }
        });
//
        //mProgressDialog = ProgressDialog.show(MainPage.this, "Connecting Bluetooth", "Please wait...", true);
        t.start();


    }

    public void connectDeviceBluetooth (int i) {
        //first cancel discovery because its very memory intensive.

        mBluetoothAdapter.cancelDiscovery();
        if (mBluetoothConnection[0] != null)
            mBluetoothConnection[0].cancel();

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
            mBluetoothConnection[0] = new BluetoothConnectionService(MainPage.this);
            startConnection();
        }
    }

    public void connectBondedDeviceBluetooth (int i) {
        //first cancel discovery because its very memory intensive.

        mBluetoothAdapter.cancelDiscovery();
        if (mBluetoothConnection[0] != null)
            mBluetoothConnection[0].cancel();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBondedDevices.get(i).getName();
        String deviceAddress = mBondedDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection[0] = new BluetoothConnectionService(MainPage.this);
            startConnection();
        }
    }

    public void requestOneWitness (View view) {

    }

    public void connectListEach () {
        mBluetoothAdapter.cancelDiscovery();
        int numOfPaired = 0;


        for (BluetoothDevice device : mBTDevices) {
            Log.d(TAG, "Device #" + numOfPaired++);
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
                mBluetoothConnection[0] = new BluetoothConnectionService(MainPage.this);

            }
            Log.d(TAG, "STARTING startConnection...");
            startConnection();
            Log.d(TAG, "ENDING startConnection...");

            while (true) {
                if (mBluetoothConnection[0].unlock) break;
            }


            Log.d(TAG, "STARTING requestPoL...");
            requestPoL(null);
            Log.d(TAG, "ENDING requestPoL...");

            if (mBluetoothConnection[0] != null)
                mBluetoothConnection[0].cancel();


        }
    }



    public void requestPoL(View view) {
        Log.d(TAG, "Requesting Proof-of-Location...");
        t.clearComposingText();
        if (mBTDevice == null) return;
        Log.d(TAG, "Compatibility: " + mBluetoothConnection[0].isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection[0].isIncompatibleDevice) {
            if (!currentLocation.equals("Unavailable")) {
                String reqPOL = "POL request: " + currentLocation;
                byte[] bytes = reqPOL.getBytes(Charset.defaultCharset());
                mBluetoothConnection[0].write(bytes);

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
        Log.d(TAG, "Compatibility: " + mBluetoothConnection[0].isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection[0].isIncompatibleDevice) {
            if (true) {
                String reqPOL = "My Public Key is: " + myPublicKey;
                byte[] bytes = reqPOL.getBytes(Charset.defaultCharset());
                mBluetoothConnection[0].write(bytes);

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
                //resultDisplay.setText("Success!");
            }
            else ;//resultDisplay.setText("Failed! Location unavailable...");
        } else {
            Log.d(TAG, "Device "

                    + mBTDevice + " is incompatible... Can't write here!");
        }
    }

    public String sendRequestPublicKeyMultiple (View view, String temp) {
        Log.d(TAG, "Sending and requesting Public Key...");
        //t.clearComposingText();
        if (mBTDevice == null) return "";
        Log.d(TAG, "Compatibility: " + mBluetoothConnection[0].isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection[0].isIncompatibleDevice) {
            if (true) {
                String reqPOL = "My Public Key is: " + myPublicKey;
                byte[] bytes = reqPOL.getBytes(Charset.defaultCharset());
                mBluetoothConnection[0].write(bytes);

                return temp;

                //resultDisplay.setText("Success!");
            }
            else ;//resultDisplay.setText("Failed! Location unavailable...");
        } else {
            Log.d(TAG, "Device "

                    + mBTDevice + " is incompatible... Can't write here!");
        }

        return "";
    }









    public void sendLocation (View view) {

        //t.clearComposingText();
        if (mBTDevice == null) return;
        Log.d(TAG, "Compatibility: " + mBluetoothConnection[0].isIncompatibleDevice + " - Device: " + mBTDevice.getName());
        if (!mBluetoothConnection[0].isIncompatibleDevice) {
            if (!currentLocation.equals("Unavailable")) {
                String locationmessage = currentLocation;

                Thread tSendLocation = new Thread( new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Encrypting Message...");
                        Log.d(TAG, "With key: " + witnessKey);
                        String encrypted = "PoL Request: " + encryptRSAToString(locationmessage, witnessKey);
                        Log.d(TAG, "Finished encrypting...");

                        Log.d(TAG, "Encrypted data: " + encrypted);

                        Log.d(TAG, "Signing Message...");
                        byte[] bytes = encrypted.getBytes(Charset.defaultCharset());
                        String prefix = PERSONAL_MESSAGE_PREFIX + encrypted.length();
                        byte[] msgHash = Hash.sha3((prefix + encrypted).getBytes());

                        Sign.SignatureData signature = Sign.signMessage(bytes, credentials.getEcKeyPair(), true);
                        byte [] v = new byte[] {signature.getV()};
                        byte [] r = signature.getR();
                        byte [] s = signature.getS();

                        byte[] bytes2 = new byte[r.length + s.length + v.length + encrypted.getBytes().length];

                        System.arraycopy(encrypted.getBytes(), 0, bytes2, 0, encrypted.getBytes().length);
                        System.arraycopy(r, 0, bytes2, encrypted.getBytes().length, r.length);
                        System.arraycopy(s, 0, bytes2, encrypted.getBytes().length + r.length, s.length);
                        System.arraycopy(v, 0, bytes2, encrypted.getBytes().length + r.length + s.length, v.length);
                        //String r = ":" + new String(signature.getR());
                        //Log.e(TAG, r);
                        //String s = ":" + new String(signature.getS());

                        //byte [] bytes2 = encrypted.getBytes(Charset.defaultCharset());

                        //Log.e(TAG, "r: " + Arrays.toString(r));
                        //Log.e(TAG, "s: " + Arrays.toString(s));
                        //Log.e(TAG, "v: " + Arrays.toString(v));

                        Log.d(TAG, "Encrypted data R: " + "0x" + Keys.getAddress(new BigInteger(1, signature.getR())));
                        Log.d(TAG, "Encrypted data S: " + "0x" + Keys.getAddress(new BigInteger(1, signature.getS())));
                        Log.d(TAG, "Encrypted data V: " + "0x" + Keys.getAddress(BigInteger.valueOf(signature.getV())));
                        Log.d(TAG, "Finished signing...");


                        Log.d(TAG, Arrays.toString( bytes ));
                        // transfer this to BluetoothConnectionService
                        String pubKey = null;
                        try {
                            pubKey = Sign.signedMessageToKey(bytes, signature).toString(16);
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        }
                        String signerAddress = "0x" + Keys.getAddress(pubKey);

                        Log.d(TAG, "My Publickey: " + myPublicKey + "\nPublickey: " + signerAddress);

                        mBluetoothConnection[0].write(bytes2);


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
        return mBluetoothConnection[0].currentMessage;
    }

//    public void dothis(View view) {
//        //CHECKSTYLE:OFF
//        String signature = "0x2c6401216c9031b9a6fb8cbfccab4fcec6c951cdf40e2320108d1856eb532250576865fbcd452bcdc4c57321b619ed7a9cfd38bd973c3e1e0243ac2777fe9d5b1b";
////        String signature = "0x0993fdb1eee17965ce2ad068292f0d99280a0539686e47f7ce3358c6a6c52f8b8e3914f43a25c446";
////        0x3bc843a917d6c19c487c1d0c660cdd61389ce2a7651ee3171bcc212ffddca164
////        0x0993fdb1eee17965ce2ad068292f0d99280a0539
//        //CHECKSTYLE:ON
//        String address = "0x31b26e43651e9371c88af3d36c14cfd938baf4fd";
//        String message = "v0G9u7huK4mJb2K1";
//
//        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
//        byte[] msgHash = Hash.sha3( (prefix + message).getBytes() );
//
//        byte[] signatureBytes = Numeric.hexStringToByteArray( signature );
//        byte v = signatureBytes[64];
//        if (v < 27) {
//            v += 27;
//        }
//
//        Sign.SignatureData sd = new Sign.SignatureData(
//                v,
//                (byte[]) Arrays.copyOfRange( signatureBytes, 0, 32 ),
//                (byte[]) Arrays.copyOfRange( signatureBytes, 32, 64 ) );
//
//        String addressRecovered = null;
//        boolean match = false;
//
//        // Iterate for each possible key to recover
//        for (int i = 0; i < 4; i++) {
//            BigInteger publicKey = Sign.recoverFromSignature(
//                    (byte) i,
//                    new ECDSASignature( new BigInteger( 1, sd.getR() ), new BigInteger( 1, sd.getS() ) ),
//                    msgHash );
//
//            if (publicKey != null) {
//                addressRecovered = "0x" + Keys.getAddress( publicKey );
//
//                if (addressRecovered.equals( address )) {
//                    Log.e(TAG, "My Publickey: " + address + "\nPublickey: " + addressRecovered);
//                    match = true;
//                    break;
//                }
//            }
//        }
//    }
//
//    public String encryptMessage (String plaintext) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
//
//        new Thread( new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "Plaintext message: " + plaintext);
//
//                // generate a new public/private key pair to test with (note. you should only do this once and keep them!)
//                KeyPair kp = getKeyPair();
//                Log.d(TAG, "HERE");
//                PublicKey publicKey = kp.getPublic();
//                byte[] publicKeyBytes = publicKey.getEncoded();
//                String publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));
//
//                PrivateKey privateKey = kp.getPrivate();
//                byte[] privateKeyBytes = privateKey.getEncoded();
//                String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));
//
//                // test encryption
//                String encrypted = encryptRSAToString(plaintext, publicKeyBytesBase64);
//                String encrypted2 = encryptRSAToString(plaintext, publicKeyBytesBase64);
//                Log.d(TAG, "Encrypted message 1: " + encrypted);
//                Log.d(TAG, "Encrypted message 2: " + encrypted2);
//
//                // test decryption
//                String decrypted = decryptRSAToString(encrypted, privateKeyBytesBase64);
//                String decrypted2 = decryptRSAToString(encrypted2, privateKeyBytesBase64);
//                Log.d(TAG, "Decrypted message 1: " + decrypted);
//                Log.d(TAG, "Decrypted message 2: " + decrypted2);
//
//            }
//        }).start();
//
//
//
//        return "";
//    }

    public void dothis (View view) {
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
                        startProgressDialog.sendEmptyMessage(0);
                        transferToContract(null, finalNumOfProofs );
                        stopProgressDialog.sendEmptyMessage(0);
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

                i = 0;

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



    public void transferToContract(View view, int numOfProofs) {
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
                        transferToContract(null, finalNumOfProofs );
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
