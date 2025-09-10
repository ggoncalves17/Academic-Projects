/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Representa a classe de Notificações e integra todos os atributos necessários para difundir mensagens associadas a ações desenvolvidas no sistema.
 * As notificações são serializáveis.
 */
import java.io.Serializable;

public class Notificacoes {
  private int idNotificacao;
  private String mensagem;
  private String dataNotificacao;
  private String tipoUserDestino;
  private boolean vista;

  /**
   * Construtor da classe Notificacoes.
   * @param aMensagem         A mensagem da notificação.
   * @param aDataNotificacao  A data da notificação.
   * @param aVista            Indica se a notificação foi vista ou não.
   */
  public Notificacoes(String aMensagem, String aDataNotificacao, boolean aVista) {
    mensagem = aMensagem;
    dataNotificacao = aDataNotificacao;
    vista = aVista;
  }

  /**
   * Define o Id da notificação.
   * @param aId - Id da notificação.
   */
  public void setId(int aId){
    idNotificacao = aId;
  }

  /**
   * Define se a notificação foi vista ou não.
   * @param aVista - Indica se a notificação foi vista ou não.
   */
  public void setVista(boolean aVista){
    vista = aVista;
  }

  /**
   * Obtém o ID da notificação.
   * @return idNotificacao, isto é, O Id da notificação.
   */
  public int getId(){
    return idNotificacao;
  }

  /**
   * Obtém a mensagem da notificação.
   * @return mensagem com a mensagem da notificação.
   */
  public String getMensagem(){
    return mensagem;
  }

  /**
   * Obtém a data da notificação.
   * @return da data da notificação.
   */
  public String getDataNotificacao(){
    return dataNotificacao;
  }

  /**
   * Verifica se a notificação foi vista.
   * @return true se a notificação foi vista, false caso contrário.
   */
  public boolean getVista(){
    return vista;
  }

  /**
   * Retorna uma representação em string da notificação.
   * @return da string formatada em específico para a notificação.
   */
  public String toString() {
    return "[ID] - " + idNotificacao + " [DATA-NOTIFICACAO] - " + dataNotificacao + " [MENSAGEM] - " + mensagem; 
  }
}