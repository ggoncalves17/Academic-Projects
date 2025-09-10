/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe responsável pelo registo e apresentação das ações que os utilizadores desenvolvem no sistema.
 * Esta classe oferece métodos para listar, adicionar informações relativas a ações na base de dados.
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;

class GereLogs {

  /**
   * Adiciona um log de ação à base de dados.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param idUtilizador  Utilizador que realizou a ação.
   * @param aAcao         Descrição da ação realizada.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
   public boolean logAcao(Statement st, int aIdUtilizador, String aAcao) {
     
     SimpleDateFormat dataFormato = new SimpleDateFormat("yyyy-MM-dd");
     Date dataAtual = new Date();
     String dataAcao = dataFormato.format(dataAtual);
      
    try {
      st.execute(" INSERT INTO logs(datalogexecucao, acao, utilizadores_idutilizador) " +
             " VALUES ('" + dataAcao + "', '" + aAcao + "', " + aIdUtilizador + ")");

         return true;
    } catch (SQLException e) {
         System.out.println("Erro ao ligar à base de dados ");
         return false;
     }
   }

  /**
   * Lista todos os logs de ações presentes na base de dados.
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return string com a listagem de todos os logs de ações, ou null em caso de erro.
   */
  public String listarLogs(Statement st){
    String listagemLogs = "";
    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM logs");
      while (rs.next()) {
        Logs logs = new Logs(rs.getString("datalogexecucao"), rs.getString("acao"), rs.getInt("utilizadores_idutilizador"));
        
        logs.setId(rs.getInt("idlog"));
        listagemLogs += logs + "\n";
      }
      return listagemLogs;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Lista os logs de ações de um determinado utilizador.
   *
   * @param st           Uma instância de Statement para executar queries na base de dados.
   * @param idUtilizador Id do utilizador cujos logs serão listados.
   * @return string com a listagem dos logs de ações do utilizador, ou null em caso de erro.
   */
  public String listarLogsUtilizador(Statement st, int idUtilizador){
    String listagemLogs = "";
    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM logs WHERE utilizadores_idutilizador = " + idUtilizador);
      while (rs.next()) {
        Logs logs = new Logs(rs.getString("datalogexecucao"), rs.getString("acao"), rs.getInt("utilizadores_idutilizador"));
        
        logs.setId(rs.getInt("idlog"));
        listagemLogs += logs + "\n";
      }
      return listagemLogs;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

}
