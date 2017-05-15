package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Weather implements Parcelable {

    public String all;
    public String kota;
    public String descToday;
    public String suhuToday;
    public String kelembabanToday;
    public String kecepatanAnginToday;
    public String arahAnginToday;
    public String descBesok;
    public String suhuBesok;
    public String kelembabanBesok;
    public String kecepatanAnginBesok;
    public String arahAnginBesok;

    public User user = new User();

    public Weather() {
    }

    public Weather(Parcel in) {
        all					= in.readString();
        kota 				= in.readString();
        descToday 			= in.readString();
        suhuToday			= in.readString();
        kelembabanToday 	= in.readString();
        kecepatanAnginToday = in.readString();
        arahAnginToday 		= in.readString();
        descBesok 			= in.readString();
        suhuBesok 			= in.readString();
        kelembabanBesok 	= in.readString();
        kecepatanAnginBesok = in.readString();
        arahAnginBesok 		= in.readString();

    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {

        @Override
        public Weather createFromParcel(Parcel in) {

            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {

            return new Weather[size];
        }
    };

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(all);
        out.writeString(kota);
        out.writeString(descToday);
        out.writeString(suhuToday);
        out.writeString(kelembabanToday);
        out.writeString(kecepatanAnginToday);
        out.writeString(arahAnginToday);
        out.writeString(descBesok);
        out.writeString(suhuBesok);
        out.writeString(kelembabanBesok);
        out.writeString(kecepatanAnginBesok);
        out.writeString(arahAnginBesok);
    }
}
