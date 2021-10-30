package org.techtown.db_6;

public class Code {

    public class RegisterCard {
        public static final int MSG_FAIL=0;
        public static final int MSG_SUCCESS_VAILCHECK = 1;


    }

    public class HomeActivity {
      public static final int MSG_SUCCESS_BALCHECK = 1;
      public static final int MSG_SUCCESS_GETSTORE = 2;
      public static final int MSG_FAIL=0;
    }

    public class LoadingActivity{
       public static final int MSG_SUCCESS_BALCHECK = 1;
       public static final int MSG_FAIL=0;


    }

    public class SearchStoreActivity{

        public static final int MSG_SUCCESS_SEARCH =1;
        public static final int MSG_SEARCH_NO_WORD=2;
        public static final int MSG_FAIL=0;

    }

    public class ViewType{
        public static final int sideMealCard = 0;
        public static final int mealCard = 1;
        public static final int eduCard =2;
        public static final int searchResult=3;
    }


}