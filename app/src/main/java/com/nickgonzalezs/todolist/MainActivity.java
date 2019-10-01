package com.nickgonzalezs.todolist;

import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
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
import retrofit2.http.HEAD;

public class MainActivity extends AppCompatActivity {


    private final String TAG = getClass().getName();
    private Retrofit retrofit;
    private PokeAdapter pokeAdapter;

    private int limit = 21;
    private int offset = 0;
    private boolean canLoad = true;

    private static final int QRCODE_READED = 1;


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
            case R.id.read_qr:
                readCode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void readCode() {
        Intent i = new Intent(this, QRReaderActivity.class);
        startActivityForResult(i, QRCODE_READED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QRCODE_READED) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra("qrcode"));

                Toast.makeText(this, data.getStringExtra("qrcode"), Toast.LENGTH_LONG).show();

                getPokeInfo(data.getStringExtra("qrcode"));

            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "onActivityResult: ni gaver");
            }
        }
    }

    private void getPokeInfo(String qrcode) {
        int flags = Base64.NO_WRAP;

        String qrcodeDecode = new String(Base64.decode(qrcode, flags));

        String qrcodeEncode = Base64.encodeToString(qrcodeDecode.getBytes(), flags);

        if (qrcodeEncode.equals(qrcode)){
            String[] data = qrcodeDecode.split(";");

            Log.i(TAG, "getPokeInfo: " + data);

            if(data.length == 2){
                String name = data[0];
                int aidi = Integer.parseInt(data[1]);

                Intent i = new Intent(this, PokeActivity.class);
                i.putExtra(getString(R.string.pokemon_aidi), aidi);
                i.putExtra(getString(R.string.pokemon_name), name);

                startActivity(i);
            }
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

