package com.example.toys_store.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ToysAsyncTask extends AsyncTask<Void, Void, List<Toy>> {
    private FirebaseFirestore db;
    private List<Toy> toys;

    public ToysAsyncTask(){
        toys = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected List<Toy> doInBackground(Void... voids) {
        db.collection("toy")
                .get()
                .addOnSuccessListener(new OnSuccessListener< QuerySnapshot >() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d("Fetch Toy", document.getId() + " => " + document.getData());
                            Toy e=document.toObject(Toy.class);
                            e.setId(Integer.parseInt(document.getId()));
                            toys.add(e);
                        }
                        // update based on adapter
                        onPostExecute(toys);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fetch Toy", "Error getting documents.", e);
                    }
                });
        return toys;
    }

    protected void onPostExecute(List<Toy> result) {
    }

}