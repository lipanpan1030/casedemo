package com.meizu.demo.provider.table;

import android.provider.BaseColumns;

/**
 * Created by liaojinlong on 15-3-24.
 *
 * 在线状态表
 */
public class TablePresenceColumn implements BaseColumns {
    public static final String TB_NAME = "tb_presence";
    /**
     * 联系人号码
     * **/
    public static final String CONTACT_NUMBER = "number";

     /**
     * 在线状态
     * */
    public static final String NOTIFY_ONLINE_STATUS = "notify_onlineStatus";
    /**
     * 能力值
     * */
    public static final String NOTIFY_CAPACITY = "notify_capacity";
    /**
     * 在线状态
     * */
    public static final String ONLINE_STATUS = "onlineStatus";
    /**
     * 能力值
     * */
    public static final String CAPACITY = "capacity";
    /**
     * 是否激活过
     * */
    public static final String IS_ACTIVE = "is_active";
    /**
     * 订阅号码,其与联系人电话号码是一对多的关系
     * 联系人的号码都将转换为+86的形式，向服务其发送订阅通知
     * **/
    public static final String SUBSCRIBE_NUMBER = "subscribe_number";

    public static final SQLiteTable CREATE_TABLE = new SQLiteTable(TB_NAME)
            .addColumn(CONTACT_NUMBER,Column.DataType.TEXT)
            .addColumn(ONLINE_STATUS,Column.DataType.TEXT)
            .addColumn(CAPACITY,Column.DataType.INTEGER)
            .addColumn(NOTIFY_ONLINE_STATUS,Column.DataType.TEXT)
            .addColumn(NOTIFY_CAPACITY,Column.DataType.INTEGER)
            .addColumn(IS_ACTIVE,Column.DataType.TEXT)
            .addColumn(SUBSCRIBE_NUMBER,Column.DataType.TEXT);

 }
