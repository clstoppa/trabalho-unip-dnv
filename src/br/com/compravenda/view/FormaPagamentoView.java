package br.com.compravenda.view;

import br.com.compravenda.controller.FormaPagamentoController;
import br.com.compravenda.model.FormaPagamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormaPagamentoView extends JFrame {

    private final FormaPagamentoController controller = new FormaPagamentoController();

    private JTextField campoCodigo, campoNome;
    private JComboBox<String> campoAtivo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public FormaPagamentoView() {
        super("Cadastro de Forma de Pagamento");
        montarTela();
        carregarTabela();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel painelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        campoCodigo = new JTextField(5);
        campoCodigo.setEditable(false);
        painelForm.add(campoCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        painelForm.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        campoNome = new JTextField(20);
        painelForm.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        painelForm.add(new JLabel("Ativo:"), gbc);
        gbc.gridx = 1;
        campoAtivo = new JComboBox<>(new String[]{"S", "N"});
        painelForm.add(campoAtivo, gbc);

        JPanel painelBotoes = new JPanel();
        JButton botaoNovo = new JButton("Novo");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoExcluir = new JButton("Excluir");
        painelBotoes.add(botaoNovo);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);

        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "Ativo"}, 0) {
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
        List<FormaPagamento> lista = controller.listarTodos();
        for (FormaPagamento f : lista) {
            modeloTabela.addRow(new Object[]{f.getCodigo(), f.getNome(), f.getAtivo()});
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        campoCodigo.setText(modeloTabela.getValueAt(linha, 0).toString());
        campoNome.setText(modeloTabela.getValueAt(linha, 1).toString());
        campoAtivo.setSelectedItem(modeloTabela.getValueAt(linha, 2).toString());
    }

    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoAtivo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void salvar() {
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        FormaPagamento forma = new FormaPagamento();
        if (!campoCodigo.getText().isEmpty()) forma.setCodigo(Integer.parseInt(campoCodigo.getText()));
        forma.setNome(campoNome.getText().trim());
        forma.setAtivo((String) campoAtivo.getSelectedItem());

        try {
            if (forma.getCodigo() == 0) {
                controller.inserir(forma);
            } else {
                controller.atualizar(forma);
            }
            JOptionPane.showMessageDialog(this, "Forma de pagamento salva com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (campoCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(Integer.parseInt(campoCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
                limparCampos();
                carregarTabela();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
