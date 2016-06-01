package com.meizu.demo.provider;

import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.meizu.demo.provider.table.TableCommonColumn;


/**
 * Created by liaojinlong on 15-3-20.
 */
public class CommonProvider extends CloudProvider.SubProvider {
    private static UriMatcher sUriMatcher;
    private static final int COMMON_DATAS = 1;
    private static final int COMMON_DATA = 2;
    private static final int CENTER_NUMBER = 3;
    private static final int CENTER_NUMBERS = 4;
    private static final int HIDDEN_NUMBER = 5;
    private static final int HIDDEN_NUMBERS = 6;

    private final String TYPE_CENTER_NUMBER = "vnd.android.cursor.dir/vnd.meizu.center_number";
    private final String TYPE_CENTER_NUMBER_ITEM = "vnd.android.cursor.item/vnd.meizu.center_number";
    private final String TYPE_HIDDEN_NUMBER = "vnd.android.cursor.dir/vnd.meizu.hidden_number";
    private final String TYPE_HIDDEN_NUMBER_ITEM = "vnd.android.cursor.item/vnd.meizu.hidden_number";

    private static CommonProvider mInstance;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "Common", COMMON_DATAS);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "Common/*", COMMON_DATA);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "center_number/*", CENTER_NUMBER);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "center_number", CENTER_NUMBERS);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "hidden_number/*", HIDDEN_NUMBER);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "hidden_number", HIDDEN_NUMBERS);
    }


    public CommonProvider(CloudProvider cloudProvider) {
        super(cloudProvider);
    }

    /***
     * 注册该类型的provider
     * **/
    public static void registerProvider(CloudProvider cloudProvider){
        mInstance = new CommonProvider(cloudProvider);
        cloudProvider.providers.add(mInstance);
    }

    @Override
    public boolean match(Uri uri) {
        return sUriMatcher.match(uri) != -1;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (CloudProvider.DBLock) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String table = matchTable(uri);
            queryBuilder.setTables(table);
            switch (sUriMatcher.match(uri)){
                case COMMON_DATA:
                    queryBuilder.appendWhere(TableCommonColumn._ID + "=" + uri.getPathSegments().get(1));
                    break;
                case CENTER_NUMBER:
                    queryBuilder.appendWhere(TableCommonColumn.TB_COLUMN_KEY + "='" + uri.getPathSegments().get(1) + "' And " +
                            TableCommonColumn.TB_COLUMN_TYPE + "='" + TableCommonColumn.TYPE_CENTER_NUMER + "'");
                    break;
                case HIDDEN_NUMBERS:
                    queryBuilder.appendWhere(TableCommonColumn.TB_COLUMN_KEY + "='" + TableCommonColumn.KEY_HIDDEN_NUMBER + "' And " +
                            TableCommonColumn.TB_COLUMN_TYPE + "='" + TableCommonColumn.TYPE_CENTER_NUMER + "'");
                    break;
            }

            SQLiteDatabase db = getDBHelper().getReadableDatabase();
            Cursor cursor = queryBuilder.query(db, // The database to
                    // queryFromDB
                    projection, //
                    selection, //
                    selectionArgs, //
                    null, // don't group the rows
                    null, // don't filter by row groups
                    sortOrder // The sort order
            );

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CENTER_NUMBER:
                return TYPE_CENTER_NUMBER_ITEM;
            case CENTER_NUMBERS:
                return TYPE_CENTER_NUMBER;
            case HIDDEN_NUMBER:
                return TYPE_HIDDEN_NUMBER_ITEM;
            case HIDDEN_NUMBERS:
                return TYPE_HIDDEN_NUMBER;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public String matchTable(Uri uri) {
        String table = null;
        switch (sUriMatcher.match(uri)) {
            case COMMON_DATAS:
            case COMMON_DATA:
            case CENTER_NUMBER:
            case CENTER_NUMBERS:
            case HIDDEN_NUMBER:
            case HIDDEN_NUMBERS:
                table = TableCommonColumn.TB_NAME;
                break;
        }
        return table;
    }
}
