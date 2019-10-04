package com.nickgonzalezs.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nickgonzalezs.todolist.model.PokeResponse;
import com.nickgonzalezs.todolist.model.PokeSingleResponse;

import java.lang.ref.Reference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PokeActivity extends AppCompatActivity {

    private static final String TAG = "PokeActivity";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        Intent intent = getIntent();

        if(intent != null){
            int aidi = intent.getIntExtra(getString(R.string.pokemon_aidi), 0);
            String name = intent.getStringExtra(getString(R.string.pokemon_name));

            if(aidi > 0){

                retrofit = new Retrofit.Builder()
                        .baseUrl("https://pokeapi.co/api/v2/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setTitle(name);

                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);


                getPokemon(aidi);
            }
        }

    }

    private void getPokemon(int aidi) {
        Glide.with(this)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + aidi + ".png")
                .into((ImageView) findViewById(R.id.poke_img));

        PokeService service = retrofit.create(PokeService.class);
        Call<PokeSingleResponse> pokeResponseCall = service.getPokemon(aidi);

        pokeResponseCall.enqueue(new Callback<PokeSingleResponse>() {
            @Override
            public void onResponse(Call<PokeSingleResponse> call, Response<PokeSingleResponse> response) {
                if(response.isSuccessful()){
                    PokeSingleResponse singlePokemonResponse = response.body();

                    Log.i(TAG, "onResponse: " + singlePokemonResponse.toString());

                }else{
                    Log.i(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeSingleResponse> call, Throwable t) {

            }
        });
    }
}
