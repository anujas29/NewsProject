package anuja.project.finalproject.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import anuja.project.finalproject.R;
import anuja.project.finalproject.data.ProductContract;

/**
 * Created by USER on 08-11-2017.
 */

public class WidgetService extends RemoteViewsService {



        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new RemoteViewsFactory() {
                private Cursor data=null;
                @Override
                public void onCreate() {

                }

                @Override
                public void onDataSetChanged() {//notifyAppWidgetViewDataChanged()
                    System.out.println("----------------------- T1-------------------------------------------");
                    if (data != null) {
                        data.close();
                    }
                    // This method is called by the app hosting the widget (e.g., the launcher)
                    // However, our ContentProvider is not exported so it doesn't have access to the
                    // data. Therefore we need to clear (and finally restore) the calling identity so
                    // that calls use our process and permission so that we can get the data from content ptovider
                    final long identityToken= Binder.clearCallingIdentity();
                    data = getContentResolver().query(
                            ProductContract.ProductEntry.CONTENT_URI,
                            ProductContract.ProductEntry.WIDGET_COLUMNS,
                            null,
                            null,
                            null
                    );
                    Binder.restoreCallingIdentity(identityToken);
                }

                @Override
                public void onDestroy() {
                    System.out.println("--------------------------- T2---------------------------------------");
                    if (data != null) {
                        data.close();
                        data = null;
                    }
                }

                @Override
                public int getCount() {
                    System.out.println("------------------------ t3------------------------------------------"+data.getCount());
                    return data == null ? 0 : data.getCount();
                }

                @Override
                public RemoteViews getViewAt(int position) {
                    System.out.println("-------------------------- t4----------------------------------------");
                    if(position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position)){
                        return null;
                    }
                    RemoteViews views = new RemoteViews(getPackageName(),
                            R.layout.widget_item);
                    views.setTextViewText(R.id.title_widget,data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_TITLE)));

                    final Intent fillInIntent = new Intent();
                    fillInIntent.setData(ProductContract.ProductEntry.CONTENT_URI);
                    views.setOnClickFillInIntent(R.id.widget_list_item_root,fillInIntent);

                    return views;
                }

                @Override
                public RemoteViews getLoadingView() {
                    System.out.println("------------------------- t5-----------------------------------------");
                    return new RemoteViews(getPackageName(),R.layout.widget_item);
                }

                @Override
                public int getViewTypeCount() {
                    System.out.println("------------------------ t6------------------------------------------");

                    return 1;
                }

                @Override
                public long getItemId(int position) {
                    System.out.println("------------------------- t7-----------------------------------------");
                    if(data.moveToPosition(position))
                        return data.getInt(data.getColumnIndex(ProductContract.ProductEntry._ID));
                    return position;
                }

                @Override
                public boolean hasStableIds() {
                    return true;
                }
            };
        }
    }