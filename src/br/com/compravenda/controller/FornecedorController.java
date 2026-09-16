package br.com.compravenda.controller;

import br.com.compravenda.dao.FornecedorDAO;
import br.com.compravenda.model.Fornecedor;

import java.util.List;

public class FornecedorController {

    private final FornecedorDAO dao = new FornecedorDAO();

    public void inserir(Fornecedor fornecedor) { dao.inserir(fornecedor); }
    public void atualizar(Fornecedor fornecedor) { dao.atualizar(fornecedor); }
    public void excluir(int codigo) { dao.excluir(codigo); }
    public List<Fornecedor> listarTodos() { return dao.listarTodos(); }
}
