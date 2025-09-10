import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) {
        //String serverIP = "127.0.0.1";
        //int serverPort = 1234;

    
        String serverIP = lerDadosString("Por favor insira o IP: ");
        int serverPort = lerDadosInt("Por favor insira o porto: ");
        
      
        // Verify input parameters
        if (args != null && args.length == 2) {
            serverIP = args[0];
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                System.err.println("** Client ** Error: Unable to retrieve port number; using default value (" + serverPort + ")");
            }
        }

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        System.out.println("## Client starting ...");
        try {
            socket = new Socket(serverIP, serverPort); // Socket connection to server
            System.out.println("## Client connected to server " + serverIP + ":" + serverPort);
            socket.setReceiveBufferSize(65536);

            out = new PrintWriter(socket.getOutputStream(), true); // Creates buffer through socket to send data
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Creates buffer through socket to receive data

            InetAddress address = InetAddress.getLocalHost(); // Retrieve local IP
            System.out.println("## Client running at IP " + address.getHostAddress() + ":" + socket.getLocalPort() + " Hostname " + address.getHostName() + " ##");

            String receivedMessage;
            while ((receivedMessage = in.readLine()) != null) {
                System.out.println("** Client ** Received: " + receivedMessage);

                if (receivedMessage.equals("<server> <hello>;")) {
                    out.println("<client> <hello>;");
                    receivedMessage = in.readLine();
                    System.out.println("** Client ** Received: " + receivedMessage);
                    break;
                }
            }

            int idUtilizadorAutenticado = -1;
            String mensagemCliente = "";

            // MAIN COM O MENU PARA O AUTOR
            while (true) {

                apresentaMenu(idUtilizadorAutenticado);
                int opcao = lerDadosInt("Por favor selecione uma opcao: ");

                if(opcao == 1 && idUtilizadorAutenticado == -1){
                  mensagemCliente = opcao1();
                  out.println(mensagemCliente);

                  receivedMessage = in.readLine();
                  System.out.println("** Client ** Received: " + receivedMessage);
                  
                  receivedMessage = in.readLine();
                  System.out.println("** Client ** Received: " + receivedMessage);
                  
                  out.println("<cliente> <ack>;");

                  String[] partesMensagem = receivedMessage.split(" ");
                  String operacao = partesMensagem[1];

                  if(operacao.equals("<auth_success>")) {
                    idUtilizadorAutenticado =  Integer.parseInt(partesMensagem[2].substring(1, partesMensagem[2].length() - 2));
                  }
                }
                else{
                  mensagemCliente = executar(idUtilizadorAutenticado, opcao); 
                  out.println(mensagemCliente);
                  
                  if (opcao == 0) {
                	  break;
                  }
                  
                  receivedMessage = in.readLine();
                  System.out.println("** Client ** Received: " + receivedMessage);
                  
                  receivedMessage = in.readLine();
                  System.out.println("** Client ** Received: " + receivedMessage);
                  
                  out.println("<cliente> <ack>;");
                  
                }
            }

        } catch (UnknownHostException uhe) {
            System.err.println("Error: unknown server " + serverIP + ":" + serverPort);
            System.out.println("## Client Exiting...");
            System.exit(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("## Client Exiting...");
            System.exit(0);
        } finally {
            System.out.println("** Client ** Closing socket");
            // Close socket resources
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("## Client Exiting...");
        System.exit(0);
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
  private static void apresentaMenu(int idUtilizadorAutenticado) {

    System.out.println("----------------------------------------------------------");
    System.out.println("|                          MENU                          |");
    System.out.println("----------------------------------------------------------");

    // Utilizador não logado
    if (idUtilizadorAutenticado == -1) {
      System.out.println("|           1. Autenticar Utilizador                     |");
    }
    else {
      // Opções Utilizador logado
      System.out.println("|           1. Consultar Dados Pessoais                  |");
      System.out.println("|           2. Editar Dados Pessoais                     |"); 
      System.out.println("|           3. Inserir Obra                              |"); 
      System.out.println("|           4. Pesquisar Obra                            |");
      System.out.println("|           5. Pesquisar Revisão                         |"); 
      System.out.println("|           6. Listar todas as Obras                     |");
      System.out.println("|           7. Listar todas as Revisões                  |");
    }
    System.out.println("|           0. Sair                                      |");
    System.out.println("----------------------------------------------------------");
  }

  private static String executar(int idUtilizadorAutenticado, int aOpcao) {
      switch (aOpcao) {
        case 0:
          return opcao0();
        case 1:
          return opcao1_1(idUtilizadorAutenticado);
        case 2:
          return opcao2(idUtilizadorAutenticado);
        case 3:
          return opcao3(idUtilizadorAutenticado);
        case 4:
          return opcao4(idUtilizadorAutenticado);
        case 5:
          return opcao5(idUtilizadorAutenticado);
        case 6:
          return opcao6(idUtilizadorAutenticado);
        case 7:
          return opcao7(idUtilizadorAutenticado);
        default:
          erro();
          break;
      }
  return null;
    }

  private static void erro() {
    System.out.println("Opção Inválida!");
  }

  // Opção 0 - Sair
  private static String opcao0() {
    return "<client> <bye>;";
  }
      
  // Opção 1 - Autenticar Utilizador
  private static String opcao1() {
    System.out.println("Insira o Username: ");
    String username = new Scanner(System.in).nextLine();
    System.out.println("Insira a Password: ");
    String password = new Scanner(System.in).nextLine();
    return "<cliente> <autenticar> <" + username + "," + password + ">;";
  }

  // Opção 1_1 - Consultar Dados Pessoais
  private static String opcao1_1(int idUtilizadorAutenticado) {
    return "<cliente> <info> <" + idUtilizadorAutenticado + ">;";
  }

  // Opção 2 - Editar Informacao Pessoal
  private static String opcao2(int idUtilizadorAutenticado) {
      System.out.println("----------------------------------------------------------");
      System.out.println("|                   1. Username                          |"); 
      System.out.println("|                   2. Nome                              |");
      System.out.println("|                   3. Password                          |"); 
      System.out.println("|                   4. Email                             |");
      System.out.println("|                   5. NIF                               |");
      System.out.println("|                   6. Telefone                          |"); 
      System.out.println("|                   7. Morada                            |"); 
      System.out.println("----------------------------------------------------------");
      int escolha = lerDadosInt("Por favor introduza o numero relativo ao campo que pretende alterar: ");
      String mensagem="";

      switch (escolha) {
      case 1:
        System.out.println("Insira o Username: ");
        String username = new Scanner(System.in).nextLine();
        mensagem = "<cliente> <update> <username> <" + idUtilizadorAutenticado + "," + username + ">;";
        break;
      case 2:
        System.out.println("Insira o Nome: ");
        String nome = new Scanner(System.in).nextLine();
        mensagem = "<cliente> <update> <nome> <" + idUtilizadorAutenticado + "," + nome + ">;";
        break;
      case 3:
        System.out.println("Insira a Password: ");
        String password = new Scanner(System.in).nextLine();
        mensagem = "<cliente> <update> <password> <" + idUtilizadorAutenticado + "," + password + ">;";
        break;
      case 4:
        System.out.println("Insira o Email: ");
        String email = new Scanner(System.in).nextLine();
        mensagem = "<cliente> <update> <email> <" + idUtilizadorAutenticado + "," + email + ">;";
        break;
      case 5:
        int nif = lerDadosInt("Insira o NIF: ");
        mensagem = "<cliente> <update> <nif> <" + idUtilizadorAutenticado + "," + nif + ">;";
        break;
      case 6:
        int telefone = lerDadosInt("Insira o Telefone: ");
        mensagem = "<cliente> <update> <telefone> <" + idUtilizadorAutenticado + "," + telefone + ">;";
        break;
      case 7:
        System.out.println("Insira a Morada: ");
        String morada = new Scanner(System.in).nextLine();
        mensagem = "<cliente> <update> <morada> <" + idUtilizadorAutenticado + "," + morada + ">;";
        break;
      }
      return mensagem;
  }

  // Opção 3 - Inserir Obra
  private static String opcao3(int idUtilizadorAutenticado) {
    System.out.println("Insira o Titulo: ");
    String titulo = new Scanner(System.in).nextLine();

    System.out.println("Insira o SubTitulo: ");
    String subtitulo = new Scanner(System.in).nextLine();

    System.out.println("Insira o Estilo Literario: ");
    String estilo_literario = new Scanner(System.in).nextLine();

    int tipo = lerDadosInt("Tipo de publicacao da obra [1 - Capa Dura | 2 - Capa Mole (Brochura) | 3 - Livro de Bolso | 4 - Edicao Digital | 5 - Edicao de Luxo]: ");

    int num_paginas = lerDadosInt("Insira o Número de Páginas: ");

    int num_edicao = lerDadosInt("Insira o Número de Edição: ");

    return "<cliente> <inserir> <obra> <" + idUtilizadorAutenticado + "," + titulo + "," + subtitulo + "," + estilo_literario + "," + tipo + "," + num_paginas + "," + num_edicao +">;";
  }

  // Opção 4 - Pesquisar Obra
  private static String opcao4(int idUtilizadorAutenticado){
    System.out.println("Insira o Titulo: ");
    String titulo = new Scanner(System.in).nextLine();

    return "<cliente> <pesquisa> <obra> <" + idUtilizadorAutenticado + "," + titulo + ">;";
  }

  // Opção 5 - Pesquisar Revisão
  private static String opcao5(int idUtilizadorAutenticado) {
    System.out.println("Insira o Número de Série: ");
    String num_serie = new Scanner(System.in).nextLine();

    return "<cliente> <pesquisa> <revisao> <" + idUtilizadorAutenticado + "," + num_serie + ">;";
  }

  // Opção 6 - Listar todas as Obras
  private static String opcao6(int idUtilizadorAutenticado) {
   return "<cliente> <listar> <obra> <" + idUtilizadorAutenticado + ">;";
  }

  // Opção 7 - Listar todas as Revisões
  private static String opcao7(int idUtilizadorAutenticado) {
    return "<cliente> <listar> <revisao> <" + idUtilizadorAutenticado + ">;";
  }
}
