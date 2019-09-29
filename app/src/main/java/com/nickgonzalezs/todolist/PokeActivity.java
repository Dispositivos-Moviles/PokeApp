package com.nickgonzalezs.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PokeActivity extends AppCompatActivity {

    private static final String TAG = "PokeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        Intent intent = getIntent();

        if(intent != null){
            int aidi = intent.getIntExtra(getString(R.string.pokemon_aidi), 0);
            String name = intent.getStringExtra(getString(R.string.pokemon_name));

            if(aidi > 0){

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
    }
}
