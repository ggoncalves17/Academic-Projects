import java.util.Vector;
import java.io.Serializable;

public class Medicamentos implements Comparable<Medicamentos>, Serializable {
  private String nomeMedicamento;
  private String marca;
  private String lote;
  private ComponenteAtiva componenteAtiva;
  private int dosagem;
  private int stock;
  private float precoMedicamento;
  private int anoFabrico;
  private boolean autorizacaoMedica;
  private boolean medicamentoGenerico;

  private Excipiente[] excipiente;
  private Categoria[] categoria;

  // PARA PASSAR
  // Construtor
  public Medicamentos(String aNomeMedicamento, String aMarca, String aLote, ComponenteAtiva aComponenteAtiva,
      int aDosagem, int aStock, float aPrecoMedicamento, int aAnoFabrico, boolean aAutorizacaoMedica,
      boolean aMedicamentoGenerico) {
    nomeMedicamento = aNomeMedicamento;
    marca = aMarca;
    lote = aLote;
    componenteAtiva = aComponenteAtiva;
    dosagem = aDosagem;
    stock = aStock;
    precoMedicamento = aPrecoMedicamento;
    anoFabrico = aAnoFabrico;
    autorizacaoMedica = aAutorizacaoMedica;
    medicamentoGenerico = aMedicamentoGenerico;
    excipiente = new Excipiente[5];
    categoria = new Categoria[3];
  }

  // Setters
  public void setStock(int aStock) {
    stock = aStock;
  }

  // Getters
  public String getNome() {
    return nomeMedicamento;
  }

  public Categoria[] getCategorias() {
    return categoria;
  }

  public ComponenteAtiva getComponenteAtiva() {
    return componenteAtiva;
  }

  public boolean getGenericos() {
    return medicamentoGenerico;
  }

  public int getStock() {
    return stock;
  }

  public float getPreco() {
    return precoMedicamento;
  }

  // Ordenar por designação
  public int compareTo(Medicamentos aMedicamento) {
    return nomeMedicamento.compareTo(aMedicamento.getNome());
  }

  // Adicionar Excipiente
  public void addExcipiente(Excipiente aExcipiente) {
    for (int i = 0; i < excipiente.length; i++) {
      if (excipiente[i] == null) {
        excipiente[i] = aExcipiente;
        break;
      }
    }
  }

  // Adicionar Categoria
  public void addCategoria(Categoria aCategoria) {
    for (int i = 0; i < categoria.length; i++) {
      if (categoria[i] == null) {
        categoria[i] = aCategoria;
        break;
      }
    }
  }

  //Método toString
  public String toString() {
    return "[Nome] - " + nomeMedicamento + " [MARCA] - " + marca + " [STOCK] - " + stock + " [PRECO] - " + precoMedicamento;
  }
}