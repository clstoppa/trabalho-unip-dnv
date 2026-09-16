package br.com.compravenda.dao;

import br.com.compravenda.model.*;
import br.com.compravenda.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    public void inserir(Compra compra) {
        String sqlCompra = "INSERT INTO compra (usu_codigo, for_codigo, cpr_emissao, cpr_valor, cpr_desconto, "
                + "cpr_total, cpr_dtentrada, cpr_obs) VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO compra_produto (cpr_codigo, pro_codigo, cpr_qtde, cpr_preco, cpr_desconto, cpr_total) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            int cprCodigo;
            try (PreparedStatement stmt = con.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, compra.getUsuario().getCodigo());
                stmt.setInt(2, compra.getFornecedor().getCodigo());
                stmt.setBigDecimal(3, compra.getValor());
                stmt.setBigDecimal(4, compra.getDesconto());
                stmt.setBigDecimal(5, compra.getTotal());
                stmt.setDate(6, compra.getDataEntrada());
                stmt.setString(7, compra.getObs());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    cprCodigo = rs.getInt(1);
                }
            }
            compra.setCodigo(cprCodigo);

            try (PreparedStatement stmt = con.prepareStatement(sqlItem)) {
                for (CompraProduto item : compra.getProdutos()) {
                    stmt.setInt(1, cprCodigo);
                    stmt.setInt(2, item.getProduto().getCodigo());
                    stmt.setBigDecimal(3, item.getQuantidade());
                    stmt.setBigDecimal(4, item.getPreco());
                    stmt.setBigDecimal(5, item.getDesconto());
                    stmt.setBigDecimal(6, item.getTotal());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir compra: " + e.getMessage(), e);
        }
    }

    public void excluir(int codigo) {
        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM compra_produto WHERE cpr_codigo = ?")) {
                stmt.setInt(1, codigo);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM compra WHERE cpr_codigo = ?")) {
                stmt.setInt(1, codigo);
                stmt.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir compra: " + e.getMessage(), e);
        }
    }

    public List<Compra> listarTodas() {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.usu_nome, p.pes_nome AS for_nome FROM compra c "
                + "JOIN usuario u ON u.usu_codigo = c.usu_codigo "
                + "JOIN fornecedor f ON f.for_codigo = c.for_codigo "
                + "JOIN pessoa p ON p.pes_codigo = f.pes_codigo "
                + "ORDER BY c.cpr_emissao DESC, c.cpr_codigo DESC";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Compra c = new Compra();
                c.setCodigo(rs.getInt("cpr_codigo"));
                c.setEmissao(rs.getDate("cpr_emissao"));
                c.setValor(rs.getBigDecimal("cpr_valor"));
                c.setDesconto(rs.getBigDecimal("cpr_desconto"));
                c.setTotal(rs.getBigDecimal("cpr_total"));
                c.setDataEntrada(rs.getDate("cpr_dtentrada"));
                c.setObs(rs.getString("cpr_obs"));
                Usuario u = new Usuario();
                u.setCodigo(rs.getInt("usu_codigo"));
                u.setNome(rs.getString("usu_nome"));
                c.setUsuario(u);
                Fornecedor f = new Fornecedor();
                f.setCodigo(rs.getInt("for_codigo"));
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("for_nome"));
                f.setPessoa(pessoa);
                c.setFornecedor(f);
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar compras: " + e.getMessage(), e);
        }
        return lista;
    }
}
