/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe responsável pela gestão das notificações no sistema.
 * Esta classe oferece métodos para adicionar, listar, alterar informações relativas às notificações na base de dados.
 */

import java.io.ObjectInputStream.GetField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class GereNotificacoes {

  /**
   * Obtém Id da última notificação inserida e presente na base de dados.
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return do Id da última notificação inserida na base de dados, ou -1 em caso de erro.
   */
  public int ultimoIdNotificacao(Statement st) {
    try {
      ResultSet rs = st.executeQuery(" SELECT MAX(idnotificacao) AS 'ultimoid' FROM notificacoes");

      int ultimoId = -1;

      while (rs.next()) {
        ultimoId = rs.getInt("ultimoid");
      }
      return ultimoId;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return -1;
    }
  }

  /**
   * Adiciona uma notificação à base de dados.
   *
   * @param st                   Uma instância de Statement para executar queries na base de dados.
   * @param aMensagem            Mensagem da notificação.
   * @param aDataAtualConvertida Data atual no formato Date.
   * @param aTipoUserDestino     Tipo de utilizador associado ao destinatário da notificação.
   * @param aIdUser              Id do utilizador associado à notificação (se for individual).
   * @param aVista               Indica se a notificação foi vista (0 - não vista, 1 - vista).
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean adicionarNotificacao(Statement st, String aMensagem, Date aDataAtualConvertida, int aTipoUserDestino,
      int aIdUser, int aVista) {
    try {
      st.execute("INSERT INTO notificacoes (mensagem, datanotificacao, tipouserdestino, vista) " +
          " VALUES ('" + aMensagem + "', '" + aDataAtualConvertida + "', " + aTipoUserDestino + ", " + aVista + ")");

      int ultimoIdNotificacao = ultimoIdNotificacao(st);

      st.execute("INSERT INTO notificacoes_utilizadores (notificacoes_idnotificacao, utilizadores_idutilizador) " +
          " VALUES (" + ultimoIdNotificacao + ", " + aIdUser + ")");

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados"+ e.getMessage());
      return false;
    }
  }

  /**
   * Lista todas as notificações não vistas e dirigidas aos gestores.
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return string com a listagem das notificações não vistas pelos gestores, ou null em caso de erro.
   */
  public String listarNotificacoesGestores(Statement st) {
    String listagemRevisoes = "";

    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM notificacoes WHERE tipouserdestino = 1 AND vista = 0");

      while (rs.next()) {

        Notificacoes notificacoes = new Notificacoes(rs.getString("mensagem"), rs.getString("datanotificacao"),
            rs.getBoolean("vista"));

        notificacoes.setId(rs.getInt("idnotificacao"));

        listagemRevisoes += notificacoes + "\n";
      }
      return listagemRevisoes;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Lista todas as notificações não vistas por um utilizador específico.
   *
   * @param st             Uma instância de Statement para executar queries na base de dados.
   * @param aIdUtilizador  Id do utilizador cujas notificações serão listadas.
   * @return string com a listagem das notificações não vistas pelo utilizador específico, ou null em caso de erro.
   */
  public String listarNotificacoes(Statement st, int aIdUtilizador, int aUserDestino) {
    String listagemRevisoes = "";

    try {
      ResultSet rs = st.executeQuery(
          " SELECT * FROM notificacoes, notificacoes_utilizadores, utilizadores WHERE idutilizador = utilizadores_idutilizador AND notificacoes_idnotificacao = idnotificacao AND idutilizador = " + aIdUtilizador + " AND vista = 0 AND tipouserdestino = " + aUserDestino);

      while (rs.next()) {

        Notificacoes notificacoes = new Notificacoes(rs.getString("mensagem"), rs.getString("datanotificacao"),
            rs.getBoolean("vista"));

        notificacoes.setId(rs.getInt("idnotificacao"));

        listagemRevisoes += notificacoes + "\n";
      }
      return listagemRevisoes;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados ");
      return null;
    }
  }

  /**
   * Marca todas as notificações dos gestores como vistas (vista = 1).
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean darVistaNotificacoesGestores(Statement st) {
    try {
      st.executeUpdate(" UPDATE notificacoes SET vista = 1 WHERE tipouserdestino = 1");

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return false;
    }
  }

  /**
   * Marca todas as notificações de um utilizador específico como vistas (vista =1).
   *
   * @param st             Uma instância de Statement para executar queries na base de dados.
   * @param aIdUtilizador  Id do utilizador cujas notificações serão marcadas como vistas.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean darVistaNotificacoes(Statement st, int aIdUtilizador, int aUserDestino) {
    try {
      st.executeUpdate(" UPDATE notificacoes SET vista = 1 WHERE idnotificacao IN (SELECT notificacoes_idnotificacao FROM notificacoes_utilizadores WHERE utilizadores_idutilizador = " + aIdUtilizador + ") AND tipouserdestino = " + aUserDestino);

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return false;
    }
  }

  /**
   * Notifica o Gestor de processos em curso sem ser finalizados há mais de 10 dias.
   *
   * @param st             Uma instância de Statement para executar queries na base de dados.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean notificarGestorPendentes(Statement st) {
      LocalDate dataAtual = LocalDate.now();
      Date dataAtualConvertida = java.sql.Date.valueOf(dataAtual);
            
      try {
          ResultSet rs = st.executeQuery("SELECT * FROM revisao WHERE estadorevisao = 3 AND datarealizacao <= DATE_SUB(NOW(), INTERVAL 10 DAY)");

          while (rs.next()) {
              String mensagem = "O pedido de revisão nr " + rs.getInt("idrevisao") + " está pendente há mais de 10 dias.";
              adicionarNotificacao(st, mensagem, dataAtualConvertida, 1, -1, 0); 
          }
          return true;
      } catch (SQLException e) {
        System.out.println("Erro ao ligar a base de dados");
        return false;
      }
  }

}
