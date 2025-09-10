import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class FormRegisto extends JFrame {
    private JTextField campoLogin, campoPassword, campoNome, campoEmail,campoNIF, campoTelefone, campoMorada, campoEstiloLiterario, 
   campoDataInicioAtividade, campoAreaEspecializacao, campoFormacaoAcademica;
    private JComboBox<String> comboTipoUtilizador;
    private JButton botaoUploadFoto, botaoRegistar, botaoCancelar;
    private JLabel labelFoto;
    private ImageIcon foto;
    private JPanel painelCamposEspecificos;
    private Dashboard dashboard;
    private GereNotificacoes gereNotificacoes;

    public FormRegisto(GereUtilizadores aGereUtilizadores, Statement st, Dashboard aDashboard) {
        dashboard = aDashboard;
        gereNotificacoes = new GereNotificacoes();

        setTitle("Registo de Utilizador");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 550));

        JPanel painelPrincipal = new JPanel(new GridLayout(7, 2));

        painelPrincipal.add(new JLabel("Tipo de Utilizador:"));
        comboTipoUtilizador = new JComboBox<>(new String[]{"Autor", "Gestor", "Revisor"});
        painelPrincipal.add(comboTipoUtilizador);

        painelPrincipal.add(new JLabel("Login:"));
        campoLogin = new JTextField();
        campoLogin.setToolTipText("Insira um nome para login.");
        painelPrincipal.add(campoLogin);

        painelPrincipal.add(new JLabel("Password:"));
        campoPassword = new JPasswordField();
        campoPassword.setToolTipText("Insira uma password.");
        painelPrincipal.add(campoPassword);

        painelPrincipal.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        campoNome.setToolTipText("Insira o seu nome completo.");
        painelPrincipal.add(campoNome);

        painelPrincipal.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        campoEmail.setToolTipText("Introduza o seu email.");
        painelPrincipal.add(campoEmail);

        painelPrincipal.add(new JLabel("Foto:"));
        labelFoto = new JLabel(new ImageIcon("default.jpg"));
        painelPrincipal.add(labelFoto);

        botaoUploadFoto = new JButton("Upload Foto");
        botaoUploadFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int resultado = fileChooser.showOpenDialog(null);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                	ImageIcon imagemOriginal = new ImageIcon(fileChooser.getSelectedFile().getPath());
                    
                    Image imagemRedimensionada = imagemOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

                    foto = new ImageIcon(imagemRedimensionada);
                    labelFoto.setIcon(foto);
                }
            }
        });
        painelPrincipal.add(botaoUploadFoto);

        add(painelPrincipal, BorderLayout.NORTH);

        painelCamposEspecificos = new JPanel(new GridLayout(6, 2));
        add(painelCamposEspecificos, BorderLayout.CENTER);

        botaoRegistar = new JButton("Registar Utilizador");
        botaoRegistar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registarUtilizador(aGereUtilizadores, st);
            }
        });
        botaoRegistar.setToolTipText("Clique aqui para proceder ao Registo.");

        botaoCancelar = new JButton("Voltar ao menu");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (dashboard != null) {
                    dashboard.voltarAoMenu();
                } else {
                    new EcraInicial(st);
                }
            }
        });
        botaoCancelar.setToolTipText("Clique aqui para cancelar o processo.");

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(botaoRegistar);
        painelBotoes.add(botaoCancelar);
        add(painelBotoes, BorderLayout.SOUTH);

        comboTipoUtilizador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verificarCamposTipoUtilizador();
            }
        });

        verificarCamposTipoUtilizador();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void verificarCamposTipoUtilizador() {
        painelCamposEspecificos.removeAll();

        String tipoUtilizador = (String) comboTipoUtilizador.getSelectedItem();
        boolean gestor = tipoUtilizador.equals("Gestor");
        boolean revisor = tipoUtilizador.equals("Revisor");
        boolean autor = tipoUtilizador.equals("Autor");

        if (!gestor) {
            painelCamposEspecificos.add(new JLabel("NIF:"));
            campoNIF = new JTextField();
            painelCamposEspecificos.add(campoNIF);

            painelCamposEspecificos.add(new JLabel("Telefone:"));
            campoTelefone = new JTextField();
            painelCamposEspecificos.add(campoTelefone);

            painelCamposEspecificos.add(new JLabel("Morada:"));
            campoMorada = new JTextField();
            painelCamposEspecificos.add(campoMorada);
        }

        if (autor) {
            painelCamposEspecificos.add(new JLabel("Estilo Literário:"));
            campoEstiloLiterario = new JTextField();
            painelCamposEspecificos.add(campoEstiloLiterario);

            painelCamposEspecificos.add(new JLabel("Data Início Atividade (AAAA-MM-dd):"));
            campoDataInicioAtividade = new JTextField();
            painelCamposEspecificos.add(campoDataInicioAtividade);
        } else if (revisor) {
            painelCamposEspecificos.add(new JLabel("Área de Especialização:"));
            campoAreaEspecializacao = new JTextField();
            painelCamposEspecificos.add(campoAreaEspecializacao);

            painelCamposEspecificos.add(new JLabel("Formação Académica:"));
            campoFormacaoAcademica = new JTextField();
            painelCamposEspecificos.add(campoFormacaoAcademica);
        }

        revalidate();
        repaint();
    }

    private void registarUtilizador(GereUtilizadores aGereUtilizadores, Statement st) {
        String login = campoLogin.getText();
        String password = campoPassword.getText();
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String tipoUtilizador = (String) comboTipoUtilizador.getSelectedItem();
        String nif = campoNIF.getText();
        String telefone = campoTelefone.getText();
        String morada = campoMorada.getText();
        String estiloLiterario = campoEstiloLiterario.getText();
        String dataInicioAtividade = campoDataInicioAtividade.getText();
        String areaEspecializacao = campoAreaEspecializacao != null ? campoAreaEspecializacao.getText() : "";
        String formacaoAcademica = campoFormacaoAcademica != null ? campoFormacaoAcademica.getText() : "";

        int tipo = tipoUtilizador.equals("Gestor") ? 1 : (tipoUtilizador.equals("Revisor") ? 2 : 3);
        boolean estado = tipo == 1;

        int NifInt = -1;
        int TelefoneInt = -1;
        LocalDate dataAtual = LocalDate.now();
        Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);

        // VERIFICAÇÕES PARA OS CAMPOS DE TODOS OS UTILIZADORES (GERAIS)

        if (login.isEmpty() || password.isEmpty() || nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos obrigatórios!",
                    "Erro de Registo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (aGereUtilizadores.verificaLogin(st, login)) {
            JOptionPane.showMessageDialog(this,
                    "Login já existente. Por favor, escolha outro login.",
                    "Erro de Registo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (aGereUtilizadores.verificaFormatoEmail(email) == false) {
            JOptionPane.showMessageDialog(this,
                    "Formato de email inválido. Por favor, verifique o email colocado.",
                    "Erro de Registo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (aGereUtilizadores.verificaEmail(st, email)) {
            JOptionPane.showMessageDialog(this,
                    "Email já registado. Por favor, utilize outro email.",
                    "Erro de Registo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (foto == null) {
            foto = new ImageIcon("default.jpg");
        }


        // VERIFICAÇÕES PARA OS CAMPOS COMUNS DO REVISOR E DO AUTOR ---------------------------------------------

        if (tipo == 2 || tipo == 3) {
            if (nif.isEmpty() || telefone.isEmpty() || morada.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, preencha todos os campos obrigatórios!",
                        "Erro de Registo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            NifInt = Integer.parseInt(nif);
            TelefoneInt = Integer.parseInt(telefone);

            if (aGereUtilizadores.verificaFormatoNIF(NifInt) == false) {
                JOptionPane.showMessageDialog(this,
                        "O NIF nao se encontra no formato correto! Por favor, tente outro.",
                        "Erro de Registo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (aGereUtilizadores.verificaNIF(st, NifInt)) {
                JOptionPane.showMessageDialog(this,
                        "O NIF ja se encontra registado! Por favor, tente outro.",
                        "Erro de Registo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (aGereUtilizadores.verificaFormatoTelefone(TelefoneInt) == false) {
                JOptionPane.showMessageDialog(this,
                        "O numero de telefone introduzido nao e valido! Por favor, tente outro.",
                        "Erro de Registo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }	
        }

        // VERIFICAÇÕES APENAS PARA O REVISOR -------------------------------------------------------------

        if (tipo == 2) {
            if (areaEspecializacao.isEmpty() || formacaoAcademica.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, preencha todos os campos obrigatórios!",
                        "Erro de Registo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else if (tipo == 3) {
            if (estiloLiterario.isEmpty() || dataInicioAtividade.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, preencha todos os campos obrigatórios!",
                        "Erro de Registo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // SE AS VERIFICAÇÕES FOREM TODAS VÁLIDAS, É FEITO O REGISTO ---------------------------------------
        
        if (aGereUtilizadores.adicionarInfoUtilizadorBD(st, login, password, nome, estado, email, tipo)) {
            if (tipo == 2) { // Revisor
                if (aGereUtilizadores.adicionarInfoRevisorBD(st, NifInt, morada, TelefoneInt, areaEspecializacao, formacaoAcademica, aGereUtilizadores.ultimoIdUtilizador(st))) {
                    int tipoUserDestino = 1;
                    int idUser = aGereUtilizadores.ultimoIdUtilizador(st);
                    String mensagem = "Utilizador " + nome + " pendente de aprovação";
                    enviarEmailRegisto(email);

                    if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)) {
                    JOptionPane.showMessageDialog(this,
                            "Revisor registado com sucesso!",
                            "Registo Concluído",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } 
            else if (tipo == 3) { // Autor
                if (aGereUtilizadores.adicionarInfoAutorBD(st, NifInt, estiloLiterario, dataInicioAtividade, TelefoneInt, morada, aGereUtilizadores.ultimoIdUtilizador(st))) {
                    int tipoUserDestino = 1;
                    int idUser = aGereUtilizadores.ultimoIdUtilizador(st);
                    String mensagem = "Utilizador " + nome + " pendente de aprovação";
                    enviarEmailRegisto(email);

                    if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)) {
                        JOptionPane.showMessageDialog(this,
                                "Autor registado com sucesso!",
                                "Registo Concluído",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } 
            else { // Gestor
                enviarEmailRegisto(email);

                JOptionPane.showMessageDialog(this,
                        "Gestor registado com sucesso!",
                        "Registo Concluído",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
            if (dashboard != null) {
                dashboard.voltarAoMenu();
            } else {
                new EcraInicial(st);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao registar utilizador. Por favor, tente novamente.",
                    "Erro de Registo",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    
    private void enviarEmailRegisto(String aEmail) {
    	  String assunto = "Registo Concluído";
    	  String conteudo = "Registo realizado com sucesso!";
    	  String username = "a2022156457@alunos.estgoh.ipc.pt";
    	  String password = "";

    	  Properties properties = System.getProperties();
    	  properties.setProperty("mail.transport.protocol", "smtp");
    	  properties.setProperty("mail.host", "smtp.estgoh.ipc.pt");
    	  properties.setProperty("mail.smtp.auth", "true");
    	  properties.setProperty("mail.smtp.port", "465");
    	  properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    	  Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
    	      protected PasswordAuthentication getPasswordAuthentication() {
    	          return new PasswordAuthentication(username, password);
    	      }
    	  });

    	  try {
    	      MimeMessage message = new MimeMessage(session);
    	      message.setFrom(new InternetAddress(username));
    	      message.addRecipient(Message.RecipientType.TO, new InternetAddress(aEmail));
    	      message.setSubject(assunto);
    	      message.setText(conteudo);

    	      Transport.send(message);

    	  } catch (MessagingException mex) {
    	      mex.printStackTrace();
    	  }
    }
}
