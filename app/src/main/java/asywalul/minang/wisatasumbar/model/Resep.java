package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asywalulfikri on 1/23/17.
 */

public class Resep implements Parcelable {

    public String idresep;
    public String title;
    public String content1;
    public String content2;
    public String image1;
    public String image2;
    public String sumber;
    public String dateSubmitted;


    public User user = new User();

    public Resep() {}

    public Resep(Parcel in) {
        idresep    = in.readString();
        title      = in.readString();
        content1   = in.readString();
        content2   = in.readString();
        image1     = in.readString();
        image2     = in.readString();
        sumber     = in.readString();
        dateSubmitted = in.readString();



        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Resep> CREATOR = new Creator<Resep>() {

        @Override
        public Resep createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Resep(in);
        }

        @Override
        public Resep[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Resep[size];
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
        out.writeString(idresep);
        out.writeString(title);
        out.writeString(content1);
        out.writeString(content2);
        out.writeString(image1);
        out.writeString(image2);
        out.writeString(sumber);
        out.writeString(dateSubmitted);
        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
