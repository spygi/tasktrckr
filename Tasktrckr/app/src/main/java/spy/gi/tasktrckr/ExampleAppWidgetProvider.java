package spy.gi.tasktrckr;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Created by giannaks on 10/04/16.
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int appWidgetId = appWidgetIds[0];

        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setOnClickPendingIntent(R.id.button_engineering, pendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public void onEngineeringClicked(View view) {
        Log.d("ExampleAppWidgetProvider", "eng clicked");
    }

}
