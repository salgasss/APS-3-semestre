package br.com.ordensservico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public void cadastrar(String nome, String telefone, String endereco) throws SQLException {
        String sql = "INSERT INTO clientes (nome, telefone, endereco) VALUES (?, ?, ?)";

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, endereco);
            stmt.executeUpdate();
        }
    }

    public void listar() throws SQLException {
        String sql = "SELECT id_cliente, nome, telefone, endereco FROM clientes ORDER BY nome";

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n========== CLIENTES CADASTRADOS ==========");
            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println("ID: " + rs.getInt("id_cliente"));
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("Telefone: " + valorOuTraco(rs.getString("telefone")));
                System.out.println("Endereco: " + valorOuTraco(rs.getString("endereco")));
                System.out.println("------------------------------------------");
            }

            if (!encontrou) {
                System.out.println("Nenhum cliente cadastrado.");
            }
        }
    }

    private String valorOuTraco(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }
}
