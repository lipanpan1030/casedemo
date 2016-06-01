package com.meizu.demo.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.meizu.demo.provider.table.TableFlymeCommunicationColumn;


/**
 * Created by jinhui on 15-4-27.
 */
public class FlymeCommProvider extends CloudProvider.SubProvider {
    private static final String TAG = "FlymeCommProvider";
    private static UriMatcher sUriMatcher;

    private static FlymeCommProvider mInstance;

    private static final String FLYME_COMMUNICATION = "flyme_communication";
    private static final int CODE_FLYME_COMMUNICATION_STATUS = 0x01;
    private final String TYPE_FLYME_COMMUNICATION_STATUS_ITEM = "vnd.android.cursor.item/vnd.meizu.flyme_communication";

    public static final Uri CONTENT_URI = Uri.parse("content://" + CloudProvider.AUTHORITY + "/" + FLYME_COMMUNICATION);

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CloudProvider.AUTHORITY, FLYME_COMMUNICATION, CODE_FLYME_COMMUNICATION_STATUS);
    }

    public FlymeCommProvider(CloudProvider cloudProvider) {
        super(cloudProvider);
    }

    public static void registerProvider(CloudProvider cloudProvider){
        mInstance = new FlymeCommProvider(cloudProvider);
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
            case CODE_FLYME_COMMUNICATION_STATUS:
                table = TableFlymeCommunicationColumn.TB_NAME;
                break;
        }
        return table;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        synchronized (CloudProvider.DBLock) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String table = matchTable(uri);
            queryBuilder.setTables(table);
            SQLiteDatabase db = getDBHelper().getReadableDatabase();
            c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(values.containsKey(TableFlymeCommunicationColumn.NUMBER)) {
            Cursor c = null;
            try {
                c = query(uri, new String[] {TableFlymeCommunicationColumn._ID, TableFlymeCommunicationColumn.NUMBER}, null, null, null);
                if(c != null && c.getCount() > 0 && c.moveToFirst()) {
                    if(update(uri, values, TableFlymeCommunicationColumn.NUMBER + " = ?",
                            new String[] {c.getString(c.getColumnIndex(TableFlymeCommunicationColumn.NUMBER))}) > 0) {
                        return ContentUris.withAppendedId(uri, c.getLong(c.getColumnIndex(TableFlymeCommunicationColumn._ID)));
                    } else {
                        Log.w(TAG, "[insert] update failed !!!");
                    }
                } else {
                    return super.insert(uri, values);
                }
            } catch (Exception e) {
                Log.e(TAG, "[insert] ex = " + e.getMessage());
            } finally {
                if(c != null) {
                    c.close();
                }
            }
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        String type = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_FLYME_COMMUNICATION_STATUS:
                type = TYPE_FLYME_COMMUNICATION_STATUS_ITEM;
                break;
        }
        return type;
    }
}
