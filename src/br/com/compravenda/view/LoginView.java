package br.com.compravenda.view;

import br.com.compravenda.controller.LoginController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private LoginController controller = new LoginController();

    public LoginView() {
        super("Login - Sistema de Compra e Venda");
        montarTela();
    }

    private void montarTela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Faça o seu login", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        painel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        campoLogin = new JTextField(15);
        painel.add(campoLogin, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        painel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        campoSenha = new JPasswordField(15);
        painel.add(campoSenha, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton botaoEntrar = new JButton("Entrar");
        painel.add(botaoEntrar, gbc);

        botaoEntrar.addActionListener(e -> autenticar());
        campoSenha.addActionListener(e -> autenticar());

        add(painel);
    }

    private void autenticar() {
        String login = campoLogin.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe login e senha.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean autenticado = controller.autenticar(login, senha);
            if (autenticado) {
                new MenuView(controller.getUsuarioLogado()).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Não foi possível conectar ao banco de dados:\n" + ex.getMessage(),
                    "Erro de conexão", JOptionPane.ERROR_MESSAGE);
        }
    }
}
