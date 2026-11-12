package br.com.ordensservico;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrdemServicoDAO {

    public void cadastrar(int idCliente) throws SQLException {
        String sqlOrdem = "INSERT INTO ordens_servico (id_cliente, data_abertura) VALUES (?, ?)";
        String sqlProducao = "INSERT INTO em_producao (id_os, operacoes_concluidas, operacoes_abertas) VALUES (?, ?, ?)";

        try (Connection con = Conexao.conectar()) {
            con.setAutoCommit(false);

            try (PreparedStatement ordem = con.prepareStatement(sqlOrdem,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                ordem.setInt(1, idCliente);
                ordem.setDate(2, Date.valueOf(LocalDate.now()));
                ordem.executeUpdate();

                try (ResultSet chaves = ordem.getGeneratedKeys()) {
                    if (!chaves.next()) {
                        throw new SQLException("Nao foi possivel obter o ID da ordem.");
                    }

                    int idOS = chaves.getInt(1);

                    try (PreparedStatement producao = con.prepareStatement(sqlProducao)) {
                        producao.setInt(1, idOS);
                        producao.setString(2, "");
                        producao.setString(3, "Aguardando inicio");
                        producao.executeUpdate();
                    }

                    con.commit();
                    System.out.println("Ordem de servico cadastrada com sucesso! ID: " + idOS);
                }
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void listarTodas() throws SQLException {
        String sql = """
                SELECT os.id_os, c.nome, os.data_abertura,
                       CASE
                           WHEN ep.id_os IS NOT NULL THEN 'EM PRODUCAO'
                           WHEN f.id_os IS NOT NULL THEN 'FINALIZADA'
                           ELSE 'SEM STATUS'
                       END AS status_os,
                       ep.operacoes_concluidas, ep.operacoes_abertas,
                       f.data_finalizacao, f.nota_fiscal
                FROM ordens_servico os
                INNER JOIN clientes c ON c.id_cliente = os.id_cliente
                LEFT JOIN em_producao ep ON ep.id_os = os.id_os
                LEFT JOIN finalizadas f ON f.id_os = os.id_os
                ORDER BY os.id_os DESC
                """;

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n============== TODAS AS ORDENS ==============");
            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                imprimirOrdem(rs);
            }

            if (!encontrou) {
                System.out.println("Nenhuma ordem cadastrada.");
            }
        }
    }

    public void listarEmProducao() throws SQLException {
        String sql = """
                SELECT os.id_os, c.nome, os.data_abertura,
                       ep.operacoes_concluidas, ep.operacoes_abertas
                FROM ordens_servico os
                INNER JOIN clientes c ON c.id_cliente = os.id_cliente
                INNER JOIN em_producao ep ON ep.id_os = os.id_os
                ORDER BY os.id_os DESC
                """;

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n============= ORDENS EM PRODUCAO =============");
            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println("ID da OS: " + rs.getInt("id_os"));
                System.out.println("Cliente: " + rs.getString("nome"));
                System.out.println("Data: " + rs.getDate("data_abertura"));
                System.out.println("Operacoes concluidas: " + rs.getString("operacoes_concluidas"));
                System.out.println("Operacoes abertas: " + rs.getString("operacoes_abertas"));
                System.out.println("----------------------------------------------");
            }

            if (!encontrou) {
                System.out.println("Nao existem ordens em producao.");
            }
        }
    }

    public void listarFinalizadas() throws SQLException {
        String sql = """
                SELECT os.id_os, c.nome, os.data_abertura,
                       f.data_finalizacao, f.nota_fiscal
                FROM ordens_servico os
                INNER JOIN clientes c ON c.id_cliente = os.id_cliente
                INNER JOIN finalizadas f ON f.id_os = os.id_os
                ORDER BY f.data_finalizacao DESC
                """;

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n============= ORDENS FINALIZADAS =============");
            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println("ID da OS: " + rs.getInt("id_os"));
                System.out.println("Cliente: " + rs.getString("nome"));
                System.out.println("Data de abertura: " + rs.getDate("data_abertura"));
                System.out.println("Data de finalizacao: " + rs.getDate("data_finalizacao"));
                System.out.println("Nota fiscal: " + rs.getString("nota_fiscal"));
                System.out.println("----------------------------------------------");
            }

            if (!encontrou) {
                System.out.println("Nao existem ordens finalizadas.");
            }
        }
    }

    public void atualizarOperacoes(int idOS, String concluidas, String abertas) throws SQLException {
        String sql = """
                UPDATE em_producao
                SET operacoes_concluidas = ?, operacoes_abertas = ?
                WHERE id_os = ?
                """;

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, concluidas);
            stmt.setString(2, abertas);
            stmt.setInt(3, idOS);

            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Operacoes atualizadas com sucesso.");
            } else {
                System.out.println("Ordem nao encontrada em producao. Verifique o ID.");
            }
        }
    }

    public void finalizar(int idOS, String notaFiscal) throws SQLException {
        String verificar = "SELECT id_os FROM em_producao WHERE id_os = ?";
        String inserir = "INSERT INTO finalizadas (id_os, data_finalizacao, nota_fiscal) VALUES (?, ?, ?)";
        String excluir = "DELETE FROM em_producao WHERE id_os = ?";

        try (Connection con = Conexao.conectar()) {
            con.setAutoCommit(false);

            try {
                boolean existe;
                try (PreparedStatement stmt = con.prepareStatement(verificar)) {
                    stmt.setInt(1, idOS);
                    try (ResultSet rs = stmt.executeQuery()) {
                        existe = rs.next();
                    }
                }

                if (!existe) {
                    con.rollback();
                    System.out.println("Ordem nao encontrada em producao ou ja finalizada.");
                    return;
                }

                try (PreparedStatement stmt = con.prepareStatement(inserir)) {
                    stmt.setInt(1, idOS);
                    stmt.setDate(2, Date.valueOf(LocalDate.now()));
                    stmt.setString(3, notaFiscal);
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = con.prepareStatement(excluir)) {
                    stmt.setInt(1, idOS);
                    stmt.executeUpdate();
                }

                con.commit();
                System.out.println("Ordem finalizada com sucesso!");
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void pesquisarPorId(int idOS) throws SQLException {
        String sql = """
                SELECT os.id_os, c.nome, os.data_abertura,
                       CASE
                           WHEN ep.id_os IS NOT NULL THEN 'EM PRODUCAO'
                           WHEN f.id_os IS NOT NULL THEN 'FINALIZADA'
                           ELSE 'SEM STATUS'
                       END AS status_os,
                       ep.operacoes_concluidas, ep.operacoes_abertas,
                       f.data_finalizacao, f.nota_fiscal
                FROM ordens_servico os
                INNER JOIN clientes c ON c.id_cliente = os.id_cliente
                LEFT JOIN em_producao ep ON ep.id_os = os.id_os
                LEFT JOIN finalizadas f ON f.id_os = os.id_os
                WHERE os.id_os = ?
                """;

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idOS);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n============= ORDEM ENCONTRADA =============");
                    imprimirOrdem(rs);
                } else {
                    System.out.println("Nenhuma ordem encontrada com esse ID.");
                }
            }
        }
    }


    public void filtrar(String nomeCliente, String dataAbertura, int status) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT os.id_os, c.nome, os.data_abertura,
                       CASE
                           WHEN ep.id_os IS NOT NULL THEN 'EM PRODUCAO'
                           WHEN f.id_os IS NOT NULL THEN 'FINALIZADA'
                           ELSE 'SEM STATUS'
                       END AS status_os,
                       ep.operacoes_concluidas, ep.operacoes_abertas,
                       f.data_finalizacao, f.nota_fiscal
                FROM ordens_servico os
                INNER JOIN clientes c ON c.id_cliente = os.id_cliente
                LEFT JOIN em_producao ep ON ep.id_os = os.id_os
                LEFT JOIN finalizadas f ON f.id_os = os.id_os
                WHERE 1 = 1
                """);

        if (nomeCliente != null && !nomeCliente.isBlank()) {
            sql.append(" AND c.nome LIKE ?");
        }
        if (dataAbertura != null && !dataAbertura.isBlank()) {
            sql.append(" AND os.data_abertura = ?");
        }
        if (status == 1) {
            sql.append(" AND ep.id_os IS NOT NULL");
        } else if (status == 2) {
            sql.append(" AND f.id_os IS NOT NULL");
        }
        sql.append(" ORDER BY os.id_os DESC");

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            int indice = 1;
            if (nomeCliente != null && !nomeCliente.isBlank()) {
                stmt.setString(indice++, "%" + nomeCliente + "%");
            }
            if (dataAbertura != null && !dataAbertura.isBlank()) {
                stmt.setDate(indice++, Date.valueOf(dataAbertura));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n============== RESULTADO DO FILTRO ==============");
                boolean encontrou = false;

                while (rs.next()) {
                    encontrou = true;
                    imprimirOrdem(rs);
                }

                if (!encontrou) {
                    System.out.println("Nenhuma ordem encontrada com esses filtros.");
                }
            }
        }
    }

    private void imprimirOrdem(ResultSet rs) throws SQLException {
        System.out.println("ID da OS: " + rs.getInt("id_os"));
        System.out.println("Cliente: " + rs.getString("nome"));
        System.out.println("Data de abertura: " + rs.getDate("data_abertura"));
        System.out.println("Status: " + rs.getString("status_os"));

        String concluidas = rs.getString("operacoes_concluidas");
        String abertas = rs.getString("operacoes_abertas");
        Date dataFinalizacao = rs.getDate("data_finalizacao");
        String notaFiscal = rs.getString("nota_fiscal");

        if (concluidas != null || abertas != null) {
            System.out.println("Operacoes concluidas: " + concluidas);
            System.out.println("Operacoes abertas: " + abertas);
        }
        if (dataFinalizacao != null) {
            System.out.println("Data de finalizacao: " + dataFinalizacao);
            System.out.println("Nota fiscal: " + notaFiscal);
        }
        System.out.println("---------------------------------------------");
    }
}
