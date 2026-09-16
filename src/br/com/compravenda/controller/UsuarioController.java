package br.com.compravenda.controller;

import br.com.compravenda.dao.UsuarioDAO;
import br.com.compravenda.model.Usuario;

import java.util.List;

public class UsuarioController {

    private final UsuarioDAO dao = new UsuarioDAO();

    public void inserir(Usuario usuario) { dao.inserir(usuario); }
    public void atualizar(Usuario usuario) { dao.atualizar(usuario); }
    public void excluir(int codigo) { dao.excluir(codigo); }
    public List<Usuario> listarTodos() { return dao.listarTodos(); }
}
