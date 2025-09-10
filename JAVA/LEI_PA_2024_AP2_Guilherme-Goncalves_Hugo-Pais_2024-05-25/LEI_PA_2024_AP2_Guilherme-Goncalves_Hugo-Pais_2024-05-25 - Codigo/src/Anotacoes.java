/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Representa a classe de Anotações, que são relevantes para o revisor incluir notas sobre as obras em análise.
 * Os anotações são serializáveis.
 */
import java.io.Serializable;

public class Anotacoes {
  private int idAnotacao;
  private String descricao;
  private int pagina;
  private int paragrafo;
  private String dataAnotacao;

  /**
   * Construtor da classe Anotacoes.
   *
   * @param aIdAnotacao    Id da anotação.
   * @param aDescricao     Descrição da anotação.
   * @param aPagina        Página associada à anotação.
   * @param aParagrafo     Parágrafo associado à anotação.
   * @param aDataAnotacao  Data da anotação.
   */
  public Anotacoes(int aIdAnotacao, String aDescricao, int aPagina, int aParagrafo, String aDataAnotacao) {
    idAnotacao = aIdAnotacao;
    descricao = aDescricao;
    pagina = aPagina;
    paragrafo = aParagrafo;
    dataAnotacao = aDataAnotacao;
  }
}
