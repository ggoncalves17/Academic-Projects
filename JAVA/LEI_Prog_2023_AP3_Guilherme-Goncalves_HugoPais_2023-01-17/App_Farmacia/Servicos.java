import java.util.Vector;
import java.io.Serializable;

public class Servicos implements Serializable {
  private int idUtilizador;
  private int idFarmaceutico;
  private Vector<Medicamentos> listaMedicamentos;
  private float precoTotal;
  private String data;
  private int tempoServico;
  private String descricao;
  private boolean urgencia;
  private int estadoProcesso;
  private int codigoServico;

  // Construtor
  public Servicos(int aIdUtilizador, int aIdFarmaceutico, Vector<Medicamentos> aListaMedicamentos, float aPrecoTotal,
      String aData, int aTempoServico, String aDescricao, boolean aUrgencia, int aEstadoProcesso, int aCodigoServico) {
    idUtilizador = aIdUtilizador;
    idFarmaceutico = aIdFarmaceutico;
    listaMedicamentos = aListaMedicamentos;
    precoTotal = aPrecoTotal;
    data = aData;
    tempoServico = aTempoServico;
    descricao = aDescricao;
    urgencia = aUrgencia;
    estadoProcesso = aEstadoProcesso;
    codigoServico = aCodigoServico;
  }

  // Getters
  public int getIdUtilizador() {
    return idUtilizador;
  }

  public int getIdFarmaceutico() {
    return idFarmaceutico;
  }

  public int getEstadoProcesso() {
    return estadoProcesso;
  }

  public int getTempoServico() {
    return tempoServico;
  }

  public String getData() {
    return data;
  }

  public String getDescricao() {
    return descricao;
  }

  public int getCodigoServico() {
    return codigoServico;
  }

  public Vector<Medicamentos> getListaMedicamentos() {
    return listaMedicamentos;
  }

  // Setters
  public void setEstadoProcesso(int aEstadoProcesso) {
    estadoProcesso = aEstadoProcesso;
  }

  public void setIdFarmaceutico(int aIdFarmaceutico) {
    idFarmaceutico = aIdFarmaceutico;
  }

  public void setTempoServico(int aTempoServico) {
    tempoServico = aTempoServico;
  }

  public void setPrecoTotal(float aPrecoTotal) {
    precoTotal = aPrecoTotal;
  }

  public String toString() {
    return "[CodigoServico] - " + codigoServico + " [IdUtilizador] - " + idUtilizador + " [IdFarmaceutico] - " + idFarmaceutico 
    + " [ESTADO PROCESSO] - " + estadoProcesso;
  }

}
