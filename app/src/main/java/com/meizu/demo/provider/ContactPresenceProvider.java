package com.meizu.demo.provider;

import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.meizu.demo.provider.table.TablePresenceColumn;
import com.meizu.demo.provider.table.TableSubscribeColumn;


/**
 * Created by liaojinlong on 15-3-9.
 */
public class ContactPresenceProvider extends CloudProvider.SubProvider {
    private static UriMatcher sUriMatcher;
    private static final int NUMBERS = 1;
    private static final int NUMBER_ID = 2;
    private static final int SUBSCRIBE_NUMBERS = 3;
    private static final int SUBSCRIBE_NUMBER = 4;
    public static final Uri CONTENT_URI = Uri.parse("content://" + CloudProvider.AUTHORITY + "/presence");

    private static ContactPresenceProvider mInstance;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "presence", NUMBERS);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "presence/*", NUMBER_ID);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "SubscribeNumbers", SUBSCRIBE_NUMBERS);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, "SubscribeNumbers/*", SUBSCRIBE_NUMBER);
    }

    public ContactPresenceProvider(CloudProvider cloudProvider) {
        super(cloudProvider);
    }

    /**
     * 注册该类型的provider
     * *
     */
    public static void registerProvider(CloudProvider cloudProvider) {
        mInstance = new ContactPresenceProvider(cloudProvider);
        cloudProvider.providers.add(mInstance);
    }

    @Override
    public boolean match(Uri uri) {
        return sUriMatcher.match(uri) != -1;
    }

    @Override
    public String matchTable(Uri uri) {
        String table = null;
        switch (sUriMatcher.match(uri)) {
            case NUMBERS:
            case NUMBER_ID:
                table = TablePresenceColumn.TB_NAME;
                break;
            case SUBSCRIBE_NUMBER:
            case SUBSCRIBE_NUMBERS:
                table = TableSubscribeColumn.TB_NAME;
                break;
        }
        return table;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (CloudProvider.DBLock) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String table = matchTable(uri);
            queryBuilder.setTables(table);
            switch (sUriMatcher.match(uri)) {
                case NUMBER_ID:
                    queryBuilder.appendWhere(TablePresenceColumn.CONTACT_NUMBER + "=" + uri.getPathSegments().get(1));
                    break;
                case SUBSCRIBE_NUMBER:
                    queryBuilder.appendWhere(TableSubscribeColumn.SUBSCRIBE_NUMBER + "=" + uri.getPathSegments().get(1));
                    break;
            }
            SQLiteDatabase db = getDBHelper().getReadableDatabase();
            Cursor cursor = queryBuilder.query(db,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }
}
