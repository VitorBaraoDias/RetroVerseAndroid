package com.example.retroverse.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.retroverse.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class ShippingAddresFragment extends DialogFragment {

    private View rootView;
    private TextInputEditText edtPostalCode;
    private TextInputLayout edtPostalCodeLayout;
    private Button botaoCancelar;
    private Button botaoConfirm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout do fragmento para personalizar a aparência


        rootView = inflater.inflate(R.layout.fragment_shipping_addres, container, false);
        edtPostalCode = rootView.findViewById(R.id.txtPostalCode);
        edtPostalCodeLayout = rootView.findViewById(R.id.txtPostalCodeLayout);
        botaoCancelar = (Button) rootView.findViewById(R.id.btnCancelShippingDetails);
        botaoConfirm = (Button) rootView.findViewById(R.id.btnConfimrShippingDetails);


        // Opcional: Configurar título no modal
        if (getDialog() != null) {
            getDialog().setTitle("SHIPPING DETAILS");
        }

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO realizando o cancelamento do dialog
            }
        });

        botaoConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateAllFields()){
                    if(isValidPostalCode(edtPostalCode)){
                        dismiss();
                    }
                    else{
                        edtPostalCodeLayout.requestFocus();
                        edtPostalCodeLayout.setError("Invalid Postal code");
                    }
                }
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
    private boolean validateField(TextInputLayout textInputLayout) {
        String fieldValue = textInputLayout.getEditText().getText().toString().trim();
        if (fieldValue.isEmpty()) {
            textInputLayout.requestFocus();
            textInputLayout.setError("Please enter a value");
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    private boolean validateAllFields() {
        TextInputLayout txtNameCheckoutLayout = rootView.findViewById(R.id.txtNameCheckoutLayout);
        TextInputLayout txtCountryCheckoutLayout = rootView.findViewById(R.id.txtCountryCheckoutLayout);
        TextInputLayout txtCidadeCheckoutLayout = rootView.findViewById(R.id.txtCidadeCheckoutLayout);
        TextInputLayout txtAddresLineLayout = rootView.findViewById(R.id.txtAddresLineLayout);
        TextInputLayout txtPostalCodeLayout = rootView.findViewById(R.id.txtPostalCodeLayout);

        boolean isValid = true;
        TextInputLayout[] textInputLayouts = {
                txtNameCheckoutLayout,
                txtCountryCheckoutLayout,
                txtCidadeCheckoutLayout,
                txtAddresLineLayout,
                txtPostalCodeLayout
        };
        for (TextInputLayout textInputLayout : textInputLayouts) {
            isValid = isValid && validateField(textInputLayout);
        }
        return isValid;
    }

    public static boolean isValidPostalCode(TextInputEditText edtPostalCode) {
        String postalCode = edtPostalCode.getText().toString().trim();

        if (TextUtils.isEmpty(postalCode)) {
            return false;
        }
        String postalCodePattern = "^[0-9]{5}-[0-9]{3}$"; // Exemplo de máscara de CEP


        Pattern pattern = Pattern.compile(postalCodePattern);
        return pattern.matcher(postalCode).matches();
    }
}
