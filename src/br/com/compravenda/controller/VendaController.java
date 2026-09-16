package br.com.compravenda.controller;

import br.com.compravenda.dao.VendaDAO;
import br.com.compravenda.model.Venda;

import java.util.List;

public class VendaController {

    private final VendaDAO dao = new VendaDAO();

    public void inserir(Venda venda) { dao.inserir(venda); }
    public void excluir(int codigo) { dao.excluir(codigo); }
    public List<Venda> listarTodas() { return dao.listarTodas(); }
}
