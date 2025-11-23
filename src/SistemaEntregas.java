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

        System.out.println("Bem-vindo ao Sistema de Distribuição de Cargas para Transporte");

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
        System.out.println("\n=== MENU ===");
        System.out.println("1. Carregar cargas de arquivo CSV");
        System.out.println("2. Inserir nova carga");
        System.out.println("3. Exibir carga de maior prioridade");
        System.out.println("4. Remover carga de maior prioridade");
        System.out.println("5. Exibir todas as cargas ordenadas por prioridade");
        System.out.println("6. Sair");
        System.out.print("Digite uma opção: ");
    }

    private static void importarEntregasCSV() {
        System.out.print("Insira o nome do arquivo CSV (exemplo: ArquivoTrabalho.csv): ");
        String caminhoArquivo = entrada.nextLine().trim();
        int linhasProcessadas = 0;
        int linhasIncorretas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }
                
                try {
                    String[] dados = linha.split(",");
                    if (dados.length == 5) {
                        int id = Integer.parseInt(dados[0].trim());
                        int tipo = Integer.parseInt(dados[1].trim());
                        int urgencia = Integer.parseInt(dados[2].trim());
                        int peso = Integer.parseInt(dados[3].trim());
                        String descricao = dados[4].trim();

                        if (urgencia < 1 || urgencia > 3 || peso < 0 || id < 0) {
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
                    linhasIncorretas++;
                }
            }
            System.out.println("Sucesso: " + linhasProcessadas + " cargas importadas.");
            if (linhasIncorretas > 0) {
                System.out.println("Aviso: " + linhasIncorretas + " linhas incorretas foram desconsideradas.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo '" + caminhoArquivo + "' não localizado.");
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
            System.out.println("Carga adicionada com sucesso! Prioridade calculada: " + e.nivelPrioridade);

        } catch (NumberFormatException e) {
            System.out.println("Erro: ID, tipo, urgência e peso precisam ser valores numéricos.");
        }
    }

    private static void mostrarMaisUrgente() {
        if (fila.eVazia()) {
            System.out.println("A fila está vazia. Nenhuma carga para mostrar.");
            return;
        }

        Entrega topo = fila.verTopo();
        System.out.println("\n=== Carga de Maior Prioridade ===");
        imprimirCabecalho();
        System.out.println(topo);
        System.out.println("-----------------------------------");
    }

    private static void eliminarMaisUrgente() {
        if (fila.eVazia()) {
            System.out.println("A fila está vazia. Nenhuma carga para remover.");
            return;
        }

        Entrega removida = fila.retirarMax();
        System.out.println("\n=== Carga Removida ===");
        imprimirCabecalho();
        System.out.println(removida);
        System.out.println("-----------------------------------");
    }

    private static void listarEntregasOrdenadas() {
        int tamanho = fila.obterTam();
        if (tamanho == 0) {
            System.out.println("A fila está vazia.");
            return;
        }

        Entrega[] ordenadas = new Entrega[tamanho];

        System.out.println("\n=== Cargas Ordenadas por Prioridade (Maior para Menor) ===");
        imprimirCabecalho();

        for (int i = 0; i < tamanho; i++) {
            ordenadas[i] = fila.retirarMax();
            System.out.println(ordenadas[i]);
        }

        System.out.println("-----------------------------------");
        System.out.println("\n(Restaurando fila...)");
        for (int i = 0; i < tamanho; i++) {
            fila.inserir(ordenadas[i]);
        }
        System.out.println("Listagem finalizada. A fila foi restaurada.");
    }

    private static void imprimirCabecalho() {
        System.out.println("| ID  | Tipo | Urgência | Peso | Prioridade | Descrição           |");
        System.out.println("|-----|------|----------|------|------------|---------------------|");
    }
}
