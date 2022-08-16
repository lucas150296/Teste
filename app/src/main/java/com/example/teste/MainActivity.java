package com.example.teste;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teste.model.LinhaBucas;
import com.example.teste.model.ParadaOnibus;
import com.example.teste.serve.Servel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView textNroLinha, textPartida, textDestino, textPrevisaoChegada, textHoraAtual, textNomePonto, textNomeOnibus;
    Button button;
    EditText textField;

    private String cookie = "";

    Servel servel;

    int codigoParada = 340015333;
    private static String token = "f1ee35df4c69f9bdf8ec5ab041415a0e45d91130f5f9fb560e17e7b8f8b58aba";
    private List<LinhaBucas> linhaDeBucas =new ArrayList<>();
    private ParadaOnibus paradaMock;




    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textNroLinha = findViewById(R.id.textNroLinha);
        textPartida = findViewById(R.id.textPartida);
        textDestino = findViewById(R.id.textDestino);

        textPrevisaoChegada = findViewById(R.id.textPrevisaoChegada);
        textHoraAtual = findViewById(R.id.textHoraAtual);
        textNomePonto = findViewById(R.id.textNomePonto);
        textNomeOnibus = findViewById(R.id.textNroNomeOnibus);

        button = findViewById(R.id.button);
        //textField = findViewById(R.id.textField);

        initRetrofitOkHttpClient();

        requisicaoToken();

        button.setOnClickListener(v -> requisicaoNroOnibus());
    }


    private void parada(){
        Call<ParadaOnibus> paradaOnibusCall = servel.getHora(codigoParada, cookie );
        paradaOnibusCall.enqueue(new Callback<ParadaOnibus>() {
            @Override
            public void onResponse(Call<ParadaOnibus> call, Response<ParadaOnibus> response) {
                if (response.body() != null){
                    String horaAtual = String.valueOf(response.body().getHr());
                    String hora = String.valueOf(response.body().getP().getL().get(0).getVs().get(0).getT());
                    String nomePonto = String.valueOf(response.body().getP().getNp());
                    String nroOnibus = String.valueOf(response.body().getP().getL().get(0).getC());
                    String nomeIda = String.valueOf(response.body().getP().getL().get(0).getLt0());
                    String nomeVolta = String.valueOf(response.body().getP().getL().get(0).getLt1());
                    textHoraAtual.setText("Hora atual: " + horaAtual);
                    textNomePonto.setText("Nome do ponto: " + nomePonto);
                    textNomeOnibus.setText("Informações do onibus: "+nroOnibus + "\n " + nomeIda + " / " + nomeVolta);
                    textPrevisaoChegada.setText("Previsão chegada: " + hora);
                }
            }

            @Override
            public void onFailure(Call<ParadaOnibus> call, Throwable t) {

            }
        });


    }

    private void requisicaoParadaHoraExemplo() {
        Call<ParadaOnibus> paradaOnibus = servel.getHora(codigoParada, cookie);
        paradaOnibus.enqueue(new Callback<ParadaOnibus>() {
            @Override
            public void onResponse(Call<ParadaOnibus> call, Response<ParadaOnibus> response) {
                if (response.body() != null) {
                    String horaAtual = String.valueOf(response.body().getHr());
                    String hora = String.valueOf(response.body().getP().getL().get(0).getVs().get(0).getT());
                    String nomePonto = String.valueOf(response.body().getP().getNp());
                    String nroOnibus = String.valueOf(response.body().getP().getL().get(0).getC());
                    String nomeIda = String.valueOf(response.body().getP().getL().get(0).getLt0());
                    String nomeVolta = String.valueOf(response.body().getP().getL().get(0).getLt1());
                    textHoraAtual.setText("Hora atual: " + horaAtual);
                    textNomePonto.setText("Nome do ponto: " + nomePonto);
                    textNomeOnibus.setText("Informações do onibus: "+nroOnibus + "\n " + nomeIda + " / " + nomeVolta);
                    textPrevisaoChegada.setText("Previsão chegada: " + hora);


                }
//                String oni = String.valueOf(response.body().getP().getL().get(0).getVs().get(0).getT());
            }

            @Override
            public void onFailure(Call<ParadaOnibus> call, Throwable t) {

            }
        });
    }


    private void reqToken(){

        Call<String> login = servel.getLogin(token);
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("deu certo" ,String.valueOf(response.body()));

                cookie = String.valueOf(response.headers().get("Set-coookie"));
                Log.d("cookie", cookie);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                t.getLocalizedMessage();

            }
        });

    }

    private void onibus(){
        Call<List<LinhaBucas>> previsao = servel.getPrivisao(textField.getText().toString(), cookie);

        previsao.enqueue(new Callback<List<LinhaBucas>>() {
            @Override
            public void onResponse(Call<List<LinhaBucas>> call, Response<List<LinhaBucas>> response) {
                if (response.code() == 200){
                    linhaDeBucas = response.body();
                    for (LinhaBucas a : linhaDeBucas){
                        if (a.getTl() == 10){
                            textNroLinha.setText("Nunero da linha" + a.getNroLinha());
                            textPartida.setText("Terminal de partida: " + a.getDenominacaoTPTS());
                            textDestino.setText("Terminal de destino:" + a.getDenominacaoTSTP());
                        }
                    }


                }if(response.raw().body().contentLength() == 2){
                    Toast.makeText(MainActivity.this, "Não encontramos esse onibus em nossa base de dados", Toast.LENGTH_SHORT).show();
                    textNroLinha.setText("");
                    textPartida.setText("");
                    textDestino.setText("");
                }
            }

            @Override
            public void onFailure(Call<List<LinhaBucas>> call, Throwable t) {

            }
        });
    }

    private void requisicaoToken() {
        Call<String> login = servel.getLogin(token);
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("deu certo",String.valueOf(response.body()));
                cookie = String.valueOf(response.headers().get("Set-Cookie"));
                Log.d("cookie", cookie);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    private void requisicaoNroOnibus() {
        requisicaoToken();
        Call<List<LinhaBucas>> previsao = servel.getPrivisao(textField.getText().toString(), cookie);

        previsao.enqueue(new Callback<List<LinhaBucas>>() {
            @Override
            public void onResponse(Call<List<LinhaBucas>> call, Response<List<LinhaBucas>> response) {
                if(response.code() == 200) {
                    linhaDeBucas = response.body();
                    for (LinhaBucas a : linhaDeBucas) {
                        if (a.getTl() == 10) {
                            textNroLinha.setText("Número da linha: " + a.getNroLinha());
                            textPartida.setText("Terminal de partida: " + a.getDenominacaoTPTS());
                            textDestino.setText("Terminal de destino:" + a.getDenominacaoTSTP());
                        }
                    }
                    requisicaoParadaHoraExemplo();
                }if(response.raw().body().contentLength() == 2){
                    Toast.makeText(MainActivity.this, "Não encontramos esse onibus em nossa base de dados", Toast.LENGTH_SHORT).show();
                    textNroLinha.setText("");
                    textPartida.setText("");
                    textDestino.setText("");
                }
            }

            @Override
            public void onFailure(Call<List<LinhaBucas>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                Log.d("erro", t.getMessage());
            }
        });

    }

    private void initRetrofitOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //client
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.olhovivo.sptrans.com.br/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        servel = retrofit.create(Servel.class);
    }



}

