package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable {

	public String id;
	public String conversationId;
	public String communityId;
	public String userId;
	public String content;
	public String image;
	public long dateSubmitted;
	
	public asywalul.minang.wisatasumbar.model.User user = new asywalul.minang.wisatasumbar.model.User();
	
	public Response() {}
	
	public Response(Parcel in) {
		id 				= in.readString();
		conversationId	= in.readString();
		communityId		= in.readString();
		userId			= in.readString();
		content 		= in.readString();
		image			= in.readString();
		dateSubmitted 	= in.readLong();
		
		user = asywalul.minang.wisatasumbar.model.User.CREATOR.createFromParcel(in);
	}
	
	public static final Creator<Response> CREATOR = new Creator<Response>() {
		
		@Override
		public Response[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Response[size];
		}
		
		@Override
		public Response createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Response(in);
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
		out.writeString(conversationId);
		out.writeString(communityId);
		out.writeString(userId);
		out.writeString(content);
		out.writeString(image);
		out.writeLong(dateSubmitted);
		
		user.writeToParcel(out, flags);
	}

}
