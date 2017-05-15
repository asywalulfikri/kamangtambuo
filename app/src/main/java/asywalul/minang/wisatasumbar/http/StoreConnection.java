package asywalul.minang.wisatasumbar.http;

import asywalul.minang.wisatasumbar.model.Question;
import asywalul.minang.wisatasumbar.model.Store;
import asywalul.minang.wisatasumbar.model.StoreWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.HttpManager1;
import asywalul.minang.wisatasumbar.util.StringUtil;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import asywalul.minang.wisatasumbar.http.core.RestConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;

/**
 * Created by Toshiba on 3/27/2016.
 */
public class StoreConnection  extends RestConnection {


    public StoreWrapper getList() throws Exception, WisataException {

        StoreWrapper wrapper = null;

        try {
            String url = Cons.CONVERSATION_URL + "/listStore.php";

            String get = HttpManager1.httpGet(url);

            String statusCode = get.substring(get.lastIndexOf("`") + 1, get.length());

            if (!statusCode.equals("200")) {
                throw new WisataException("Wrong data or doesnt exists");
            } else {
                String response = get.substring(0, get.lastIndexOf("`"));

                JSONObject object = new JSONObject(get);
                JSONArray List = object.getJSONArray("data");

                int size = List.length();

                wrapper = new StoreWrapper();
                wrapper.list = new ArrayList<Store>();

                for (int i = 0; i < size; i++) {

                    JSONObject itemJson = List.getJSONObject(i);

                    Store store = new Store();

                    store.iduser = itemJson.getString("iduser");
                    store.idbarang = itemJson.getString("idbarang");
                    store.namabarang = itemJson.getString("namabarang");
                    store.hargabarang = itemJson.getInt("hargabarang");
                    store.satuan = itemJson.getString("satuan");
                    store.foto = itemJson.getString("foto");
                    store.keterangan = itemJson.getString("keterangan");
                    store.pathh = itemJson.getString("pathh");
                    store.lokasi = itemJson.getString("lokasi");

                    store.user = new User();

                    store.user.userId = itemJson.getString("iduser");
                    store.user.fullName = itemJson.getString("username");
                    store.user.avatar = itemJson.getString("pathuser");
                    store.user.email = itemJson.getString("email");

                    wrapper.list.add(store);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }


    public Question postStore(String iduser,String namabarang, String kategori, String harga, String satuan, String keterangan,
                              String image_name,String path)
            throws Exception, WisataException {

        try {
            Debug.i("Posting question...");
            Log.e("iduser", iduser);
            Log.e("namabarang",namabarang);
            Log.e("kategori",kategori);
            Log.e("harga",harga);
            Log.e("satuan",satuan);
            Log.e("ket",keterangan);
            Log.e("image_n",image_name);
            Log.e("path",path);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("iduser", iduser));
            nameValuePairs.add(new BasicNameValuePair("namabarang",namabarang));
            nameValuePairs.add(new BasicNameValuePair("kategoribarang", kategori));
            nameValuePairs.add(new BasicNameValuePair("hargabarang", harga));
            nameValuePairs.add(new BasicNameValuePair("satuan", satuan));
            nameValuePairs.add(new BasicNameValuePair("keterangan",keterangan));
            nameValuePairs.add(new BasicNameValuePair("foto",image_name));
            nameValuePairs.add(new BasicNameValuePair("pathh",path));

            String url = Cons.CONVERSATION_URL+"/postStore.php";

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
