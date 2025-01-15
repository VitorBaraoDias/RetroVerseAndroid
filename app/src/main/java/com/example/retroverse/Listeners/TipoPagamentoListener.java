package com.example.retroverse.Listeners;

import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.Models.Tipopagamento;

import java.util.ArrayList;

public interface TipoPagamentoListener {
    void onRefreshTipoPagamento(ArrayList<Tipopagamento> tipopagamentos);

}
