package com.iaihussein.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        String mTitleText = "", mDisplayText = "";
        String mResponse = context.getSharedPreferences(Variables.SHARED_PREFERENCE, Context.MODE_PRIVATE).getString(Variables.SP_RESPONSE, null);
        if (mResponse != null || mResponse.isEmpty()) {
            List<Recipe> mRecipes = new Gson().fromJson(mResponse, new TypeToken<List<Recipe>>() {
            }.getType());
            mTitleText = mRecipes.get(0).getName() + " Ingredients are :";
            for (Ingredient i :
                    mRecipes.get(0).getIngredients()) {
                mDisplayText += (i.getIngredient() + "\n");
            }
            for (Ingredient i :
                    mRecipes.get(0).getIngredients()) {
                mDisplayText += (i.getIngredient() + "\n");
            }
        }


        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, mTitleText);
        views.setTextViewText(R.id.appwidget_text_des, mDisplayText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

