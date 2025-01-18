package com.example.retroverse.Modals;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import com.example.retroverse.R;

public class Modal {
    private static Dialog loadingDialog;  // Tornar a variável como estática (caso queira usá-la como singleton) ou de instância

    public static void showDialogProgress(Context context) {
        if (!(context instanceof Activity) || ((Activity) context).isFinishing() || ((Activity) context).isDestroyed()) {
            Log.e("Modal", "Cannot show dialog: Context is not a valid Activity or the Activity is not running.");
            return;
        }

        if (loadingDialog == null || !loadingDialog.isShowing()) {
            // Inicializa o Dialog com a animação Lottie
            loadingDialog = new Dialog(context);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // Para remover título
            loadingDialog.setContentView(R.layout.dialog_loading_lottie);
            loadingDialog.setCancelable(false);  // Impede o fechamento ao tocar fora

            // Definir o fundo escurecido
            Window window = loadingDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(0x80000000)); // Preto com 50% de opacidade (ARGB)
            }

            loadingDialog.show();
        } else {
            Log.d("Modal", "Loading dialog is already showing.");
        }
    }

    public static void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
