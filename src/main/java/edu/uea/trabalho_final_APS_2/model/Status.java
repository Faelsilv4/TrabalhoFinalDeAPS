package edu.uea.trabalho_final_APS_2.model;

public enum Status {
    
    // Conforme o diagrama:
    DISPONIVEL(0), 
    EMPRESTADO(1), 
    RESERVADO(2);

    private final int valor;

    Status(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}