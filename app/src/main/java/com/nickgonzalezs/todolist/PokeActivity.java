package com.nickgonzalezs.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nickgonzalezs.todolist.model.PokeResponse;
import com.nickgonzalezs.todolist.model.Pokemon;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PokeActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Retrofit retrofit;
    private int aidi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Intent i = getIntent();

        if (i != null) {

            int aidi = i.getIntExtra(getString(R.string.poke_id_argument), 0);

            if (aidi > 0) {
                getPokeInfo(aidi);
            } else {
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            }

        } else {
            i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void getPokeInfo(int aidi) {
        PokeService service = retrofit.create(PokeService.class);
        Call<PokeResponse> pokeResponseCall = service.getPokemon(aidi);

        pokeResponseCall.enqueue(new Callback<PokeResponse>() {
            @Override
            public void onResponse(Call<PokeResponse> call, Response<PokeResponse> response) {

                Log.i(TAG, String.valueOf(call.request().url()));

                if (response.isSuccessful()) {

                    PokeResponse pokeResponse = response.body();

                    Log.i(TAG, "onResponse: " + pokeResponse);


                } else {
                    Log.i(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeResponse> call, Throwable t) {

            }
        });
    }
}
