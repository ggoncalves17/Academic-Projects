import java.io.Serializable;

class SistemaInfo implements Serializable {
  private int totalExecucoes;
  private String ultimoUtilizador;

  public SistemaInfo(int aTotalExecucoes, String aUltimoUtilizador) {
      totalExecucoes = aTotalExecucoes;
      ultimoUtilizador = aUltimoUtilizador;
  }

  //Getters
  public int getTotalExecucoes() {
      return totalExecucoes;
  }

  public String getUltimoUtilizador() {
      return ultimoUtilizador;
  }
}