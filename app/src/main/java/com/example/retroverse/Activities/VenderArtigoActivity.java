package com.example.retroverse.Activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.retroverse.Listeners.AddArtigoListener;
import com.example.retroverse.Models.Categoria;
import com.example.retroverse.Models.Estado;
import com.example.retroverse.Models.Marca;
import com.example.retroverse.Models.Tamanho;
import com.example.retroverse.Models.Tipopagamento;
import com.example.retroverse.R;
import com.example.retroverse.Singleton.Singleton;
import com.example.retroverse.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VenderArtigoActivity extends AppCompatActivity implements AddArtigoListener {

    private List<Uri> selectedImages = new ArrayList<>();
    private ImagePagerAdapter adapter;
    private ViewPager2 viewPager;
    AutoCompleteTextView dropdown_Categorias, dropdown_menuMarca, dropdown_menuCondicao, dropdown_menuTamanho;

    TextInputEditText etTitle, etDescription, etPrice;
    private final ActivityResultLauncher<String> pickImagesLauncher =
            registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
                selectedImages.clear();
                selectedImages.addAll(uris);
                adapter.notifyDataSetChanged();
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vender_artigo);

        findViews();

        adapter = new ImagePagerAdapter(selectedImages);
        viewPager.setAdapter(adapter);

        carregarInfoDropDown();

        dropdown_Categorias.setOnItemClickListener((parent, view, position, id) -> {
            dropdown_Categorias.setTag((Categoria) parent.getItemAtPosition(position));
        });
        dropdown_menuMarca.setOnItemClickListener((parent, view, position, id) -> {
            dropdown_menuMarca.setTag((Marca) parent.getItemAtPosition(position));
        });
        dropdown_menuCondicao.setOnItemClickListener((parent, view, position, id) -> {
            dropdown_menuCondicao.setTag((Estado) parent.getItemAtPosition(position));
        });
        dropdown_menuTamanho.setOnItemClickListener((parent, view, position, id) -> {
            dropdown_menuTamanho.setTag((Tamanho) parent.getItemAtPosition(position));
        });
    }

    private void findViews(){
        viewPager = findViewById(R.id.viewPagerInserirArtigo);
        dropdown_Categorias = findViewById(R.id.dropdown_Categorias);
        dropdown_menuMarca = findViewById(R.id.dropdown_menuMarca);
        dropdown_menuCondicao = findViewById(R.id.dropdown_menuCondicao);
        dropdown_menuTamanho = findViewById(R.id.dropdown_menuTamanho);

        etTitle = findViewById(R.id.etProfileDescription);
        etDescription = findViewById(R.id.edescricaoInserirArtigo);
        etPrice = findViewById(R.id.etPrice);
    }
    public void backMainActivity(View view) {
        finish();
    }

    public void btnUploadPhotos(View view) {
        pickImagesLauncher.launch("image/*");
    }
    private void carregarInfoDropDown(){
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Singleton.getInstance(this).getCategorias());
        dropdown_Categorias.setAdapter(adapter);

        ArrayAdapter<Marca> adapterMarca = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Singleton.getInstance(this).getMarcas());
        dropdown_menuMarca.setAdapter(adapterMarca);

        ArrayAdapter<Estado> adapterEstado = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Singleton.getInstance(this).getEstados());
        dropdown_menuCondicao.setAdapter(adapterEstado);

        ArrayAdapter<Tamanho> adapterTamanho = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Singleton.getInstance(this).getTamanhos() );
        dropdown_menuTamanho.setAdapter(adapterTamanho);
    }

    public void saveItem(View view) {
        if (validateForm()) {
            Categoria selectedCategory = (Categoria) dropdown_Categorias.getTag();  // Supondo que você tenha armazenado o objeto no tag
            Marca selectedBrand = (Marca) dropdown_menuMarca.getTag();
            Estado selectedCondition = (Estado) dropdown_menuCondicao.getTag();
            Tamanho selectedSize = (Tamanho) dropdown_menuTamanho.getTag();

            Singleton.getInstance(this).setAddArtigoListener(this);
            Singleton.getInstance(this).addArtigoAPI(Utils.getToken(this),
                    etTitle.getText().toString(), etDescription.getText().toString(), etPrice.getText().toString(),
            selectedCondition.getId(), selectedBrand.getId(), selectedSize.getId(), selectedCategory.getId(), getBase64ImagesList(), this);
            Toast.makeText(this, "Valid form! Saving..." + selectedCategory.getNome(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Fill in all the required fields!", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validateForm() {
        boolean isValid = true;

        // Validate title
        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError("Title is required!");
            etTitle.requestFocus();
            isValid = false;
        }

        // Validate description
        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Description is required!");
            etDescription.requestFocus();
            isValid = false;
        }

        // Validate category
        if (dropdown_Categorias.getText().toString().trim().isEmpty()) {
            dropdown_Categorias.setError("Please select a category!");
            dropdown_Categorias.requestFocus();
            isValid = false;
        }

        // Validate brand
        if (dropdown_menuMarca.getText().toString().trim().isEmpty()) {
            dropdown_menuMarca.setError("Please select a brand!");
            dropdown_menuMarca.requestFocus();
            isValid = false;
        }

        // Validate condition
        if (dropdown_menuCondicao.getText().toString().trim().isEmpty()) {
            dropdown_menuCondicao.setError("Please select the condition of the item!");
            dropdown_menuCondicao.requestFocus();
            isValid = false;
        }

        // Validate size
        if (dropdown_menuTamanho.getText().toString().trim().isEmpty()) {
            dropdown_menuTamanho.setError("Please select a size!");
            dropdown_menuTamanho.requestFocus();
            isValid = false;
        }

        // Validate price
        if (etPrice.getText().toString().trim().isEmpty()) {
            etPrice.setError("Price is required!");
            etPrice.requestFocus();
            isValid = false;
        }

        return isValid;
    }
    public List<String> getBase64ImagesList() {
        List<String> base64Images = new ArrayList<>();

        try {
            for (Uri imageUri : selectedImages) {
                Bitmap bitmap = getBitmapFromUri(imageUri);
                String base64Image = encodeBitmapToBase64(bitmap);
                base64Images.add(base64Image);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao converter imagem", Toast.LENGTH_SHORT).show();
        }

        return base64Images;
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
    }
    public String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    @Override
    public void onAddArtigo() {
        finish();
    }
}