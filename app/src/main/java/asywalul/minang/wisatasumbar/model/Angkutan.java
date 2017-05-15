package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Angkutan implements Parcelable {

    public String idangkutan;
    public String nama_perusahaan;
    public String pemilik;
    public String alamat;
    public String jumlahkendaraan;
    public String merek;
    public String kategori;
    public String keterangan;
    public String foto;


    public User user = new User();

    public  Angkutan() {}

    public  Angkutan(Parcel in) {
        idangkutan      = in.readString();
        nama_perusahaan = in.readString();
        pemilik	        = in.readString();
        alamat       	= in.readString();
        jumlahkendaraan = in.readString();
        merek          = in.readString();
        kategori       = in.readString();
        keterangan     = in.readString();
        foto           = in.readString();



        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Angkutan> CREATOR = new Creator<Angkutan>() {

        @Override
        public  Angkutan createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new  Angkutan(in);
        }

        @Override
        public  Angkutan[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Angkutan[size];
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
        out.writeString(idangkutan);
        out.writeString(nama_perusahaan);
        out.writeString(pemilik);
        out.writeString(alamat);
        out.writeString(jumlahkendaraan);
        out.writeString(merek);
        out.writeString(kategori);
        out.writeString(keterangan);
        out.writeString(foto);

        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}
