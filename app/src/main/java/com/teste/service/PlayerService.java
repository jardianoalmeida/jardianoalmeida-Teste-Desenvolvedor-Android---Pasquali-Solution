package com.teste.service;


import retrofit2.Call;
import retrofit2.http.GET;

public interface PlayerService {

    @GET("/teste/teste.json")
    Call<String> buscarPlayer();
}
