package br.com.compravenda.controller;

import br.com.compravenda.dao.CompraDAO;
import br.com.compravenda.model.Compra;

import java.util.List;

public class CompraController {

    private final CompraDAO dao = new CompraDAO();

    public void inserir(Compra compra) { dao.inserir(compra); }
    public void excluir(int codigo) { dao.excluir(codigo); }
    public List<Compra> listarTodas() { return dao.listarTodas(); }
}
