package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    public String conversationId;
    public String responseId;
    public String content;
    public String time;
    public String username;
    public String foto;
    public String pathhh;
    public String waktu;
    public String timeunix;


    public User user = new User();

    public Comment() {}

    public Comment(Parcel in) {
        conversationId = in.readString();
        responseId     = in.readString();
        content        = in.readString();
        time           = in.readString();
        username       = in.readString();
        foto           = in.readString();
        pathhh         = in.readString();
        waktu          = in.readString();
        timeunix       = in.readString();

        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {

        @Override
        public Comment createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Comment[size];
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
        out.writeString(conversationId);
        out.writeString(responseId);
        out.writeString(content);
        out.writeString(time);
        out.writeString(username);
        out.writeString(foto);
        out.writeString(pathhh);
        out.writeString(waktu);
        out.writeString(timeunix);

        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
