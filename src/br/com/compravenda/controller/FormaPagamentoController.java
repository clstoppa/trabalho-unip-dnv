package br.com.compravenda.controller;

import br.com.compravenda.dao.FormaPagamentoDAO;
import br.com.compravenda.model.FormaPagamento;

import java.util.List;

public class FormaPagamentoController {

    private final FormaPagamentoDAO dao = new FormaPagamentoDAO();

    public void inserir(FormaPagamento forma) { dao.inserir(forma); }
    public void atualizar(FormaPagamento forma) { dao.atualizar(forma); }
    public void excluir(int codigo) { dao.excluir(codigo); }
    public List<FormaPagamento> listarTodos() { return dao.listarTodos(); }
}
