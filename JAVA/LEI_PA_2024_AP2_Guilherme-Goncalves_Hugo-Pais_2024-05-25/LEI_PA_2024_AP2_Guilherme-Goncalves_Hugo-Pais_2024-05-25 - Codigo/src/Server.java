import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;
import java.time.LocalDate;
import java.sql.*;

public class Server {

    public void servidor(Statement st, GereUtilizadores gereUtilizadores, GereObras gereObras, GereRevisoes gereRevisoes) {
        //int portNumber = 1234;
        
        int portNumber = lerDadosInt("Por favor insira o porto: ");
        

        /*
        if (args != null && args.length == 1) {
            try {
                portNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                System.err.println("** Server ** Error: Unable to retrieve port number; using default value (" + portNumber + ")");
            }
        }
		*/
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        System.out.println("## Server starting ...");
        try {
            serverSocket = new ServerSocket(portNumber); // starts server

            InetAddress address = InetAddress.getLocalHost(); // retrieves local IP            
            System.out.println("## Server up and running at IP " + address.getHostAddress() + ":" + serverSocket.getLocalPort() + " Hostname " + address.getHostName() + " ##" + "\n## Server waiting for connections...");

            clientSocket = serverSocket.accept(); // waits until a client requests a connection
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("## Server Exiting due to error...");
            System.exit(0);
        }

        String receivedMessage;

        try {
            System.out.println("## Client connected from " + clientSocket.getRemoteSocketAddress() + " ##");

            out.println("<server> <hello>;");
            while (true) {
                receivedMessage = in.readLine();
                System.out.println("** Server ** Received Message: " + receivedMessage);

                if (receivedMessage.equals("<client> <hello>;")) {
                    out.println("<server> <ack>;");
                    break;
                } else {
                    out.println("<server> <hello>;");
                }
            }

            while (true) {

                try {
                    receivedMessage=in.readLine();
                    out.println("<server> <ack>;");
                    
                    System.out.println("** Server ** Received: " + receivedMessage);

                    if (receivedMessage == null || receivedMessage.equals("<client> <bye>;")) {
                        System.out.println("** Server ** Connection closed by client.");
                        break;
                    }                        

                    String[] partesMensagem = receivedMessage.split(" ");
                    String operacao = partesMensagem[1];
                    String tipoPesquisa = "";

                    if (partesMensagem.length > 2) {
                        tipoPesquisa = partesMensagem[2];
                    }
                    
                    // AUTENTICAR
                    if (operacao.equals("<autenticar>")) {
                        String resposta = autenticacao(st, partesMensagem, gereUtilizadores);
                        out.println(resposta);
                    } 
                    // CONSULTAR DADOS PESSOAIS
                    else if (operacao.equals("<info>")) {
                        String resposta = consultaDadosPessoais(st, partesMensagem, gereUtilizadores);
                        out.println(resposta);
                    } 
                    // EDITAR DADOS PESSOAIS
                    else if (operacao.equals("<update>")) {
                        String resposta = editaDadosPessoais(st, partesMensagem, gereUtilizadores);
                        out.println(resposta);
                    }
                    // INSERIR OBRA
                    else if (operacao.equals("<inserir>")) {
                        String resposta = insereObra(st, partesMensagem, gereObras);
                        out.println(resposta);
                    } 
                    // PESQUISAR OBRA
                    else if (operacao.equals("<pesquisa>") && tipoPesquisa.equals("<obra>")) {
                        String resposta = pesquisaObra(st, partesMensagem, gereObras);
                        out.println(resposta);
                    } 
                    // PESQUISAR REVISÃO
                    else if (operacao.equals("<pesquisa>") && tipoPesquisa.equals("<revisao>")) {
                        String resposta = pesquisaRevisao(st, partesMensagem, gereRevisoes);
                        out.println(resposta);
                    } 
                    // LISTAR OBRAS
                    else if (operacao.equals("<listar>") && tipoPesquisa.equals("<obra>")) {
                        String resposta = listarObras(st, partesMensagem, gereObras);
                        out.println(resposta);
                    } 
                    // LISTAR REVISÕES
                    else if (operacao.equals("<listar>") && tipoPesquisa.equals("<revisao>")) {
                        String resposta = listarRevisoes(st, partesMensagem, gereRevisoes);
                        out.println(resposta);
                    } 
                    else 
                       out.println("** Server ** Error: Unrecognized command.");
                    
                    receivedMessage = in.readLine();
                    System.out.println("** Server ** Received: " + receivedMessage);
                       
                }catch (SocketException se) {
                System.out.println("Error: Socket closed " + se.getMessage());
                break;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("## Server Exiting...");
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
                System.out.println("## Server closed connection with client.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    System.out.println("## Server Exiting...");
}

private static String autenticacao(Statement st, String[] partesMensagem, GereUtilizadores gereUtilizadores) {

    String [] parametros = funcaoSplit(partesMensagem);
    String username = parametros[0];
    String password = parametros[1];

    Utilizadores utilizador = gereUtilizadores.verificaLoginUtilizador(st, username, password); // Passar o st por parâmetro para esta função e para a classe

    if (utilizador != null) {
        return "<server> <auth_success> <" + utilizador.getId() + ">;";
    } 
    else {
        return "<server> <auth_fail>;";
    }
}

private static String consultaDadosPessoais(Statement st, String[] partesMensagem, GereUtilizadores gereUtilizadores) {

    int idUtilizador = Integer.parseInt(partesMensagem[2].substring(1, partesMensagem[2].length() - 2));
    
    Utilizadores utilizador = gereUtilizadores.pesquisaPorId(st, idUtilizador); // Passar o st por parâmetro para esta função e para a classe
    return "<server> <info> <" + utilizador.getLogin() + "," + utilizador.getPassword() + "," + utilizador.getNome() + "," + utilizador.getEmail() + "," + utilizador.getEstado() + ">;";
}

private static String editaDadosPessoais(Statement st, String[] partesMensagem, GereUtilizadores gereUtilizadores) {

    String tipoUpdate = partesMensagem[2];

    String [] parametros = funcaoSplit(partesMensagem);
    
    int idUtilizador = Integer.parseInt(parametros[0]);

    if (tipoUpdate.equals("<username>")) {
        String username = parametros[1];
        if(gereUtilizadores.atualizaUsernameAutores(st, idUtilizador, username))
        	return "<server> <update> <ok>;";
    }
    else if (tipoUpdate.equals("<nome>")) {
        String nome = parametros[1];
        if(gereUtilizadores.atualizaNome(st, idUtilizador, nome))
        	return "<server> <update> <ok>;";

    }
    else if (tipoUpdate.equals("<password>")) {
        String password = parametros[1];
        if(gereUtilizadores.atualizaPassword(st, idUtilizador, password))
        	return "<server> <update> <ok>;";
    }
    else if (tipoUpdate.equals("<email>")) {
        String email = parametros[1];
        if(gereUtilizadores.verificaFormatoEmail(email)) {
        	if(gereUtilizadores.verificaEmail(st, email) == false) {
        		if(gereUtilizadores.atualizaEmail(st, idUtilizador, email))
                	return "<server> <update> <ok>;";
        	}	
        }
    }
    else if (tipoUpdate.equals("<nif>")) {
        int nif = Integer.parseInt(parametros[1]);
        if(gereUtilizadores.atualizaNIFAutores(st, idUtilizador, nif))
        	return "<server> <update> <ok>;";
    }
    else if (tipoUpdate.equals("<telefone>")) {
        int telefone = Integer.parseInt(parametros[1]);
        if (gereUtilizadores.verificaFormatoTelefone(telefone)) {
        	if(gereUtilizadores.atualizaTelefoneAutores(st, idUtilizador, telefone))
            	return "<server> <update> <ok>;";
        }   
    }
    else if (tipoUpdate.equals("<morada>")) {
        String morada = parametros[1];
        if(gereUtilizadores.atualizaMoradaAutores(st, idUtilizador, morada))
        	return "<server> <update> <ok>;";
    }
    
    return "<server> <update> <fail>;";
}

private static String insereObra(Statement st, String[] partesMensagem, GereObras gereObras) {

    String [] parametros = funcaoSplit(partesMensagem);
    int idUtilizador = Integer.parseInt(parametros[0]);
    String titulo = parametros[1];
    String subtitulo = parametros[2];
    String estilo_literario = parametros[3];
    int tipo = Integer.parseInt(parametros[4]);
    int num_paginas = Integer.parseInt(parametros[5]);
    int num_edicao = Integer.parseInt(parametros[6]);

    int codISBN = gereObras.criaVerificaISBN(st);

    LocalDate dataSubmissaoInicial = LocalDate.now();
    Date dataSubmissaoConvertida = java.sql.Date.valueOf(dataSubmissaoInicial);
    LocalDate dataAprovacao = null;

    if(gereObras.adicionarObra(st, titulo, subtitulo, estilo_literario, tipo, num_paginas, codISBN, num_edicao, dataSubmissaoConvertida, dataAprovacao)) {
        gereObras.atribuirAutorObra(st, idUtilizador, gereObras.ultimoIdObra(st));

        return "<server> <inserir> <obra> <ok>;";
    }

    return "<server> <inserir> <obra> <fail>;";
}

private static String pesquisaObra(Statement st, String[] partesMensagem, GereObras gereObras) {

    String [] parametros = funcaoSplit(partesMensagem);
    int idUtilizador = Integer.parseInt(parametros[0]);
    String titulo = parametros[1];

    String listagemObras = gereObras.pesquisarObrasDeAutorTitulo(st, idUtilizador, titulo);

    if (listagemObras != null && listagemObras != "") {
        return "<server> <pesquisa> <obra> <" + listagemObras + ">;";
    }

    return "<server> <pesquisa> <obra> <fail>;"; 
}
   
private static String pesquisaRevisao(Statement st, String[] partesMensagem, GereRevisoes gereRevisoes) {
    
    String [] parametros = funcaoSplit(partesMensagem);
    int idUtilizador = Integer.parseInt(parametros[0]);
    String num_serie = parametros[1];

    String listagemRevisao = gereRevisoes.pesquisarPedidosRevisaoDeAutorDataTitulo(st, idUtilizador, 3, num_serie);

    if (listagemRevisao != null && listagemRevisao != "") {
        return "<server> <pesquisa> <revisao> <" + listagemRevisao + ">;";
    }

    return "<server> <pesquisa> <revisao> <fail>;";
}

private static String listarObras(Statement st, String[] partesMensagem, GereObras gereObras) {
    String [] parametros = funcaoSplit(partesMensagem);
    int idUtilizador = Integer.parseInt(parametros[0]);

    String listagemObras = gereObras.listarObrasDeAutor(st, idUtilizador, 1);
        
    if (listagemObras != null && listagemObras != "") {
        return "<server> <listar> <obra> <" + listagemObras + ">;";
    }
    
    return "<server> <listar> <obra> <fail>;";
}

private static String listarRevisoes(Statement st, String[] partesMensagem, GereRevisoes gereRevisoes) {
    String [] parametros = funcaoSplit(partesMensagem);
    int idUtilizador = Integer.parseInt(parametros[0]);
    
    String listagemRevisao = gereRevisoes.listarPedidosRevisaoDeAutor(st, idUtilizador, 2);
    
    if (listagemRevisao != null && listagemRevisao != ""){
        return "<server> <listar> <revisao> <" + listagemRevisao + ">;";
    }
    
    return "<server> <listar> <revisao> <fail>;";	
}

private static String[] funcaoSplit(String [] partesMensagem) {
    String [] parametros = null;
    if (partesMensagem.length == 3){
        parametros = partesMensagem[2].substring(1, partesMensagem[2].length() - 2).split(",");
    }
    else {
        if (partesMensagem.length == 4) {
            parametros = partesMensagem[3].substring(1, partesMensagem[3].length() - 2).split(",");
        }
    }
    return parametros;
}

private static int lerDadosInt(String aMensagem) {
    System.out.println(aMensagem);
    return new Scanner(System.in).nextInt();
  }

}

