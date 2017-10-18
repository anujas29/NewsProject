package anuja.project.finalproject.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import anuja.project.finalproject.pojo.Products;

/**
 * Created by USER on 18-10-2017.
 */

public class ProductResponse {

    @SerializedName("products")
    private ArrayList<Products> results;

    public ArrayList<Products> getResults() {
        return results;
    }
}
