package com.trabalhoed1;

/*
 *   Tendo como base um arquivo (clientes.csv) contendo uma lista de nomes, datas de nascimento e um
 *   campo chamado prioridade, com a seguinte lista de prioridades em ordem:
 *   1: gestante
 *   2: PCDs
 *   3: acima de 60 anos
 *   4: Comorbidades
 *   5: Criança de colo
 *   -1: pessoas sem prioridade.
 *
 *   Leia o arquivo, classifique-o de acordo com a data de nascimento. Crie 2 filas de atendimento, uma fila
 *   que atende apenas pessoas sem prioridades, e outra que pode atender pessoas com prioridade ou não.
 *   Submeta os clientes do arquivo a suas duas filas de prioridade.
 *
 *   • Considere a implementação de filas com lista ligada;
 *   • Para uma das filas crie um método chamado fura_fila, que poderá ser acessado apenas por pessoas que
 *   tenham prioridade.
 *   • O detalhe é que seu procedimento (o fura fila) não pode movimentar os outros itens da fila.
 *   • Ao final o seu sistema deverá gravar um arquivo relatório com a ordem que foram atendidos as pessoas
 *   nas duas filas que você criou
 */

import com.trabalhoed1.models.Contador;
import com.trabalhoed1.models.Pessoa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

////////////////////////////////////////////////////////////////
// TODO: Implementar fura fila (parcialmente); Gerar relatório dos clientes atendidos (parcial).
public class MainApp {
    public static final String CSV_CLIENTES = "clientes.csv";
    public static final String CSV_RELATORIO_COMUM = "relatorio_filacomum.csv";
    public static final String CSV_RELATORIO_PRIORITARIO = "relatorio_filaprioritaria.csv";

    public static final int TEMPORIZADOR_COMUM = 300; // 0.3 seg
    public static final int TEMPORIZADOR_PRIORITARIO = 1000; // 1 seg

    public static void main(String[] args)
            throws IOException, NumberFormatException, ParseException, InterruptedException {
        // Carrega o arquivo "clientes.csv"
        final FileReader clientesReader = new FileReader(new File(CSV_CLIENTES).getAbsolutePath());
        final BufferedReader clientesBuffer = new BufferedReader(clientesReader);

        // Abre o arquivo e faz a leitura, incluindo numa ArrayList temporária.
        String linha = null;
        ArrayList<String> temp = new ArrayList<>();
        while ((linha = clientesBuffer.readLine()) != null) {
            temp.add(linha);
        }
        clientesBuffer.close();

        // Remove o primeiro indice, do cabeçalho da lista.
        temp.remove(0);

        // Cria uma ArrayList da classe Pessoa.
        ArrayList<Pessoa> pessoas = new ArrayList<>();

        // Inicializa o formatador de data, a fim de converter a data (String) do
        // arquivo
        // "csv" para o formato correto.
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Cria os objetos da classe Pessoa e insere na ArrayList com as informações
        // coletadas anteriormente.
        for (String dados : temp) {
            String[] partes = dados.split(";");

            String nome = partes[0];
            Date dataNascimento = df.parse(partes[1]);
            int prioridade = Integer.parseInt(partes[2]);
            pessoas.add(new Pessoa(nome, dataNascimento, prioridade));
        }

        // Faz o sort da ArrayList das pessoas conforme a data de nascimento
        // utilizando uma funcionalidade nativa do java (Collections).
        Collections.sort(pessoas, new Comparator<Pessoa>() {
            public int compare(Pessoa p1, Pessoa p2) {
                return p1.getDataNascimento().compareTo(p2.getDataNascimento());
            }
        });

        // TODO: Fazer duas filas de atendimento;
        ArrayList<Pessoa> relatorioComum = new ArrayList<>();
        ArrayList<Pessoa> relatorioPrioritario = new ArrayList<>();

        LinkList filaComum = new LinkList();
        LinkList filaPrioritaria = new LinkList();

        // Inicializa um contador de modo a percorrer toda a ArrayList das pessoas.
        Contador contator = new Contador();

        // Inicializa o temporizador que será utilizado nos atendimentos.
        Timer timer = new Timer();

        // Insere as pessoas na fila.
        // --
        // Nota: o loop continua a rodar enquanto aguarda o tempo de atendimento das
        // pessoas antes de remover elas das filas e inserir no relatorio.
        while (true) {
            // Não tem mais pessoas para serem atendidas, para o loop.
            if (pessoas.isEmpty() || pessoas.size() <= 0) {
                break;
            }

            // Loop já incrementou até o indice final ou decrementou para menos de 0, reinicia.
            if ((contator.valor() >= pessoas.size()) || contator.valor() < 0) {
                contator.reset();
            }

            // Hotfix: garante que os indices estão corretos devido ao atraso do temporizador e uso de threads.
            // O ideal seria utilizar algum tipo de sincronismo como semaforo, etc.
            int valor = contator.valor();
            if (valor == 0 && pessoas.size() <= 0) {
                break;
            }

            // Hotfix: verifica se o objeto é nulo devido ao atraso do temporizador e uso de threads.
            // O ideal seria utilizar algum tipo de sincronismo como semaforo, etc.
            Pessoa p = pessoas.get(valor);
            if (p == null) {
                contator.reset();

                // TODO: Bug devido a falta de sincronismo dos threads
                // --
                // Ao não "dormir", a array continua retornando "false" no metodo isEmpty,
                // apesar do indice "0" da ArrayList já ser nulo.
                // Portanto, "dorme" por 4 ms antes de reiniciar o loop.
                Thread.sleep(4);
                //System.out.println("loop = " + pessoas.isEmpty());
                continue;
            }

            // Incrementa o contador.
            contator.increment();

            // Pessoa já está sendo atendida.
            if (p.emAtendimento()) {
                // "Dorme" por 4 ms antes de reiniciar o loop, devido aos atrasos do
                // temporizador e threads.
                Thread.sleep(4);
                //System.out.println(" [" + valor + "] Aguardando Atendimento= " + p.toString());
                continue;
            }

            // Define que a pessoa já está em atendimento, a inserção nas filas adequadas e
            // tempo de atendimento será feito em seguida.
            p.setEmAtendimento(true);

            // Simula a chegada da pessoa aleatoriamente.
            Thread.sleep(utils.random_int(50, 200)); // 0.05~0.2 seg

            // Pessoa tem prioridade.
            if (p.temPrioridade()) {
                // Verifica se a fila prioritaria está vazia.
                if (filaPrioritaria.isEmpty()) {
                    filaPrioritaria.insertFirst(p);
                }
                // Já tem pessoas na fila prioritaria.
                else {
                    // Verifica se o primeiro da fila prioritaria, tem de fato prioridade ou se ele é um fura fila.
                    // Caso contrário, insere o novo fura fila atrás do atual sendo atendido.
                    // Possíveis problemas de "roubo" de lugar de atendimento é resolvido indiretamente pelo uso de TimeTask.
                	Pessoa atualFila = filaPrioritaria.peekFirst();
                    if (!atualFila.temPrioridade() && !filaPrioritaria.isFuraFila(atualFila)) {
                    	System.out.println("[Fura Fila]" + p.toString() + " utilizou o furou a fila e entrou atras de " + atualFila.toString());
                        filaPrioritaria.furaFila(p);
                    } else {
                        // Insere atrás do atual primeiro da fila prioritaria.
                        filaPrioritaria.insertLast(p);
                    }
                }

                // Temporizador da pessoa com prioridade.
                timer.schedule(new TimerTask() {
                    public void run() {
                        // Pessoa foi atendida, remove ela da fila e insere no relatório.
                        if (filaPrioritaria.isFuraFila(p)) {
                            // Remove a pessoa atendida do fura fila.
                            filaPrioritaria.furaFila(null);
                            System.out.println("Pessoa foi atendida Prioridade [Fura Fila]= " + p.toString());
                        } else {
                            System.out.println("Pessoa foi atendida [Prioridade]= " + p.toString());
                        }

                        relatorioPrioritario.add(p);
                        filaPrioritaria.deleteKey(p);
                        p.setEmAtendimento(false);
                        pessoas.remove(p);
                        contator.decrement();
                        cancel();
                    }
                }, TEMPORIZADOR_PRIORITARIO, TEMPORIZADOR_PRIORITARIO*2);
            }
            // Pessoa não tem prioridade.
            else {
                // Fila prioritaria está vazia, pode entrar mesmo não tendo prioridade caso
                // esteja.
                if (filaPrioritaria.isEmpty()) {
                    p.setPegouPrioridade(true);
                    System.out.println("PegouPrioridade= " + p.toString());
                    filaPrioritaria.insertFirst(p);
                }
                // Fila prioritaria já possui pessoas, então insere na fila comum.
                else {
                    // Insere no final da fila comum, mas caso ele seja o primeiro, já é atendido na
                    // hora.
                    filaComum.insertLast(p);
                }

                // Temporizador da pessoa comum.
                timer.schedule(new TimerTask() {
                    public void run() {
                        // Pessoa foi atendida, remove ela da fila e insere no relatório.
                        if (p.pegouPrioridade()) {
                            System.out.println("Pessoa foi atendida [Prioridade]= " + p.toString());
                            relatorioPrioritario.add(p);
                            filaPrioritaria.deleteKey(p);
                        } else {
                            System.out.println("Pessoa foi atendida= " + p.toString());
                            relatorioComum.add(p);
                            filaComum.deleteKey(p);
                        }
                        p.setEmAtendimento(false);
                        pessoas.remove(p);
                        contator.decrement();
                        cancel();
                    }
                }, TEMPORIZADOR_COMUM, TEMPORIZADOR_COMUM*2);
            }
        }

        // Testa as filas para ver se ainda existe alguma pessoa pendente.
        System.out.println("\nFila prioridade:");
        filaPrioritaria.displayForward();

        System.out.println("\nFila comum:");
        filaComum.displayForward();

        // Informa que o atendimento terminou e finaliza o temporizador.
        System.out.println("\nATENDIMENTO TERMINOU!");
        timer.cancel();

        // Gera os relatorios de atendimento em um arquivo externo.
        gerarRelatorio(relatorioPrioritario, CSV_RELATORIO_PRIORITARIO);
        gerarRelatorio(relatorioComum, CSV_RELATORIO_COMUM);
    }

    // Metodo para exportar a lista em um arquivo externo, a fim de gerar o
    // relatorio.
    public static void gerarRelatorio(ArrayList<Pessoa> lista, String outputPath) throws IOException {
        // Inicializa e cria o arquivo no destino especificado.
        final FileWriter relatorioReader = new FileWriter(new File(outputPath).getAbsolutePath());

        // Inicializa o buffer e abre o arquivo.
        final BufferedWriter relatorioBuffer = new BufferedWriter(relatorioReader);

        // Insere o cabeçalho no arquivo.
        relatorioBuffer.write("nome_cliente;data_nascimento;Prioridade" + System.lineSeparator());

        // Insere os resultados da lista no arquivo enquanto houver.
        while (!lista.isEmpty()) {
            Pessoa temp = lista.remove(0);
            relatorioBuffer.write(temp.toString() + System.lineSeparator());
        }

        // Da um flush no fluxo de dados para garantir que tudo foi salvo.
        relatorioBuffer.flush();

        // Fecha o buffer do arquivo.
        relatorioBuffer.close();
    }
}
