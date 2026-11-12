package br.com.ordensservico;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // Altere usuario e senha de acordo com a configuracao do seu MySQL.
    private static final String URL =
            "jdbc:mysql://localhost:3306/ordens_servico?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String SENHA = "SUA_SENHA_AQUI";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
