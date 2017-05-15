package asywalul.minang.wisatasumbar.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.model.Angkutan;
import asywalul.minang.wisatasumbar.model.AngkutanWrapper;
import asywalul.minang.wisatasumbar.model.Articles;
import asywalul.minang.wisatasumbar.model.ArticlesWrapper;
import asywalul.minang.wisatasumbar.model.Cerita;
import asywalul.minang.wisatasumbar.model.CeritaWrapper;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.ConversationWrapper;
import asywalul.minang.wisatasumbar.model.Daerah;
import asywalul.minang.wisatasumbar.model.DaerahWrapper;
import asywalul.minang.wisatasumbar.model.Resep;
import asywalul.minang.wisatasumbar.model.ResepWrapper;
import asywalul.minang.wisatasumbar.model.Story;
import asywalul.minang.wisatasumbar.model.StoryWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Video;
import asywalul.minang.wisatasumbar.model.VideoWrapper;
import asywalul.minang.wisatasumbar.model.Wisata;
import asywalul.minang.wisatasumbar.model.WisataWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;

/**
 * Created by Toshiba on 4/8/2016.
 */
public class CacheDb extends Database {

    public CacheDb(SQLiteDatabase sqlite) {
        super(sqlite);
    }

    public ConversationWrapper getListQuestion(String cid) {
        ConversationWrapper wrapper = null;
        wrapper 	 = new ConversationWrapper();
        wrapper.list = new ArrayList<Conversation>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM cache_questions where cid = '"+cid+"'";
        Debug.i(sql);
        Cursor c 	= mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new ConversationWrapper();

                wrapper.list 			= new ArrayList<Conversation>();

                while (c.isAfterLast()  == false) {
                    Conversation conv 		= new Conversation();

                    conv.conversationId		= c.getString(c.getColumnIndex(Cons.CONV_ID));
                    conv.content	 		= c.getString(c.getColumnIndex(Cons.CONV_CONTENT));
                    conv.tags   			= c.getString(c.getColumnIndex(Cons.CONV_TAGS));
                    conv.attachment         = c.getString(c.getColumnIndex(Cons.CONV_ATTACHMENT));
                    conv.dateSubmitted      = c.getString(c.getColumnIndex(Cons.CONV_DATE_SUBMITTED));
                    conv.totalResponses     = c.getInt   (c.getColumnIndex(Cons.CONV_TOTAL_RESPONSES));
                    conv.totalVotes         = c.getInt   (c.getColumnIndex(Cons.CONV_TOTAL_VOTE));
                    conv.isVoted            = c.getInt   (c.getColumnIndex(Cons.CONV_ISVOTED));
                    conv.type               = c.getString(c.getColumnIndex(Cons.CONV_TYPE));
                    conv.title              = c.getString(c.getColumnIndex(Cons.CONV_TITLE));
                    conv.summary            = c.getString(c.getColumnIndex(Cons.CONV_SUMMARY));
                    conv.latitude           = c.getString(c.getColumnIndex(Cons.CONV_LATITUDE));
                    conv.longitude          = c.getString(c.getColumnIndex(Cons.CONV_LONGITUDE));
                    conv.time               = c.getString(c.getColumnIndex(Cons.CONV_TIME));

                    conv.user               = new User();
                    conv.user.userId 		= c.getString(c.getColumnIndex(Cons.KEY_ID));
                    conv.user.fullName	 	= c.getString(c.getColumnIndex(Cons.KEY_NAME));
                    conv.user.avatar		= c.getString(c.getColumnIndex(Cons.KEY_AVATAR));
                    conv.user.email 		= c.getString(c.getColumnIndex(Cons.KEY_EMAIL));
                    conv.user.birthDate     = c.getString(c.getColumnIndex(Cons.KEY_BIRTH));


                    Debug.i("From db id  " + conv.conversationId);

                    wrapper.list.add(conv);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }

    public VideoWrapper getListVideo(String id) {
        VideoWrapper wrapper = null;
        wrapper 	 = new VideoWrapper();
        wrapper.list = new ArrayList<Video>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM video WHERE vid  = '"+id+"'";
        Cursor c 	= mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new VideoWrapper();

                wrapper.list 			= new ArrayList<Video>();

                while (c.isAfterLast()  == false) {
                    Video conv 		= new Video();
                    conv.videoId            = c.getString(c.getColumnIndex(Cons.VIDEO_ID));
                    conv.dateSubmitted      = c.getString(c.getColumnIndex(Cons.VIDEO_DATESUBMITTED));
                    conv.url                = c.getString(c.getColumnIndex(Cons.VIDEO_URL));
                    conv.channel            = c.getString(c.getColumnIndex(Cons.VIDEO_CHANNEL));
                    conv.avatar             = c.getString(c.getColumnIndex(Cons.VIDEO_AVATAR));
                    conv.category           = c.getString(c.getColumnIndex(Cons.VIDEO_CATEGORY));
                    conv.totalView          = c.getInt(c.getColumnIndex(Cons.VIDEO_TOTAL_VIWE));
                    conv.title              = c.getString(c.getColumnIndex(Cons.VIDEO_TITLE));

                    Debug.i("From db id  " + conv.videoId);

                    wrapper.list.add(conv);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }

    public ArticlesWrapper getListArticles(String id) {
        ArticlesWrapper wrapper = null;
        wrapper 	 = new ArticlesWrapper();
        wrapper.list = new ArrayList<Articles>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM articles WHERE aid  = '" + id + "'";
        Cursor c 	= mSqLite.rawQuery(sql, null);
        Log.d("ekse",String.valueOf(c));
        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new ArticlesWrapper();

                wrapper.list 			= new ArrayList<Articles>();

                while (c.isAfterLast()  == false) {
                    Articles articles 		= new Articles();

                    articles.articlesId      = c.getString(c.getColumnIndex(Cons.ARTICLES_ID));
                    articles.content         = c.getString(c.getColumnIndex(Cons.ARTICLES_CONTENT));
                    articles.tags            = c.getString(c.getColumnIndex(Cons.ARTICLES_TAGS));
                    articles.dateSubmitted   = c.getString(c.getColumnIndex(Cons.ARTICLES_DATE_SUBMITTED));
                    articles.latitude        = c.getString(c.getColumnIndex(Cons.ARTICLES_LATITUDE));
                    articles.longitude       = c.getString(c.getColumnIndex(Cons.ARTICLES_LONGITUDE));
                    articles.image_satu      = c.getString(c.getColumnIndex(Cons.ARTICLES_IMAGE_1));
                    articles.image_dua       = c.getString(c.getColumnIndex(Cons.ARTICLES_IMAGE_2));
                    articles.image_dua       = c.getString(c.getColumnIndex(Cons.ARTICLES_IMAGE_3));
                    articles.title           = c.getString(c.getColumnIndex(Cons.ARTICLES_TITLE));
                    articles.type            = c.getString(c.getColumnIndex(Cons.ARTICLES_TYPE));
                    articles.category        = c.getString(c.getColumnIndex(Cons.ARTICLES_CATEGORY));
                    articles.sumber_url      = c.getString(c.getColumnIndex(Cons.ARTICLES_URL));
                    articles.summary         = c.getString(c.getColumnIndex(Cons.ARTICLES_SUMMARY));




                    Debug.i("From db id  " + articles.articlesId);

                    wrapper.list.add(articles);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }


    public void updateDaerahList(ArrayList<Daerah> list, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");
        mSqLite.delete("kabupaten", "kid = '" + cid + "'", null);


        int size = list.size();

        for (int i = 0; i < size; i++) {
            Daerah daerah = list.get(i);
            ContentValues values = new ContentValues();

            values.put("kid", cid);
            values.put(Cons.DAERAH_IDK,daerah.idk);
            values.put(Cons.DAERAH_KABUPATEN,daerah.namakabupaten);
            values.put(Cons.DAERAH_NAMA_BUPATI,daerah.namabupati);
            values.put(Cons.DAERAH_LUAS_WILAYAH,daerah.luaswilayah);
            values.put(Cons.DAERAH_IBUKOTA,daerah.ibukota);
            values.put(Cons.DAERAH_JUMLAH_KECAMATAN,daerah.jumlahkecamatan);
            values.put(Cons.DAERAH_FOTO,daerah.foto);
            values.put(Cons.DAERAH_KETERANGAN,daerah.keterangan);

            mSqLite.insert("kabupaten", null, values);
        }
    }

    public DaerahWrapper getListKabupaten(String id) {
        DaerahWrapper wrapper = null;
        wrapper = new DaerahWrapper();
        wrapper.list = new ArrayList<Daerah>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM kabupaten WHERE kid  = '" + id + "'";
        Cursor c = mSqLite.rawQuery(sql, null);
        Log.d("kabupaten",String.valueOf(c));
        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper = new DaerahWrapper();

                wrapper.list = new ArrayList<Daerah>();

                while (c.isAfterLast() == false) {
                    Daerah daerah = new Daerah();

                    daerah.idk  = c.getString(c.getColumnIndex(Cons.DAERAH_IDK));
                    daerah.namakabupaten = c.getString(c.getColumnIndex(Cons.DAERAH_KABUPATEN));
                    daerah.namabupati = c.getString(c.getColumnIndex(Cons.DAERAH_NAMA_BUPATI));
                    daerah.ibukota = c.getString(c.getColumnIndex(Cons.DAERAH_IBUKOTA));
                    daerah.jumlahkecamatan = c.getString(c.getColumnIndex(Cons.DAERAH_JUMLAH_KECAMATAN));
                    daerah.luaswilayah = c.getString(c.getColumnIndex(Cons.DAERAH_LUAS_WILAYAH));
                    daerah.foto = c.getString(c.getColumnIndex(Cons.DAERAH_FOTO));
                    daerah.keterangan = c.getString(c.getColumnIndex(Cons.DAERAH_KETERANGAN));

                    Debug.i("From db id  " + daerah.idk);

                    wrapper.list.add(daerah);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }


    public WisataWrapper getListWisata(String id) {
        WisataWrapper wrapper = null;
        wrapper = new WisataWrapper();
        wrapper.list = new ArrayList<Wisata>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM wisata WHERE wid  = '" + id + "'";
        Cursor c = mSqLite.rawQuery(sql, null);
        Log.d("ekseku",String.valueOf(c));
        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper = new WisataWrapper();

                wrapper.list = new ArrayList<Wisata>();

                while (c.isAfterLast() == false) {
                    Wisata wisata = new Wisata();

                    wisata.wisataId      = c.getString(c.getColumnIndex(Cons.WISATA_ID));
                    wisata.content       = c.getString(c.getColumnIndex(Cons.WISATA_CONTENT));
                    wisata.tags          = c.getString(c.getColumnIndex(Cons.WISATA_TAGS));
                    wisata.title         = c.getString(c.getColumnIndex(Cons.WISATA_TITLE));
                    wisata.latitude      = c.getString(c.getColumnIndex(Cons.WISATA_LATITUDE));
                    wisata.longitude     = c.getString(c.getColumnIndex(Cons.WISATA_LONGITUDE));
                    wisata.location      = c.getString(c.getColumnIndex(Cons.WISATA_LOCATION));
                    wisata.category      = c.getString(c.getColumnIndex(Cons.WISATA_CATEGORY));
                    wisata.sumber_url    = c.getString(c.getColumnIndex(Cons.WISATA_SUMBER_URL));
                    wisata.dateSubmitted = c.getString(c.getColumnIndex(Cons.WISATA_DATE_SUBMITTED));
                    wisata.attachment    = c.getString(c.getColumnIndex(Cons.WISATA_ATTACHMENT));
                    wisata.daerah        = c.getString(c.getColumnIndex(Cons.WISATA_DAERAH));
                    wisata.summary       = c.getString(c.getColumnIndex(Cons.WISATA_SUMMARY));

                    Debug.i("From db id  " + wisata.wisataId);

                    wrapper.list.add(wisata);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }

    public StoryWrapper getListStory(String id) {
        StoryWrapper wrapper = null;
        wrapper 	 = new StoryWrapper();
        wrapper.list = new ArrayList<Story>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM cache_questions WHERE cid  = '"+id+"'";
        Cursor c 	= mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new StoryWrapper();

                wrapper.list 			= new ArrayList<Story>();
                wrapper.hasNext			= false;
                wrapper.totalRecords	= c.getCount();

                while (c.isAfterLast()  == false) {
                    Story conv 		= new Story();

                    conv.dateSubmitted      = c.getString(c.getColumnIndex(Cons.CONV_DATE_SUBMITTED));
                    conv.title              = c.getString(c.getColumnIndex(Cons.CONV_TITLE));



                    Debug.i("From db id  " + conv.storyId);

                    wrapper.list.add(conv);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }


    public void updateQuestionList(ArrayList<Conversation> list,String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");
        mSqLite.delete("cache_questions", "cid = '" + cid + "'", null);


        int size = list.size();

        for (int i = 0; i < size; i++){
            Conversation conv		= list.get(i);
            ContentValues values 	= new ContentValues();

            values.put("cid",                    cid);
            values.put(Cons.CONV_ID,             conv.conversationId);
            values.put(Cons.CONV_CONTENT,        conv.content);
            values.put(Cons.CONV_TAGS,           conv.tags);
            values.put(Cons.CONV_DATE_SUBMITTED, conv.dateSubmitted);
            values.put(Cons.CONV_ATTACHMENT,     conv.attachment);
            values.put(Cons.CONV_TOTAL_RESPONSES,conv.totalResponses);
            values.put(Cons.CONV_TOTAL_VOTE,     conv.totalVotes);
            values.put(Cons.CONV_ISVOTED,        conv.isVoted);
            values.put(Cons.CONV_TYPE,           conv.type);
            values.put(Cons.CONV_TITLE,          conv.title);
            values.put(Cons.CONV_SUMMARY,        conv.summary);
            values.put(Cons.CONV_LATITUDE,       conv.latitude);
            values.put(Cons.CONV_LONGITUDE,      conv.longitude);
            values.put(Cons.CONV_TIME,           conv.time);

            values.put(Cons.KEY_ID,    	         conv.user.userId);
            values.put(Cons.KEY_NAME,    	     conv.user.fullName);
            values.put(Cons.KEY_GENDER,          conv.user.gender);
            values.put(Cons.KEY_AVATAR,    	     conv.user.avatar);
            values.put(Cons.KEY_EMAIL,      	 conv.user.email);
            values.put(Cons.KEY_BIRTH,           conv.user.birthDate);

            mSqLite.insert("cache_questions", null, values);
        }
    }

    public void updateVideoList(ArrayList<Video> list, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");
        mSqLite.delete("video", "vid = '" + cid + "'", null);


        int size = list.size();

        for (int i = 0; i < size; i++){
            Video video		= list.get(i);
            ContentValues values 	= new ContentValues();

            values.put("vid",cid);
            values.put(Cons.VIDEO_ID,video.videoId);
            values.put(Cons.VIDEO_DATESUBMITTED,video.dateSubmitted);
            values.put(Cons.VIDEO_URL,video.url);
            values.put(Cons.VIDEO_CHANNEL,video.channel);
            values.put(Cons.VIDEO_AVATAR,video.avatar);
            values.put(Cons.VIDEO_CATEGORY,video.category);
            values.put(Cons.VIDEO_TOTAL_VIWE,video.totalView);
            values.put(Cons.VIDEO_TITLE,video.title);

            mSqLite.insert("video", null, values);
        }
    }
    public void updateArticlesList(ArrayList<Articles> list, String aid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");
        mSqLite.delete("articles", "aid = '" + aid + "'", null);


        int size = list.size();

        for (int i = 0; i < size; i++) {
            Articles articles = list.get(i);
            ContentValues values = new ContentValues();

            values.put("aid", aid);
            values.put(Cons.ARTICLES_ID,articles.articlesId);
            values.put(Cons.ARTICLES_CONTENT,articles.content);
            values.put(Cons.ARTICLES_TAGS,articles.tags);
            values.put(Cons.ARTICLES_DATE_SUBMITTED,articles.dateSubmitted);
            values.put(Cons.ARTICLES_LATITUDE,articles.latitude);
            values.put(Cons.ARTICLES_LONGITUDE,articles.longitude);
            values.put(Cons.ARTICLES_IMAGE_1,articles.image_satu);
            values.put(Cons.ARTICLES_IMAGE_2,articles.image_dua);
            values.put(Cons.ARTICLES_IMAGE_3,articles.image_tiga);
            values.put(Cons.ARTICLES_TITLE,articles.title);
            values.put(Cons.ARTICLES_TYPE,articles.type);
            values.put(Cons.ARTICLES_CATEGORY,articles.category);
            values.put(Cons.ARTICLES_URL,articles.sumber_url);
            values.put(Cons.ARTICLES_SUMMARY,articles.summary);


            mSqLite.insert("articles", null, values);
        }
    }

    public void updateWisataList(ArrayList<Wisata> list, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");
        mSqLite.delete("wisata", "wid = '" + cid + "'", null);


        int size = list.size();

        for (int i = 0; i < size; i++) {
            Wisata wisata = list.get(i);
            ContentValues values = new ContentValues();

            values.put("wid", cid);
            values.put(Cons.WISATA_ID,wisata.wisataId);
            values.put(Cons.WISATA_CONTENT,wisata.content);
            values.put(Cons.WISATA_TAGS,wisata.tags);
            values.put(Cons.WISATA_DATE_SUBMITTED,wisata.dateSubmitted);
            values.put(Cons.WISATA_LATITUDE,wisata.latitude);
            values.put(Cons.WISATA_LONGITUDE,wisata.longitude);
            values.put(Cons.WISATA_ATTACHMENT,wisata.attachment);
            values.put(Cons.WISATA_LOCATION,wisata.location);
            values.put(Cons.WISATA_TITLE,wisata.title);
            values.put(Cons.WISATA_CATEGORY,wisata.category);
            values.put(Cons.WISATA_SUMBER_URL,wisata.sumber_url);
            values.put(Cons.WISATA_DAERAH,wisata.daerah);
            values.put(Cons.WISATA_SUMMARY,wisata.summary);


            mSqLite.insert("wisata", null, values);
        }
    }

    public void updateStoryList(ArrayList<Story> list, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");
        mSqLite.delete("cache_question", "cid = '" + cid + "'", null);


        int size = list.size();

        for (int i = 0; i < size; i++){
            Story conv		= list.get(i);
            ContentValues values 	= new ContentValues();

            values.put("cid",cid);



            mSqLite.insert("cache_question", null, values);
        }
    }


    //UPDATE CACHE  di back dari detail

    public void update(Wisata conv, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        ContentValues values = new ContentValues();

       // values.put("totalResponses",	conv.totalResponses);
        values.put("totalVotes",		conv.totalVotes);
        values.put("isVoted",			conv.isVoted);

        mSqLite.update("wisata", values, "wisataId = '" + conv.wisataId + "'", null);
    }

    public void update(Conversation conv, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put(Cons.CONV_TOTAL_RESPONSES,	conv.totalResponses);
        values.put(Cons.CONV_TOTAL_VOTE,		conv.totalVotes);
        values.put(Cons.CONV_ISVOTED,			conv.isVoted);

        mSqLite.update("cache_questions", values, "conversationId = '" + conv.id + "'", null);
    }

    public void update(Articles conv, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        ContentValues values = new ContentValues();

      //  values.put("totalResponses",	conv.totalViews);
       // values.put("totalVotes",		conv.totalVotes);
      //  values.put("isVoted",			conv.isVoted);

        mSqLite.update("articles", values, "id = '" + conv.articlesId + "'", null);
    }

    public void update(Story conv, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        ContentValues values = new ContentValues();

       // values.put("totalResponses",	conv.totalResponses);
        values.put("totalVotes",		conv.totalVotes);
        values.put("isVoted",			conv.isVoted);

        mSqLite.update("cache", values, "id = '" + conv.storyId + "'", null);
    }


    public void update(Video conv, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put("totalResponses",	conv.totalView);
       // values.put("totalVotes",		conv.totalVotes);
      //  values.put("isVoted",			conv.isVoted);

        mSqLite.update("video", values, "videoId = '" + conv.videoId + "'", null);
    }



    public void updateLike(Conversation conv) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put("isVoted",		conv.isVoted);
        values.put("totalVotes", 	conv.totalVotes);

        mSqLite.update("cache_question", values, "conversationId = '" + conv.conversationId + "'", null);
    }




    public AngkutanWrapper getListAngkutan(String id) {
        AngkutanWrapper wrapper = null;
        wrapper = new AngkutanWrapper();
        wrapper.list = new ArrayList<Angkutan>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql = "SELECT * FROM angkutan WHERE awid  = '" + id + "'";
        Cursor c = mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper = new AngkutanWrapper();

                wrapper.list = new ArrayList<Angkutan>();

                while (c.isAfterLast() == false) {
                    Angkutan angkutan = new Angkutan();

                    angkutan.idangkutan      = c.getString(c.getColumnIndex(Cons.ANGKUTAN_ID));
                    angkutan.nama_perusahaan = c.getString(c.getColumnIndex(Cons.ANGKUTAN_NAME));
                    angkutan.pemilik         = c.getString(c.getColumnIndex(Cons.ANGKUTAN_PEMILIK));
                    angkutan.jumlahkendaraan = c.getString(c.getColumnIndex(Cons.ANGKUTAN_JUMLAH_KENDARAAN));
                    angkutan.kategori        = c.getString(c.getColumnIndex(Cons.ANGKUTAN_KATEGORI));
                    angkutan.keterangan      = c.getString(c.getColumnIndex(Cons.ANGKUTAN_KETERANGAN));
                    angkutan.foto            = c.getString(c.getColumnIndex(Cons.ANGKUTAN_FOTO));
                    angkutan.merek           = c.getString(c.getColumnIndex(Cons.ANGKUTAN_MEREK));

                   // Debug.i("From db id  " + wisata.wisataId);

                    wrapper.list.add(angkutan);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }

    public void updateAngkutanList(ArrayList<Angkutan> list, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");
        mSqLite.delete("angkutan", "awid = '" + cid + "'", null);


        int size = list.size();

        for (int i = 0; i < size; i++) {
            Angkutan angkutan = list.get(i);
            ContentValues values = new ContentValues();

            values.put("awid", cid);
            values.put(Cons.ANGKUTAN_ID,angkutan.idangkutan);
            values.put(Cons.ANGKUTAN_NAME,angkutan.nama_perusahaan);
            values.put(Cons.ANGKUTAN_PEMILIK,angkutan.pemilik);
            values.put(Cons.ANGKUTAN_JUMLAH_KENDARAAN,angkutan.jumlahkendaraan);
            values.put(Cons.ANGKUTAN_MEREK,angkutan.merek);
            values.put(Cons.ANGKUTAN_FOTO,angkutan.foto);
            values.put(Cons.ANGKUTAN_KATEGORI,angkutan.kategori);
            values.put(Cons.ANGKUTAN_KETERANGAN,angkutan.keterangan);



            mSqLite.insert("angkutan", null, values);
        }
    }


    public boolean delete(String id) {
        boolean result = false;

        if (mSqLite == null || !mSqLite.isOpen()) {
            return false;
        }

        Debug.i("Delete question from database");

        String sql 	= "DELETE FROM cache_questions WHERE conversationId = '" + id + "'";

        Cursor c	= mSqLite.rawQuery(sql, null);

        if (c != null) {
            result = (c.getCount() == 0) ? false : true;

            c.close();
        }

        return result;
    }



    public boolean exist(String id) {
        boolean result = false;

        if (mSqLite == null || !mSqLite.isOpen()) {
            return false;
        }

        String sql 	= "SELECT * FROM cache_questions WHERE conversationId = '" + id + "'";

        Cursor c	= mSqLite.rawQuery(sql, null);

        if (c != null) {
            result = (c.getCount() == 0) ? false : true;

            c.close();
        }

        return result;
    }



    public void update(ArrayList<Cerita> list, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");

        mSqLite.delete("cache_cerita", "cid = '" + cid + "'", null);

        int size = list.size();

        for (int i = 0; i < size; i++){
            Cerita cerita	= list.get(i);
            ContentValues values 	= new ContentValues();

            values.put("cid",       cid);
            values.put(Cons.CERITA_ID,    	   cerita.id);
            values.put(Cons.CERITA_TITLE,	   cerita.title);
            values.put(Cons.CERITA_CONTENT,    cerita.content);
            values.put(Cons.CERITA_TYPE,	   cerita.type);
            values.put(Cons.CERITA_CREATED_AT, cerita.createdAt);
            values.put(Cons.CERITA_IMAGE,      cerita.image);
            values.put(Cons.CERITA_SUMBER,	   cerita.sumber);
            values.put(Cons.CERITA_CATEGORY,   cerita.category);

            mSqLite.insert("cache_cerita", null, values);
        }
    }

    public CeritaWrapper getListCerita(String id) {
        CeritaWrapper wrapper = null;
        wrapper 	 = new CeritaWrapper();
        wrapper.list = new ArrayList<Cerita>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM cache_cerita WHERE cid  = '"+id+"'";
        Cursor c 	= mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new CeritaWrapper();

                wrapper.list 			= new ArrayList<Cerita>();

                while (c.isAfterLast()  == false) {
                    Cerita cerita = new Cerita();

                    cerita.id 			= c.getString(c.getColumnIndex(Cons.CERITA_ID));
                    cerita.title 		= c.getString(c.getColumnIndex(Cons.CERITA_TITLE));
                    cerita.content 	    = c.getString(c.getColumnIndex(Cons.CERITA_CONTENT));
                    cerita.type 		= c.getString(c.getColumnIndex(Cons.CERITA_TYPE));
                    cerita.createdAt 	= c.getString(c.getColumnIndex(Cons.CERITA_CREATED_AT));
                    cerita.image 		= c.getString(c.getColumnIndex(Cons.CERITA_IMAGE));
                    cerita.sumber 		= c.getString(c.getColumnIndex(Cons.CERITA_SUMBER));
                    cerita.category     = c.getString(c.getColumnIndex(Cons.CERITA_CATEGORY));


                    Debug.i("From db id  " + cerita.id);

                    wrapper.list.add(cerita);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }


    public ResepWrapper getListResep(String id) {
        ResepWrapper wrapper = null;
        wrapper 	 = new ResepWrapper();
        wrapper.list = new ArrayList<Resep>();

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM cache_resep WHERE rid  = '"+id+"'";
        Cursor c 	= mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new ResepWrapper();

                wrapper.list 			= new ArrayList<Resep>();

                while (c.isAfterLast()  == false) {
                    Resep resep = new Resep();

                    resep.idresep 			= c.getString(c.getColumnIndex(Cons.RESEP_ID));
                    resep.title 			= c.getString(c.getColumnIndex(Cons.RESEP_TITLE));
                    resep.content1			= c.getString(c.getColumnIndex(Cons.RESEP_CONTENT_1));
                    resep.content2 			= c.getString(c.getColumnIndex(Cons.RESEP_CONTENT_2));
                    resep.image1 			= c.getString(c.getColumnIndex(Cons.RESEP_IMAGE_1));
                    resep.image2 			= c.getString(c.getColumnIndex(Cons.RESEP_IMAGE_2));
                    resep.sumber 			= c.getString(c.getColumnIndex(Cons.RESEP_SUMBER));
                    resep.dateSubmitted     = c.getString(c.getColumnIndex(Cons.RESEP_DATE_SUBMITTED));



                    Debug.i("From db id  " + resep.idresep);

                    wrapper.list.add(resep);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }


    public void updateResep(ArrayList<Resep> list, String cid) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        if (list == null) {
            return;
        }

        Debug.i("Updating cache ");

        mSqLite.delete("cache_resep", "rid = '" + cid + "'", null);

        int size = list.size();

        for (int i = 0; i < size; i++){
            Resep resep	= list.get(i);
            ContentValues values 	= new ContentValues();

            values.put("rid",       cid);
            values.put(Cons.RESEP_ID,    	   resep.idresep);
            values.put(Cons.RESEP_TITLE,	   resep.title);
            values.put(Cons.RESEP_CONTENT_1,   resep.content1);
            values.put(Cons.RESEP_CONTENT_2,   resep.content2);
            values.put(Cons.RESEP_IMAGE_1,     resep.image1);
            values.put(Cons.RESEP_IMAGE_2,     resep.image2);
            values.put(Cons.RESEP_SUMBER,	   resep.sumber);
            values.put(Cons.RESEP_DATE_SUBMITTED,   resep.dateSubmitted);

            mSqLite.insert("cache_resep", null, values);
        }
    }

}
