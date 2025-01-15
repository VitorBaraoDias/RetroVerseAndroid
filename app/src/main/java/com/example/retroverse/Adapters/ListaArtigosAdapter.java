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

public class ListaArtigosAdapter extends RecyclerView.Adapter<ListaArtigosAdapter.ViewHolder>{


    private List<Artigo> artigoList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ListaArtigosAdapter(List<Artigo> artigoList, Context context) {
        this.artigoList=artigoList;
        this.context=context;
    }


    ///listener
    public interface OnItemClickListener {
        void onItemClick(Artigo artigo, int position); // Passa o artigo e a posição
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    ///listener

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgArtigoCard;
        TextView txtTamanhoCardArtigo, txtMarcaCard, txtPrecoArtigoCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArtigoCard = (ImageView) itemView.findViewById(R.id.imgArtigoCardCarrinho);
            txtTamanhoCardArtigo = (TextView) itemView.findViewById(R.id.txtTamanhoCardArtigo);
            txtMarcaCard = (TextView) itemView.findViewById(R.id.txtMarcaArtigoCarrinho);
            txtPrecoArtigoCard = (TextView) itemView.findViewById(R.id.txtPrecoArtigoCard);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardartigo, parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Artigo artigo = artigoList.get(position);


        holder.txtTamanhoCardArtigo.setText(artigo.getTamanho());
        holder.txtMarcaCard.setText(artigo.getMarca());
        holder.txtPrecoArtigoCard.setText(String.valueOf(artigo.getPrecoAnuncio()) );

        Glide.with(context)
                .load(artigo.getPrimeiraFotoUrl())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgArtigoCard);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(artigo, position); // Passa o artigo e a posição clicada
            }
        });

    }
    public int getItemCount() {
        return artigoList.size();
    }
}
