package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

	public String iduser;
	public String content;
	public String tags;
	public String foto;
	public String pathh;
	public String idquestion;
	public String time;
	public int totalComments;
	public int totalViews;
	public int totalResponse;
	public int totalUpvotes;
	public int totalDownvotes;
	public long dateSubmitted;
	public long lastViewed;
	public long lastActivityTime;
	public int isVoted;
	public int totalVotes;
	
	public User user = new User();

	public Question() {}

	public Question(Parcel in) {
		iduser = in.readString();
		content 		= in.readString();
		tags	    	= in.readString();
		foto 			= in.readString();
		pathh			= in.readString();
		idquestion   	= in.readString();
		time 	        = in.readString();
		totalComments 	= in.readInt();
		totalViews 		= in.readInt();
		totalResponse 	= in.readInt();
		totalUpvotes 	= in.readInt();
		totalDownvotes 	= in.readInt();
		dateSubmitted 	= in.readLong();
		lastViewed 		= in.readLong();
		lastActivityTime = in.readLong();
		isVoted         = in.readInt();
		totalVotes      = in.readInt();


		user = User.CREATOR.createFromParcel(in);
	}
	
	public static final Creator<Question> CREATOR = new Creator<Question>() {

		@Override
		public Question createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Question(in);
		}

		@Override
		public Question[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Question[size];
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
		out.writeString(content);
		out.writeString(tags);
		out.writeString(foto);
		out.writeString(pathh);
		out.writeString(idquestion);
		out.writeString(time);
		out.writeInt(totalComments);
		out.writeInt(totalViews);
		out.writeInt(totalResponse);
		out.writeInt(totalUpvotes);
		out.writeInt(totalDownvotes);
		out.writeLong(dateSubmitted);
		out.writeLong(lastViewed);
		out.writeLong(lastActivityTime);
		out.writeInt(isVoted);
		out.writeInt(totalVotes);
		
		if (user != null) {
			user.writeToParcel(out, flags);
		}
	}

}
