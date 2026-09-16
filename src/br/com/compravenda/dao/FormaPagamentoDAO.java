package br.com.compravenda.dao;

import br.com.compravenda.model.FormaPagamento;
import br.com.compravenda.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormaPagamentoDAO {

    public void inserir(FormaPagamento forma) {
        String sql = "INSERT INTO formapagto (fpg_nome, fpg_ativo) VALUES (?, ?)";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, forma.getNome());
            stmt.setString(2, forma.getAtivo());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) forma.setCodigo(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir forma de pagamento: " + e.getMessage(), e);
        }
    }

    public void atualizar(FormaPagamento forma) {
        String sql = "UPDATE formapagto SET fpg_nome = ?, fpg_ativo = ? WHERE fpg_codigo = ?";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, forma.getNome());
            stmt.setString(2, forma.getAtivo());
            stmt.setInt(3, forma.getCodigo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar forma de pagamento: " + e.getMessage(), e);
        }
    }

    public void excluir(int codigo) {
        String sql = "DELETE FROM formapagto WHERE fpg_codigo = ?";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir forma de pagamento: " + e.getMessage(), e);
        }
    }

    public List<FormaPagamento> listarTodos() {
        List<FormaPagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM formapagto ORDER BY fpg_nome";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar formas de pagamento: " + e.getMessage(), e);
        }
        return lista;
    }

    private FormaPagamento mapear(ResultSet rs) throws SQLException {
        FormaPagamento f = new FormaPagamento();
        f.setCodigo(rs.getInt("fpg_codigo"));
        f.setNome(rs.getString("fpg_nome"));
        f.setAtivo(rs.getString("fpg_ativo"));
        return f;
    }
}
