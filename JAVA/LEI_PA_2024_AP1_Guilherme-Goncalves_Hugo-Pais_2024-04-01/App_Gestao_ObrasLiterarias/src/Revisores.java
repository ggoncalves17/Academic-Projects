/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Representa a classe de Revisores, que são utilizadores do sistema com funções específicas.
 * Os revisores estendem a classe Utilizadores e são serializáveis.
 */
import java.io.Serializable;

public class Revisores extends Utilizadores implements Serializable {
  private int nif;
  private String morada;
  private int telefone;
  private String areaEspecializacao;
  private String formacaoAcademica;

  /**
   * Construtor que inicializa um revisor com os atributos fornecidos.
   *
   * @param aLogin               Login do revisor.
   * @param aPassword            Password do revisor.
   * @param aNome                Nome do revisor.
   * @param aEstado              Estado do revisor.
   * @param aEmail               Email do revisor.
   * @param aTipo                Tipo de revisor.
   * @param aNif                 NIF do revisor.
   * @param aMorada              Morada do revisor.
   * @param aTelefone            Telefone do revisor.
   * @param aAreaEspecializacao  Área de especialização do revisor.
   * @param aFormacaoAcademica   Formação académica do revisor.
   */
  public Revisores(String aLogin, String aPassword, String aNome, boolean aEstado, String aEmail, int aTipo, int aNif, String aMorada, int aTelefone, String aAreaEspecializacao, String aFormacaoAcademica) {
    super(aLogin, aPassword, aNome, aEstado, aEmail, aTipo);
    nif = aNif;
    morada = aMorada;
    telefone = aTelefone;
    areaEspecializacao = aAreaEspecializacao;
    formacaoAcademica = aFormacaoAcademica;
  }

  
  // Setters
  
  /**
   * Define o NIF do revisor.
   * @param aNif - NIF a ser definido.
   */
  public void setNif(int aNif) {
    nif = aNif;
  }

  /**
   * Define a morada do revisor.
   * @param aMorada - Morada a ser definida.
   */
  public void setMorada(String aMorada) {
    morada = aMorada;
  }

  /**
   * Define o telefone do revisor.
   * @param aTelefone - Telefone a ser definido.
   */
  public void setTelefone(int aTelefone) {
    telefone = aTelefone;
  }

  
  // Getters

  /**
   * Obtém o NIF do revisor.
   * @return NIF do revisor.
   */
  public int getNif() {
    return nif;
  }

  /**
   * Obtém o telefone do revisor.
   * @return telefone do revisor.
   */
  public int getTelefone() {
    return telefone;
  }
  
  /**
   * Obtém a morada do revisor.
   * @return Morada do revisor.
   */
  public String getMorada() {
    return morada;
  }

  /**
   * Imprime informações sobre a subclasse Revisores.
   * Este método é utilizado para apresentar informações específicas da subclasse.
   */
  public void info() {
    System.out.println("SubClasse : Autor ou Revisor");
  }

  /**
   * Devolve o formato de apresentação toString deste objeto Revisores.
   *A string contém as informações genéricas do utilizador, além do NIF, morada e telefone do revisor.
   * @return String com as informações do utilizador formatadas.
   */
  public String toString() {
    return super.toString() + " [NIF] - " + nif + " [MORADA] - " + morada + " [TELEFONE] - " + telefone;
  }
 }
