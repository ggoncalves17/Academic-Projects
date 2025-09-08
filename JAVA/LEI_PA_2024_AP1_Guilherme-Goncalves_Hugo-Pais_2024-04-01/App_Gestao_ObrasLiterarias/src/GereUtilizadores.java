/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe responsável pela gestão dos Utilizadores no sistema.
 * Esta classe oferece métodos para verificar, listar, pesquisar e adicionar informações relativas aos Utilizadores na base de dados.
 */
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;

class GereUtilizadores {

  
  // VERIFICAÇÕES -----------------------------------------------

  
  /**
   * Verifica se um login já existe na base de dados.
   *
   * @param st     Uma instância de Statement para executar queries na base de dados.
   * @param aLogin Login a ser verificado.
   * @return true se o login já existe, false caso contrário.
   */
  public boolean verificaLogin(Statement st, String aLogin) {
    try {
      ResultSet rs = st.executeQuery("SELECT idutilizador FROM utilizadores WHERE login ='" + aLogin + "'");

      while (rs.next()) {
        int cl1 = rs.getInt("idutilizador");

        if (cl1 != 0)
          return true; // True - Quer dizer que existe utilizador com o login
      }
      return false; // False - Quer dizer que não existe utilizador com o login
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Verifica se o formato do email é válido.
   *
   * @param aEmail O email a ser verificado.
   * @return true se o email estiver em um formato válido, false caso contrário.
   */
  public boolean verificaFormatoEmail(String aEmail) {
    if (Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$", aEmail))
      return true;
    return false;
  }

  /**
   * Verifica se o email existe.
   *
   * @param st     Uma instância de Statement para executar queries na base de dados.
   * @param aEmail Email a ser verificado.
   * @return true se o email já estiver registado na base de dados, false caso contrário.
   */
  public boolean verificaEmail(Statement st, String aEmail) {
    try {
      ResultSet rs = st.executeQuery("SELECT email FROM utilizadores WHERE email ='" + aEmail + "'");

      while (rs.next()) {
        String cl1 = rs.getString("email");

        if (cl1 != null)
          return true; // True - Quer dizer que existe utilizador com o email
      }
      return false; // False - Quer dizer que não existe utilizador com o email
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Verifica se o NIF existe.
   *
   * @param st     Uma instância de Statement para executar queries na base de dados.
   * @param aNIF   NIF a ser verificado.
   * @return true se o NIF já estiver registado na base de dados, false caso contrário.
   */
  public boolean verificaNIF(Statement st, int aNIF) {
    try {
      ResultSet rs = st.executeQuery(" SELECT idutilizador \n"
          + "FROM utilizadores u\n"
          + "LEFT JOIN autores a ON u.idutilizador = a.utilizadores_idutilizador\n"
          + "LEFT JOIN revisores r ON u.idutilizador = r.utilizadores_idutilizador\n"
          + "WHERE r.nif = '" + aNIF + "' OR a.nif ='" + aNIF + "'");
      while (rs.next()) {
        int cl1 = rs.getInt("idutilizador");

        if (cl1 != 0)
          return true; // true - Quer dizer que existe utilizador com o nif
      }
      return false; // False - Quer dizer que não existe utilizador com o nif
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Verifica se o formato do NIF é válido.
   *
   * @param aNIF   NIF a ser verificado.
   * @return true se o NIF estiver num formato válido, false caso contrário.
   */
  public boolean verificaFormatoNIF(int aNIF) {

    String nif = "" + aNIF;
    
    if (Pattern.matches("\\d{9}", nif))
      return true;
    return false;
  }

  /**
   * Verifica se o telefone possui 9 digitos e é iniciado por 9, 2 ou 3.
   *
   * @param aTelefone   Telefone a ser verificado.
   * @return true se o Telefone estiver num formato válido, false caso contrário.
   */
  public boolean verificaFormatoTelefone(int aTelefone) {

    String telefone = "" + aTelefone;

    if (Pattern.matches("[923]\\d{8}", telefone)) 
      return true;
    return false;
  }

  /**
   * Verifica as credenciais de login.
   *
   * @param st         Uma instância de Statement para executar queries na base de dados.
   * @param aLogin     Login a ser verificado.
   * @param aPassword  Password a ser verificada.
   * @return utilizador se o login foi efetuado corretamente, null caso contrário.
   */
  public Utilizadores verificaLoginUtilizador(Statement st, String aLogin, String aPassword) {
    try {
      ResultSet rs = st.executeQuery("SELECT * FROM utilizadores WHERE login ='" + aLogin + "' AND password ='" + aPassword + "'");

      while (rs.next()) {
        int cl1 = rs.getInt("idutilizador");

        if (cl1 != 0) {
          Utilizadores utilizador = new Utilizadores(rs.getString("login"), rs.getString("password"),
                  rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"));
          utilizador.setId(rs.getInt("idutilizador"));

          return utilizador;
        }
      }
      return null; //Quer dizer que não existe utilizador com o login e a password
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return null;
    }
  }

  // LISTAGENS/PESQUISAS -----------------------------------------------

  /**
   * Lista todos os utilizadores presentes na base de dados.
   *
   * @param st   Uma instância de Statement para executar queries na base de dados.
   * @return string com a listagem de utilizadores, ou null se ocorrer um erro.
   */
  public String listarUtilizadores(Statement st) {
    String listagemUtilizadores = "";
    try {
      ResultSet rs = st.executeQuery("SELECT * FROM utilizadores ORDER BY nome");

      while (rs.next()) {
        Utilizadores utilizadores = new Utilizadores(rs.getString("login"), rs.getString("password"),
            rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"));
        utilizadores.setId(rs.getInt("idutilizador"));
        listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return null;
    }
  }

  /**
   * Lista todos os utilizadores pendentes na base de dados (i.e., estado = 0).
   *
   * @param st   Uma instância de Statement para executar queries na base de dados.
   * @return string com a listagem de utilizadores pendentes, ou null se ocorrer um erro.
   */
  public String listarUtilizadoresPendentes(Statement st) {
    String listagemUtilizadores = "";
    try {
      ResultSet rs = st.executeQuery("SELECT * FROM utilizadores WHERE estado = 0");

      while (rs.next()) {
        Utilizadores utilizadores = new Utilizadores(rs.getString("login"), rs.getString("password"),
            rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"));
        utilizadores.setId(rs.getInt("idutilizador"));
        listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return null;
    }
  }

  /**
   * Aprova um utilizador pendente, alterando o seu estado para 1 na base de dados.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param aIdUtilizador ID do utilizador a ser aprovado.
   * @return true se a aprovação for bem-sucedida, false caso contrário.
   */
  public boolean aprovarUtilizadoresPendentes(Statement st, int aIdUtilizador) {
    try {
      st.executeUpdate("UPDATE utilizadores SET estado = 1 WHERE idutilizador = " + aIdUtilizador);

      return true;
      
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Pesquisa utilizadores na base de dados por nome ou login, de acordo com a escolha especificada.
   *
   * @param st         Uma instância de Statement para executar queries na base de dados.
   * @param aNomeLogin Nome ou login a ser pesquisado.
   * @param aEscolha   Escolha que indica se a pesquisa é por nome (1) ou por login (outro valor).
   * @return string com a listagem de utilizadores encontrados, ou null se ocorrer um erro.
   */
  public String pesquisaPorNomeLogin(Statement st, String aNomeLogin, int aEscolha) {
    String listagemUtilizadores = "";
    String variavel = null;

    if (aEscolha == 1)
      variavel = "nome";
    else {
      variavel = "login";
    }

    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM utilizadores WHERE " + variavel + " LIKE '" + aNomeLogin + "%'");

      while (rs.next()) {
        Utilizadores utilizadores = new Utilizadores(rs.getString("login"), rs.getString("password"),
            rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"));
        utilizadores.setId(rs.getInt("idutilizador"));
        listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Pesquisa utilizadores na base de dados por tipo.
   *
   * @param st    Uma instância de Statement para executar queries na base de dados.
   * @param aTipo Tipo de utilizadores a serem pesquisados.
   * @return string com a listagem de utilizadores encontrados do tipo especificado, ou null se ocorrer um erro.
   */
  public String pesquisaPorTipo(Statement st, int aTipo) {
    String listagemUtilizadores = "";

    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM utilizadores WHERE tipo=" + aTipo);

      while (rs.next()) {
        Utilizadores utilizadores = new Utilizadores(rs.getString("login"), rs.getString("password"),
            rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"));
        utilizadores.setId(rs.getInt("idutilizador"));
        listagemUtilizadores += utilizadores + "\n";
      }
      return listagemUtilizadores;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Pesquisa um utilizador na base de dados pelo ID.
   *
   * @param st   Uma instância de Statement para executar queries na base de dados.
   * @param aId  ID do utilizador a ser pesquisado.
   * @return (objeto) utilizador correspondente ao Id fornecido, ou null se o utilizador não for encontrado ou ocorrer um erro.
   */
  public Utilizadores pesquisaPorId(Statement st, int aId) {
    Utilizadores utilizadores = null;
    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM utilizadores WHERE idUtilizador=" + aId);
      while (rs.next()) {
        utilizadores = new Utilizadores(rs.getString("login"), rs.getString("password"),
            rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"));
        utilizadores.setId(rs.getInt("idutilizador"));
      }
      return utilizadores;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  
  // ADICIONAR INFO BD -------------------------------------------------

  
  /**
   * Obtém o Id do último utilizador inserido na base de dados.
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return ultimoId ou seja o id do último utilizador inserido na base de dados, ou -1 se ocorrer um erro.
   */
  public int ultimoIdUtilizador(Statement st) {
    try {
      ResultSet rs = st.executeQuery(" SELECT MAX(idutilizador) AS 'ultimoid' FROM utilizadores");

      int ultimoId = -1;
    
      while (rs.next()) {
        ultimoId = rs.getInt("ultimoid");
      }
      return ultimoId;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return -1;
    }
  }

  /**
   * Insere dados de um novo utilizador na base de dados.
   *
   * @param st        Uma instância de Statement para executar queries na base de dados.
   * @param aLogin    Login do novo utilizador.
   * @param aPassword Senha do novo utilizador.
   * @param aNome     Nome do novo utilizador.
   * @param aEstado   Estado do novo utilizador.
   * @param aEmail    Email do novo utilizador.
   * @param aTipo     Tipo do novo utilizador.
   * @return true se a inserção for bem-sucedida, false caso contrário.
   */
  public boolean adicionarInfoUtilizadorBD(Statement st, String aLogin, String aPassword, String aNome, boolean aEstado, String aEmail, int aTipo) {
    try {
      st.execute("INSERT INTO utilizadores (login, password, nome, estado, email, tipo) " +
         " VALUES ('" + aLogin + "', '" + aPassword + "', '" + aNome + "', " + aEstado + ", '" + aEmail + "', " + aTipo + ")");

      return true;
      
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Insere dados de um novo revisor na base de dados.
   *
   * @param st                    Uma instância de Statement para executar queries na base de dados.
   * @param aNIF                  NIF do novo revisor.
   * @param aMorada               Morada do novo revisor.
   * @param aTelefone             Telefone do novo revisor.
   * @param aAreaEspecializacao   Área de especialização do novo revisor.
   * @param aFormacaoAcademica    Formação académica do novo revisor.
   * @param aUltimoId             Último Id do utilizador.
   * @return true se a inserção for bem-sucedida, false caso contrário.
   */
  public boolean adicionarInfoRevisorBD(Statement st, int aNIF, String aMorada, int aTelefone, String aAreaEspecializacao, String aFormacaoAcademica, int aUltimoId) {
    try {
      st.execute("INSERT INTO revisores (nif, morada, telefone, areaEspecializacao, formacaoAcademica, utilizadores_idutilizador) " +
          " VALUES (" + aNIF + ", '" + aMorada + "', " + aTelefone + ", '" + aAreaEspecializacao + "', '" + aFormacaoAcademica + "', " + aUltimoId + ")");

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Insere informações de um novo autor na base de dados.
   *
   * @param st                    Uma instância de Statement para executar queries na base de dados.
   * @param aNIF                  NIF do novo autor.
   * @param aEstiloLiterario      Estilo literário do novo autor.
   * @param aInicioAtividade      Data de início de atividade do novo autor.
   * @param aTelefone             Telefone do novo autor.
   * @param aMorada               Morada do novo autor.
   * @param aUltimoId             Último Id do utilizador.
   * @return true se a inserção for bem-sucedida, false caso contrário.
   */
  public boolean adicionarInfoAutorBD(Statement st, int aNIF, String aEstiloLiterario, String aInicioAtividade , int aTelefone, String aMorada, int aUltimoId) {
    try {

      st.execute("INSERT INTO autores (nif, estiloliterario, inicioatividade, telefone, morada, utilizadores_idutilizador) " +
          " VALUES (" + aNIF + ", '" + aEstiloLiterario + "', '" + aInicioAtividade + "', " + aTelefone + ", '" + aMorada + "', " + aUltimoId + ")");

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Ativa ou desativa um utilizador na base de dados.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param aIdUtilizador Id do utilizador a ser ativado ou desativado.
   * @param aEstado       Estado a ser atribuído ao utilizador (0 para desativado, 1 para ativado).
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean ativarUtilizador(Statement st, int aIdUtilizador, int aEstado) {
    try {
      st.executeUpdate("UPDATE utilizadores SET estado = " + aEstado + " WHERE idutilizador = " + aIdUtilizador);

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados" );
      return false;
    }
  }

  /**
   * Lista os dados segmentados por páginas.
   *
   * @param st              Uma instância de Statement para executar queries na base de dados.
   * @param aDadosSegmentar String com listagem de informações.
   * @param aPagina         Página a ser consultada.
   * @return string com os dados listados por páginas se a operação for bem-sucedida.
   */
  public String listarPorPagina(Statement st, String aDadosSegmentar, int aPagina) {
      int registosPorPagina = 10;
      int linhasADescartar = (aPagina - 1) * registosPorPagina; // Isto é se é a página 1 não se descarta nenhum registo, se for página 2 descartamos 10 (referentes à primeira página).
      String[] dadosSegmentados = aDadosSegmentar.split("\n"); // Divisão da listagem obtida noutros métodos em linhas

      String resultadoPaginado = "";

      int inicio = linhasADescartar;
      int fim;
      if (linhasADescartar + registosPorPagina < dadosSegmentados.length) {
          fim = linhasADescartar + registosPorPagina;
      } else {
          fim = dadosSegmentados.length;
      }
      for (int i = inicio; i < fim; i++) {
          resultadoPaginado += dadosSegmentados[i] + "\n"; // Adicionamos cada linha da página ao resultado paginado
      }
      return resultadoPaginado;
  }
  
  //Atualiza nome na base de dados
  public boolean atualizaNome(Statement st, int aIdUtilizador, String aNovoNome) {
	    try {
	      st.executeUpdate("UPDATE utilizadores SET nome = '" + aNovoNome+"' WHERE idutilizador = " + aIdUtilizador);

	      return true;
	      
	    } catch (SQLException e) {
	      System.out.println("Erro ao ligar à base de dados");
	      return false;
	    }
  }
  
  //Atualiza password na base de dados
  public boolean atualizaPassword(Statement st, int aIdUtilizador, String aNovaPassword) {
	    try {
	      st.executeUpdate("UPDATE utilizadores SET password = '" + aNovaPassword+"' WHERE idutilizador = " + aIdUtilizador);

	      return true;
	      
	    } catch (SQLException e) {
	      System.out.println("Erro ao ligar à base de dados");
	      return false;
	    }
  }
  
  //Atualiza email na base de dados
  public boolean atualizaEmail(Statement st, int aIdUtilizador, String aNovoEmail) {
	    try {
	      st.executeUpdate("UPDATE utilizadores SET email = '" + aNovoEmail+"' WHERE idutilizador = " + aIdUtilizador);

	      return true;
	      
	    } catch (SQLException e) {
	      System.out.println("Erro ao ligar à base de dados");
	      return false;
	    }
  }
  
  //Atualiza telefone na base de dados dos autores
  public boolean atualizaTelefoneAutores(Statement st, int aIdUtilizador, int aNovoTelefone) {
	    try {
	      st.executeUpdate("UPDATE autores SET telefone = '" + aNovoTelefone+"' WHERE utilizadores_idutilizador = " + aIdUtilizador);

	      return true;
	      
	    } catch (SQLException e) {
	      System.out.println("Erro ao ligar à base de dados");
	      return false;
	    }
  }
  
  //Atualiza telefone na base de dados dos revisores
  public boolean atualizaTelefoneRevisores(Statement st, int aIdUtilizador, int aNovoTelefone) {
	    try {
	      st.executeUpdate("UPDATE revisores SET telefone = '" + aNovoTelefone+"' WHERE utilizadores_idutilizador = " + aIdUtilizador);

	      return true;
	      
	    } catch (SQLException e) {
	      System.out.println("Erro ao ligar à base de dados");
	      return false;
	    }
  }
  
  //Atualiza morada na base de dados dos autores
  public boolean atualizaMoradaAutores(Statement st, int aIdUtilizador, String aNovaMorada) {
	    try {
	      st.executeUpdate("UPDATE autores SET morada = '" + aNovaMorada+"' WHERE utilizadores_idutilizador = " + aIdUtilizador);

	      return true;
	      
	    } catch (SQLException e) {
	      System.out.println("Erro ao ligar à base de dados");
	      return false;
	    }
  }
  
  //Atualiza morada na base de dados dos revisores
  public boolean atualizaMoradaRevisores(Statement st, int aIdUtilizador, String aNovaMorada) {
	    try {
	      st.executeUpdate("UPDATE revisores SET morada = '" + aNovaMorada+"' WHERE utilizadores_idutilizador = " + aIdUtilizador);

	      return true;
	      
	    } catch (SQLException e) {
	      System.out.println("Erro ao ligar à base de dados");
	      return false;
	    }
  }

  

}
