package org.techtown.db_6;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class MemberStore implements Parcelable {

    public MemberStore(int sid, String snum, String store_name, String stype, String saddress, BigDecimal latitude, BigDecimal longitude) {

        this.sid= sid;
        this.snum = snum;
        this.sname = store_name;
        this.stype = stype;
        this.latitude = latitude;
        this.longitude = longitude;
        this.saddress = saddress;
    }

    @SerializedName("sid")
    private int sid;

    @SerializedName("snum")
    private String snum;

    @SerializedName("sname")
    private String sname;

    @SerializedName("stype")
    private String stype;

    @SerializedName("latitude")
    private BigDecimal latitude; //위도

    @SerializedName("longitude")
    private BigDecimal longitude; //경도

    @SerializedName("saddress")
    private String saddress;


    protected MemberStore(Parcel in) {
        sid= in.readInt();
        snum = in.readString();
        sname = in.readString();
        stype = in.readString();
        latitude= BigDecimal.valueOf(in.readDouble());
        longitude= BigDecimal.valueOf(in.readDouble());
        saddress =in.readString();
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

    public String getSnum(){
        return snum;
    }

    public String getSname(){
        return sname;
    }

    public String getStype(){
        return stype;
    }

    public Double getLatitude(){
        return latitude.doubleValue();
    }

    public Double getSlongitude(){
        return longitude.doubleValue();
    }

    public String getSaddress(){return saddress;}

    public int getSid() {
        return sid;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "store_id=" + sid+
                "store_num=" + snum +
                ", store_name=" + sname +
                ", store_type=" + stype +
                ", latitude=" + latitude +
                ", longitude" + longitude+'\'' +
                ", store_address="+ saddress +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sid);
        dest.writeString(snum);
        dest.writeString(sname);
        dest.writeString(stype);
        dest.writeDouble(latitude.doubleValue());
        dest.writeDouble(longitude.doubleValue());
        dest.writeString(saddress);
    }
}