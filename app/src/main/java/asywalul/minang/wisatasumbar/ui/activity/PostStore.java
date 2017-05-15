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
import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CropOption;
import asywalul.minang.wisatasumbar.adapter.CropOptionAdapter;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.database.QuestionDb;
import asywalul.minang.wisatasumbar.http.StoreConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.util.Base64;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.NetworkUtils;
import asywalul.minang.wisatasumbar.util.ValueConfig;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Toshiba on 3/12/2016.
 */
public class PostStore extends BaseActivity {

    private ImageView mCloseIv;
    private ImageView mPhotoIv;
    private ImageView attachIv;
    private Button postBtn;
    private RelativeLayout warningView;
    private QuestionDb mQuestionDb;
    AlertDialog dialogg;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    Bitmap bitmap;

    String tagsList[];
    String kategori;
    String namabarang;
    Bitmap bitmappp = null;

    private String mFilePath = "";
    private ArrayList<Integer> mSelectedItems;
    boolean[] isSelectedArray = {false, false, false, false};
    public final static String SP = "sharedAt";
    PostQuestionTask mPostQuestionTask;
    boolean isImageSet = false;
    private TextView etNamaproduk,etKategori,etHarga,etSatuan,etKeterangan;
    String latitude;
    String longitude;
    String image_name;
    String harga;
    String satuan;

    private Timer timer;
    private Handler handler;
    private Runnable runnable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_store);
        enableDatabase();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mQuestionDb = new QuestionDb(getDatabase());

        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mPhotoIv = (ImageView) findViewById(R.id.iv_attachment);
        attachIv = (ImageView) findViewById(R.id.iv_clip);
        postBtn = (Button) findViewById(R.id.btn_send);
        etNamaproduk = (EditText) findViewById(R.id.et_nama_produk);
        etKategori = (EditText)findViewById(R.id.et_kategori_produk);
        etHarga = (EditText)findViewById(R.id.et_harga_produk);
        etSatuan = (EditText)findViewById(R.id.et_satuan_produk);
        etKeterangan = (EditText)findViewById(R.id.et_keterangan_produk);

        warningView = (RelativeLayout) findViewById(R.id.ll_warning);


        etKategori.setFocusableInTouchMode(false);
        etKategori.setFocusable(false);

        etKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showTagsOptions();
            }
        });


        etSatuan.setFocusableInTouchMode(false);
        etSatuan.setFocusable(false);

        etSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                 showTagsOptions2();
            }
        });



        postBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String keterangan = etKeterangan.getText().toString().replaceAll("\\s+", " ").trim();
                kategori = etKategori.getText().toString();
                harga = etHarga.getText().toString();
                namabarang = etNamaproduk.getText().toString();
                satuan = etSatuan.getText().toString();
                Log.i("ISI", keterangan);

                if (kategori.equals("")) {
                    Toast.makeText(PostStore.this, "Kategori Belum Di pilih", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (namabarang.equals("")) {
                    Toast.makeText(PostStore.this, "Nama Product belum disi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (harga.equals("")) {
                    Toast.makeText(PostStore.this, "Harga Product belum disi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (satuan.equals("")) {
                    Toast.makeText(PostStore.this, "Satuan Product belum disi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (keterangan.equals("")) {
                    Toast.makeText(PostStore.this,"Keterangan belum di isi",Toast.LENGTH_SHORT).show();
                    // warningView.setVisibility(View.VISIBLE);
                    return;

                }

                mPostQuestionTask.execute();
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



        final String[] items = new String[]{getString(R.string.text_camera),
                getString(R.string.text_gallery)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.text_take));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), "tmp_avatar_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg"));

                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { // pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_FILE);
                }
            }
        });
        final AlertDialog dialog = builder.create();



        attachIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.show();
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

    public void showAttachment(Bitmap bitmap) {
        mCloseIv.setVisibility(View.VISIBLE);
        mPhotoIv.setVisibility(View.VISIBLE);
        attachIv.setVisibility(View.GONE);

        mPhotoIv.setImageBitmap(bitmap);
    }

    private void showTagsOptions() {

        mSelectedItems = new ArrayList<Integer>();
        tagsList = getResources().getStringArray(R.array.array_kategori);

        CustomArrayAdapter adapter  = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, tagsList);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih Kategori")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        etKategori.setText(tagsList[arg1]);
                    }
                });

        builder.create().show();
    }

    private void showTagsOptions2() {

        mSelectedItems = new ArrayList<Integer>();
        tagsList = getResources().getStringArray(R.array.array_satuan);

        CustomArrayAdapter adapter  = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, tagsList);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih Satuan")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        etSatuan.setText(tagsList[arg1]);
                    }
                });

        builder.create().show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:

                doCrop();
                isImageSet = true;

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    bitmappp = extras.getParcelable("data");
                    Log.d("Get photo", bitmappp.toString());
                    mPhotoIv.setImageBitmap(bitmappp);
                    mCloseIv.setVisibility(View.VISIBLE);
                    mPhotoIv.setVisibility(View.VISIBLE);
                    isImageSet = true;

                }

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists())

                    f.delete();

                break;

        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getActivity().getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getActivity(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null,
                                    null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }





    private boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent     = new Intent(action);
        List<ResolveInfo> list  = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    private void refreshFeed() {
        ((BaseActivity) getActivity()).finish();

        Intent intent = new Intent(getActivity(), MainActivityBeranda.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        getActivity().startActivity(intent);
    }

    public class PostQuestionTask extends AsyncTask<URL, Integer, Long> {
        String error = "";

        CustomProgressDialog progressDlg;

        // User userMode = getUser();

        public PostQuestionTask() {
            progressDlg 	= new CustomProgressDialog(getActivity(),getString(R.string.text_sending));
        }

        protected void onCancelled() {

            progressDlg.dismiss();
        }

        protected void onPreExecute() {

            progressDlg.show();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            //noinspection ResourceType
            kategori = etKategori.getText().toString();
            SharedPreferences getId = getApplicationContext()
                    .getSharedPreferences(SP, 0);
            String iduser = getId.getString("iduser", "Nothing");


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            StoreConnection conn = new StoreConnection();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            if(bitmappp==null){
            }
            else {
                bitmappp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }
            byte[] byte_arr = stream.toByteArray();
            String image_str = Base64.encodeBytes(byte_arr);
            image_name = "IMG_" + timeStamp + ".jpg";
            String path;
            path = Cons.FOTOWISATA+image_name;
            Log.e("bit",String.valueOf(bitmappp));

            // Check for Internet availability
            if (!NetworkUtils.isNetworkAvailable(getActivity())) {
                if (bitmappp==null || image_name.equals("")||image_str.equals("")) {
                    mQuestionDb.update(iduser,namabarang,kategori);
                } else {
                    mQuestionDb.update(iduser,namabarang,kategori,image_name);
                }

                result = 2;
            } else {
                try {
                        conn.postStore(iduser, namabarang, kategori, harga, satuan,image_str, image_name, path);

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
            progressDlg.dismiss();

            if (result == 1) {
                Toast.makeText(PostStore.this,getString(R.string.text_pertanyaan_ditambah),Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();

                refreshFeed();
            } else if (result == 2) {
                Toast.makeText(PostStore.this,getString(R.string.no_connection_for_post),Toast.LENGTH_SHORT).show();

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
