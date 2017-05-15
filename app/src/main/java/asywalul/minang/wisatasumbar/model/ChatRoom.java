package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toshiba on 3/14/2016.
 */
public class ChatRoom implements Parcelable {

    public String id;
    public String name;
    public String price;
    public String avatar;
    public String type;
    public String conversationId;
    public String lastMessage;
    public String lastMessageBy;
    public String userId;
    public String userPhone;
    public String userAvatar;
    public String userName;
    public String isUnread;
    public String timestamp;
    public String storeOwner;

    public ChatRoom() {}

    public ChatRoom(Parcel in) {
        id              = in.readString();
        name 		    = in.readString();
        price 		    = in.readString();
        avatar 	        = in.readString();
        type            = in.readString();
        conversationId  = in.readString();
        lastMessage     = in.readString();
        lastMessageBy   = in.readString();
        userId          = in.readString();
        userPhone       = in.readString();
        userAvatar      = in.readString();
        userName        = in.readString();
        isUnread        = in.readString();
        timestamp       = in.readString();
        storeOwner       = in.readString();
    }

    public static final Creator<ChatRoom> CREATOR = new Creator<ChatRoom>() {

        @Override
        public ChatRoom createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new ChatRoom(in);
        }

        @Override
        public ChatRoom[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ChatRoom[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(name);
        out.writeString(price);
        out.writeString(avatar);
        out.writeString(type);
        out.writeString(conversationId);
        out.writeString(lastMessage);
        out.writeString(lastMessageBy);
        out.writeString(userId);
        out.writeString(userPhone);
        out.writeString(userAvatar);
        out.writeString(userName);
        out.writeString(isUnread);
        out.writeString(timestamp);
        out.writeString(storeOwner);
    }

}
