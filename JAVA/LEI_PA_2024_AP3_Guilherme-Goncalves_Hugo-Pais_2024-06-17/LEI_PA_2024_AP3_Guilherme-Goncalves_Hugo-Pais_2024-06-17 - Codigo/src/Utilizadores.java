/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
  * Representa um utilizador do sistema.
  * Implementa a interface Comparable para permitir comparação entre utilizadores.
  */
import java.io.Serializable;

public class Utilizadores implements Comparable<Utilizadores> {
  /** Login do utilizador. */
  protected String login;
  
  /** Password do utilizador. */
  protected String password;
  
  /** Nome do utilizador. */
  protected String nome;
  
  /** Estado (ativo ou inativo) do utilizador. */
  protected boolean estado;
  
  /** Email do utilizador. */
  protected String email;
  
  /** Tipo de utilizador (1- Gestor; 2- Revisor; 3- Autor). */
  protected int tipo;
  
  /** Id do utilizador no Sistema. */
  protected int id;


  /**
   * Construtor que inicializa um utilizador com os atributos fornecidos.
   *
   * @param aLogin    Login do utilizador.
   * @param aPassword Password do utilizador.
   * @param aNome     Nome do utilizador.
   * @param aEstado   Estado do utilizador.
   * @param aEmail    Email do utilizador.
   * @param aTipo     Tipo de utilizador.
   */
  public Utilizadores(String aLogin, String aPassword, String aNome, boolean aEstado, String aEmail, int aTipo) {
    login = aLogin;
    password = aPassword;
    nome = aNome;
    estado = aEstado;
    email = aEmail;
    tipo = aTipo;
  }

  /**
   * Construtor vazio que inicializa um utilizador com valores por defeito.
   */
  public Utilizadores() {
    this(null, null, null, false, null, -1);
  }

  
  // Getters

  /**
   * Obtém o login do utilizador.
   * @return login do utilizador.
   */
  public String getLogin() {
    return login;
  }

  /**
   * Obtém a password do utilizador.
   * @return password do utilizador.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Obtém o email do utilizador.
   * @return email do utilizador.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Obtém o nome do utilizador.
   * @return nome do utilizador.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Obtém o tipo do utilizador.
   * @return tipo do utilizador.
   */
  public int getTipo() {
    return tipo;
  }

  /**
   * Obtém o estado do utilizador.
   * @return estado do utilizador.
   */
  public boolean getEstado() {
    return estado;
  }

  /**
   * Obtém o id do utilizador.
   * @return id do utilizador.
   */
  public int getId() {
    return id;
  }

  
  // Setters
  
  /**
   * Define o login do utilizador.
   * @param aLogin - Login a ser definido.
   */
  public void setLogin(String aLogin) {
    login = aLogin;
  }

  /**
   * Define a password do utilizador.
   * @param aPassword - Password a ser definida.
   */
  public void setPassword(String aPassword) {
    password = aPassword;
  }

  /**
   * Define o nome do utilizador.
   * @param aNome - Nome a ser definido.
   */
  public void setNome(String aNome) {
    nome = aNome;
  }

  /**
   * Define o estado do utilizador.
   *
   * @param aEstado - Estado a ser definido.
   */
  public void setEstado(boolean aEstado) {
    estado = aEstado;
  }

  /**
   * Define o email do utilizador.
   *
   * @param aEmail - Email a ser definido.
   */
  public void setEmail(String aEmail) {
    email = aEmail;
  }

  /**
   * Define o tipo do utilizador.
   * @param aTipo - Tipo a ser definido.
   */
  public void setTipo(int aTipo) {
    tipo = aTipo;
  }

  /**
   * Define o id do utilizador.
   * @param aId - Id a ser definido.
   */
  public void setId(int aId) {
    id = aId;
  }

  /**
   * Compara o utilizador com o utilizador especificado.
   * @param aUtilizador O utilizador a ser comparado.
   * @return Um valor negativo, zero ou um valor positivo, dependendo da comparação obtida pelo nome.
   */
  public int compareTo(Utilizadores aUtilizador) {
    return nome.compareTo(aUtilizador.getNome());
  }

  /**
   * Imprime informações sobre a superclasse Utilizadores.
   * Este método é utilizado para apresentar informações específicas da superclasse.
   */
  public void info() {
    System.out.println("SuperClasse: Utilizadores");
  }

  /**
   * Devolve o formato de apresentação toString deste objeto Utilizadores.
   * A string contém o id, tipo, nome, email e estado do utilizador.
   * @return String com as informações do utilizador formatadas.
   */
  public String toString() {
    return "[ID] - " + id + " [TIPO] - " + tipo + " [NOME] - " + nome + " [EMAIL] - " + email + " [ESTADO] - " + estado;
  }
}
