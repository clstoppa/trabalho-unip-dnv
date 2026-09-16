package br.com.compravenda.dao;

import br.com.compravenda.model.Cliente;
import br.com.compravenda.model.Pessoa;
import br.com.compravenda.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente cliente) {
        String sqlPessoa = "INSERT INTO pessoa (pes_nome, pes_fantasia, pes_fisica, pes_cpfcnpj, pes_rgie, "
                + "pes_cadastro, pes_endereco, pes_numero, pes_complemento, pes_bairro, pes_cidade, pes_uf, "
                + "pes_cep, pes_fone1, pes_fone2, pes_celular, pes_site, pes_email, pes_ativo) "
                + "VALUES (?, ?, ?, ?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente (pes_codigo, cli_limitecred) VALUES (?, ?)";

        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            Pessoa p = cliente.getPessoa();
            int pesCodigo;
            try (PreparedStatement stmt = con.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                preencherPessoa(stmt, p);
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    pesCodigo = rs.getInt(1);
                }
            }
            p.setCodigo(pesCodigo);
            try (PreparedStatement stmt = con.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, pesCodigo);
                stmt.setBigDecimal(2, cliente.getLimiteCredito());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    cliente.setCodigo(rs.getInt(1));
                }
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        }
    }

    public void atualizar(Cliente cliente) {
        String sqlPessoa = "UPDATE pessoa SET pes_nome=?, pes_fantasia=?, pes_fisica=?, pes_cpfcnpj=?, pes_rgie=?, "
                + "pes_endereco=?, pes_numero=?, pes_complemento=?, pes_bairro=?, pes_cidade=?, pes_uf=?, pes_cep=?, "
                + "pes_fone1=?, pes_fone2=?, pes_celular=?, pes_site=?, pes_email=?, pes_ativo=? WHERE pes_codigo=?";
        String sqlCliente = "UPDATE cliente SET cli_limitecred=? WHERE cli_codigo=?";

        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            Pessoa p = cliente.getPessoa();
            try (PreparedStatement stmt = con.prepareStatement(sqlPessoa)) {
                preencherPessoa(stmt, p);
                stmt.setInt(19, p.getCodigo());
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement(sqlCliente)) {
                stmt.setBigDecimal(1, cliente.getLimiteCredito());
                stmt.setInt(2, cliente.getCodigo());
                stmt.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    public void excluir(int codigo) {
        String sqlBuscaPessoa = "SELECT pes_codigo FROM cliente WHERE cli_codigo = ?";
        String sqlExcluiCliente = "DELETE FROM cliente WHERE cli_codigo = ?";
        String sqlExcluiPessoa = "DELETE FROM pessoa WHERE pes_codigo = ?";
        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            int pesCodigo = -1;
            try (PreparedStatement stmt = con.prepareStatement(sqlBuscaPessoa)) {
                stmt.setInt(1, codigo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) pesCodigo = rs.getInt(1);
                }
            }
            try (PreparedStatement stmt = con.prepareStatement(sqlExcluiCliente)) {
                stmt.setInt(1, codigo);
                stmt.executeUpdate();
            }
            if (pesCodigo != -1) {
                try (PreparedStatement stmt = con.prepareStatement(sqlExcluiPessoa)) {
                    stmt.setInt(1, pesCodigo);
                    stmt.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
        }
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.cli_codigo, c.cli_limitecred, p.* FROM cliente c "
                + "JOIN pessoa p ON p.pes_codigo = c.pes_codigo ORDER BY p.pes_nome";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    private void preencherPessoa(PreparedStatement stmt, Pessoa p) throws SQLException {
        stmt.setString(1, p.getNome());
        stmt.setString(2, p.getFantasia());
        stmt.setString(3, p.getFisica());
        stmt.setString(4, p.getCpfCnpj());
        stmt.setString(5, p.getRgIe());
        stmt.setString(6, p.getEndereco());
        stmt.setString(7, p.getNumero());
        stmt.setString(8, p.getComplemento());
        stmt.setString(9, p.getBairro());
        stmt.setString(10, p.getCidade());
        stmt.setString(11, p.getUf());
        stmt.setString(12, p.getCep());
        stmt.setString(13, p.getFone1());
        stmt.setString(14, p.getFone2());
        stmt.setString(15, p.getCelular());
        stmt.setString(16, p.getSite());
        stmt.setString(17, p.getEmail());
        stmt.setString(18, p.getAtivo());
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setCodigo(rs.getInt("cli_codigo"));
        c.setLimiteCredito(rs.getBigDecimal("cli_limitecred"));
        Pessoa p = new Pessoa();
        p.setCodigo(rs.getInt("pes_codigo"));
        p.setNome(rs.getString("pes_nome"));
        p.setFantasia(rs.getString("pes_fantasia"));
        p.setFisica(rs.getString("pes_fisica"));
        p.setCpfCnpj(rs.getString("pes_cpfcnpj"));
        p.setRgIe(rs.getString("pes_rgie"));
        p.setCadastro(rs.getDate("pes_cadastro"));
        p.setEndereco(rs.getString("pes_endereco"));
        p.setNumero(rs.getString("pes_numero"));
        p.setComplemento(rs.getString("pes_complemento"));
        p.setBairro(rs.getString("pes_bairro"));
        p.setCidade(rs.getString("pes_cidade"));
        p.setUf(rs.getString("pes_uf"));
        p.setCep(rs.getString("pes_cep"));
        p.setFone1(rs.getString("pes_fone1"));
        p.setFone2(rs.getString("pes_fone2"));
        p.setCelular(rs.getString("pes_celular"));
        p.setSite(rs.getString("pes_site"));
        p.setEmail(rs.getString("pes_email"));
        p.setAtivo(rs.getString("pes_ativo"));
        c.setPessoa(p);
        return c;
    }
}
