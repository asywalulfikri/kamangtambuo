package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toshiba on 4/13/2016.
 */
public class Wisata  implements Parcelable {

    public String wisataId;
    public String content;
    public String tags;
    public String attachment;
    public String dateSubmitted;
    public String title;
    public String location;
    public String sumber_url;
    public String category;
    public String latitude;
    public String longitude;
    public String daerah;
    public String summary;

    //////////////////
    public int totalComments;
    public int totalViews;
    public int totalResponses;
    public int totalUpvotes;
    public int totalDownvotes;
    public long lastViewed;
    public long lastActivityTime;
    public int isVoted;
    public int totalVotes;

    public User user = new User();

    public  Wisata () {}

    public  Wisata (Parcel in) {
        wisataId      = in.readString();
        content 		= in.readString();
        tags	    	= in.readString();
        attachment	    = in.readString();
        dateSubmitted 	= in.readString();
        title           = in.readString();
        location        = in.readString();
        sumber_url      = in.readString();
        category        = in.readString();
        latitude        = in.readString();
        longitude       = in.readString();
        daerah          = in.readString();
        summary         = in.readString();


        totalComments 	= in.readInt();
        totalViews 		= in.readInt();
        totalResponses 	= in.readInt();
        totalUpvotes 	= in.readInt();
        totalDownvotes 	= in.readInt();
        lastViewed 		= in.readLong();
        lastActivityTime = in.readLong();
        isVoted         = in.readInt();
        totalVotes      = in.readInt();


        user = User.CREATOR.createFromParcel(in);
    }

    public static final Creator< Wisata > CREATOR = new Creator< Wisata >() {

        @Override
        public Wisata  createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new  Wisata (in);
        }

        @Override
        public  Wisata [] newArray(int size) {
            // TODO Auto-generated method stub
            return new Wisata [size];
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
        out.writeString(wisataId);
        out.writeString(content);
        out.writeString(tags);
        out.writeString(attachment);
        out.writeString(dateSubmitted);
        out.writeString(title);
        out.writeString(location);
        out.writeString(sumber_url);
        out.writeString(category);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(daerah);
        out.writeString(summary);
        out.writeInt(totalComments);
        out.writeInt(totalViews);
        out.writeInt(totalResponses);
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


