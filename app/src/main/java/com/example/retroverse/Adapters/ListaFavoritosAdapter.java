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

import java.util.ArrayList;
import java.util.List;

public class ListaFavoritosAdapter extends RecyclerView.Adapter<ListaFavoritosAdapter.ViewHolder> {
    private List<Artigo> artigoList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ListaFavoritosAdapter(List<Artigo> artigoList, Context context) {
        this.artigoList = artigoList != null ? artigoList : new ArrayList<>();
        this.context = context;
    }


    public interface OnItemClickListener {
        void onItemClick(Artigo artigo, int position); // Passa o artigo e a posição
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArtigoCard;
        TextView txtTamanhoCardArtigo, txtMarcaCard, txtPrecoArtigoCard;
        ImageView btnRemoveFavorito; // Botão para remover favorito

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArtigoCard = itemView.findViewById(R.id.imgArtigoCardCarrinho);
            txtTamanhoCardArtigo = itemView.findViewById(R.id.txtTamanhoCardArtigo);
            txtMarcaCard = itemView.findViewById(R.id.txtMarcaArtigoCarrinho);
            txtPrecoArtigoCard = itemView.findViewById(R.id.txtPrecoArtigoCard);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Artigo artigo = artigoList.get(position);

        // Verifica se o favorito está associado corretamente ao artigo
        if (artigo != null) {

            if (artigo != null) {
                // Preenche os dados do artigo no card
                holder.txtTamanhoCardArtigo.setText(artigo.getTamanho());
                holder.txtMarcaCard.setText(artigo.getMarca());
                holder.txtPrecoArtigoCard.setText(String.format("€%.2f", artigo.getPrecoAnuncio()));

                // Carregar a primeira imagem do artigo
                if (artigo.getFotos() != null && !artigo.getFotos().isEmpty()) {
                    String primeiraFotoUrl = artigo.getFotos().get(0);
                    Glide.with(context)
                            .load(primeiraFotoUrl)
                            .placeholder(R.drawable.image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgArtigoCard);
                } else {
                    // Caso não haja fotos, exibir uma imagem padrão
                    holder.imgArtigoCard.setImageResource(R.drawable.image);
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return artigoList.size(); // Retorna o tamanho da lista de favoritos
    }
}
