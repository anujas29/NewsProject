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

import anuja.project.finalproject.R;
import anuja.project.finalproject.data.ProductContract;
import anuja.project.finalproject.rest.ProductModel;
import anuja.project.finalproject.rest.Webhose;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    Context mContext;
    public static final String ACTION_DATA_UPDATED = "anuja.project.finalproject.ACTION_DATA_UPDATED";
    public static final int SYNC_INTERVAL = 120;//24 hours
    private static final String ACCOUNT_TYPE = "com.anuja.finalproject";
    private static final String ACCOUNT_NAME = "Default Account";


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        System.out.println("---------------------------- NewsSyncAdapter ---------------------------------");
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
        System.out.println("-------------------- createDummyAccount ----------------------------");
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                context.getString(R.string.app_name), ACCOUNT_TYPE);
        if ( null == accountManager.getPassword(newAccount) )
        {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
        }
        return newAccount;
    }

    public static void Sync(Context context) {
        System.out.println("---------------------------- syncImmediately ---------------------------------");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(createSyncAccount(context),ProductContract.AUTHORITY, bundle);
    }

    public static void initializeAdapter(Context context) {
        System.out.println("-------------------- initializeSyncAdapter ----------------------------");
        Log.e("NewsSyncAdapter  ", "initialize SyncAdapter");
        Account ACCOUNT = createSyncAccount(context);
        ContentResolver.setSyncAutomatically(ACCOUNT, ProductContract.AUTHORITY,true);//Set whether or not the provider is synced when it receives a network tickle.
        ContentResolver.addPeriodicSync(ACCOUNT,ProductContract.AUTHORITY,Bundle.EMPTY,SYNC_INTERVAL);//This schedules your sync adapter to run after a certain amount of time
    }






//    public void fetchJson()
//    {
//
//        WebhoseInterface apiService = WebhoseClient.getClient().create(WebhoseInterface.class);
//        Call<ProductResponse> call = apiService.getProducts("api_key","dresses",true);
//
//        call.enqueue(new Callback<ProductResponse>() {
//            @Override
//            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
//                ArrayList<Products> products = response.body().getResults();
//                Log.d(TAG, "Number of products received: " + products.size());
//            }
//
//            @Override
//            public void onFailure(Call<ProductResponse> call, Throwable t) {
//              //System.out.println("------------------------- inside onFailure ------------------:");
//                Log.d(TAG, "inside onFailure ");
//            }
//        });
//    }

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
        System.out.println("-------------------- onPerformSync ----------------------------");
        Log.d(TAG, "<<<<onPerformSync");
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String line;
        String jsonStr = null;
        StringBuilder lines = new StringBuilder();
        String type = "";
        //fetchJson();

        try {
            Uri builtUri = Uri.parse(Webhose.BASE_URL).buildUpon()
                    .appendQueryParameter(Webhose.TOKEN, Webhose.API_KEY)
                    .appendQueryParameter(Webhose.FORMAT, Webhose.FORMAT_VALUE)
                    .appendQueryParameter(Webhose.QUERY, Webhose.QUERY_VALUE)
                    .appendQueryParameter(Webhose.ON_SALE, Webhose.ON_SALE_VALUE)
                    .appendQueryParameter(Webhose.SIZE, Webhose.SIZE_VALUE)
                    .build();

            Log.e(TAG, "Built URI " + builtUri.toString());
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                lines.append(line);
            }
            if (lines.length() == 0) {
                return;
            }
            jsonStr = lines.toString();
            Log.e(TAG, type + " ---- " + jsonStr);
            System.out.println("-------------------------------------------jsonStr::::" + jsonStr);
            //***************************_DATABASE_*******
            fetchJsonAndSave(jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
        mContext.sendBroadcast(dataUpdatedIntent);// notify components waiting for updates in our app with the db update

    }

    private void fetchJsonAndSave(String jsonStr) {

        if (jsonStr != null) {
            try {
                String type = "";
                JSONObject jObjectAll = new JSONObject(jsonStr);
                // do we have an error?(404)
                JSONArray jArray = jObjectAll.getJSONArray(Webhose.PRODUCTS);
                Vector<ContentValues> contentvalue_vector = new Vector<ContentValues>(jArray.length());

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    JSONObject threadObject = jObject.getJSONObject(Webhose.SOURCE);
                    JSONArray jsonArray = jObject.getJSONArray(Webhose.IMAGE);
                    ProductModel productModel = new ProductModel(
                            jObject.getString(Webhose.UUID),
                            jObject.getString(Webhose.URL),
                            threadObject.getString(Webhose.SITE),
                            threadObject.getString(Webhose.TITLE),
                            jObject.getString(Webhose.NAME),
                            jObject.getString(Webhose.DESCRIPTION),
                            jObject.getString(Webhose.LASTCHANGE),
                            jObject.getString(Webhose.PRICE),
                            jObject.getString(Webhose.CURRENCY),
                            jObject.getString(Webhose.OFFER));

                    ContentValues Values = new ContentValues();

                    Values.put(ProductContract.ProductEntry.COLUMN_UUID,productModel.mUUID);
                    Values.put(ProductContract.ProductEntry.COLUMN_URL,productModel.mUrl);
                    Values.put(ProductContract.ProductEntry.COLUMN_SITE,productModel.mSite);
                    Values.put(ProductContract.ProductEntry.COLUMN_TITLE,productModel.mTitle);
                    Values.put(ProductContract.ProductEntry.COLUMN_NAME,productModel.mName);
                    Values.put(ProductContract.ProductEntry.COLUMN_DESCRIPTION,productModel.mDescription);
                    Values.put(ProductContract.ProductEntry.COLUMN_LASTCHANGE,productModel.mLastChange);
                    Values.put(ProductContract.ProductEntry.COLUMN_PRICE,productModel.mPrice);
                    Values.put(ProductContract.ProductEntry.COLUMN_CURRENCY,productModel.mCurrency);
                    Values.put(ProductContract.ProductEntry.COLUMN_OFFER,productModel.mOffer);
//                    Values.put(ProductContract.ProductEntry.COLUMN_IMAGE,productModel.mImage);
                    Values.put(ProductContract.ProductEntry.COLUMN_IMAGE,jsonArray.getString(0));

                    contentvalue_vector.add(Values);

                    System.out.println("------------------------------- i ----------------------" + i);
                    System.out.println("------------------------------- UUID ----------------------" + jObject.getString(Webhose.UUID));
                    System.out.println("------------------------------- URL ----------------------" + jObject.getString(Webhose.URL));
                    System.out.println("------------------------------- SITE ----------------------" + threadObject.getString(Webhose.SITE));
                    System.out.println("------------------------------- TITLE ----------------------" + threadObject.getString(Webhose.TITLE));
                    System.out.println("------------------------------- NAME ----------------------" + jObject.getString(Webhose.NAME));
                    System.out.println("------------------------------- DESCRIPTION ----------------------" + jObject.getString(Webhose.DESCRIPTION));
                    System.out.println("------------------------------- IMAGE ----------------------" + jsonArray.getString(0));
                    System.out.println("-----------------------------------------------------------------" );
                }

                int insert =0;
                if(contentvalue_vector.size() > 0){
                    ContentValues[] cvArray = new ContentValues[contentvalue_vector.size()];
                    contentvalue_vector.toArray(cvArray);

                    //insert new data
                    insert=mContext.getContentResolver().bulkInsert(
                            ProductContract.ProductEntry.CONTENT_URI
                            ,cvArray);

                    Log.e(TAG, "FetchWeatherTask Completed. " + insert + " Inserted "+ type+"");
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
