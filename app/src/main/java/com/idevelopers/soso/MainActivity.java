package com.idevelopers.soso;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.idevelopers.soso.connections.RetrofitApi;
import com.idevelopers.soso.connections.RetrofitSingleTone;
import com.idevelopers.soso.models.SendClas;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity {

    private ImageView ivLoader;
    private Button btnSos;
    Animation animationRotate;
    EditText etInput;
    View activity_main;
    ImageView ivCar;

    Animation fadeIn;
    Animation fadeOut;


    public static final String INPUTNUMBER = "ნომერი უნდა შედგებოდეს 9 ციფრისგან !";
    public static final String NO_NUMBER = "შეიყვანეთ ნომერი !";
    public static final String NOINTERNET = "ჩართეთ ინტერნეტი";
    public static final String NOINTERNET_POSSITIVE = "თავიდან სცადე";
    public static final String DIAX = "დიახ";


    public static final String LAST_NUMBER = "last number";

    LocationManager locationManager;
    LocationListener locationListener;

    double latitude;
    double longtitude;

    TextView tvDGG;

    boolean requested = false;
    boolean updated = false;

    boolean gps_enabled = false;
    boolean network_enabled = false;

    Snackbar snackbar;


    int[] suratebi;
    int countImage = 0;

    Timer t;
    private ImageView ivInfo;
    private FrameLayout fInfo;
    Fragment infoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        //=========================================================================

        //=========================================================================
        checkSettings();

        suratebi = new int[5];
        suratebi[0] = R.drawable.towaway4;
        suratebi[1] = R.drawable.drink2;
        suratebi[2] = R.drawable.fuel3;
        suratebi[3] = R.drawable.battery1;
        suratebi[4] = R.drawable.tyre5;


        this.t = new Timer();
        this.t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      shuffleImage();
                                  }
                              }

                );
            }
        }, 2000L, 2000);  //


        //=========================================================================//=========================================================================


    }

    public void shuffleImage() {
        if (countImage == 5) {
            countImage = 0;
        }
        ivCar.setImageResource(suratebi[countImage]);
        ivCar.startAnimation(fadeOut);
        countImage++;

    }

    private void makeUseOfNewLocation(Location location) {
        latitude = location.getLatitude();
        longtitude = location.getLongitude();
        updated = true;
        if (requested == true) {
            requested = false;
            sendSos();
        }
    }


    private void initView() {


        tvDGG = (TextView) findViewById(R.id.tvDGG);

        ivCar = (ImageView) findViewById(R.id.ivCar);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        Typeface tf = Typeface.createFromAsset(getAssets(), "customfont.otf");

        activity_main = findViewById(R.id.activity_main);
        activity_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
        ivLoader = (ImageView) findViewById(R.id.ivLoader);
        btnSos = (Button) findViewById(R.id.btnSos);
        etInput = (EditText) findViewById(R.id.etInput);

        etInput.setTypeface(tf);

        etInput.setText(SosApplication.getInstance().getSharedString(LAST_NUMBER));

        if (SosApplication.getInstance().getSharedString(LAST_NUMBER).equals("") == false) {
//=========================================================================

            // cursoris moxsna

//=========================================================================
        }
        animationRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        ivInfo = (ImageView) findViewById(R.id.ivInfo);
        fInfo = (FrameLayout) findViewById(R.id.fInfo);
    }

    public void initLocationManager() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //makeUseOfNewLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }


    public void checkSettings() {
        //=========================================================================
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {


            //=========================================================================
            if (checkInternetConnection()) {
                initLocationManager();
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkLocation();
                }
                else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            78);
                }
            } else {
                makeDialog(NOINTERNET, NOINTERNET_POSSITIVE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CHANGE_NETWORK_STATE},
                    77);
        }
    }
    private void sendSos() {
//        Gson gson = new Gson();

        SendClas goObject = new SendClas(latitude, longtitude, etInput.getText().toString());
//        Toast.makeText(this, Double.toString(latitude) + "     " + Double.toString(longtitude), Toast.LENGTH_SHORT).show();
//        //request
//
//        String jsonInString = gson.toJson(goObject);


        //=========================================================================
        RetrofitApi api = RetrofitSingleTone.getInstance().getRetrofitApi();
        final Call<SendClas> send = api.send(goObject);
        send.enqueue(new Callback<SendClas>() {
            @Override
            public void onResponse(Call<SendClas> call, Response<SendClas> response) {
            }

            @Override
            public void onFailure(Call<SendClas> call, Throwable t) {

            }
        });


        //=========================================================================

    }

    public void sosClick(View view) {

        hideKeyboard();

        if (!checkInternetConnection()) {
            makeDialog(NOINTERNET, NOINTERNET_POSSITIVE);
            return;
        }
        if (etInput.getText().toString().equals("")) {
            makeDialog(NO_NUMBER, DIAX);
        } else if (etInput.getText().length() != 9) {
            makeDialog(INPUTNUMBER, DIAX);
        } else {
            SosApplication.getInstance().setSharedString(LAST_NUMBER, etInput.getText().toString());

            //=========================================================================
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("ნამდვილად გსურთ ოპერატორი ამ ნომერზე " + etInput.getText().toString() + " დაგიკავშირდეთ ?");
            dialog.setPositiveButton("კი", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    sosAgreed();
                }
            });
            dialog.setNegativeButton("არა", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            AlertDialog alert = dialog.create();

            alert.show();
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.parseColor("#007AFF"));
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.parseColor("#007AFF"));
            //=========================================================================
        }

        //=========================================================================
    }

    public void makeDialog(String message, String positiveButtonText) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this)
                .setTitle("შეცდომა !")
                .setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                    }
                });
//        builder1.show();

        AlertDialog alert = builder1.create();

        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#007AFF"));

    }

    //=========================================================================
    private boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;


    }

    public void checkLocation() {
        final MainActivity context = this;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("აპლიკაციის სრული გამოყენებისთვის, საჭიროა თქვენი ადგილმდებარეობის სერვისის გააქტიურება !");
            dialog.setPositiveButton("open location settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            AlertDialog alert = dialog.create();

            alert.show();
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.parseColor("#007AFF"));
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.parseColor("#007AFF"));
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //=========================================================================
    public void sosAgreed() {
        bzriali();
        //=========================================================================
        //  თუ არაა ლოცატიონ

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            sendSos();
        }
        //=========================================================================


        else if (updated == true) {
            sendSos();
        } else {
            requested = true;
        }
    }
    //=========================================================================

    public void showSnackbar() {
        snackbar = Snackbar
                .make(activity_main, "გთხოვთ მოითმინოთ, ჩვენი ოპერატორი მალე დაგიკავშირდებათ !", Snackbar.LENGTH_INDEFINITE);

        snackbar.show();
    }

    public void hideSnackbar() {
        snackbar.dismiss();
    }


    public void bzriali() {
        btnSos.setBackgroundResource(R.drawable.checked);
        btnSos.setClickable(false);
        ivLoader.startAnimation(animationRotate);

        tvDGG.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                tvDGG.setVisibility(View.GONE);
                btnSos.setBackgroundResource(R.drawable.sos);
                ivLoader.clearAnimation();
                ivLoader.setVisibility(View.INVISIBLE);
                btnSos.setClickable(true);

            }
        }, 30000);


    }

    public void showFragment(View view) {
        hideKeyboard();

        btnSos.setVisibility(View.GONE);
        etInput.setVisibility(View.GONE);

        infoFragment = new InfoFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.fInfo, infoFragment).commit();
//        activity_main.setAnimation(fadeOut);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        btnSos.setVisibility(View.VISIBLE);
        etInput.setVisibility(View.VISIBLE);
    }
}
