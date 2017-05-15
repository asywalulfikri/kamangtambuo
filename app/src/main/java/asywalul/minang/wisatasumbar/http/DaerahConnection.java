package asywalul.minang.wisatasumbar.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.http.core.RestConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.model.Daerah;
import asywalul.minang.wisatasumbar.model.DaerahWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.HttpManager1;

/**
 * Created by Toshiba on 3/28/2016.
 */
public class DaerahConnection extends RestConnection {

    public DaerahWrapper getList() throws Exception, WisataException {

        DaerahWrapper wrapper = null;

        try {
            String url = Cons.CONVERSATION_URL+ "/listKabupaten.php";

            String get = HttpManager1.httpGet(url);

            String statusCode = get.substring(get.lastIndexOf("`") + 1, get.length());

            if (!statusCode.equals("200")){
                throw new WisataException("Wrong data or doesnt exists");
            } else {
                String response = get.substring(0, get.lastIndexOf("`"));

                JSONObject object = new JSONObject(get);
                JSONArray List = object.getJSONArray("data");

                int size = List.length();

                wrapper = new DaerahWrapper();
                wrapper.list = new ArrayList<Daerah>();

                for (int i = 0; i < size ; i++) {

                    JSONObject itemJson = List.getJSONObject(i);

                    Daerah daerah		= new Daerah();

                    daerah.idk	            = itemJson.getString(Cons.DAERAH_IDK);
                    daerah.namakabupaten    = itemJson.getString(Cons.DAERAH_KABUPATEN);
                    daerah.namabupati	    = itemJson.getString(Cons.DAERAH_NAMA_BUPATI);
                    daerah.luaswilayah      = itemJson.getString(Cons.DAERAH_LUAS_WILAYAH);
                    daerah.ibukota	        = itemJson.getString(Cons.DAERAH_IBUKOTA);
                    daerah.jumlahkecamatan  = itemJson.getString(Cons.DAERAH_JUMLAH_KECAMATAN);
                    daerah.jumlahwisata     = itemJson.getString(Cons.DAERAH_JUMLAH_KECAMATAN);
                    daerah.foto	            = itemJson.getString(Cons.DAERAH_FOTO);
                    daerah.keterangan       = itemJson.getString(Cons.DAERAH_KETERANGAN);





                    wrapper.list.add(daerah);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }
}
