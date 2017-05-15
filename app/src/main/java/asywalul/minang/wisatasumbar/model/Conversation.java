package asywalul.minang.wisatasumbar.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Conversation implements Parcelable {


    public String latitude;
    public String longitude;
    public String tags ;
    public String id;
    public String userId;
    public String title;
    public String content;
    public String location;
    public String time;
    public String question;
    public String typeContent;
    public String options;

    public String summary;
    public String attachment;
    public String conversationId;
    public String type;
    public int hasAds;
    public int totalResponses;
    public int totalMembers;
    public int totalViews;
    public int totalVotes;
    public int totalShares;


    public int isMember;
    public int isVoted;
    public int price;
    public int page;
    public String dateSubmitted;
    public long lastViewed;
    public long lastActivityTime;

    public String custom_source;

    public User user = new User();

    public ArrayList<Response> responses = new ArrayList<Response>();

    public String[] value = new String[0];

    public String productTitle;
    public String productStatus;
    public String productPrice;
    public String productCategory;
    public String productUnit;
    public String productImageProduct;
    public String productHeadType;
    public String productStock;

    public int productIsBuy;
    public int productIsPaid;
    public int productIsNewOrder;
    public int productOnProcess;

    public int shortId;
    public String priceStore;

    public long eventStartTime;
    public long eventEndTime;

    public String eventStartDate;
    public String eventEndDate;
    public String eventLocation;
    public String eventTime;

    public String state;

    public Conversation() {
    }

    public Conversation(Parcel in) {
        id = in.readString();
        userId = in.readString();
        title = in.readString();
        content = in.readString();
        location = in.readString();
        time   = in.readString();
        question = in.readString();
        typeContent = in.readString();
        options = in.readString();

        summary = in.readString();
        attachment = in.readString();
        conversationId = in.readString();
        type = in.readString();
        hasAds = in.readInt();
        totalResponses = in.readInt();
        totalMembers = in.readInt();
        totalViews = in.readInt();
        totalVotes = in.readInt();
        totalShares = in.readInt();
        isMember = in.readInt();
        isVoted	= in.readInt();
        price = in.readInt();
        page = in.readInt();
        dateSubmitted = in.readString();
        lastViewed = in.readLong();
        lastActivityTime = in.readLong();

        custom_source = in.readString();

        user = User.CREATOR.createFromParcel(in);

        in.readTypedList(responses, Response.CREATOR);

        value               = in.createStringArray();

        latitude            = in.readString();
        longitude           = in.readString();
        tags                = in.readString();
        productTitle 		= in.readString();
        productStatus		= in.readString();
        productPrice		= in.readString();
        productCategory		= in.readString();
        productUnit			= in.readString();
        productImageProduct = in.readString();
        productHeadType		= in.readString();
        productStock        = in.readString();
        priceStore          = in.readString();



        productIsBuy		= in.readInt();
        productIsNewOrder	= in.readInt();
        productIsPaid		= in.readInt();
        productOnProcess	= in.readInt();
        shortId	= in.readInt();

        eventStartTime      = in.readLong();
        eventEndTime        = in.readLong();

        eventStartDate      = in.readString();
        eventEndDate        = in.readString();
        eventLocation       = in.readString();
        eventTime           = in.readString();

        state	           	= in.readString();
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {

        @Override
        public Conversation createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Conversation[size];
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
        out.writeString(userId);
        out.writeString(title);
        out.writeString(content);
        out.writeString(location);
        out.writeString(time);
        out.writeString(question);
        out.writeString(typeContent);
        out.writeString(options);

        out.writeString(summary);
        out.writeString(attachment);
        out.writeString(conversationId);
        out.writeString(type);
        out.writeInt(hasAds);
        out.writeInt(totalResponses);
        out.writeInt(totalMembers);
        out.writeInt(totalViews);
        out.writeInt(totalVotes);
        out.writeInt(totalShares);
        out.writeInt(isMember);
        out.writeInt(isVoted);
        out.writeInt(price);
        out.writeInt(page);
        out.writeString(dateSubmitted);
        out.writeLong(lastViewed);
        out.writeLong(lastActivityTime);
        out.writeString(custom_source);

        user.writeToParcel(out, flags);

        out.writeTypedList(responses);
        out.writeStringArray(value);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(tags);
        out.writeString(productTitle);
        out.writeString(productStatus);
        out.writeString(productPrice);
        out.writeString(productCategory);
        out.writeString(productUnit);
        out.writeString(productImageProduct);
        out.writeString(productHeadType);
        out.writeString(productStock);
        out.writeString(priceStore);

        out.writeInt(productIsBuy);
        out.writeInt(productIsNewOrder);
        out.writeInt(productIsPaid);
        out.writeInt(productOnProcess);
        out.writeInt(shortId);
        out.writeLong(eventStartTime);
        out.writeLong(eventEndTime);

        out.writeString(eventStartDate);
        out.writeString(eventEndDate);
        out.writeString(eventLocation);
        out.writeString(eventTime);

        out.writeString(state);
    }

}
