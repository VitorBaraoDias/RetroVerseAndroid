package com.example.retroverse.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Fatura;
import com.example.retroverse.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.ViewHolder>{


    private List<Fatura> faturaList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemClickOpenInvoiceListener onOpenInvoiceDetailsClickListener;

    public HistoricoAdapter(List<Fatura> faturaList, Context context) {
        this.faturaList = faturaList;
        this.context=context;
    }



    ///listener
    public interface OnItemClickListener {
        void onItemClick(Fatura fatura, int position); // Passa o artigo e a posição
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickOpenInvoiceListener {
        void onOpenInvoiceDetailsClick(Fatura fatura, int position); // Passa o artigo e a posição
    }
    public void setOnItemInvoiceClickListener(OnItemClickOpenInvoiceListener listener) {
        this.onOpenInvoiceDetailsClickListener = listener;
    }
    ///listener

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtCodeHistoricoOrder, txtQuantiadeItemsHistoricoOrder, txtStatusShippedHistoricoOrder, txtTotalOrderHistorico;
        Button btnViewOrderDetailsHistorico, btnOpenInvoiceDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            txtCodeHistoricoOrder = (TextView) itemView.findViewById(R.id.txtCodeHistoricoOrder);
            txtQuantiadeItemsHistoricoOrder = (TextView) itemView.findViewById(R.id.txtQuantiadeItemsHistoricoOrder);
            txtStatusShippedHistoricoOrder = (TextView) itemView.findViewById(R.id.txtStatusShippedHistoricoOrder);
            txtTotalOrderHistorico = (TextView) itemView.findViewById(R.id.txtTotalOrderHistorico);
            btnViewOrderDetailsHistorico = (Button) itemView.findViewById(R.id.btnViewOrderDetailsHistorico);
            btnOpenInvoiceDetails = (Button) itemView.findViewById(R.id.btnOpenInvoiceDetails);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order, parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Fatura fatura = faturaList.get(position);


        holder.txtCodeHistoricoOrder.setText("ORDER#" + fatura.getCodigo());
        holder.txtQuantiadeItemsHistoricoOrder.setText(String.valueOf(fatura.getLinhasVenda().size()));
        holder.txtStatusShippedHistoricoOrder.setText(String.valueOf(fatura.getEstadoEncomenda()) );
        holder.txtTotalOrderHistorico.setText(String.valueOf(fatura.getPrecoFormatado()));


        holder.btnViewOrderDetailsHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(fatura, position); // Passa o artigo e a posição clicada
                }
            }
        });
        holder.btnOpenInvoiceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOpenInvoiceDetailsClickListener != null) {
                    onOpenInvoiceDetailsClickListener.onOpenInvoiceDetailsClick(fatura, position);
                }
            }
        });

    }
    public int getItemCount() {
        return faturaList.size();
    }
    public void updateData(ArrayList<Fatura> faturas) {
        this.faturaList.clear();
        this.faturaList.addAll(faturas);
    }

}
