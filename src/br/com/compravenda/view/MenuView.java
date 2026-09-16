package br.com.compravenda.view;

import br.com.compravenda.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JFrame {

    private final Usuario usuarioLogado;

    public MenuView(Usuario usuarioLogado) {
        super("EMPRESA X - Gerenciamento de Compra e Venda");
        this.usuarioLogado = usuarioLogado;
        montarTela();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem itemUsuario = new JMenuItem("Usuário");
        JMenuItem itemCliente = new JMenuItem("Cliente");
        JMenuItem itemFornecedor = new JMenuItem("Fornecedor");
        JMenuItem itemProduto = new JMenuItem("Produto");
        JMenuItem itemFormaPagamento = new JMenuItem("Forma de Pagamento");
        menuCadastros.add(itemUsuario);
        menuCadastros.add(itemCliente);
        menuCadastros.add(itemFornecedor);
        menuCadastros.add(itemProduto);
        menuCadastros.add(itemFormaPagamento);

        JMenu menuMovimentos = new JMenu("Movimentos");
        JMenuItem itemVenda = new JMenuItem("Venda");
        JMenuItem itemCompra = new JMenuItem("Compra");
        menuMovimentos.add(itemVenda);
        menuMovimentos.add(itemCompra);

        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Encerrar sistema");
        menuSair.add(itemSair);

        menuBar.add(menuCadastros);
        menuBar.add(menuMovimentos);
        menuBar.add(menuSair);
        setJMenuBar(menuBar);

        JLabel boasVindas = new JLabel(
                "Bem-vindo(a), " + usuarioLogado.getNome() + "!", SwingConstants.CENTER);
        boasVindas.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(boasVindas, BorderLayout.CENTER);

        itemUsuario.addActionListener(e -> new UsuarioView().setVisible(true));
        itemCliente.addActionListener(e -> new ClienteView().setVisible(true));
        itemFornecedor.addActionListener(e -> new FornecedorView().setVisible(true));
        itemProduto.addActionListener(e -> new ProdutoView().setVisible(true));
        itemFormaPagamento.addActionListener(e -> new FormaPagamentoView().setVisible(true));
        itemVenda.addActionListener(e -> new VendaView(usuarioLogado).setVisible(true));
        itemCompra.addActionListener(e -> new CompraView(usuarioLogado).setVisible(true));
        itemSair.addActionListener(e -> System.exit(0));
    }
}
