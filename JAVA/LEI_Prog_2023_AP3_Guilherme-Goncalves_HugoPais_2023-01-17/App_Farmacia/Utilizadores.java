import java.io.Serializable;

public class Utilizadores implements Comparable<Utilizadores>, Serializable {
  protected String login;
  protected String password;
  protected String nome;
  protected boolean estado;
  protected String email;
  protected int tipo;
  protected int id;

  public Utilizadores(String aLogin, String aPassword, String aNome, boolean aEstado, String aEmail, int aTipo, int aId) {
    login = aLogin;
    password = aPassword;
    nome = aNome;
    estado = aEstado;
    email = aEmail;
    tipo = aTipo;
    id = aId;
  }

  public Utilizadores() {
    this(null, null, null, false, null, -1, -1);
  }

  // Getters
  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getNome() {
    return nome;
  }

  public int getTipo() {
    return tipo;
  }

  public boolean getEstado() {
    return estado;
  }

  public int getId() {
    return id;
  }

  // Setters
  public void setLogin(String aLogin) {
    login = aLogin;
  }

  public void setPassword(String aPassword) {
    password = aPassword;
  }

  public void setNome(String aNome) {
    nome = aNome;
  }

  public void setEstado(boolean aEstado) {
    estado = aEstado;
  }

  public void setEmail(String aEmail) {
    email = aEmail;
  }

  public void setTipo(int aTipo) {
    tipo = aTipo;
  }

  public int compareTo(Utilizadores aUtilizador) {
    return nome.compareTo(aUtilizador.getNome());
  }

  public void info() {
    System.out.println("SuperClasse: Utilizadores");
  }

  public String toString() {
    return "[ID] - " + id + " [TIPO] - " + tipo + " [NOME] - " + nome + " [EMAIL] - " + email;
  }
}
