package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toshiba on 4/13/2016.
 */
public class Articles  implements Parcelable {

    public String articlesId;
    public String content;
    public String tags;
    public String image_satu;
    public String image_dua;
    public String image_tiga;
    public String dateSubmitted;
    public String title;
    public String summary;
    public String sumber_url;
    public String type;
    public String category;
    public String latitude;
    public String longitude;

    //////////////////
    public int totalComments;
    public int totalViews;
    public int totalResponse;
    public int totalUpvotes;
    public int totalDownvotes;
    public long lastViewed;
    public long lastActivityTime;
    public int isVoted;
    public int totalVotes;

    public User user = new User();

    public Articles() {}

    public Articles(Parcel in) {
        articlesId      = in.readString();
        content 		= in.readString();
        tags	    	= in.readString();
        image_satu	    = in.readString();
        image_dua	    = in.readString();
        image_tiga	    = in.readString();
        dateSubmitted 	= in.readString();
        title           = in.readString();
        summary         = in.readString();
        sumber_url      = in.readString();
        type            = in.readString();
        category        = in.readString();
        latitude        = in.readString();
        longitude       = in.readString();


        totalComments 	= in.readInt();
        totalViews 		= in.readInt();
        totalResponse 	= in.readInt();
        totalUpvotes 	= in.readInt();
        totalDownvotes 	= in.readInt();
        lastViewed 		= in.readLong();
        lastActivityTime = in.readLong();
        isVoted         = in.readInt();
        totalVotes      = in.readInt();


        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator<Articles> CREATOR = new Creator<Articles>() {

        @Override
        public Articles createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Articles(in);
        }

        @Override
        public Articles[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Articles[size];
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
        out.writeString(articlesId);
        out.writeString(content);
        out.writeString(tags);
        out.writeString(image_satu);
        out.writeString(image_dua);
        out.writeString(image_tiga);
        out.writeString(dateSubmitted);
        out.writeString(title);
        out.writeString(summary);
        out.writeString(sumber_url);
        out.writeString(type);
        out.writeString(category);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeInt(totalComments);
        out.writeInt(totalViews);
        out.writeInt(totalResponse);
        out.writeInt(totalUpvotes);
        out.writeInt(totalDownvotes);
        out.writeLong(lastViewed);
        out.writeLong(lastActivityTime);
        out.writeInt(isVoted);
        out.writeInt(totalVotes);

        if (user != null) {
            user.writeToParcel(out, flags);
        }
    }

}


