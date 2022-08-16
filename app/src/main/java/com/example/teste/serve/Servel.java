package com.example.teste.serve;

import com.example.teste.model.LinhaBucas;
import com.example.teste.model.ParadaOnibus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Servel {
    @POST("/Login/Autenticar?token=")
    Call<String> getLogin(@Query("token") String token );

    @GET("/Linha/Buscar?termosBusca=")
    Call<List<LinhaBucas>> getPrivisao(@Query("ternoBusca")String nrolinha,
                                       @Header("cookie") String cookie);

    @GET("Previsao/Parada")
    Call<ParadaOnibus> getHora(@Query("codigoParada") int codigoParada,
                               @Header("cookie") String cookie);
}
