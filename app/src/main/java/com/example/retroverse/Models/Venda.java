package com.example.retroverse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Venda implements Serializable {
    @SerializedName("idvenda")
    private int idVenda;

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

    @SerializedName("linhas_venda")
    private List<Artigo> artigoList;

    // Getters e Setters
    public int getIdVenda() {
        return idVenda;
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

    public List<Artigo> getArtigosList() {
        return artigoList;
    }

    public void setArtigosList( List<Artigo> artigoList) {
        this.artigoList = artigoList;
    }
}
