package com.teste.service;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.teste.model.Barra;
import com.teste.model.CopaMundoDisputada;
import com.teste.model.CopaMundoVencida;
import com.teste.model.Player;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HTTPService extends AsyncTask<Void, Void, Player> {

    Player player;

    @Override
    protected Player doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        JSONObject jsonObject;
        Type userListType = new TypeToken<ArrayList<Player>>(){}.getType();


        try {
            URL url = new URL("http://sportsmatch.com.br/teste/teste.json");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                resposta.append(scanner.next());
            }
            jsonObject = new JSONObject(resposta.toString());
            JSONObject playerJson = new JSONObject(jsonObject.getJSONArray("Object").get(0).toString());
            JSONObject copaMundoVencidaJson = new JSONObject(playerJson.getJSONObject("Player").get("Barras").toString()).getJSONObject("Copas_do_Mundo_Vencidas");
            JSONObject copaMundoDisputadaJson = new JSONObject(playerJson.getJSONObject("Player").get("Barras").toString()).getJSONObject("Copas_do_Mundo_Disputadas");


            Player playerObject = new Gson().fromJson(playerJson.get("Player").toString(), Player.class);
            CopaMundoVencida copaMundoVencida = new Gson().fromJson(copaMundoVencidaJson.toString(), CopaMundoVencida.class);
            CopaMundoDisputada copaMundoDisputada = new Gson().fromJson(copaMundoDisputadaJson.toString(), CopaMundoDisputada.class);
            Barra barra = new Barra();
            barra.setCopaMundoVencida(copaMundoVencida);
            barra.setCopaMundoDisputada(copaMundoDisputada);
            playerObject.setBarra(barra);

            player = playerObject;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return player;
    }
}
