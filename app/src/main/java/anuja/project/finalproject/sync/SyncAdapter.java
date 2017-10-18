package anuja.project.finalproject.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import anuja.project.finalproject.pojo.Products;
import anuja.project.finalproject.rest.ProductResponse;
import anuja.project.finalproject.rest.WebhoseClient;
import anuja.project.finalproject.rest.WebhoseInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";

    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @SuppressWarnings("unused")
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    public void fetchJson()
    {

        WebhoseInterface apiService = WebhoseClient.getClient().create(WebhoseInterface.class);
        Call<ProductResponse> call = apiService.getProducts("api_key","dresses",true);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ArrayList<Products> products = response.body().getResults();
                Log.d(TAG, "Number of products received: " + products.size());
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
              //System.out.println("------------------------- inside onFailure ------------------:");
                Log.d(TAG, "inside onFailure ");
            }
        });
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
        System.out.println("-------------------- onPerformSync ----------------------------");
        Log.d(TAG, "<<<<onPerformSync");
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String line;
        String jsonStr = null;
        StringBuilder lines = new StringBuilder();
        String type = "";
        fetchJson();

//        try {
//            Uri builtUri = Uri.parse(WebhoseClient.BASE_URL).buildUpon()
//                    .appendQueryParameter(WebhoseClient.TOKEN, WebhoseClient.API_KEY)
//                    .appendQueryParameter(WebhoseClient.FORMAT, WebhoseClient.FORMAT_VALUE)
//                    .appendQueryParameter(WebhoseClient.QUERY, WebhoseClient.QUERY_VALUE)
//                    .appendQueryParameter(WebhoseClient.ON_SALE, WebhoseClient.ON_SALE_VALUE)
//                    .appendQueryParameter(WebhoseClient.SIZE, WebhoseClient.SIZE_VALUE)
//                    .build();
//
//            Log.e(TAG, "Built URI " + builtUri.toString());
//            URL url = new URL(builtUri.toString());
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            InputStream inputStream = urlConnection.getInputStream();
//            if (inputStream == null) {
//                return;
//            }
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            while ((line = bufferedReader.readLine()) != null) {
//                lines.append(line);
//            }
//            if (lines.length() == 0) {
//                return;
//            }
//            jsonStr = lines.toString();
//            Log.e(TAG, type + " ---- " + jsonStr);
//            System.out.println("-------------------------------------------jsonStr::::" + jsonStr);
//            //***************************_DATABASE_*******
//            fetchJson(jsonStr);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null)
//                urlConnection.disconnect();
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

//    private void fetchJson(String jsonStr) {
//
//        if (jsonStr != null) {
//            try {
//                String type = "";
////                JSONObject jObjectAll = new JSONObject(jsonStr);
////                // do we have an error?(404)
////                JSONArray jArray = jObjectAll.getJSONArray(WebhoseClient.PRODUCTS);
////                Vector<ContentValues> cvVector = new Vector<ContentValues>(jArray.length());
////
////                for (int i = 0; i < jArray.length(); i++) {
////                    JSONObject jObject = jArray.getJSONObject(i);
////                    JSONObject threadObject = jObject.getJSONObject(WebhoseClient.SOURCE);
////                    JSONArray jsonArray = jObject.getJSONArray(WebhoseClient.IMAGE);
////                    ProductModel productModel = new ProductModel(
////                            jObject.getString(WebhoseClient.UUID),
////                            jObject.getString(WebhoseClient.URL),
////                            threadObject.getString(WebhoseClient.SITE),
////                            threadObject.getString(WebhoseClient.TITLE),
////                            jObject.getString(WebhoseClient.NAME),
////                            jObject.getString(WebhoseClient.DESCRIPTION),
////
//// );
//
////                    System.out.println("------------------------------- i ----------------------" + i);
////                    System.out.println("------------------------------- UUID ----------------------" + jObject.getString(WebhoseClient.UUID));
////                    System.out.println("------------------------------- URL ----------------------" + jObject.getString(WebhoseClient.URL));
////                    System.out.println("------------------------------- SITE ----------------------" + threadObject.getString(WebhoseClient.SITE));
////                    System.out.println("------------------------------- TITLE ----------------------" + threadObject.getString(WebhoseClient.TITLE));
////                    System.out.println("------------------------------- NAME ----------------------" + jObject.getString(WebhoseClient.NAME));
////                    System.out.println("------------------------------- DESCRIPTION ----------------------" + jObject.getString(WebhoseClient.DESCRIPTION));
////                    System.out.println("------------------------------- IMAGE ----------------------" + jsonArray.getString(0));
////                    System.out.println("-----------------------------------------------------------------" );
//
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
 //   }
}
