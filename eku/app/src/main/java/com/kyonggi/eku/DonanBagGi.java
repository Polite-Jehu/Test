package com.kyonggi.eku;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.List;

public class DonanBagGi extends AppCompatActivity {
    private PermissionSupport permission;
    private MinewBeaconManager mMinewBeaconManager;
    private boolean isScanning;
    boolean stealing = false;
    TextView textView;
    Button button;
    Button b;
    ImageView s;
    MediaPlayer player;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donan_bag_gi);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE |
                        PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        bluetoothOn();
        permissionCheck();
        initManager();
        initListener();
    }

    private void bluetoothOn() {
        BluetoothAdapter ap = BluetoothAdapter.getDefaultAdapter();
        //   ap.enable();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getBaseContext(), "??????????????? ??????????????????. ?????? ????????? ?????? ?????? ????????? ????????????", Toast.LENGTH_LONG).show();
            Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getBaseContext(), "??????????????? ????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // ?????? ??????
    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // PermissionSupport.java ????????? ?????? ??????
            permission = new PermissionSupport(this, this);
            // ?????? ?????? ??? ????????? false??? ????????????
            if (!permission.checkPermission()) {
                //?????? ??????
                permission.requestPermission();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    888);
            //Toast.makeText(getApplicationContext(),(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION))+"",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "?????? ?????? ???????????????.", Toast.LENGTH_LONG).show();

        }
    }

    // Request Permission??? ?????? ?????? ??? ?????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 888) {
            if (!permission.permissionResult(requestCode, permissions, grantResults)) {
                // ?????? permission ??????
                permission.requestPermission();
            }
        } else {

        }
        //???????????? ????????? false??? ??????????????? (???????????? ?????? ?????? ??????)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //????????? ?????????
    private void initManager() {
        button = findViewById(R.id.Donan_Button);
        textView = findViewById(R.id.Donan_TextView);
        b = findViewById(R.id.BackButton);
        s = findViewById(R.id.Donan_ImageView);
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);

    }


    private void initListener() {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainBoard.class);
                intent.putExtra("GANG", getIntent().getStringExtra("GANG"));
                intent.putExtra("NoMap", getIntent().getStringExtra("NoMap"));
                startActivity(intent);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????? ???????????? ???????????????????????? ?????????
                if (isScanning) {
                    button.setBackgroundColor(Color.rgb(42, 81, 137));
                    button.setText("???????????? ?????? ??????");
                    isScanning = false;
                    textView.setText("??????????????? ??????????????????.");
                    s.setColorFilter(Color.parseColor("#5786CA"));

                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();

                    }
                    stealing = false;
                } else {
                    button.setText("???????????? ?????? ??????");
                    button.setBackgroundColor(Color.RED);
                    //??????????????? ??????
                    isScanning = true;
                    textView.setText("?????? ????????? ????????? ??????????????? ????????? ???????????????.");
                    s.setColorFilter(Color.RED);

                    try {
                        mMinewBeaconManager.startScan();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            /**
             *   ?????? ??????  ?????????.
             *  @param minewBeacons  new beacons the manager scanned
             */
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {


            }

            /**
             *  if a beacon didn't update data in 10 seconds, we think this beacon is out of rang, the manager will call back this method.
             *  ????????? ???????????? ??????
             *  @param minewBeacons beacons out of range
             */
            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {


            }


            @Override
            public void onRangeBeacons(List<MinewBeacon> minewBeacons) {
                for (MinewBeacon m : minewBeacons) {
                    wakeLock.acquire();
                    //Toast.makeText(getApplicationContext(),"?????? ?????? ????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                    String temp = m.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue();
                    String rssi = m.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getStringValue();
                    Toast.makeText(getApplicationContext(), rssi, Toast.LENGTH_SHORT).show();
                    double iRssi = 0;
                    try {
                        iRssi = Double.valueOf(rssi);
                    } catch (Exception e) {
                        iRssi = 0;
                    }
                    if (temp.equals("40010") && iRssi < -85) {
                        if (stealing == true) {
                        } else {
                            stealing = true;
                            textView.setText("????????? ???????????? ?????????!!!!!");
                            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            audio.setStreamVolume(AudioManager.STREAM_MUSIC, 15, AudioManager.FLAG_PLAY_SOUND);
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(new long[]{50, 300}, 0); // 0.5?????? ??????
                            player = MediaPlayer.create(getBaseContext(), R.raw.sirent);
                            player.setLooping(true);
                            player.start();
                            Button offButton = findViewById(R.id.Donan_Off);
                            offButton.setVisibility(View.VISIBLE);
                            offButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    offButton.setVisibility(View.INVISIBLE);
                                    player.stop();
                                    vibrator.cancel();
                                    if (player == null) {

                                    } else {
                                        player.release();
                                    }
                                    stealing = false;
                                    textView.setText("???????????? ?????????");
                                    button.setBackgroundColor(Color.BLUE);
                                    button.setText("????????????");
                                    isScanning = false;
                                    if (mMinewBeaconManager != null) {
                                        mMinewBeaconManager.stopScan();
                                    }
                                    if (wakeLock.isHeld())
                                        wakeLock.release();

                                    return;
                                }
                            });
                        }


                    }
                }
                //Toast.makeText(getApplicationContext(),  "????????????==>"+list.size(), Toast.LENGTH_SHORT).show();
             /*   if(minewBeacons.size()>=1)
                {
                    for (MinewBeacon minewBeacon : minewBeacons) {
                        String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getStringValue();
                        Toast.makeText(getApplicationContext(), deviceName + "?????? ?????????", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {

            }


        });

    }

    @Override
    public void onBackPressed() {
        long backKeyPressedTime = 0;
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            Button offButton = findViewById(R.id.Donan_Off);
            offButton.setVisibility(View.VISIBLE);
            offButton.setVisibility(View.INVISIBLE);
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.cancel();
            if (player == null) {

            } else {
                player.release();
            }
            stealing = false;
            isScanning = false;
            if (mMinewBeaconManager != null) {
                mMinewBeaconManager.stopScan();
            }
            Intent intent = new Intent(getApplicationContext(), MainBoard.class);
            intent.putExtra("GANG", getIntent().getStringExtra("GANG"));
            intent.putExtra("NoMap", getIntent().getStringExtra("NoMap"));
            startActivity(intent);
            finish();
            return;
        }
    }

    /*
     * ???????????? ?????????
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isScanning) {
            mMinewBeaconManager.stopScan();
        }
    }

}
