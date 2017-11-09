package anuja.project.finalproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import anuja.project.finalproject.MainActivity;
import anuja.project.finalproject.R;
import anuja.project.finalproject.sync.SyncAdapter;

/**
 * Created by USER on 08-11-2017.
 */

public class WidgetProvider  extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            //widget header click handle
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            views.setOnClickPendingIntent(R.id.widget_header,pendingIntent);
// Set up our remoteService adapter
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                views.setRemoteAdapter(R.id.widget_list,new Intent(context,WidgetService.class));
            } else {
                views.setRemoteAdapter(0,R.id.widget_list,new Intent(context,WidgetService.class));
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(SyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())){//get the action and start the intent service to update the widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context,getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }
}
