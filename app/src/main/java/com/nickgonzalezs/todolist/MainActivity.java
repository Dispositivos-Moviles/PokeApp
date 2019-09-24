package com.nickgonzalezs.todolist;

import android.os.Bundle;
import android.util.Log;

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvPokemons.setLayoutManager(gridLayoutManager);

        getPokemons();
    }

    private void getPokemons(){

        PokeService service = retrofit.create(PokeService.class);
        Call<PokeResponse> pokeResponseCall =
                service.getPokemons(0, 20);

        pokeResponseCall.enqueue(new Callback<PokeResponse>() {
            @Override
            public void onResponse(Call<PokeResponse> call, Response<PokeResponse> response) {
                if(response.isSuccessful()){

                    PokeResponse pokeResponse = response.body();

                    ArrayList<Pokemon> pokemons = pokeResponse.getResults();
                    //Log.i(TAG,pokemons.toString());
                    pokeAdapter.addPokemons(pokemons);

                } else{
                    Log.i(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeResponse> call, Throwable t) {

            }
        });

    }

}

