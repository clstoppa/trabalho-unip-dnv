package br.com.compravenda.view;

import br.com.compravenda.controller.UsuarioController;
import br.com.compravenda.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioView extends JFrame {

    private final UsuarioController controller = new UsuarioController();

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JComboBox<String> campoAtivo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public UsuarioView() {
        super("Cadastro de Usuário");
        montarTela();
        carregarTabela();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 450);
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
        painelForm.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        campoLogin = new JTextField(20);
        painelForm.add(campoLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        painelForm.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        campoSenha = new JPasswordField(20);
        painelForm.add(campoSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
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

        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "Login", "Ativo"}, 0) {
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
        List<Usuario> lista = controller.listarTodos();
        for (Usuario u : lista) {
            modeloTabela.addRow(new Object[]{u.getCodigo(), u.getNome(), u.getLogin(), u.getAtivo()});
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        campoCodigo.setText(modeloTabela.getValueAt(linha, 0).toString());
        campoNome.setText(modeloTabela.getValueAt(linha, 1).toString());
        campoLogin.setText(modeloTabela.getValueAt(linha, 2).toString());
        campoSenha.setText("");
        campoAtivo.setSelectedItem(modeloTabela.getValueAt(linha, 3).toString());
    }

    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoLogin.setText("");
        campoSenha.setText("");
        campoAtivo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void salvar() {
        if (campoNome.getText().trim().isEmpty() || campoLogin.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e login são obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Usuario u = new Usuario();
        if (!campoCodigo.getText().isEmpty()) u.setCodigo(Integer.parseInt(campoCodigo.getText()));
        u.setNome(campoNome.getText().trim());
        u.setLogin(campoLogin.getText().trim());
        u.setSenha(new String(campoSenha.getPassword()));
        u.setAtivo((String) campoAtivo.getSelectedItem());

        try {
            if (u.getCodigo() == 0) {
                controller.inserir(u);
            } else {
                if (u.getSenha().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Informe a senha para atualizar o usuário.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                controller.atualizar(u);
            }
            JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (campoCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este usuário?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(Integer.parseInt(campoCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                limparCampos();
                carregarTabela();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
