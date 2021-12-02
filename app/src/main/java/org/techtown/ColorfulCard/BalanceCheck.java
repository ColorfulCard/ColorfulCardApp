package org.techtown.ColorfulCard;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//Balance-잔액 Check-조회 뜻으로 사용함
public class BalanceCheck {

    private String  web01 = "http://www.colorfulcard.or.kr";
    private String  web02= "http://www.colorfulcard.or.kr/card/cardRestAmt";
    private String userAgent = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)";
    private String cardNo1,cardNo2,cardNo3,cardNo4;
    private String[] results = new String[7];;

    public BalanceCheck(String cardNo1,String cardNo2,String cardNo3,String cardNo4)
    {
        this.cardNo1=cardNo1;
        this.cardNo2=cardNo2;
        this.cardNo3=cardNo3;
        this.cardNo4=cardNo4;
    }

    public String tryBalanceCheck(int version) throws IOException
    {
        Connection.Response checkPageResponse = Jsoup.connect(web01)
                .timeout(50000)
                .method(Connection.Method.GET)
                .execute();

        Map<String, String> checkTryCookie = checkPageResponse.cookies();
        Log.d("tag",checkTryCookie.toString());

        Connection.Response res = Jsoup.connect(web02)
                .userAgent(userAgent)
                .timeout(50000)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Host", "www.colorfulcard.or.kr")
                .header("Upgrade-Insecure-Requests", "1")
                .cookies(checkTryCookie)
                .data("cardNo1",cardNo1)
                .data("cardNo2",cardNo2)
                .data("cardNo3",cardNo3)
                .data("cardNo4",cardNo4)
                .method(Connection.Method.POST)
                .execute();

        Document cardPageDocument = res.parse();
        Elements span = cardPageDocument.select("span.taho.bold.fs_16");
        System.out.println(span.toString()); //파싱 & select 잘되었는지 확인용

        if(version==2)
        {
           if(!span.toString().equals(""))
            return "success";
           else
               return "fail";
        }


        if(version==1 && !span.toString().equals(""))
        {
            String regExp="[>](.*?)[<]";
            Pattern pat = Pattern.compile(regExp);
            Matcher match = pat.matcher(span.toString());
            int i=0;
            while(match.find()) {

                results[i]=match.group(); //  >14,420<
                results[i] =results[i].substring(1,results[i].length()-1); //금액만
                System.out.println(results[i]); //값 똑바로 얻었는지 확인용
                i++;
            }
            return "success";
        }
        else
        {
            return "false";
        }

    }

    /*   results[]
       *[0] 이월 잔여금액	14,920 원	-
        [1] 당월 충전금액	0 원
        [2] 당월 사용금액	7,700 원
        [3] 당월 잔여금액	7,220 원    -> 찐 잔액
        [4] 금일 한도금액	0 원
        [5] 금일 사용금액	0 원
        [6] 금일 잔여금액	0 원
       * */

    public String[] getAllBalanceAttributes ()
    {
        return this.results;
    }
/*
    public String getCurrentBalance()
    {
      int carryOverAmount = Integer.parseInt(results[0].replace(",",""));  //이월 잔여금액
       int currentMonthAmount =  Integer.parseInt(results[3].replace(",","")); //당월 잔여금액
       int totalAmount=carryOverAmount + currentMonthAmount;

       DecimalFormat formatter = new DecimalFormat("###,###");
       return formatter.format(totalAmount);

    }*/
}
