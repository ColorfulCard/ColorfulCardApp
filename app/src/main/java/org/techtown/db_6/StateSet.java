package org.techtown.db_6;

public class StateSet {


    public class MailMsg {
        public static final int MSG_FAIL=0;
        public static final int MSG_SUCCESS = 1;
    }

    public class RegisterCardMsg {
        public static final int MSG_FAIL=0;
        public static final int MSG_SUCCESS_VAILCHECK = 1;
    }

    public class HomeMsg {
        public static final int MSG_FAIL=0;
        public static final int MSG_SUCCESS_GETFAVORSTORE = 1;
        public static final int MSG_SUCCESS_GETSTORE = 2;

    }

    public class LoadingMsg {
        public static final int MSG_FAIL=0;
        public static final int MSG_SUCCESS_BALCHECK = 1;
    }

    public class SearchMsg {
        public static final int MSG_FAIL=0;
        public static final int MSG_SUCCESS_SEARCH =1;
        public static final int MSG_SEARCH_NO_WORD=2;
    }

    public class BoardMsg {

        public static final int MSG_FAIL=0;
        public static final int MSG_SUCCESS_GET_FIRST=1;
        public static final int MSG_SUCCESS_GETPOSTINGS=2;
        public static final int MSG_NO_POSTINGS =3;
        public static final int MSG_ALREADY_GET_ALLPOSTINGS=4;
        public static final int MSG_SUCCESS_GETCMENTS=5;
        public static final int MSG_SUCCESS_DEL_POSTING=6;
        public static final int MSG_SUCCESS_HEARTPRESS=7;
        public static final int MSG_SUCCESS_INSERTPOSTING=8;
    }
    public class ViewType{
        public static final int sideMealCard = 0;
        public static final int mealCard = 1;
        public static final int eduCard =2;
        public static final int searchResult=3;
        public static final int posting=4;
        public static final int comment=5;
        public static final int ccomment=6;
        public static final int loading=7;
    }


}