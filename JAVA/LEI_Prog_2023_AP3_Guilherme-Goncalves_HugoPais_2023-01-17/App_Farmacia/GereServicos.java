import java.util.Vector;
import java.util.Enumeration;
import java.util.Collections;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.io.Serializable;

class GereServicos implements Serializable {

  private Vector<Servicos> listaServicos;

  GereServicos() {
    listaServicos = new Vector<Servicos>();
  }
  
  //Retornar codigo ultimo servico
  public int ultimoCodigoServico (int aCodigoServico) {
    if (listaServicos != null && listaServicos.size() > 0) {
      Enumeration<Servicos> indices = listaServicos.elements();

      while (indices.hasMoreElements()) {
        Servicos servico = indices.nextElement();
        aCodigoServico = servico.getCodigoServico();
      }
      return aCodigoServico;
    }
    return 0;
  }

  // Adicionar Serviço
  public boolean adicionarServico(Servicos aServico) {
    if (listaServicos != null && listaServicos != null)
      return listaServicos.add(aServico);

    return false;
  }

  // Listagem dos Serviços por tipo de utilizador
  public String listarServicos(int aId, int aTipo) {

    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();
      String listagemServicos = "";

      if (aTipo == 1) {
        while (indice.hasMoreElements()) {
          Servicos servico = indice.nextElement();
          listagemServicos += servico + "\n";
        }
        return listagemServicos;
      } else {
        while (indice.hasMoreElements()) {
          Servicos servico = indice.nextElement();
          if ((servico.getIdUtilizador() == aId || servico.getIdFarmaceutico() == aId) && (servico.getEstadoProcesso() == 3 || servico.getEstadoProcesso() == 4 || servico.getEstadoProcesso() == 5))
            listagemServicos += servico + "\n";
        }
        return listagemServicos;
      }
    }
    return null;
  }

  // Listagem dos Serviços Por Estado 3 ou 5
  public String listarServicosEstado() {

    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();
      String listagemServicos = "";

      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getEstadoProcesso() == 3 || servico.getEstadoProcesso() == 5)
          listagemServicos += servico + "\n";
      }
      return listagemServicos;
    }
    return null;
  }

  // Listagem dos Serviços Por Estado Pendente
  public String listarServicosEstadoPendente() {
    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();
      String listagemServicos = "";

      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getEstadoProcesso() == 1)
          listagemServicos += servico + "\n";
      }
      return listagemServicos;
    }
    return null;
  }

  // Alterar estado serviço
  public boolean alterarEstadoServico(int aCodigoServico, int aEstado) {
    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();

      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getCodigoServico() == aCodigoServico) {
          servico.setEstadoProcesso(aEstado);
          return true;
        }
      }
    }
    return false;
  }

  // Atribuir farmacêutico a serviço
  public boolean atribuirFarmaceutico(int aCodigoServico, int aIdFarmaceutico) {
    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();

      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getCodigoServico() == aCodigoServico) {
          servico.setIdFarmaceutico(aIdFarmaceutico);
          return true;
        }
      }
    }
    return false;
  }

  // Listagem dos Serviços Por Estado Concluído (Estado 4) para gestor encerrar
  public String listarServicosEstadoConcluido() {
    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();
      String listagemServicos = "";

      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getEstadoProcesso() == 4)
          listagemServicos += servico + "\n";
      }
      return listagemServicos;
    }
    return null;
  }

  // Listagem dos Serviços de farmacêuticos por Estado aceite (Estado 2) para
  // farmacêutico iniciar processo
  public String listarServicosEstadoAceite(int aIdFarmaceutico) {
    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();
      String listagemServicos = "";

      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getIdFarmaceutico() == aIdFarmaceutico && servico.getEstadoProcesso() == 2)
          listagemServicos += servico + "\n";
      }
      return listagemServicos;
    }
    return null;
  }

  // pesquisar servico por código (para determinado utilizador))
  public String pesquisarServicosPorCodigoUtilizador(int aCodigo, int aID, int aTipo) {
    if (listaServicos != null && listaServicos.size() > 0) {
      Enumeration<Servicos> indices = listaServicos.elements();
      String listagemServicos = "";

      if (aTipo == 1) {
        while (indices.hasMoreElements()) {
          Servicos servico = indices.nextElement();
          if (servico.getCodigoServico() == aCodigo)
            listagemServicos += servico + "\n";
        }
        return listagemServicos;
      } else {
        while (indices.hasMoreElements()) {
          Servicos servico = indices.nextElement();
          if (servico.getIdUtilizador() == aID && servico.getCodigoServico() == aCodigo)
            listagemServicos += servico + "\n";
        }
        return listagemServicos;
      }
    }
    return null;
  }

  // pesquisar servico por código
  public Servicos pesquisarServicosPorCodigo(int aCodigo) {
    if (listaServicos != null && listaServicos.size() > 0) {
      Enumeration<Servicos> indices = listaServicos.elements();
      String listagemServicos = "";

      while (indices.hasMoreElements()) {
        Servicos servico = indices.nextElement();
        if (servico.getCodigoServico() == aCodigo)
          return servico;
      }
    }
    return null;
  }

  // pesquisar serviços tempo despendido superior a um determinado limite
  public String pesquisarServicosPorTempo(int aTempo) {
    if (listaServicos != null && listaServicos.size() > 0) {
      Enumeration<Servicos> indices = listaServicos.elements();
      String listagemServicos = "";
      while (indices.hasMoreElements()) {
        Servicos servico = indices.nextElement();
        if (servico.getTempoServico() > aTempo)
          listagemServicos += servico + "\n";
      }
      return listagemServicos;
    }
    return null;
  }

  // alterar tempo despendido do serviço
  public boolean alterarTempoServico(int aCodigoServico, String aData) {
    if (listaServicos != null && listaServicos.size() > 0) {
      Enumeration<Servicos> indice = listaServicos.elements();
      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getCodigoServico() == aCodigoServico) {
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          try {
            Date dataServico = dateFormat.parse(aData);
            Date dataAtual = new Date();
            long horasDiferenca = (dataAtual.getTime() - dataServico.getTime()) / (1000 * 60 * 60);
            int horasDiferencaInt = (int) horasDiferenca;
            servico.setTempoServico(horasDiferencaInt);
            return true;
          } catch (ParseException e) {
            e.printStackTrace(); // Ou outra forma de tratamento, como imprimir a mensagem de erro
            return false; // Indicar que ocorreu um erro
          }
        }
      }
    }
    return false;
  }

  // Alterar preço total do serviço
  public boolean alterarPrecoServico(int aCodigoServico, float aPrecoTotal) {
    if (listaServicos != null && listaServicos.size() > 0) {

      Enumeration<Servicos> indice = listaServicos.elements();

      while (indice.hasMoreElements()) {
        Servicos servico = indice.nextElement();
        if (servico.getCodigoServico() == aCodigoServico) {
          servico.setPrecoTotal(aPrecoTotal);
          return true;
        }
      }
    }
    return false;
  }
}