package com.trabalhoed1;

public class utils {
    // Função para obter um valor inteiro aleatorio com minimo e máximo.
    // Obtido em: https://stackoverflow.com/questions/2444019/
    public static int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }
}
