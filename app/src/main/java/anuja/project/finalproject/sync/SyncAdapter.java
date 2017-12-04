package anuja.project.finalproject.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import anuja.project.finalproject.FindLocation;
import anuja.project.finalproject.R;
import anuja.project.finalproject.data.NewsContract;
import anuja.project.finalproject.rest.News;
import anuja.project.finalproject.rest.Webhose;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    Context mContext;
    public static final String ACTION_DATA_UPDATED = "anuja.project.finalproject.ACTION_DATA_UPDATED";
    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    private static final String ACCOUNT_TYPE = "com.anuja.finalproject";
    private static final String ACCOUNT_NAME = "Default Account";
    String location="";


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = getContext();
    }

    @SuppressWarnings("unused")
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    public static Account createSyncAccount(Context context) {
        Log.e(TAG, "createSyncAccount....");
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                context.getString(R.string.app_name), ACCOUNT_TYPE);
        if ( null == accountManager.getPassword(newAccount) )
        {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
        }
        Log.e(TAG, "Account created...."+newAccount);
        return newAccount;
    }

    public static void Sync(Context context) {
        Log.e(TAG, " Sync....");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(createSyncAccount(context), NewsContract.AUTHORITY, bundle);
    }

    public static void initializeAdapter(Context context) {
        Log.e(TAG, " initializeAdapter....");
        Account ACCOUNT = createSyncAccount(context);
        ContentResolver.setSyncAutomatically(ACCOUNT, NewsContract.AUTHORITY,true);//Set whether or not the provider is synced when it receives a network tickle.
        ContentResolver.addPeriodicSync(ACCOUNT,NewsContract.AUTHORITY,Bundle.EMPTY,SYNC_INTERVAL);//This schedules your sync adapter to run after a certain amount of time
    }

    /**
     * Called by the framework to do remote synchronization of data.
     *
     * @param account    Our default account
     * @param syncBundle Bundle to pass additional information about the sync request.
     * @param authority  Our authority
     * @param provider   Points to the ContentProvider for this sync.
     * @param syncResult Results of the sync
     */


    @Override
    public void onPerformSync(Account account, final Bundle syncBundle, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {

        Log.d(TAG, "Inside onPerformSync.....");
      //  int deleted = deleteOldDataFromDatabase();
       // Log.e(TAG, "Inside onPerformSync Delete Old Data " + deleted + " deleted");

        int i;
        for( i=0 ; i<2 ; i++) {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String type="";
            String str;
            String jsonStr = null;
            StringBuilder str_lines = new StringBuilder();

            if(i==1){
                type = "global";
                location = Webhose.QUERY_GLOBAL;
                Log.e(TAG, "Global Location = "+location);
            }else{
                location = FindLocation.Location(mContext);
                type = "local";
                Log.e(TAG, "Local Location = "+location);
            }

            try {
                Uri builtUri=Uri.parse(Webhose.WEBHOSE_URL).buildUpon()
                        .appendQueryParameter(Webhose.TOKEN,Webhose.APIKEY)
                        .appendQueryParameter(Webhose.FORMAT,Webhose.VALUE)
                        .appendQueryParameter(Webhose.QUERY, location)
                        .appendQueryParameter(Webhose.SIZE,Webhose.SIZE_VALUE)
                        .appendQueryParameter(Webhose.LANGUAGE,Webhose.LANGUAGE_VALUE)
                        .appendQueryParameter(Webhose.SORT,Webhose.SORT_VALUE)
                        .appendQueryParameter(Webhose.SITE_TYPE,Webhose.SITE_TYPE_VALUE)
                        .build();
                Log.e(TAG,"Built URI " + builtUri.toString());
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream==null){
                    return ;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((str = bufferedReader.readLine()) != null){
                    str_lines.append(str);
                }
                if(str_lines.length() == 0){
                    return ;
                }
                jsonStr = str_lines.toString();
                Log.e(TAG ,type+" ---- " + jsonStr);
                //***************************_DATABASE_*******
                //fetchJsonAndSave(jsonStr,i);//------

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
                if(bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
        // notify components waiting for updates in our app with the db update
        mContext.sendBroadcast(dataUpdatedIntent);

    }

//    private int deleteOldDataFromDatabase(){
//        //Delete old date except fav
//        return mContext.getContentResolver().delete(
//                ProductContract.ProductEntry.CONTENT_URI
//                , ProductContract.ProductEntry.COLUMN_FAV+" = ?"
//                ,new String[]{String.valueOf(0)});
//    }

    private void fetchJsonAndSave(String jsonStr, int i) {

        if (jsonStr != null) {
            try {
                JSONObject jObjectAll = new JSONObject(jsonStr);

                JSONArray jArray = jObjectAll.getJSONArray(Webhose.NEWS_POSTS);
                Vector<ContentValues> contentvalue_vector = new Vector<ContentValues>(jArray.length());

                for (int j = 0; j < jArray.length(); j++) {
                    JSONObject jsonObject = jArray.getJSONObject(j);
                    JSONObject NewsValue = jsonObject.getJSONObject(Webhose.NEWS_THREAD);

                    News news = new News(
                            NewsValue.getString(Webhose.UUID),
                            NewsValue.getString(Webhose.URL),
                            NewsValue.getString(Webhose.SITE),
                            NewsValue.getString(Webhose.PUBLISHED_DATE).substring(0,10),
                            NewsValue.getString(Webhose.TITLE),
                            jsonObject.getString(Webhose.TEXT),
                            NewsValue.getString(Webhose.MAIN_IMAGE));

                    ContentValues Values = new ContentValues();

                   // newsValues.put(NewsContract.NewsEntry.COLUMN_NEWS_ID,newsModel.mId);
                    Values.put(NewsContract.NewsEntry.COLUMN_ID,news.Id);
                    Values.put(NewsContract.NewsEntry.COLUMN_URL,news.Url);
                    Values.put(NewsContract.NewsEntry.COLUMN_SITE,news.Site);
                    Values.put(NewsContract.NewsEntry.COLUMN_DATE,news.Date);
                    Values.put(NewsContract.NewsEntry.COLUMN_TITLE,news.Title);
                    Values.put(NewsContract.NewsEntry.COLUMN_TEXT,news.Text);
                    Values.put(NewsContract.NewsEntry.COLUMN_IMAGE,news.ImagePath);

                    if(i==0){
                        Values.put(NewsContract.NewsEntry.COLUMN_GLOBAL,1);
                    }else if(i == 1){
                        Values.put(NewsContract.NewsEntry.COLUMN_LOCAL,1);
                    }

                    contentvalue_vector.add(Values);
                }

                int inserted =0;
                if(contentvalue_vector.size() > 0){
                    ContentValues[] cvArray = new ContentValues[contentvalue_vector.size()];
                    contentvalue_vector.toArray(cvArray);

                    //insert new data
//                    inserted=mContext.getContentResolver().bulkInsert(
//                            ProductContract.ProductEntry.CONTENT_URI
//                            ,cvArray);

                    Log.e(TAG, "SyncAdapter Completed. " + inserted + " Data Inserted ");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
