package asywalul.minang.wisatasumbar.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by asywalul fikri on 5/14/16.
 */
public class EditProfileActivity extends BaseActivity {

    private EditText editTextUsername, editTexttgllahir,editTextstatus,editTexthobby,editTextgender;
    private Button buttonSave;
    ImageView btnBack;
    Calendar myCalendar = Calendar.getInstance();
    private String username, id, tgllahir, email,avatar,status,hobby,gender;
    private boolean mIsLoading = false;
    private  User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = getUser();

        editTextUsername = (EditText) findViewById(R.id.usernameregister);
        editTexttgllahir = (EditText) findViewById(R.id.tgl_lahir);
        editTextstatus = (EditText)findViewById(R.id.status);
        editTexthobby = (EditText)findViewById(R.id.hobby);
        editTextgender = (EditText)findViewById(R.id.gender);
        btnBack        = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editTextgender.setFocusableInTouchMode(false);
        editTextgender.setFocusable(false);
        editTextgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderOptions();
                hideKeyboard();
            }
        });

        editTexttgllahir.setFocusableInTouchMode(false);
        editTexttgllahir.setFocusable(false);

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


        editTexttgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        editTextUsername.setText(user.fullName);
        editTexttgllahir.setText(user.birthDate);
        editTextstatus.setText(user.status);
        editTexthobby.setText(user.hobby);
        editTextgender.setText(user.gender);


        buttonSave = (Button) findViewById(R.id.btnRegister);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = editTextUsername.getText().toString().trim();
                tgllahir = editTexttgllahir.getText().toString().trim();
                status   = editTextstatus.getText().toString().trim();
                hobby    = editTexthobby.getText().toString().trim();
                gender   = editTextgender.getText().toString().trim();

                if (username.equals("")) {
                    Toast.makeText(EditProfileActivity.this,getString(R.string.text_emailpassword_null), Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    EditProfileTask(username,tgllahir,status,hobby,gender);
                }
            }
        });

    }
        private void showCalender() {
        String myFormat = "dd/MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTexttgllahir.setText(sdf.format(myCalendar.getTime()));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    private void refreshFeed() {
        ((BaseActivity) getActivity()).finish();

        Intent intent = new Intent(getActivity(),
                MainActivityBeranda.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        getActivity().startActivity(intent);
    }
    private void showGenderOptions() {
        final String[] genders = {getString(R.string.text_male), getString(R.string.text_female)};

        AlertDialog.Builder builder = getBuilder(getActivity());
        builder.setTitle(getString(R.string.title_dialog_gender))
                .setItems(genders, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editTextgender.setText(genders[which]);
                        Toast.makeText(getActivity(), editTextgender.getText(), Toast.LENGTH_SHORT).show();
                    }
                });

        builder.create().show();
    }



    private void EditProfileTask(final String username, final String tgllahir, final String status, final String hobby,final String gender) {
        final CustomProgressDialog progressDialog;
        progressDialog = new CustomProgressDialog(getActivity(), getString(R.string.text_sending));
        progressDialog.show();
        String url = Cons.CONVERSATION_URL+"/update_data_user.php";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            user = parsing.userParsing(response);
                            saveUser(user);
                            Toast.makeText(EditProfileActivity.this, getString(R.string.text_edit_profile_succes), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();

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
                Toast.makeText(EditProfileActivity.this, getString(R.string.text_edit_profile_failed), Toast.LENGTH_SHORT).show();
                VolleyLog.e("Edit profile", "Error" + error.getMessage());
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
                obj.put(Cons.KEY_ID, user.userId);
                obj.put(Cons.KEY_NAME, username);
                obj.put(Cons.KEY_STATUS,status);
                obj.put(Cons.KEY_HOBBY,hobby);
                obj.put(Cons.KEY_BIRTH,tgllahir);
                obj.put(Cons.KEY_GENDER,gender);
                obj.put(Cons.KEY_EMAIL,user.email);
                obj.put(Cons.KEY_AVATAR,user.avatar);
                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(strReq);
    }
}
