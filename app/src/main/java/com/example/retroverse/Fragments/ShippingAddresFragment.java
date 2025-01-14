package com.example.retroverse.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout do fragmento para personalizar a aparência
        rootView = inflater.inflate(R.layout.fragment_shipping_addres, container, false);
        edtPostalCode = rootView.findViewById(R.id.txtPostalCode);

        // Opcional: Configurar título no modal
        if (getDialog() != null) {
            getDialog().setTitle("SHIPPING DETAILS");
        }

        edtPostalCode.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private final String mask = "####-###"; // Máscara do CEP

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String unmasked = s.toString().replaceAll("[^\\d]", ""); // Remove caracteres não numéricos
                StringBuilder masked = new StringBuilder();
                int i = 0;

                // Aplica a máscara enquanto o texto é digitado
                for (char m : mask.toCharArray()) {
                    if (m != '#' && unmasked.length() > i) {
                        masked.append(m);
                    } else {
                        if (unmasked.length() > i) {
                            masked.append(unmasked.charAt(i));
                            i++;
                        } else {
                            break;
                        }
                    }
                }

                isUpdating = true;
                edtPostalCode.setText(masked.toString());
                edtPostalCode.setSelection(masked.length()); // Move o cursor para o final
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Usa o AlertDialog.Builder para criar o diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Configurar o layout do diálogo
        if (rootView == null) {
            // Garante que o layout está inflado antes de configurar o diálogo
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            rootView = inflater.inflate(R.layout.fragment_shipping_addres, null);
        }
        builder.setView(rootView);
        builder.setTitle("SHIPPING DETAILS");

        // Configurar botão positivo
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Validar antes de fechar o diálogo
                if (validateAllFields()) {
                    if (isValidPostalCode(edtPostalCode)) {
                        // Fechar o diálogo após validação bem-sucedida
                        dismiss(); // Fecha o diálogo
                        Toast.makeText(getContext(), "Address Confirmed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Invalid Postal Code", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill in all fields correctly", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Configurar botão negativo
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // O diálogo não deve ser fechado automaticamente aqui
                Toast.makeText(getContext(), "Cancel clicked!", Toast.LENGTH_LONG).show();
            }
        });
        Dialog dialog = builder.create();
        dialog.setCancelable(true);

        return dialog;
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
