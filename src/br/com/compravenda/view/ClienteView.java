package br.com.compravenda.view;

import br.com.compravenda.controller.ClienteController;
import br.com.compravenda.model.Cliente;
import br.com.compravenda.model.Pessoa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ClienteView extends JFrame {

    private final ClienteController controller = new ClienteController();

    private JTextField campoCodigo, campoNome, campoFantasia, campoCpfCnpj, campoRgIe, campoEndereco,
            campoNumero, campoComplemento, campoBairro, campoCidade, campoUf, campoCep,
            campoFone1, campoCelular, campoEmail, campoLimiteCredito;
    private JComboBox<String> campoFisica, campoAtivo;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private int pessoaCodigoSelecionado = 0;

    public ClienteView() {
        super("Cadastro de Cliente");
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
        setSize(760, 560);
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

        campoComplemento = criarCampo(painelForm, gbc, "Complemento:", 4, 0, 20);
        campoBairro = criarCampo(painelForm, gbc, "Bairro:", 4, 2, 20);

        campoCidade = criarCampo(painelForm, gbc, "Cidade:", 5, 0, 20);
        campoUf = criarCampo(painelForm, gbc, "UF:", 5, 2, 5);

        campoCep = criarCampo(painelForm, gbc, "CEP:", 6, 0, 12);
        campoFone1 = criarCampo(painelForm, gbc, "Telefone:", 6, 2, 15);

        campoCelular = criarCampo(painelForm, gbc, "Celular:", 7, 0, 15);
        campoEmail = criarCampo(painelForm, gbc, "E-mail:", 7, 2, 20);

        campoLimiteCredito = criarCampo(painelForm, gbc, "Limite de Crédito:", 8, 0, 12);
        gbc.gridx = 2; gbc.gridy = 8;
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

        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "CPF/CNPJ", "Cidade"}, 0) {
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
        topo.setPreferredSize(new Dimension(760, 320));
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        botaoNovo.addActionListener(e -> limparCampos());
        botaoSalvar.addActionListener(e -> salvar());
        botaoExcluir.addActionListener(e -> excluir());
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Cliente> lista = controller.listarTodos();
        for (Cliente c : lista) {
            modeloTabela.addRow(new Object[]{c.getCodigo(), c.getPessoa().getNome(),
                    c.getPessoa().getCpfCnpj(), c.getPessoa().getCidade()});
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        int codigo = (int) modeloTabela.getValueAt(linha, 0);
        List<Cliente> lista = controller.listarTodos();
        for (Cliente c : lista) {
            if (c.getCodigo() == codigo) {
                preencherFormulario(c);
                break;
            }
        }
    }

    private void preencherFormulario(Cliente c) {
        Pessoa p = c.getPessoa();
        campoCodigo.setText(String.valueOf(c.getCodigo()));
        pessoaCodigoSelecionado = p.getCodigo();
        campoNome.setText(p.getNome());
        campoFantasia.setText(p.getFantasia());
        campoFisica.setSelectedItem(p.getFisica());
        campoCpfCnpj.setText(p.getCpfCnpj());
        campoRgIe.setText(p.getRgIe());
        campoEndereco.setText(p.getEndereco());
        campoNumero.setText(p.getNumero());
        campoComplemento.setText(p.getComplemento());
        campoBairro.setText(p.getBairro());
        campoCidade.setText(p.getCidade());
        campoUf.setText(p.getUf());
        campoCep.setText(p.getCep());
        campoFone1.setText(p.getFone1());
        campoCelular.setText(p.getCelular());
        campoEmail.setText(p.getEmail());
        campoLimiteCredito.setText(c.getLimiteCredito() != null ? c.getLimiteCredito().toString() : "0");
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
        campoComplemento.setText("");
        campoBairro.setText("");
        campoCidade.setText("");
        campoUf.setText("");
        campoCep.setText("");
        campoFone1.setText("");
        campoCelular.setText("");
        campoEmail.setText("");
        campoLimiteCredito.setText("0");
        campoAtivo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void salvar() {
        if (campoNome.getText().trim().isEmpty() || campoCpfCnpj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CPF/CNPJ são obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cliente cliente = new Cliente();
        Pessoa pessoa = new Pessoa();
        if (!campoCodigo.getText().isEmpty()) cliente.setCodigo(Integer.parseInt(campoCodigo.getText()));
        pessoa.setCodigo(pessoaCodigoSelecionado);
        pessoa.setNome(campoNome.getText().trim());
        pessoa.setFantasia(campoFantasia.getText().trim());
        pessoa.setFisica((String) campoFisica.getSelectedItem());
        pessoa.setCpfCnpj(campoCpfCnpj.getText().trim());
        pessoa.setRgIe(campoRgIe.getText().trim());
        pessoa.setEndereco(campoEndereco.getText().trim());
        pessoa.setNumero(campoNumero.getText().trim());
        pessoa.setComplemento(campoComplemento.getText().trim());
        pessoa.setBairro(campoBairro.getText().trim());
        pessoa.setCidade(campoCidade.getText().trim());
        pessoa.setUf(campoUf.getText().trim());
        pessoa.setCep(campoCep.getText().trim());
        pessoa.setFone1(campoFone1.getText().trim());
        pessoa.setCelular(campoCelular.getText().trim());
        pessoa.setEmail(campoEmail.getText().trim());
        pessoa.setAtivo((String) campoAtivo.getSelectedItem());
        cliente.setPessoa(pessoa);
        try {
            cliente.setLimiteCredito(new BigDecimal(campoLimiteCredito.getText().replace(",", ".")));
        } catch (NumberFormatException ex) {
            cliente.setLimiteCredito(BigDecimal.ZERO);
        }

        try {
            if (cliente.getCodigo() == 0) {
                controller.inserir(cliente);
            } else {
                controller.atualizar(cliente);
            }
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (campoCodigo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este cliente?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(Integer.parseInt(campoCodigo.getText()));
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                limparCampos();
                carregarTabela();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
