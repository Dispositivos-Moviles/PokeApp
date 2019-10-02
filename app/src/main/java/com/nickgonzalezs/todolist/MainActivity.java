package com.nickgonzalezs.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


    private static final int QR_READ = 2345;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qr_reader:
                readQR();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void readQR() {
        Intent i = new Intent(this, QRReaderActivity.class);
        startActivityForResult(i, QR_READ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_READ){
            if(resultCode == RESULT_OK){

                getPokemonInfo(data.getStringExtra(getString(R.string.qrcode)));

            } else if(resultCode == RESULT_CANCELED){

            }
        }
    }

    private void getPokemonInfo(String qrcode) {

        String qrdecoded = new String(Base64.decode(qrcode, Base64.NO_WRAP));
        Log.i(TAG, "getPokemonInfo: "  + qrdecoded);

        String[] pokedata = qrdecoded.split(";");

        if(pokedata.length  == 2 ){
            String name = pokedata[0];
            int id = Integer.parseInt(pokedata[1]);

            Intent i = new Intent(this, PokeActivity.class);

            i.putExtra(this.getString(R.string.pokemon_aidi), id);
            i.putExtra(this.getString(R.string.pokemon_name), name);

            startActivity(i);
        }
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

