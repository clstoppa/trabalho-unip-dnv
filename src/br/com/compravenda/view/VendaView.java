package br.com.compravenda.view;

import br.com.compravenda.controller.ClienteController;
import br.com.compravenda.controller.FormaPagamentoController;
import br.com.compravenda.controller.ProdutoController;
import br.com.compravenda.controller.VendaController;
import br.com.compravenda.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class VendaView extends JFrame {

    private final VendaController controller = new VendaController();
    private final ClienteController clienteController = new ClienteController();
    private final ProdutoController produtoController = new ProdutoController();
    private final FormaPagamentoController formaPagamentoController = new FormaPagamentoController();
    private final Usuario usuarioLogado;

    private JComboBox<Cliente> comboCliente;
    private JComboBox<Produto> comboProduto;
    private JTextField campoQtde, campoPrecoItem;
    private JComboBox<FormaPagamento> comboFormaPagamento;
    private JTextField campoValorPagto;

    private DefaultTableModel modeloItens;
    private JTable tabelaItens;
    private DefaultTableModel modeloPagtos;
    private JTable tabelaPagtos;
    private JLabel labelTotal;

    private DefaultTableModel modeloVendas;
    private JTable tabelaVendas;

    public VendaView(Usuario usuarioLogado) {
        super("Venda");
        this.usuarioLogado = usuarioLogado;
        montarTela();
        carregarVendas();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelTopo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelTopo.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        comboCliente = new JComboBox<>(clienteController.listarTodos().toArray(new Cliente[0]));
        painelTopo.add(comboCliente, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        painelTopo.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1;
        comboProduto = new JComboBox<>(produtoController.listarTodos().toArray(new Produto[0]));
        painelTopo.add(comboProduto, gbc);
        comboProduto.addActionListener(e -> {
            Produto p = (Produto) comboProduto.getSelectedItem();
            if (p != null) campoPrecoItem.setText(p.getPreco().toString());
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

        gbc.gridx = 0; gbc.gridy = 3;
        painelTopo.add(new JLabel("Forma pagto:"), gbc);
        gbc.gridx = 1;
        comboFormaPagamento = new JComboBox<>(formaPagamentoController.listarTodos().toArray(new FormaPagamento[0]));
        painelTopo.add(comboFormaPagamento, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        painelTopo.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 3;
        campoValorPagto = new JTextField(8);
        painelTopo.add(campoValorPagto, gbc);

        gbc.gridx = 4; gbc.gridy = 3;
        JButton botaoAddPagto = new JButton("Adicionar pagamento");
        painelTopo.add(botaoAddPagto, gbc);

        modeloPagtos = new DefaultTableModel(new Object[]{"Forma de Pagamento", "Valor"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaPagtos = new JTable(modeloPagtos);

        labelTotal = new JLabel("Total: R$ 0.00");
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton botaoFinalizar = new JButton("Finalizar Venda");
        JButton botaoNova = new JButton("Nova Venda");

        JPanel painelItensEPagtos = new JPanel(new GridLayout(1, 2, 8, 0));
        JPanel painelItens = new JPanel(new BorderLayout());
        painelItens.add(new JLabel("Itens da venda:"), BorderLayout.NORTH);
        painelItens.add(new JScrollPane(tabelaItens), BorderLayout.CENTER);
        JPanel painelPagtos = new JPanel(new BorderLayout());
        painelPagtos.add(new JLabel("Formas de pagamento:"), BorderLayout.NORTH);
        painelPagtos.add(new JScrollPane(tabelaPagtos), BorderLayout.CENTER);
        painelItensEPagtos.add(painelItens);
        painelItensEPagtos.add(painelPagtos);

        JPanel painelInferior = new JPanel(new BorderLayout());
        JPanel painelBotoesFinais = new JPanel();
        painelBotoesFinais.add(labelTotal);
        painelBotoesFinais.add(botaoFinalizar);
        painelBotoesFinais.add(botaoNova);
        painelInferior.add(painelBotoesFinais, BorderLayout.NORTH);

        modeloVendas = new DefaultTableModel(new Object[]{"Código", "Data", "Cliente", "Total"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaVendas = new JTable(modeloVendas);
        painelInferior.add(new JScrollPane(tabelaVendas), BorderLayout.CENTER);

        JSplitPane splitCentral = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelItensEPagtos, painelInferior);
        splitCentral.setResizeWeight(0.5);

        add(painelTopo, BorderLayout.NORTH);
        add(splitCentral, BorderLayout.CENTER);

        botaoAddItem.addActionListener(e -> adicionarItem());
        botaoAddPagto.addActionListener(e -> adicionarPagamento());
        botaoFinalizar.addActionListener(e -> finalizarVenda());
        botaoNova.addActionListener(e -> limparVenda());
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

    private void adicionarPagamento() {
        FormaPagamento forma = (FormaPagamento) comboFormaPagamento.getSelectedItem();
        if (forma == null) return;
        try {
            BigDecimal valor = new BigDecimal(campoValorPagto.getText().trim().replace(",", "."));
            modeloPagtos.addRow(new Object[]{forma, valor});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor deve ser um número válido.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < modeloItens.getRowCount(); i++) {
            total = total.add((BigDecimal) modeloItens.getValueAt(i, 3));
        }
        labelTotal.setText("Total: R$ " + total.toString());
    }

    private void finalizarVenda() {
        Cliente cliente = (Cliente) comboCliente.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (modeloItens.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um produto.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Venda venda = new Venda();
        venda.setUsuario(usuarioLogado);
        venda.setCliente(cliente);

        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < modeloItens.getRowCount(); i++) {
            VendaProduto item = new VendaProduto();
            item.setProduto((Produto) modeloItens.getValueAt(i, 0));
            item.setQuantidade((BigDecimal) modeloItens.getValueAt(i, 1));
            item.setPreco((BigDecimal) modeloItens.getValueAt(i, 2));
            item.setDesconto(BigDecimal.ZERO);
            item.setTotal((BigDecimal) modeloItens.getValueAt(i, 3));
            venda.getProdutos().add(item);
            total = total.add(item.getTotal());
        }
        venda.setValor(total);
        venda.setDesconto(BigDecimal.ZERO);
        venda.setTotal(total);

        for (int i = 0; i < modeloPagtos.getRowCount(); i++) {
            VendaPagamento pagto = new VendaPagamento();
            pagto.setFormaPagamento((FormaPagamento) modeloPagtos.getValueAt(i, 0));
            pagto.setValor((BigDecimal) modeloPagtos.getValueAt(i, 1));
            venda.getPagamentos().add(pagto);
        }

        try {
            controller.inserir(venda);
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            limparVenda();
            carregarVendas();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparVenda() {
        modeloItens.setRowCount(0);
        modeloPagtos.setRowCount(0);
        atualizarTotal();
    }

    private void carregarVendas() {
        modeloVendas.setRowCount(0);
        List<Venda> lista = controller.listarTodas();
        for (Venda v : lista) {
            modeloVendas.addRow(new Object[]{v.getCodigo(), v.getData(), v.getCliente().getPessoa().getNome(), v.getTotal()});
        }
    }
}
