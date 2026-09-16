package br.com.compravenda.view;

import br.com.compravenda.controller.CompraController;
import br.com.compravenda.controller.FornecedorController;
import br.com.compravenda.controller.ProdutoController;
import br.com.compravenda.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class CompraView extends JFrame {

    private final CompraController controller = new CompraController();
    private final FornecedorController fornecedorController = new FornecedorController();
    private final ProdutoController produtoController = new ProdutoController();
    private final Usuario usuarioLogado;

    private JComboBox<Fornecedor> comboFornecedor;
    private JComboBox<Produto> comboProduto;
    private JTextField campoQtde, campoPrecoItem;

    private DefaultTableModel modeloItens;
    private JTable tabelaItens;
    private JLabel labelTotal;

    private DefaultTableModel modeloCompras;
    private JTable tabelaCompras;

    public CompraView(Usuario usuarioLogado) {
        super("Compra");
        this.usuarioLogado = usuarioLogado;
        montarTela();
        carregarCompras();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelTopo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelTopo.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        comboFornecedor = new JComboBox<>(fornecedorController.listarTodos().toArray(new Fornecedor[0]));
        painelTopo.add(comboFornecedor, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        painelTopo.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1;
        comboProduto = new JComboBox<>(produtoController.listarTodos().toArray(new Produto[0]));
        painelTopo.add(comboProduto, gbc);
        comboProduto.addActionListener(e -> {
            Produto p = (Produto) comboProduto.getSelectedItem();
            if (p != null) campoPrecoItem.setText(p.getCusto().toString());
        });

        gbc.gridx = 2; gbc.gridy = 1;
        painelTopo.add(new JLabel("Qtde:"), gbc);
        gbc.gridx = 3;
        campoQtde = new JTextField("1", 6);
        painelTopo.add(campoQtde, gbc);

        gbc.gridx = 4; gbc.gridy = 1;
        painelTopo.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 5;
        campoPrecoItem = new JTextField(8);
        painelTopo.add(campoPrecoItem, gbc);

        gbc.gridx = 6; gbc.gridy = 1;
        JButton botaoAddItem = new JButton("Adicionar item");
        painelTopo.add(botaoAddItem, gbc);

        modeloItens = new DefaultTableModel(new Object[]{"Produto", "Qtde", "Preço", "Total"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modeloItens);

        labelTotal = new JLabel("Total: R$ 0.00");
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton botaoFinalizar = new JButton("Finalizar Compra");
        JButton botaoNova = new JButton("Nova Compra");

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.add(new JLabel("Itens da compra:"), BorderLayout.NORTH);
        painelCentro.add(new JScrollPane(tabelaItens), BorderLayout.CENTER);

        JPanel painelBotoesFinais = new JPanel();
        painelBotoesFinais.add(labelTotal);
        painelBotoesFinais.add(botaoFinalizar);
        painelBotoesFinais.add(botaoNova);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelBotoesFinais, BorderLayout.NORTH);
        modeloCompras = new DefaultTableModel(new Object[]{"Código", "Emissão", "Fornecedor", "Total"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaCompras = new JTable(modeloCompras);
        painelInferior.add(new JScrollPane(tabelaCompras), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelCentro, painelInferior);
        split.setResizeWeight(0.5);

        add(painelTopo, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        botaoAddItem.addActionListener(e -> adicionarItem());
        botaoFinalizar.addActionListener(e -> finalizarCompra());
        botaoNova.addActionListener(e -> limparCompra());
    }

    private void adicionarItem() {
        Produto produto = (Produto) comboProduto.getSelectedItem();
        if (produto == null) return;
        try {
            BigDecimal qtde = new BigDecimal(campoQtde.getText().trim().replace(",", "."));
            BigDecimal preco = new BigDecimal(campoPrecoItem.getText().trim().replace(",", "."));
            BigDecimal total = qtde.multiply(preco);
            modeloItens.addRow(new Object[]{produto, qtde, preco, total});
            atualizarTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números válidos.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < modeloItens.getRowCount(); i++) {
            total = total.add((BigDecimal) modeloItens.getValueAt(i, 3));
        }
        labelTotal.setText("Total: R$ " + total.toString());
    }

    private void finalizarCompra() {
        Fornecedor fornecedor = (Fornecedor) comboFornecedor.getSelectedItem();
        if (fornecedor == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (modeloItens.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um produto.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Compra compra = new Compra();
        compra.setUsuario(usuarioLogado);
        compra.setFornecedor(fornecedor);
        compra.setDataEntrada(new Date(System.currentTimeMillis()));

        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < modeloItens.getRowCount(); i++) {
            CompraProduto item = new CompraProduto();
            item.setProduto((Produto) modeloItens.getValueAt(i, 0));
            item.setQuantidade((BigDecimal) modeloItens.getValueAt(i, 1));
            item.setPreco((BigDecimal) modeloItens.getValueAt(i, 2));
            item.setDesconto(BigDecimal.ZERO);
            item.setTotal((BigDecimal) modeloItens.getValueAt(i, 3));
            compra.getProdutos().add(item);
            total = total.add(item.getTotal());
        }
        compra.setValor(total);
        compra.setDesconto(BigDecimal.ZERO);
        compra.setTotal(total);

        try {
            controller.inserir(compra);
            JOptionPane.showMessageDialog(this, "Compra registrada com sucesso!");
            limparCompra();
            carregarCompras();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCompra() {
        modeloItens.setRowCount(0);
        atualizarTotal();
    }

    private void carregarCompras() {
        modeloCompras.setRowCount(0);
        List<Compra> lista = controller.listarTodas();
        for (Compra c : lista) {
            modeloCompras.addRow(new Object[]{c.getCodigo(), c.getEmissao(), c.getFornecedor().getPessoa().getNome(), c.getTotal()});
        }
    }
}
