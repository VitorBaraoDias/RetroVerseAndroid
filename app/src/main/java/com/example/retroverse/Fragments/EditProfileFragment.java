package com.example.retroverse.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap lastBitmap;

    private View rootView;
    private TextInputEditText etDescricao, etLocalizacao;
    private Button btnConfirmar;
    private ImageView ivFotoPerfil, btnEditarFoto;


    public static EditProfileFragment newInstance(String descricao, String localizacao, String fotoUrl) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("descricao", descricao);
        args.putString("localizacao", localizacao);
        args.putString("fotoUrl", fotoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnProfileEditListener {
        void onProfileEdited(String descricao, String localizacao, String fotoUrl);
    }

    private OnProfileEditListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (EditProfileFragment.OnProfileEditListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement OnProfileEditListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.dialog_edit_profile, container, false);
        etDescricao = rootView.findViewById(R.id.etProfileDescription);
        etLocalizacao = rootView.findViewById(R.id.etProfileLocation);
        ivFotoPerfil = rootView.findViewById(R.id.ivProfileImg);
        btnConfirmar = rootView.findViewById(R.id.btnSavePerfil);
        btnEditarFoto = rootView.findViewById(R.id.btnEditProfilePicture);

        if (getArguments() != null) {
            String descricao = getArguments().getString("descricao", "");
            String localizacao = getArguments().getString("localizacao", "");
            String fotoUrl = getArguments().getString("fotoUrl", "");

            etDescricao.setText(descricao);
            etLocalizacao.setText(localizacao);

            Glide.with(this)
                    .load(fotoUrl)
                    .placeholder(R.drawable.profile_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true) // Ignorar o cache em memória
                    .into(ivFotoPerfil);
        }

        btnEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btnConfirmar.setOnClickListener(v -> {
            if (validateFields()) {

                String profileImage = "";

                if (lastBitmap != null) {
                    profileImage = getStringImage(lastBitmap);
                } else {
                    String fotoUrlAtual = getArguments().getString("fotoUrl", "");
                    profileImage = fotoUrlAtual;
                }

                    Singleton.getInstance(getActivity()).putPerfilAPI(
                            Utils.getToken(getActivity()),
                            getActivity(),
                            etDescricao.getText().toString(),
                            etLocalizacao.getText().toString(),
                            profileImage
                    );

                    if (mListener != null) {
                        mListener.onProfileEdited(
                                etDescricao.getText().toString().trim(),
                                etLocalizacao.getText().toString().trim(),
                                profileImage
                        );
                    }
                }
            dismiss();
        });

        return rootView;
    }

    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                lastBitmap = bitmap;

                ImageView ivFotoPerfil = rootView.findViewById(R.id.ivProfileImg);
                ivFotoPerfil.setImageBitmap(lastBitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Erro ao carregar imagem!", Toast.LENGTH_SHORT).show();
            }
        }
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
