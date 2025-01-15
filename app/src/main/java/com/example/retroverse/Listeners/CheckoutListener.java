package com.example.retroverse.Listeners;

import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.Models.Venda;

public interface CheckoutListener {
    void onOrderDetails(Venda venda);

}
