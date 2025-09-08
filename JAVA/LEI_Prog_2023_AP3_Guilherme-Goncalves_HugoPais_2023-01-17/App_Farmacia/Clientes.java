import java.io.Serializable;

public class Clientes extends Utilizadores implements Serializable {
  private int nif;
  private String morada;
  private int telefone;

  // Construtor
  public Clientes(String aLogin, String aPassword, String aNome, boolean aEstado, String aEmail, int aTipo, int aId, int aNif,
      String aMorada, int aTelefone) {
    super(aLogin, aPassword, aNome, aEstado, aEmail, aTipo, aId);
    nif = aNif;
    morada = aMorada;
    telefone = aTelefone;
  }

  Clientes() {
    this(null, null, null, false, null, -1, -1, -1, null, -1);
  }

  // Setters
  public void setNif(int aNif) {
    nif = aNif;
  }

  public void setMorada(String aMorada) {
    morada = aMorada;
  }

  public void setTelefone(int aTelefone) {
    telefone = aTelefone;
  }

  // Getters
  public int getNif() {
    return nif;
  }

  public int getTelefone() {
    return telefone;
  }

  public String getMorada() {
    return morada;
  }

  public void info() {
    System.out.println("SubClasse : Cliente ou Farmaceutico");
  }

  public String toString() {
    return super.toString() + " [NIF] - " + nif + " [MORADA] - " + morada + " [TELEFONE] - " + telefone;}

}
