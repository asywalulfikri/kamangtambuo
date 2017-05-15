package asywalul.minang.wisatasumbar.http;

import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.StringUtil;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import asywalul.minang.wisatasumbar.http.core.RestConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;

/**
 * Created by Toshiba on 1/23/2016.
 */
public class UserConnection extends RestConnection {

    public User Detailuser(String iduser, String username, String tgllhair, String foto , String email) throws Exception, WisataException {

        User user = null;

        try {

            String url = Cons.ACCOUNTS_URL + "/detailuser.php?iduser ="+iduser;
            Log.e("url", url);

            /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("email", identifier));
            nameValuePairs.add(new BasicNameValuePair("password", password));
*/
            InputStream is = connectPost(url);

            if (is != null) {
                String response = StringUtil.streamToString(is);

                JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

                JSONArray profile = jsonObj.getJSONArray("data");

                for (int i = 0; i < profile.length(); i++) {

                    JSONObject profileJson = profile.getJSONObject(i);

                    user = new User();

                    user.fullName = profileJson.getString("username");
                    user.userId = profileJson.getString("iduser");
                    user.email = profileJson.getString("email");
                    user.birthDate = profileJson.getString("birth");
                    user.avatar = profileJson.getString("foto");

                }
                is.close();

                throw new WisataException("Response does not contain any data.");
            }

            }catch(Exception e){
                throw e;
            }

            return user;
        }
    public User daftar(String username, String password, String email, String tgllahir, String image,String imagename, String pathh,String latitude, String longitude)
            throws Exception, WisataException {

        Log.e("username", username);
        Log.e("password", password);
        Log.e("email", email);
        Log.e("birth", tgllahir);

        try {
            String url = Cons.CONVERSATION_URL + "/register.php";

            Log.e("xxx", url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("fullName", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("birthDate", tgllahir));
            nameValuePairs.add(new BasicNameValuePair("image_name",imagename));
            nameValuePairs.add(new BasicNameValuePair("foto",image));
            nameValuePairs.add(new BasicNameValuePair("avatar",pathh));
            nameValuePairs.add(new BasicNameValuePair("latitude",latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude",longitude));


            InputStream is = connectPost(url, nameValuePairs);

            if (is != null) {
                String response = StringUtil.streamToString(is);

                Debug.i(response);

                is.close();
            } else {
                throw new WisataException("Response does not contain any data.");
            }

        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    public User editprofile(String id,String username,String email, String tgllahir)
            throws Exception, WisataException {

        Log.e("username", username);
        Log.e("email", email);
        Log.e("birth", tgllahir);

        try {
            String url = Cons.CONVERSATION_URL + "/update_data_user.php";

            Log.e("xxx", url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_ID, id));
            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_NAME, username));
            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_EMAIL, email));
            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_BIRTH, tgllahir));


            InputStream is = connectPost(url, nameValuePairs);

            if (is != null) {
                String response = StringUtil.streamToString(is);

                Debug.i(response);

                is.close();
            } else {
                throw new WisataException("Response does not contain any data.");
            }

        } catch (Exception e) {
            throw e;
        }
        return null;
    }
}

