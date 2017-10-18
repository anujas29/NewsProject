package anuja.project.finalproject.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USER on 11-10-2017.
 */

public class WebhoseClient {
 public static final String BASE_URL = "http://webhose.io/";
 private static Retrofit retrofit = null;

 public static Retrofit getClient() {
  if (retrofit == null) {
   retrofit = new Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build();
  }
  return retrofit;
 }

}