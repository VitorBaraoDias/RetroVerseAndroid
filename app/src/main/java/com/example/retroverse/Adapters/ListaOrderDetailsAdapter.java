package com.example.retroverse.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Linhavenda;
import com.example.retroverse.R;

import java.util.ArrayList;
import java.util.List;

public class ListaOrderDetailsAdapter extends RecyclerView.Adapter<ListaOrderDetailsAdapter.ViewHolder>{


    private List<Linhavenda> linhavendas;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private boolean mostrarLikes; // Novo sinalizador


    public ListaOrderDetailsAdapter(List<Linhavenda> linhavendas, Context context, boolean mostrarLikes) {
        this.linhavendas=linhavendas;
        this.context=context;
        this.mostrarLikes=mostrarLikes;
    }


    ///listener
    public interface OnItemClickListener {
        void onItemClick(Linhavenda linhavenda, int position, TextView txtm, Button btn); // Passa o artigo e a posição
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    ///listener

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgArtigoCard;
        TextView txtTamanhoCardArtigo, txtMarcaCard, txtPrecoArtigoCard, txtStatusLinhaVenda;
        Button btnAllOkayOrderDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArtigoCard = (ImageView) itemView.findViewById(R.id.imgArtigoCardOrderDetails);
            txtTamanhoCardArtigo = (TextView) itemView.findViewById(R.id.txtTamanhoCardOrderDetails);
            txtMarcaCard = (TextView) itemView.findViewById(R.id.txtMarcaOrderDetails);
            txtPrecoArtigoCard = (TextView) itemView.findViewById(R.id.txtPrecorderDetails);
            txtStatusLinhaVenda = (TextView) itemView.findViewById(R.id.txtStatusLinhaVenda);
            btnAllOkayOrderDetails = (Button) itemView.findViewById(R.id.btnAllOkayOrderDetails);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_details, parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Linhavenda linhavenda = linhavendas.get(position);


        holder.txtTamanhoCardArtigo.setText(linhavenda.getArtigo().getTamanho());
        holder.txtMarcaCard.setText(linhavenda.getArtigo().getMarca());
        holder.txtPrecoArtigoCard.setText(String.valueOf(linhavenda.getArtigo().getPrecoAnuncio()) );
        holder.txtStatusLinhaVenda.setText(linhavenda.getEstadoencomenda());

        if(linhavenda.getEstadoencomenda().equals("ORDER SENT")){
            holder.btnAllOkayOrderDetails.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(linhavenda.getArtigo().getPrimeiraFotoUrl())
                .placeholder(R.drawable.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgArtigoCard);


        holder.btnAllOkayOrderDetails.setOnClickListener(v -> {
            onItemClickListener.onItemClick(linhavenda, position,
                    holder.txtStatusLinhaVenda,  holder.btnAllOkayOrderDetails);
        });
    }
    public int getItemCount() {
        return linhavendas.size();
    }
    public void updateData(ArrayList<Linhavenda> linhavendas) {
        this.linhavendas.clear();
        this.linhavendas.addAll(linhavendas);
    }
}
