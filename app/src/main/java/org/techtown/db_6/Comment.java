package org.techtown.db_6;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable {


    private static final long serialVersionUID = 3L;

    @SerializedName("pno")
    private int pno;

    @SerializedName("cno")
    private int cno;

    @SerializedName("cid")
    private String cid;

    @SerializedName("cment")
    private String cment;

    @SerializedName("cdate")
    private Timestamp cdate;

    @SerializedName("ccnt")
    private int cccnt;

    public Comment(int pno, int cno ,String cid, String cment, Timestamp cdate, int cccnt) {

        this.pno=pno;
        this.cno=cno;
        this.cid= cid;
        this.cment= cment;
        this.cdate= cdate;
        this.cccnt= cccnt;

    }

    public int getPno() {
        return pno;
    }

    public int getCno() {
        return cno;
    }

    public String getCid() {
        return cid;
    }

    public String getCment() {
        return cment;
    }

    public Timestamp getCdate() {
        return cdate;
    }

    public int getCccnt() {
        return cccnt;
    }

}
