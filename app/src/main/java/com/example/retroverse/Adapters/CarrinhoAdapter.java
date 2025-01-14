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
import com.example.retroverse.Listeners.CartRefreshListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.R;

import java.util.List;

public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.ViewHolder>{


    private List<Artigo> artigoList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CarrinhoAdapter(List<Artigo> artigoList, Context context) {
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
        ImageView imgArtigoCardCarrinho, imgCloseCardCarrinho;
        TextView txtTamanhoArtigoCarrinho, txtCondicaoArtigoCarrinho, txtMarcaArtigoCarrinho, txtPrecoArtigoCarrinho, txtTipoArtigoCarrinho;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgArtigoCardCarrinho = (ImageView) itemView.findViewById(R.id.imgArtigoCardCarrinho);
            imgCloseCardCarrinho = (ImageView) itemView.findViewById(R.id.imgCloseCardCarrinho);
            txtTamanhoArtigoCarrinho = (TextView) itemView.findViewById(R.id.txtTamanhoArtigoCarrinho);
            txtCondicaoArtigoCarrinho = (TextView) itemView.findViewById(R.id.txtCondicaoArtigoCarrinho);
            txtMarcaArtigoCarrinho = (TextView) itemView.findViewById(R.id.txtMarcaArtigoCarrinho);
            txtPrecoArtigoCarrinho = (TextView) itemView.findViewById(R.id.txtPrecoArtigoCarrinho);
            txtTipoArtigoCarrinho = (TextView) itemView.findViewById(R.id.txtTipoArtigoCarrinho);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardcarrinho, parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Artigo artigo = artigoList.get(position);


        holder.txtTamanhoArtigoCarrinho.setText(artigo.getTamanho());
        holder.txtMarcaArtigoCarrinho.setText(artigo.getMarca());
        holder.txtPrecoArtigoCarrinho.setText(String.valueOf(artigo.getPrecoFormatado()) );
        holder.txtCondicaoArtigoCarrinho.setText(String.valueOf(artigo.getEstado()) );
        holder.txtCondicaoArtigoCarrinho.setText(String.valueOf(artigo.getEstado()) );
        holder.txtTipoArtigoCarrinho.setText(artigo.getTipoArtigo());

        Glide.with(context)
                .load(artigo.getPrimeiraFotoUrl())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgArtigoCardCarrinho);

        holder.imgCloseCardCarrinho.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(artigo, position);
            }
        });

    }
    public int getItemCount() {
        return artigoList.size();
    }
}
