package com.example.retroverse.Adapters;

import android.annotation.SuppressLint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.R;

import java.util.ArrayList;
import java.util.List;

public class ListaArtigosAdapter extends RecyclerView.Adapter<ListaArtigosAdapter.ViewHolder>{


    private List<Artigo> artigoList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLikeClickListener onItemClickLikeListener;
    private boolean mostrarLikes; // Novo sinalizador


    public ListaArtigosAdapter(List<Artigo> artigoList, Context context, boolean mostrarLikes) {
        this.artigoList=artigoList;
        this.context=context;
        this.mostrarLikes=mostrarLikes;
    }


    ///listener
    public interface OnItemClickListener {
        void onItemClick(Artigo artigo, int position); // Passa o artigo e a posição
    }
    public interface OnItemLikeClickListener {
        void onItemLikeClick(Artigo artigo, int position); // Passa o artigo e a posição
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setOnItemLikeClickListener(OnItemLikeClickListener listener) {
        this.onItemClickLikeListener = listener;
    }
    ///listener

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgArtigoCard, imgContainerLike;
        TextView txtTamanhoCardArtigo, txtMarcaCard, txtPrecoArtigoCard;
        CardView cardContainerLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArtigoCard = (ImageView) itemView.findViewById(R.id.imgArtigoCardCarrinho);
            txtTamanhoCardArtigo = (TextView) itemView.findViewById(R.id.txtTamanhoCardArtigo);
            txtMarcaCard = (TextView) itemView.findViewById(R.id.txtMarcaArtigoCarrinho);
            txtPrecoArtigoCard = (TextView) itemView.findViewById(R.id.txtPrecoArtigoCard);
            imgContainerLike = (ImageView) itemView.findViewById(R.id.imgContainerLike);
            cardContainerLike = (CardView) itemView.findViewById(R.id.cardContainerLike);
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


        configurarLike(holder, this.mostrarLikes, artigo.isLiked());

        Glide.with(context)
                .load(artigo.getPrimeiraFotoUrl())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgArtigoCard);

        holder.imgArtigoCard.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(artigo, position); // Passa o artigo e a posição clicada
            }
        });
        holder.cardContainerLike.setOnClickListener(v -> {
            configurarLike(holder,true, artigo.isLiked());
            onItemClickLikeListener.onItemLikeClick(artigo, position);
        });

    }
    private void configurarLike(ViewHolder holder, boolean mostrarLikes, boolean isLiked) {
        if (mostrarLikes) {
            holder.imgContainerLike.setVisibility(View.VISIBLE);
            if (isLiked) {
                holder.imgContainerLike.setImageResource(R.drawable.group_170);
            } else {
                holder.imgContainerLike.setImageResource(R.drawable.group_171);
            }
        } else {
            holder.imgContainerLike.setVisibility(View.GONE);
            holder.cardContainerLike.setVisibility(View.GONE);
        }
    }
    public int getItemCount() {
        return artigoList.size();
    }
    public void updateData(ArrayList<Artigo> novosArtigos) {
        this.artigoList.clear();
        this.artigoList.addAll(novosArtigos);
    }
}
