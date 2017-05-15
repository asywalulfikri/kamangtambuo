package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Kuliner implements Parcelable {

    public String iduser;
    public String idkuliner;
    public String namakuliner;
    public String foto;
    public String foto1;
    public String foto2;
    public String foto3;
    public String keterangan;


    public User user = new User();

    public Kuliner() {}

    public Kuliner(Parcel in) {
        iduser          = in.readString();
        idkuliner 		= in.readString();
        namakuliner	    = in.readString();
        foto        	= in.readString();
        foto1          = in.readString();
        foto2          = in.readString();
        foto3          = in.readString();
        keterangan      = in.readString();



        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Kuliner> CREATOR = new Creator<Kuliner>() {

        @Override
        public Kuliner createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Kuliner(in);
        }

        @Override
        public Kuliner[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Kuliner[size];
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
        out.writeString(iduser);
        out.writeString(idkuliner);
        out.writeString(namakuliner);
        out.writeString(foto);
        out.writeString(foto1);
        out.writeString(foto2);
        out.writeString(foto3);
        out.writeString(keterangan);

        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
