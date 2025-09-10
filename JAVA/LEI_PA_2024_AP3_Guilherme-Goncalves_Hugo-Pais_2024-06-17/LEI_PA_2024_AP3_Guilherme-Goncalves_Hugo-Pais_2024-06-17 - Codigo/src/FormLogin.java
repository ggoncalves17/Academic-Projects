import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;

public class FormLogin extends JFrame {
    private JTextField campoUsername;
    private JPasswordField campoPassword;
    private JButton botaoLogin, botaoMenu;
    private Utilizadores utilizadorAutenticado = null;
    private GereLogs gereLogs;

    public FormLogin(GereUtilizadores aGereUtilizadores, Statement st) {
    	
    	gereLogs = new GereLogs();
    	
        setTitle("Login");
        setLayout(new GridLayout(3, 2)); 
        setPreferredSize(new Dimension(500, 300));

        Font font = new Font("Arial", Font.PLAIN, 15);
        
        add(new JLabel("Username:")).setFont(font);
        campoUsername = new JTextField();
        campoUsername.setToolTipText("Introduza o seu username.");
        add(campoUsername);

        add(new JLabel("Password:")).setFont(font);
        campoPassword = new JPasswordField();
        campoPassword.setToolTipText("Introduza a sua password.");
        add(campoPassword);

        botaoLogin = new JButton("Login");
        botaoLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autenticar(aGereUtilizadores, st);
            }
        });
        botaoLogin.setToolTipText("Clique aqui para proceder ao Login.");

        add(botaoLogin);
        
        botaoMenu = new JButton("Voltar ao menu");
        botaoMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	dispose();
                new EcraInicial(st);
            }
        });
        botaoMenu.setToolTipText("Clique aqui para voltar ao menu.");

        add(botaoMenu);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void autenticar(GereUtilizadores aGereUtilizadores, Statement st) {
        String login = campoUsername.getText();
        String password = new String(campoPassword.getPassword());
        utilizadorAutenticado = aGereUtilizadores.verificaLoginUtilizador(st, login, password);
        if (utilizadorAutenticado != null && utilizadorAutenticado.getEstado()){
            dispose();
            new Dashboard(utilizadorAutenticado, st);
            String acao = "Login"; 
            gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas ou utilizador inválido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}