package com.meizu.demo.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by liaojinlong on 15-3-6.
 */
public class CloudProvider extends ContentProvider {
    private static final String TAG = "CloudProvider";
    /**The authority for the cloud provider**/
    public static final String AUTHORITY = "com.meizu.servicedemo";
    /** A content:// style uri to the authority for the cloud provider */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    /**a map for saving registered subProvider**/
    public ArrayList<SubProvider> providers;

    private static CloudDBHelper cloudDBHelper;

    protected static final Object DBLock = new Object();

    @Override
    public boolean onCreate() {
        Log.d(TAG, "[onCreate]");
        providers = new ArrayList<>();
        ContactPresenceProvider.registerProvider(this);
        CommonProvider.registerProvider(this);
        FlymeCommProvider.registerProvider(this);
        initDBHelper(getContext());
        return true;
    }

    private static CloudDBHelper initDBHelper(Context context){
        if(cloudDBHelper == null){
            cloudDBHelper = new CloudDBHelper(context);
        }
        return cloudDBHelper;
    }

    private SubProvider getProvider(Uri uri){
        SubProvider provider = null;
        for(SubProvider subProvider : providers){
            if(subProvider.match(uri)){
                provider = subProvider;
                break;
            }
        }
        return provider;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SubProvider subProvider = getProvider(uri);
        if(subProvider != null){
            return subProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        SubProvider subProvider = getProvider(uri);
        if(subProvider != null){
            return subProvider.getType(uri);
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SubProvider subProvider = getProvider(uri);
        if(subProvider != null){
           return subProvider.insert(uri, values);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SubProvider subProvider = getProvider(uri);
        if(subProvider != null){
            return subProvider.delete(uri, selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SubProvider subProvider = getProvider(uri);
        if(subProvider != null){
           return subProvider.update(uri, values, selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        synchronized (DBLock) {
            SQLiteDatabase db = cloudDBHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentProviderResult[] results = super.applyBatch(operations);
                db.setTransactionSuccessful();
                return results;
            } finally {
                db.endTransaction();
            }
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        synchronized (DBLock) {
            SQLiteDatabase db = cloudDBHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                int results = super.bulkInsert(uri, values);
                db.setTransactionSuccessful();
                return results;
            } finally {
                db.endTransaction();
            }
        }
    }

    public abstract static class SubProvider {
        protected CloudProvider cloudProvider;

        public SubProvider(CloudProvider cloudProvider) {
            this.cloudProvider = cloudProvider;
        }

        public Context getContext(){
            return cloudProvider.getContext();
        }

        public CloudDBHelper getDBHelper(){
            return cloudDBHelper;
        }

        /**
         * @param uri
         *          根据传入的uri确定其归属的provider
         * **/
        public abstract boolean match(Uri uri);

        /**
         * @param uri
         *          根据uri确定其要查询的表名
         * **/
        public abstract String matchTable(Uri uri);

        /**
         * @param uri
         *          the uri for query table
         * @param projection
         *          The columns to return from the queryFromDB
         * @param selection
         *          The columns for the where clause
         * @param selectionArgs
         *          The values for the where clause
         * @param sortOrder
         *          The sortOrder for the order values
         * */
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {return null;}

        public String getType(Uri uri) {return null;}

        /**
         * 插入数据
         * @param uri
         *               The Uri for math delete table
         * @param values
         *               the values for insert
         * */
        public Uri insert(Uri uri, ContentValues values) {
            Uri returnUri = null;
            synchronized (DBLock) {
                 String table = matchTable(uri);
                 SQLiteDatabase db = getDBHelper().getWritableDatabase();
                 if(!TextUtils.isEmpty(table) &&
                         db != null) {
                     long rowId = 0;
                     try {
                         rowId = db.insert(table, null, values);
                     } catch (Exception e) {
                         Log.e(TAG, e.getMessage());
                     }
                     if (rowId > 0) {
                         returnUri = ContentUris.withAppendedId(uri, rowId);
                         getContext().getContentResolver().notifyChange(uri, null);
                     }
                 }
            }
            return returnUri;
        }

        /**
         * 删除数据
         * @param uri
         *               The Uri for math delete table
         * @param selection
         *               The columns for the where clause
         * @param selectionArgs
         *               The values for the where clause
         * */
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            int count = 0;
            synchronized (DBLock) {
                SQLiteDatabase db = getDBHelper().getWritableDatabase();
                String table = matchTable(uri);
                if(!TextUtils.isEmpty(table) &&
                        db != null) {
                    try {
                        count = db.delete(table, selection, selectionArgs);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            }
            return count;
        }

        /**
         * 删除数据
         * @param uri
         *               The Uri for math delete table
         * @param values
         *               the values for update
         * @param selection
         *               The columns for the where clause
         * @param selectionArgs
         *               The values for the where clause
         * */
        public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            int count = 0;
            synchronized (DBLock) {
                SQLiteDatabase db = getDBHelper().getWritableDatabase();
                String table = matchTable(uri);
                if(!TextUtils.isEmpty(table) &&
                        db != null) {
                    try {
                        count = db.update(table, values, selection, selectionArgs);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            }
            return count;
        }
    }
}
