package com.meizu.demo.provider.table;

import android.provider.BaseColumns;

/**
 * Created by liaojinlong on 15-3-26.
 */
public class TableSubscribeColumn implements BaseColumns {
    /**
     * 订阅状态表
     * */
    public static final String TB_NAME = "tb_subscribe";
    /**
     * 订阅号码,其与联系人电话号码是一对多的关系
     * 联系人的号码都将转换为+86的形式，向服务其发送订阅通知
     * **/
    public static final String SUBSCRIBE_NUMBER = "subscribe_number";

    /**
     * 订阅状态
     * 该状态作为该该号码的订阅过程中的状态转换
     * **/
    public static final String SUBSCRIBE_STATE = "subscribe_state";

    /**
     * 等待订阅
     * */
    public static final int WAITING_SUBSCRIBE = 1;

    /**
     * 正在订阅
     * */
    public static final int SUBSCRIBING = 2;

    /**
     * 等待取消订阅
     * */
    public static final int WAITING_UNSUBSCRIBE = 3;

    /**
     * 正在取消订阅
     * */
    public static final int UNSUBSCRIBING = 4;

    /**
     * 订阅失败
     * */
    public static final int SUBSCRIBE_FAILED = 5;

    public static final SQLiteTable CREATE_TABLE = new SQLiteTable(TB_NAME)
            .addColumn(SUBSCRIBE_NUMBER,Column.DataType.TEXT)
            .addColumn(SUBSCRIBE_STATE,Column.DataType.TEXT);
  }
