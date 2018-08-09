package ro.atoming.abnrnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleSource implements Parcelable{
    private String mId;
    private String mName;
    private String mDescription;
    private String mUrl;
    private String mCategory;
    private String mLanguage;
    private String mCountry;

    public ArticleSource(String id, String name, String description, String url, String category,
                         String language, String country){
        mId = id;
        mName = name;
        mDescription = description;
        mUrl = url;
        mCategory = category;
        mLanguage = language;
        mCountry = country;
    }

    public ArticleSource(String id, String name){
        mId = id;
        mName = name;
    }

    protected ArticleSource(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mDescription = in.readString();
        mUrl = in.readString();
        mCategory = in.readString();
        mLanguage = in.readString();
        mCountry = in.readString();
    }

    public String getSourceName() {
        return mName;
    }

    public String getId() {
        return mId;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getSourceUrl() {
        return mUrl;
    }

    public String getSourceCategory() {
        return mCategory;
    }

    public String getSourceLanguage() {
        return mLanguage;
    }

    public String getSourceCountry() {
        return mCountry;
    }

    public static final Creator<ArticleSource> CREATOR = new Creator<ArticleSource>() {
        @Override
        public ArticleSource createFromParcel(Parcel in) {
            return new ArticleSource(in);
        }

        @Override
        public ArticleSource[] newArray(int size) {
            return new ArticleSource[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mDescription);
        parcel.writeString(mUrl);
        parcel.writeString(mCategory);
        parcel.writeString(mLanguage);
        parcel.writeString(mCountry);
    }
}
