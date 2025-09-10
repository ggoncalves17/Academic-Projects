/*
  * Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe principal onde consta o método main para executar a aplicação.
 * Esta classe dispõe de um menu que permite a interação com o utilizador e o manuseamento da aplicação.
 */
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.util.Enumeration;
import java.io.*;
import java.sql.*;

class Main {

  // Inicializar gestores
  private static GereUtilizadores gereUtilizadores = new GereUtilizadores();
  private static GereRevisoes gereRevisoes = new GereRevisoes();
  private static GereObras gereObras = new GereObras();
  private static GereNotificacoes gereNotificacoes = new GereNotificacoes();
  private static GereLogs gereLogs = new GereLogs();
  private static Server server = new Server();


  public static void main(String[] args) {

    SimpleDateFormat dataFormato = new SimpleDateFormat("EEEE; yyyy-MM-dd HH:mm:ss");

    Date dataInicio = new Date();
    String dataInicioFormatada = dataFormato.format(dataInicio);
    
    Statement st;
    LigacaoBD ligacao = new LigacaoBD();
    
    if(ligacao.carregarPropriedades() && ligacao.conectar()) {
      if (ligacao != null) {
        
        st = ligacao.getStatement();
        
        System.out.println("Conexao bem-sucedida!");
        
        while (true) {
            System.out.println("----------------------------------------------------------");
            System.out.println("|                          TIPO                          |");
            System.out.println("----------------------------------------------------------");
            System.out.println("|              1. Comunicação Via Sockets                |");
            System.out.println("|              2. Consola		                 |");
            System.out.println("|              0. Sair                                   |");
            System.out.println("----------------------------------------------------------");
            
            int escolhaTipo = lerDadosInt("Por favor selecione uma opcao: ");
            
            switch (escolhaTipo) {
            case 0:
                System.out.println("Adeus...");
                return; 
            case 1:
                server.servidor(st, gereUtilizadores, gereObras, gereRevisoes);
                break;
            case 2:
            	// Utilizador autenticado
                Utilizadores utilizadorAutenticado = null;
                int primeiroAcesso = 0;
                
                while (true) {
                  
                  if(primeiroAcesso != 0) {
                    limpaTerminal();
                  }
                  primeiroAcesso ++;
                  
                  apresentaMenu(utilizadorAutenticado);
                  
                  int opcao = lerDadosInt("Por favor selecione uma opcao: ");

                  if(opcao == 2 && utilizadorAutenticado == null){
                    utilizadorAutenticado = opcao2(utilizadorAutenticado, st);
                  }
                  else{
                    executar(utilizadorAutenticado, opcao, ligacao, st, dataInicioFormatada, dataInicio, dataFormato); 
                  }
                }
            default:
                System.out.println("Escolha inválida");
        }

        }
        
      }
    }
  }

  public static void limpaTerminal() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  private static void limpaBuffer() {
    new Scanner(System.in).nextLine();
  }

  private static int lerDadosInt(String aMensagem) {
    System.out.println(aMensagem);
    return new Scanner(System.in).nextInt();
  }

  private static String lerDadosString(String aMensagem) {
    System.out.println(aMensagem);
    return new Scanner(System.in).nextLine();
  }

  private static Float lerDadosFloat(String aMensagem) {
    System.out.println(aMensagem);
    return new Scanner(System.in).nextFloat();
  }

  // Menu
  private static void apresentaMenu(Utilizadores utilizadorAutenticado) {

    System.out.println("----------------------------------------------------------");
    System.out.println("|                          MENU                          |");
    System.out.println("----------------------------------------------------------");

    
    // Utilizador não logado
    if (utilizadorAutenticado == null) {
      System.out.println("|              1. Registar Utilizador                    |");
      System.out.println("|              2. Autenticar Utilizador                  |");
    }  
    else {
      // Opções Utilizador logado
      System.out.println("|           1. Editar informação pessoal                 |");

      // Opções específicas do Administrador / Gestor
      if (utilizadorAutenticado.getTipo() == 1) {
        System.out.println("|           2. Criar novo Utilizador                     |");
        System.out.println("|           3. Alterar Dados de um Utilizador            |");
        System.out.println("|           4. Consultar Notificações                    |"); 
        System.out.println("|           5. Consultar Pedidos Registo                 |");
        System.out.println("|           6. Consultar Pedidos Revisão                 |"); 
        System.out.println("|           7. Arquivar Processos de Revisão             |");
        System.out.println("|           8. (In)Ativar Contas de Utilizador           |");
        System.out.println("|           9. Consultar Logs                            |");
      }
      
      // Opções específicas do Revisor
      if (utilizadorAutenticado.getTipo() == 2) {
        System.out.println("|           2. Consultar Pedidos Revisão Pendentes       |");
        System.out.println("|           3. Consultar os Meus Processos Revisão       |");
        System.out.println("|           4. Concluir Processo de Revisão              |");
        System.out.println("|           5. Consultar Notificações                    |");
      }
        
      // Opções específicas dos Autores
      if (utilizadorAutenticado.getTipo() == 3) {
        System.out.println("|           2. Solicitar Revisão de Obra                 |");
        System.out.println("|           3. Consultar Processos de Revisão            |");
        System.out.println("|           4. Processos a Pagamento                     |");
        System.out.println("|           5. Consultar Notificações                    |");
      }
    }
    System.out.println("|              0. Sair                                   |");
    System.out.println("----------------------------------------------------------");
 }

  private static void executar(Utilizadores utilizadorAutenticado, int aOpcao, LigacaoBD ligacao, Statement st, String dataInicioFormatada, Date dataInicio, SimpleDateFormat dataFormato) {
    if (utilizadorAutenticado == null) {
      switch (aOpcao) {
        case 0:
          opcao0(utilizadorAutenticado, ligacao, st, dataInicioFormatada, dataInicio, dataFormato);
          System.exit(0);
          break;
        case 1:
          opcao1(utilizadorAutenticado, st);
          break;
        case 2:
          opcao2(utilizadorAutenticado, st);
          break;
        default:
          erro();
          break;
      }
    }
    else {
      switch (aOpcao) {
        case 0:
          opcao0(utilizadorAutenticado, ligacao, st, dataInicioFormatada, dataInicio, dataFormato);
          System.exit(0);
          break;
        case 1:
          opcao1_1(utilizadorAutenticado, st);
          break;
        default:
          break;
      }  
      if (utilizadorAutenticado.getTipo() == 1) {
        switch (aOpcao) {
          case 2:
            opcao1(utilizadorAutenticado, st);
            break;
          case 3:
            opcao2_3(utilizadorAutenticado, st);
            break;
          case 4:
            opcao2_4(utilizadorAutenticado, st);
            break;
          case 5:
            opcao2_5(utilizadorAutenticado, st);
            break;
          case 6:
            opcao2_6(utilizadorAutenticado, st);
            break;
          case 7:
            opcao2_7(utilizadorAutenticado, st);
            break;
          case 8:
            opcao2_8(utilizadorAutenticado, st);
            break;
          case 9:
            opcao2_9(utilizadorAutenticado, st);
            break;
          default:
            erro();
            break;
        }
      }
      if (utilizadorAutenticado.getTipo() == 2) {
          switch (aOpcao) {
            case 2:
              opcao3_2(utilizadorAutenticado, st);
              break;
            case 3:
              opcao3_3(utilizadorAutenticado, st, 1);
              break;
            case 4:
              opcao3_4(utilizadorAutenticado, st);
              break;
            case 5:
              opcao2_4(utilizadorAutenticado, st);
              break;
            default:
              erro();
              break;
          }
        }
        if (utilizadorAutenticado.getTipo() == 3) {
          switch (aOpcao) {
            case 2:
              opcao4_2(utilizadorAutenticado, st);
              break;
            case 3:
              opcao4_3(utilizadorAutenticado, st);
              break;
            case 4:
              opcao4_4(utilizadorAutenticado, st);
              break;
            case 5:
              opcao2_4(utilizadorAutenticado, st);
              break;
            default:
              erro();
              break;
          }
        }
      }
    }

    private static void erro() {
      System.out.println("Opção Inválida!");
    }
    

    // Opção 0 - Sair
    private static void opcao0(Utilizadores utilizadorAutenticado, LigacaoBD ligacao, Statement st, String dataInicioFormatada, Date dataInicio, SimpleDateFormat dataFormato) {

      Date dataFim = new Date();
      String dataFimFormatada = dataFormato.format(dataFim);
      
      if (utilizadorAutenticado != null) {
        String acao = "Logout"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
      }

      long tempoExecucao = dataFim.getTime() - dataInicio.getTime();
      long segundos = tempoExecucao / 1000;
      long minutos = segundos / 60;
      long horas = minutos / 60;

      System.out.println("Inicio do processo: " + dataInicioFormatada);
      System.out.println("Fim do processo: " + dataFimFormatada);
      System.out.println("Tempo de execucao: " + tempoExecucao + " Milissegundos (" + segundos + 
          " Segundos; " + minutos + " Minutos; "+ horas + " Horas)");
      
      if (utilizadorAutenticado != null) { 
    	  System.out.println("Adeus " + utilizadorAutenticado.getNome());
      }
      else {
    	  System.out.println("Adeus!");
      }
      
      ligacao.desconectar();
    }

  
    // Opção 1 - Registar Utilizador
    private static void opcao1(Utilizadores utilizadorAutenticado, Statement st) {
      int opcao = lerDadosInt("Escolheu [Registar Utilizador]. Pretende continuar? (1 - Sim / 0 - Nao)");
      LocalDate dataAtual = LocalDate.now();
      Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
      
      if (opcao == 1) {
        String nome = lerDadosString("Por favor introduza o nome: ");
        String password = lerDadosString("Por favor introduza a password: ");
        String login, email;

        do {
          login = lerDadosString("Por favor introduza o login (username): ");
          if (gereUtilizadores.verificaLogin(st, login))
            System.out.println("O login ja se encontra registado! Por favor, tente outro.");
        } while (gereUtilizadores.verificaLogin(st, login));

        do {
          email = lerDadosString("Por favor introduza o email: ");

          if(gereUtilizadores.verificaFormatoEmail(email) == false)
            System.out.println("O email nao se encontra no formato correto! Por favor, tente outro.");
          else {
            if (gereUtilizadores.verificaEmail(st, email))
              System.out.println("O email ja se encontra registado! Por favor, tente outro.");
          }
        } while (gereUtilizadores.verificaEmail(st, email) || gereUtilizadores.verificaFormatoEmail(email) == false);

        int tipo;
        boolean estado;

        if (gereUtilizadores.listarUtilizadores(st).equals("")) {
          tipo = 1;
          estado = true;
          
          if (gereUtilizadores.adicionarInfoUtilizadorBD(st, login, password, nome, estado, email, tipo)) {     
        	  System.out.println("Gestor registado com sucesso!");
           }
        } 
        else {
          System.out.println("----------------------------------------------------------");
          if(utilizadorAutenticado != null && utilizadorAutenticado.getTipo() == 1){
        	  System.out.println("|                   1. Gestor                            |");
          }
          System.out.println("|                   2. Revisor                           |");
          System.out.println("|                   3. Autor                             |");
          System.out.println("----------------------------------------------------------");

          tipo = lerDadosInt("Por favor introduza o numero relativo ao tipo de utilizador que deseja: ");
          estado = false;
          
          if (utilizadorAutenticado != null && utilizadorAutenticado.getTipo() == 1 && tipo == 1) {
        	  if (gereUtilizadores.adicionarInfoUtilizadorBD(st, login, password, nome, estado, email, tipo)) {     
            	  System.out.println("Gestor registado com sucesso!");
               }
          }
          
	        // Registo Revisores
	        if (tipo == 2) {
	          int NIF;
	
	          do {
	            NIF = lerDadosInt("Por favor introduza o NIF: ");
	
	            if(gereUtilizadores.verificaFormatoNIF(NIF) == false)
	              System.out.println("O NIF nao se encontra no formato correto! Por favor, tente outro.");
	            else {
	              if (gereUtilizadores.verificaNIF(st, NIF))
	                System.out.println("O NIF ja se encontra registado! Por favor, tente outro.");
	            }
	          } while (gereUtilizadores.verificaNIF(st, NIF) || gereUtilizadores.verificaFormatoNIF(NIF) == false);
	
	          int telefone;
	          
	          do {
	            telefone = lerDadosInt("Por favor introduza o telefone: ");
	            
	            if (gereUtilizadores.verificaFormatoTelefone(telefone) == false)
	              System.out.println("O numero de telefone introduzido nao e valido! Por favor, tente outro.");
	          } while (gereUtilizadores.verificaFormatoTelefone(telefone) == false);
	          
	          String morada = lerDadosString("Por favor introduza a morada: ");
	          String areaEspecializacao = lerDadosString("Por favor introduza a area de especializacao: ");
	          String formacaoAcademica = lerDadosString("Por favor introduza a formacao academica: ");
	
	          if (gereUtilizadores.adicionarInfoUtilizadorBD(st, login, password, nome, estado, email, tipo)) {
	            
	            int ultimoId = gereUtilizadores.ultimoIdUtilizador(st);
	            
	            if (gereUtilizadores.adicionarInfoRevisorBD(st, NIF, morada, telefone, areaEspecializacao, formacaoAcademica, ultimoId)) {
	              String mensagem = "Utilizador " + nome + " pendente de aprovação";
	              int tipoUserDestino = 1;
	                int idUser = gereUtilizadores.ultimoIdUtilizador(st);
	              if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)) {
	                System.out.println("Revisor registado com sucesso!");
	              }
	            }
	          }
	        
	        } 
	        else {
	          // Registo Autores
	          if (tipo == 3) {
	            int NIF;
	            
	            do {
	              NIF = lerDadosInt("Por favor introduza o NIF: ");
	              
	              if(gereUtilizadores.verificaFormatoNIF(NIF) == false)
		              System.out.println("O NIF nao se encontra no formato correto! Por favor, tente outro.");
		          else {
		              if (gereUtilizadores.verificaNIF(st, NIF))
		                System.out.println("O NIF ja se encontra registado! Por favor, tente outro.");
		            }
	            } while (gereUtilizadores.verificaNIF(st, NIF)  || gereUtilizadores.verificaFormatoNIF(NIF) == false);
	            
	            int telefone;
	            do {
	              telefone = lerDadosInt("Por favor introduza o telefone: ");
	              if (gereUtilizadores.verificaFormatoTelefone(telefone) == false)
	                System.out.println("O numero de telefone introduzido nao e valido! Por favor, tente outro.");
	            } while (gereUtilizadores.verificaFormatoTelefone(telefone) == false);
	            
	            String morada = lerDadosString("Por favor introduza a morada: ");
	            String estiloLiterario = lerDadosString("Por favor introduza o estilo literario: ");
	            String dataInicioAtividade = lerDadosString("Por favor introduza a data de inicio da atividade (AAAA-MM-dd): ");
	
	            if (gereUtilizadores.adicionarInfoUtilizadorBD(st, login, password, nome, estado, email, tipo)) {
	            
	              int ultimoId = gereUtilizadores.ultimoIdUtilizador(st);
	              
	              if (gereUtilizadores.adicionarInfoAutorBD(st, NIF, estiloLiterario, dataInicioAtividade, telefone, morada, ultimoId)) {
	                String mensagem = "Utilizador " + nome + " pendente de aprovação";
	                int tipoUserDestino = 1;
	                int idUser = gereUtilizadores.ultimoIdUtilizador(st);

	                if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)) {
	                  System.out.println("Autor registado com sucesso!");
	                }
	              }
	            }
	            else {
	               System.out.println("Erro ao registar autor!");
	            }
	          } 
	          else {
	        	  if (opcao != 0) {
	        		  System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
	        	  }
	          }
	        }
	     }
       }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }

    
    // Opção 2 - Autenticar Utilizador
    private static Utilizadores opcao2(Utilizadores utilizadorAutenticado, Statement st) {
      int opcao = lerDadosInt("Escolheu [Autenticar]. Pretende continuar? (1 - Sim / 0 - Nao)");
      int escolhaLogin = 0;
      if (opcao == 1) {
        String login, password;
        Utilizadores utilizador = null;
        
        do {
          login = lerDadosString("Por favor introduza o login (username): ");
          password = lerDadosString("Por favor introduza a password: ");
          if (gereUtilizadores.verificaLoginUtilizador(st, login, password) != null) {
            utilizador = gereUtilizadores.verificaLoginUtilizador(st, login, password);
            if (utilizador.getEstado() == true) {
              utilizadorAutenticado = utilizador;
              System.out.println("Bem-vindo " + utilizadorAutenticado.getNome());
              String acao = "Login"; 
              gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
            } else {
              System.out.println("Utilizador nao encontrado (Inativo)!");
            }
          } else {
              System.out.println("Login ou password incorretos!");
              escolhaLogin = lerDadosInt("Pretende continuar a tentar ou deseja sair para o menu? (1 - Continuar / 0 - Sair)");
          }
        } while (utilizador == null && escolhaLogin == 1);
        System.out.println("Pressione alguma tecla para continuar!");
        limpaBuffer();
        return utilizadorAutenticado;
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
      return null;
    }

  
    // Opção 1.1 - Editar Informacao Pessoal
    private static void opcao1_1(Utilizadores utilizadorAutenticado, Statement st) {
      int opcao = lerDadosInt("Escolheu [Editar informacao]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1) {
        String acao = "Editar Dados Pessoais"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        System.out.println("----------------------------------------------------------");
        System.out.println("|                   1. Nome                              |");
        System.out.println("|                   2. Password                          |");
        System.out.println("|                   3. Email                             |");
        if(utilizadorAutenticado.getTipo() != 1) {
        	System.out.println("|                   4. Telefone                          |");
        	System.out.println("|                   5. Morada                            |");
        }
        System.out.println("----------------------------------------------------------");

        int escolha = lerDadosInt("Por favor introduza o numero relativo ao campo que pretende alterar: ");

        switch (escolha) {
        case 1:
          String nome = lerDadosString("Por favor introduza o novo nome: ");
          if(gereUtilizadores.atualizaNome(st, utilizadorAutenticado.getId(), nome)) {
              utilizadorAutenticado.setNome(nome);
        	  System.out.println("Nome atualizado com sucesso!");
          }
          else {
              System.out.println("Erro ao atualizar o nome!");
          }
          break;
        case 2:
          String atualPassword;
          boolean correto = false;
          do { 
            atualPassword = lerDadosString("Por favor introduza a password atual: ");
            if (gereUtilizadores.verificaLoginUtilizador(st, utilizadorAutenticado.getLogin(), atualPassword) != null) {
              String novaPassword = lerDadosString("Por favor introduza a nova password: ");
              if(gereUtilizadores.atualizaPassword(st, utilizadorAutenticado.getId(), novaPassword)) {
                  utilizadorAutenticado.setPassword(novaPassword);
            	  System.out.println("Password atualizada com sucesso!");
              }
              else {
                  System.out.println("Erro ao atualizar a password!");
              }
              correto = true;
            } else{
              System.out.println("Password atual incorreta!");
              int sim = lerDadosInt("Pretende introduzir novamente a password atual? (1 - Sim / 0 - Nao)");
              if (sim == 0)
                correto = true;
              }
          } while (!correto);
          break;
        case 3:
          String email;
          do {
              email = lerDadosString("Por favor introduza o email: ");

              if(gereUtilizadores.verificaFormatoEmail(email) == false)
                System.out.println("O email nao se encontra no formato correto! Por favor, tente outro.");
              else {
                if (gereUtilizadores.verificaEmail(st, email))
                  System.out.println("O email ja se encontra registado! Por favor, tente outro.");
              }
            } while (gereUtilizadores.verificaEmail(st, email) || gereUtilizadores.verificaFormatoEmail(email) == false);
          
          if(gereUtilizadores.atualizaEmail(st, utilizadorAutenticado.getId(), email)) {
              utilizadorAutenticado.setEmail(email);
        	  System.out.println("Email atualizado com sucesso!");
          }
          else {
              System.out.println("Erro ao atualizar o email!");
          }
          
          break;
        case 4:
        	if (utilizadorAutenticado.getTipo() != 1) {
    	         
        		int telefone;
  	          
	  	          do {
	  	            telefone = lerDadosInt("Por favor introduza o telefone: ");
	  	            
	  	            if (gereUtilizadores.verificaFormatoTelefone(telefone) == false)
	  	              System.out.println("O numero de telefone introduzido nao e valido! Por favor, tente outro.");
	  	          } while (gereUtilizadores.verificaFormatoTelefone(telefone) == false);
                
                if(utilizadorAutenticado.getTipo() == 2){
                  //Revisores revisor = (Revisores) utilizadorAutenticado;
                  if(gereUtilizadores.atualizaTelefoneRevisores(st, utilizadorAutenticado.getId(), telefone)) {
                      //revisor.setTelefone(telefone);
                	  System.out.println("Telefone atualizado com sucesso!");
                  }
                  else {
                      System.out.println("Erro ao atualizar o telefone!");
                  }
                }
                else{
                  if(utilizadorAutenticado.getTipo() == 3){
                    //Autores autor = (Autores) utilizadorAutenticado;
                    if(gereUtilizadores.atualizaTelefoneAutores(st, utilizadorAutenticado.getId(), telefone)) {
                        //autor.setTelefone(telefone);
                  	  System.out.println("Telefone atualizado com sucesso!");
                    }
                    else {
                        System.out.println("Erro ao atualizar o telefone!");
                    }
                  }
                  else{
                    System.out.println("O utilizador e gestor da plataforma e nao tem telefone associado!");
                  }
                }
        	}
        	break;
        case 5:
          String morada = lerDadosString("Por favor introduza a nova morada: ");
          if(utilizadorAutenticado.getTipo() == 2){
            //Revisores revisor = (Revisores) utilizadorAutenticado;
            if(gereUtilizadores.atualizaMoradaRevisores(st, utilizadorAutenticado.getId(), morada)) {
                //revisor.setMorada(morada);
          	   System.out.println("Morada atualizada com sucesso!");
            }
            else {
                System.out.println("Erro ao atualizar a morada!");
            }
          }
          else{
            if(utilizadorAutenticado.getTipo() == 3){
              //Autores autor = (Autores) utilizadorAutenticado;
              if(gereUtilizadores.atualizaMoradaAutores(st, utilizadorAutenticado.getId(), morada)) {
                  //autor.setMorada(morada);
            	   System.out.println("Morada atualizada com sucesso!");
              }
              else {
                  System.out.println("Erro ao atualizar a morada!");
              }
            }
            else{
              System.out.println("O utilizador e gestor da plataforma e nao tem morada associada!");
            }
          }
          break;
        default:
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
          break;
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }


    // Opção 2.3 - Alterar Dados de um Utilizador
    private static void opcao2_3(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Alterar Dados de um Utilizador]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Alterar Dados de Utilizador"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);

        int registosPorPagina = 10;
        int utilizadoresTotaisBD = gereUtilizadores.ultimoIdUtilizador(st);

        int paginasPossiveis = utilizadoresTotaisBD / registosPorPagina;
        if (utilizadoresTotaisBD % registosPorPagina != 0) {
            paginasPossiveis ++;
        }
        int paginaConsultar = 1;
        int encontrou = 0;
        do{
          String listagemUtilizadores = gereUtilizadores.listarUtilizadores(st);
          String listagem = gereUtilizadores.listarPorPagina(st, listagemUtilizadores, paginaConsultar);
          if (listagem != null && listagem != "") {
            System.out.println("----------------------------------------------------------");
            System.out.println("|               LISTAGEM UTILIZADORES                    |");
            System.out.println("----------------------------------------------------------");
            System.out.println(listagem);
            System.out.println("----------------------------------------------------------");
            System.out.println(paginaConsultar + " de " + paginasPossiveis);
          } else {
            System.out.println("Nao foram encontrados utilizadores!");
          }
          encontrou = lerDadosInt("Encontrou o utilizador que pretende alterar? (1 - Sim / 0 - Nao)");
          paginaConsultar ++;
        } while(encontrou == 0 && paginaConsultar <= paginasPossiveis);
        
        if(encontrou ==1){
          int idUtilizador = lerDadosInt("Por favor introduza o id do utilizador que pretende mudar os dados: ");
          if(gereUtilizadores.pesquisaPorId(st, idUtilizador) != null){
            Utilizadores utilizadorEditar = gereUtilizadores.pesquisaPorId(st, idUtilizador);
            opcao1_1(utilizadorEditar, st);
          } else {
            System.out.println("Utilizador nao encontrado!");
          }
        } 
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();    
    }


    // Opção 2.4 - Consultar Notificações
    private static void opcao2_4(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Consultar Notificacoes]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1) {
        String acao = "Consultar Notificacoes"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        String listagemNotificacoes = null;
        
        if (utilizadorAutenticado.getTipo() == 1){
          gereNotificacoes.notificarGestorPendentes(st);
          listagemNotificacoes = gereNotificacoes.listarNotificacoesGestores(st); 
          if (listagemNotificacoes != null && listagemNotificacoes != "") {
            System.out.println("----------------------------------------------------------");
            System.out.println("|               LISTAGEM NOTIFICACOES                    |");
            System.out.println("----------------------------------------------------------");
            System.out.println(listagemNotificacoes);
            System.out.println("----------------------------------------------------------");
            int darVista = lerDadosInt("Pretende marcar como lidas a(s) notificacao(oes) apresentada(s)? (1 - Sim / 0 - Nao)");
            if (darVista == 1){
              gereNotificacoes.darVistaNotificacoesGestores(st);
              System.out.println("Notificacoes marcadas como lidas com sucesso!");
            } else {
              if (darVista != 0) {
                System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
              }
            }
          } else {
            System.out.println("Nao foram encontradas notificacoes!");
          }
        } 
        else {
          listagemNotificacoes = gereNotificacoes.listarNotificacoes(st,utilizadorAutenticado.getId(),utilizadorAutenticado.getTipo());
          if (listagemNotificacoes != null && listagemNotificacoes != "") {
            System.out.println("----------------------------------------------------------");
            System.out.println("|               LISTAGEM NOTIFICACOES                    |");
            System.out.println("----------------------------------------------------------");
            System.out.println(listagemNotificacoes);
            System.out.println("----------------------------------------------------------");
            int darVista = lerDadosInt("Pretende marcar como lidas a(s) notificacao(oes) apresentada(s)? (1 - Sim / 0 - Nao)");
            if (darVista == 1){
              gereNotificacoes.darVistaNotificacoes(st, utilizadorAutenticado.getId(),utilizadorAutenticado.getTipo());
              System.out.println("Notificacoes marcadas como lidas com sucesso!");
            } 
            else {
              if (darVista != 0) {
                System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
              }
            }
          } 
          else {
            System.out.println("Nao foram encontradas notificacoes!");
          }
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer(); 
    }

  
    // Opção 2.5 - Consultar Pedidos de Registo
    private static void opcao2_5(Utilizadores utilizadorAutenticado, Statement st) {
      int opcao = lerDadosInt("Escolheu [Consultar Pedidos de Registo]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1) {
        String acao = "Consultar Pedidos Registo"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        String listagem = gereUtilizadores.listarUtilizadoresPendentes(st);
        if (listagem != null && listagem != "") {
          System.out.println("----------------------------------------------------------");
          System.out.println("|           LISTAGEM UTILIZADORES PENDENTES              |");
          System.out.println("----------------------------------------------------------");
          System.out.println(listagem);
          System.out.println("----------------------------------------------------------");
          int escolha = lerDadosInt("Pretende aprovar algum registo dos utilizadores apresentados? (1 - Sim / 0 - Nao) ");
          if (escolha == 1){  
            int idUtilizador = lerDadosInt("Por favor introduza o id do utilizador que pretende aprovar: ");
           
            if(gereUtilizadores.pesquisaPorId(st, idUtilizador) != null){
            	if (gereUtilizadores.aprovarUtilizadoresPendentes(st, idUtilizador)) {
                    System.out.println("Utilizador aprovado com sucesso!");
                  } else {
                    System.out.println("Erro ao aprovar utilizador!");
                }
            }
            else {
                System.out.println("Utilizador nao encontrado!");
            }
          } else {
            if (opcao != 0) {
              System.out.println("Operacao Cancelada!");
            }
          }
        } else
          System.out.println("Nao existem registos de utilizadores pendentes. \n");
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }

  
    // Opção 2.6 - Consultar Pedidos de Revisão
    private static void opcao2_6(Utilizadores utilizadorAutenticado, Statement st) {
      int opcao = lerDadosInt("Escolheu [Consultar Pedidos de Revisao]. Pretende continuar? (1 - Sim / 0 - Nao)");
      int idPedido;
      if (opcao == 1) {
        String acao = "Consultar Pedidos Revisao"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        System.out.println("----------------------------------------------------------");
        System.out.println("|         1. Consultar Todos os Pedidos                  |");
        System.out.println("|         2. Aprovar / Rejeitar Pedidos Pendentes        |");
        System.out.println("|         3. Atribuir novo Revisor                       |");
        System.out.println("|         4. Voltar ao Menu Anterior                     |");
        System.out.println("----------------------------------------------------------");
        int escolha = lerDadosInt("Selecione a opção desejada: ");
        int escolhaOrdenacao;
        switch (escolha){
          case 1:           
            escolhaOrdenacao = lerDadosInt("Escolha a ordenacao que pretende: (1 - Data Realizacao / 2 - Titulo / 3 - Nome Autor)");

            String listagem = gereRevisoes.listarPedidosRevisao(st, escolhaOrdenacao);
            if (listagem != null && listagem != ""){
            	
              System.out.println("----------------------------------------------------------");
              System.out.println("|           LISTAGEM PEDIDOS DE REVISAO                  |");
              System.out.println("----------------------------------------------------------");
              System.out.println(listagem);
              System.out.println("----------------------------------------------------------");
            }
            else {
                System.out.println("Não existem pedidos de revisao!");
            }
            System.out.println("Pressione alguma tecla para continuar!");
            limpaBuffer();
            break;
          case 2:            
            listagem = gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 2, 1);
            if (listagem != null && listagem != ""){
	            System.out.println("----------------------------------------------------------");
	            System.out.println("|           LISTAGEM PEDIDOS POR APROVAR                 |");
	            System.out.println("----------------------------------------------------------");
	            System.out.println(listagem);
	            System.out.println("----------------------------------------------------------");
	            
	            idPedido = lerDadosInt("Por favor introduza o id do pedido que pretende aprovar / rejeitar: ");
	            String listagemRevisao = gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 1, idPedido);
	            if (listagemRevisao != null && listagemRevisao != "") {
		            int aprovar = lerDadosInt("Pretende aprovar ou rejeitar o pedido introduzido? (1 - Aprovar / 0 - Rejeitar)");
		            if (aprovar == 1){
		              if (gereRevisoes.alteraEstadoRevisao(st, idPedido, 2)) {
		                 int ok = 0;
		                 do {
		                   String listagemUtilizadores = gereUtilizadores.pesquisaPorTipo(st, 2);
		                   if (listagemUtilizadores != null && listagemUtilizadores != "") {
		                     System.out.println("----------------------------------------------------------");
		                     System.out.println("|               LISTAGEM REVISORES                       |");
		                     System.out.println("----------------------------------------------------------");
		                     System.out.println(listagemUtilizadores);
		                     int idUtilizador = lerDadosInt("Indique o Id do revisor a atribuir a este pedido: ");
		                     if (gereRevisoes.atribuirUtilizadorRevisao(st, idUtilizador, idPedido)) {
		                       ok = 1;
		                       System.out.println("Revisor atribuido com sucesso!");

		                     } 
		                     else {
		                       System.out.println("Erro ao atribuir revisor!");
		                     }
		                    } else{
		                     System.out.println("Nao existem revisores!");
		                     ok = 1;
		                   }
		                 } while (ok != 1);
		              } else {
		                System.out.println("Erro ao aprovar pedido de revisao!");
		              }
		            } else {
		              if (aprovar == 0){
		                if (gereRevisoes.alteraEstadoRevisao(st, idPedido, 7)) {
		                  LocalDate dataAtual = LocalDate.now();
		                  Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
		                  String mensagem = "O seu pedido de revisao " + idPedido + " foi rejeitado";
		                  int tipoUserDestino = 3;
		                  int idUser = gereRevisoes.obterIdAutorPorRevisao(st, tipoUserDestino, idPedido);
		                  if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)){
		                    System.out.println("Pedido de revisao rejeitado com sucesso!");
		                  }
		                } else {
		                  System.out.println("Erro ao rejeitar pedido de revisao!");
		                }
		              } else {
		                System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
		              } 
		            }
	            }
	            else {
	          	  System.out.println("Nao existe nenhum pedido com esse id");
	            }
            }
            else {
                System.out.println("Não existem pedidos de revisao por aprovar!");
            }
            System.out.println("Pressione alguma tecla para continuar!");
            limpaBuffer();
            break;
          case 3:
            listagem = gereRevisoes.listarPedidosRevisaoEstados(st);
            if (listagem != null && listagem != ""){
	            System.out.println("----------------------------------------------------------");
	            System.out.println("|           LISTAGEM PEDIDOS DE REVISAO                  |");
	            System.out.println("----------------------------------------------------------");
	            System.out.println(listagem);
	            System.out.println("----------------------------------------------------------");
	            idPedido = lerDadosInt("Por favor introduza o id do pedido que pretede atribuir um novo revisor: ");
	            if (gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 1, idPedido) != null){
	              int ok = 0;
	               do {
	                 String listagemUtilizadores = gereRevisoes.pesquisaRevisoresDisponiveis(st, idPedido); // Função que devolve os revisores que ainda não estiveram associados ao processo de revisão
	                 if (listagemUtilizadores != null && listagemUtilizadores != "") {
	                   System.out.println("----------------------------------------------------------");
	                   System.out.println("|            LISTAGEM REVISORES DISPONÍVEIS              |");
	                   System.out.println("----------------------------------------------------------");
	                   System.out.println(listagemUtilizadores);
	                   int idRevisor = lerDadosInt("Indique o Id do revisor a atribuir a este pedido: ");
	                   if (gereRevisoes.atribuirUtilizadorRevisao(st, idRevisor, idPedido)) {
	                     ok = 1;
	                     LocalDate dataAtual = LocalDate.now();
	                     Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
	                     String mensagem = "O pedido de revisao " + idPedido + " foi atribuido ao revisor com id " + idRevisor;
	                     int tipoUserDestino = 2;
	                     if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idRevisor, 0)){
	                       System.out.println("Pedido de revisao associado ao revisor!");
	                     }
	                   } 
	                   else {
	                     System.out.println("Erro ao atribuir revisor!");
	                   }
	                 } 
	                 else{
	                   System.out.println("Nao existem revisores disponíveis!");
	                   ok = 1;
	                 }
	               } while (ok == 1);
	            } 
	            else {
	              System.out.println("Nao foi encontrado nenhum pedido com esse id!");
	            }
        	}
            else {
                System.out.println("Não existem pedidos de revisao por aprovar!");
            }
            System.out.println("Pressione alguma tecla para continuar!");
            limpaBuffer();
            break;
          case 4:
            System.out.println("Pressione alguma tecla para continuar!");
            limpaBuffer();
            break;
        }
      }
    }

        
    // Opção 2.7 - Arquivar Processos de Revisão
    private static void opcao2_7(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Arquivar Processos de Revisao]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Arquivar Processos Revisao"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        String listagem = gereRevisoes.listarPedidosRevisao(st, 2);
        if (listagem != null && listagem != ""){
          System.out.println("----------------------------------------------------------");
          System.out.println("|           LISTAGEM PEDIDOS DE REVISAO                  |");
          System.out.println("----------------------------------------------------------");
          System.out.println(listagem);
          System.out.println("----------------------------------------------------------");
          int idPedido = lerDadosInt("Por favor introduza o id do pedido que pretende arquivar: ");
          String listagemRevisao = gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 1, idPedido);
          if (listagemRevisao != null && listagemRevisao != ""){
	          if (gereRevisoes.alteraEstadoRevisao(st, idPedido, 6)){
	            System.out.println("Pedido de revisao arquivado com sucesso!");
	          } else {
	            System.out.println("Erro ao arquivar pedido de revisao!");
	          }
          }
          else {
	            System.out.println("Pedido de revisao nao encontrado!");    	
          }
        } else{
          System.out.println("Nao existem registos de pedidos de revisao pendentes.");
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }
        

    // Opção 2.8 - (In)Ativar Contas de Utilizador
    private static void opcao2_8(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [(In)Ativar Contas de Utilizador]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Ativar / Desativar Contas de Utilizador"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        String listagem = gereUtilizadores.listarUtilizadores(st);
        if (listagem != null && listagem != ""){
          System.out.println("----------------------------------------------------------");
          System.out.println("|               LISTAGEM UTILIZADORES                    |");
          System.out.println("----------------------------------------------------------");
          System.out.println(listagem);
          System.out.println("----------------------------------------------------------");
          int ativar = lerDadosInt("Pretende ativar ou desativar a conta de utilizador? (1 - Ativar / 0 - Desativar)");
          if (ativar == 1){
            int idUtilizador = lerDadosInt("Por favor introduza o id do utilizador que pretende ativar: ");
            if (gereUtilizadores.ativarUtilizador(st, idUtilizador, ativar)){
              System.out.println("Utilizador ativado com sucesso!");
            } else{
              System.out.println("Erro ao ativar utilizador!");
            }
          } else {
            if (ativar == 0){
              int idUtilizador = lerDadosInt("Por favor introduza o id do utilizador que pretende desativar: ");
              if (gereUtilizadores.ativarUtilizador(st, idUtilizador, ativar)){
                System.out.println("Utilizador desativado com sucesso!");
              } else{
                System.out.println("Erro ao desativar utilizador!");
              }
            } else{
              System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
            }
          }
        } else {
          System.out.println("Nao existem registos de utilizadores.");
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }  


    // Opção 2.9 - Consultar Logs
    private static void opcao2_9(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Consultar Logs]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Consultar Logs"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        int tipoPesquisa = lerDadosInt("Pretende consultar logs de um utilizador ou de todos? (1 - Utilizador / 2 - Todos)");
        if (tipoPesquisa == 1){
          int idUtilizador = lerDadosInt("Por favor introduza o id do utilizador que pretende consultar: ");
          String listagem = gereLogs.listarLogsUtilizador(st, idUtilizador);
          if (listagem != null && listagem != ""){
            System.out.println("----------------------------------------------------------");
            System.out.println("|               LISTAGEM LOGS UTILIZADORES               |");
            System.out.println("----------------------------------------------------------");
            System.out.println(listagem);
            System.out.println("----------------------------------------------------------");
          } else {
            System.out.println("Nao existem registos de logs deste utilizador.");
          }
        } else {
          if (tipoPesquisa == 2){
            String listagem = gereLogs.listarLogs(st);
            if (listagem != null && listagem != ""){
              System.out.println("----------------------------------------------------------");
              System.out.println("|               LISTAGEM LOGS                           |");
              System.out.println("----------------------------------------------------------");
              System.out.println(listagem);
              System.out.println("----------------------------------------------------------");
            } else{
              System.out.println("Nao existem registos de logs.");
            }
          } else {
            System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
          }
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
    }
        

    // Opção 3.2 - Consultar Pedidos de Revisão Pendentes
    private static void opcao3_2(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Consultar Pedidos de Revisao]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Consultar Pedidos Revisao Pendentes"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        String listagem = gereRevisoes.pesquisarPedidosRevisaoDeRevisorEstado(st, utilizadorAutenticado.getId(), 2);
        if (listagem != null && listagem != ""){
          System.out.println("----------------------------------------------------------");
          System.out.println("|           PEDIDOS DE REVISAO PENDENTES                 |");
          System.out.println("----------------------------------------------------------");
          System.out.println(listagem);
          System.out.println("----------------------------------------------------------");
          int aceitar = lerDadosInt("Pretende aceitar ou rejeitar um pedido de revisao? (1 - Aceitar / 0 - Rejeitar / 9 - Sair)");
          if (aceitar == 1){
            int idPedido = lerDadosInt("Por favor introduza o id do pedido que pretende aceitar: ");
            String listagemRevisao = gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 1, idPedido);
            if (listagemRevisao != null && listagemRevisao != ""){
	            if (gereRevisoes.alteraEstadoRevisao(st, idPedido, 3)){
	              System.out.println("Pedido de revisao aceite com sucesso!");
	            } else {
	              System.out.println("Erro ao aceitar pedido de revisao!");
	            }
            }
            else {
	            System.out.println("Pedido de revisao nao encontrado!");    	
            }
          } else {
            if (aceitar == 0){
              int idPedido = lerDadosInt("Por favor introduza o id do pedido que pretende rejeitar: ");
              if (gereRevisoes.alteraEstadoRevisao(st, idPedido, 8)){

                LocalDate dataAtual = LocalDate.now();
                Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
                String mensagem = "O pedido de revisao " + idPedido + " foi rejeitado pelo revisor com id " + utilizadorAutenticado.getId();
                int tipoUserDestino = 1;
                int idUser = utilizadorAutenticado.getId();
                if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)){
                  System.out.println("Pedido de revisao rejeitado com sucesso!");
                }
              } else {
                System.out.println("Erro ao rejeitar pedido de revisao!");
              }
            } else {
              if (aceitar == 9){
                System.out.println("Operacao Cancelada.");
              } else {
                System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
              }
            }
          }
        } else {
          System.out.println("Nao existem registos de pedidos de revisao pendentes.");
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }

        
    // Opção 3.3 - Consultar os meus Processos de Revisão
    private static void opcao3_3(Utilizadores utilizadorAutenticado, Statement st, int entradaMenu) {
      int opcao = 1;
      
      if(entradaMenu == 1){
        opcao = lerDadosInt("Escolheu [Consultar Processos de Revisao]. Pretende continuar? (1 - Sim / 0 - Nao)");
      }
      else {
    	opcao = 1;  
      }
      
       if (opcao == 1) {
          String acao = "Consultar os Meus Processos Revisao"; 
          gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
          String listagem = gereRevisoes.listarPedidosRevisaoDeRevisor(st, utilizadorAutenticado.getId(), 1);
          if (listagem != null && listagem != ""){
            System.out.println("----------------------------------------------------------");
            System.out.println("|           LISTAGEM PROCESSOS DE REVISAO                |");
            System.out.println("----------------------------------------------------------");
            System.out.println(listagem);
            System.out.println("----------------------------------------------------------");
          }
          else {
              System.out.println("Nao existem processos de revisao.");
          }
        } else {
          if (opcao != 0) {
            System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
          }
        }
        System.out.println("Pressione alguma tecla para continuar!");
        limpaBuffer();  
      }
   


    // Opção 3.4 - Concluir Pedidos de Revisão
    private static void opcao3_4(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Concluir Pedidos de Revisao]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Concluir Pedidos de Revisao"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        opcao3_3(utilizadorAutenticado, st, 2);
        int idPedido = lerDadosInt("Por favor introduza o id do pedido que pretende concluir: ");
        String listagemRevisao = gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 1, idPedido);
        if (listagemRevisao != null && listagemRevisao != ""){
	        Float custoRevisao = lerDadosFloat("Por favor introduza o custo da revisao: ");
	        LocalDate dataAtual = LocalDate.now();
	        Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
	        String observacoes = lerDadosString("Por favor introduza as observacoes que desejar: ");
	        if (gereRevisoes.concluirPedidoRevisao(st, dataAtualConvertida, observacoes, custoRevisao, idPedido)) {
	          System.out.println("Pedido de revisao concluido com sucesso!");
	        } else {
	          System.out.println("Erro ao concluir pedido de revisao!");
	        }
        }
        else {
            System.out.println("Pedido de revisao nao encontrado!");    	
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }


    // Opção 4.2 - Solicitar Revisão de Obra
    private static void opcao4_2(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Solicitar Revisao de Obra]. Pretende continuar? (1 - Sim / 0 - Nao)");
      LocalDate dataAtual = LocalDate.now();
      Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
      
      if (opcao == 1){
    	  
        String acao = "Solicitar Revisao Obra"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        System.out.println("Para solicitar uma revisão de obra tem de preencher os seguintes dados. ");
        String titulo;
        
        do {
            titulo = lerDadosString("Titulo da obra: ");
            if (gereObras.verificaTitulo(st, titulo))
              System.out.println("O titulo ja se encontra registado! Por favor, tente outro.");
        } while (gereObras.verificaTitulo(st, titulo));
        
        String subTitulo = lerDadosString("Subtitulo da obra: ");
        String estiloLiterario = lerDadosString("Estilo literario da obra: ");
        int tipoPublicacao = lerDadosInt("Tipo de publicacao da obra [1 - Capa Dura | 2 - Capa Mole (Brochura) | 3 - Livro de Bolso | 4 - Edicao Digital | 5 - Edicao de Luxo]: ");
        int nPaginas = lerDadosInt("Numero de paginas da obra: ");
        int codISBN = gereObras.criaVerificaISBN(st);
        int nEdicao = lerDadosInt("Numero de edicao da obra: ");
        LocalDate dataSubmissaoInicial = LocalDate.now();
        Date dataSubmissaoConvertida = java.sql.Date.valueOf(dataSubmissaoInicial);
        LocalDate dataAprovacao = null;
        LocalDate dataFinal = null;
        
        if(gereObras.adicionarObra(st, titulo, subTitulo, estiloLiterario, tipoPublicacao, nPaginas, codISBN, nEdicao, dataSubmissaoConvertida, dataAprovacao)){
          System.out.println("Obra adicionada com sucesso!");
          if(gereObras.atribuirAutorObra(st, utilizadorAutenticado.getId(), gereObras.ultimoIdObra(st))) {
              System.out.println("Autor adicionado com sucesso a obra!!");
              System.out.println("Estamos a criar um pedido de revisão. Aguarde.");
              
              int idUltimoPedido = gereRevisoes.idUltimoPedido(st);
              String nSerie = gereRevisoes.geraNSerie(idUltimoPedido);
              
              if(gereRevisoes.adicionarPedidoRevisao(st, nSerie, dataSubmissaoConvertida, dataFinal, null, -1, 1, gereObras.ultimoIdObra(st))) {
                String mensagem = "O Utilizador " + utilizadorAutenticado.getNome() + " colocou um pedido de revisao.";
                int tipoUserDestino = 1;
                int idUser = utilizadorAutenticado.getId();
                
                if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)) {
                  System.out.println("Pedido de revisao criado com sucesso!!");
                }
                if(gereRevisoes.atribuirUtilizadorRevisao(st, utilizadorAutenticado.getId(), gereRevisoes.idUltimoPedido(st))) {
                  System.out.println("Autor adicionado com sucesso a revisao!!");
                }
                else {
                  System.out.println("Erro ao adicionar o autor a revisao!!");
                }
              } 
              else {
                System.out.println("Erro ao criar pedido de revisao!!");
              }
          } else {
            System.out.println("Erro ao adicionar o autor a obra!!");
          } 
        } else {
          System.out.println("Erro ao adicionar obra!");
        }
      } else {
          if (opcao != 0) {
            System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
          }
      }
    }
        
    // Opção 4.3 - Consultar Processos de Revisão
    private static void opcao4_3(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Consultar Processos de Revisao]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Consultar Processos Revisao"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        String listagem = gereRevisoes.listarPedidosRevisaoDeAutor(st, utilizadorAutenticado.getId(), 1);
        if (listagem != null && listagem != ""){
          System.out.println("----------------------------------------------------------");
          System.out.println("|           LISTAGEM PROCESSOS DE REVISAO                |");
          System.out.println("----------------------------------------------------------");
          System.out.println(listagem);
          System.out.println("----------------------------------------------------------");
        } else {
          System.out.println("Nao existem registos de processos de revisao.");
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }

        
    // Opção 4.4 - Processos a Pagamento
    private static void opcao4_4(Utilizadores utilizadorAutenticado, Statement st){
      int opcao = lerDadosInt("Escolheu [Processos a Pagamento]. Pretende continuar? (1 - Sim / 0 - Nao)");
      if (opcao == 1){
        String acao = "Processos a Pagamento"; 
        gereLogs.logAcao(st, utilizadorAutenticado.getId(), acao);
        String listagem = gereRevisoes.pesquisarPedidosRevisaoDeAutorEstado(st, utilizadorAutenticado.getId(), 4);
        if (listagem != null && listagem != ""){
          System.out.println("----------------------------------------------------------");
          System.out.println("|           MEUS PROCESSOS A PAGAMENTO                   |");
          System.out.println("----------------------------------------------------------");
          System.out.println(listagem);
          System.out.println("----------------------------------------------------------");
          int idRevisao = lerDadosInt("Para prosseguir o pagamento introduza o id da revisao: "); 
          String listagemRevisao = gereRevisoes.PesquisarPedidosRevisaoIdEstado(st, 1, idRevisao);
          if (listagemRevisao != null && listagemRevisao != "") {
              Float valorAPagar = gereRevisoes.valorAPagar(st, idRevisao);
              System.out.println("Valor a pagar: " + valorAPagar);
              int opcaoPagamento = lerDadosInt("Pretende pagar o processo? (1 - Sim / 0 - Nao)");
              if (opcaoPagamento == 1){
                if (gereRevisoes.alteraEstadoRevisao(st, idRevisao, 5)){
                  LocalDate dataAtual = LocalDate.now();
                  Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
                  int idUser = utilizadorAutenticado.getId();
                  String mensagem = "O seu pedido de revisao " + idRevisao + " foi pago com sucesso pelo utilizador" + idUser;
                  int tipoUserDestino = 3;
                  if(gereNotificacoes.adicionarNotificacao(st, mensagem, dataAtualConvertida, tipoUserDestino, idUser, 0)){
                    System.out.println("Processo pago com sucesso!");
                  }
                } else {
                  System.out.println("Erro ao pagar processo!");
                }  
              } else {
                if (opcaoPagamento != 0){
                  System.out.println("Operacao Cancelada.");
                }
              }
          }
          else {
        	  System.out.println("Nao existe nenhum pedido com esse id");
          }
        } else {
          System.out.println("Nao existem registos de processos de revisao para pagamento.");
        }
      } else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }
}