package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Daerah implements Parcelable {

    public String idk;
    public String namakabupaten;
    public String namabupati;
    public String luaswilayah;
    public String ibukota;
    public String jumlahkecamatan;
    public String jumlahwisata;
    public String keterangan;
    public String foto;


    public User user = new User();

    public Daerah() {}

    public Daerah(Parcel in) {
        idk = in.readString();
        namakabupaten = in.readString();
        namabupati = in.readString();
        luaswilayah = in.readString();
        ibukota = in.readString();
        jumlahkecamatan = in.readString();
        jumlahwisata = in.readString();
        keterangan      = in.readString();
        foto = in.readString();



        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Daerah> CREATOR = new Creator<Daerah>() {

        @Override
        public Daerah createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Daerah(in);
        }

        @Override
        public Daerah[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Daerah[size];
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
        out.writeString(idk);
        out.writeString(namakabupaten);
        out.writeString(namabupati);
        out.writeString(luaswilayah);
        out.writeString(ibukota);
        out.writeString(jumlahkecamatan);
        out.writeString(jumlahwisata);
        out.writeString(keterangan);
        out.writeString(foto);

        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
