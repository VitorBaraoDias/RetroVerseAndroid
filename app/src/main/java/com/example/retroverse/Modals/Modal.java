package com.example.retroverse.Modals;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.example.retroverse.R;

public class Modal {
    private static Dialog loadingDialog;  // Tornar a variável como estática (caso queira usá-la como singleton) ou de instância

    public static void showDialogProgress(Context context) {
        if (loadingDialog == null) {
            // Inicializa o Dialog com a animação Lottie
            loadingDialog = new Dialog(context);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // Para remover título
            loadingDialog.setContentView(R.layout.dialog_loading_lottie);
            loadingDialog.setCancelable(false);  // Impede o fechamento ao tocar fora

            // Definir o fundo escurecido
            Window window = loadingDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(0x80000000)); // 0x80000000 é a cor preta com 50% de opacidade (ARGB)
            }
        }
        loadingDialog.show();
    }

    public static void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
