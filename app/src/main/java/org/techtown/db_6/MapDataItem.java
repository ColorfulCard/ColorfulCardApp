package org.techtown.db_6;

import android.widget.Button;

public class MapDataItem {

    private String store_name;
    private String store_address;
    private String store_type;
    private int viewType;


    public MapDataItem(String store_name, String store_address ,String store_type, int viewType ) {
        this.store_name=store_name;
        this.store_address=store_address;
        this.store_type=store_type;
        this.viewType=viewType;
    }

    public String getStore_name(){return store_name;}
    public String getStore_address(){return store_address;}
    public String getStore_type(){return store_type;}

    public int getViewType() {
        return viewType;
    }
}
