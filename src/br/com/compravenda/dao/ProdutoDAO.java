package br.com.compravenda.dao;

import br.com.compravenda.model.Produto;
import br.com.compravenda.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (pro_nome, pro_estoque, pro_unidade, pro_preco, pro_custo, "
                + "pro_atacado, pro_min, pro_max, pro_embalagem, pro_peso, pro_cadastro, pro_obs, pro_ativo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, ?)";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(stmt, produto);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) produto.setCodigo(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto: " + e.getMessage(), e);
        }
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET pro_nome=?, pro_estoque=?, pro_unidade=?, pro_preco=?, pro_custo=?, "
                + "pro_atacado=?, pro_min=?, pro_max=?, pro_embalagem=?, pro_peso=?, pro_obs=?, pro_ativo=? "
                + "WHERE pro_codigo=?";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getEstoque());
            stmt.setString(3, produto.getUnidade());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setBigDecimal(5, produto.getCusto());
            stmt.setBigDecimal(6, produto.getPrecoAtacado());
            stmt.setBigDecimal(7, produto.getEstoqueMinimo());
            stmt.setBigDecimal(8, produto.getEstoqueMaximo());
            stmt.setBigDecimal(9, produto.getEmbalagem());
            stmt.setBigDecimal(10, produto.getPeso());
            stmt.setString(11, produto.getObs());
            stmt.setString(12, produto.getAtivo());
            stmt.setInt(13, produto.getCodigo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    public void excluir(int codigo) {
        String sql = "DELETE FROM produto WHERE pro_codigo = ?";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage(), e);
        }
    }

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY pro_nome";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Produto buscarPorCodigo(int codigo) {
        String sql = "SELECT * FROM produto WHERE pro_codigo = ?";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
        }
        return null;
    }

    private void preencher(PreparedStatement stmt, Produto p) throws SQLException {
        stmt.setString(1, p.getNome());
        stmt.setBigDecimal(2, p.getEstoque());
        stmt.setString(3, p.getUnidade());
        stmt.setBigDecimal(4, p.getPreco());
        stmt.setBigDecimal(5, p.getCusto());
        stmt.setBigDecimal(6, p.getPrecoAtacado());
        stmt.setBigDecimal(7, p.getEstoqueMinimo());
        stmt.setBigDecimal(8, p.getEstoqueMaximo());
        stmt.setBigDecimal(9, p.getEmbalagem());
        stmt.setBigDecimal(10, p.getPeso());
        stmt.setString(11, p.getObs());
        stmt.setString(12, p.getAtivo());
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setCodigo(rs.getInt("pro_codigo"));
        p.setNome(rs.getString("pro_nome"));
        p.setEstoque(rs.getBigDecimal("pro_estoque"));
        p.setUnidade(rs.getString("pro_unidade"));
        p.setPreco(rs.getBigDecimal("pro_preco"));
        p.setCusto(rs.getBigDecimal("pro_custo"));
        p.setPrecoAtacado(rs.getBigDecimal("pro_atacado"));
        p.setEstoqueMinimo(rs.getBigDecimal("pro_min"));
        p.setEstoqueMaximo(rs.getBigDecimal("pro_max"));
        p.setEmbalagem(rs.getBigDecimal("pro_embalagem"));
        p.setPeso(rs.getBigDecimal("pro_peso"));
        p.setCadastro(rs.getDate("pro_cadastro"));
        p.setObs(rs.getString("pro_obs"));
        p.setAtivo(rs.getString("pro_ativo"));
        return p;
    }
}
