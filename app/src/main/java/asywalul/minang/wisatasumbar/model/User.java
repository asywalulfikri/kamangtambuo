package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	public String userId;
	public String email;
	public String fullName;
	public String phone;
	public String birthDate;
	public String createAt;
	public String gender;
	public String hobby;
	public String location;
	public String state;
	public String subdistrict;
	public String avatar;
	public String storeOwner;
	public String stringPoint;
	public String status;
	public String updatedAt;
	public String msisdn;
	public String latitude;
	public String longitude;
	public String isLogin;
	public String typeLogin;

	public int points = 0;
	public int forums = 0;
	public int questions = 0;
	public int articles = 0;
	public int responses = 0;
	public int storeItems = 0;


	public User() {
	}

	public User(Parcel in) {
		userId      = in.readString();
		email       = in.readString();
		fullName    = in.readString();
		phone       = in.readString();
		gender      = in.readString();
		birthDate   = in.readString();
		createAt    = in.readString();
		hobby       = in.readString();
		location    = in.readString();
		state       = in.readString();
		subdistrict = in.readString();
		avatar      = in.readString();
		storeOwner  = in.readString();
		stringPoint = in.readString();
		status		= in.readString();
		updatedAt   = in.readString();
		msisdn  	= in.readString();
		latitude 	= in.readString();
		longitude	= in.readString();
		isLogin     = in.readString();
		typeLogin   = in.readString();
		points      = in.readInt();
		forums      = in.readInt();
		questions   = in.readInt();
		articles    = in.readInt();
		responses   = in.readInt();
		storeItems  = in.readInt();

	}

	public static final Creator<User> CREATOR = new Creator<User>() {

		@Override
		public User[] newArray(int size) {
			// TODO Auto-generated method stub
			return new User[size];
		}

		@Override
		public User createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new User(in);
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
		out.writeString(userId);
		out.writeString(email);
		out.writeString(fullName);
		out.writeString(phone);
		out.writeString(gender);
		out.writeString(birthDate);
		out.writeString(createAt);
		out.writeString(hobby);
		out.writeString(location);
		out.writeString(state);
		out.writeString(subdistrict);
		out.writeString(avatar);
		out.writeString(storeOwner);
		out.writeString(stringPoint);
		out.writeString(status);
		out.writeString(updatedAt);
		out.writeString(msisdn);
		out.writeString(latitude);
		out.writeString(longitude);
		out.writeString(isLogin);
		out.writeString(typeLogin);

		out.writeInt(points);
		out.writeInt(forums);
		out.writeInt(questions);
		out.writeInt(articles);
		out.writeInt(responses);
		out.writeInt(storeItems);

	}

}
