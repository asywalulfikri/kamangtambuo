package asywalul.minang.wisatasumbar.ui.activity;

/**
 * Created by asywalulfikri on 10/5/16.
 */

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.LocationNearbeAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.LocationNearby;
import asywalul.minang.wisatasumbar.model.LocationNearbyWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.OnSwipeTouchListener;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class ListLocationNearby extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, LocationListener {
    // inisial buat user interface
    private ExpandableHeightListView mQuestionLv;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;



    //inisial adapter
    private LocationNearbeAdapter mAdapter;
    private LocationNearbyWrapper wrapper;
    private ArrayList<LocationNearby> mWisataList = new ArrayList<LocationNearby>();
    private int mPage = 1;

    //variabel digunakan
    private int page = 1;
    private int vote = 0;
    private static final int REQ_CODE = 1000;
    protected int mListSize = 0;
    protected boolean mIsRefresh = false;

    //shared Preference
    private AdView mAdView;
    private Intent intent;
    private String category;
    private String object = "mylistwisatasumbar";
    public final static String SP = "sharedAt";
    User user;
    private CardView llkategori;
    private int[] groupChoice = {0, 0, 0};
    private int mCurrentGrouping = 0;
    private String API =  "/listArticles.php?page=" + page + "&count=20";
    private TextView mKategori;
    private Toolbar toolbar;

    private LocationManager locationManager;
    private Location location;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLanguage();
        setContentView(R.layout.activity_list_lokasi_nearme);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        permission();
        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        user = getUser();
        intent = getIntent();

        llkategori = (CardView)findViewById(R.id.layout_kategori);
        mKategori  = (TextView)findViewById(R.id.tv_kategori);

        llkategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGroupOptions();
            }
        });



       /* mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);*/
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //or whatever you used to do on your onOptionItemSelected's android.R.id.home callback
            }
        });

        ApplyFontToolbar();

        mLoadingLayout = (LoadingLayout) findViewById(R.id.layout_loading);
        swipContainer = (SwipeRefreshLayout) findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

        mLoadingLayout = (LoadingLayout) findViewById(R.id.layout_loading);


        mQuestionLv = (ExpandableHeightListView) findViewById(R.id.lv_data);
         mQuestionLv.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
             @Override
             public void onClick(View view, MotionEvent e) {
                 int position = mQuestionLv.pointToPosition((int) e.getX(), (int) e.getY());
                 final LocationNearby wisata = mWisataList.get(position);
                 if (user.isLogin.equals("") || user.typeLogin.equals("null")) {
                     showDialogLogin(getString(R.string.text_login_first));
                 } else {
                     String urlfoto ="https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=";
                     String googlemapkey = "&key=AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
                     Intent intent = new Intent(getActivity(), DetailLocationNearby.class);
                     intent.putExtra("latitude", wisata.latitude);
                     intent.putExtra("longitude", wisata.longitude);
                     intent.putExtra("jambuka",wisata.jambuka);
                     intent.putExtra("lokasi",wisata.location);
                     intent.putExtra("foto",urlfoto+wisata.foto+googlemapkey);
                     intent.putExtra("name",wisata.name);
                     intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                     startActivity(intent);
                 }

                 super.onClick(view, e);
             }

             // your on onDoubleClick here
         });
        API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
        loadTask2(API);

    }

    public void ApplyFontToolbar() {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/wwf-webfont.ttf");
                if (tv.getText().equals(toolbar.getTitle())) {
                    tv.setTypeface(custom_font);
                    tv.setTextSize(25);
                    tv.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                }
            }
        }
    }



    public void onBackPressed() {
        Intent i = new Intent(ListLocationNearby.this, MainActivityBeranda.class);
        i.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
        setResult(Activity.RESULT_OK, intent);
        startActivity(i);
    }


    private void showGroupOptions() {
        String[] options = (getResources().getStringArray(R.array.array_nearme));

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.text_kategori));
        builder.setSingleChoiceItems(options, mCurrentGrouping, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String optionMenu = String.valueOf(which);
                if (optionMenu.equals("0")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.semua_kategori));
                    mCurrentGrouping = 0;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("1")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=bank&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.bank));
                    mCurrentGrouping = 1;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("2")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=hospital&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.rumah_sakit));
                    mCurrentGrouping = 2;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("3")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=atm&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.atm));
                    mCurrentGrouping = 3;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("4")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=cafe&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.kafe));
                    mCurrentGrouping = 4;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("5")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=mosque&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.mesjid));
                    mCurrentGrouping = 5;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("6")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=museum&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.museum));
                    mCurrentGrouping = 6;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("7")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=police&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.kantor_polisi));
                    mCurrentGrouping = 7;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("8")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=restaurant&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.restorant));
                    mCurrentGrouping = 8;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("9")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=school&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.sekolah));
                    mCurrentGrouping = 9;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("10")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=store&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.pasar));
                    mCurrentGrouping = 10;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("11")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=airport&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.bandara));
                    mCurrentGrouping = 11;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("12")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=food&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.toko_makanan));
                    mCurrentGrouping = 12;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("13")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=zoo&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.kebun_binatang));
                    mCurrentGrouping = 13;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("14")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=university&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.universitas));
                    mCurrentGrouping = 14;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("15")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=post_office&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.kantor_pos));
                    mCurrentGrouping = 15;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("16")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=shopping_mall&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.mall));
                    mCurrentGrouping = 16;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("17")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=taxi_stand&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.taksi));
                    mCurrentGrouping = 17;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("18")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=fire_station&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.pemadam_kebakaran));
                    mCurrentGrouping = 19;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("19")) {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=parking&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.parkir));
                    mCurrentGrouping = 20;
                    onRefresh();
                    dialog.dismiss();

                } else {
                    API = Cons.URL_API_GOOGLE+latitude+","+longitude+"&radius=1000&types=train_station&sensor=true&key="+Cons.GOOGLE_MAP_APIKEY;
                    mKategori.setText(getString(R.string.stasiun));
                    mCurrentGrouping = 20;
                    onRefresh();
                    dialog.dismiss();

                }
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected boolean isListVisible() {
        return (mQuestionLv.getVisibility() == View.VISIBLE) ? true : false;
    }

    protected void showList() {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mQuestionLv.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    protected void showEmpty(String message) {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mLoadingLayout.hideAndEmpty(message, false);
        }
    }

    protected void scrollMyListViewToBottom(final int sel) {
        mQuestionLv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mQuestionLv.setSelection(sel);
            }
        });
    }



    public void loadData() {
        loadTask2(API);
    }




    private void updateList(LocationNearbyWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new LocationNearbeAdapter(getActivity());

                mAdapter.setData(mWisataList);
                mAdapter.notifyDataSetChanged();
                mQuestionLv.setAdapter(mAdapter);
                if (mListSize != 0) {
                    scrollMyListViewToBottom(-1 + (mWisataList.size() - mListSize));
                }
                mIsRefresh = false;
                return;
            }
            mWisataList.add((LocationNearby) wrapper.list.get(j));
           // Debug.i((new StringBuilder("ID ")).append(((LocationNearby) mWisataList.get(j)).wisataId).append(" pos ").append(String.valueOf(j)).toString());
            j++;
        } while (true);
    }


    @Override
    public void onRefresh() {

        mIsRefresh = true;
        loadTask2(API);

    }

    public void loadTask2(String API) {
        startCountTime();
        String url =  API;
        Log.d("response",url);

        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        LocationNearbyWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.locationParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mWisataList = new ArrayList<LocationNearby>();
                                    }
                                    updateList(wrapper);
                                   // (mUpdateCacheTask = new UpdateCacheTask(wrapper)).execute();
                                }
                            } else {
                                showEmpty(getString(R.string.text_download_failed));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimerPage.cancel();
                        try {
                            // actionTracking("getHomeList", "homeList", "failed", String.valueOf(totalTimeSec), user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        swipContainer.setRefreshing(false);
                        VolleyLog.d("Response Error", "Error" + error.getMessage());
                        showEmpty(getString(R.string.lokasi_blank));
                        errorHandle(error);
                    }
                });

        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mRequest);
    }

    public void permission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ListLocationNearby.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                // latitudePosition.setText(String.valueOf(location.getLatitude()));
                // longitudePosition.setText(String.valueOf(location.getLongitude()));
                latitude = String.valueOf(location.getLatitude());
                longitude = String .valueOf(location.getLongitude());


            }

        } else {
            showGPSDisabledAlertToUser();
        }
    }
    @Override
    public void onLocationChanged(final Location location) {
        //latitudePosition.setText(String.valueOf(location.getLatitude()));
        // longitudePosition.setText(String.valueOf(location.getLongitude()));
        latitude = String.valueOf(location.getLatitude());
        longitude = String .valueOf(location.getLongitude());

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
    private void showGPSDisabledAlertToUser() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public static void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality() + ", " +  address.getCountryName() ;
                    }
                } catch (IOException e) {
                    Log.e("Post", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }



}