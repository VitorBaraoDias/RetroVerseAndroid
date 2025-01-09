package com.example.retroverse;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.retroverse.Models.Artigo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
    public static void displayError(VolleyError error, Context context) {
        if (error.networkResponse != null) {
            // Extrair o código de status HTTP
            int statusCode = error.networkResponse.statusCode;

            try {
                String responseBody = new String(error.networkResponse.data, "UTF-8");
                JSONObject jsonResponse = new JSONObject(responseBody);
                String errorMessage = getErrorMessage(statusCode, jsonResponse);

                // Exibe a mensagem do erro
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
                // Caso não consiga processar o erro JSON ou a resposta
                Toast.makeText(context, "Erro ao processar a resposta do servidor", Toast.LENGTH_LONG).show();
            }

        } else {
            handleNetworkError(error, context);
        }
    }

    private static String getErrorMessage(int statusCode, JSONObject jsonResponse) {
        String errorMessage = "Erro desconhecido";

        // Verifica se existe a chave "message" no JSON
        if (jsonResponse.has("message")) {
            errorMessage = jsonResponse.optString("message", errorMessage);
        }
        switch (statusCode) {
            case 400:
                errorMessage = "Erro 400: Solicitação inválida";
                break;
            case 401:
                errorMessage = "Erro 401: " + errorMessage; // Se o JSON contiver a chave "message"
                break;
            case 403:
                errorMessage = "Erro 403: " + errorMessage; // Se o JSON contiver a chave "message"
                break;
            case 500:
                errorMessage = "Erro 500: Erro interno do servidor";
                break;
            default:
                errorMessage = "Erro " + statusCode + ": " + errorMessage;
                break;
        }

        return errorMessage;
    }

    private static void handleNetworkError(VolleyError error, Context context) {
        if (error instanceof TimeoutError) {
            Toast.makeText(context, "Erro: O tempo de resposta expirou", Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(context, "Erro: Sem conexão com a internet", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Erro desconhecido de rede", Toast.LENGTH_LONG).show();
        }
    }
    public static String parserJsonLogin(String response){
        String token = null;
        try {
            JSONObject login = new JSONObject(response);
            token = login.getString("auth_key");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return token;
    }



    ///artigos
    public static ArrayList<Artigo> parseArtigosJson(String jsonResponse) {
        // Criar uma instância de Gson
        Gson gson = new Gson();

        // Definir o tipo de retorno (uma ArrayList de Artigos)
        Type tipoListaArtigos = new TypeToken<ArrayList<Artigo>>() {}.getType();

        // Deserializar o JSON para o ArrayList de artigos
        ArrayList<Artigo> artigos = gson.fromJson(jsonResponse, tipoListaArtigos);

        return artigos;
    }
    ///
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        return token != null ? token : "false";
    }
}
