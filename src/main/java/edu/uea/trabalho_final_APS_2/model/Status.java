package edu.uea.trabalho_final_APS_2.model;

public enum Status {

    DISPONIVEL(0),
    EMPRESTADO(1);

    private final int valor;

    Status(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}