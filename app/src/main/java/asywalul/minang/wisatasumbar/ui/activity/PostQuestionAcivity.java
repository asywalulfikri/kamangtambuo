package asywalul.minang.wisatasumbar.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.database.QuestionDb;
import asywalul.minang.wisatasumbar.image.CameraActivity;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Base64;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.StringUtil;
import asywalul.minang.wisatasumbar.util.TimeUtil;
import asywalul.minang.wisatasumbar.util.Util;

/**
 * Created by Toshiba on 3/12/2016.
 */
public class PostQuestionAcivity extends BaseActivity implements LocationListener {

    private ImageView mCloseIv;
    private ImageView mPhotoIv;
    private ImageView attachIv;
    private EditText tagsEt;
    private EditText contentEt;
    private Button postBtn;
    private QuestionDb mQuestionDb;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    String a;
    String tagsList[];
    String tags;
    Bitmap bitmappp = null;

    private String mFilePath = "";
    private ArrayList<Integer> mSelectedItems;
    boolean[] isSelectedArray = {false, false, false, false};
    public final static String SP = "sharedAt";
    private Uri mImageCaptureUri;
    private Uri mImageCropUri;
    private User user;
    private String userId;
    private Toolbar toolbar;
    private LocationManager locationManager;
    private Location location;
    private String latitude ="0.0";
    private String longitude = "0.0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        permission();

        enableDatabase();

        toolbar();

        user   = getUser();
        userId = user.userId;
        mQuestionDb = new QuestionDb(getDatabase());
        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mPhotoIv = (ImageView) findViewById(R.id.iv_attachment);
        attachIv = (ImageView) findViewById(R.id.iv_clip);
        postBtn  = (Button)    findViewById(R.id.btn_send);
        tagsEt   = (EditText)  findViewById(R.id.et_tags);

        contentEt = (EditText) findViewById(R.id.et_content);

        tagsEt.setFocusableInTouchMode(false);
        tagsEt.setFocusable(false);

        tagsEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showTagsOptions();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String content = contentEt.getText().toString().replaceAll("\\s+", " ").trim();
                tags = tagsEt.getText().toString();
                Log.i("ISI", content);

                if (content.equals("")) {
                    Toast.makeText(PostQuestionAcivity.this, getString(R.string.text_post_question_here), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tags.equals("")) {
                    Toast.makeText(PostQuestionAcivity.this, getString(R.string.text_tags_not_choise), Toast.LENGTH_SHORT).show();

                    return;

                }
                postQuestionTask(content, tags);
            }
        });


        mCloseIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mFilePath = "";
                mCloseIv.setVisibility(View.GONE);
                mPhotoIv.setVisibility(View.GONE);
                attachIv.setVisibility(View.VISIBLE);
            }
        });


        attachIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragesPermission();
                } else {
                    //showChooserDialog();
                    Intent intent = new Intent(PostQuestionAcivity.this, CameraActivity.class);
                    startActivityForResult(intent,1313);
                }
            }
        });

    }


    private void  toolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        ApplyFontToolbar();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        mQuestionDb.reload(getDatabase());
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;
                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.i("On Result");

        if (requestCode == PICK_FROM_FILE && resultCode == Activity.RESULT_OK) {
            mImageCaptureUri = data.getData();
            doCrop();

        } else if (requestCode == 1313 && resultCode == Activity.RESULT_OK) {
            // String path = data.getData().getPath();
            String path = data.getStringExtra("path");
            Log.e("PathMain", path);
            Log.e("step5","5");

            if (path != null || !path.equals("")) {
                mFilePath = path;
                bitmappp = getBitmap(path);
                attachIv.setVisibility(View.GONE);
                mPhotoIv.setVisibility(View.VISIBLE);
                mPhotoIv.setImageBitmap(bitmappp);
                mCloseIv.setVisibility(View.VISIBLE);
            }
        }else if (requestCode == CROP_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            mFilePath = mImageCropUri.getPath();
            File file = new File(mFilePath);

            if (!file.exists()) {
                showToast("Image crop failed");
            } else {
                bitmappp = getBitmap(mFilePath);

                mPhotoIv.setVisibility(View.VISIBLE);
                mPhotoIv.setImageBitmap(bitmappp);
                mCloseIv.setVisibility(View.VISIBLE);

            }
        } else if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            doCrop();
        }
    }

    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            showToast("Can not find image crop app");

            return;
        } else {
            intent.setData(mImageCaptureUri);

            //  intent.putExtra("outputX",  500);
            //   intent.putExtra("outputY", 200);
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
             //   intent.putExtra("scale", true);
             //   intent.putExtra("return-data", true);

            String fileName = StringUtil.getFileName(mImageCaptureUri.getPath());
            String cropFile = Util.getAppDir() + "/crop_" + fileName + ".jpg";

            Debug.i(cropFile);
            Debug.i("File " + mImageCaptureUri.getPath());

            mImageCropUri = Uri.fromFile(new File(cropFile));

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropUri);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            }
        }
    }

    private void showChooserDialog() {
        String[] options = {getString(R.string.text_camera), getString(R.string.text_gallery)};

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ambil Menggnakan");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int position) {
                if (position == 0) {
                    if (isIntentAvailable(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE)) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                        try {
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_FROM_CAMERA);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("Sorry, camera is not available");
                    }
                } else if (position == 1) { //gallery
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        builder.create().show();
    }

    private boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    public void showAttachment(Bitmap bitmap) {
        mCloseIv.setVisibility(View.VISIBLE);
        mPhotoIv.setVisibility(View.VISIBLE);
        attachIv.setVisibility(View.GONE);

        mPhotoIv.setImageBitmap(bitmap);
    }

    private void showTagsOptions() {

        mSelectedItems = new ArrayList<Integer>();
        tagsList = new String[]
                {getString(R.string.text_wisata), getString(R.string.text_kuliner), getString(R.string.text_budaya), getString(R.string.text_lokasi)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.posttopik))
                .setMultiChoiceItems(tagsList, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            isSelectedArray[which] = true;
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < isSelectedArray.length; i++) {
                            if (isSelectedArray[i] == true) {
                                mSelectedItems.add(i);
                            }
                        }
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            int index = mSelectedItems.get(i);
                            String hasil = tagsList[index];
                            sb.append(hasil);
                            if (i < mSelectedItems.size() - 1) {
                                sb.append(", "); // Add a comma for separation
                            }
                        }

                        tagsEt.setText(sb);
                        Log.e("tags", mSelectedItems.toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }


    private void refreshFeed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    /*public class PostQuestionTask extends AsyncTask<URL, Integer, Long> {
        String content, error = "";

        //  CustomProgressDialog progressDlg;
        final LoadingAlertDialog alert = new LoadingAlertDialog(PostQuestionAcivity.this);
        // User userMode = getUser();

        public PostQuestionTask(String content) {
            this.content = content;

            alert.show();
        }

        protected void onCancelled() {

            alert.dismiss();
        }

        protected void onPreExecute() {

            alert.show();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            //noinspection ResourceType
            tags = tagsEt.getText().toString();
            SharedPreferences getId = getApplicationContext()
                    .getSharedPreferences(SP, 0);
            String iduser = getId.getString(Cons.KEY_ID, "Nothing");
            String waktu = (String.valueOf(TimeUtil.getUnix()));


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            QuestionConnection conn = new QuestionConnection();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            if (bitmappp == null) {
            } else {
                bitmappp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }
            byte[] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeBytes(byte_arr);
            String image_name = "IMG_" + timeStamp + ".jpg";
            String path;
            path = Cons.FOTOWISATA + image_name;
            Log.e("bit", String.valueOf(bitmappp));


            // Check for Internet availability
            if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                if (bitmappp == null || image_name.equals("") || image_str.equals("")) {
                    mQuestionDb.update(iduser, content, tags,waktu);
                } else {
                    mQuestionDb.update(iduser, content, tags, image_name,waktu);
                }

                result = 2;
            } else {
                try {

                    if (bitmappp == null || image_name.equals("") || image_str.equals("")) {
                        conn.postQuestion(iduser, content, tags, waktu, latitude, longitude);
                        result = 1;
                    } else {
                        conn.postQuestion(iduser, content, tags, image_str, image_name, path, waktu, latitude, longitude);
                    }
                    result = 1;
                } catch (WisataException e) {
                    e.printStackTrace();
                    error = e.getError();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            alert.dismiss();

            if (result == 1) {
                Toast.makeText(PostQuestionAcivity.this, getString(R.string.text_pertanyaan_ditambah), Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();

                ///  refreshFeed();
            } else if (result == 2) {
                Toast.makeText(PostQuestionAcivity.this, getString(R.string.no_connection_for_post), Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();

                refreshFeed();
            } else {
                error = (error.equals("")) ? getString(R.string.text_post_question_failed) : error;

                showError(error);
            }
        }
    }*/

    private void postQuestionTask(String content, String tags) {
        startCountTime();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmappp == null) {
        } else {
            bitmappp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        byte[] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeBytes(byte_arr);
        String image_name = "IMG_" + timeStamp + ".jpg";
        String path;
        String waktu = (String.valueOf(TimeUtil.getUnix()));
        path = Cons.FOTOWISATA + image_name;
        if (mFilePath.equals("")) {
            postQuestionText(content, tags,waktu);
        } else {
            postQuestionImage(content, tags,image_name,image_str,waktu);
        }
    }


    private void postQuestionText(final String content, final String tags,final String waktu) {
        final CustomProgressDialog progressDialog;
        progressDialog = new CustomProgressDialog(getActivity(), getString(R.string.text_sending));
        progressDialog.show();

        String url = Cons.CONVERSATION_URL + "/ListQuestion/";

        /*if(android.os.Build.VERSION.SDK_INT > 17){
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        }else {

        }*/


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PostQuestionAcivity.this, getString(R.string.text_post_question_success), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        refreshFeed();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PostQuestionAcivity.this, getString(R.string.text_post_question_failed), Toast.LENGTH_SHORT).show();
                VolleyLog.e("PostQuestion", "Error" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> obj = new HashMap<String, String>();
                obj.put(Cons.KEY_ID,userId);
                obj.put(Cons.CONV_CONTENT, content);
                obj.put(Cons.CONV_TAGS, tags);
                obj.put(Cons.CONV_DATE_SUBMITTED,waktu);
                obj.put(Cons.CONV_LATITUDE,latitude);
                obj.put(Cons.CONV_LONGITUDE,longitude);
                obj.put(Cons.CONV_TYPE,"questions");
              /*  obj.put(Cons.ANDROID_VERSION,String.valueOf(android.os.Build.VERSION.SDK_INT));
                obj.put(Cons.APP_VERSION,getString(R.string.app_version));*/
                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(strReq);
    }

    private void postQuestionImage(final String content, final String tags, final String image_name, final String image_str,final String waktu) {
        final CustomProgressDialog progressDialog;
        progressDialog = new CustomProgressDialog(getActivity(), getString(R.string.text_sending));
        progressDialog.show();

        String url = Cons.CONVERSATION_URL+"/post_question.php";

        final String path;
        path = Cons.FOTOWISATA + image_name;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PostQuestionAcivity.this, getString(R.string.text_post_question_success), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        refreshFeed();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PostQuestionAcivity.this, getString(R.string.text_post_question_failed), Toast.LENGTH_SHORT).show();
                VolleyLog.e("PostQuestion", "Error" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> obj = new HashMap<String, String>();
                obj.put(Cons.KEY_ID, userId);
                obj.put(Cons.CONV_CONTENT, content);
                obj.put(Cons.CONV_TAGS, tags);
                obj.put(Cons.CONV_DATE_SUBMITTED,waktu);
                obj.put(Cons.CONV_LATITUDE,longitude);
                obj.put(Cons.CONV_LONGITUDE,latitude);
                obj.put("foto", image_str);
                obj.put("image_name",image_name);
                obj.put(Cons.CONV_ATTACHMENT, path);
                obj.put(Cons.ANDROID_VERSION,String.valueOf(android.os.Build.VERSION.SDK_INT));
                obj.put(Cons.APP_VERSION,getString(R.string.app_version));
                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(strReq);
    }



    public void permission(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PostQuestionAcivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());

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
        longitude = String.valueOf(location.getLongitude());

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
        AlertDialog alert = alertDialogBuilder.create();
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
