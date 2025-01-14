package com.example.retroverse.Singleton;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.retroverse.Listeners.AuthCreatAccountListener;
import com.example.retroverse.Listeners.AuthListener;
import com.example.retroverse.Listeners.ListaArtigosListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Singleton {
    public static final String baseUrl = "http://10.0.2.2/defesa/RetroVerse/backend/web/api/";


    private static Singleton instance = null;
    private static RequestQueue volleyQueue = null;
    private AuthListener loginListener;
    private AuthCreatAccountListener authCreatAccountListener;

    private ListaArtigosListener listaArtigosListener;

    public ArrayList<Artigo> artigos = new ArrayList<>();
    public static synchronized Singleton getInstance(Context context) {
        if (instance == null) {
            instance = new Singleton(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }
    private Singleton(Context context){
        artigos = new ArrayList<>();
    }

    //listeners
    public void setLoginListener(AuthListener listener) {
        this.loginListener = listener;
    }
    public void setCreatAccountListener(AuthCreatAccountListener listener) {
        this.authCreatAccountListener = listener;
    }
    public void setArtigosListener(ListaArtigosListener listener) {
        this.listaArtigosListener = listener;
    }
    //listeners


    /// USER
    public void loginAPI(final String username, final String password, Context context) {
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            // Volley RequestQueue para enviar requisição HTTP
                StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl + "users/login", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("resposta", response );

                        String token = Utils.parserJsonLogin(response);
                        if (loginListener != null) {
                            loginListener.onValidateLogin(token, context);
                        }
                    }
                }
            , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };
            // Configurando o RetryPolicy
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));

            volleyQueue.add(stringRequest);
        }
    }
    public void createAccountAPI(final String username, final String email, final String password, Context context) {
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            // Volley RequestQueue para enviar requisição HTTP
            StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl + "users", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("resposta", response );

                    String token = Utils.parserJsonLogin(response);
                    if (authCreatAccountListener != null) {
                        authCreatAccountListener.onValidateCreatAccount(token, context);
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            // Configurando o RetryPolicy
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));

            volleyQueue.add(stringRequest);
        }
    }

    /// USER

    ///ARTIGOS
    public void getAllArtigosAPI( String token,final String tipoartigo, final String tamanho, final String estado, final String marca, final String categoria, final Context context){
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "artigos/filtro?access-token=" + token;
        // Adicionando parâmetros à URL, se existirem
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Resposta Artigos", response);

                    // Parse da resposta JSON
                    // Exemplo: Uma função fictícia para parse de artigos, adapte conforme necessário
                    artigos = Utils.parseArtigosJson(response);

                    // Chamando o listener para atualizar a UI com os artigos recebidos
                    if (listaArtigosListener != null) {
                        listaArtigosListener.onRefreshListaArtigos(artigos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            // Configurando o RetryPolicy
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));

            // Adicionando a requisição à fila de requisições do Volley
            volleyQueue.add(stringRequest);
        }
    }

    public Artigo getArtigo(int id){
        for(Artigo l:artigos){
            if(l.getId() == id){
                return l;
            }
        }
        return null;
    }
    // Filter non-premium articles
    public ArrayList<Artigo> filterNonPremiumArticles() {
        ArrayList<Artigo> nonPremiumArticles = new ArrayList<>();

        for (Artigo article : artigos) {
            if (!article.isPremium()) {
                nonPremiumArticles.add(article);
            }
        }

        return nonPremiumArticles;
    }

    // Filter premium articles
    public ArrayList<Artigo> filterPremiumArticles() {
        ArrayList<Artigo> premiumArticles = new ArrayList<>();

        for (Artigo article : artigos) {
            if (article.isPremium()) {
                premiumArticles.add(article);
            }
        }

        return premiumArticles;
    }
    /// ARTICLES
}
