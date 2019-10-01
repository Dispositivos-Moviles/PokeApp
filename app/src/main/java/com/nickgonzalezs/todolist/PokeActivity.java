package com.nickgonzalezs.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.nickgonzalezs.todolist.model.PokeResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.HEAD;

public class PokeActivity extends AppCompatActivity {

    private static final String TAG = "PokeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        final Toolbar toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();

        if (intent != null) {
            int aidi = intent.getIntExtra(getString(R.string.pokemon_aidi), 0);
            String name = intent.getStringExtra(getString(R.string.pokemon_name));

            if (aidi > 0) {

                toolbar.setTitle(name.toUpperCase());

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
    }
}
