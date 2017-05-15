package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Chat implements Parcelable {

    public String idchat;
    public String idinbox;
    public String iduser;
    public String title;
    public String pathinbox;
    public String time;


    public User user = new User();

    public Chat() {}

    public Chat(Parcel in) {
        idchat = in.readString();
        idinbox = in.readString();
        iduser = in.readString();
        title = in.readString();
        pathinbox = in.readString();
        time = in.readString();

        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {

        @Override
        public Chat createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Chat[size];
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
        out.writeString(idchat);
        out.writeString(idinbox);
        out.writeString(iduser);
        out.writeString(title);
        out.writeString(pathinbox);
        out.writeString(time);

        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
