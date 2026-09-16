package br.com.compravenda.dao;

import br.com.compravenda.model.Usuario;
import br.com.compravenda.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (usu_nome, usu_login, usu_senha, usu_cadastro, usu_ativo) "
                + "VALUES (?, ?, ?, CURRENT_DATE, ?)";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getAtivo());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) usuario.setCodigo(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário: " + e.getMessage(), e);
        }
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET usu_nome = ?, usu_login = ?, usu_senha = ?, usu_ativo = ? "
                + "WHERE usu_codigo = ?";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getAtivo());
            stmt.setInt(5, usuario.getCodigo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public void excluir(int codigo) {
        String sql = "DELETE FROM usuario WHERE usu_codigo = ?";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage(), e);
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY usu_nome";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Usado pela tela de login. Retorna null se login/senha não conferem. */
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuario WHERE usu_login = ? AND usu_senha = ? AND usu_ativo = 'S'";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
        return null;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setCodigo(rs.getInt("usu_codigo"));
        u.setNome(rs.getString("usu_nome"));
        u.setLogin(rs.getString("usu_login"));
        u.setSenha(rs.getString("usu_senha"));
        u.setCadastro(rs.getDate("usu_cadastro"));
        u.setAtivo(rs.getString("usu_ativo"));
        return u;
    }
}
