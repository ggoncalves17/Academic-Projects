/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Representa a classe de Autores, que são utilizadores do sistema com funções específicas.
 * Os autores estendem a classe Utilizadores e são serializáveis.
 */
import java.io.Serializable;

public class Autores extends Utilizadores implements Serializable {
  private int nif;
  private String morada;
  private int telefone;
  private String estiloLiterario;
  private String dataInicioAtividade;

  /**
   * Construtor que inicializa um autor com os atributos fornecidos.
   *
   * @param aLogin                Login do autor.
   * @param aPassword             Password do autor.
   * @param aNome                 Nome do autor.
   * @param aEstado               Estado do autor.
   * @param aEmail                Email do autor.
   * @param aTipo                 Tipo de autor.
   * @param aNif                  NIF do autor.
   * @param aMorada               Morada do autor.
   * @param aTelefone             Telefone do autor.
   * @param aEstiloLiterario      Estilo literário do autor.
   * @param aDataInicioAtividade  Data de início da atividade do autor.
   */
  public Autores(String aLogin, String aPassword, String aNome, boolean aEstado, String aEmail, int aTipo, int aNif, String aMorada, int aTelefone, String aEstiloLiterario, String aDataInicioAtividade) {
    super(aLogin, aPassword, aNome, aEstado, aEmail, aTipo);
    nif = aNif;
    morada = aMorada;
    telefone = aTelefone;
    estiloLiterario = aEstiloLiterario;
    dataInicioAtividade = aDataInicioAtividade;
  }

  
  // Setters

  /**
   * Define o NIF do autor.
   * @param aNif - NIF a ser definido.
   */
  public void setNif(int aNif) {
    nif = aNif;
  }

  /**
   * Define a morada do autor.
   * @param aMorada - Morada a ser definida.
   */
  public void setMorada(String aMorada) {
    morada = aMorada;
  }

  /**
   * Define o telefone do autor.
   * @param aTelefone - Telefone a ser definido.
   */
  public void setTelefone(int aTelefone) {
    telefone = aTelefone;
  }

  
  // Getters

  /**
   * Obtém o nome do autor.
   * @return nome do autor.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Obtém o NIF do autor.
   * @return NIF do autor.
   */
  public int getNif() {
    return nif;
  }

  /**
   * Obtém o telefone do autor.
   * @return telefone do autor.
   */
  public int getTelefone() {
    return telefone;
  }

  /**
   * Obtém a morada do autor.
   * @return Morada do autor.
   */
  public String getMorada() {
    return morada;
  }

  /**
   * Imprime informações sobre a subclasse Autores.
   * Este método é utilizado para apresentar informações específicas da subclasse.
   */
  public void info() {
    System.out.println("SubClasse : Autor ou Revisor");
  }

  /**
   * Devolve uma representação em formato de string deste objeto Autores.
   * A string contém as informações genéricas do utilizador, além do NIF, morada e telefone do autor.
   * @return String com as informações do autor formatadas.
   */
  public String toString() {
    return super.toString() + " [NIF] - " + nif + " [MORADA] - " + morada + " [TELEFONE] - " + telefone;
  }
}