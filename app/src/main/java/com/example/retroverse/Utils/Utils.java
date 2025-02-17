package com.example.retroverse.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
            Log.d("erro total", String.valueOf(error.networkResponse.data));

            try {
                String responseBody = new String(error.networkResponse.data, "UTF-8");
                JSONObject jsonResponse = new JSONObject(responseBody);
                String errorMessage = getErrorMessage(statusCode, jsonResponse);

                // Exibe a mensagem do erro
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();

            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
                Toast.makeText(context, "Erro ao processar a resposta do servidor", Toast.LENGTH_LONG).show();
            }

        } else {
            handleNetworkError(error, context);
        }
    }
    private static String getErrorMessage(int statusCode, JSONObject jsonResponse) {
        String errorMessage = "Erro desconhecido";

        Log.d("erros", String.valueOf(jsonResponse));
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
    public static <T> ArrayList<T> parseJsonToList(String jsonResponse, Class<T> typeClass) {
        Gson gson = new Gson();
        Type listType = TypeToken.getParameterized(ArrayList.class, typeClass).getType();
        return gson.fromJson(jsonResponse, listType);
    }
    public static <T> T fromJson(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
    ///
    public static String getToken(Context context) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        return token != null ? token : "false";
    }
}
