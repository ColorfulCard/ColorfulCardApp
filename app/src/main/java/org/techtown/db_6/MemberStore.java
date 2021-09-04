package org.techtown.db_6;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class MemberStore implements Parcelable {

    private static final long serialVersionUID = 2L;

    public MemberStore(String store_num, String store_name, String store_type, BigDecimal latitude, BigDecimal longitude) {

        this.store_num = store_num;
        this.store_name = store_name;
        this.store_type = store_type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @SerializedName("store_num")
    private String store_num;

    @SerializedName("store_name")
    private String store_name;

    @SerializedName("store_type")
    private String store_type;

    @SerializedName("latitude")
    private BigDecimal latitude; //위도

    @SerializedName("longitude")
    private BigDecimal longitude; //경도

    protected MemberStore(Parcel in) {
        store_num = in.readString();
        store_name = in.readString();
        store_type = in.readString();
        latitude= BigDecimal.valueOf(in.readDouble());
        longitude= BigDecimal.valueOf(in.readDouble());
    }

    public static final Creator<MemberStore> CREATOR = new Creator<MemberStore>() {
        @Override
        public MemberStore createFromParcel(Parcel in) {
            return new MemberStore(in);
        }

        @Override
        public MemberStore[] newArray(int size) {
            return new MemberStore[size];
        }
    };

    public String getStore_num(){
        return store_num;
    }

    public String getStore_name(){
        return store_name;
    }

    public String getStore_type(){
        return store_type;
    }

    public Double getLatitude(){
        return latitude.doubleValue();
    }

    public Double getLongitude(){
        return longitude.doubleValue();
    }


    @Override
    public String toString() {
        return "PostResult{" +
                "store_num=" + store_num +
                ", store_name=" + store_name +
                ", store_type=" +store_type +
                ", latitude=" + latitude +
                ", longitude" + longitude+'\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(store_num);
        dest.writeString(store_name);
        dest.writeString(store_type);
        dest.writeDouble(latitude.doubleValue());
        dest.writeDouble(longitude.doubleValue());
    }
}
