package asywalul.minang.wisatasumbar.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.model.Cerita;
import asywalul.minang.wisatasumbar.model.CeritaWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;


/**
 * Created by asywalulfikri on 12/15/16.
 */

public class CeritaDb extends Database {


    public CeritaDb(SQLiteDatabase sqlite) {
        super(sqlite);
    }

    public boolean exist(String id) {
        boolean result = false;

        if (mSqLite == null || !mSqLite.isOpen()) {
            return false;
        }

        String sql 	= "SELECT * FROM cerita WHERE id ='"+id+"'";

        Cursor c	= mSqLite.rawQuery(sql, null);

        if (c != null) {
            result = (c.getCount() == 0) ? false : true;

            c.close();
        }

        return result;
    }

    public void delete(Cerita cerita) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        Debug.i("Updating cerita ");

        ContentValues values = new ContentValues();

        values.put("id", cerita.id);

        if (exist(cerita.id)) {
            mSqLite.delete("cerita", "id = '" + cerita.id + "'", null);
        } else {
            mSqLite.delete("cerita","id = '" + cerita.id + "'", null);
        }
    }

    public void update(Cerita cerita, String bookmark) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        Debug.i("Updating cerita ");

        ContentValues values = new ContentValues();

        values.put("cid",       bookmark);
        values.put(Cons.CERITA_ID,    	   cerita.id);
        values.put(Cons.CERITA_TITLE,	   cerita.title);
        values.put(Cons.CERITA_CONTENT,    cerita.content);
        values.put(Cons.CERITA_TYPE,	   cerita.type);
        values.put(Cons.CERITA_CREATED_AT, cerita.createdAt);
        values.put(Cons.CERITA_IMAGE,      cerita.image);
        values.put(Cons.CERITA_SUMBER,	   cerita.sumber);
        values.put(Cons.CERITA_CATEGORY,   cerita.category);


        if (exist(cerita.id)) {
            mSqLite.update("cerita", values, "id = '" + cerita.id + "'", null);
        } else {
            mSqLite.insert("cerita", null, values);
        }
    }


    public CeritaWrapper getList(String type) {
        CeritaWrapper wrapper = null;

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM cerita WHERE cid = '" + type +"'";

        Cursor c 	= mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new CeritaWrapper();

                wrapper.list 			= new ArrayList<Cerita>();


                while (c.isAfterLast()  == false) {
                    Cerita cerita	= new Cerita();

                    cerita.id 			= c.getString(c.getColumnIndex(Cons.CERITA_ID));
                    cerita.title 		= c.getString(c.getColumnIndex(Cons.CERITA_TITLE));
                    cerita.content 	    = c.getString(c.getColumnIndex(Cons.CERITA_CONTENT));
                    cerita.type 		= c.getString(c.getColumnIndex(Cons.CERITA_TYPE));
                    cerita.createdAt 	= c.getString(c.getColumnIndex(Cons.CERITA_CREATED_AT));
                    cerita.image 		= c.getString(c.getColumnIndex(Cons.IMAGE_NAME));
                    cerita.sumber 		= c.getString(c.getColumnIndex(Cons.CERITA_SUMBER));
                    cerita.category     = c.getString(c.getColumnIndex(Cons.CERITA_CATEGORY));

                    //Debug.i(conv.type);

                    wrapper.list.add(cerita);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }

    public CeritaWrapper getListBookmark() {
        CeritaWrapper wrapper = null;

        if (mSqLite == null || !mSqLite.isOpen()) {
            return wrapper;
        }

        String sql	= "SELECT * FROM cerita order by createdAt DESC";

        Cursor c 	= mSqLite.rawQuery(sql, null);

        Debug.i(sql);

        if (c != null) {

            if (c.moveToFirst()) {
                wrapper 	 = new CeritaWrapper();

                wrapper.list 			= new ArrayList<Cerita>();


                while (c.isAfterLast()  == false) {
                    Cerita cerita	= new Cerita();


                    cerita.id 			= c.getString(c.getColumnIndex(Cons.CERITA_ID));
                    cerita.title 		= c.getString(c.getColumnIndex(Cons.CERITA_TITLE));
                    cerita.content 	    = c.getString(c.getColumnIndex(Cons.CERITA_CONTENT));
                    cerita.type 		= c.getString(c.getColumnIndex(Cons.CERITA_TYPE));
                    cerita.createdAt 	= c.getString(c.getColumnIndex(Cons.CERITA_CREATED_AT));
                    cerita.image 		= c.getString(c.getColumnIndex(Cons.IMAGE_NAME));
                    cerita.sumber 		= c.getString(c.getColumnIndex(Cons.CERITA_SUMBER));
                    cerita.category     = c.getString(c.getColumnIndex(Cons.CERITA_CATEGORY));


                    wrapper.list.add(cerita);

                    c.moveToNext();
                }
            }

            c.close();
        }

        return wrapper;
    }


}
