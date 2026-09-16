package br.com.compravenda.view;

import br.com.compravenda.controller.ProdutoController;
import br.com.compravenda.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoView extends JFrame {

    private final ProdutoController controller = new ProdutoController();

    private JTextField campoCodigo, campoNome, campoEstoque, campoUnidade, campoPreco, campoCusto,
            campoAtacado, campoMin, campoMax;
    private JTextArea campoObs;
    private JComboBox<String> campoAtivo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public ProdutoView() {
        super("Cadastro de Produto");
        montarTela();
        carregarTabela();
    }

    private JTextField criarCampo(JPanel painel, GridBagConstraints gbc, String rotulo, int linha, int coluna, int tamanho) {
        gbc.gridx = coluna * 2; gbc.gridy = linha;
        painel.add(new JLabel(rotulo), gbc);
        gbc.gridx = coluna * 2 + 1;
        JTextField campo = new JTextField(tamanho);
        painel.add(campo, gbc);
        return campo;
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);

        JPanel painelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoCodigo = criarCampo(painelForm, gbc, "Código:", 0, 0, 5);
        campoCodigo.setEditable(false);
        campoNome = criarCampo(painelForm, gbc, "Nome:", 0, 1, 20);

        campoEstoque = criarCampo(painelForm, gbc, "Estoque:", 1, 0, 10);
        campoUnidade = criarCampo(painelForm, gbc, "Unidade:", 1, 1, 8);

        campoPreco = criarCampo(painelForm, gbc, "Preço venda:", 2, 0, 10);
        campoCusto = criarCampo(painelForm, gbc, "Custo:", 2, 1, 10);

        campoAtacado = criarCampo(painelForm, gbc, "Preço atacado:", 3, 0, 10);
        gbc.gridx = 2; gbc.gridy = 3;
        painelForm.add(new JLabel("Ativo:"), gbc);
        gbc.gridx = 3;
        campoAtivo = new JComboBox<>(new String[]{"S", "N"});
        painelForm.add(campoAtivo, gbc);

        campoMin = criarCampo(painelForm, gbc, "Estoque mínimo:", 4, 0, 10);
        campoMax = criarCampo(painelForm, gbc, "Estoque máximo:", 4, 1, 10);

        gbc.gridx = 0; gbc.gridy = 5;
        painelForm.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        campoObs = new JTextArea(3, 30);
        painelForm.add(new JScrollPane(campoObs), gbc);
        gbc.gridwidth = 1;

        JPanel painelBotoes = new JPanel();
        JButton botaoNovo = new JButton("Novo");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoExcluir = new JButton("Excluir");
        painelBotoes.add(botaoNovo);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);

        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "Estoque", "Preço"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarSelecionado();
        });

        setLayout(new BorderLayout());
        JPanel topo = new JPanel(new BorderLayout());
        topo.add(painelForm, BorderLayout.CENTER);
        topo.add(painelBotoes, BorderLayout.SOUTH);
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        botaoNovo.addActionListener(e -> limparCampos());
        botaoSalvar.addActionListener(e -> salvar());
        botaoExcluir.addActionListener(e -> excluir());
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Produto> lista = controller.listarTodos();
        for (Produto p : lista) {
            modeloTabela.addRow(new Object[]{p.getCodigo(), p.getNome(), p.getEstoque(), p.getPreco()});
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        int codigo = (int) modeloTabela.getValueAt(linha, 0);
        Produto p = controller.buscarPorCodigo(codigo);
        if (p != null) preencherFormulario(p);
    }

    private void preencherFormulario(Produto p) {
        campoCodigo.setText(String.valueOf(p.getCodigo()));
        campoNome.setText(p.getNome());
        campoEstoque.setText(p.getEstoque().toString());
        campoUnidade.setText(p.getUnidade());
        campoPreco.setText(p.getPreco().toString());
        campoCusto.setText(p.getCusto().toString());
        campoAtacado.setText(p.getPrecoAtacado().toString());
        campoMin.setText(p.getEstoqueMinimo().toString());
        campoMax.setText(p.getEstoqueMaximo().toString());
        campoObs.setText(p.getObs());
        campoAtivo.setSelectedItem(p.getAtivo());
    }

    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoEstoque.setText("0");
        campoUnidade.setText("");
        campoPreco.setText("0");
        campoCusto.setText("0");
        campoAtacado.setText("0");
        campoMin.setText("0");
        campoMax.setText("0");
        campoObs.setText("");
        campoAtivo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private BigDecimal lerDecimal(JTextField campo) {
        try {
            return new BigDecimal(campo.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private void salvar() {
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Produto produto = new Produto();
        if (!campoCodigo.getText().isEmpty()) produto.setCodigo(Integer.parseInt(campoCodigo.getText()));
        produto.setNome(campoNome.getText().trim());
        produto.setEstoque(lerDecimal(campoEstoque));
        produto.setUnidade(campoUnidade.getText().trim());
        produto.setPreco(lerDecimal(campoPreco));
        produto.setCusto(lerDecimal(campoCusto));
        produto.setPrecoAtacado(lerDecimal(campoAtacado));
        produto.setEstoqueMinimo(lerDecimal(campoMin));
        produto.setEstoqueMaximo(lerDecimal(campoMax));
        produto.setObs(campoObs.getText().trim());
        produto.setAtivo((String) campoAtivo.getSelectedItem());

        try {
            if (produto.getCodigo() == 0) {
                controller.inserir(produto);
            } else {
                controller.atualizar(produto);
            }
            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (campoCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este produto?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(Integer.parseInt(campoCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                limparCampos();
                carregarTabela();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
