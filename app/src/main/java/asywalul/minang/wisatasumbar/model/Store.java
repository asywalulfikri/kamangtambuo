package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Store implements Parcelable {

    public String iduser;
    public String idbarang;
    public String namabarang;
    public int hargabarang;
    public String satuan;
    public String foto;
    public String pathh;
    public String keterangan;
    public String lokasi;
    public String time;
    public String latitude;
    public String longitude;

    public User user = new User();

    public Store() {}

    public Store(Parcel in) {
        iduser          = in.readString();
        idbarang 		= in.readString();
        namabarang	    = in.readString();
        hargabarang 	= in.readInt();
        satuan			= in.readString();
        foto        	= in.readString();
        pathh           = in.readString();
        keterangan      = in.readString();
        lokasi        	= in.readString();
        latitude        = in.readString();
        longitude       = in.readString();


        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {

        @Override
        public Store createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Store[size];
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
        out.writeString(idbarang);
        out.writeString(namabarang);
        out.writeInt(hargabarang);
        out.writeString(satuan);
        out.writeString(foto);
        out.writeString(pathh);
        out.writeString(keterangan);
        out.writeString(lokasi);
        out.writeString(latitude);


        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
