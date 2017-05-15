package asywalul.minang.wisatasumbar.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.model.Angkutan;
import asywalul.minang.wisatasumbar.model.AngkutanWrapper;
import asywalul.minang.wisatasumbar.model.Articles;
import asywalul.minang.wisatasumbar.model.ArticlesWrapper;
import asywalul.minang.wisatasumbar.model.Cerita;
import asywalul.minang.wisatasumbar.model.CeritaWrapper;
import asywalul.minang.wisatasumbar.model.Comment;
import asywalul.minang.wisatasumbar.model.CommentWrapper;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.ConversationWrapper;
import asywalul.minang.wisatasumbar.model.Daerah;
import asywalul.minang.wisatasumbar.model.DaerahWrapper;
import asywalul.minang.wisatasumbar.model.LocationNearby;
import asywalul.minang.wisatasumbar.model.LocationNearbyWrapper;
import asywalul.minang.wisatasumbar.model.Resep;
import asywalul.minang.wisatasumbar.model.ResepWrapper;
import asywalul.minang.wisatasumbar.model.Store;
import asywalul.minang.wisatasumbar.model.StoreWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Video;
import asywalul.minang.wisatasumbar.model.VideoWrapper;
import asywalul.minang.wisatasumbar.model.Wisata;
import asywalul.minang.wisatasumbar.model.WisataWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;

/**
 * Created by asywalulfikri on 9/13/16.
 */
public class VolleyParsing {

    public static ConversationWrapper conversationParsing(String response) throws Exception {
        ConversationWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            if (!jsonObject.isNull("data")) {
                JSONArray dataJson = jsonObject.getJSONArray("data");
                int size = dataJson.length();

                if (size > 0) {
                    wrapper = new ConversationWrapper();

                    wrapper.list = new ArrayList<Conversation>();

                    for (int i = 0; i < size; i++) {
                        JSONObject itemJson = dataJson.getJSONObject(i);

                        Conversation conversation = new Conversation();
                        conversation.conversationId = (!itemJson.isNull(Cons.CONV_ID)) ? itemJson.getString(Cons.CONV_ID) : "";
                        conversation.content = (!itemJson.isNull(Cons.CONV_CONTENT)) ? itemJson.getString(Cons.CONV_CONTENT) : "";
                        conversation.tags = (!itemJson.isNull(Cons.CONV_TAGS)) ? itemJson.getString(Cons.CONV_TAGS) : "";
                        conversation.dateSubmitted = (!itemJson.isNull(Cons.CONV_DATE_SUBMITTED)) ? itemJson.getString(Cons.CONV_DATE_SUBMITTED) : "";
                        conversation.attachment = (!itemJson.isNull(Cons.CONV_ATTACHMENT)) ? itemJson.getString(Cons.CONV_ATTACHMENT) : "";
                        conversation.type = (!itemJson.isNull(Cons.CONV_TYPE)) ? itemJson.getString(Cons.CONV_TYPE) : "";
                        conversation.title = (!itemJson.isNull(Cons.CONV_TITLE)) ? itemJson.getString(Cons.CONV_TITLE) : "";
                        conversation.summary = (!itemJson.isNull(Cons.CONV_SUMMARY)) ? itemJson.getString(Cons.CONV_SUMMARY) : "";
                        conversation.latitude = (!itemJson.isNull(Cons.CONV_LATITUDE)) ? itemJson.getString(Cons.CONV_LATITUDE) : "";
                        conversation.longitude = (!itemJson.isNull(Cons.CONV_LONGITUDE)) ? itemJson.getString(Cons.CONV_LONGITUDE) : "";
                        conversation.isVoted = (itemJson.isNull(Cons.CONV_ISVOTED)) ? 0 : itemJson.getInt(Cons.CONV_ISVOTED);
                        conversation.totalResponses = (itemJson.isNull(Cons.CONV_TOTAL_RESPONSES)) ? 0 : itemJson.getInt(Cons.CONV_TOTAL_RESPONSES);
                        conversation.totalVotes = (itemJson.isNull(Cons.CONV_TOTAL_VOTE)) ? 0 : itemJson.getInt(Cons.CONV_TOTAL_VOTE);
                        conversation.time = (!itemJson.isNull(Cons.CONV_TIME)) ?  itemJson.getString(Cons.CONV_TIME) :"";
                        conversation.page = jsonObject.getInt("page");

                        conversation.user = new User();

                        conversation.user.userId = (!itemJson.isNull(Cons.KEY_ID)) ? itemJson.getString(Cons.KEY_ID) : "";
                        conversation.user.fullName = (!itemJson.isNull(Cons.KEY_NAME)) ? itemJson.getString(Cons.KEY_NAME) : "";
                        conversation.user.avatar = (!itemJson.isNull(Cons.KEY_AVATAR)) ? itemJson.getString(Cons.KEY_AVATAR) : "";
                        conversation.user.gender = (!itemJson.isNull(Cons.KEY_GENDER)) ? itemJson.getString(Cons.KEY_GENDER) : "";
                        conversation.user.status = (!itemJson.isNull(Cons.KEY_STATUS)) ? itemJson.getString(Cons.KEY_STATUS) : "";
                        conversation.user.msisdn = (!itemJson.isNull(Cons.KEY_MSISDN)) ? itemJson.getString(Cons.KEY_MSISDN) : "";
                        conversation.user.email  = (!itemJson.isNull(Cons.KEY_EMAIL)) ? itemJson.getString(Cons.KEY_EMAIL) : "";
                        conversation.user.birthDate = (!itemJson.isNull(Cons.KEY_BIRTH)) ? itemJson.getString(Cons.KEY_BIRTH) : "";

                        wrapper.list.add(conversation);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }

    public ArticlesWrapper articlesParsing(String response) throws Exception {

        ArticlesWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            if (!jsonObject.isNull("data")) {

                JSONArray dataJson = jsonObject.getJSONArray("data");
                int size = dataJson.length();

                if (size > 0) {
                    wrapper = new ArticlesWrapper();

                    wrapper.list = new ArrayList<Articles>();

                    for (int i = 0; i < size; i++) {
                        JSONObject itemJson = dataJson.getJSONObject(i);

                        Articles articles = new Articles();

                        articles.articlesId = (!itemJson.isNull(Cons.ARTICLES_ID)) ? itemJson.getString(Cons.ARTICLES_ID) : "";
                        articles.content = (!itemJson.isNull(Cons.ARTICLES_CONTENT)) ? itemJson.getString(Cons.ARTICLES_CONTENT) : "";
                        articles.tags = (!itemJson.isNull(Cons.ARTICLES_TAGS)) ? itemJson.getString(Cons.ARTICLES_TAGS) : "";
                        articles.dateSubmitted = (!itemJson.isNull(Cons.ARTICLES_DATE_SUBMITTED)) ? itemJson.getString(Cons.ARTICLES_DATE_SUBMITTED) : "";
                        articles.latitude = (!itemJson.isNull(Cons.ARTICLES_LATITUDE)) ? itemJson.getString(Cons.ARTICLES_LATITUDE) : "";
                        articles.longitude = (!itemJson.isNull(Cons.ARTICLES_LONGITUDE)) ? itemJson.getString(Cons.ARTICLES_LONGITUDE) : "";
                        articles.image_satu = (!itemJson.isNull(Cons.ARTICLES_IMAGE_1)) ? itemJson.getString(Cons.ARTICLES_IMAGE_1) : "";
                        articles.image_dua = (!itemJson.isNull(Cons.ARTICLES_IMAGE_2)) ? itemJson.getString(Cons.ARTICLES_IMAGE_2) : "";
                        articles.image_tiga = (!itemJson.isNull(Cons.ARTICLES_IMAGE_3)) ? itemJson.getString(Cons.ARTICLES_IMAGE_3) : "";
                        articles.title = (!itemJson.isNull(Cons.ARTICLES_TITLE)) ? itemJson.getString(Cons.ARTICLES_TITLE) : "";
                        articles.type = (!itemJson.isNull(Cons.ARTICLES_TYPE)) ? itemJson.getString(Cons.ARTICLES_TYPE) : "";
                        articles.category = (!itemJson.isNull(Cons.ARTICLES_CATEGORY)) ? itemJson.getString(Cons.ARTICLES_CATEGORY) : "";
                        articles.sumber_url = (!itemJson.isNull(Cons.ARTICLES_URL)) ? itemJson.getString(Cons.ARTICLES_URL) : "";
                        articles.summary = (!itemJson.isNull(Cons.ARTICLES_SUMMARY)) ? itemJson.getString(Cons.ARTICLES_SUMMARY) : "";

                        wrapper.list.add(articles);
                    }
                }
            }

            }catch(Exception e){
                e.printStackTrace();

                throw e;
            }

        return wrapper;
    }

    public WisataWrapper wisataParsing(String response) throws Exception {

        WisataWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            if (!jsonObject.isNull("data")) {

                JSONArray dataJson = jsonObject.getJSONArray("data");
                int size = dataJson.length();

                if (size > 0) {
                    wrapper = new WisataWrapper();

                    wrapper.list = new ArrayList<Wisata>();

                    for (int i = 0; i < size; i++) {
                        JSONObject itemJson = dataJson.getJSONObject(i);

                        Wisata wisata = new Wisata();

                        wisata.wisataId = (!itemJson.isNull(Cons.WISATA_ID)) ? itemJson.getString(Cons.WISATA_ID) : "";
                        wisata.content  = (!itemJson.isNull(Cons.WISATA_CONTENT)) ? itemJson.getString(Cons.WISATA_CONTENT) : "";
                        wisata.title    = (!itemJson.isNull(Cons.WISATA_TITLE)) ? itemJson.getString(Cons.WISATA_TITLE) : "";
                        wisata.tags     = (!itemJson.isNull(Cons.WISATA_TAGS)) ? itemJson.getString(Cons.WISATA_TAGS) : "";
                        wisata.dateSubmitted = (!itemJson.isNull(Cons.WISATA_DATE_SUBMITTED)) ? itemJson.getString(Cons.WISATA_DATE_SUBMITTED) : "";
                        wisata.latitude   = (!itemJson.isNull(Cons.WISATA_LATITUDE)) ? itemJson.getString(Cons.WISATA_LATITUDE) : "";
                        wisata.longitude  = (!itemJson.isNull(Cons.WISATA_LONGITUDE)) ? itemJson.getString(Cons.WISATA_LONGITUDE) : "";
                        wisata.attachment = (!itemJson.isNull(Cons.WISATA_ATTACHMENT)) ? itemJson.getString(Cons.WISATA_ATTACHMENT) : "";
                        wisata.location = (!itemJson.isNull(Cons.WISATA_LOCATION)) ? itemJson.getString(Cons.WISATA_LOCATION) : "";
                        wisata.category = (!itemJson.isNull(Cons.WISATA_CATEGORY)) ? itemJson.getString(Cons.WISATA_CATEGORY) : "";
                        wisata.sumber_url = (!itemJson.isNull(Cons.WISATA_SUMBER_URL)) ? itemJson.getString(Cons.WISATA_SUMBER_URL) : "";
                        wisata.daerah     = (!itemJson.isNull(Cons.WISATA_DAERAH)) ? itemJson.getString(Cons.WISATA_DAERAH) : "";
                        wisata.summary    = (!itemJson.isNull(Cons.WISATA_SUMMARY)) ? itemJson.getString(Cons.WISATA_SUMMARY) : "";

                        wrapper.list.add(wisata);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }


    public static User userLoginParsing(String stringJson) throws JSONException, WisataException {
        User user = null;

        Debug.i(stringJson);

        JSONObject jsonObj = (JSONObject) new JSONTokener(stringJson).nextValue();


        JSONArray profileJson = jsonObj.getJSONArray("login");

        for (int i = 0; i < profileJson.length(); i++) {

            user = new User();
            JSONObject itemJson = profileJson.getJSONObject(i);

            user.fullName  = (itemJson.isNull(Cons.KEY_NAME))       ? "" : itemJson.getString(Cons.KEY_NAME);
            user.userId    = (itemJson.isNull(Cons.KEY_ID))         ? "" : itemJson.getString(Cons.KEY_ID);
            user.phone     = (itemJson.isNull(Cons.KEY_MSISDN))     ? "" : itemJson.getString(Cons.KEY_MSISDN);
            user.gender    = (itemJson.isNull(Cons.KEY_GENDER))     ? "" : itemJson.getString(Cons.KEY_GENDER);
            user.birthDate = (itemJson.isNull(Cons.KEY_BIRTH))      ? "" : itemJson.getString(Cons.KEY_BIRTH);
            user.hobby     = (itemJson.isNull(Cons.KEY_HOBBY))      ? "" : itemJson.getString(Cons.KEY_HOBBY);
            user.avatar    = (itemJson.isNull(Cons.KEY_AVATAR))     ? "" : itemJson.getString(Cons.KEY_AVATAR);
            user.email     = (itemJson.isNull(Cons.KEY_EMAIL))      ? "" : itemJson.getString(Cons.KEY_EMAIL);
            user.createAt  = (itemJson.isNull(Cons.KEY_AVATAR))     ? "" : itemJson.getString(Cons.KEY_AVATAR);
            user.updatedAt = (itemJson.isNull(Cons.KEY_UPDATED_AT)) ? "" : itemJson.getString(Cons.KEY_UPDATED_AT);
            user.status    = (itemJson.isNull(Cons.KEY_STATUS))     ? "" : itemJson.getString(Cons.KEY_STATUS);
            user.typeLogin = "manual";


        }
        return user;

    }

    public VideoWrapper videoParsing(String response) throws Exception {

        VideoWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {

            if (!jsonObject.isNull("data")) {

                JSONArray dataJson = jsonObject.getJSONArray("data");
                int size = dataJson.length();

                if (size > 0) {
                    wrapper = new VideoWrapper();

                    wrapper.list = new ArrayList<Video>();

                    for (int i = 0; i < size; i++) {
                        JSONObject itemJson = dataJson.getJSONObject(i);

                        Video video = new Video();

                        video.videoId = (!itemJson.isNull(Cons.VIDEO_ID)) ? itemJson.getString(Cons.VIDEO_ID) : "";
                        video.url = (!itemJson.isNull(Cons.VIDEO_URL)) ? itemJson.getString(Cons.VIDEO_URL) : "";
                        video.avatar = (!itemJson.isNull(Cons.VIDEO_AVATAR)) ? itemJson.getString(Cons.VIDEO_AVATAR) : "";
                        video.title = (!itemJson.isNull(Cons.VIDEO_TITLE)) ? itemJson.getString(Cons.VIDEO_TITLE) : "";
                        video.channel = (!itemJson.isNull(Cons.VIDEO_CHANNEL)) ? itemJson.getString(Cons.VIDEO_CHANNEL) : "";
                        video.totalView = (itemJson.isNull(Cons.VIDEO_TOTAL_VIWE)) ? 0 : itemJson.getInt(Cons.VIDEO_TOTAL_VIWE);
                        video.dateSubmitted = (!itemJson.isNull(Cons.VIDEO_DATESUBMITTED)) ? itemJson.getString(Cons.VIDEO_DATESUBMITTED) : "";
                        video.category = (!itemJson.isNull(Cons.VIDEO_CATEGORY)) ? itemJson.getString(Cons.VIDEO_CATEGORY) : "";
                        video.isWatch = (itemJson.isNull(Cons.VIDEO_IS_WATCH)) ? 0 : itemJson.getInt(Cons.VIDEO_IS_WATCH);
                        wrapper.list.add(video);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }


    public static User userParsing(String stringJson) throws JSONException, WisataException {
        User user = null;

        Debug.i(stringJson);

        JSONObject jsonObj = (JSONObject) new JSONTokener(stringJson).nextValue();

        user = new User();

        user.fullName  = (jsonObj.isNull(Cons.KEY_NAME))      ? "" : jsonObj.getString(Cons.KEY_NAME);
        user.userId    = (jsonObj.isNull(Cons.KEY_ID))        ? "" : jsonObj.getString(Cons.KEY_ID);
        user.email     = (jsonObj.isNull(Cons.KEY_EMAIL))     ? "" : jsonObj.getString(Cons.KEY_EMAIL);
        user.status    = (jsonObj.isNull(Cons.KEY_STATUS))    ? "" : jsonObj.getString(Cons.KEY_STATUS);
        user.birthDate = (jsonObj.isNull(Cons.KEY_BIRTH))     ? "" : jsonObj.getString(Cons.KEY_BIRTH);
        user.hobby     = (jsonObj.isNull(Cons.KEY_HOBBY))     ? "" : jsonObj.getString(Cons.KEY_HOBBY);
        user.avatar    = (jsonObj.isNull(Cons.KEY_AVATAR))    ? "" : jsonObj.getString(Cons.KEY_AVATAR);
        user.gender    = (jsonObj.isNull(Cons.KEY_GENDER))    ? "" : jsonObj.getString(Cons.KEY_GENDER);
        user.latitude  = (jsonObj.isNull(Cons.KEY_LATITUDE))  ? "" : jsonObj.getString(Cons.KEY_LATITUDE);
        user.longitude = (jsonObj.isNull(Cons.KEY_LONGITUDE)) ? "" : jsonObj.getString(Cons.KEY_LONGITUDE);
        user.typeLogin = "manual";

        return user;
    }

    public static User userParsing2(String stringJson) throws JSONException, WisataException {
        User user = null;

        Debug.i(stringJson);

        JSONObject jsonObj = (JSONObject) new JSONTokener(stringJson).nextValue();

        user = new User();

        user.fullName  = (jsonObj.isNull(Cons.KEY_NAME))      ? "" : jsonObj.getString(Cons.KEY_NAME);
        user.userId    = (jsonObj.isNull(Cons.KEY_ID))        ? "" : jsonObj.getString(Cons.KEY_ID);
        user.email     = (jsonObj.isNull(Cons.KEY_EMAIL))     ? "" : jsonObj.getString(Cons.KEY_EMAIL);
        user.avatar    = (jsonObj.isNull(Cons.KEY_AVATAR))    ? "" : jsonObj.getString(Cons.KEY_AVATAR);
        user.gender    = (jsonObj.isNull(Cons.KEY_GENDER))    ? "" : jsonObj.getString(Cons.KEY_GENDER);
        user.typeLogin = "facebook";

        return user;
    }


    public static Comment postCommentParsing(String stringJson) throws Exception {
        Comment comment = null;

        JSONObject jsonObj = (JSONObject) new JSONTokener(stringJson).nextValue();

        JSONObject response = jsonObj.getJSONObject("response");
        JSONArray userprofile = jsonObj.getJSONArray("data");

        for (int i = 0; i < userprofile.length(); i++) {

            JSONObject itemJson = userprofile.getJSONObject(i);
            comment = new Comment();
            comment.responseId     = (!response.isNull(Cons.RESPONSE_ID))         ? response.getString(Cons.RESPONSE_ID) : "";
            comment.conversationId = (!response.isNull(Cons.CONV_ID))             ? response.getString(Cons.CONV_ID) : "";
            comment.content        = (!response.isNull(Cons.CONV_CONTENT))        ? response.getString(Cons.CONV_CONTENT) : "";
            comment.waktu          = (!response.isNull(Cons.CONV_DATE_SUBMITTED)) ? response.getString(Cons.CONV_DATE_SUBMITTED) : "";
            comment.timeunix       = (!response.isNull(Cons.TIME_UNIX))           ? response.getString(Cons.TIME_UNIX) : "";

            comment.user = new User();

            comment.user.userId    = (!itemJson.isNull(Cons.KEY_ID))        ? itemJson.getString(Cons.KEY_ID) : "";
            comment.user.fullName  = (!itemJson.isNull(Cons.KEY_NAME))      ? itemJson.getString(Cons.KEY_NAME) : "";
            comment.user.avatar    = (!itemJson.isNull(Cons.KEY_AVATAR))    ? itemJson.getString(Cons.KEY_AVATAR) : "";
            comment.user.email     = (!itemJson.isNull(Cons.KEY_EMAIL))     ? itemJson.getString(Cons.KEY_EMAIL) : "";
            comment.user.birthDate = (!itemJson.isNull(Cons.KEY_BIRTH))     ? itemJson.getString(Cons.KEY_BIRTH) : "";
            comment.user.gender    = (!itemJson.isNull(Cons.KEY_GENDER))    ? itemJson.getString(Cons.KEY_GENDER) : "";
            comment.user.hobby     = (!itemJson.isNull(Cons.KEY_HOBBY))     ? itemJson.getString(Cons.KEY_HOBBY) : "";
            comment.user.status    = (!itemJson.isNull(Cons.KEY_STATUS))    ? itemJson.getString(Cons.KEY_STATUS) : "";
            comment.user.msisdn    = (!itemJson.isNull(Cons.KEY_MSISDN))    ? itemJson.getString(Cons.KEY_MSISDN) : "";
            comment.user.latitude  = (!itemJson.isNull(Cons.KEY_LATITUDE))  ? itemJson.getString(Cons.KEY_LATITUDE) : "";
            comment.user.longitude = (!itemJson.isNull(Cons.KEY_LONGITUDE)) ? itemJson.getString(Cons.KEY_LONGITUDE) : "";

        }
        return comment;
    }


    public CommentWrapper commentParsing(String response) throws Exception {

        CommentWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("data");
            int size = dataJson.length();

            if (size > 0) {
                wrapper = new CommentWrapper();

                wrapper.list = new ArrayList<Comment>();

                for (int i = 0; i < size; i++) {
                    JSONObject itemJson = dataJson.getJSONObject(i);

                    Comment comment = new Comment();
                    comment.responseId     = (!itemJson.isNull(Cons.RESPONSE_ID))         ? itemJson.getString(Cons.RESPONSE_ID) : "";
                    comment.conversationId = (!itemJson.isNull(Cons.CONV_ID))             ? itemJson.getString(Cons.CONV_ID) : "";
                    comment.content        = (!itemJson.isNull(Cons.CONV_CONTENT))        ? itemJson.getString(Cons.CONV_CONTENT) : "";
                    comment.waktu          = (!itemJson.isNull(Cons.CONV_DATE_SUBMITTED)) ? itemJson.getString(Cons.CONV_DATE_SUBMITTED) : "";
                    comment.timeunix       = (!itemJson.isNull(Cons.TIME_UNIX))           ? itemJson.getString(Cons.TIME_UNIX) : "";
                    comment.user = new User();

                    comment.user.userId    = (!itemJson.isNull(Cons.KEY_ID))        ? itemJson.getString(Cons.KEY_ID) : "";
                    comment.user.fullName  = (!itemJson.isNull(Cons.KEY_NAME))      ? itemJson.getString(Cons.KEY_NAME) : "";
                    comment.user.avatar    = (!itemJson.isNull(Cons.KEY_AVATAR))    ? itemJson.getString(Cons.KEY_AVATAR) : "";
                    comment.user.email     = (!itemJson.isNull(Cons.KEY_EMAIL))     ? itemJson.getString(Cons.KEY_EMAIL) : "";
                    comment.user.birthDate = (!itemJson.isNull(Cons.KEY_BIRTH))     ? itemJson.getString(Cons.KEY_BIRTH) : "";
                    comment.user.gender    = (!itemJson.isNull(Cons.KEY_GENDER))    ? itemJson.getString(Cons.KEY_GENDER) : "";
                    comment.user.hobby     = (!itemJson.isNull(Cons.KEY_HOBBY))     ? itemJson.getString(Cons.KEY_HOBBY) : "";
                    comment.user.status    = (!itemJson.isNull(Cons.KEY_STATUS))    ? itemJson.getString(Cons.KEY_STATUS) : "";
                    comment.user.msisdn    = (!itemJson.isNull(Cons.KEY_MSISDN))    ? itemJson.getString(Cons.KEY_MSISDN) : "";
                    comment.user.latitude  = (!itemJson.isNull(Cons.KEY_LATITUDE))  ? itemJson.getString(Cons.KEY_LATITUDE) : "";
                    comment.user.longitude = (!itemJson.isNull(Cons.KEY_LONGITUDE)) ? itemJson.getString(Cons.KEY_LONGITUDE) : "";

                    wrapper.list.add(comment);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;

    }


    public static LocationNearbyWrapper locationParsing(String response) throws Exception {
        LocationNearbyWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("results");
            String status = jsonObject.getString("status");

            if (status.equals("ZERO_RESULTS")) {
                wrapper.list = null;
                return wrapper;
            } else {


                wrapper = new LocationNearbyWrapper();

                wrapper.list = new ArrayList<LocationNearby>();

                for (int i = 0; i < dataJson.length(); i++) {

                    if (dataJson.get(i).equals(null) || dataJson.equals("")) {
                        continue;
                    }
                    JSONObject itemJson = dataJson.getJSONObject(i);


                    LocationNearby nearby = new LocationNearby();
                    JSONObject geometry   = itemJson.getJSONObject("geometry");
                    JSONObject location   = geometry.getJSONObject("location");

                    String photos         = (!itemJson.isNull("photos")) ? itemJson.getString("photos") : "";
                    if (photos.equals("") || photos.equals("null")) {
                        continue;
                    } else {
                        JSONArray photoreference = itemJson.getJSONArray("photos");
                        JSONObject photo         = photoreference.getJSONObject(0);
                        nearby.foto              = (!photo.isNull(Cons.NEARBY_FOTO)) ? photo.getString(Cons.NEARBY_FOTO) : "";
                    }
                    String openingHours          = (!itemJson.isNull("opening_hours")) ? itemJson.getString("opening_hours") : "";
                    if (openingHours.equals("") || openingHours.equals("null")) {
                        continue;
                    } else {
                        JSONObject open          = itemJson.getJSONObject("opening_hours");
                        nearby.jambuka           = (!open.isNull(Cons.NEARBY_JAMBUKA)) ? open.getString(Cons.NEARBY_JAMBUKA) : "";
                    }

                    nearby.id        = (!itemJson.isNull(Cons.NEARBY_ID))        ? itemJson.getString(Cons.NEARBY_ID) : "";
                    nearby.name      = (!itemJson.isNull(Cons.NEARBY_NAME))      ? itemJson.getString(Cons.NEARBY_NAME) : "";
                    nearby.location  = (!itemJson.isNull(Cons.NEARBY_LOCATION))  ? itemJson.getString(Cons.NEARBY_LOCATION) : "";
                    nearby.icon      = (!itemJson.isNull(Cons.NEARBY_ICON))      ? itemJson.getString(Cons.NEARBY_ICON) : "";
                    nearby.latitude  = (!location.isNull(Cons.NEARBY_LATITUDE))  ? location.getString(Cons.NEARBY_LATITUDE) : "";
                    nearby.longitude = (!location.isNull(Cons.NEARBY_LONGITUDE)) ? location.getString(Cons.NEARBY_LONGITUDE) : "";


                    wrapper.list.add(nearby);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }


    public static StoreWrapper storeParsing(String response) throws Exception {
        StoreWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("data");
            int size = dataJson.length();

            if (size > 0) {
                wrapper = new StoreWrapper();

                wrapper.list = new ArrayList<Store>();

                for (int i = 0; i < size; i++) {
                    JSONObject itemJson = dataJson.getJSONObject(i);

                    Store store = new Store();
                    store.idbarang       = (!itemJson.isNull(Cons.STORE_ID))         ? itemJson.getString(Cons.STORE_ID) : "";
                    store.namabarang     = (!itemJson.isNull(Cons.STORE_NAME))       ? itemJson.getString(Cons.STORE_NAME) : "";
                    store.satuan         = (!itemJson.isNull(Cons.STORE_UNIT))       ? itemJson.getString(Cons.STORE_UNIT) : "";
                    store.foto           = (!itemJson.isNull(Cons.STORE_ATTACHMENT)) ? itemJson.getString(Cons.STORE_ATTACHMENT) : "";
                    store.pathh          = (!itemJson.isNull(Cons.STORE_PATH))       ? itemJson.getString(Cons.STORE_PATH) : "";
                    store.lokasi         = (!itemJson.isNull(Cons.STORE_LOCATION))   ? itemJson.getString(Cons.STORE_LOCATION) : "";
                    store.keterangan     = (!itemJson.isNull(Cons.STORE_INFORMATON)) ? itemJson.getString(Cons.STORE_INFORMATON) : "";
                    store.latitude       = (!itemJson.isNull(Cons.STORE_LATITUDE))   ? itemJson.getString(Cons.STORE_LATITUDE) : "";
                    store.longitude      = (!itemJson.isNull(Cons.STORE_LONGITUDE))  ? itemJson.getString(Cons.STORE_LONGITUDE) : "";
                    store.hargabarang    = (itemJson.isNull(Cons.STORE_PRICE))       ? 0 : itemJson.getInt(Cons.STORE_PRICE);


                    store.user = new User();

                    store.user.userId    = (!itemJson.isNull(Cons.KEY_ID))     ? itemJson.getString(Cons.KEY_ID) : "";
                    store.user.fullName  = (!itemJson.isNull(Cons.KEY_NAME))   ? itemJson.getString(Cons.KEY_NAME) : "";
                    store.user.avatar    = (!itemJson.isNull(Cons.KEY_AVATAR)) ? itemJson.getString(Cons.KEY_AVATAR) : "";
                    store.user.gender    = (!itemJson.isNull(Cons.KEY_GENDER)) ? itemJson.getString(Cons.KEY_GENDER) : "";
                    store.user.status    = (!itemJson.isNull(Cons.KEY_STATUS)) ? itemJson.getString(Cons.KEY_STATUS) : "";
                    store.user.msisdn    = (!itemJson.isNull(Cons.KEY_MSISDN)) ? itemJson.getString(Cons.KEY_MSISDN) : "";
                    store.user.email     = (!itemJson.isNull(Cons.KEY_EMAIL))  ? itemJson.getString(Cons.KEY_EMAIL) : "";
                    store.user.birthDate = (!itemJson.isNull(Cons.KEY_BIRTH))  ? itemJson.getString(Cons.KEY_BIRTH) : "";

                    wrapper.list.add(store);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }


    public static AngkutanWrapper angkutanParsing(String response) throws Exception {
        AngkutanWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("data");
            int size = dataJson.length();

            if (size > 0) {
                wrapper = new AngkutanWrapper();

                wrapper.list = new ArrayList<Angkutan>();

                for (int i = 0; i < size; i++) {
                    JSONObject itemJson = dataJson.getJSONObject(i);

                    Angkutan angkutan = new Angkutan();
                    angkutan.idangkutan     = (!itemJson.isNull(Cons.ANGKUTAN_ID))         ? itemJson.getString(Cons.ANGKUTAN_ID) : "";
                    angkutan.nama_perusahaan= (!itemJson.isNull(Cons.ANGKUTAN_NAME))       ? itemJson.getString(Cons.ANGKUTAN_NAME) : "";
                    angkutan.jumlahkendaraan= (!itemJson.isNull(Cons.ANGKUTAN_JUMLAH_KENDARAAN))? itemJson.getString(Cons.ANGKUTAN_JUMLAH_KENDARAAN) : "";
                    angkutan.foto           = (!itemJson.isNull(Cons.ANGKUTAN_FOTO))       ? itemJson.getString(Cons.ANGKUTAN_FOTO) : "";
                    angkutan.alamat         = (!itemJson.isNull(Cons.ANGKUTAN_ALAMAT))     ? itemJson.getString(Cons.ANGKUTAN_ALAMAT) : "";
                    angkutan.pemilik        = (!itemJson.isNull(Cons.ANGKUTAN_PEMILIK))    ? itemJson.getString(Cons.ANGKUTAN_PEMILIK) : "";
                    angkutan.keterangan     = (!itemJson.isNull(Cons.ANGKUTAN_KETERANGAN)) ? itemJson.getString(Cons.ANGKUTAN_KETERANGAN) : "";
                    angkutan.merek          = (!itemJson.isNull(Cons.ANGKUTAN_MEREK))      ? itemJson.getString(Cons.ANGKUTAN_MEREK) : "";
                    angkutan.kategori       = (!itemJson.isNull(Cons.ANGKUTAN_KATEGORI))   ? itemJson.getString(Cons.ANGKUTAN_KATEGORI) : "";

                    wrapper.list.add(angkutan);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }


    public static DaerahWrapper daerahParsing(String response) throws Exception {
        DaerahWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("data");
            int size = dataJson.length();

            if (size > 0) {
                wrapper = new DaerahWrapper();

                wrapper.list = new ArrayList<Daerah>();

                for (int i = 0; i < size; i++) {
                    JSONObject itemJson = dataJson.getJSONObject(i);

                    Daerah daerah = new Daerah();


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


    public static CeritaWrapper ceritaParsing(String response) throws Exception {
        CeritaWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("data");
            int size = dataJson.length();

            if (size > 0) {
                wrapper = new CeritaWrapper();

                wrapper.list = new ArrayList<Cerita>();

                for (int i = 0; i < size; i++) {
                    JSONObject itemJson = dataJson.getJSONObject(i);

                    Cerita cerita   = new Cerita();
                    cerita.id         = (!itemJson.isNull(Cons.CERITA_ID))         ? itemJson.getString(Cons.CERITA_ID) : "";
                    cerita.title      = (!itemJson.isNull(Cons.CERITA_TITLE))      ? itemJson.getString(Cons.CERITA_TITLE) : "";
                    cerita.content    = (!itemJson.isNull(Cons.CERITA_CONTENT))    ? itemJson.getString(Cons.CERITA_CONTENT) : "";
                    cerita.createdAt  = (!itemJson.isNull(Cons.CERITA_CREATED_AT)) ? itemJson.getString(Cons.CERITA_CREATED_AT) : "";
                    cerita.type       = (!itemJson.isNull(Cons.CERITA_TYPE))       ? itemJson.getString(Cons.CERITA_TYPE) : "";
                    cerita.image      = (!itemJson.isNull(Cons.CERITA_IMAGE))      ? itemJson.getString(Cons.CERITA_IMAGE) : "";
                    cerita.sumber     = (!itemJson.isNull(Cons.CERITA_SUMBER))     ? itemJson.getString(Cons.CERITA_SUMBER) : "";
                    cerita.category   = (!itemJson.isNull(Cons.CERITA_CATEGORY))   ? itemJson.getString(Cons.CERITA_CATEGORY) : "";


                    wrapper.list.add(cerita);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }

    public static ResepWrapper resepParsing(String response) throws Exception {
        ResepWrapper wrapper = null;
        JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

        try {
            JSONArray dataJson = jsonObject.getJSONArray("data");
            int size = dataJson.length();

            if (size > 0) {
                wrapper = new ResepWrapper();

                wrapper.list = new ArrayList<Resep>();

                for (int i = 0; i < size; i++) {
                    JSONObject itemJson = dataJson.getJSONObject(i);

                    Resep resep   = new Resep();
                    resep.idresep     = (!itemJson.isNull(Cons.RESEP_ID))         ? itemJson.getString(Cons.RESEP_ID) : "";
                    resep.title       = (!itemJson.isNull(Cons.RESEP_TITLE))      ? itemJson.getString(Cons.RESEP_TITLE) : "";
                    resep.content1    = (!itemJson.isNull(Cons.RESEP_CONTENT_1))  ? itemJson.getString(Cons.RESEP_CONTENT_1) : "";
                    resep.content2    = (!itemJson.isNull(Cons.RESEP_CONTENT_2))  ? itemJson.getString(Cons.RESEP_CONTENT_2) : "";
                    resep.image1      = (!itemJson.isNull(Cons.RESEP_IMAGE_1))    ? itemJson.getString(Cons.RESEP_IMAGE_1) : "";
                    resep.image2      = (!itemJson.isNull(Cons.RESEP_IMAGE_2))    ? itemJson.getString(Cons.RESEP_IMAGE_2) : "";
                    resep.sumber      = (!itemJson.isNull(Cons.RESEP_SUMBER))     ? itemJson.getString(Cons.RESEP_SUMBER) : "";
                    resep.dateSubmitted = (!itemJson.isNull(Cons.RESEP_DATE_SUBMITTED)) ? itemJson.getString(Cons.RESEP_DATE_SUBMITTED) : "";


                    wrapper.list.add(resep);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return wrapper;
    }

}