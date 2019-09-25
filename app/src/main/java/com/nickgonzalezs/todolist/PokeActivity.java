package com.nickgonzalezs.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class PokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent != null){
            int aidi = intent.getIntExtra(getString(R.string.pokemon_aidi), 0);

            if(aidi > 0){
                getPokemon(aidi);
            }
        }

    }

    private void getPokemon(int aidi) {

    }
}
