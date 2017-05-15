package asywalul.minang.wisatasumbar.http;

import asywalul.minang.wisatasumbar.model.Comment;
import asywalul.minang.wisatasumbar.model.CommentWrapper;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.ConversationWrapper;
import asywalul.minang.wisatasumbar.model.Question;
import asywalul.minang.wisatasumbar.model.QuestionWrapper;
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
 * Created by Toshiba on 3/12/2016.
 */
public class QuestionConnection extends RestConnection {


    public ConversationWrapper getList(String iduser, int page) throws Exception, WisataException {

        ConversationWrapper wrapper = null;

        try {
            String url = Cons.CONVERSATION_URL+ "/listQuestion.php?userId="+iduser+"&offset="+page+"&limit=20";

            String get = HttpManager1.httpGet(url);

            String statusCode = get.substring(get.lastIndexOf("`") + 1, get.length());

            if (!statusCode.equals("200")){
                throw new WisataException("Wrong data or doesnt exists");
            } else {

                JSONObject object = new JSONObject(get);
                JSONArray List = object.getJSONArray("data");

                int size = List.length();

                wrapper = new ConversationWrapper();
                wrapper.list = new ArrayList<Conversation>();

                for (int i = 0; i < size ; i++) {

                    JSONObject itemJson = List.getJSONObject(i);

                    Conversation conversation		= new Conversation();

                    conversation.conversationId     = itemJson.getString(Cons.CONV_ID);
                    conversation.content		    = itemJson.getString(Cons.CONV_CONTENT);
                    conversation.tags	            = itemJson.getString(Cons.CONV_TAGS);
                    conversation.dateSubmitted	    = itemJson.getString(Cons.CONV_DATE_SUBMITTED);
                    conversation.totalResponses     = itemJson.getInt(Cons.CONV_TOTAL_RESPONSES);
                    conversation.totalVotes         = itemJson.getInt(Cons.CONV_TOTAL_VOTE);
                    conversation.attachment         = itemJson.getString(Cons.CONV_ATTACHMENT);
                    conversation.isVoted 			= itemJson.getInt(Cons.CONV_ISVOTED);
                    conversation.type               = itemJson.getString(Cons.CONV_TYPE);
                    conversation.title              = itemJson.getString(Cons.CONV_TITLE);
                    conversation.summary            = itemJson.getString(Cons.CONV_SUMMARY);
                    conversation.latitude           = itemJson.getString(Cons.KEY_LATITUDE);
                    conversation.longitude          = itemJson.getString(Cons.KEY_LONGITUDE);

                    conversation.user				= new User();

                    conversation.user.userId	    = itemJson.getString(Cons.KEY_ID);
                    conversation.user.fullName      = itemJson.getString(Cons.KEY_NAME);
                    conversation.user.avatar        = itemJson.getString(Cons.KEY_AVATAR);
                    conversation.user.gender        = itemJson.getString(Cons.KEY_GENDER);
                    conversation.user.status        = itemJson.getString(Cons.KEY_STATUS);
                    conversation.user.msisdn        = itemJson.getString(Cons.KEY_MSISDN);
                    conversation.user.email         = itemJson.getString(Cons.KEY_EMAIL);
                    conversation.user.birthDate     = itemJson.getString(Cons.KEY_BIRTH);

                    wrapper.list.add(conversation);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }

    public Question postQuestion(String iduser,String content, String tags, String foto, String image_name,String path,String waktu,String latitude,String longitude)
            throws Exception, WisataException {

        try {
            Debug.i("Posting question...");
            Debug.i(content);
            Log.e("iduser", iduser);
            Log.e("content", content);
            Log.e("tags", tags);
            Log.e("foto", foto);
            Log.e("in",image_name);
            Log.e("put", path);
            Log.e("lat",latitude);
            Log.e("Long",longitude);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_ID, iduser));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_CONTENT, content));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_TAGS, tags));
            nameValuePairs.add(new BasicNameValuePair("foto", foto));
            nameValuePairs.add(new BasicNameValuePair("image_name", image_name));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_ATTACHMENT,path));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_DATE_SUBMITTED,waktu));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_LATITUDE,latitude));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_LONGITUDE,longitude));

            String url = Cons.CONVERSATION_URL+"/post_question.php";

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


    public Question postQuestion(String iduser,String content, String tags,String waktu,String latitude, String longitude)
            throws Exception, WisataException {

        try {
            Debug.i("Posting question...");
            Debug.i(content);
            Log.e("iduser", iduser);
            Log.e("content", content);
            Log.e("tags", tags);
            Log.e("waktu", waktu);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_ID, iduser));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_CONTENT, content));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_TAGS, tags));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_DATE_SUBMITTED,waktu));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_LATITUDE,latitude));
            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_LONGITUDE,longitude));

            String url = Cons.CONVERSATION_URL+"/post_question2.php";;

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




    public Question postCommentQuestion(String iduser,String idquestion,String content,String time)
            throws Exception, WisataException {
        CommentWrapper wrapper;
        try {
            Debug.i("Posting question...");
            Debug.i(content);
            Log.e("iduser", iduser);
            Log.e("content", content);
            Log.e("idques", idquestion);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("iduser", iduser));
            nameValuePairs.add(new BasicNameValuePair("idquestion", idquestion));
            nameValuePairs.add(new BasicNameValuePair("content", content));
            nameValuePairs.add(new BasicNameValuePair("waktu", time));

            String responses = Cons.CONVERSATION_URL + "/coment.php";

            InputStream is = connectPost(responses, nameValuePairs);

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





    public Question postCommentQuestion(String iduser,String idquestion, String content, String foto, String image_name,String path)
            throws Exception, WisataException {

        try {
            Debug.i("Posting question...");
            Debug.i(content);
            Log.e("iduser", iduser);
            Log.e("content", content);
            Log.e("idque", idquestion);
            Log.e("foto", foto);
            Log.e("in", image_name);
            Log.e("put", path);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("iduser", iduser));
            nameValuePairs.add(new BasicNameValuePair("content", content));
            nameValuePairs.add(new BasicNameValuePair("idquestion", idquestion));
            nameValuePairs.add(new BasicNameValuePair("foto", foto));
            nameValuePairs.add(new BasicNameValuePair("image_name", image_name));
            nameValuePairs.add(new BasicNameValuePair("pathh",path));

            String url = Cons.CONVERSATION_URL+"/post_question.php";

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


    public CommentWrapper getListcomment(String idquestion) throws Exception, WisataException {

        CommentWrapper wrapper = null;

        try {
            String url = Cons.CONVERSATION_URL+ "/list_comment_question.php?idquestion="+idquestion;
            Log.d("jess",url);

            String get = HttpManager1.httpGet(url);

            String statusCode = get.substring(get.lastIndexOf("`") + 1, get.length());

            if (!statusCode.equals("200")){
                throw new WisataException("Wrong data or doesnt exists");
            } else {
                String response = get.substring(0, get.lastIndexOf("`"));

                JSONObject object = new JSONObject(get);
                JSONArray List = object.getJSONArray("data");

                int size = List.length();

                wrapper = new CommentWrapper();
                wrapper.list = new ArrayList<Comment>();

                for (int i = 0; i < size ; i++) {

                    JSONObject itemJson = List.getJSONObject(i);

                    Comment comment		= new Comment();

                    comment.conversationId     = itemJson.getString(Cons.CONV_ID);
                    comment.content		       = (!itemJson.isNull(Cons.CONV_CONTENT)) ? itemJson.getString(Cons.CONV_CONTENT) : "";
                    comment.foto	           = itemJson.getString(Cons.CONV_ATTACHMENT);
                    comment.time	           = (!itemJson.isNull("time")) ? itemJson.getString("time") : "";
                    comment.waktu              = (!itemJson.isNull("waktu")) ? itemJson.getString("waktu") : "";

                    comment.user			   = new User();

                    comment.user.userId	       = itemJson.getString(Cons.KEY_ID);
                    comment.user.fullName      = (!itemJson.isNull(Cons.KEY_NAME)) ? itemJson.getString(Cons.KEY_NAME) : "";
                    comment.user.avatar        = itemJson.getString(Cons.KEY_AVATAR);
                    comment.user.gender        = itemJson.getString(Cons.KEY_GENDER);
                    comment.user.status        = itemJson.getString(Cons.KEY_STATUS);
                    comment.user.msisdn        = itemJson.getString(Cons.KEY_MSISDN);
                    comment.user.email         = itemJson.getString(Cons.KEY_EMAIL);
                    comment.user.birthDate     = itemJson.getString(Cons.KEY_BIRTH);
                    comment.user.latitude      = itemJson.getString(Cons.KEY_LATITUDE);
                    comment.user.longitude     = itemJson.getString(Cons.KEY_LONGITUDE);


                    wrapper.list.add(comment);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }



    public void like(String id,String iduser) throws Exception, WisataException {

        try {
            Debug.i("Liking...");
            Debug.i(id);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_ID, id));
            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_ID, iduser));
            String url = Cons.CONVERSATION_URL+"/postVote.php";

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

    }





    public void unlike(String id,String iduser) throws Exception, WisataException {

        try {
            Debug.i("Liking...");
            Debug.i(id);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair(Cons.CONV_ID, id));
            nameValuePairs.add(new BasicNameValuePair(Cons.KEY_ID,iduser));
            String url = Cons.CONVERSATION_URL+"/deleteVote.php?conversationId="+id+"&userId="+iduser;

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

    }
}