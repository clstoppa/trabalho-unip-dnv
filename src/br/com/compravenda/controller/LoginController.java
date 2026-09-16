package br.com.compravenda.controller;

import br.com.compravenda.dao.UsuarioDAO;
import br.com.compravenda.model.Usuario;

public class LoginController {

    private final UsuarioDAO dao = new UsuarioDAO();
    private Usuario usuarioLogado;

    public boolean autenticar(String login, String senha) {
        usuarioLogado = dao.autenticar(login, senha);
        return usuarioLogado != null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
