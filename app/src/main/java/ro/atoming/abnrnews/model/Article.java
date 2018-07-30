package ro.atoming.abnrnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable{
    private ArticleSource mArticleSource;
    private String mAuthor;
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mImage;
    private String mDate;

    public Article(ArticleSource articleSource,String author, String title, String description,String url,String image,String date){
        mArticleSource = articleSource;
        mAuthor = author;
        mTitle = title;
        mDescription = description;
        mUrl = url;
        mImage = image;
        mDate = date;
    }

    protected Article(Parcel in) {
        mArticleSource = in.readParcelable(ArticleSource.class.getClassLoader());
        mAuthor = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mUrl = in.readString();
        mImage = in.readString();
        mDate = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getAuthor() {
        return mAuthor;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getDescription(){
        return mDescription;
    }
    public String getUrl(){
        return mUrl;
    }
    public String getImage(){
        return mImage;
    }
    public String getDate(){
        return mDate;
    }
    public ArticleSource getSource(){
        return mArticleSource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mArticleSource, i);
        parcel.writeString(mAuthor);
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mUrl);
        parcel.writeString(mImage);
        parcel.writeString(mDate);
    }
}
