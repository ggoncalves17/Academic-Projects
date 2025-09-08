import java.io.Serializable;

public class Categoria implements Serializable {
  private String designacaoCategoria;
  private int classificacao;
  private static int codigoCategoria = 0;
  private String fornecedor;

  public Categoria(String aDesignacaoCategoria, int aClassificacao, String aFornecedor) {
    designacaoCategoria = aDesignacaoCategoria;
    classificacao = aClassificacao;
    fornecedor = aFornecedor;
    codigoCategoria++;
  }

  public String getDesignacaoCategoria() {
    return designacaoCategoria;
  }
}
