package com.meizu.demo.provider.table;

import android.provider.BaseColumns;

/**
 * Created by zhangbin on 15-6-19.
 */
public class TableFlymeCommunicationColumn implements BaseColumns {
    /**
     * flyme通讯状态表
     * */
    public static final String TB_NAME = "tb_flyme_communication";

    public static final String INTERNATIONAL_CODE = "code";

    public static final String NUMBER = "number";

    public static final String PHONE_STATUS = "phone_status";

    public static final String MMS_STATUS = "mms_status";

    public static final String PHONE_WIFI_ONLY = "wifi_only";

    public static final SQLiteTable CREATE_TABLE = new SQLiteTable(TB_NAME)
            .addColumn(INTERNATIONAL_CODE, Column.DataType.TEXT)
            .addColumn(NUMBER, Column.DataType.TEXT)
            .addColumn(PHONE_STATUS, Column.DataType.INTEGER)
            .addColumn(MMS_STATUS, Column.DataType.INTEGER)
            .addColumn(PHONE_WIFI_ONLY, Column.DataType.INTEGER);
}
