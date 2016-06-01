package com.meizu.demo.provider.table;

import android.provider.BaseColumns;

/**
 * Created by liaojinlong on 15-3-20.
 * 通用表
 */
public class TableCommonColumn implements BaseColumns {

    public static final String TB_NAME = "tb_common";
    public static final String TB_COLUMN_TYPE = "type";
    public static final String TB_COLUMN_KEY = "key";
    public static final String TB_COLUMN_VALUE = "value";

    /*push id*/
    public static final String TYPE_PUSH_ID = "PUSH_ID";

    /*短信中心号码*/
    public static final String TYPE_CENTER_NUMER = "CENTER_NUMER";
    /**时间**/
    public static final String TYPE_TIMESTAMP = "TIMESTAMP";

    public static final String KEY_HIDDEN_NUMBER = "_____hidden_number_____";


    public static final SQLiteTable CREATE_TABLE = new SQLiteTable(TB_NAME)
            .addColumn(TB_COLUMN_TYPE, Column.DataType.TEXT)
            .addColumn(TB_COLUMN_KEY, Column.DataType.TEXT)
            .addColumn(TB_COLUMN_VALUE,Column.DataType.TEXT);

}
