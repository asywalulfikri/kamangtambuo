package asywalul.minang.wisatasumbar.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import asywalul.minang.wisatasumbar.model.Question;
import asywalul.minang.wisatasumbar.model.QuestionWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;

import java.util.ArrayList;


public class QuestionDb extends Database {

	public QuestionDb(SQLiteDatabase sqlite) {
		super(sqlite);
	}
	
	public void update(String iduser,String content, String tags) {
		if (mSqLite == null || !mSqLite.isOpen()) {
			return;
		}
			
		Debug.i("Add question to database");

		ContentValues values = new ContentValues();

		values.put(Cons.KEY_ID, iduser);
		values.put(Cons.CONV_CONTENT, content);
		values.put(Cons.CONV_TAGS, tags);
		
		mSqLite.insert("question", null, values);
		
	}	
	
	public void update(String iduser,String content, String tags, String foto) {
		if (mSqLite == null || !mSqLite.isOpen()) {
			return;
		}
			
		Debug.i("Add question to database");

		ContentValues values = new ContentValues();

		values.put(Cons.KEY_ID,iduser);
		values.put(Cons.CONV_CONTENT, content);
		values.put(Cons.CONV_TAGS, tags);
		values.put(Cons.CONV_ATTACHMENT,foto);
		
		mSqLite.insert("question", null, values);
		
	}		
	
	public boolean delete(String content) {
		boolean result = false;
		
		if (mSqLite == null || !mSqLite.isOpen()) {
			return false;
		}
			
		Debug.i("Delete question from database");
		
		String sql 	= "DELETE FROM cache_question WHERE content = '" + content + "'";
		
		Cursor c	= mSqLite.rawQuery(sql, null);
		
		if (c != null) {
			result = (c.getCount() == 0) ? false : true;
				
			c.close();
		}
		
		return result;		
	}	

	public QuestionWrapper getList() {
		QuestionWrapper wrapper = null;
		
		if (mSqLite == null || !mSqLite.isOpen()) {
			return wrapper;
		}
		
		String sql	= "SELECT * FROM cache_question";
	
		Cursor c 	= mSqLite.rawQuery(sql, null);
	
		Debug.i(sql);
		
		if (c != null) {
			
			if (c.moveToFirst()) {
				wrapper 	 = new QuestionWrapper();
				
				wrapper.list 			= new ArrayList<Question>();
				wrapper.hasNext			= false;
				wrapper.totalRecords	= c.getCount();
				
				while (c.isAfterLast()  == false) {
					Question question 	= new Question();

					question.iduser		= c.getString(c.getColumnIndex(Cons.KEY_ID));
					question.idquestion = c.getString(c.getColumnIndex(Cons.CONV_ID));
					question.tags		= c.getString(c.getColumnIndex(Cons.CONV_TAGS));
					question.content 	= c.getString(c.getColumnIndex(Cons.CONV_CONTENT));
					question.pathh		= c.getString(c.getColumnIndex(Cons.CONV_ATTACHMENT));
					
					wrapper.list.add(question);
					
					c.moveToNext();
				}
			}
			
			c.close();
		}
	
		return wrapper;
	}
}
