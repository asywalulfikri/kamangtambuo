package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationNearby implements Parcelable {

    public String id;
    public String name;
    public String  jambuka;
    public String foto;
    public String type;
    public String location;
    public String latitude;
    public String longitude;
    public String icon;
    public String status;


    public User user = new User();

    public LocationNearby() {}

    public LocationNearby(Parcel in) {
        id              = in.readString();
        name 		    = in.readString();
        jambuka	    	= in.readString();
        foto 			= in.readString();
        type			= in.readString();
        location      	= in.readString();
        latitude        = in.readString();
        longitude       = in.readString();
        icon            = in.readString();
        status          = in.readString();

        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<LocationNearby> CREATOR = new Creator<LocationNearby>() {

        @Override
        public  LocationNearby createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new  LocationNearby(in);
        }

        @Override
        public LocationNearby[] newArray(int size) {
            // TODO Auto-generated method stub
            return new  LocationNearby[size];
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
        out.writeString(jambuka);
        out.writeString(foto);
        out.writeString(type);
        out.writeString(location);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(icon);
        out.writeString(status);



        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
