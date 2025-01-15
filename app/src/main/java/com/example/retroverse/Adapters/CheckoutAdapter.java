package com.example.retroverse.Adapters;

import android.annotation.SuppressLint;
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
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.R;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder>{


    private List<Artigo> artigoList;
    private Context context;

    public CheckoutAdapter(List<Artigo> artigoList, Context context) {
        this.artigoList=artigoList;
        this.context=context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgArtigoCardCheckout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgArtigoCardCheckout = (ImageView) itemView.findViewById(R.id.imgArtigoCardCheckout);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_checkout_foto, parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Artigo artigo = artigoList.get(position);

        Glide.with(context)
                .load(artigo.getPrimeiraFotoUrl())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgArtigoCardCheckout);

    }
    public int getItemCount() {
        return artigoList.size();
    }
}
