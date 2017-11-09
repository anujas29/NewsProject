package anuja.project.finalproject.rest;

/**
 * Created by USER on 18-10-2017.
 */

public class Webhose {
    //http://webhose.io/productFilter?token=dd64f85a-114f-443d-87ef-a358a27ab135&format=json&q=name%3Adresses%20on_sale%3Atrue%20in_stock%3Atrue%20last_changed%3A%3E1507586400000%20country%3ANL
//
//http://webhose.io/productFilter?token=dd64f85a-114f-443d-87ef-a358a27ab135&format=json&q=name%3Adresses%20%26%20on_sale%3Atrue%20%26%20in_stock%3Atrue%20%26%20last_changed%3A%3E1507586400000%20%26%20country%3ANL

    //http://webhose.io/productFilter?token=dd64f85a-114f-443d-87ef-a358a27ab135&format=json&q=(name:dresses)&on_sale=true

    //http://webhose.io/filterWebContent?token=dec130bd-1e05-4205-878c-40521605253c&format=json&sort=crawled&q=%22donald%20trump%22%20language%3Aenglish
    // URL constans
    public static final String BASE_URL="http://webhose.io/productFilter";
    public static final String TOKEN = "token";
    public static final String API_KEY = "dec130bd-1e05-4205-878c-40521605253c";
    public static final String FORMAT = "format";
    public static final String FORMAT_VALUE = "json";
    public static final String QUERY = "q";
    public static final String QUERY_VALUE="(name:dresses)";
    public static final String ON_SALE = "on_sale";
    public static final String ON_SALE_VALUE = "true";
    public static final String SIZE = "size";
    public static final String SIZE_VALUE = "2";
    public static final String COUNTRY = "country";

    // for Response
    public static final String PRODUCTS = "products";
    public static final String URL = "url";
    public static final String UUID = "uuid";
    public static final String SOURCE = "source";
    public static final String TITLE = "section_title";
    public static final String SITE = "site";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String LASTCHANGE = "last_changed";
    public static final String PRICE = "price";
    public static final String CURRENCY = "currency";
    public static final String OFFER = "offer_price";
    public static final String IMAGE = "images";



////    //URL constans
//    public static final String BASE_URL="http://webhose.io/search";
//    public static final String TOKEN_KEY = "token";
//    public static final String API_KEY = "09efcf63-1c83-47f4-b1ff-62a433a5a999";
//    public static final String FORMAT = "format";
//    public static final String FORMAT_VALUE = "json";
//    public static final String QUERY = "q";
//    public static final String QUERY_VALUE_GLOBAL="(site_type:news)";
//    public static final String SIZE = "size";
//    public static final String SIZE_VALUE = "15";
//    public static final String LANGUAGE = "language";
//    public static final String LANGUAGE_ENGLISH_VALUE="english";
//    public static final String SORT = "sort";
//    public static final String SORT_VALUE="relevancy";
//    public static final String SITE_TYPE = "site_type";
//    public static final String SITE_TYPE_VALUE = "news";
//
//    // Json constants for Response
//    public static final String POSTS = "posts";
//    public static final String THREAD = "thread";
//    public static final String UUID = "uuid";
//    public static final String URL = "url";
//    public static final String SITE = "site";
//    public static final String TITLE = "title";
//    public static final String PUBLISHED = "published";
//    public static final String MAIN_IMAGE = "main_image";
//    public static final String TEXT = "text";




}
