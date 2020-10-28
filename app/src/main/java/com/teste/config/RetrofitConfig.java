package com.teste.config;

import com.teste.service.PlayerService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://sportsmatch.com.br")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public PlayerService getPlayerService() {
        return this.retrofit.create(PlayerService.class);
    }

}
