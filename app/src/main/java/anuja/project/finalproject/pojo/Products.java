package anuja.project.finalproject.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 18-10-2017.
 */

public class Products implements Parcelable {

    @SerializedName("product_id")
    private String product_id;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("name")
    private String Name;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private String price;
    @SerializedName("currency")
    private String currency;
    @SerializedName("offer_price")
    private String offer_price;
    @SerializedName("last_changed")
    private String last_changed;

//    @SerializedName("source")
//    private Source source;

    protected Products(Parcel in) {
        this.product_id = in.readString();
        this.uuid = in.readString();
        this.Name = in.readString();
        this.description = in.readString();
        this.price = in.readString();
        this.currency = in.readString();
        this.offer_price = in.readString();
        this.last_changed = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.uuid);
        dest.writeString(this.Name);
        dest.writeString(this.description);
        dest.writeString(this.price);
        dest.writeString(this.currency);
        dest.writeString(this.offer_price);
        dest.writeString(this.last_changed);

    }

    public static final Parcelable.Creator<Products> CREATOR = new Parcelable.Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel str) {
            return new Products(str);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getLast_changed() {
        return last_changed;
    }

    public void setLast_changed(String last_changed) {
        this.last_changed = last_changed;
    }


}
