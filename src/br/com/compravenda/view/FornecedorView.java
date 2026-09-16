package br.com.compravenda.view;

import br.com.compravenda.controller.FornecedorController;
import br.com.compravenda.model.Fornecedor;
import br.com.compravenda.model.Pessoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FornecedorView extends JFrame {

    private final FornecedorController controller = new FornecedorController();

    private JTextField campoCodigo, campoNome, campoFantasia, campoCpfCnpj, campoRgIe, campoEndereco,
            campoNumero, campoBairro, campoCidade, campoUf, campoCep, campoFone1, campoEmail, campoContato;
    private JComboBox<String> campoFisica, campoAtivo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private int pessoaCodigoSelecionado = 0;

    public FornecedorView() {
        super("Cadastro de Fornecedor");
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
        setSize(760, 520);
        setLocationRelativeTo(null);

        JPanel painelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoCodigo = criarCampo(painelForm, gbc, "Código:", 0, 0, 5);
        campoCodigo.setEditable(false);

        gbc.gridx = 2; gbc.gridy = 0;
        painelForm.add(new JLabel("Pessoa Física/Jurídica:"), gbc);
        gbc.gridx = 3;
        campoFisica = new JComboBox<>(new String[]{"F", "J"});
        painelForm.add(campoFisica, gbc);

        campoNome = criarCampo(painelForm, gbc, "Nome:", 1, 0, 25);
        campoFantasia = criarCampo(painelForm, gbc, "Fantasia:", 1, 2, 20);

        campoCpfCnpj = criarCampo(painelForm, gbc, "CPF/CNPJ:", 2, 0, 20);
        campoRgIe = criarCampo(painelForm, gbc, "RG/IE:", 2, 2, 20);

        campoEndereco = criarCampo(painelForm, gbc, "Endereço:", 3, 0, 25);
        campoNumero = criarCampo(painelForm, gbc, "Número:", 3, 2, 10);

        campoBairro = criarCampo(painelForm, gbc, "Bairro:", 4, 0, 20);
        campoCidade = criarCampo(painelForm, gbc, "Cidade:", 4, 2, 20);

        campoUf = criarCampo(painelForm, gbc, "UF:", 5, 0, 5);
        campoCep = criarCampo(painelForm, gbc, "CEP:", 5, 2, 12);

        campoFone1 = criarCampo(painelForm, gbc, "Telefone:", 6, 0, 15);
        campoEmail = criarCampo(painelForm, gbc, "E-mail:", 6, 2, 20);

        campoContato = criarCampo(painelForm, gbc, "Contato:", 7, 0, 20);
        gbc.gridx = 2; gbc.gridy = 7;
        painelForm.add(new JLabel("Ativo:"), gbc);
        gbc.gridx = 3;
        campoAtivo = new JComboBox<>(new String[]{"S", "N"});
        painelForm.add(campoAtivo, gbc);

        JPanel painelBotoes = new JPanel();
        JButton botaoNovo = new JButton("Novo");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoExcluir = new JButton("Excluir");
        painelBotoes.add(botaoNovo);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);

        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "CPF/CNPJ", "Contato"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarSelecionado();
        });

        setLayout(new BorderLayout());
        JScrollPane scrollForm = new JScrollPane(painelForm);
        JPanel topo = new JPanel(new BorderLayout());
        topo.add(scrollForm, BorderLayout.CENTER);
        topo.add(painelBotoes, BorderLayout.SOUTH);
        topo.setPreferredSize(new Dimension(760, 300));
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        botaoNovo.addActionListener(e -> limparCampos());
        botaoSalvar.addActionListener(e -> salvar());
        botaoExcluir.addActionListener(e -> excluir());
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Fornecedor> lista = controller.listarTodos();
        for (Fornecedor f : lista) {
            modeloTabela.addRow(new Object[]{f.getCodigo(), f.getPessoa().getNome(),
                    f.getPessoa().getCpfCnpj(), f.getContato()});
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        int codigo = (int) modeloTabela.getValueAt(linha, 0);
        List<Fornecedor> lista = controller.listarTodos();
        for (Fornecedor f : lista) {
            if (f.getCodigo() == codigo) {
                preencherFormulario(f);
                break;
            }
        }
    }

    private void preencherFormulario(Fornecedor f) {
        Pessoa p = f.getPessoa();
        campoCodigo.setText(String.valueOf(f.getCodigo()));
        pessoaCodigoSelecionado = p.getCodigo();
        campoNome.setText(p.getNome());
        campoFantasia.setText(p.getFantasia());
        campoFisica.setSelectedItem(p.getFisica());
        campoCpfCnpj.setText(p.getCpfCnpj());
        campoRgIe.setText(p.getRgIe());
        campoEndereco.setText(p.getEndereco());
        campoNumero.setText(p.getNumero());
        campoBairro.setText(p.getBairro());
        campoCidade.setText(p.getCidade());
        campoUf.setText(p.getUf());
        campoCep.setText(p.getCep());
        campoFone1.setText(p.getFone1());
        campoEmail.setText(p.getEmail());
        campoContato.setText(f.getContato());
        campoAtivo.setSelectedItem(p.getAtivo());
    }

    private void limparCampos() {
        campoCodigo.setText("");
        pessoaCodigoSelecionado = 0;
        campoNome.setText("");
        campoFantasia.setText("");
        campoFisica.setSelectedIndex(0);
        campoCpfCnpj.setText("");
        campoRgIe.setText("");
        campoEndereco.setText("");
        campoNumero.setText("");
        campoBairro.setText("");
        campoCidade.setText("");
        campoUf.setText("");
        campoCep.setText("");
        campoFone1.setText("");
        campoEmail.setText("");
        campoContato.setText("");
        campoAtivo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void salvar() {
        if (campoNome.getText().trim().isEmpty() || campoCpfCnpj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CPF/CNPJ são obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Fornecedor fornecedor = new Fornecedor();
        Pessoa pessoa = new Pessoa();
        if (!campoCodigo.getText().isEmpty()) fornecedor.setCodigo(Integer.parseInt(campoCodigo.getText()));
        pessoa.setCodigo(pessoaCodigoSelecionado);
        pessoa.setNome(campoNome.getText().trim());
        pessoa.setFantasia(campoFantasia.getText().trim());
        pessoa.setFisica((String) campoFisica.getSelectedItem());
        pessoa.setCpfCnpj(campoCpfCnpj.getText().trim());
        pessoa.setRgIe(campoRgIe.getText().trim());
        pessoa.setEndereco(campoEndereco.getText().trim());
        pessoa.setNumero(campoNumero.getText().trim());
        pessoa.setBairro(campoBairro.getText().trim());
        pessoa.setCidade(campoCidade.getText().trim());
        pessoa.setUf(campoUf.getText().trim());
        pessoa.setCep(campoCep.getText().trim());
        pessoa.setFone1(campoFone1.getText().trim());
        pessoa.setEmail(campoEmail.getText().trim());
        pessoa.setAtivo((String) campoAtivo.getSelectedItem());
        fornecedor.setPessoa(pessoa);
        fornecedor.setContato(campoContato.getText().trim());

        try {
            if (fornecedor.getCodigo() == 0) {
                controller.inserir(fornecedor);
            } else {
                controller.atualizar(fornecedor);
            }
            JOptionPane.showMessageDialog(this, "Fornecedor salvo com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (campoCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este fornecedor?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(Integer.parseInt(campoCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Fornecedor excluído com sucesso!");
                limparCampos();
                carregarTabela();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
