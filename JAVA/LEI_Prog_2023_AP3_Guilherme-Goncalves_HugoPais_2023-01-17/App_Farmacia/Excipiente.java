import java.io.Serializable;

public class Excipiente implements Serializable  {
    private String designacaoExcipiente;
    private int classificacao;
    private int quantidadeExcipiente;

    public Excipiente(String aDesignacaoExcipiente, int aClassificacao, int aQuantidadeExcipiente) {
        designacaoExcipiente = aDesignacaoExcipiente;
        classificacao = aClassificacao;
        quantidadeExcipiente = aQuantidadeExcipiente;
    }
}
