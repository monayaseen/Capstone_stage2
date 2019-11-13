package com.example.toys_store;
import android.app.ActivityOptions;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toys_store.adapters.ToysAdapter;
import com.example.toys_store.data.LaterViewModel;
import com.example.toys_store.data.Toy;
import com.example.toys_store.data.ToysAsyncTask;
import com.example.toys_store.widget.LaterReadWidget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreActivity extends AppCompatActivity {
    @BindView(R.id.fav) Button fav;
    @BindView(R.id.wread) Button wread;
    @BindView(R.id.don)Button don;
    @BindView(R.id.allbooks)Button allbooks;

    @BindView(R.id.toolbar)
    Toolbar myToolBar;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;
    private ToysAdapter mToysAdapter;
    private ArrayList<Toy> toys = new ArrayList<>();
    private Context context;
    private Bundle bundle;
    private List<Toy> later = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.recycler_view);
            getWindow().setEnterTransition(slide);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        setSupportActionBar(myToolBar);
        title.setText(R.string.book_list);
        setTitle("");
        context = getApplicationContext();
        initRecyclerView();
        setUpViewModel();
        callTheAsyncTask();
        allbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTheAsyncTask();
            }
        });
        wread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToysAdapter.setToys(later);
                mToysAdapter.notifyDataSetChanged();            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all:
                callTheAsyncTask();
                break;
            case R.id.wread:
                mToysAdapter.setToys(later);
                mToysAdapter.notifyDataSetChanged();
                break;
            case R.id.log_out:
                LogOut();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void LogOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mToysAdapter = new ToysAdapter(toys, context);
        mRecyclerView.setAdapter(mToysAdapter);

        mToysAdapter.setItemClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                Intent n = new Intent(context, ToysDetails.class);
                Gson gson = new Gson();
                n.putExtra(Toy.class.getName(), gson.toJson(mToysAdapter.getToys().get(position)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(n, bundle);
                }
            }
        });
    }

    private void callTheAsyncTask(){
        new ToysAsyncTask(){
            @Override
            protected void onPostExecute(List<Toy> result) {
                super.onPostExecute(result);
                mToysAdapter.setToys(result);
                mToysAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void setUpViewModel(){
        Log.d("MyLog","setUpViewModel");
        LaterViewModel viewModel = ViewModelProviders.of(this).get(LaterViewModel.class);
        viewModel.getLater().observe(this, new Observer<List<Toy>>() {
            @Override
            public void onChanged(List<Toy> toys) {
                later.clear();
                later.addAll(toys);
                mToysAdapter.notifyDataSetChanged();
                for (int i=0; i< toys.size();i++)
                    Log.d("MyLogWidget", toys.get(i).getId()+"");
                addLaterToWidget(toys);
            }
        });
    }

    private void addLaterToWidget(List<Toy> b){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, LaterReadWidget.class));
        LaterReadWidget.updateAppWidget(context, appWidgetManager, appWidgetIds, b);
    }
}
