package com.teste.service;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.teste.model.Barra;
import com.teste.model.CopaMundoDisputada;
import com.teste.model.CopaMundoVencida;
import com.teste.model.Player;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPService extends AsyncTask<Void, Void, Player> {

    Player player;

    @Override
    protected Player doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        JSONObject jsonObject;

        try {
            URL url = new URL("http://sportsmatch.com.br/teste/teste.json");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.connect();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bufferedInputStream.read();
            while(result2 != -1) {
                buf.write((byte) result2);
                result2 = bufferedInputStream.read();
            }

            jsonObject = new JSONObject(buf.toString());
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
