package com.example.teste.model;

import com.google.gson.annotations.SerializedName;

public class LinhaBucas {


        @SerializedName("lt")
        String lt;
        @SerializedName("tp")
        String tp;
        @SerializedName("ts")
        String ts;
        @SerializedName("tl")
        int tl;



        public String getNroLinha() {
            return lt;
        }

        public String getDenominacaoTPTS() {
            return tp;
        }

        public String getDenominacaoTSTP() {
            return ts;
        }
        public int getTl() {
            return tl;
        }
}
