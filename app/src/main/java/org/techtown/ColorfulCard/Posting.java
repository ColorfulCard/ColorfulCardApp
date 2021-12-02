package org.techtown.ColorfulCard;

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

    public String getPdate() {

        StringBuilder stringBuilder= new StringBuilder();
        String pdate=this.pdate.toString();

        //2021-11-28 12:05
        //21/11/28 17:32
        stringBuilder.append(pdate.substring(2,4));
        stringBuilder.append("/");
        stringBuilder.append(pdate.substring(5,7));
        stringBuilder.append("/");
        stringBuilder.append(pdate.substring(8,10));
        stringBuilder.append(" ");
        stringBuilder.append(pdate.substring(11,16));

        return stringBuilder.toString();
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

    public void addCcnt(int i) {
        this.ccnt=ccnt+i;
    }

    public void addHcnt(int i) {
        this.hcnt= hcnt+i;

    }
    //이거 굳이 안필요할거같음 뭔가
    public void addVcnt() {
        this.vcnt++;
    }

}
