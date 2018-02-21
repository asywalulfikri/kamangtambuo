package asywalul.minang.wisatasumbar.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.http.UserConnection;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.image.CameraActivity;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Base64;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.StringUtil;
import asywalul.minang.wisatasumbar.util.TimeUtil;
import asywalul.minang.wisatasumbar.util.Util;


/**
 * Created by Asuwalulfikri on 2/6/2016.
 */
public class RegisterActivity  extends BaseActivity implements LocationListener {
    private EditText mUsernameEt;
    private EditText mBirthEt;
    private EditText mEmailEt;
    private ImageView mAvatarIv;
    private EditText mGender;
    private String Id = "";
    private EditText mPasswordEt;
    private Button   mRegisterBtn;
    private CheckBox mShowpasswordCb;
    private boolean  mIsLoading = false;
    private Calendar myCalendar = Calendar.getInstance();
    private ImageView mTakeImageIv;
    private String mFilePath = "";
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE   = 3;
    private Uri mImageCaptureUri;
    private Uri mImageCropUri;
    private String username, password, birth, email,gender;
    Bitmap bitmap = null;
    private LocationManager locationManager;
    private Location location;
    private String latitude = "0.0";
    private String longitude = "0.0";
    private CheckBox cbShowPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        permission();

        mUsernameEt       = (EditText) findViewById(R.id.et_username);
        mPasswordEt       = (EditText) findViewById(R.id.et_password);
        mEmailEt          = (EditText) findViewById(R.id.et_email);
        mAvatarIv         = (ImageView)findViewById(R.id.iv_avatar);
        mGender           = (EditText)findViewById(R.id.et_gender);
        mBirthEt          = (EditText) findViewById(R.id.et_birthDate);
        mBirthEt.setFocusableInTouchMode(false);
        mBirthEt.setFocusable(false);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showCalender();
            }
        };


        mTakeImageIv = (ImageView) findViewById(R.id.iv_take_image);
        mTakeImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragesPermission();
                } else {
                    Intent intent = new Intent(RegisterActivity.this, CameraActivity.class);
                    startActivityForResult(intent,1313);
                }
            }
        });
        mGender.setFocusableInTouchMode(false);
        mGender.setFocusable(false);
        mGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderOptions();
                hideKeyboard();
            }
        });


        cbShowPassword = (CheckBox)findViewById(R.id.cbShowPassword);
        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (!isChecked) {
                    mPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    mPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });



        mBirthEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });





        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = mUsernameEt.getText().toString().trim();
                password = mPasswordEt.getText().toString().trim();
                email    = mEmailEt.getText().toString().trim();
                birth    = mBirthEt.getText().toString().trim();
                gender   = mGender.getText().toString().trim();

                if (username.equals("") || password.equals("") || email.equals("") ||
                        birth.equals("")) {
                    Toast.makeText(RegisterActivity.this,getString(R.string.text_complete_from), Toast.LENGTH_SHORT).show();
                    return;

                } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    Toast.makeText(RegisterActivity.this,getString(R.string.text_email_not_valid), Toast.LENGTH_SHORT).show();
                    return;
                }  else if (mPasswordEt.getText().length() < 6) {
                    mPasswordEt.setError(getString(R.string.text_password_must_more));
                    return;

                }

                if(Id.equals("")||Id.equals("null")){
                    Id = UUID.randomUUID().toString().trim();

                }
                postRegister(Id,username,email,gender,birth,password);

            }
        });
    }
    private void showGenderOptions() {
        final String[] genders = {getString(R.string.text_male), getString(R.string.text_female)};

        AlertDialog.Builder builder = getBuilder(getActivity());
        builder.setTitle(getString(R.string.title_dialog_gender))
                .setItems(genders, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mGender.setText(genders[which]);
                        Toast.makeText(getActivity(), mGender.getText(), Toast.LENGTH_SHORT).show();
                    }
                });

        builder.create().show();
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

        if (requestCode == PICK_FROM_FILE  && resultCode == Activity.RESULT_OK) {
            mImageCaptureUri = data.getData();

            doCrop();
        } else if (requestCode == 1313 && resultCode == Activity.RESULT_OK) {
                // String path = data.getData().getPath();
                String path = data.getStringExtra("path");
                Log.e("PathMain", path);
                Log.e("step5","5");

                if (path != null || !path.equals("")) {
                    mFilePath = path;
                    bitmap = getBitmap(path);
                    mAvatarIv.setImageBitmap(bitmap);
                }
        } else if (requestCode == CROP_FROM_CAMERA  && resultCode == Activity.RESULT_OK) {
            mFilePath   = mImageCropUri.getPath();
            File file   = new File(mFilePath);

            if (!file.exists()) {
                showToast("Image crop failed");
            } else {
                 bitmap = getBitmap(mFilePath);

            //    mCloseIv.setVisibility(View.VISIBLE);
             //   mPhotoIv.setVisibility(View.VISIBLE);

                  mAvatarIv.setImageBitmap(bitmap);

//                float scale = getResources().getDisplayMetrics().density;
//                int dpAsPixels = (int) (72*scale + 0.5f);
//
//                mCommentEt.setPadding(dpAsPixels, 0, 0, 0);

            }
        } else if (requestCode == PICK_FROM_CAMERA   && resultCode == Activity.RESULT_OK) {
            doCrop();
        }
    }

    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            showToast("Can not find image crop app");

            return;
        } else {
            intent.setData(mImageCaptureUri);

           // intent.putExtra("outputX", 200);
           // intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
           // intent.putExtra("scale", true);
           // intent.putExtra("return-data", true);

            String fileName = StringUtil.getFileName(mImageCaptureUri.getPath());
            String cropFile = Util.getAppDir() + "/crop_" + fileName + ".jpg";

            Debug.i(cropFile);
            Debug.i("File " + mImageCaptureUri.getPath());

            mImageCropUri   = Uri.fromFile(new File(cropFile));

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropUri);

            if (size == 1) {
                Intent i        = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                Intent i        = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            }
        }
    }

    private void showChooserDialog() {
        String[] options = {getString(R.string.text_camera), getString(R.string.text_gallery)};

        CustomArrayAdapter adapter  = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ambil Menggnakan");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int position) {
                if (position == 0) {
                    if (isIntentAvailable(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE)) {
                        Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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
        final Intent intent     = new Intent(action);
        List<ResolveInfo> list  = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }


    public void onBackPressed() {
        Intent backlogin = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(backlogin);
    }

    public class Registerexecute extends AsyncTask<URL, Integer, Long> {
        User user;
        String error = "";

        ProgressDialog progressDlg;

        public Registerexecute() {
            progressDlg = new ProgressDialog(getActivity());

            progressDlg.setMessage("daftar...");
            progressDlg.setCancelable(false);
        }

        protected void onCancelled() {
            mIsLoading = false;

            if (progressDlg.isShowing()) {
                progressDlg.dismiss();
            }
        }

        protected void onPreExecute() {
            mIsLoading = true;

            progressDlg.show();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            if(bitmap ==null){
            }
            else {
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
            }
            byte[] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeBytes(byte_arr);
            String image_name = "IMG_" + timeStamp + ".jpg";
            String path;
            path = Cons.FOTOUSER+image_name;
            Log.e("bit",String.valueOf(bitmap));

            try {
                UserConnection conn = new UserConnection();

                user = conn.daftar(username, password, email, birth,image_str,image_name,path,latitude,longitude);
                result = 1;
            } catch (WisataException e) {
                e.printStackTrace();
                error = e.getError();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }


        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            progressDlg.dismiss();

            mIsLoading = false;
            Toast.makeText(RegisterActivity.this, getString(R.string.text_register_succses),
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

   /* protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }*/

    private void showCalender() {
        String myFormat = "dd/MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBirthEt.setText(sdf.format(myCalendar.getTime()));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    private void postRegister(String Id,String username,String email,String gender, String birth, String password) {
        startCountTime();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap == null) {
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        byte[] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeBytes(byte_arr);
        String image_name = "IMG_" + timeStamp + ".jpg";
        String path;
        String waktu = (String.valueOf(TimeUtil.getUnix()));
        path = Cons.FOTOWISATA + image_name;
        if (mFilePath.equals("")) {
          // registerUser2(Id, username,email,gender,birth,password,waktu);
            registerUser(Id, username,email,gender,birth,password,image_name,image_str,waktu);
        } else {
            registerUser(Id, username,email,gender,birth,password,image_name,image_str,waktu);
        }
    }



    private void registerUser(final String userId, final String fullName, final String email,
                              final String gender,final String birthDate, final String password,final String image_name,final String image_str,final String  waktu) {
        final CustomProgressDialog progressDialog;


        progressDialog = new CustomProgressDialog(getActivity(), getString(R.string.text_sending));
        progressDialog.show();

        //String url = Cons.CONVERSATION_URL+"/register.php";

        String url = "http://dhiva.16mb.com/rest_server/index.php/api/Register";
        final String path;
        path = Cons.FOTOUSER + image_name;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        User user =null;
                        try {
                       /* VolleyParsing parsing = new VolleyParsing();
                        user = parsing.userParsing(response);
                            Log.d("responya",String.valueOf(response));
                            saveUser(user);
                            showToast(getString(R.string.text_welcome) + " " + user.fullName);

                            Intent intent = new Intent(getActivity(), MainActivityBeranda.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/
                            Toast.makeText(RegisterActivity.this,"Daftar Sukses Silakan Login",Toast.LENGTH_SHORT).show();
                        refreshFeed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            },
            new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, getString(R.string.text_register_failed), Toast.LENGTH_SHORT).show();
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
                obj.put(Cons.KEY_NAME, fullName);
                obj.put(Cons.KEY_EMAIL,email);
                obj.put(Cons.KEY_GENDER, gender);
                obj.put(Cons.KEY_BIRTH,birthDate);
                obj.put(Cons.CONV_LATITUDE,latitude);
                obj.put(Cons.CONV_LONGITUDE,longitude);
                obj.put(Cons.KEY_PASSWORD, password);
              //  obj.put(Cons.KEY_HOBBY,"-");
              //  obj.put(Cons.KEY_STATUS,"-");
             //   obj.put(Cons.KEY_MSISDN,"-");
              //  obj.put(Cons.KEY_UPDATED_AT,"-");
             //   obj.put(Cons.KEY_AVATAR,path);
             //   obj.put(Cons.KEY_FOTO,image_str);
            //    obj.put(Cons.IMAGE_NAME,image_name);
                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(strReq);
    }

    private void registerUser2(final String userId, final String fullName, final String email,
                              final String gender,final String birthDate, final String password,final String  waktu) {
        final CustomProgressDialog progressDialog;


        progressDialog = new CustomProgressDialog(getActivity(), getString(R.string.text_sending));
        progressDialog.show();

        String url = Cons.CONVERSATION_URL+"/register2.php";
        final String path;
        path = Cons.FOTOUSER + "ic_profile_blank.png";

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        User user =null;
                        try {
                           /* VolleyParsing parsing = new VolleyParsing();
                            user = parsing.userParsing(response);
                            Log.d("responya",String.valueOf(response));
                            saveUser(user);
                            showToast(getString(R.string.text_welcome) + " " + user.fullName);

                            Intent intent = new Intent(getActivity(), MainActivityBeranda.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/
                            Toast.makeText(RegisterActivity.this,"Daftar Sukses Silakan Login",Toast.LENGTH_SHORT).show();
                            refreshFeed();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, getString(R.string.text_register_failed), Toast.LENGTH_SHORT).show();
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
                obj.put(Cons.KEY_NAME, fullName);
                obj.put(Cons.KEY_EMAIL,email);
                obj.put(Cons.KEY_GENDER, gender);
                obj.put(Cons.KEY_BIRTH,birthDate);
                obj.put(Cons.CONV_LATITUDE,latitude);
                obj.put(Cons.CONV_LONGITUDE,longitude);
                obj.put(Cons.KEY_PASSWORD, password);
                obj.put(Cons.KEY_HOBBY,"-");
                obj.put(Cons.KEY_STATUS,"-");
                obj.put(Cons.KEY_MSISDN,"-");
                obj.put(Cons.KEY_UPDATED_AT,"-");
                obj.put(Cons.KEY_AVATAR,path);
                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(strReq);
    }

    private void refreshFeed() {
        ((BaseActivity) getActivity()).finish();

        Intent intent = new Intent(getActivity(),
                MainActivityBeranda.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        getActivity().startActivity(intent);
    }

    public void permission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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



