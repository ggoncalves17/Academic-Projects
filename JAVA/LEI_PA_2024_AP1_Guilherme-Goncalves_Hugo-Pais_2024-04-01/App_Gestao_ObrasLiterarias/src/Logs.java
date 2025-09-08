/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe responsável pelo registo de Logs.
 */
public class Logs {
  private int idLog;
  private String dataExecucao;
  private String acao;
  private int idUtilizador;

  /**
   * Construtor da classe Logs.
   * @param aDataExecucao   Data de execução do log.
   * @param aAcao           Ação realizada.
   * @param aIdUtilizador   Id do utilizador relacionado ao log.
   */
  public Logs(String aDataExecucao, String aAcao, int aIdUtilizador){
    dataExecucao = aDataExecucao;
    acao = aAcao;
    idUtilizador = aIdUtilizador;
  }

  /**
   * Define o Id do log.
   * @param aId - Id do log.
   */
  public void setId(int aId) {
    idLog = aId; 
  }

  /**
   * Devolve uma representação textual do objeto Logs.
   * @return da string formatada com as informações do log.
   */
  public String toString() {
    return "[ID] - " + idLog + " [DATA-EXECUCAO] - " + dataExecucao + " [ACAO] - " + acao + " [ID-UTILIZADOR] - " + idUtilizador;
  }
}
  