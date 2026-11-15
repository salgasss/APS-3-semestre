package br.com.ordensservico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final OrdemServicoDAO ordemDAO = new OrdemServicoDAO();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     SISTEMA DE ORDENS DE SERVICO");
        System.out.println("========================================");

        if (!testarConexao()) {
            System.out.println("\nNao foi possivel conectar ao banco de dados.");
            System.out.println("Confira se o MySQL esta ligado e se os dados de Conexao.java estao corretos.");
            scanner.close();
            return;
        }

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opcao: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarCliente();
                    case 2 -> cadastrarOrdem();
                    case 3 -> ordemDAO.listarTodas();
                    case 4 -> ordemDAO.listarEmProducao();
                    case 5 -> ordemDAO.listarFinalizadas();
                    case 6 -> atualizarOperacoes();
                    case 7 -> finalizarOrdem();
                    case 8 -> pesquisarOrdem();
                    case 9 -> clienteDAO.listar();
                    case 10 -> filtrarOrdens();
                    case 0 -> System.out.println("Sistema encerrado. Ate mais!");
                    default -> System.out.println("Opcao invalida. Tente novamente.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
            }

            if (opcao != 0) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n========================================");
        System.out.println("       SISTEMA DE ORDENS DE SERVICO");
        System.out.println("========================================");
        System.out.println("1 - Cadastrar cliente");
        System.out.println("2 - Cadastrar ordem de servico");
        System.out.println("3 - Consultar todas as ordens");
        System.out.println("4 - Consultar ordens em producao");
        System.out.println("5 - Consultar ordens finalizadas");
        System.out.println("6 - Atualizar operacoes da ordem");
        System.out.println("7 - Finalizar ordem de servico");
        System.out.println("8 - Pesquisar ordem por ID");
        System.out.println("9 - Listar clientes");
        System.out.println("10 - Filtrar ordens por cliente, data e status");
        System.out.println("0 - Sair");
        System.out.println("========================================");
    }

    private static boolean testarConexao() {
        try (Connection con = Conexao.conectar()) {
            System.out.println("Conexao com o MySQL realizada com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("Detalhe da conexao: " + e.getMessage());
            return false;
        }
    }

    private static void cadastrarCliente() throws SQLException {
        System.out.println("\n========== CADASTRO DE CLIENTE ==========");
        String nome = lerTextoObrigatorio("Nome: ");
        String telefone = lerTexto("Telefone: ");
        String endereco = lerTexto("Endereco: ");

        clienteDAO.cadastrar(nome, telefone, endereco);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void cadastrarOrdem() throws SQLException {
        System.out.println("\n======= CADASTRO DE ORDEM DE SERVICO =======");
        clienteDAO.listar();
        int idCliente = lerInteiro("Digite o ID do cliente: ");

        if (!clienteExiste(idCliente)) {
            System.out.println("Cliente nao encontrado. Cadastre ou informe um ID valido.");
            return;
        }

        ordemDAO.cadastrar(idCliente);
    }

    private static boolean clienteExiste(int idCliente) throws SQLException {
        String sql = "SELECT id_cliente FROM clientes WHERE id_cliente = ?";
        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void atualizarOperacoes() throws SQLException {
        System.out.println("\n========== ATUALIZAR OPERACOES ==========");
        int idOS = lerInteiro("ID da ordem de servico: ");
        String concluidas = lerTexto("Operacoes concluidas: ");
        String abertas = lerTexto("Operacoes abertas: ");
        ordemDAO.atualizarOperacoes(idOS, concluidas, abertas);
    }

    private static void finalizarOrdem() throws SQLException {
        System.out.println("\n========== FINALIZAR ORDEM ==========");
        int idOS = lerInteiro("ID da ordem de servico: ");
        String notaFiscal = lerTextoObrigatorio("Numero da nota fiscal: ");
        ordemDAO.finalizar(idOS, notaFiscal);
    }

    private static void pesquisarOrdem() throws SQLException {
        System.out.println("\n========== PESQUISAR ORDEM ==========");
        int idOS = lerInteiro("Digite o ID da ordem: ");
        ordemDAO.pesquisarPorId(idOS);
    }


    private static void filtrarOrdens() throws SQLException {
        System.out.println("\n========== FILTRAR ORDENS ==========");
        System.out.println("Deixe o campo de cliente ou data vazio para ignorar esse filtro.");
        String nome = lerTexto("Nome (ou parte do nome) do cliente: ");
        String data = lerTexto("Data de abertura (AAAA-MM-DD) ou ENTER: ");

        if (!data.isBlank()) {
            try {
                java.time.LocalDate.parse(data);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Data invalida. Use o formato AAAA-MM-DD.");
                return;
            }
        }

        System.out.println("Status: 0 - Todos | 1 - Em producao | 2 - Finalizadas");
        int status = lerInteiro("Escolha o status: ");
        if (status < 0 || status > 2) {
            System.out.println("Status invalido.");
            return;
        }

        ordemDAO.filtrar(nome, data, status);
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static String lerTextoObrigatorio(String mensagem) {
        while (true) {
            String valor = lerTexto(mensagem);
            if (!valor.isBlank()) {
                return valor;
            }
            System.out.println("Esse campo nao pode ficar vazio.");
        }
    }
}
