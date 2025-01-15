package com.example.retroverse.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.retroverse.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileFragment extends DialogFragment {

    private View rootView;
    private TextInputEditText etDescricao, etLocalizacao;
    private Button btnCancelar, btnConfirmar;
    private ImageView ivFotoPerfil;


    public interface OnProfileEditListener {
        void onProfileEdited(String descricao, String localizacao, String fotoUrl);
    }

    private OnProfileEditListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Verifica se a atividade implementa a interface
            mListener = (EditProfileFragment.OnProfileEditListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnProfileEditListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.dialog_edit_profile, container, false);
        etDescricao = rootView.findViewById(R.id.etProfileDescription);
        ivFotoPerfil = rootView.findViewById(R.id.ivProfileImg);
        btnConfirmar = rootView.findViewById(R.id.btnSavePerfil);

        etDescricao.setText("Atual Descrição");

        Glide.with(this)
                .load("URL_da_foto_atual")
                .placeholder(R.drawable.profile_default_image)
                .into(ivFotoPerfil);


        // Botão de confirmar
        btnConfirmar.setOnClickListener(v -> {
            if (validateFields()) {
                // Coletar os dados editados
                String descricao = etDescricao.getText().toString().trim();
                String localizacao = etLocalizacao.getText().toString().trim();

                // Aqui você pode definir a URL da foto de perfil ou outro valor
                String fotoUrl = "nova_url_foto";

                // Notificar o fragmento ou atividade de que os dados foram editados
                mListener.onProfileEdited(descricao, localizacao, fotoUrl);
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    // Função de validação dos campos
    private boolean validateFields() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etDescricao.getText().toString().trim())) {
            Toast.makeText(getActivity(), "DESCRICAO INVALIDA", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (TextUtils.isEmpty(etLocalizacao.getText().toString().trim())) {
            Toast.makeText(getActivity(), "LOCALIZACAO INVALIDA", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
}
