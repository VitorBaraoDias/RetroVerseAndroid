package com.example.retroverse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.retroverse.Models.Fatura;
import com.example.retroverse.Models.Linhavenda;
import com.example.retroverse.R;

import java.util.ArrayList;

public class ListViewOrdersAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Linhavenda> linhavendas; // Mantemos somente as linhas de venda

    public ListViewOrdersAdapter(Context context, Fatura fatura) {
        this.context = context;
        this.linhavendas = fatura.getLinhasVenda(); // Obtemos as linhas de venda da fatura
    }

    @Override
    public int getCount() {
        return linhavendas.size(); // Retorna o tamanho das linhas de venda
    }

    @Override
    public Object getItem(int i) {
        return linhavendas.get(i); // Retorna o item na posição 'i'
    }

    @Override
    public long getItemId(int i) {
        return i; // Pode ser usado o índice como ID
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null) {
            view = inflater.inflate(R.layout.card_fatura, null);
        }

        // Otimização com ViewHolder
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(linhavendas.get(i));

        return view;
    }

    private class ViewHolderLista {
        private TextView tvPrimeiro, tvSegundo, tvTerceiro;

        public ViewHolderLista(View view) {
            tvPrimeiro = view.findViewById(R.id.tvPrimeiro);
            tvSegundo = view.findViewById(R.id.tvSegundo);
            tvTerceiro = view.findViewById(R.id.tvTerceiro);
        }

        public void update(Linhavenda linhavenda) {
            // Atualizamos os TextViews e o ImageView com os dados da linha de venda
            tvPrimeiro.setText("1");
            tvSegundo.setText(linhavenda.getArtigo().getNome());
            tvTerceiro.setText(linhavenda.getArtigo().getPrecoFormatado());
        }
    }
}
