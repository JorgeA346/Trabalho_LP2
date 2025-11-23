import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SistemaEntregas {

    private static FilaPrioritaria fila;
    private static Scanner entrada;

    public static void main(String[] args) {
        fila = new FilaPrioritaria(10);
        entrada = new Scanner(System.in);

        System.out.println("Ola, seja bem-vindo ao Sistema de Entregas");

        int escolha = 0;
        while (escolha != 6) {
            mostrarOpcoes();
            try {
                escolha = Integer.parseInt(entrada.nextLine());

                switch (escolha) {
                    case 1 -> importarEntregasCSV();
                    case 2 -> adicionarManualmente();
                    case 3 -> mostrarMaisUrgente();
                    case 4 -> eliminarMaisUrgente();
                    case 5 -> listarEntregasOrdenadas();
                    case 6 -> System.out.println("Finalizando o programa...");
                    default -> System.out.println("Tente novamente. Opcao invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Valor inserido não é válido. Insira um número.");
                escolha = 0;
            }
            System.out.println("===================================================");
        }

        entrada.close();
    }

    private static void mostrarOpcoes() {
        System.out.println("\n=== OPCOES ===");
        System.out.println("1. Importar entregas de arquivo .CSV");
        System.out.println("2. Adicionar manualmente uma nova entrega");
        System.out.println("3. Mostrar entrega de maior urgencia");
        System.out.println("4. Eliminar entrega de maior urgencia");
        System.out.println("5. Listar todas as entregas ordenadas por urgencia");
        System.out.println("6. Sair");
        System.out.print("Digite uma das opcoes a cima: ");
    }

    private static void importarEntregasCSV() {
        System.out.print("Insira o nome do arquivo (exemplo: cargas.csv): ");
        String caminhoArquivo = entrada.nextLine();
        int linhasProcessadas = 0;
        int linhasIncorretas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                try {
                    String[] dados = linha.split(",");
                    if (dados.length == 5) {
                        int id = Integer.parseInt(dados[0].trim());
                        int tipo = Integer.parseInt(dados[1].trim());
                        int urgencia = Integer.parseInt(dados[2].trim());
                        int peso = Integer.parseInt(dados[3].trim());
                        String descricao = dados[4].trim();

                        if (urgencia < 1 || urgencia > 3 || peso < 0) {
                            linhasIncorretas++;
                            continue;
                        }

                        Entrega e = new Entrega(id, tipo, urgencia, peso, descricao);
                        fila.inserir(e);
                        linhasProcessadas++;
                    } else {
                        linhasIncorretas++;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Aviso: Linha com formato inadequado no CSV será desconsiderada.");
                    linhasIncorretas++;
                }
            }
            System.out.println("Sucesso: " + linhasProcessadas + " entregas importadas.");
            if (linhasIncorretas > 0) {
                System.out.println("Aviso: " + linhasIncorretas + " linhas incorretas foram desconsideradas.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo '" + caminhoArquivo + "' nao localizado.");
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo: " + e.getMessage());
        }
    }

    private static void adicionarManualmente() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(entrada.nextLine());
            System.out.print("Tipo (exemplo: 9 para Medicamentos): ");
            int tipo = Integer.parseInt(entrada.nextLine());
            System.out.print("Urgência (1=baixa, 2=média, 3=alta): ");
            int urgencia = Integer.parseInt(entrada.nextLine());
            System.out.print("Peso (kg): ");
            int peso = Integer.parseInt(entrada.nextLine());
            System.out.print("Descrição: ");
            String descricao = entrada.nextLine();

            if (urgencia < 1 || urgencia > 3 || peso < 0 || id < 0) {
                System.out.println("Erro: Informações inválidas (exemplo: urgência 1-3, peso positivo).");
                return;
            }

            Entrega e = new Entrega(id, tipo, urgencia, peso, descricao);
            fila.inserir(e);
            System.out.println("Entrega adicionada com sucesso! Prioridade calculada: " + e.nivelPrioridade);

        } catch (NumberFormatException e) {
            System.out.println("Erro: ID, tipo, urgência e peso precisam ser valores numéricos.");
        }
    }

    private static void mostrarMaisUrgente() {
        if (fila.eVazia()) {
            System.out.println("A fila está vazia. Nenhuma entrega para mostrar.");
            return;
        }

        Entrega topo = fila.verTopo();
        System.out.println("Entrega de maior urgência:");
        System.out.println(topo);
    }

    private static void eliminarMaisUrgente() {
        if (fila.eVazia()) {
            System.out.println("A fila está vazia. Nenhuma entrega para eliminar.");
            return;
        }

        Entrega eliminada = fila.retirarMax();
        System.out.println("Entrega eliminada:");
        System.out.println(eliminada);
    }

    private static void listarEntregasOrdenadas() {
        int tamanho = fila.obterTam();
        if (tamanho == 0) {
            System.out.println("A fila está vazia.");
            return;
        }

        Entrega[] ordenadas = new Entrega[tamanho];

        System.out.println("=== Entregas Ordenadas por Urgência (Maior para Menor) ===");

        for (int i = 0; i < tamanho; i++) {
            ordenadas[i] = fila.retirarMax();
            System.out.println(ordenadas[i]);
        }

        System.out.println("\n(Restaurando fila...)");
        for (int i = 0; i < tamanho; i++) {
            fila.inserir(ordenadas[i]);
        }
        System.out.println("Listagem finalizada. A fila foi restaurada.");
    }
}
