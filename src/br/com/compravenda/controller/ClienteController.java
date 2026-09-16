package br.com.compravenda.controller;

import br.com.compravenda.dao.ClienteDAO;
import br.com.compravenda.model.Cliente;

import java.util.List;

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO();

    public void inserir(Cliente cliente) { dao.inserir(cliente); }
    public void atualizar(Cliente cliente) { dao.atualizar(cliente); }
    public void excluir(int codigo) { dao.excluir(codigo); }
    public List<Cliente> listarTodos() { return dao.listarTodos(); }
}
