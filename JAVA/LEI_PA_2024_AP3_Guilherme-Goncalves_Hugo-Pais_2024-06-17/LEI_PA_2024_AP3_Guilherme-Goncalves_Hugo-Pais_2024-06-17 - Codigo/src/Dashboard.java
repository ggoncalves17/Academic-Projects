import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.awt.print.*;

public class Dashboard extends JFrame {
    private JTextField fieldPassword, fieldNome, fieldEmail, fieldTelefone, fieldMorada;
    
    private JPanel painelGeralDevolvido, cards, painelMenu, painelEditarInfoPessoal, painelAlterarDadosUtilizador, painelConsultarNotificacoes, painelPedidosRevisaoPendentes, 
    painelRevisao, painelAtribuirNovoRevisor, painelConcluirProcessoRevisao, painelLogs, painelSolicitarRevisaoObra, painelProcessosPagamento, painelConsultarProcessosRevisao, painelConsultarNotificacoesRevisorAutor,
    painelConsultarPedidosRegisto, painelConsultarPedidosRevisao, painelAprovarRejeitarPedidosRevisao, painelListarRevisores,
painelArquivarRevisao, painelAtivaDesativaUtilizador, painelConsultarLogsUtilizador, painelConsultarLogsGerais, painelMeusProcessosRevisao;
    private GereNotificacoes gereNotificacoes;
    private GereUtilizadores gereUtilizadores;
    private GereRevisoes gereRevisoes;
    private GereObras gereObras;
    private GereLogs gereLogs;
    private Statement st;
    private Utilizadores utilizadorAutenticado;
    private CardLayout cardLayout;
    private int idUtilizador = 0;
    private Paginacao paginacaoGeral;

    public Dashboard(Utilizadores aUtilizadorAutenticado, Statement aSt) {
        gereNotificacoes = new GereNotificacoes();
        gereUtilizadores = new GereUtilizadores();
        gereRevisoes = new GereRevisoes();
        gereObras = new GereObras();
        gereLogs = new GereLogs();
        utilizadorAutenticado = aUtilizadorAutenticado;
        st = aSt;

        setTitle("Dashboard");
        setPreferredSize(new Dimension(500, 600));

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        painelMenu = criaPainelMenu();
        painelEditarInfoPessoal = criaPainelEditarInfoPessoal(utilizadorAutenticado.getId());
        painelConsultarPedidosRegisto = criaPainelConsultarPedidosRegisto();
        painelAlterarDadosUtilizador = criaPainelAlterarDadosUtilizador();
        painelConsultarNotificacoes = criaPainelConsultarNotificacoes();
        painelRevisao = criaPainelRevisao();
        painelConsultarPedidosRevisao = criaPainelConsultarPedidosRevisao();
        painelAprovarRejeitarPedidosRevisao = criaPainelAprovarRejeitarPedidosRevisao();
        painelListarRevisores = criaPainelListarRevisores(idUtilizador);
        painelAtribuirNovoRevisor = criaPainelAtribuirNovoRevisor();
        painelArquivarRevisao = criaPainelArquivarRevisao();
        painelAtivaDesativaUtilizador = criaPainelAtivaDesativaUtilizador();
        painelLogs = criaPainelLogs();
        painelConsultarLogsUtilizador = criaPainelConsultarLogsUtilizador();
        painelConsultarLogsGerais = criaPainelConsultarLogsGerais();
        painelPedidosRevisaoPendentes = criaPainelPedidosRevisaoPendentes();
        painelMeusProcessosRevisao = criaPainelMeusProcessosRevisao();
        painelConcluirProcessoRevisao = criaPainelConcluirProcessoRevisao();     
        painelSolicitarRevisaoObra = criaPainelSolicitarRevisaoObra();
        painelConsultarProcessosRevisao = criaPainelConsultarProcessosRevisao();
        painelProcessosPagamento = criaPainelProcessosPagamento();
        painelConsultarNotificacoesRevisorAutor = criaPainelConsultarNotificacoesRevisorAutor();

        cards.add(painelMenu, "Menu");
        cards.add(painelEditarInfoPessoal, "cardEditarInfoPessoal");
        cards.add(painelAlterarDadosUtilizador, "cardAlterarDadosUtilizador");
        cards.add(painelConsultarNotificacoes, "cardConsultarNotificacoes");
        cards.add(painelConsultarPedidosRegisto, "cardConsultarPedidosRegisto");
        cards.add(painelRevisao, "cardRevisao");
        cards.add(painelConsultarPedidosRevisao, "cardConsultarPedidosRevisao");
        cards.add(painelAprovarRejeitarPedidosRevisao, "cardAprovarRejeitarPedidosRevisao");
        cards.add(painelListarRevisores, "cardListarRevisores");
        cards.add(painelAtribuirNovoRevisor, "cardAtribuirNovoRevisor");
        cards.add(painelArquivarRevisao, "cardArquivarRevisao");
        cards.add(painelAtivaDesativaUtilizador, "cardAtivaDesativaUtilizador");
        cards.add(painelLogs, "cardMenuLogs");
        cards.add(painelConsultarLogsUtilizador, "cardConsultarLogsUtilizador");
        cards.add(painelConsultarLogsGerais, "cardConsultarLogsGerais");
        cards.add(painelPedidosRevisaoPendentes, "cardPedidosRevisaoPendentes");
        cards.add(painelMeusProcessosRevisao, "cardMeusProcessosRevisao");
        cards.add(painelConcluirProcessoRevisao, "cardConcluirProcessoRevisao");
        cards.add(painelSolicitarRevisaoObra, "cardSolicitarRevisaoObra");
        cards.add(painelConsultarProcessosRevisao, "cardConsultarProcessosRevisao");
        cards.add(painelProcessosPagamento, "cardProcessosPagamento");      
        cards.add(painelConsultarNotificacoesRevisorAutor, "cardConsultarNotificacoesRevisorAutor");

        verificarNotificacoes(utilizadorAutenticado);

        
        getContentPane().add(cards, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }

    private JPanel criaPainelMenu() {
        painelGeralDevolvido = new JPanel(new GridLayout(0, 1));
        
        // OPÇÃO GERAL ----------------------------------------------------------------------------

        JButton botaoEditarDadosPessoais = new JButton("Editar informação pessoal");
        botaoEditarDadosPessoais.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String acao = "Editar Dados Pessoais"; 
                gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                cardLayout.show(cards, "cardEditarInfoPessoal");
            }
        });
        botaoEditarDadosPessoais.setToolTipText("Clique aqui para editar os seus dados pessoais.");
        painelGeralDevolvido.add(botaoEditarDadosPessoais);

        // OPÇÕES DOS GESTORES ---------------------------------------------------------------------
        
        if (utilizadorAutenticado.getTipo() == 1) {
            JButton botaoCriarNovoUtilizador = new JButton("Criar novo Utilizador");
            botaoCriarNovoUtilizador.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new FormRegisto(gereUtilizadores, st, Dashboard.this);
                }
            });
            botaoCriarNovoUtilizador.setToolTipText("Clique aqui para criar um novo utilizador.");
            painelGeralDevolvido.add(botaoCriarNovoUtilizador);

            JButton botaoAlterarDadosUtilizador = new JButton("Alterar Dados de um Utilizador");
            botaoAlterarDadosUtilizador.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Alterar Dados de Utilizador"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardAlterarDadosUtilizador");
                }
            });
            botaoAlterarDadosUtilizador.setToolTipText("Clique aqui para alterar dados de utilizador.");
            painelGeralDevolvido.add(botaoAlterarDadosUtilizador);

            JButton botaoConsultarNotificacoes = new JButton("Consultar Notificações");
            botaoConsultarNotificacoes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Notificacoes"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardConsultarNotificacoes");
                }
            });
            botaoConsultarNotificacoes.setToolTipText("Clique aqui para consultar notificações.");
            painelGeralDevolvido.add(botaoConsultarNotificacoes);

            JButton botaoConsultarPedidosRegisto = new JButton("Consultar Pedidos Registo");
            botaoConsultarPedidosRegisto.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Pedidos Registo"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardConsultarPedidosRegisto");
                }
            });
            botaoConsultarPedidosRegisto.setToolTipText("Clique aqui para consultar pedidos de registo.");
            painelGeralDevolvido.add(botaoConsultarPedidosRegisto);

            JButton botaoConsultarPedidosRevisao = new JButton("Consultar Pedidos Revisão");
            botaoConsultarPedidosRevisao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Pedidos Revisao"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardRevisao");
                }
            });
            botaoConsultarPedidosRevisao.setToolTipText("Clique aqui para consultar pedidos de revisão.");
            painelGeralDevolvido.add(botaoConsultarPedidosRevisao);
            
            JButton botaoImprimirExtrato = new JButton("Imprimir Extrato de Revisão");
            botaoImprimirExtrato.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                    imprimirExtratoRevisao();
                }
            });
            botaoImprimirExtrato.setToolTipText("Clique aqui para imprimir extrato de revisão.");
            painelGeralDevolvido.add(botaoImprimirExtrato);

            JButton botaoArquivarProcessosRevisao = new JButton("Arquivar Processos de Revisão");
            botaoArquivarProcessosRevisao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cards, "cardArquivarRevisao");
                }
            });
            botaoArquivarProcessosRevisao.setToolTipText("Clique aqui para arquivar processo de revisão.");
            painelGeralDevolvido.add(botaoArquivarProcessosRevisao);

            JButton botaoAtivarInativarContas = new JButton("Inativar/Ativar Contas de Utilizador");
            botaoAtivarInativarContas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String acao = "Ativar / Desativar Contas de Utilizador"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardAtivaDesativaUtilizador");               	
                }
            });
            botaoAtivarInativarContas.setToolTipText("Clique aqui para ativar/desativar utilizador.");
            painelGeralDevolvido.add(botaoAtivarInativarContas);

            JButton botaoConsultarLogs = new JButton("Consultar Logs");
            botaoConsultarLogs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Logs"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardMenuLogs");               	
                }
            });
            botaoConsultarLogs.setToolTipText("Clique aqui para consultar logs.");
            painelGeralDevolvido.add(botaoConsultarLogs);
        }

        // OPÇÕES DO REVISOR -----------------------------------------------------------------------------
        
        if (utilizadorAutenticado.getTipo() == 2) {
            JButton botaoConsultarPedidosRevisaoPendentes = new JButton("Consultar Pedidos Revisão Pendentes");
            botaoConsultarPedidosRevisaoPendentes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Pedidos Revisao Pendentes"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardPedidosRevisaoPendentes");
                }
            });
            botaoConsultarPedidosRevisaoPendentes.setToolTipText("Clique aqui para consultar pedidos de revisão pendentes.");
            painelGeralDevolvido.add(botaoConsultarPedidosRevisaoPendentes);

            JButton botaoConsultarMeusProcessosRevisao = new JButton("Consultar os Meus Processos Revisao");
            botaoConsultarMeusProcessosRevisao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar os Meus Processos Revisao"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardMeusProcessosRevisao");
                }
            });
            botaoConsultarMeusProcessosRevisao.setToolTipText("Clique aqui para consultar os seus processos de revisao.");
            painelGeralDevolvido.add(botaoConsultarMeusProcessosRevisao);

            JButton botaoConcluirProcessoRevisao = new JButton("Concluir Processo de Revisão");
            botaoConcluirProcessoRevisao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Concluir Pedidos de Revisao"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardConcluirProcessoRevisao");
                }
            });
            botaoConcluirProcessoRevisao.setToolTipText("Clique aqui concluir processos de revisao.");
            painelGeralDevolvido.add(botaoConcluirProcessoRevisao);

            JButton botaoConsultarNotificacoes = new JButton("Consultar Notificações");
            botaoConsultarNotificacoes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Notificacoes"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardConsultarNotificacoesRevisorAutor");
                }
            });
            botaoConsultarNotificacoes.setToolTipText("Clique aqui para consultar notificações.");
            painelGeralDevolvido.add(botaoConsultarNotificacoes);
        }

        // OPÇÕES DO AUTOR ------------------------------------------------------------------
        
        if (utilizadorAutenticado.getTipo() == 3) {
            JButton botaoSolicitarRevisaoObra = new JButton("Solicitar Revisão de Obra");
            botaoSolicitarRevisaoObra.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Solicitar Revisao Obra"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardSolicitarRevisaoObra");
                }
            });
            botaoSolicitarRevisaoObra.setToolTipText("Clique aqui para solicitar uma revisão a uma obra.");
            painelGeralDevolvido.add(botaoSolicitarRevisaoObra);

            JButton botaoConsultarProcessosRevisao = new JButton("Consultar Processos de Revisão");
            botaoConsultarProcessosRevisao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Processos Revisao"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardConsultarProcessosRevisao");
                }
            });
            botaoConsultarProcessosRevisao.setToolTipText("Clique aqui para consultar os processos de revisão.");
            painelGeralDevolvido.add(botaoConsultarProcessosRevisao);

            JButton botaoProcessosPagamento = new JButton("Processos a Pagamento");
            botaoProcessosPagamento.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Processos a Pagamento"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardProcessosPagamento");
                }
            });
            botaoProcessosPagamento.setToolTipText("Clique aqui para verficar os processos a pagamento.");
            painelGeralDevolvido.add(botaoProcessosPagamento);

            JButton botaoConsultarNotificacoes = new JButton("Consultar Notificações");
            botaoConsultarNotificacoes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	String acao = "Consultar Notificacoes"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    cardLayout.show(cards, "cardConsultarNotificacoesRevisorAutor");
                }
            });
            botaoConsultarNotificacoes.setToolTipText("Clique aqui para consultar notificações.");
            painelGeralDevolvido.add(botaoConsultarNotificacoes);
        }

        JButton botaoLogout = new JButton("Logout");
        botaoLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(Dashboard.this,
                        "Tem a certeza que deseja sair?",
                        "Confirmação de Logout",
                        JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    dispose();
                    new EcraInicial(st);
                    
                    String acao = "Logout"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                }
            }
        });
        botaoLogout.setToolTipText("Clique aqui para fazer sair da sua conta.");
        painelGeralDevolvido.add(botaoLogout);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelEditarInfoPessoal(int aIdUtilizador) {
        painelGeralDevolvido = new JPanel(new GridLayout(0, 2));

        Utilizadores utilizador = gereUtilizadores.pesquisaPorId(st, aIdUtilizador);

        painelGeralDevolvido.add(new JLabel("Nome:"));
        fieldNome = new JTextField(20);
        painelGeralDevolvido.add(fieldNome);

        painelGeralDevolvido.add(new JLabel("Password:"));
        fieldPassword = new JPasswordField(20);
        painelGeralDevolvido.add(fieldPassword);

        painelGeralDevolvido.add(new JLabel("Email:"));
        fieldEmail = new JTextField(20);
        painelGeralDevolvido.add(fieldEmail);

        if (utilizador.getTipo() != 1) {
            painelGeralDevolvido.add(new JLabel("Telefone:"));
            fieldTelefone = new JTextField(20);
            painelGeralDevolvido.add(fieldTelefone);

            painelGeralDevolvido.add(new JLabel("Morada:"));
            fieldMorada = new JTextField(20);
            painelGeralDevolvido.add(fieldMorada);
        } else {
            fieldTelefone = new JTextField("");
            fieldMorada = new JTextField("");
        }

        JButton botaoGuardar = new JButton("Guardar Alterações");
        botaoGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = fieldPassword.getText();
                String nome = fieldNome.getText();
                String email = fieldEmail.getText();
                String telefone = fieldTelefone.getText();
                String morada = fieldMorada.getText();
                int TelefoneInt = -1;

                if (!nome.isEmpty()) {
                    if (gereUtilizadores.atualizaNome(st, aIdUtilizador, nome))
                        utilizador.setNome(nome);
                }

                if (!password.isEmpty()) {
                    if (gereUtilizadores.atualizaPassword(st, aIdUtilizador, password))
                        utilizador.setPassword(password);
                }

                if (!email.isEmpty()) {

                    if (gereUtilizadores.verificaFormatoEmail(email) == false) {
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Formato de email inválido. Por favor, verifique o email colocado.",
                                "Erro de alteração de dados!",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (gereUtilizadores.verificaEmail(st, email)) {
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Email já registado. Por favor, utilize outro email.",
                                "Erro de alteração de dados!",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (gereUtilizadores.atualizaEmail(st, aIdUtilizador, email))
                        utilizador.setEmail(email);
                }

                // VERIFICAÇÕES PARA OS CAMPOS COMUNS DO REVISOR E DO AUTOR -----------------------------------
                
                if (utilizador.getTipo() == 2) {
                    if (!telefone.isEmpty()) {
                        TelefoneInt = Integer.parseInt(telefone);

                        if (gereUtilizadores.verificaFormatoTelefone(TelefoneInt) == false) {
                            JOptionPane.showMessageDialog(painelGeralDevolvido,
                                    "O numero de telefone introduzido nao e valido! Por favor, tente outro.",
                                    "Erro de Registo",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        gereUtilizadores.atualizaTelefoneRevisores(st, aIdUtilizador, TelefoneInt);
                    }

                    if (!morada.isEmpty()) {
                        gereUtilizadores.atualizaMoradaRevisores(st, aIdUtilizador, morada);
                    }
                } else if (utilizador.getTipo() == 3) {

                    if (!telefone.isEmpty()) {
                        TelefoneInt = Integer.parseInt(telefone);

                        if (gereUtilizadores.verificaFormatoTelefone(TelefoneInt) == false) {
                            JOptionPane.showMessageDialog(painelGeralDevolvido,
                                    "O numero de telefone introduzido nao e valido! Por favor, tente outro.",
                                    "Erro de Registo",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        gereUtilizadores.atualizaTelefoneAutores(st, aIdUtilizador, TelefoneInt);
                    }

                    if (!morada.isEmpty()) {
                        gereUtilizadores.atualizaMoradaAutores(st, aIdUtilizador, morada);
                    }
                }

                JOptionPane.showMessageDialog(painelGeralDevolvido, "Informações pessoais editadas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                 
                cards.remove(painelAlterarDadosUtilizador);
                JPanel painelAlterarDadosUtilizador = criaPainelAlterarDadosUtilizador();
                cards.add(painelAlterarDadosUtilizador, "cardAlterarDadosUtilizador");
                
                cardLayout.show(cards, "Menu");

                fieldNome.setText("");
                fieldPassword.setText("");
                fieldEmail.setText("");
                fieldTelefone.setText("");
                fieldMorada.setText("");
                
            }
        });
        painelGeralDevolvido.add(botaoGuardar);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelGeralDevolvido.add(botaoCancelar);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelAlterarDadosUtilizador() {
    	painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagemUtilizadores = gereUtilizadores.listarUtilizadores(st);
        paginacaoGeral = new Paginacao(listagemUtilizadores, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoEditar = new JButton("Editar utilizador selecionado");
        botaoEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JPanel painelEditarInfoSelecionado = criaPainelEditarInfoPessoal(idUtilizador);
                cards.add(painelEditarInfoSelecionado, "cardEditarInfoSelecionado");
                cardLayout.show(cards, "cardEditarInfoSelecionado");
            }
        });
        painelBotoes.add(botaoEditar);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelBotoes.add(botaoCancelar, BorderLayout.SOUTH);
        painelGeralDevolvido.add(painelBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    public void setIdListagensSelecionado(String aId) {
        idUtilizador = Integer.parseInt(aId);
    }
    
    private JPanel criaPainelConsultarNotificacoes() {
        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagemNotificacoes = gereNotificacoes.listarNotificacoesGestores(st); 
;
        Paginacao paginacao = new Paginacao(listagemNotificacoes, this);

        painelGeralDevolvido.add(paginacao, BorderLayout.CENTER);

        JButton botaoOK = new JButton("OK");
        botaoOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gereNotificacoes.darVistaNotificacoesGestores(st);
                
                cards.remove(painelConsultarNotificacoes);
            	JPanel painelConsultarNotificacoes = criaPainelConsultarNotificacoes();
                cards.add(painelConsultarNotificacoes, "cardConsultarNotificacoes");
                cardLayout.show(cards, "Menu");
            }
        });
        painelGeralDevolvido.add(botaoOK, BorderLayout.SOUTH);


        return painelGeralDevolvido;
    }

    private JPanel criaPainelConsultarPedidosRegisto() {
        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereUtilizadores.listarUtilizadoresPendentes(st);
        
        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoEditar = new JButton("Ativar utilizador selecionado");
        botaoEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(gereUtilizadores.aprovarUtilizadoresPendentes(st, idUtilizador)) {
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Utilizador ativado com sucesso!",
                            "Pedido Registo",
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    cards.remove(painelConsultarPedidosRegisto);
                    painelConsultarPedidosRegisto = criaPainelConsultarPedidosRegisto();
                    cards.add(painelConsultarPedidosRegisto, "cardConsultarPedidosRegisto");
                    cardLayout.show(cards, "cardConsultarPedidosRegisto");
                }
                else {
                    JOptionPane.showMessageDialog(painelConsultarPedidosRegisto,
                            "Erro ao ativar utilizador!",
                            "Pedido Registo",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        painelBotoes.add(botaoEditar);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelBotoes.add(botaoCancelar, BorderLayout.SOUTH);
        painelGeralDevolvido.add(painelBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelRevisao() {
        painelGeralDevolvido = new JPanel(new GridLayout(4, 1));

        JButton botaoConsultarTodosPedidos = new JButton("Consultar todos os pedidos");
        botaoConsultarTodosPedidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                cardLayout.show(cards, "cardConsultarPedidosRevisao");
            }
        });
        painelGeralDevolvido.add(botaoConsultarTodosPedidos);

        JButton botaoAprovarRejeitarPedidos = new JButton("Aprovar / Rejeitar Pedidos Pendentes");
        botaoAprovarRejeitarPedidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardAprovarRejeitarPedidosRevisao");
            }
        });
        painelGeralDevolvido.add(botaoAprovarRejeitarPedidos);

        JButton botaoAtribuirNovoRevisor = new JButton("Atribuir novo revisor");
        botaoAtribuirNovoRevisor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardAtribuirNovoRevisor");
            }
        });
        painelGeralDevolvido.add(botaoAtribuirNovoRevisor);

        JButton botaoVoltar = new JButton("Voltar ao menu anterior");
        botaoVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelGeralDevolvido.add(botaoVoltar);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelConsultarPedidosRevisao() {
  	  painelGeralDevolvido = new JPanel(new BorderLayout());
  	
  	  String listagem = gereRevisoes.listarPedidosRevisao(st, 1);
  	  String[] linhas = listagem.split("\n");
  	  JPanel listaPainel = new JPanel();
  	  listaPainel.setLayout(new BoxLayout(listaPainel, BoxLayout.Y_AXIS));
  	
  	  for (String linha : linhas) {
  	      JPanel linhaPainel = new JPanel();
  	      linhaPainel.setLayout(new BoxLayout(linhaPainel, BoxLayout.Y_AXIS));
  	
  	      JLabel labelLinha = new JLabel(linha);
  	      linhaPainel.add(labelLinha);
  	
  	      JLabel labelObservacao = new JLabel("Observação:");
  	      linhaPainel.add(labelObservacao);
  	
  	      JTextField campoObservacao = new JTextField();
  	      linhaPainel.add(campoObservacao);
  	
  	      JButton botaoGuardar = new JButton("Guardar Observação");
  	      botaoGuardar.addActionListener(new ActionListener() {
  	          public void actionPerformed(ActionEvent e) {
  	              String observacao = campoObservacao.getText();
  	              if (observacao != null && !observacao.isEmpty()) {
  	                  String idRevisaoStr = getIdRevisaoLinha(linha);
  	                  try {
  	                      int idRevisao = Integer.parseInt(idRevisaoStr);
  	                      if (gereRevisoes.adicionarObservacao(st, idRevisao, observacao)) {
  	                          JOptionPane.showMessageDialog(painelGeralDevolvido,
  	                                  "Observação guardada com sucesso!",
  	                                  "Sucesso",
  	                                  JOptionPane.INFORMATION_MESSAGE);
  	                      } else {
  	                          JOptionPane.showMessageDialog(painelGeralDevolvido,
  	                                  "Erro ao guardar observação!",
  	                                  "Erro",
  	                                  JOptionPane.ERROR_MESSAGE);
  	                      }
  	                  } catch (NumberFormatException ex) {
  	                      JOptionPane.showMessageDialog(painelGeralDevolvido,
  	                              "ID de revisão inválido!",
  	                              "Erro",
  	                              JOptionPane.ERROR_MESSAGE);
  	                  }
  	              } else {
  	                  JOptionPane.showMessageDialog(painelGeralDevolvido,
  	                          "Observação não pode ser vazia!",
  	                          "Erro",
  	                          JOptionPane.ERROR_MESSAGE);
  	              }
  	          }
  	      });
  	      linhaPainel.add(botaoGuardar);
  	
  	      linhaPainel.add(Box.createRigidArea(new Dimension(0, 20)));
  	
  	      listaPainel.add(linhaPainel);
  	  }
  	
  	  JScrollPane painelScroll = new JScrollPane(listaPainel);
  	painelGeralDevolvido.add(painelScroll, BorderLayout.CENTER);
  	
  	  JButton botaoCancelar = new JButton("Cancelar");
  	  botaoCancelar.addActionListener(new ActionListener() {
  	      public void actionPerformed(ActionEvent e) {
  	          cardLayout.show(cards, "cardRevisao");
  	      }
  	  });
  	
  	painelGeralDevolvido.add(botaoCancelar, BorderLayout.SOUTH);
  	
  	  return painelGeralDevolvido;
  	}

    private JPanel criaPainelAprovarRejeitarPedidosRevisao() {
        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 2, 1);
        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoesAprovarRejeitar = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoAprovar = new JButton("Aprovar revisão selecionada");
        botaoAprovar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gereRevisoes.alteraEstadoRevisao(st, idUtilizador, 2)) {
                    atualizarPedidosRevisao();

                    JPanel painelListarRevisores = criaPainelListarRevisores(idUtilizador);
                    cards.add(painelListarRevisores, "cardListarRevisores");
                    cardLayout.show(cards, "cardListarRevisores");

                } 
            }
        });
        painelBotoesAprovarRejeitar.add(botaoAprovar);

        JButton botaoRejeitar = new JButton("Rejeitar revisão selecionada");
        botaoRejeitar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gereRevisoes.alteraEstadoRevisao(st, idUtilizador, 7)) {
                    LocalDate dataAtual = LocalDate.now();
                    Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
                    String mensagem = "O seu pedido de revisao " + idUtilizador + " foi rejeitado";
                    int tipoUserDestino = 3;
                    int idUser = gereRevisoes.obterIdAutorPorRevisao(st, tipoUserDestino, idUtilizador);
                    if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)){
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Pedido rejeitado com sucesso!",
                                "Pedido Revisao",
                                JOptionPane.INFORMATION_MESSAGE);
                        atualizarPedidosRevisao();
                    }
                } else {
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Erro ao rejeitar pedido!",
                            "Pedido Revisao",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        painelBotoesAprovarRejeitar.add(botaoRejeitar);

        JPanel painelBotaoCancelar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardRevisao");
            }
        });
        painelBotaoCancelar.add(botaoCancelar);

        JPanel painelPrincipalBotoes = new JPanel();
        painelPrincipalBotoes.setLayout(new BoxLayout(painelPrincipalBotoes, BoxLayout.Y_AXIS));
        painelPrincipalBotoes.add(painelBotoesAprovarRejeitar);
        painelPrincipalBotoes.add(painelBotaoCancelar);

        painelGeralDevolvido.add(painelPrincipalBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelListarRevisores(int aIdRevisao) {
        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagemUtilizadores = gereUtilizadores.pesquisaPorTipo(st, 2);
        paginacaoGeral = new Paginacao(listagemUtilizadores, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotaoAtribuirRevisor = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoAtribuir = new JButton("Atribuir revisor selecionado");
        botaoAtribuir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gereRevisoes.atribuirUtilizadorRevisao(st, idUtilizador, aIdRevisao)) {            		            	
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Revisor atribuido com sucesso!",
                            "Pedido Revisao",
                            JOptionPane.INFORMATION_MESSAGE);
                    atualizarPedidosRevisao();
                } else {
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Erro ao atribuir revisor!",
                            "Pedido Revisao",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        painelBotaoAtribuirRevisor.add(botaoAtribuir);

        painelGeralDevolvido.add(painelBotaoAtribuirRevisor, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private void atualizarPedidosRevisao() {
        cards.remove(painelAprovarRejeitarPedidosRevisao);
        painelAprovarRejeitarPedidosRevisao = criaPainelAprovarRejeitarPedidosRevisao();
        cards.add(painelAprovarRejeitarPedidosRevisao, "cardAprovarRejeitarPedidosRevisao");
        cardLayout.show(cards, "cardAprovarRejeitarPedidosRevisao");
    }

    private JPanel criaPainelAtribuirNovoRevisor() {

        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereRevisoes.listarPedidosRevisaoEstados(st);

        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoAtribuir = new JButton("Atribuir novo revisor a revisao selecionada");
        botaoAtribuir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JPanel painelListarRevisoresDisponiveis = criaPainelListarRevisoresDisponiveis(idUtilizador);
                cards.add(painelListarRevisoresDisponiveis, "cardListarRevisoresDisponiveis");
                cardLayout.show(cards, "cardListarRevisoresDisponiveis");

            }
        });
        painelBotoes.add(botaoAtribuir);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardRevisao");
            }
        });
        painelBotoes.add(botaoCancelar, BorderLayout.SOUTH);
        painelGeralDevolvido.add(painelBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;

    }

    private JPanel criaPainelListarRevisoresDisponiveis(int aIdRevisao) {
    	painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagemUtilizadores = gereRevisoes.pesquisaRevisoresDisponiveis(st, aIdRevisao);
        paginacaoGeral = new Paginacao(listagemUtilizadores, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotaoAtribuirRevisor = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoAtribuir = new JButton("Atribuir revisor selecionado");
        botaoAtribuir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gereRevisoes.atribuirUtilizadorRevisao(st, idUtilizador, aIdRevisao)) {
                    LocalDate dataAtual = LocalDate.now();
                    Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
                    String mensagem = "O pedido de revisao " + aIdRevisao + " foi atribuido ao revisor com id " + idUtilizador;
                    int tipoUserDestino = 2;
                    if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUtilizador, 0)){
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Revisor atribuido com sucesso!",
                                "Pedido Revisao",
                                JOptionPane.INFORMATION_MESSAGE);
                    } 
                    cardLayout.show(cards, "cardAtribuirNovoRevisor");
                } else {
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Erro ao atribuir revisor!",
                            "Pedido Revisao",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        painelBotaoAtribuirRevisor.add(botaoAtribuir);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardAtribuirNovoRevisor");
            }
        });
        painelBotaoAtribuirRevisor.add(botaoCancelar, BorderLayout.SOUTH);

        painelGeralDevolvido.add(painelBotaoAtribuirRevisor, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelArquivarRevisao() {
    	painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereRevisoes.listarPedidosRevisao(st, 2);
        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoArquivar = new JButton("Arquivar revisão selecionada");
        botaoArquivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if (gereRevisoes.alteraEstadoRevisao(st, idUtilizador, 6)){
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Revisão arquivada com sucesso!",
                            "Arquivo de Revisão",
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    String acao = "Arquivar Processos Revisao"; 
                    gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
                    
                    cards.remove(painelArquivarRevisao);
                    painelArquivarRevisao = criaPainelArquivarRevisao();
                    cards.add(painelArquivarRevisao, "cardArquivarRevisao");
                    cardLayout.show(cards, "cardArquivarRevisao");
                }
                else {
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Erro ao arquivar revisão!",
                            "Arquivo de Revisão",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        painelBotoes.add(botaoArquivar);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelBotoes.add(botaoCancelar, BorderLayout.SOUTH);
        painelGeralDevolvido.add(painelBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelAtivaDesativaUtilizador() {
        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereUtilizadores.listarUtilizadores(st);
        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton buttonAtivar = new JButton("Ativar utilizador selecionado");
        buttonAtivar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gereUtilizadores.ativarUtilizador(st, idUtilizador, 1)){
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Utilizador ativado com sucesso!",
                            "Ativar/Desativar Utilizador",
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    cards.remove(painelAtivaDesativaUtilizador);
                    painelAtivaDesativaUtilizador = criaPainelAtivaDesativaUtilizador();
                    cards.add(painelAtivaDesativaUtilizador, "cardAtivaDesativaUtilizador");
                    cardLayout.show(cards, "cardAtivaDesativaUtilizador");
                }
                else {
                    JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Erro ao ativar utilizador!",
                            "Ativar/Desativar Utilizador",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        painelBotoes.add(buttonAtivar);

        JButton botaoDesativar = new JButton("Desativar utilizador selecionado");
        botaoDesativar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(painelGeralDevolvido,
                        "Deseja mesmo desativar o utilizador?",
                        "Desativar Utilizador",
                        JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    if (gereUtilizadores.ativarUtilizador(st, idUtilizador, 0)){
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Utilizador desativado com sucesso!",
                                "Ativar/Desativar Utilizador",
                                JOptionPane.INFORMATION_MESSAGE);
                        
                        cards.remove(painelAtivaDesativaUtilizador);
                        painelAtivaDesativaUtilizador = criaPainelAtivaDesativaUtilizador();
                        cards.add(painelAtivaDesativaUtilizador, "cardAtivaDesativaUtilizador");
                        cardLayout.show(cards, "cardAtivaDesativaUtilizador");
                    } else {
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Erro ao desativar utilizador!",
                                "Ativar/Desativar Utilizador",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        painelBotoes.add(botaoDesativar);

        JPanel painelBotaoCancelar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelBotaoCancelar.add(botaoCancelar);

        JPanel painelPrincipalBotoes = new JPanel();
        painelPrincipalBotoes.setLayout(new BoxLayout(painelPrincipalBotoes, BoxLayout.Y_AXIS));
        painelPrincipalBotoes.add(painelBotoes);
        painelPrincipalBotoes.add(painelBotaoCancelar);

        painelGeralDevolvido.add(painelPrincipalBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelLogs() {
        painelGeralDevolvido = new JPanel(new GridLayout(3, 1));

        JButton botaoConsultarLogsUtilizador = new JButton("Consultar logs de um utilizador especifico");
        botaoConsultarLogsUtilizador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardConsultarLogsUtilizador");
            }
        });
        painelGeralDevolvido.add(botaoConsultarLogsUtilizador);

        JButton botaoConsultarTodosLogs = new JButton("Consultar todas as logs");
        botaoConsultarTodosLogs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardConsultarLogsGerais");
            }
        });
        painelGeralDevolvido.add(botaoConsultarTodosLogs);

        JButton botaoVoltar = new JButton("Voltar ao menu anterior");
        botaoVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelGeralDevolvido.add(botaoVoltar);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelConsultarLogsUtilizador() {
        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereUtilizadores.listarUtilizadores(st);
;
        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoVisualizar = new JButton("Ver logs de utilizador selecionado");
        botaoVisualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel painelLogsUtilizador = criaPainelListarLogsUtilizador(idUtilizador);
                cards.add(painelLogsUtilizador, "cardLogsUtilizador");
                cardLayout.show(cards, "cardLogsUtilizador");
            }
        });
        painelBotoes.add(botaoVisualizar, BorderLayout.SOUTH);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardMenuLogs");
            }
        });
        painelBotoes.add(botaoCancelar, BorderLayout.SOUTH);
        painelGeralDevolvido.add(painelBotoes, BorderLayout.SOUTH);


        return painelGeralDevolvido;
    }

    private JPanel criaPainelListarLogsUtilizador(int aIdUtilizador) {
    	painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereLogs.listarLogsUtilizador(st, aIdUtilizador);
        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JButton botaoOK = new JButton("OK");
        botaoOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardConsultarLogsUtilizador");
            }
        });
        painelGeralDevolvido.add(botaoOK, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelConsultarLogsGerais() {
        painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereLogs.listarLogs(st);
;
        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JButton botaoOK = new JButton("OK");
        botaoOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "cardMenuLogs");
            }
        });
        painelGeralDevolvido.add(botaoOK, BorderLayout.SOUTH);


        return painelGeralDevolvido;
    }

    private void verificarNotificacoes(Utilizadores utilizadorAutenticado) {
        String listagemNotificacoes;
        
        if (utilizadorAutenticado.getTipo() == 1) { // Tipo Gestor
            listagemNotificacoes = gereNotificacoes.listarNotificacoesGestores(st);
        } else {
            listagemNotificacoes = gereNotificacoes.listarNotificacoes(st, utilizadorAutenticado.getId(), utilizadorAutenticado.getTipo());
        }

        if (listagemNotificacoes != null && !listagemNotificacoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Existem novas notificações!", "Notificações", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Não existem notificações!", "Notificações", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel criaPainelSolicitarRevisaoObra() {
        JPanel painelSolicitarRevisaoObra = new JPanel(new GridLayout(0, 2));

        JTextField fieldTitulo = new JTextField();
        JTextField fieldSubTitulo = new JTextField();
        JTextField fieldEstiloLiterario = new JTextField();
        JTextField fieldNPaginas = new JTextField();
        JTextField fieldNEdicao = new JTextField();
        JComboBox<String> comboTipoPublicacao;
        
        painelSolicitarRevisaoObra.add(new JLabel("Titulo da obra:"));
        painelSolicitarRevisaoObra.add(fieldTitulo);

        painelSolicitarRevisaoObra.add(new JLabel("Subtitulo da obra:"));
        painelSolicitarRevisaoObra.add(fieldSubTitulo);

        painelSolicitarRevisaoObra.add(new JLabel("Estilo literário da obra:"));
        painelSolicitarRevisaoObra.add(fieldEstiloLiterario);

        painelSolicitarRevisaoObra.add(new JLabel("Tipo de publicação:"));
        comboTipoPublicacao = new JComboBox<>(new String[]{"Capa Dura", "Capa Mole (Brochura)", "Livro de Bolso", "Edição Digital", "Edição de Luxo"});
        painelSolicitarRevisaoObra.add(comboTipoPublicacao);
        
        painelSolicitarRevisaoObra.add(new JLabel("Número de páginas da obra:"));
        painelSolicitarRevisaoObra.add(fieldNPaginas);

        painelSolicitarRevisaoObra.add(new JLabel("Número de edição da obra:"));
        painelSolicitarRevisaoObra.add(fieldNEdicao);

        JButton botaoSubmeter = new JButton("Submeter");
        botaoSubmeter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LocalDate dataAtual = LocalDate.now();
                Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);

                
                String titulo = fieldTitulo.getText();
                String subTitulo = fieldSubTitulo.getText();
                String estiloLiterario = fieldEstiloLiterario.getText();
                String tipoPublicacao = (String) comboTipoPublicacao.getSelectedItem();
                String nPaginas = fieldNPaginas.getText();
                String nEdicao = fieldNEdicao.getText();
                int codISBN = gereObras.criaVerificaISBN(st);
                
                if (titulo.isEmpty() || subTitulo.isEmpty() || estiloLiterario.isEmpty() || tipoPublicacao.isEmpty() || nPaginas.isEmpty() || nEdicao.isEmpty()) {
                    JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                            "Por favor, preencha todos os campos obrigatórios!",
                            "Erro de Solicitação de Revisão",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int nPaginasInt = Integer.parseInt(nPaginas);
                int nEdicaoInt = Integer.parseInt(nEdicao);
                
                int tipoPublicacaoInt;
                switch (tipoPublicacao.toString()) {
                    case "Capa Dura":
                        tipoPublicacaoInt = 1;
                        break;
                    case "Capa Mole (Brochura)":
                        tipoPublicacaoInt = 2;
                        break;
                    case "Livro de Bolso":
                        tipoPublicacaoInt = 3;
                        break;
                    case "Edição Digital":
                        tipoPublicacaoInt = 4;
                        break;
                    case "Edição de Luxo":
                        tipoPublicacaoInt = 5;
                        break;
                    default:
                        JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                            "Tipo inválido!",
                            "Erro de Tipo de Publicação",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                }

                if (gereObras.verificaTitulo(st, titulo)) {
                    JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                            "O título já se encontra registado! Por favor, tente outro.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (gereObras.adicionarObra(st, titulo, subTitulo, estiloLiterario, tipoPublicacaoInt, nPaginasInt, codISBN, nEdicaoInt, dataAtualConvertida, null)) {
                        JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                                "Obra adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        int idUltimoObra = gereObras.ultimoIdObra(st);
                        if (gereObras.atribuirAutorObra(st, utilizadorAutenticado.getId(), idUltimoObra)) {
                            int idUltimoPedido = gereRevisoes.idUltimoPedido(st);
                            String nSerie = gereRevisoes.geraNSerie(idUltimoPedido);

                            if (gereRevisoes.adicionarPedidoRevisao(st, nSerie, dataAtualConvertida, null, null, -1, 1, idUltimoObra)) {
                                String mensagem = "O Utilizador " + utilizadorAutenticado.getNome() + " colocou um pedido de revisão.";
                                int tipoUserDestino = 1;
                                int idUser = utilizadorAutenticado.getId();

                                if (gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)) {
                                    JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                                            "Pedido de revisão criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                                }
                                if (gereRevisoes.atribuirUtilizadorRevisao(st, utilizadorAutenticado.getId(), gereRevisoes.idUltimoPedido(st))) {
                                    JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                                            "Autor adicionado com sucesso à revisão!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                                    
		                                    cards.remove(painelSolicitarRevisaoObra);
		                                	JPanel painelSolicitarRevisaoObra = criaPainelSolicitarRevisaoObra();
		                                    cards.add(painelSolicitarRevisaoObra, "cardSolicitarRevisaoObra");
		                                    cardLayout.show(cards, "Menu");
                                    
                                    
                                } else {
                                    JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                                            "Erro ao adicionar o autor à revisão!", "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                                        "Erro ao criar pedido de revisão!", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                                    "Erro ao adicionar o autor à obra!", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(painelSolicitarRevisaoObra,
                                "Erro ao adicionar obra!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        painelSolicitarRevisaoObra.add(botaoSubmeter);
        
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelSolicitarRevisaoObra.add(botaoCancelar);

        return painelSolicitarRevisaoObra;
    }

    private JPanel criaPainelConsultarProcessosRevisao() {
        JPanel painelConsultarProcessosRevisao = new JPanel(new BorderLayout());

        String listagem = gereRevisoes.listarPedidosRevisaoDeAutor(st, utilizadorAutenticado.getId(), 1);
        
        paginacaoGeral = new Paginacao(listagem, this);

        painelConsultarProcessosRevisao.add(paginacaoGeral, BorderLayout.CENTER);

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });

        painelConsultarProcessosRevisao.add(botaoVoltar, BorderLayout.SOUTH);

        return painelConsultarProcessosRevisao;
    }

    private JPanel criaPainelProcessosPagamento() {
    	painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereRevisoes.pesquisarPedidosRevisaoDeAutorEstado(st, utilizadorAutenticado.getId(), 4);

        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoPagar = new JButton("Pagar revisão selecionada");
        botaoPagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Float valorAPagar = gereRevisoes.valorAPagar(st, idUtilizador);
                String mensagem = "Valor a pagar é " + valorAPagar + ". Deseja mesmo pagar a revisão?";

                int resposta = JOptionPane.showConfirmDialog(null, mensagem, "Confirmar Pagamento", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                	
                	if (gereRevisoes.alteraEstadoRevisao(st, idUtilizador, 5)){
                        LocalDate dataAtual = LocalDate.now();
                        Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
                        int idUser = utilizadorAutenticado.getId();
                        
                        String mensagemNotificacao = "O seu pedido de revisao " + idUtilizador + " foi pago com sucesso pelo utilizador" + idUser;
                        int tipoUserDestino = 3;
                        if(gereNotificacoes.adicionarNotificacao(st, mensagemNotificacao, dataAtualConvertida, tipoUserDestino, idUser, 0)){
                        	JOptionPane.showMessageDialog(painelGeralDevolvido,
                                    "Processo pago com sucesso!",
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        	
                        	cards.remove(painelProcessosPagamento);
                        	JPanel painelProcessosPagamento = criaPainelProcessosPagamento();
                            cards.add(painelProcessosPagamento, "cardProcessosPagamento");
                            cardLayout.show(cards, "cardProcessosPagamento");
                        }
                      } 
                	else {
                		JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Erro ao pagar o processo!",
                                "Erro", JOptionPane.INFORMATION_MESSAGE);
                      }      	
                }

            }
        });
        painelBotoes.add(botaoPagar);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelBotoes.add(botaoCancelar, BorderLayout.SOUTH);
        painelGeralDevolvido.add(painelBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelConsultarNotificacoesRevisorAutor() {
        JPanel painelConsultarNotificacoesRevisorAutor = new JPanel(new BorderLayout());

        String listagemNotificacoes = gereNotificacoes.listarNotificacoes(st,utilizadorAutenticado.getId(),utilizadorAutenticado.getTipo());
;
        paginacaoGeral = new Paginacao(listagemNotificacoes, this);

        painelConsultarNotificacoesRevisorAutor.add(paginacaoGeral, BorderLayout.CENTER);

        JButton botaoOK = new JButton("OK");
        botaoOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	gereNotificacoes.darVistaNotificacoes(st, utilizadorAutenticado.getId(),utilizadorAutenticado.getTipo());
            	
                cards.remove(painelConsultarNotificacoesRevisorAutor);
            	JPanel painelConsultarNotificacoesRevisorAutor = criaPainelConsultarNotificacoesRevisorAutor();
                cards.add(painelConsultarNotificacoesRevisorAutor, "cardConsultarNotificacoesRevisorAutor");
                cardLayout.show(cards, "Menu");
            }
        });
        painelConsultarNotificacoesRevisorAutor.add(botaoOK, BorderLayout.SOUTH);


        return painelConsultarNotificacoesRevisorAutor;
    }

    private JPanel criaPainelPedidosRevisaoPendentes() {
    	painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereRevisoes.pesquisarPedidosRevisaoDeRevisorEstado(st, utilizadorAutenticado.getId(), 2);

        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoesAprovarRejeitar = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoAprovar = new JButton("Aceitar revisão selecionada");
        botaoAprovar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (gereRevisoes.alteraEstadoRevisao(st, idUtilizador, 3)){
            		JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Pedido de revisao aceite com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                	
                	cards.remove(painelPedidosRevisaoPendentes);
                	JPanel painelPedidosRevisaoPendentes = criaPainelPedidosRevisaoPendentes();
                    cards.add(painelPedidosRevisaoPendentes, "cardPedidosRevisaoPendentes");
                    cardLayout.show(cards, "cardPedidosRevisaoPendentes");
  	            } 
            	else {
  	            	JOptionPane.showMessageDialog(painelGeralDevolvido,
                            "Erro ao aceitar revisão!",
                            "Erro", JOptionPane.INFORMATION_MESSAGE);
  	            }
            }
        });
        painelBotoesAprovarRejeitar.add(botaoAprovar);

        JButton botaoRejeitar = new JButton("Rejeitar revisão selecionada");
        botaoRejeitar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int resposta = JOptionPane.showConfirmDialog(null, "Deseja mesmo rejeitar a revisão selecionada?", "Rejeitar Revisão", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                	if (gereRevisoes.alteraEstadoRevisao(st, idUtilizador, 8)){
                		
                		LocalDate dataAtual = LocalDate.now();
                        Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
                        String mensagem = "O pedido de revisao " + idUtilizador + " foi rejeitado pelo revisor com id " + utilizadorAutenticado.getId();
                        int tipoUserDestino = 1;
                        int idUser = utilizadorAutenticado.getId();
                        if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)){
                        	JOptionPane.showMessageDialog(painelGeralDevolvido,
                                    "Pedido de revisao rejeitado com sucesso!",
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }
                			
                    	cards.remove(painelPedidosRevisaoPendentes);
                    	JPanel painelPedidosRevisaoPendentes = criaPainelPedidosRevisaoPendentes();
                        cards.add(painelPedidosRevisaoPendentes, "cardPedidosRevisaoPendentes");
                        cardLayout.show(cards, "cardPedidosRevisaoPendentes");
      	            } 
                	else {
      	            	JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Erro ao rejeitar pedido de revisão!",
                                "Erro", JOptionPane.INFORMATION_MESSAGE);
      	            }	
                }
            }
        });
        painelBotoesAprovarRejeitar.add(botaoRejeitar);

        JPanel painelBotaoCancelar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelBotaoCancelar.add(botaoCancelar);

        JPanel painelPrincipalBotoes = new JPanel();
        painelPrincipalBotoes.setLayout(new BoxLayout(painelPrincipalBotoes, BoxLayout.Y_AXIS));
        painelPrincipalBotoes.add(painelBotoesAprovarRejeitar);
        painelPrincipalBotoes.add(painelBotaoCancelar);

        painelGeralDevolvido.add(painelPrincipalBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }

    private JPanel criaPainelMeusProcessosRevisao() {
        JPanel painelMeusProcessosRevisao = new JPanel(new BorderLayout());
    
        String listagem = gereRevisoes.listarPedidosRevisaoDeRevisor(st, utilizadorAutenticado.getId(), 1);
        paginacaoGeral = new Paginacao(listagem, this);
    
        painelMeusProcessosRevisao.add(paginacaoGeral, BorderLayout.CENTER);
    
        JButton botaoOK = new JButton("OK");
        botaoOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
    
        painelMeusProcessosRevisao.add(botaoOK, BorderLayout.SOUTH);
    
        return painelMeusProcessosRevisao;
    }

    private JPanel criaPainelConcluirProcessoRevisao() {
    	painelGeralDevolvido = new JPanel(new BorderLayout());

        String listagem = gereRevisoes.listarPedidosRevisaoDeRevisor(st, utilizadorAutenticado.getId(), 1);

        paginacaoGeral = new Paginacao(listagem, this);

        painelGeralDevolvido.add(paginacaoGeral, BorderLayout.CENTER);

        JPanel painelBotoesAprovarRejeitar = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoConcluir = new JButton("Concluir revisão selecionada");
        botaoConcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    String custoRevisao = JOptionPane.showInputDialog(painelGeralDevolvido, "Por favor introduza o custo da revisão:");
                    Float custoRevisaoFloat = Float.parseFloat(custoRevisao);

                    LocalDate dataAtual = LocalDate.now();
                    Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);

                    String observacoes = JOptionPane.showInputDialog(painelGeralDevolvido, "Por favor introduza as observações que deseja:");
                    
                    if (gereRevisoes.concluirPedidoRevisao(st, dataAtualConvertida, observacoes, custoRevisaoFloat, idUtilizador)) {
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Pedido de revisao concluído com sucesso!",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        
                        cards.remove(painelConcluirProcessoRevisao);
                    	JPanel painelConcluirProcessoRevisao = criaPainelConcluirProcessoRevisao();
                        cards.add(painelConcluirProcessoRevisao, "cardConcluirProcessoRevisao");
                        cardLayout.show(cards, "cardConcluirProcessoRevisao");
                    } 
                    else {
                        JOptionPane.showMessageDialog(painelGeralDevolvido,
                                "Erro ao concluir processo de revisão!",
                                "Erro", JOptionPane.INFORMATION_MESSAGE);
                    }        
            }
        });
        painelBotoesAprovarRejeitar.add(botaoConcluir);

        JPanel painelBotaoCancelar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "Menu");
            }
        });
        painelBotaoCancelar.add(botaoCancelar);

        JPanel painelPrincipalBotoes = new JPanel();
        painelPrincipalBotoes.setLayout(new BoxLayout(painelPrincipalBotoes, BoxLayout.Y_AXIS));
        painelPrincipalBotoes.add(painelBotoesAprovarRejeitar);
        painelPrincipalBotoes.add(painelBotaoCancelar);

        painelGeralDevolvido.add(painelPrincipalBotoes, BorderLayout.SOUTH);

        return painelGeralDevolvido;
    }
    
    private void imprimirExtratoRevisao() {
        String extrato = gereRevisoes.listarPedidosRevisao(st, 1);
        if (extrato != null && !extrato.isEmpty()) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new Printable() {
                public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
                    if (page > 0) {
                        return NO_SUCH_PAGE;
                    }
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.translate(pf.getImageableX(), pf.getImageableY());

                    Font font = new Font("Serif", Font.PLAIN, 10);
                    g2d.setFont(font);
                    FontMetrics metrics = g2d.getFontMetrics(font);
                    int lineHeight = metrics.getHeight();

                    int x = 100;
                    int y = 100;
                    for (String line : extrato.split("\n")) {
                        g2d.drawString(line, x, y);
                        y += lineHeight;
                    }
                    return PAGE_EXISTS;
                }
            });
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Não há ações registadas.");
        }
    }

    
    private String getIdRevisaoLinha(String line) {
    	  return line.split(" ")[2];
    }

    public void voltarAoMenu() {
        cardLayout.show(cards, "Menu");
    }
}
