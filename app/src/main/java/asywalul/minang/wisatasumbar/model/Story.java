package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toshiba on 4/13/2016.
 */
public class Story  implements Parcelable {

    public String storyId;
    public String content;
    public String tags;
    public String attachment;
    public String dateSubmitted;
    public String title;
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

    public  Story() {}

    public  Story(Parcel in) {
        storyId      = in.readString();
        content 		= in.readString();
        tags	    	= in.readString();
        attachment	    = in.readString();
        dateSubmitted 	= in.readString();
        title           = in.readString();
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

    public static final Creator< Story> CREATOR = new Creator< Story>() {

        @Override
        public Story createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new  Story(in);
        }

        @Override
        public  Story[] newArray(int size) {
            // TODO Auto-generated method stub
            return new  Story[size];
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
        out.writeString(storyId);
        out.writeString(content);
        out.writeString(tags);
        out.writeString(attachment);
        out.writeString(dateSubmitted);
        out.writeString(title);
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


