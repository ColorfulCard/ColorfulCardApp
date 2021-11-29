package org.techtown.db_6;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

public class Ccomment implements Serializable {


    private static final long serialVersionUID = 4L;

    @SerializedName("pno")
    private int pno;

    @SerializedName("cno")
    private int cno;

    @SerializedName("ccno")
    private int ccno;

    @SerializedName("ccid")
    private String ccid;

    @SerializedName("ccment")
    private String ccment;

    @SerializedName("ccdate")
    private Timestamp ccdate;


    public Ccomment(int pno, int cno, int ccno, String ccid, String ccment, Timestamp ccdate) {
        super();
        this.pno = pno;
        this.cno = cno;
        this.ccno = ccno;
        this.ccid = ccid;
        this.ccment = ccment;
        this.ccdate = ccdate;
    }


    public int getPno() {
        return pno;
    }

    public int getCno() {
        return cno;
    }

    public int getCcno() {
        return ccno;
    }

    public String getCcid() {
        return ccid;
    }

    public String getCcment() {
        return ccment;
    }

    public String getCcdate() {
        StringBuilder stringBuilder= new StringBuilder();
        String ccdate=this.ccdate.toString();

        //2021-11-28 12:05
        //21/11/28 17:32
        stringBuilder.append(ccdate.substring(2,4));
        stringBuilder.append("/");
        stringBuilder.append(ccdate.substring(5,7));
        stringBuilder.append("/");
        stringBuilder.append(ccdate.substring(8,10));
        stringBuilder.append(" ");
        stringBuilder.append(ccdate.substring(11,16));

        return stringBuilder.toString();

    }

}
