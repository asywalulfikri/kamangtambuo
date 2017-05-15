package asywalul.minang.wisatasumbar.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Util;

public class LoginActivity extends BaseActivity {

	private String email;
	private String password;
	private AutoCompleteTextView mEmailEt;
	private EditText mPasswordEt;
	private RelativeLayout rlImage;
	private RelativeLayout rlLayout;
	private TextView mLanguage;
	private ImageView ivBackground;
	SharedPreferences sharedAt;
	public final static String SP = "sharedAt";
	private ProgressDialog mDialog;
	User user = null;
	CheckBox cbShowPassword;
	private  Button btnLogin;
	private CallbackManager callbackManager;
	private LoginButton btnFacebook;
	private ImageView logo;
	private boolean mIsLoading = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupLanguage();
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_login);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION);

		mEmailEt = (AutoCompleteTextView) findViewById(R.id.et_identifier);
		mPasswordEt = (EditText) findViewById(R.id.et_password);
		mLanguage = (TextView) findViewById(R.id.tv_language);
		btnLogin = (Button)findViewById(R.id.btn_login);
		logo = (ImageView)findViewById(R.id.rl_image);

		String language = mSharedPref.getString(Cons.USER_LANGUAGE, Cons.LANG_ID);

		mLanguage.setText((language.equals(Cons.LANG_EN) ? getString(R.string.text_english) : getString(R.string.text_indonesia)));

		mLanguage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showLanguageOptions();
			}
		});

		Button register = (Button) findViewById(R.id.btn_register);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginManager.getInstance().logOut();
				Intent daftar = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(daftar);
			}
		});
		FacebookLogin();
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
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				email = mEmailEt.getText().toString().trim();
				password = mPasswordEt.getText().toString().trim();
				if (email.equals("") || password.equals("")) {
					Toast.makeText(LoginActivity.this,getString(R.string.text_emailpassword_null),Toast.LENGTH_LONG).show();
					return;

				} else {
				   loginTask(email,password);
				}
			}
		});
		hideKeyboard();
		cekUser();
		startTootipAnimation();

	}

	private void update_ui(String message) {
		Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
	}
	public void FacebookLogin(){
		callbackManager = CallbackManager.Factory.create();
		btnFacebook = (LoginButton) findViewById(R.id.btn_facebook);
		btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				update_ui("login successfully");

				GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject json, GraphResponse response) {
						if (response.getError() != null) {
							update_ui("error");

						} else {
							String jsonresult = String.valueOf(json);
							System.out.println("JSON Result" + jsonresult);

							try {
								User user = new User();
								user.userId    = json.getString("id");
								user.avatar    = "https://graph.facebook.com/"+json.getString("id")+"/picture?type=large";
								user.fullName  = json.getString("name");
								user.gender    = json.getString("gender");
								user.email     = json.getString("email");
								user.typeLogin = "facebook";

								//loginTask2(user.userId,user.avatar,user.fullName,user.gender,user.email);
								//Log.d("hasil",user.userId+","+user.avatar);

							} catch (JSONException e) {
								e.printStackTrace();
							}

							update_ui(jsonresult);
						}
					}
				});
				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,name,email,gender");
				request.setParameters(parameters);
				request.executeAsync();


			}

			@Override
			public void onCancel() {
				update_ui("CANCELED");
			}

			@Override
			public void onError(FacebookException exception) {
				update_ui("ERROR");
			}
		});
	}

	private void showLanguageOptions() {
		CustomArrayAdapter adapter 	= new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter,
				getResources().getStringArray(R.array.array_lang));
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.title_dialog_changelanguage))
				.setAdapter(adapter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0,int arg1) {
						String[] keys = getResources().getStringArray(R.array.array_lang_key);

						saveDefaultLanguage(keys[arg1]);
						Log.d("bahasa2",keys[arg1]);

						Intent i = new Intent(LoginActivity.this,LoginActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(i);

						finish();
					}
				});

		builder.create().show();
	}

	public void cekUser() {
		sharedAt = getBaseContext()
				.getSharedPreferences(SP, 0);
		String iduser = sharedAt.getString(Cons.KEY_ID, "");

		if (!iduser.equals("")) {
			//goToMain(iduser);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(LoginActivity.this,MainActivityBeranda.class);
		startActivity(intent);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}


	public void loginTask(String email, String password) {
		final ProgressDialog progressDlg;
		progressDlg = new ProgressDialog(getActivity());

		progressDlg.setMessage(getString(R.string.dialog_login));
		progressDlg.setCancelable(false);
		progressDlg.show();

		String url = "http://wisatasumbar.esy.es/restful/login.php?" + "email="
				+ email + "&password="
				+ password;

		StringRequest strReq = new StringRequest(Request.Method.GET,
				url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (progressDlg.isShowing()) {
							progressDlg.dismiss();
						}
						User user = null;
						String error = "";

						Log.e("HERE1", response);

						try {
							user = VolleyParsing.userLoginParsing(response);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (WisataException e) {
							e.printStackTrace();
							error = e.getError();
						}

						if (user != null) {
							saveUser(user);

							Toast.makeText(LoginActivity.this, getString(R.string.text_welcome) + "" + user.fullName, Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(getActivity(), MainActivityBeranda.class);

							intent.putExtra(Util.getIntentName("login"), false);

							startActivity(intent);

							finish();
						}else {
							Toast.makeText(LoginActivity.this,"Username atau password salah", Toast.LENGTH_SHORT).show();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (progressDlg.isShowing()) {
							progressDlg.dismiss();
						}
						String json = null;

						NetworkResponse response = error.networkResponse;

						if (response != null && response.data != null) {
							switch (response.statusCode) {
								case 400:
									json = new String(response.data);
									try {
										JSONObject jsonObj = new JSONObject(json);
										String code = jsonObj.getString("message");
										Log.e("Error", code);
										code = (code.equals("")) ? getString(R.string.text_login_failed) : code;

										if (code.equals("0")) {
											showError("Email atau Password salah!");
										} else if (code.equals("AccountNotRegistered")) {
											//code = code.replace("AccountNotRegistered", getString(R.string.akun_not_registered));
											showError(code);
										} else if (code.equals("IncorrectPassword")) {
											//code = code.replace("IncorrectPassword", getString(R.string.akun_incorrect_password));
											showError(code);
										} else if (code.equals("NotAllowed")) {
											//code = code.replace("NotAllowed", getString(R.string.akun_not_allowed));
											showError(code);
										}

									} catch (JSONException e) {
										e.printStackTrace();
									}

									break;
							}
						}
					}
				});

		RequestQueue queue = Volley.newRequestQueue(getActivity());
		queue.add(strReq);
	}

	/*public void loginTask2(final String userId, final String avatar, final String fullName, final String gender, final String email) {
		final ProgressDialog progressDlg;
		progressDlg = new ProgressDialog(getActivity());

		progressDlg.setMessage(getString(R.string.dialog_login));
		progressDlg.setCancelable(false);
		progressDlg.show();

		String url = "http://wisatasumbar.esy.es/restful/login.php?" + "email="
				+ email + "&userId="
				+ userId;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);

		nameValuePairs.add(new BasicNameValuePair(Cons.KEY_ID,userId.trim()));
		nameValuePairs.add(new BasicNameValuePair(Cons.KEY_NAME,fullName));
		nameValuePairs.add(new BasicNameValuePair(Cons.KEY_AVATAR, avatar));
		nameValuePairs.add(new BasicNameValuePair(Cons.KEY_EMAIL, email));
		nameValuePairs.add(new BasicNameValuePair(Cons.KEY_GENDER, gender));

		StringRequest strReq = new StringRequest(Request.Method.POST,
				url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						User user =null;
						try {
							VolleyParsing parsing = new VolleyParsing();
							user = parsing.userParsing2(response);
							Log.d("responya",String.valueOf(response));
							saveUser(user);
							setResult(RESULT_OK);
							finish();

							refreshFeed();
						} catch (Exception e) {
							e.printStackTrace();
						}
						progressDlg.dismiss();
					}
				},
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						progressDlg.dismiss();
						Toast.makeText(LoginActivity.this, getString(R.string.text_post_question_failed), Toast.LENGTH_SHORT).show();
						VolleyLog.e("Login Facebook", "Error" + error.getMessage());
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
				obj.put(Cons.KEY_AVATAR,avatar);
				return obj;


			}
		};

		strReq.setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		Volley.newRequestQueue(this).add(strReq);
	}
*/

	private void goToMain(String userId) {
		Intent i = new Intent(LoginActivity.this, MainActivityBeranda.class);
		i.putExtra(Cons.KEY_ID, userId);
		startActivity(i);
	}


	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = getCurrentFocus();
		if (v != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}


	private void showDialog() {
		mDialog = new ProgressDialog(LoginActivity.this);
		mDialog.setMessage(getString(R.string.text_loading));
		mDialog.setCancelable(false);
		mDialog.show();
	}

	private void hideDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog.cancel();
		}
	}

	private void startTootipAnimation() {
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.8f);
		scaleY.setDuration(200);

		ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(logo, "scaleY", 1f);
		scaleYBack.setDuration(500);



		scaleYBack.setInterpolator(new BounceInterpolator());
		final AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setStartDelay(500);
		animatorSet.playSequentially(scaleY, scaleYBack);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animatorSet.setStartDelay(500);
				animatorSet.start();
			}
		});
		logo.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		animatorSet.start();
	}

}


