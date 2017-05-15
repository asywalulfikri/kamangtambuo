package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    public String videoId;
    public String url;
    public String avatar;
    public String channel;
    public String title;
    public String dateSubmitted;
    public String category;
    public int totalView;
    public int isWatch;



    public Video() {
    }

    public Video(Parcel in) {
        videoId       = in.readString();
        url           = in.readString();
        avatar        = in.readString();
        channel       = in.readString();
        title         = in.readString();
        dateSubmitted = in.readString();
        category      = in.readString();
        totalView     = in.readInt();
        isWatch       = in.readInt();


    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {

        @Override
        public Video[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Video[size];
        }

        @Override
        public Video createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Video(in);
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
        out.writeString(videoId);
        out.writeString(url);
        out.writeString(avatar);
        out.writeString(channel);
        out.writeString(title);
        out.writeString(dateSubmitted);
        out.writeString(category);
        out.writeInt(totalView);
        out.writeInt(isWatch);


    }

}
