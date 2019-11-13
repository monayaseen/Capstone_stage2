package com.example.toys_store.widget;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import android.util.Log;

import com.example.toys_store.R;
import com.example.toys_store.data.Toy;
import java.util.List;

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Toy> toys;
    public MyRemoteViewsFactory(Context context){
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        toys = LaterReadWidget.getToys();
    }
    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return toys.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_card);
        Toy b = toys.get(i);
        views.setTextViewText(R.id.book_info, b.getName()+" : "+b.getBuy_url());
        Log.d("MyLog",b.getName());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}