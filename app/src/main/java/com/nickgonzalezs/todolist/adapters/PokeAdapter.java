package com.nickgonzalezs.todolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nickgonzalezs.todolist.R;
import com.nickgonzalezs.todolist.model.Pokemon;

import java.util.ArrayList;

public class PokeAdapter extends RecyclerView.Adapter<PokeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Pokemon> pokemons;

    public PokeAdapter(Context context, ArrayList<Pokemon> pokemons) {
        this.context = context;
        this.pokemons = pokemons;
    }

    public PokeAdapter(Context context) {
        this.context = context;
        pokemons = new ArrayList<>();
    }

    @NonNull
    @Override
    public PokeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poke_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokeAdapter.ViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);

        holder.pokeName.setText(pokemon.getName());

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getAidi() + ".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pokeImg);

    }

    @Override
    public int getItemCount() {
        return pokemons != null ? pokemons.size() : 0;
    }

    public void addPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons.addAll(pokemons);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pokeImg;
        private TextView pokeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pokeImg = itemView.findViewById(R.id.poke_img);
            pokeName = itemView.findViewById(R.id.poke_name);

        }
    }
}
