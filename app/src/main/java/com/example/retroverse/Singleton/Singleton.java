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
import com.example.retroverse.Listeners.CartCountRefreshListener;
import com.example.retroverse.Listeners.CartListener;
import com.example.retroverse.Listeners.CartRefreshListener;
import com.example.retroverse.Listeners.CheckoutListener;
import com.example.retroverse.Listeners.ListaArtigosListener;
import com.example.retroverse.Listeners.ListaFavoritosListener;
import com.example.retroverse.Listeners.PerfilRefreshListener;
import com.example.retroverse.Listeners.RefreshArtigosListener;
import com.example.retroverse.Models.Artigo;
import com.example.retroverse.Models.Carrinho;
import com.example.retroverse.Listeners.MetodoExpedicaoListener;
import com.example.retroverse.Listeners.TipoPagamentoListener;
import com.example.retroverse.Models.Metodoexpedicao;
import com.example.retroverse.Models.Perfil;
import com.example.retroverse.Models.Tipopagamento;
import com.example.retroverse.Models.Venda;
import com.example.retroverse.Utils;
import com.example.retroverse.bd.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Singleton {
    public static final String baseUrl = "http://10.0.2.2/RetroVerse/backend/web/api/";


    private static Singleton instance = null;
    private static RequestQueue volleyQueue = null;
    private AuthListener loginListener;
    private AuthCreatAccountListener authCreatAccountListener;
    private ListaArtigosListener listaArtigosListener;
    private RefreshArtigosListener refreshArtigosListener;
    private  ListaFavoritosListener listaFavoritosListener;
    private CartListener cartListener;
    private CartRefreshListener cartRefreshListener;
    private CartCountRefreshListener cartCountRefreshListener;
    private PerfilRefreshListener perfilRefreshListener;
    private ArrayList<Artigo> artigos = new ArrayList<>();
    private ArrayList<Artigo> favoritos = new ArrayList<>();
    private ArrayList<Tipopagamento> tipopagamentos = new ArrayList<>();
    private ArrayList<Metodoexpedicao> metodoexpedicaos = new ArrayList<>();
    private Carrinho carrinho;
    private Perfil perfil;
    private TipoPagamentoListener tipoPagamentoListener;
    private MetodoExpedicaoListener metodoExpedicaoListener;
    private CheckoutListener checkoutListener;

    private DBHelper dbHelper = null;


    public static synchronized Singleton getInstance(Context context) {
        if (instance == null) {
            instance = new Singleton(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }


    private Singleton(Context context){
        artigos = new ArrayList<>();
        carrinho = new Carrinho();
        dbHelper = new DBHelper(context);

    }

    //listeners
    public void setLoginListener(AuthListener listener) {
        this.loginListener = listener;
    }

    public void setPerfilRefreshListener(PerfilRefreshListener listener) {
        this.perfilRefreshListener = listener;
    }

    public void setCreatAccountListener(AuthCreatAccountListener listener) {
        this.authCreatAccountListener = listener;
    }
    public void setArtigosListener(ListaArtigosListener listener) {
        this.listaArtigosListener = listener;
    }
    public void setArtigosRefreshListener(RefreshArtigosListener listener) {
        this.refreshArtigosListener = listener;
    }
    public void setFavoritosListener(ListaFavoritosListener listener) {
        this.listaFavoritosListener = listener;
    }

    public void setCartListeneristener(CartListener listener) {
        this.cartListener = listener;
    }
    public void setCartRefreshListener(CartRefreshListener listener) {
        this.cartRefreshListener = listener;
    }
    public void setCartCountRefreshListener(CartCountRefreshListener listener) {
        this.cartCountRefreshListener = listener;
    }
    public void setTipoPagamentoListener(TipoPagamentoListener listener) {
        this.tipoPagamentoListener = listener;
    }
    public void setMetodoexpedicaoListener(MetodoExpedicaoListener listener) {
        this.metodoExpedicaoListener = listener;
    }
    public void setCheckoutListener(CheckoutListener listener) {
        this.checkoutListener = listener;
    }
    //listeners

    public void adicionarArtigosBD(ArrayList<Artigo> artigos){
        //apagar todos os livros e adicionar os livros atuais
        dbHelper.removerAllArtigosBD();
        for(Artigo l:favoritos)
            adicionarArtigoBD(l);
    }
    public void adicionarArtigoBD(Artigo artigo){
        dbHelper.adicionarFavoritoArtigo(artigo);
    }
    public void removerArtigoBD(Artigo artigo){
        dbHelper.removerFavoritoArtigo(artigo.getId());
    }



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
                    Log.d("ERRO", error.getMessage().toString() );

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

    ///ARTIGOS API
    public void getAllArtigosAPI( String token,final String tipoartigo, final String tamanho, final String estado, final String marca, final String categoria, final Context context){
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "artigos/filtro?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Resposta Artigos", response);

                    artigos = Utils.parseJsonToList(response, Artigo.class);

                    if (listaArtigosListener != null) {
                        listaArtigosListener.onGetListaArtigos(getArtigos());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));
            volleyQueue.add(stringRequest);
        }
    }
    ///ARTIGOS API


    ///CARRINHO API

    //usa o listener cartListener.onAddCarrinho e da refresh do count
    public void addToCartAPI(String token, int idartigo , Context context){
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "carrinhos?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //get do artigo
                    carrinho = Utils.fromJson(response, Carrinho.class);
                    if (cartListener != null) {
                        cartListener.onAddCarrinho(carrinho);
                    }
                    if (cartCountRefreshListener != null) {
                        cartCountRefreshListener.onRefreshCarrinho(carrinho);
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
                    params.put("idartigo", String.valueOf(idartigo));
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
    public void getCartApi(String token, Context context) {
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "carrinhos?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Resposta cart", response);

                    carrinho = Utils.fromJson(response, Carrinho.class);

                    if (cartCountRefreshListener != null) {
                        cartCountRefreshListener.onRefreshCarrinho(carrinho);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));
            volleyQueue.add(stringRequest);
        }
    }
    public void removeItemCartApi(String token, Artigo artigo, Context context) {
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "linhacarrinhos/"+ String.valueOf(artigo.getId()) +"?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Resposta linhacarrinho", response);


                    if (cartCountRefreshListener != null && cartRefreshListener != null) {
                        removeItemToCart(artigo);

                        cartCountRefreshListener.onRefreshCarrinho(carrinho);
                        cartRefreshListener.onRefreshCarrinho(carrinho);

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));
            volleyQueue.add(stringRequest);
        }
    }

    ////CARINHO API

    ///CHECKOUT API

    public void addCheckoutAPI(String token, int idmetodoexpedicao, int idtipopagamento, String nome, String codigopostal, String morada,  String pais,  String cidade, Context context){
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "vendas/efetuarcompra?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("venda", response);
                    Venda venda = Utils.fromJson(response, Venda.class);
                    if (cartCountRefreshListener != null && checkoutListener != null && listaArtigosListener != null) {
                        artigos = removeArtigosAfterCheckout();
                        listaArtigosListener.onGetListaArtigos(artigos);                           //remove os artigos do carrinho
                        //remove os artigos que foram vendidos


                        carrinho.getLinhasCarrinho().clear();
                        cartCountRefreshListener.onRefreshCarrinho(carrinho);
                        checkoutListener.onOrderDetails(venda);
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
                    params.put("idmetodoexpedicao", String.valueOf(idmetodoexpedicao));
                    params.put("idtipopagamento", String.valueOf(idtipopagamento));
                    params.put("nome", String.valueOf(nome));
                    params.put("codigopostal", String.valueOf(codigopostal));
                    params.put("morada", String.valueOf(morada));
                    params.put("pais", String.valueOf(pais));
                    params.put("cidade", String.valueOf(cidade));
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


    //PERFIL API
    public void getPerfilAPI(String token, Context context) {
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "perfils?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Resposta perfil", response);

                    perfil = Utils.fromJson(response, Perfil.class);

                    if (perfilRefreshListener != null) {
                        perfilRefreshListener.onRefreshPerfil(perfil);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));
            volleyQueue.add(stringRequest);
        }
    }

    public void putPerfilAPI(String token, Context context, String descricao, String morada){
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "perfils/editar?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("perfil", response);

                    Perfil perfil = Utils.fromJson(response, Perfil.class);
                    if (perfilRefreshListener != null) {
                        perfilRefreshListener.onRefreshPerfil(perfil);
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
                    params.put("descricao", String.valueOf(descricao));
                    params.put("morada", String.valueOf(morada));
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            volleyQueue.add(stringRequest);
        }
    }

    //PERFIL API


    ///Metodo pagamento API
    public void getAllATiposPagamentoAPI( String token, final Context context){
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "tipopagamentos?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Resposta tipos de pagamento", response);

                    tipopagamentos = Utils.parseJsonToList(response, Tipopagamento.class);

                    if (tipoPagamentoListener != null) {
                        tipoPagamentoListener.onRefreshTipoPagamento(tipopagamentos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            volleyQueue.add(stringRequest);
        }
    }

    public void getAllMetodoExpedicaoAPI( String token, final Context context){
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "metodoexpedicaos?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    metodoexpedicaos = Utils.parseJsonToList(response, Metodoexpedicao.class);

                    if (metodoExpedicaoListener != null) {
                        metodoExpedicaoListener.onRefreshMetodoExpedicao(metodoexpedicaos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            volleyQueue.add(stringRequest);
        }
    }

    ///Metodo pagamento API

    public void getFavoritosAPI(String token, final Context context) {
        if (!Utils.isConnectionInternet(context)) {
            Toast.makeText(context, "No Internet connection, local favourites have been loaded", Toast.LENGTH_LONG).show();
            favoritos = dbHelper.getTodosFavoritos(Utils.getToken(context));

            if (listaFavoritosListener != null) {
                listaFavoritosListener.onRefreshListaFavoritos(favoritos);
            }
        } else {
            String url = baseUrl + "favoritos?access-token=" + token;

            // Requisição GET
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(" Favoritos", response);

                    favoritos = Utils.parseJsonToList(response, Artigo.class);
                    adicionarArtigosBD(favoritos);


                    if (listaFavoritosListener != null) {
                        listaFavoritosListener.onRefreshListaFavoritos(favoritos);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));

            volleyQueue.add(stringRequest);
        }
    }

    /// Functions ITEMS
    public ArrayList<Artigo> getArtigos(){
        return new ArrayList<>(artigos);
    }
    public Artigo getArtigo(int id){
        for(Artigo l:artigos){
            if(l.getId() == id){
                return l;
            }
        }
        return null;
    }

    public ArrayList<Artigo>  removeArtigosAfterCheckout(){
        for (Artigo artigoCarrinho : carrinho.getLinhasCarrinho()) {
            artigos.removeIf(artigo -> artigo.getId() == artigoCarrinho.getId());
        }
        return new ArrayList<>(artigos);
    }
    public ArrayList<Artigo> filterNonPremiumArticles(int idActiveItem) {
        ArrayList<Artigo> nonPremiumArticles = new ArrayList<>();

        for (Artigo article : artigos) {
            if (!article.isPremium() && article.getId() != idActiveItem) {
                nonPremiumArticles.add(article);
            }
        }

        return nonPremiumArticles;
    }
    public ArrayList<Artigo> filterPremiumArticles() {
        ArrayList<Artigo> premiumArticles = new ArrayList<>();

        for (Artigo article : artigos) {
            if (article.isPremium()) {
                premiumArticles.add(article);
            }
        }

        return premiumArticles;
    }
    /// Functions ITEMS
    /// Functions Cart
    public Carrinho getCart() {
        return carrinho;
    }

    public void removeItemToCart(Artigo artigo){
        if(artigo != null){
            carrinho.getLinhasCarrinho().remove(artigo);
        }
    }
    /// ITEMS


    /// FAVORITOS

    public void addFavoritoAPI(String token, int idartigo, Context context){
        // Realizando a requisição GET
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "favoritos?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("adicionar favorito", response);

                    Artigo artigo = Utils.fromJson(response, Artigo.class);
                    favoritos.add(artigo);
                    adicionarArtigoBD(artigo);

                    if (refreshArtigosListener != null) {
                        refreshArtigosListener.onRefreshListaArtigos();

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
                    params.put("idartigo", String.valueOf(idartigo));
                    return params;
                }
            };
            // Configurando o RetryPolicy
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));

            volleyQueue.add(stringRequest);
        }
    }

    public void removeFavoritoAPI(String token,  Artigo artigo, Context context) {
        if(!Utils.isConnectionInternet(context)){
            Toast.makeText(context, "Não tem ligação a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            String url = baseUrl + "favoritos/"+ String.valueOf(artigo.getId()) +"?access-token=" + token;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Resposta linhacarrinho", response);

                    removerArtigoBD(artigo);
                    if (refreshArtigosListener!= null) {
                        refreshArtigosListener.onRefreshListaArtigos();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.displayError(error, context);
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000, // Tempo de timeout em milissegundos (15 segundos)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Número máximo de tentativas
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Fator de multiplicação
            ));
            volleyQueue.add(stringRequest);
        }
    }

    ///FAVORITOS


    public Tipopagamento getTipoPagementoByPosition(int position){

        return tipopagamentos.get(position);
    }
    public Metodoexpedicao getMetodoExpedicaoById(int id){
        for(Metodoexpedicao l:metodoexpedicaos){
            if(l.getId() == id){
                return l;
            }
        }
        return null;
    }

    public Metodoexpedicao getMetodoExpedicaoByPosition(int position){
        return metodoexpedicaos.get(position);
    }


    //RETORNAR O PERFIL DO USER
    public Perfil getPerfil() {
        return perfil;
    }
}
