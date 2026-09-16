package br.com.compravenda.dao;

import br.com.compravenda.model.Fornecedor;
import br.com.compravenda.model.Pessoa;
import br.com.compravenda.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public void inserir(Fornecedor fornecedor) {
        String sqlPessoa = "INSERT INTO pessoa (pes_nome, pes_fantasia, pes_fisica, pes_cpfcnpj, pes_rgie, "
                + "pes_cadastro, pes_endereco, pes_numero, pes_complemento, pes_bairro, pes_cidade, pes_uf, "
                + "pes_cep, pes_fone1, pes_fone2, pes_celular, pes_site, pes_email, pes_ativo) "
                + "VALUES (?, ?, ?, ?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlFornecedor = "INSERT INTO fornecedor (pes_codigo, for_contato) VALUES (?, ?)";

        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            Pessoa p = fornecedor.getPessoa();
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
            try (PreparedStatement stmt = con.prepareStatement(sqlFornecedor, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, pesCodigo);
                stmt.setString(2, fornecedor.getContato());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    fornecedor.setCodigo(rs.getInt(1));
                }
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir fornecedor: " + e.getMessage(), e);
        }
    }

    public void atualizar(Fornecedor fornecedor) {
        String sqlPessoa = "UPDATE pessoa SET pes_nome=?, pes_fantasia=?, pes_fisica=?, pes_cpfcnpj=?, pes_rgie=?, "
                + "pes_endereco=?, pes_numero=?, pes_complemento=?, pes_bairro=?, pes_cidade=?, pes_uf=?, pes_cep=?, "
                + "pes_fone1=?, pes_fone2=?, pes_celular=?, pes_site=?, pes_email=?, pes_ativo=? WHERE pes_codigo=?";
        String sqlFornecedor = "UPDATE fornecedor SET for_contato=? WHERE for_codigo=?";

        try (Connection con = ConexaoFactory.getConexao()) {
            con.setAutoCommit(false);
            Pessoa p = fornecedor.getPessoa();
            try (PreparedStatement stmt = con.prepareStatement(sqlPessoa)) {
                preencherPessoa(stmt, p);
                stmt.setInt(19, p.getCodigo());
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement(sqlFornecedor)) {
                stmt.setString(1, fornecedor.getContato());
                stmt.setInt(2, fornecedor.getCodigo());
                stmt.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar fornecedor: " + e.getMessage(), e);
        }
    }

    public void excluir(int codigo) {
        String sqlBuscaPessoa = "SELECT pes_codigo FROM fornecedor WHERE for_codigo = ?";
        String sqlExcluiFornecedor = "DELETE FROM fornecedor WHERE for_codigo = ?";
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
            try (PreparedStatement stmt = con.prepareStatement(sqlExcluiFornecedor)) {
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
            throw new RuntimeException("Erro ao excluir fornecedor: " + e.getMessage(), e);
        }
    }

    public List<Fornecedor> listarTodos() {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT f.for_codigo, f.for_contato, p.* FROM fornecedor f "
                + "JOIN pessoa p ON p.pes_codigo = f.pes_codigo ORDER BY p.pes_nome";
        try (Connection con = ConexaoFactory.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fornecedores: " + e.getMessage(), e);
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

    private Fornecedor mapear(ResultSet rs) throws SQLException {
        Fornecedor f = new Fornecedor();
        f.setCodigo(rs.getInt("for_codigo"));
        f.setContato(rs.getString("for_contato"));
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
        f.setPessoa(p);
        return f;
    }
}
