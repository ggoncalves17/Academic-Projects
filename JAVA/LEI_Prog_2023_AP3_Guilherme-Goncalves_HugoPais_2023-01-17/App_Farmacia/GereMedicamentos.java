import java.util.Vector;
import java.util.Enumeration;
import java.util.Collections;
import java.io.Serializable;

class GereMedicamentos implements Serializable {

  private Vector<Medicamentos> listaMedicamentos;

  GereMedicamentos() {
    listaMedicamentos = new Vector<Medicamentos>();
  }

  // Adicionar Medicamentos
  public boolean adicionarMedicamento(Medicamentos aMedicamento) {
    if (listaMedicamentos != null && listaMedicamentos != null)
      return listaMedicamentos.add(aMedicamento);

    return false;
  }

  // Ordenamento dos Medicamentos
  public boolean ordenar() {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {
      Collections.sort(listaMedicamentos);
      return true;
    }
    return false;
  }

  // Listagem dos Medicamentos
  public String listarMedicamentos() {

    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {

      Enumeration<Medicamentos> indice = listaMedicamentos.elements();
      String listagemMedicamentos = "";
      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();
        listagemMedicamentos += medicamento + "\n";
      }
      return listagemMedicamentos;
    }
    return null;
  }

  // Pesquisa de Medicamentos por designação
  public Medicamentos pesquisarPorDesignacao(String aNome) {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {

      Enumeration<Medicamentos> indice = listaMedicamentos.elements();
      String listagemMedicamentos = "";
      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();
        if (medicamento.getNome().equalsIgnoreCase(aNome))
          return medicamento;
      }
    }
    return null;
  }

  // Pesquisa de Medicamentos por categoria
  public String pesquisarPorCategoria(String aNomeCategoria) {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {

      Enumeration<Medicamentos> indice = listaMedicamentos.elements();
      String listagemMedicamentos = "";
      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();

        Categoria[] categorias = medicamento.getCategorias();

        for (int i = 0; i < categorias.length; i++) {
          if (categorias[i].getDesignacaoCategoria().equalsIgnoreCase(aNomeCategoria))
            listagemMedicamentos += medicamento + "\n";
        }
      }
      return listagemMedicamentos;
    }
    return null;
  }

  // Pesquisa de Medicamentos por Componente Ativa
  public Vector<Medicamentos> pesquisarPorComponenteAtiva(String aNomeComponente) {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {

      Enumeration<Medicamentos> indice = listaMedicamentos.elements();

      Vector<Medicamentos> medicamentosPretendidos = new Vector<>();

      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();

        ComponenteAtiva componenteAtiva = medicamento.getComponenteAtiva();
        String designacaoComponenteAtiva = componenteAtiva.getDesignacaoComponenteAtiva();

        if (designacaoComponenteAtiva.equalsIgnoreCase(aNomeComponente))
          medicamentosPretendidos.add(medicamento);
      }
      return medicamentosPretendidos;
    }
    return null;
  }

  // Pesquisa de Medicamentos Genéricos
  public String pesquisaMedicamentoGenerico() {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {

      Enumeration<Medicamentos> indice = listaMedicamentos.elements();
      String listagemMedicamentos = "";
      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();

        if (medicamento.getGenericos())
          listagemMedicamentos += medicamento + "\n";
      }
      return listagemMedicamentos;
    }
    return null;
  }

  // Pesquisa de Medicamentos Não Genéricos
  public String pesquisaMedicamentoNaoGenerico() {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {

      Enumeration<Medicamentos> indice = listaMedicamentos.elements();
      String listagemMedicamentos = "";
      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();

        if (medicamento.getGenericos() == false)
          listagemMedicamentos += medicamento + "\n";
      }
      return listagemMedicamentos;
    }
    return null;
  }

  // Pesquisa de Medicamentos com quantidade de stock abaixo de um limite
  public String pesquisaPorStock(int aLimiteStock) {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {

      Enumeration<Medicamentos> indice = listaMedicamentos.elements();
      String listagemMedicamentos = "";
      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();

        if (medicamento.getStock() < aLimiteStock)
          listagemMedicamentos += medicamento + "\n";
      }
      return listagemMedicamentos;
    }
    return null;
  }

  // Alterar stock de medicamentos vendidos
  public boolean alterarStock(String aNomeMedicamento) {
    if (listaMedicamentos != null && listaMedicamentos.size() > 0) {
      Enumeration<Medicamentos> indice = listaMedicamentos.elements();

      while (indice.hasMoreElements()) {
        Medicamentos medicamento = indice.nextElement();
        if (medicamento.getNome().equalsIgnoreCase(aNomeMedicamento)) {

          medicamento.setStock(medicamento.getStock() - 1);
          return true;
        }
      }
    }
    return false;
  }

}