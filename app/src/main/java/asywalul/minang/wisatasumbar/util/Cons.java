package asywalul.minang.wisatasumbar.util;

/**
 * Created by Toshiba on 3/13/2016.
 */
public class Cons {
    public static final boolean ENABLE_DEBUG = true;
    public static final String TAG = "Wisatasumbar";
    public static final int HTTP_CONNECTION_TIMEOUT = 30000;
   // public static final String CONVERSATION_URL2= "http://dhiva.16mb.com/rest_server/index.php/api/";
    public static final String CONVERSATION_URL = "http://dhiva.16mb.com/rest_server/index.php/api/";
    public static final String FOTOWISATA = "http://dhiva.16mb.com/restful/fotoquestion/";
    public static final String FOTOUSER = "http://dhiva.16mb.com/restful/fotouser/";
    public static final String URL_DETAIL_USER   = "http://dhiva.16mb.com/restful/detailUser.php?userId=";
    public static final String URL_API_GOOGLE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    public static final String PACKAGE_PREFIX = "android.minang";
    public static final String USER_LANGUAGE = "user_language";
    public static final String LANG_ID = "id";
    public static final String LANG_EN = "en";
    public static final String LANG_MN = "zsm";
    public static final int RESULT_CLOSE_ALL = 1001;
    public static final String ACCOUNTS_URL = "http://dhiva.16mb.com/restful";
    public static final String INBOUND_URL = "http://8villages.com/inbound";
    public static final String PRIVATE_PREF = "wisatasumbar_pref";
    public static final String SESSION_PREF = "session_pref";
    public static final String COORDINATE_PREF = "coordinate_pref";

    public static final String DBNAME = "wisatasumbar.db";
    public static final String DBPATH = "/data/data/sumbarrancak.padang.minang/";
    public static final String APP_DIR = "/.minang";

    public static final int DB_VERSION = 5;
    public static final String DBVERSION_KEY = "dbversion";

    public static final String API_KEY = "AIzaSyAwxSpIUZamaTh9iRXlb5-WAodYwLyJJkQ";
    public static final String GOOGLE_MAP_APIKEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    public static final String ANDROID_VERSION = "androidVersion";
    public static final String APP_VERSION = "appVersion";


    // Login Table Columns names
    public static final String KEY_ID          = "userId";
    public static final String KEY_NAME        = "fullName";
    public static final String KEY_EMAIL       = "email";
    public static final String KEY_PASSWORD    = "password";
    public static final String KEY_BIRTH       = "birthDate";
    public static final String KEY_AVATAR      = "avatar";
    public static final String KEY_CREATED_AT  = "createdAt";
    public static final String KEY_HOBBY       = "hobby";
    public static final String KEY_GENDER      = "gender";
    public static final String KEY_STATUS      = "status";
    public static final String KEY_MSISDN      = "msisdn";
    public static final String KEY_UPDATED_AT  = "updatedAt";
    public static final String KEY_LATITUDE    = "latitude";
    public static final String KEY_LONGITUDE   = "longitude";
    public static final String KEY_IS_LOGIN    = "isLogin";
    public static final String KEY_TYPE_LOGIN  = "typeLogin";
    public static final String KEY_FOTO        = "foto";
    public static final String IMAGE_NAME      = "image_name";


    //Conversation names
    public static final String CONV_ID               = "conversationId";
    public static final String CONV_TITLE            = "title";
    public static final String CONV_CONTENT          = "content";
    public static final String CONV_ISVOTED          = "isVoted";
    public static final String CONV_TOTAL_RESPONSES  = "totalResponses";
    public static final String CONV_TOTAL_VOTE       = "totalVotes";
    public static final String CONV_ATTACHMENT       = "attachment";
    public static final String CONV_CREATED_AT       = "createdAt";
    public static final String CONV_TYPE             = "type";
    public static final String CONV_DATE_SUBMITTED   = "dateSubmitted";
    public static final String CONV_TAGS             = "tags";
    public static final String CONV_TOTAL_VIEW       = "totalViews";
    public static final String CONV_LATITUDE         = "latitude";
    public static final String CONV_LONGITUDE        = "longitude";
    public static final String CONV_SUMMARY          = "summary";
    public static final String CONV_TIME             = "time";

    //Video names
    public static final String VIDEO_ID             = "videoId";
    public static final String VIDEO_URL            = "url";
    public static final String VIDEO_DATESUBMITTED  = "dateSubmitted";
    public static final String VIDEO_CATEGORY       = "category";
    public static final String VIDEO_AVATAR         = "avatar";
    public static final String VIDEO_TITLE          = "title";
    public static final String VIDEO_TOTAL_VIWE     = "totalView";
    public static final String VIDEO_CHANNEL        = "channel";
    public static final String VIDEO_IS_WATCH       = "isWatch";

    //Articles names
    public static final String ARTICLES_ID             = "articlesId";
    public static final String ARTICLES_URL            = "sumber_url";
    public static final String ARTICLES_IMAGE_1        = "image_satu";
    public static final String ARTICLES_IMAGE_2        = "image_dua";
    public static final String ARTICLES_IMAGE_3        = "image_tiga";
    public static final String ARTICLES_DATE_SUBMITTED = "dateSubmitted";
    public static final String ARTICLES_CATEGORY       = "category";
    public static final String ARTICLES_TYPE           = "type";
    public static final String ARTICLES_TITLE          = "title";
    public static final String ARTICLES_LATITUDE       = "latitude";
    public static final String ARTICLES_LONGITUDE      = "longitude";
    public static final String ARTICLES_CONTENT        = "content";
    public static final String ARTICLES_TAGS           = "tags";
    public static final String ARTICLES_SUMMARY        = "summary";



    public static final String STORY_ID= "storyId";

    public static final String WISATA_ID              = "wisataId";
    public static final String WISATA_CONTENT         = "content";
    public static final String WISATA_TITLE           = "title";
    public static final String WISATA_DATE_SUBMITTED  = "dateSubmitted";
    public static final String WISATA_LOCATION        = "location";
    public static final String WISATA_CATEGORY        = "category";
    public static final String WISATA_LATITUDE        = "latitude";
    public static final String WISATA_LONGITUDE       = "longitude";
    public static final String WISATA_SUMBER_URL      = "sumber_url";
    public static final String WISATA_TAGS            = "tags";
    public static final String WISATA_ATTACHMENT      = "attachment";
    public static final String WISATA_DAERAH          = "daerah";
    public static final String WISATA_SUMMARY         = "summary";

    public static final String RESPONSE_ID = "id_comment";


    //Location Nearby
    public static final String NEARBY_ID       = "id";
    public static final String NEARBY_NAME     = "name";
    public static final String NEARBY_JAMBUKA  = "open_now";
    public static final String NEARBY_ICON     = "icon";
    public static final String NEARBY_TYPE     = "type";
    public static final String NEARBY_LOCATION = "vicinity";
    public static final String NEARBY_LATITUDE = "lat";
    public static final String NEARBY_LONGITUDE = "lng";
    public static final String NEARBY_FOTO = "photo_reference";


    //Store
    public static final String STORE_ID              = "idbarang";
    public static final String STORE_CATEGORY        = "kategoribarang";
    public static final String STORE_NAME            = "namabarang";
    public static final String STORE_PRICE           = "hargabarang";
    public static final String STORE_UNIT            = "satuan";
    public static final String STORE_ATTACHMENT      = "foto";
    public static final String STORE_PATH            = "pathh";
    public static final String STORE_INFORMATON      = "keterangan";
    public static final String STORE_LOCATION        = "lokasi";
    public static final String STORE_DATE_SUBMITTED  = "time";
    public static final String STORE_LATITUDE        = "latitude";
    public static final String STORE_LONGITUDE       = "longitude";

    //Angkutan
    public static final String ANGKUTAN_ID               = "idangkutan";
    public static final String ANGKUTAN_NAME             = "nama_perusahaan";
    public static final String ANGKUTAN_PEMILIK          = "pemilik";
    public static final String ANGKUTAN_ALAMAT           = "alamat";
    public static final String ANGKUTAN_JUMLAH_KENDARAAN = "jumlah_kendaraan";
    public static final String ANGKUTAN_MEREK            = "merek";
    public static final String ANGKUTAN_KATEGORI         = "kategori";
    public static final String ANGKUTAN_KETERANGAN       = "keterangan";
    public static final String ANGKUTAN_FOTO             = "foto";

    public static final String DAERAH_IDK               = "idk";
    public static final String DAERAH_KABUPATEN         = "nama_kabupaten";
    public static final String DAERAH_NAMA_BUPATI       = "nama_bupati";
    public static final String DAERAH_LUAS_WILAYAH      = "luas_wilayah";
    public static final String DAERAH_IBUKOTA           = "ibukota";
    public static final String DAERAH_JUMLAH_KECAMATAN  = "jumlah_kecamatan";
    public static final String DAERAH_WISATA            = "jumlah_wisata";
    public static final String DAERAH_FOTO              = "foto";
    public static final String DAERAH_KETERANGAN        = "keterangan";

    public static final String CERITA_ID            = "id";
    public static final String CERITA_TITLE         = "title";
    public static final String CERITA_CONTENT       = "content";
    public static final String CERITA_CREATED_AT    = "createdAt";
    public static final String CERITA_TYPE          = "type";
    public static final String CERITA_IMAGE         = "image";
    public static final String CERITA_SUMBER        = "sumber";
    public static final String CERITA_CATEGORY      = "category";

    public static final String RESEP_ID         = "idresep";
    public static final String RESEP_TITLE      = "title";
    public static final String RESEP_CONTENT_1  = "content1";
    public static final String RESEP_CONTENT_2  = "content2";
    public static final String RESEP_IMAGE_1    = "image1";
    public static final String RESEP_IMAGE_2    = "image2";
    public static final String RESEP_SUMBER     = "sumber_url";
    public static final String RESEP_DATE_SUBMITTED = "dateSubmitted";



    public static final String TYPE_QUESTIONS = "questions";
    public static final String TYPE_ARTICLES = "articles";

    public static final String TIME_UNIX = "time_unix";

    public static final String CONVERSATION= "conversation";
    public static final String IKLAN_LOGOUT = "ca-app-pub-4914903732265878/4371983347";



}
