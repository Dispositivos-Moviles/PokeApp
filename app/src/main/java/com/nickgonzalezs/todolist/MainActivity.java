package com.nickgonzalezs.todolist;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.nickgonzalezs.todolist.adapters.PokeAdapter;
import com.nickgonzalezs.todolist.model.PokeResponse;
import com.nickgonzalezs.todolist.model.Pokemon;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {


    private final String TAG = getClass().getName();
    private Retrofit retrofit;
    private PokeAdapter pokeAdapter;

    int limit = 21;
    int offset = 0;
    boolean canLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RecyclerView rvPokemons = findViewById(R.id.rv_pokemons);

        pokeAdapter = new PokeAdapter(this);
        rvPokemons.setAdapter(pokeAdapter);

        rvPokemons.setHasFixedSize(true);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvPokemons.setLayoutManager(gridLayoutManager);

        rvPokemons.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.i(TAG, "onScrolled: " + dy);

                if (dy > 0) {


                    int visibleCount = gridLayoutManager.getChildCount();
                    int pastVisibleCount = gridLayoutManager.findFirstVisibleItemPosition();
                    int totalItems = gridLayoutManager.getItemCount();

                    Log.i(TAG, "onScrolled: " + visibleCount);
                    Log.i(TAG, "onScrolled: " + totalItems);
                    Log.i(TAG, "onScrolled: " + pastVisibleCount);
                    if (canLoad) {
                        if ((visibleCount + pastVisibleCount) >= totalItems) {
                            offset += limit;
                            getPokemons(offset);
                        }
                    }

                }
            }
        });

        getPokemons(offset);
    }

    private void getPokemons(int offset) {

        canLoad = false;

        PokeService service = retrofit.create(PokeService.class);
        Call<PokeResponse> pokeResponseCall =
                service.getPokemons(offset, limit);

        pokeResponseCall.enqueue(new Callback<PokeResponse>() {
            @Override
            public void onResponse(Call<PokeResponse> call, Response<PokeResponse> response) {
                if (response.isSuccessful()) {

                    PokeResponse pokeResponse = response.body();

                    ArrayList<Pokemon> pokemons = pokeResponse.getResults();
                    //Log.i(TAG,pokemons.toString());
                    pokeAdapter.addPokemons(pokemons);
                    canLoad = true;

                } else {
                    Log.i(TAG, "Error: " + response.errorBody());
                    canLoad = true;
                }
            }

            @Override
            public void onFailure(Call<PokeResponse> call, Throwable t) {
                canLoad = true;
            }
        });

    }

}

