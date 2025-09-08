import java.io.Serializable;

public class ComponenteAtiva implements Serializable {
  private String designacaoComponenteAtiva;
  private int codigo;
  private int quantidade;

  public ComponenteAtiva(String aDesignacaoComponenteAtiva, int aCodigo, int aQuantidade) {
    designacaoComponenteAtiva = aDesignacaoComponenteAtiva;
    codigo = aCodigo;
    quantidade = aQuantidade;
  }

  public String getDesignacaoComponenteAtiva() {
    return designacaoComponenteAtiva;
  }
}