package com.example.toys_store;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.toys_store.adapters.ToysAdapter;
import com.example.toys_store.data.AppDatabase;
import com.example.toys_store.data.AppExecutors;
import com.example.toys_store.data.Toy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToysDetails extends AppCompatActivity {
    @BindView(R.id.readpdf)
    Button buy_url;
    @BindView(R.id.add_later)
    Button add_later;
    @BindView(R.id.add_fav)
    Button add_fav;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.bookcover)
    ImageView bookcover;
    @BindView(R.id.bookdesc)
    TextView bookdesc;
    private Toy b;
    private AppDatabase db;
    private LiveData< Toy > f;
    private boolean fav;

    private FirebaseAuth firebaseAuth;
    private ToysAdapter mBooksAdapter;
    private Context context;
    private Bundle bundle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookdetails_activity);
        ButterKnife.bind(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.TOP);
            slide.addTarget(R.id.add_fav);
            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(new Explode());
        }

        db = AppDatabase.getInstance(getApplicationContext());

        String Extra = getIntent().getStringExtra(Toy.class.getName());
        Gson gson = new Gson();
        if (savedInstanceState == null) {
            b = gson.fromJson(Extra, Toy.class);
        }
//        Toast.makeText(BookDetailsActivity.this,
//                b.getDesc(), Toast.LENGTH_LONG).show();
        bookdesc.setText(b.getDesc());
       // String imageUri =b.getCover_url();
        //Picasso.with(this).load(imageUri).into(bookcover);

        //setSupportActionBar(mToolBar);
        title.setText(b.getName());
        //setTitle("");
        add_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (fav) {
                            //remove from database
                            db.laterDAO().deleteToy(b);
                        } else {
                            //add to database
                            db.laterDAO().insertToy(b);
                        }
                    }
                });
                if (fav) {
                    add_later.setText("Add to Later read");
                    Toast.makeText(ToysDetails.this,
                            "Removed from Later read", Toast.LENGTH_SHORT).show();
                } else {
                    add_later.setText("Remove from Later");
                    Toast.makeText(ToysDetails.this,
                            "Added To Later", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buy_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookUrl= b.getBuy_url();
                Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(bookUrl));
                startActivity(n);
            }
        });

    }

    @Override
    protected void onResume() {
        f = db.laterDAO().loadBookById(b.getId());
        f.observeForever(new Observer< Toy >() {
            @Override
            public void onChanged(Toy exercise) {
                if (f.getValue() != null) {
                    fav = true;
                    add_fav.setText("Remove from Later read");
                } else {
                    fav = false;
                }
            }
        });
        if (fav) {
            add_later.setText("Remove from Later");
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("toy", b);
        super.onSaveInstanceState(outState);
    }




}