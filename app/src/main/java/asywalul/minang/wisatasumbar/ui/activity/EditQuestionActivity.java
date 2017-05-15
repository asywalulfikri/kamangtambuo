package asywalul.minang.wisatasumbar.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.database.QuestionDb;
import asywalul.minang.wisatasumbar.http.QuestionConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.util.Base64;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.LoadingAlertDialog;
import asywalul.minang.wisatasumbar.util.NetworkUtils;
import asywalul.minang.wisatasumbar.util.StringUtil;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.util.ValueConfig;

/**
 * Created by Toshiba on 3/12/2016.
 */
public class EditQuestionActivity extends BaseActivity {

    private ImageView mCloseIv;
    private ImageView mPhotoIv;
    private ImageView attachIv;
    private EditText tagsEt;
    private Button postBtn;
    private RelativeLayout warningView;
    private QuestionDb mQuestionDb;
    AlertDialog dialogg;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    Bitmap bitmap;
    String waktu;
    String a;
    String tagsList[];
    String tags;
    Bitmap bitmappp = null;

    int inWidth = 0;
    int inHeight = 0;


    private String mFilePath = "";
    private ArrayList<Integer> mSelectedItems;
    boolean[] isSelectedArray = {false, false, false, false};
    public final static String SP = "sharedAt";
    PostQuestionTask mPostQuestionTask;
    boolean isImageSet = false;
    String latitude;
    String longitude;
    private Uri mImageCaptureUri;
    private Uri mImageCropUri;
    private Timer timer;
    private Handler handler;
    private Runnable runnable;
    private  String tagss;
    private String pertanyaan;
    private EditText contentEt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);
        enableDatabase();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Time today = new Time();
        today.setToNow();
        String dd = (today.monthDay + "");             // Day of the month (1-31)
        String mm = (today.month + "");              // Month (0-11)
        String yy = (today.year + "");                // Year
        String hh = (today.format("%k:%M:%S"));  // Current time
        tagss = getIntent().getStringExtra("tags");
        pertanyaan = getIntent().getStringExtra("pertanyaan");

        Log.d("asep",tagss+","+pertanyaan);
        if (mm.equals("1")) {
            a = "02";
        }
        if (mm.equals("2")) {
            a = "03";
        }
        if (mm.equals("3")) {
            a = "04";
        }
        if (mm.equals("4")) {
            a = "05";
        }
        if (mm.equals("5")) {
            a = "06";
        }
        if (mm.equals("6")) {
            a = "7";
        }
        if (mm.equals("7")) {
            a = "8";
        }

        if (mm.equals("8")) {
            a = "9";
        }
        if (mm.equals("0")) {
            a = "10";
        }

        if (mm.equals("10")) {
            a = "11";
        }
        if (mm.equals("11")) {
            a = "12";
        }if(mm.equals("0")){
            a = "1";
        }

        waktu = yy+"-"+a+"-"+dd+" "+hh;

        Log.d("waktu",waktu);

        mQuestionDb = new QuestionDb(getDatabase());

        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mPhotoIv = (ImageView) findViewById(R.id.iv_attachment);
        attachIv = (ImageView) findViewById(R.id.iv_clip);
        postBtn = (Button) findViewById(R.id.btn_send);

        tagsEt = (EditText) findViewById(R.id.et_tags);
        tagsEt.setText(tagss);

        warningView = (RelativeLayout) findViewById(R.id.ll_warning);

        contentEt = (EditText) findViewById(R.id.et_content);
        contentEt.setText(pertanyaan);

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
                    Toast.makeText(EditQuestionActivity.this, "Pertanyaan Belum Di tulis", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tags.equals("")) {
                    Toast.makeText(EditQuestionActivity.this,"Tags belum di pilih",Toast.LENGTH_SHORT).show();
                   // warningView.setVisibility(View.VISIBLE);
                    return;

                }

                (mPostQuestionTask = new PostQuestionTask(content)).execute();
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
               showChooserDialog();
            }
        });
    }






    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        mLocation.beginUpdates();
        mQuestionDb.reload(getDatabase());
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
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

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b,450,
                        450, true);
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
        } else if (requestCode == CROP_FROM_CAMERA  && resultCode == Activity.RESULT_OK) {
            mFilePath   = mImageCropUri.getPath();
            File file   = new File(mFilePath);

            if (!file.exists()) {
                showToast("Image crop failed");
            } else {
                bitmappp = getBitmap(mFilePath);

                //    mCloseIv.setVisibility(View.VISIBLE);
                //   mPhotoIv.setVisibility(View.VISIBLE);
                mPhotoIv.setImageBitmap(bitmappp);
                mCloseIv.setVisibility(View.VISIBLE);
                mPhotoIv.setVisibility(View.VISIBLE);

//               float scale = getResources().getDisplayMetrics().density;
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

          //  intent.putExtra("outputX",  500);
         //   intent.putExtra("outputY", 200);
              intent.putExtra("aspectX", 1);
              intent.putExtra("aspectY", 1);
        //    intent.putExtra("scale", true);
        //    intent.putExtra("return-data", true);

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

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

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
    public void showAttachment(Bitmap bitmap) {
        mCloseIv.setVisibility(View.VISIBLE);
        mPhotoIv.setVisibility(View.VISIBLE);
        attachIv.setVisibility(View.GONE);

        mPhotoIv.setImageBitmap(bitmap);
    }

    private void showTagsOptions() {

        mSelectedItems = new ArrayList<Integer>();
        tagsList = new String[]
                {getString(R.string.text_wisata),getString(R.string.text_kuliner),getString(R.string.text_budaya),getString(R.string.text_lokasi)};

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
        ((BaseActivity) getActivity()).finish();

        Intent intent = new Intent(getActivity(), MainActivityBeranda.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        getActivity().startActivity(intent);
    }

    public class PostQuestionTask extends AsyncTask<URL, Integer, Long> {
        String content,error = "";

      //  CustomProgressDialog progressDlg;
      final LoadingAlertDialog alert = new LoadingAlertDialog(EditQuestionActivity.this);
       // User userMode = getUser();

        public PostQuestionTask(String content) {
            this.content	= content;

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
            String iduser = getId.getString("iduser", "Nothing");


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            QuestionConnection conn = new QuestionConnection();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            if(bitmappp==null){
            }
            else {
                bitmappp.compress(Bitmap.CompressFormat.JPEG,100, stream);
            }
            byte[] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeBytes(byte_arr);
            String image_name = "IMG_" + timeStamp + ".jpg";
            String path;
            path = Cons.FOTOWISATA+image_name;
            Log.e("bit",String.valueOf(bitmappp));




           // Check for Internet availability
            if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                if (bitmappp==null || image_name.equals("")||image_str.equals("")) {
                    mQuestionDb.update(iduser,content,tags);
                } else {
                    mQuestionDb.update(iduser,content,tags,image_name);
                }

                result = 2;
            } else {
                try {

                    if (bitmappp ==null || image_name.equals("")||image_str.equals("")) {
                        conn.postQuestion(iduser,content, tags,waktu,latitude,longitude);
                        result 	= 1;
                    } else {
                        conn.postQuestion(iduser, content, tags,image_str,image_name,path,waktu,latitude,longitude);
                    }
                    result 	= 1;
                } catch (WisataException e) {
                    e.printStackTrace();
                    error  = e.getError();
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
                Toast.makeText(EditQuestionActivity.this,getString(R.string.text_pertanyaan_ditambah),Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();

                refreshFeed();
            } else if (result == 2) {
                Toast.makeText(EditQuestionActivity.this,getString(R.string.no_connection_for_post),Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();

                refreshFeed();
            } else {
                error = (error.equals("")) ? getString(R.string.text_post_question_failed) : error;

                showError(error);
            }
        }
    }
}
