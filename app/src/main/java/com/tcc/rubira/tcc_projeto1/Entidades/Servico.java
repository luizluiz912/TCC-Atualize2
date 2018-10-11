package com.tcc.rubira.tcc_projeto1.Entidades;

public class Servico {

    private String id;
    private String nome;
    private String descricao;
    private String tipo;
    private Float preco_metro;

    public Servico() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Float getPreco_metro() {
        return preco_metro;
    }

    public void setPreco_metro(Float preco_metro) {
        this.preco_metro = preco_metro;
    }



}
