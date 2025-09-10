import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;

public class EcraInicial extends JFrame {
    private JButton botaoRegistar, botaoLogin;

    public EcraInicial(Statement st) {
        setTitle("Tela Inicial");
        GereUtilizadores gereUtilizadores = new GereUtilizadores();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 300));

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new GridLayout(0, 1));

        botaoRegistar = new JButton("Registar");

        botaoRegistar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FormRegisto(gereUtilizadores, st, null);
                dispose(); 
            }
        });

        painelPrincipal.add(botaoRegistar);

        botaoLogin = new JButton("Login");

        botaoLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new FormLogin(gereUtilizadores,st);
            }
        });

        painelPrincipal.add(botaoLogin);

        add(painelPrincipal, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        Statement st;
        LigacaoBD ligacao = new LigacaoBD();

        if(ligacao.carregarPropriedades() && ligacao.conectar()) {
          if (ligacao != null) {

            st = ligacao.getStatement();

            new EcraInicial(st);
          }
        }
    }
}
