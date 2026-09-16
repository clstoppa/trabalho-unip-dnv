package br.com.compravenda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões com o banco PostgreSQL.
 * Ajuste HOST, PORTA, BANCO, USUARIO e SENHA conforme o seu ambiente.
 */
public class ConexaoFactory {

    private static final String HOST = "localhost";
    private static final String PORTA = "5432";
    private static final String BANCO = "faculdade";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "ninjasamurai123";

    private static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PORTA + "/" + BANCO;

    public static Connection getConexao() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "Driver do PostgreSQL não encontrado. Adicione o postgresql-42.x.x.jar ao classpath do projeto.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    public static void fechar(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
