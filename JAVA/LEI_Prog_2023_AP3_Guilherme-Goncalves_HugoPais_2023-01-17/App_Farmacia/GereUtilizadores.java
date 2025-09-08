import java.util.Vector;
import java.util.Enumeration;
import java.util.Collections;
import java.io.Serializable;

class GereUtilizadores implements Serializable {
  private Vector<Utilizadores> lista;

  GereUtilizadores() {
    lista = new Vector<Utilizadores>();
  }

  //Retornar id ultimo utilizador
  public int ultimoIdUtilizador (int aId) {
    if (lista != null && lista.size() > 0) {
      Enumeration<Utilizadores> indices = lista.elements();

      while (indices.hasMoreElements()) {
        Utilizadores utilizador = indices.nextElement();
        aId = utilizador.getId();
      }
      return aId;
    }
    return 0;
  }

  // inserir
  public boolean adicionarUtilizadores(Utilizadores aUtilizadores) {
    if (lista != null) {
      return lista.add(aUtilizadores);
    }
    return false;
  }

  // ordenar
  public boolean ordenarUtilizadores(String aNome) {
    if (lista != null && lista.size() > 1) {
      Collections.sort(lista);
      return true;
    }
    return false;
  }

  // pesquisar por login
  public Utilizadores verificaUtilizador(String aLogin, String aPassword) {
    if (lista != null && lista.size() > 0) {
      Enumeration<Utilizadores> indices = lista.elements();

      while (indices.hasMoreElements()) {
        Utilizadores utilizador = indices.nextElement();
        if ((utilizador.getLogin().equalsIgnoreCase(aLogin)) && (utilizador.getPassword().equalsIgnoreCase(aPassword)))
          return utilizador;
      }
    }
    return null;
  }

  // pesquisar por nome
  public String pesquisaPorNome(String aNome) {
    if (lista != null && lista.size() > 0) {
      Enumeration <Utilizadores> indices = lista.elements();
      String listagemUtilizadores = "";
      while (indices.hasMoreElements()) {
        Utilizadores utilizadores = indices.nextElement();
        if (utilizadores.getNome().equalsIgnoreCase(aNome))
          listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    }
    return null;
  }

  // pesquisa avançada
  public boolean contemTermo(Utilizadores utilizador, String termoPesquisa) {
    return utilizador.getNome().toLowerCase().contains(termoPesquisa.toLowerCase()) ||
        utilizador.getLogin().toLowerCase().contains(termoPesquisa.toLowerCase()) ||
        utilizador.getEmail().toLowerCase().contains(termoPesquisa.toLowerCase());
  }

  // listar utilizadores por tipo (gestores, farmacêutico ou clientes)
  public String listarUtilizadoresPorTipo(int aTipo) {
    if (lista != null && lista.size() > 0) {

      Enumeration<Utilizadores> indices = lista.elements();
      String listagemUtilizadores = "";
      while (indices.hasMoreElements()) {
        Utilizadores utilizadores = indices.nextElement();
        if (utilizadores.getTipo() == aTipo)
          listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    }
    return null;
  }

  // listar todos os utilizadores
  public String listarUtilizadores() {
    if (lista != null && lista.size() > 0) {

      Enumeration<Utilizadores> indices = lista.elements();
      String listagemUtilizadores = "";
      while (indices.hasMoreElements()) {
        Utilizadores utilizadores = indices.nextElement();
        listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    }
    return null;
  }

  // Listagem de todos os utilizadores pendentes
  public String listarUtilizadoresPendentes() {
    if (lista != null && lista.size() > 0) {

      Enumeration<Utilizadores> indices = lista.elements();
      String listagemUtilizadores = "";
      while (indices.hasMoreElements()) {
        Utilizadores utilizadores = indices.nextElement();
        if (utilizadores.getEstado() == false)
          listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    }
    return null;
  }

  // Aprovar utilizador pendente
  public boolean aprovarUtilizador(int aIdUtilizador) {
    if (lista != null && lista.size() > 0) {

      Enumeration<Utilizadores> indices = lista.elements();

      while (indices.hasMoreElements()) {
        Utilizadores utilizadores = indices.nextElement();
        if (utilizadores.getId() == aIdUtilizador) {
          utilizadores.setEstado(true);
          return true;
        }
      }
    }
    return false;
  }

  // Verificar se login existe
  public boolean verificaLogin(String aLogin) {
    if (lista != null && lista.size() > 0) {

      Enumeration<Utilizadores> indices = lista.elements();
      while (indices.hasMoreElements()) {
        Utilizadores utilizadores = indices.nextElement();
        if (utilizadores.getLogin().equalsIgnoreCase(aLogin))
          return true;
      }
    }
    return false;
  }

  // Verificar se email existe
  public boolean verificaEmail(String aEmail) {
    if (lista != null && lista.size() > 0) {

      Enumeration<Utilizadores> indices = lista.elements();
      while (indices.hasMoreElements()) {
        Utilizadores utilizadores = indices.nextElement();
        if (utilizadores.getEmail().equalsIgnoreCase(aEmail))
          return true;
      }
    }
    return false;
  }

  // Verifica se NIF existe
  public boolean verificaNIF(int aNIF) {
    if (lista != null && !lista.isEmpty()) {
      Enumeration<Utilizadores> indices = lista.elements();

      while (indices.hasMoreElements()) {
        Utilizadores utilizador = indices.nextElement();
        
        if (utilizador.getClass() == Clientes.class) {
          Clientes cliente = (Clientes) utilizador;
          if (cliente.getNif() == aNIF) {
            return true;
          }
        }
      }
    }
    return false;
  }

  // Verifica se telefone existe
  public boolean verificaTelefone(int aTelefone) {
    if (lista != null && !lista.isEmpty()) {
      Enumeration<Utilizadores> indices = lista.elements();

      while (indices.hasMoreElements()) {
        Utilizadores utilizador = indices.nextElement();
        
        if (utilizador.getClass() == Clientes.class) {
          Clientes cliente = (Clientes) utilizador;
          if (cliente.getTelefone() == aTelefone) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
