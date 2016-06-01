package com.meizu.demo.provider;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaojinlong on 15-3-26.
 * 提供cloud provider 相关的数据操作api
 */
public class CloudProviderHelper {
    private String mTag = "CloudProviderHelper";
    private Builder mBuilder;

    public CloudProviderHelper(Builder builder){
        mBuilder = builder;
    }

    /**
     * A content uri for common data CRUD
     * */
    public static final Uri COMMON_CONTENT_URI = Uri.withAppendedPath(CloudProvider.AUTHORITY_URI, "Common");
    /**
     * A content uri for common data CRUD
     * */
    public static final Uri PRESENCE_CONTENT_URI = Uri.withAppendedPath(CloudProvider.AUTHORITY_URI, "presence");
    /**
     * A content uri for common data CRUD
     * */
    public static final Uri SUBSCRIBE_CONTENT_URI = Uri.withAppendedPath(CloudProvider.AUTHORITY_URI, "SubscribeNumbers");
    /**
     * A content uri for common data CRUD
     * */
    public static final Uri RECEIVE_TYPE_CONTENT_URI = Uri.withAppendedPath(CloudProvider.AUTHORITY_URI, "ReceiveType");

    public static final int TYPE_NEW_INSERT = 10001;
    public static final int TYPE_NEW_UPDATE = 10002;
    public static final int TYPE_NEW_DELETE = 10003;
    public static final int TYPE_NEW_QUERY  = 10004;
    public static final int TYPE_NEW_INSERT_UPDATE = 10005;

    public void start(){
        if(mBuilder != null) {
            switch (mBuilder.operateType){
                case TYPE_NEW_INSERT:
                    insert();
                    break;
                case TYPE_NEW_UPDATE:
                    update();
                    break;
                case TYPE_NEW_DELETE:
                    delete();
                    break;
                case TYPE_NEW_QUERY:
                    query();
                    break;
                case TYPE_NEW_INSERT_UPDATE:
                    insertOrUpdate();
                    break;
            }
        }
    }

    /**
     * start insert data
     * */
    private void insert(){
        if(mBuilder.contentValuesList.size() > 1) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            for(ContentValues contentValues : mBuilder.contentValuesList) {
                ops.add(ContentProviderOperation.newInsert(mBuilder.operateUri)
                    .withValues(contentValues)
                    .build());
            }
            apply(ops);
        } else if(mBuilder.contentValuesList.size() == 1) {
            mBuilder.context.getContentResolver()
                    .insert(mBuilder.operateUri, mBuilder.contentValuesList.get(0));
        }
    }

    /**
     * update data
     * */
    private void update(){
        if(mBuilder.contentValuesList.size() == 1) {
            mBuilder.context.getContentResolver().update(mBuilder.operateUri,
                    mBuilder.contentValuesList.get(0),
                    mBuilder.selection,
                    mBuilder.selectionArgs);
        }
    }

    /**
     * Insert or update
     * */
    private void insertOrUpdate(){
        //先更新数据,如果不成功则插入数据
        Cursor updateCursor = null;
        try {
             updateCursor = mBuilder.context.getContentResolver().query(mBuilder.operateUri,
                     null,
                     mBuilder.selection,
                     mBuilder.selectionArgs,
                     null);
            if(updateCursor != null && updateCursor.getCount() > 0){
                //执行update
                update();
            } else {
                //执行insert
                insert();
            }
        } catch (Exception e){
            Log.d(mTag, e+"");
        } finally {
            if(updateCursor != null){
                updateCursor.close();
            }
        }

    }

    private void delete(){
        mBuilder.context.getContentResolver().delete(mBuilder.operateUri,
                mBuilder.selection,
                mBuilder.selectionArgs);
    }

    private void query(){
        Cursor cursor = mBuilder.context.getContentResolver()
                .query(mBuilder.operateUri, mBuilder.projection,
                        mBuilder.selection, mBuilder.selectionArgs, null);
        if(mBuilder.queryCursorListener != null){
            mBuilder.queryCursorListener.onQueryCursor(cursor);
        }
        if(cursor != null) {
            cursor.close();
        }
    }

    private ContentProviderResult[] apply(ArrayList<ContentProviderOperation> ops){
        ContentProviderResult[] contentProviderResults = null;
        try {
            contentProviderResults = mBuilder.context.getContentResolver()
                    .applyBatch(CloudProvider.AUTHORITY,ops);
        } catch (Exception e) {
            Log.d(mTag, e+"");
        }
        return contentProviderResults;
    }

    public static Builder newUpdateCommon(Context context){
        return update(context, COMMON_CONTENT_URI);
    }

    public static Builder newDeleteCommon(Context context){
        return delete(context, COMMON_CONTENT_URI);
    }

    public static Builder newInsertCommon(Context context){
        return insert(context, COMMON_CONTENT_URI);
    }

    public static Builder newInsertOrUpdateCommon(Context context){
        return insertOrUpdate(context, COMMON_CONTENT_URI);
    }

    public static Builder newQueryCommon(Context context){
        return query(context, COMMON_CONTENT_URI);
    }

    public static Builder newUpdateReceive(Context context){
        return update(context, RECEIVE_TYPE_CONTENT_URI);
    }

    public static Builder newDeleteReceive(Context context){
        return delete(context, RECEIVE_TYPE_CONTENT_URI);
    }

    public static Builder newInsertReceive(Context context){
        return insert(context, RECEIVE_TYPE_CONTENT_URI);
    }

    public static Builder newQueryReceive(Context context){
        return query(context, RECEIVE_TYPE_CONTENT_URI);
    }

    public static Builder newUpdatePresence(Context context){
        return update(context, PRESENCE_CONTENT_URI);
    }

    public static Builder newDeletePresence(Context context){
        return delete(context, PRESENCE_CONTENT_URI);
    }

    public static Builder newInsertPresence(Context context){
        return insert(context, PRESENCE_CONTENT_URI);
    }

    public static Builder newQueryPresence(Context context){
        return query(context, PRESENCE_CONTENT_URI);
    }

    public static Builder newUpdateSubscribe(Context context){
        return update(context, SUBSCRIBE_CONTENT_URI);
    }

    public static Builder newDeleteSubscribe(Context context){
        return delete(context, SUBSCRIBE_CONTENT_URI);
    }

    public static Builder newInsertSubscribe(Context context){
        return insert(context, SUBSCRIBE_CONTENT_URI);
    }

    public static Builder newQuerySubscribe(Context context){
        return query(context, SUBSCRIBE_CONTENT_URI);
    }

    public static Builder update(Context context, Uri uri) {
        return new Builder(context,uri,TYPE_NEW_UPDATE);
    }

    public static Builder delete(Context context, Uri uri){
        return new Builder(context,uri,TYPE_NEW_DELETE);
    }

    public static Builder insert(Context context, Uri uri){
        return new Builder(context,uri,TYPE_NEW_INSERT);
    }

    public static Builder insertOrUpdate(Context context, Uri uri){
        return new Builder(context,uri,TYPE_NEW_INSERT_UPDATE);
    }

    public static Builder query(Context context, Uri uri){
        return new Builder(context,uri,TYPE_NEW_QUERY);
    }

    public static class Builder{
        private Context context;
        private Uri operateUri;
        private int operateType;
        private String[] projection;
        private String selection;
        private String[] selectionArgs;
        private List<ContentValues> contentValuesList = new ArrayList<>();
        private QueryCursorListener queryCursorListener;

        public Builder withUri(Uri uri){
            this.operateUri = uri;
            return this;
        }

        public Builder withOperateType(int operateType){
            this.operateType = operateType;
            return this;
        }

        /**
         * 优先处理批量数据，如果没有则处理单条数据
         * **/
        public Builder addValue(ContentValues contentValue){
            contentValuesList.add(contentValue);
            return this;
        }

        public Builder withSelection(String selection, String[] selectionArgs){
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            return this;
        }

        public Builder withProjection(String[] projection){
            this.projection = projection;
            return this;
        }

        /**
         * 处理查询的回调
         * */
        public Builder withQueryCursorListener(QueryCursorListener queryCursorListener){
            this.queryCursorListener = queryCursorListener;
            return this;
        }

        public CloudProviderHelper build(){
            return new CloudProviderHelper(this);
        }

        public Builder(Context context){
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context;
        }

        public Builder(Context context,Uri uri,int operateType){
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context;
            this.operateUri = uri;
            this.operateType = operateType;
        }
    }

    public interface QueryCursorListener{
        void onQueryCursor(Cursor cursor);
    }

}
