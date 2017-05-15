package asywalul.minang.wisatasumbar.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
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
import asywalul.minang.wisatasumbar.widget.CircleTransform;
import asywalul.minang.wisatasumbar.widget.FullImageActivity;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

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

/**
 * Created by ferissadjohan on 5/14/16.
 */
public class ProfileActivity extends BaseActivity {

    private String id,username,avatar,email,tgllahir;
    private TextView userName,Email,tglLahir;
    private SimpleDraweeView Avatar;
    private ImageView background;
    private FloatingActionButton mbtnEdit,takeImage;
    private User user;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Uri mImageCaptureUri;
    private Uri mImageCropUri;
    private String mFilePath = "";
    Bitmap bitmappp = null;
    private TextView hobi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_scrool);

        user = getUser();
        userName   = (TextView)findViewById(R.id.tv_user_name);
        Avatar     = (SimpleDraweeView) findViewById(R.id.iv_avatar);
        Email      = (TextView)findViewById(R.id.tv_email);
        tglLahir   = (TextView)findViewById(R.id.tv_tgl_lahir);
        mbtnEdit   = (FloatingActionButton)findViewById(R.id.fab);
        background = (ImageView)findViewById(R.id.iv_background);
        hobi       = (TextView)findViewById(R.id.hobi);
        takeImage  = (FloatingActionButton) findViewById(R.id.fab2);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(username);
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
        Email.setText(user.email);
        tglLahir.setText(user.birthDate);
        userName.setText(user.fullName);
        hobi.setText(user.hobby);

       /* Picasso.with(getApplicationContext())
                .load(user.avatar.equals("") ? null : user.avatar)
                .placeholder(R.color.text_color_white)
                .error(R.drawable.ic_profile_blank)
                .transform(new CircleTransform())
                .into(Avatar);*/
        Uri path = Uri.parse(user.avatar);
        Avatar.setImageURI(path);

        Picasso.with(getApplicationContext())
                .load(user.avatar.equals("") ? null : user.avatar)
                .transform(new BlurTransformation(getApplicationContext(),25,1))
                .placeholder(R.color.text_color_white)
                .error(R.drawable.ic_profile_blank)
                .into(background);

        Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buildVersion()) {
                    Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);
                    intent.putExtra("imageFull",user.avatar);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            new Pair<View, String>(view.findViewById(R.id.iv_avatar), getString(R.string.shared_element_image_cover)));
                    ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
                }else{
                    Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);

                    intent.putExtra("imageFull",user.avatar);
                    startActivity(intent);
                }
            }
        });

        takeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragesPermission();
                } else {
                    //showChooserDialog();
                    Intent intent = new Intent(ProfileActivity.this, CameraActivity.class);
                    startActivityForResult(intent,1313);
                }
            }
        });


        mbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editprofile = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(editprofile);
            }
        });
    }




 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    public void onBackPressed() {
        setResult(Activity.RESULT_OK,
                new Intent().putExtra("avatar", "avatar"));
        finish();
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
                    postQuestionTask();
                }
        } else if (requestCode == CROP_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            mFilePath = mImageCropUri.getPath();
            File file = new File(mFilePath);

            if (!file.exists()) {
                showToast("Image crop failed");
            } else {
                bitmappp = getBitmap(mFilePath);
                postQuestionTask();
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

    private void postQuestionTask() {
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

        if (mFilePath.equals("")) {

        } else {
           updateAvatar(image_name,image_str);
        }
    }


    private void updateAvatar( final String image_name, final String image_str) {
        final CustomProgressDialog progressDialog;
        progressDialog = new CustomProgressDialog(getActivity(), getString(R.string.text_sending));
        progressDialog.show();

        String url = Cons.CONVERSATION_URL+"/updateprofiluser.php?iduser="+user.userId;
        final String path;
        path = Cons.FOTOUSER + image_name;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        User user = null;
                        String error = "";
                        try {
                            user = VolleyParsing.userLoginParsing(response);
                            Log.d("apaya",response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (WisataException e) {
                            e.printStackTrace();
                            error = e.getError();
                        }
                        if (user != null) {
                            saveUser(user,path);
                            progressDialog.dismiss();
                            updateAvatar(path);
                           /* Toast.makeText(ProfileActivity.this, getString(R.string.text_welcome) + "" + user.fullName, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivityBeranda.class);

                            intent.putExtra(Util.getIntentName("login"), false);

                            startActivity(intent);

                            finish();*/
                        }else {
                            Toast.makeText(ProfileActivity.this,"Gagal Merubah FOtok", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, getString(R.string.text_updated_avatar_failed), Toast.LENGTH_SHORT).show();
                VolleyLog.e("Gagal Update Foto Profile", "Error" + error.getMessage());
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
                obj.put("foto", image_str);
                obj.put("image_name",image_name);
                obj.put("avatar", path);
                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(strReq);
    }

    private void updateAvatar(final String path){
        Uri uri = Uri.parse(path);
        Avatar.setImageURI(uri);

        Picasso.with(getApplicationContext())
                .load(path.equals("") ? null : path)
                .transform(new BlurTransformation(getApplicationContext(),25,1))
                .placeholder(R.color.text_color_white)
                .error(R.drawable.ic_profile_blank)
                .into(background);

        Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buildVersion()) {
                    Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);
                    intent.putExtra("imageFull",path);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            new Pair<View, String>(view.findViewById(R.id.iv_avatar), getString(R.string.shared_element_image_cover)));
                    ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
                }else{
                    Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);

                    intent.putExtra("imageFull",path);
                    startActivity(intent);
                }
            }
        });
    }


}