package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rio on 25/01/16.
 */
public class Header implements Parcelable {
    public String authorization;
    public String url;

    public Header() {}

    public Header(Parcel in) {
        authorization = in.readString();
        url = in.readString();
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authorization);
        dest.writeString(url);
    }
}
