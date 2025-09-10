/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe que representa uma obra.
 * As obras são serializáveis.
 */
import java.io.Serializable;

public class Obras {
  private int idObra;
  private Autores autor;
  private String titulo;
  private String subTitulo;
  private String estiloLiterario;
  private int tipoPublicacao;
  private int nPaginas;
  private int codISBN;
  private int nEdicao;
  private String dataSubmissao;
  private String dataAprovacao;

  /**
   * Construtor da classe Obras.
   * @param aAutor           Autor da obra.
   * @param aTitulo          Título da obra.
   * @param aSubTitulo       Subtítulo da obra.
   * @param aEstiloLiterario Estilo literário da obra.
   * @param aTipoPublicacao  Tipo de publicação da obra.
   * @param aNPaginas        Número de páginas da obra.
   * @param aCodISBN         Código ISBN da obra.
   * @param aNEdicao         Número da edição da obra.
   * @param aDataSubmissao   Data de submissão da obra.
   * @param aDataAprovacao   Data de aprovação da obra.
   */
  public Obras(Autores aAutor, String aTitulo, String aSubTitulo, String aEstiloLiterario, int aTipoPublicacao, int aNPaginas, int aCodISBN, int aNEdicao, String aDataSubmissao, String aDataAprovacao) {
    autor = aAutor;
    titulo = aTitulo;
    subTitulo = aSubTitulo;
    estiloLiterario = aEstiloLiterario;
    tipoPublicacao = aTipoPublicacao;
    nPaginas = aNPaginas;
    codISBN = aCodISBN;
    nEdicao = aNEdicao;
    dataSubmissao = aDataSubmissao;
    dataAprovacao = aDataAprovacao;   
  }

  /**
   * Define o ID da obra.
   * @param aId - Id da obra.
   */
  public void setId(int aId) {
      idObra = aId; 
    }
  
  /**
   * Devolve o ID da obra.
   * @return idObra.
   */
  public int getId() {
    return idObra;
  }

  /**
   * Devolve o título da obra.
   * @return do título da obra.
   */
  public String getTitulo() {
    return titulo;
  }

  /**
   * Devolve uma representação da string com os atributos da obra.
   * @return string formatada com as informações da obra.
   */
  public String toString() {
    return "[ID] - " + idObra + " [DATA-SUBMISSAO] - " + dataSubmissao + " [TITULO-OBRA] - " + titulo + " [AUTOR] - " + autor.getNome();
  }
}
