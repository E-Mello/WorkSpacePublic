package com.trabalhoed1.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Pessoa {
    private final String identificacao;
    private final String nome;
    private final Date dataNascimento;
    private final int prioridade;

    // Utilizado na rotação das filas.
    private boolean emAtendimento;
    private boolean pegouPrioridade;

    // Construtor.
    public Pessoa(String nome, Date dataNascimento, int prioridade) {
        this.identificacao = UUID.randomUUID().toString();
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.prioridade = prioridade;
        this.emAtendimento = false;
        this.pegouPrioridade = false;
    }

    // Identificação da pessoa, utilizado internamente na rotação das filas.
    public String getId() {
        return identificacao;
    }

    // Utilizado na classificação por ordem de nascimento.
    public Date getDataNascimento() {
        return dataNascimento;
    }

    // Utilizado para verificar se a pessoa possui uma prioridade especial.
    public boolean temPrioridade() {
        return prioridade != -1;
    }

    public boolean emAtendimento() {
        return emAtendimento;
    }

    public void setEmAtendimento(boolean value) {
        this.emAtendimento = value;
    }

    public boolean pegouPrioridade() {
        return pegouPrioridade;
    }

    public void setPegouPrioridade(boolean value) {
        this.pegouPrioridade = value;
    }

    @Override
    public String toString() {
        // Inicializa o formatador da data, para dar display corretamente.
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return this.nome + ";" + df.format(this.dataNascimento) + ";" + this.prioridade;
    }
}
