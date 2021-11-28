package org.techtown.db_6;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

public class Posting  implements Serializable {

    private static final long serialVersionUID = 5L;

    @SerializedName("pno")
    private int pno;

    @SerializedName("pid")
    private String pid;

    @SerializedName("pdate")
    private Timestamp pdate;

    @SerializedName("pcontent")
    private String pcontent;

    @SerializedName("hcnt")
    private int hcnt;

    @SerializedName("ccnt")
    private int ccnt;

    @SerializedName("vcnt")
    private int vcnt;


    public Posting(int pno, String pid, Timestamp pdate, String pcontent, int hcnt, int ccnt, int vcnt) {

        this.pno= pno;
        this.pid= pid;
        this.pdate=pdate;
        this.pcontent=pcontent;
        this.hcnt= hcnt;
        this.ccnt= ccnt;
        this.vcnt= vcnt;

    }

    public int getPno() {
        return pno;
    }

    public String getPid() {
        return pid;
    }

    public Timestamp getPdate() {
        return pdate;
    }

    public String getPcontent() {
        return pcontent;
    }

    public int getHcnt() {
        return hcnt;
    }

    public int getCcnt() {
        return ccnt;
    }

    public int getVcnt() {
        return vcnt;
    }

    public void addCcnt(String sign) {

        if(sign.equals("+")){
            this.ccnt++;
        }
        else
        {
            this.ccnt--;
        }
    }

    public void addHcnt(String sign) {

        if(sign.equals("+")){
            this.hcnt++;
        }
        else
        {
            this.hcnt--;
        }

    }
    //이거 굳이 안필요할거같음 뭔가
    public void addVcnt() {
        this.vcnt++;
    }

}
