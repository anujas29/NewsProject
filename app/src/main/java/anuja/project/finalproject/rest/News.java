package anuja.project.finalproject.rest;

/**
 * Created by USER on 02-12-2017.
 */

public class News {
    public String Id;
    public String Url;
    public String Site;
    public String Date;
    public String Title;
    public String Text;
    public String ImagePath;


    public News(String id,String url,String site,String date,String title,String text,String imagePath){
        Id = id;
        Url=url;
        Site=site;
        Date=date;
        Title=title;
        Text=text;
        ImagePath=imagePath;
    }
}