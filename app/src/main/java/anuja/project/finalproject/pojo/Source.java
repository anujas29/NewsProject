package anuja.project.finalproject.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 18-10-2017.
 */

public class Source implements Parcelable
{
    @SerializedName("site")
    private String site;

    protected Source(Parcel in) {

        this.site = in.readString();

    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel str) {
            return new Source(str);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.site);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
