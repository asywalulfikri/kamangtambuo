package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AsywalulFikri on 6/7/16.
 */
public class Inbox implements Parcelable {

    public String chatId;
    public String title;
    public String time;


    public User user = new User();

    public Inbox() {}

    public Inbox(Parcel in) {
        chatId = in.readString();
        title  = in.readString();
        time   = in.readString();


        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Inbox> CREATOR = new Creator<Inbox>() {
        @Override
        public Inbox createFromParcel(Parcel in) {
            return new Inbox(in);
        }

        @Override
        public Inbox[] newArray(int size) {
            return new Inbox[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chatId);
        dest.writeString(title);
        dest.writeString(time);


        user.writeToParcel(dest, flags);
    }
}
