package com.example.retroverse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class Fatura implements Serializable {
    @SerializedName("idvenda")
    private int idVenda;
    private String codigo;

    @SerializedName("total")
    private double total;

    @SerializedName("datavenda")
    private String dataVenda;

    @SerializedName("estadoencomenda")
    private String estadoEncomenda;

    @SerializedName("metodoexpedicao")
    private String metodoExpedicao;

    @SerializedName("tipopagamento")
    private String tipoPagamento;
    private String nome;
    private String codigopostal;
    private String morada;

    private String pais;
    private String cidade;
    @SerializedName("linhasvenda")
    private ArrayList<Linhavenda> linhavendas = new ArrayList<>();
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Linhavenda> getLinhavendas() {
        return linhavendas;
    }

    public void setLinhavendas(ArrayList<Linhavenda> linhavendas) {
        this.linhavendas = linhavendas;
    }

    // Getters e Setters
    public int getIdVenda() {
        return idVenda;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getEstadoEncomenda() {
        return estadoEncomenda;
    }

    public void setEstadoEncomenda(String estadoEncomenda) {
        this.estadoEncomenda = estadoEncomenda;
    }

    public String getMetodoExpedicao() {
        return metodoExpedicao;
    }

    public void setMetodoExpedicao(String metodoExpedicao) {
        this.metodoExpedicao = metodoExpedicao;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public ArrayList<Linhavenda> getLinhasVenda() {
        return linhavendas;
    }

    public void setLinhasVenda( ArrayList<Linhavenda> linhavendas) {
        this.linhavendas = linhavendas;
    }
    public ArrayList<Artigo> getArtigos() {
        ArrayList<Artigo> artigos = new ArrayList<>();
        for (Linhavenda linha : linhavendas) {
            if (linha.getArtigo() != null) {
                artigos.add(linha.getArtigo());
            }
        }
        return artigos;
    }


    public String getPrecoFormatado() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);

        return format.format(total) + "€";
    }
}
