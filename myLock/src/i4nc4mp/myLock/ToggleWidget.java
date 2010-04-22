package i4nc4mp.myLock;

//how this works - the widget starts toggler service when clicked
//we manually update the widget via manager interface when toggler executes a change
//so we don't have to care about binding the mediator from here at all

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class ToggleWidget extends AppWidgetProvider {
	
	@Override 
  public void onEnabled(Context context) {	
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		//retrieve a ref to the manager so we can pass a view update
		RemoteViews v = makeView(context);
		ComponentName comp = new ComponentName(context.getPackageName(), ToggleWidget.class.getName());
		mgr.updateAppWidget(comp, v);
	}
	
	/*
	@Override
	public void onReceive (Context context, Intent intent) {
		super.onReceive(context, intent);
		String ex = "";
		Bundle e = intent.getExtras();
		if (e!=null) ex = e.toString();
		Log.v("toggle widget","Intent is " + intent.toString() + " extras - " + ex);
		
	}
	*/
	//some kind of extra is being sent that causes the super implementation to call on update
	//probably the widget ID.
	//we're safe to just change the button's image from toggler service
	
	@Override
	public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//at boot this seems to be getting called twice

			RemoteViews v = makeView(context);
			
			ComponentName comp = new ComponentName(context.getPackageName(), ToggleWidget.class.getName());
			 appWidgetManager.updateAppWidget(comp, v);
      
		//as far as I can tell, repeat calls to this do not cause errors
	}
	
	//We don't need to do anything that retrieves info from network.
	//this should always be fast enough to avoid ANR
	public RemoteViews makeView(Context context) {
				
		Intent i = new Intent();
		i.setClassName("i4nc4mp.myLock", "i4nc4mp.myLock.Toggler");
		PendingIntent myPI = PendingIntent.getService(context, 0, i, 0);
		//tells the widget button to do start command on toggler service when clicked.
		
		// Spawn view, specify layout
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.togglelayout);
      
		//attach an on-click listener to the button element
		views.setOnClickPendingIntent(R.id.toggleButton, myPI);
      
		//determine the currently assumed status of service
		SharedPreferences p = context.getSharedPreferences("myLock",0);
		boolean on = p.getBoolean("enabled", false);
      
		int img;
      
		if (on) img = R.drawable.widg_on_icon;
		else img = R.drawable.widg_off_icon;
      
		//change the button image to reflect service state
		views.setImageViewResource(R.id.toggleButton, img);
      
		return views;
     
	}
}