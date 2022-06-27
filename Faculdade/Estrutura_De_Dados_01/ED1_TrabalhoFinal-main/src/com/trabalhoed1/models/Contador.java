package com.trabalhoed1.models;

// Classe contador, utilizada no loop de atendimento dos clientes.
// Foi necessário para burlar uma limitação do java que não permite a manipulação de primitivos na callback da TimeTask.
public class Contador {
    private int contador;

    public Contador() {
        this.contador = 0;
    }

    public int valor() {
        return contador;
    }

    public void increment() {
        this.contador++;
    }

    public void decrement() {
        // Verifica se o contador já atingiu o zero.
        this.contador--;
        if (this.contador <= 0) {
            this.contador = 0;
        }
    }

    public void reset() {
        this.contador = 0;
    }
}
