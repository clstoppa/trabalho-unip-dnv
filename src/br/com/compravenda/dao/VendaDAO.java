package br.com.compravenda.dao;

import br.com.compravenda.model.*;
import br.com.compravenda.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public void inserir(Venda venda) {
        String sqlVenda = "INSERT INTO venda (usu_codigo, cli_codigo, vda_data, vda_valor, vda_desconto, vda_total, vda_obs) "
                + "VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO venda_produto (vda_codigo, pro_codigo, vep_qtde, vep_preco, vep_desconto, vep_total) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPagto = "INSERT INTO venda_pagto (vda_codigo, fpg_codigo, vdp_valor) VALUES (?, ?, ?)";

        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            int vdaCodigo;
            try (PreparedStatement stmt = con.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, venda.getUsuario().getCodigo());
                stmt.setInt(2, venda.getCliente().getCodigo());
                stmt.setBigDecimal(3, venda.getValor());
                stmt.setBigDecimal(4, venda.getDesconto());
                stmt.setBigDecimal(5, venda.getTotal());
                stmt.setString(6, venda.getObs());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    vdaCodigo = rs.getInt(1);
                }
            }
            venda.setCodigo(vdaCodigo);

            try (PreparedStatement stmt = con.prepareStatement(sqlItem)) {
                for (VendaProduto item : venda.getProdutos()) {
                    stmt.setInt(1, vdaCodigo);
                    stmt.setInt(2, item.getProduto().getCodigo());
                    stmt.setBigDecimal(3, item.getQuantidade());
                    stmt.setBigDecimal(4, item.getPreco());
                    stmt.setBigDecimal(5, item.getDesconto());
                    stmt.setBigDecimal(6, item.getTotal());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            try (PreparedStatement stmt = con.prepareStatement(sqlPagto)) {
                for (VendaPagamento pagto : venda.getPagamentos()) {
                    stmt.setInt(1, vdaCodigo);
                    stmt.setInt(2, pagto.getFormaPagamento().getCodigo());
                    stmt.setBigDecimal(3, pagto.getValor());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir venda: " + e.getMessage(), e);
        }
    }

    public void excluir(int codigo) {
        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM venda_pagto WHERE vda_codigo = ?")) {
                stmt.setInt(1, codigo);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM venda_produto WHERE vda_codigo = ?")) {
                stmt.setInt(1, codigo);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM venda WHERE vda_codigo = ?")) {
                stmt.setInt(1, codigo);
                stmt.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir venda: " + e.getMessage(), e);
        }
    }

    public List<Venda> listarTodas() {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT v.*, u.usu_nome, p.pes_nome AS cli_nome FROM venda v "
                + "JOIN usuario u ON u.usu_codigo = v.usu_codigo "
                + "JOIN cliente c ON c.cli_codigo = v.cli_codigo "
                + "JOIN pessoa p ON p.pes_codigo = c.pes_codigo "
                + "ORDER BY v.vda_data DESC, v.vda_codigo DESC";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Venda v = new Venda();
                v.setCodigo(rs.getInt("vda_codigo"));
                v.setData(rs.getDate("vda_data"));
                v.setValor(rs.getBigDecimal("vda_valor"));
                v.setDesconto(rs.getBigDecimal("vda_desconto"));
                v.setTotal(rs.getBigDecimal("vda_total"));
                v.setObs(rs.getString("vda_obs"));
                Usuario u = new Usuario();
                u.setCodigo(rs.getInt("usu_codigo"));
                u.setNome(rs.getString("usu_nome"));
                v.setUsuario(u);
                Cliente c = new Cliente();
                c.setCodigo(rs.getInt("cli_codigo"));
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("cli_nome"));
                c.setPessoa(pessoa);
                v.setCliente(c);
                lista.add(v);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas: " + e.getMessage(), e);
        }
        return lista;
    }
}
