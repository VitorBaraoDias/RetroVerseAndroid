package com.example.retroverse.Listeners;

import android.content.Context;

import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;

import java.util.ArrayList;

public interface CartListener {
    void onAddCarrinho(Carrinho carrinho);
}
