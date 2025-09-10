/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe que representa uma revisão.
 * As revisões são serializáveis.
 */
import java.util.Vector;
import java.io.Serializable;

public class Revisao {
  private int idRevisao;
  private String nSerie;
  private String dataRealizacao;
  private String tempoDecorrido;
  private String observacoes;
  private float custoProcesso;
  private int estado;
  private Obras obra;
  private Autores autor;
  private Utilizadores gestor;
  private Revisores revisor;
  private Vector <Anotacoes> listaAnotacoes;

  /**
   * Construtor da classe Revisao.
   *
   * @param aNSerie          Número de série da revisão.
   * @param aDataRealizacao  Data de realização da revisão.
   * @param aTempoDecorrido  Tempo decorrido desde a realização da revisão.
   * @param aObservacoes     Observações da revisão.
   * @param aCustoProcesso   Custo do processo de revisão.
   * @param aEstado          Estado da revisão.
   * @param aObra            Obra associada à revisão.
   */
  public Revisao(String aNSerie, String aDataRealizacao, String aTempoDecorrido, String aObservacoes, float aCustoProcesso, int aEstado, Obras aObra /*Vector<Anotacoes> aListaAnotacoes*/) {
    nSerie = aNSerie;
    dataRealizacao = aDataRealizacao;
    tempoDecorrido = aTempoDecorrido;
    observacoes = aObservacoes;
    custoProcesso = aCustoProcesso;
    estado = aEstado;
    obra = aObra;
    //listaAnotacoes = aListaAnotacoes;
  }

  /**
   * Define o Id da revisão.
   * @param aId relativo ao Id da revisão.
   */
  public void setId(int aId) {
    idRevisao = aId;
  }

  /**
   * Devolve o Id da revisão.
   * @return id da revisão.
   */
  public int getId() {
    return idRevisao;
  }

  /**
   * Devolve uma representação para a string obtida no objeto revisão.
   * @return da string formatada com as informações da revisão.
   */
  public String toString() {
    return "[ID] - " + idRevisao + " [DATA-CRIACAO] - " + dataRealizacao + " [TITULO-OBRA] - " + obra.getTitulo() + " [ESTADO-REVISAO] - " + estado;
  }
}
