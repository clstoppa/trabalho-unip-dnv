package br.com.compravenda.controller;

import br.com.compravenda.dao.ProdutoDAO;
import br.com.compravenda.model.Produto;

import java.util.List;

public class ProdutoController {

    private final ProdutoDAO dao = new ProdutoDAO();

    public void inserir(Produto produto) { dao.inserir(produto); }
    public void atualizar(Produto produto) { dao.atualizar(produto); }
    public void excluir(int codigo) { dao.excluir(codigo); }
    public List<Produto> listarTodos() { return dao.listarTodos(); }
    public Produto buscarPorCodigo(int codigo) { return dao.buscarPorCodigo(codigo); }
}
