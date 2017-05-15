package asywalul.minang.wisatasumbar.http;

import asywalul.minang.wisatasumbar.model.Kuliner;
import asywalul.minang.wisatasumbar.model.KulinerWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.HttpManager1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.http.core.RestConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;

/**
 * Created by Toshiba on 3/27/2016.
 */
public class KulinerConnection  extends RestConnection {


    public KulinerWrapper getList() throws Exception, WisataException {

        KulinerWrapper wrapper = null;

        try {
            String url = Cons.CONVERSATION_URL + "/listKuliner.php";

            String get = HttpManager1.httpGet(url);

            String statusCode = get.substring(get.lastIndexOf("`") + 1, get.length());

            if (!statusCode.equals("200")) {
                throw new WisataException("Wrong data or doesnt exists");
            } else {
                String response = get.substring(0, get.lastIndexOf("`"));

                JSONObject object = new JSONObject(get);
                JSONArray List = object.getJSONArray("data");

                int size = List.length();

                wrapper = new KulinerWrapper();
                wrapper.list = new ArrayList<Kuliner>();

                for (int i = 0; i < size; i++) {

                    JSONObject itemJson = List.getJSONObject(i);

                    Kuliner kuliner = new Kuliner();

                    kuliner.iduser = itemJson.getString("iduser");
                    kuliner.idkuliner =itemJson.getString("idkuliner");
                    kuliner.namakuliner =itemJson.getString("namakuliner");
                    kuliner.foto  =itemJson.getString("foto");
                    kuliner.foto1=itemJson.getString("foto1");
                    kuliner.foto2 =itemJson.getString("foto2");
                    kuliner.foto3 =itemJson.getString("foto3");
                    kuliner.keterangan =itemJson.getString("keterangan");




                    wrapper.list.add(kuliner);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }
}

