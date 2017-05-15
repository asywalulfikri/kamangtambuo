package asywalul.minang.wisatasumbar.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.http.core.RestConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.model.Story;
import asywalul.minang.wisatasumbar.model.StoryWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.HttpManager1;

/**
 * Created by asywalulfikri on 10/6/16.
 */

public class StoryConnection  extends RestConnection {


    public StoryWrapper getList() throws Exception, WisataException {

        StoryWrapper wrapper = null;

        try {
            String url = Cons.CONVERSATION_URL + "/listStory.php";

            String get = HttpManager1.httpGet(url);

            String statusCode = get.substring(get.lastIndexOf("`") + 1, get.length());

            if (!statusCode.equals("200")) {
                throw new WisataException("Wrong data or doesnt exists");
            } else {

                JSONObject object = new JSONObject(get);
                JSONArray List = object.getJSONArray("data");

                int size = List.length();

                wrapper = new StoryWrapper();
                wrapper.list = new ArrayList<Story>();

                for (int i = 0; i < size; i++) {

                    JSONObject itemJson = List.getJSONObject(i);

                    Story story = new Story();

                    story.storyId = itemJson.getString(Cons.STORY_ID);
                    story.content = itemJson.getString(Cons.CONV_CONTENT);
                    story.dateSubmitted = itemJson.getString(Cons.CONV_DATE_SUBMITTED);
                    story.attachment = itemJson.getString(Cons.CONV_ATTACHMENT);
                    story.title = itemJson.getString(Cons.CONV_TITLE);

                    story.user = new User();

                    story.user.userId = itemJson.getString(Cons.KEY_ID);
                    story.user.fullName = itemJson.getString(Cons.KEY_NAME);
                    story.user.avatar = itemJson.getString(Cons.KEY_AVATAR);
                    story.user.gender = itemJson.getString(Cons.KEY_GENDER);
                    story.user.status = itemJson.getString(Cons.KEY_STATUS);
                    story.user.msisdn = itemJson.getString(Cons.KEY_MSISDN);
                    story.user.email = itemJson.getString(Cons.KEY_EMAIL);
                    story.user.birthDate = itemJson.getString(Cons.KEY_BIRTH);

                    wrapper.list.add(story);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }
}
