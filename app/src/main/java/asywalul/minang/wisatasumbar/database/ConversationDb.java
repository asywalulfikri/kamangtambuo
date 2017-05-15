package asywalul.minang.wisatasumbar.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.util.Debug;

/**
 * Created by asywalulfikri on 10/2/16.
 */

    public class ConversationDb extends Database {

        public ConversationDb(SQLiteDatabase sqlite) {
            super(sqlite);
        }

        public boolean exist(String id) {
            boolean result = false;

            if (mSqLite == null || !mSqLite.isOpen()) {
                return false;
            }

            String sql 	= "SELECT * FROM conversation WHERE conversationId = '" + id + "'";

            Cursor c	= mSqLite.rawQuery(sql, null);

            if (c != null) {
                result = (c.getCount() == 0) ? false : true;

                c.close();
            }

            return result;
        }

    public void delete(Conversation conv) {
        if (mSqLite == null || !mSqLite.isOpen()) {
            return;
        }

        Debug.i("Updating conversation ");

        ContentValues values = new ContentValues();

        values.put("conversationId", conv.id);

        if (exist(conv.id)) {
            mSqLite.delete("conversation", "conversationId = '" + conv.id + "'", null);
        } else {
            mSqLite.delete("conversation","conversationId = '" + conv.id + "'", null);
        }
    }

    }
