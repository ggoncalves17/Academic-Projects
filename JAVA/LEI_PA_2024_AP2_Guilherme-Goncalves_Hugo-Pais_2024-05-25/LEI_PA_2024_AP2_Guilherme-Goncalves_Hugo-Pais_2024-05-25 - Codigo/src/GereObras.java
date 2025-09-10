/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe responsável pela gestão de ações relativas às obras literárias.
 * Esta classe oferece métodos para verificar, listar, pesquisar e adicionar informações importantes à gestão das obras literárias na base de dados.
 */
import java.io.ObjectInputStream.GetField;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

class GereObras {

  /**
   * Cria e verifica a existência de um ISBN na base de dados.
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return código ISBN gerado e verificado, ou -1 em caso de erro.
   */
  public int criaVerificaISBN(Statement st) {
    try { 

      int codigo = -1;
      int codigoGerado;

      do {
        Random rand = new Random();

        codigoGerado = rand.nextInt(1000000) + 1;

        ResultSet rs = st.executeQuery("SELECT codisbn FROM obrasliterarias WHERE codisbn = " + codigoGerado);
        
        while (rs.next()){
          codigo = rs.getInt("codisbn");
        }
      } while (codigo > 0);

      return codigoGerado;
      
    }catch (SQLException e){
       System.out.println("Erro ao ligar a base de dados");
       return -1;
    }
  }

  /**
   * Verifica se um determinado título de obra já existe na base de dados.
   *
   * @param st      Uma instância de Statement para executar queries na base de dados.
   * @param aTitulo Título da obra a ser verificado.
   * @return true se o título já existe na base de dados, false caso contrário.
   */
  public boolean verificaTitulo(Statement st, String aTitulo) {
    try { 
      ResultSet rs = st.executeQuery("SELECT titulo FROM obrasliterarias WHERE titulo = '" + aTitulo + "'");
      while (rs.next()){
        String titulo = rs.getString("titulo");

        if (titulo != null) {
          return true; //Quer dizer que existe então temos de voltar a perguntar para inserir novo titulo
        }
      }
      return false;
    }catch (SQLException e){
       System.out.println("Erro ao ligar a base de dados");
       return false;
    }
  }

  /**
   * Lista as obras de um determinado autor.
   *
   * @param st       Uma instância de Statement para executar queries na base de dados.
   * @param idAutor  Id do autor cujas obras serão listadas.
   * @param aEscolha Critério de ordenação das obras (1 para data de submissão, 2 para título).
   * @return string com a listagem das obras do autor, ou null em caso de erro.
   */
  public String listarObrasDeAutor(Statement st, int idAutor, int aEscolha) {
  String listagemObras = "";

  String ordenacao = null;

  if (aEscolha == 1)
    ordenacao = "datasubmissao";
  else {
    if (aEscolha == 2)
      ordenacao = "titulo";
  }

  try {
    ResultSet rs = st.executeQuery(" SELECT *\r\n"
      + "FROM obrasliterarias, utilizadores_obrasliterarias uo, utilizadores, autores a\r\n"
      + "WHERE uo.obrasliterarias_idobra = idobra\r\n"
      + "AND uo.utilizadores_idutilizador = idutilizador\r\n"
      + "AND idutilizador = a.utilizadores_idutilizador\r\n"
      + "AND idutilizador = " + idAutor + "\r\n"
      + " ORDER BY " + ordenacao);

      while (rs.next()) {
        
      Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

      Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
            rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
            rs.getString("datasubmissao"),rs.getString("dataaprovacao"));

      obras.setId(rs.getInt("idobra"));

      listagemObras += obras + " | ";
      }
      return listagemObras;
    }catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados" + e.getMessage());
      return null;
    }
  }

  /**
   * Pesquisa obras de um determinado autor por data de submissão.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param idAutor       Id do autor cujas obras serão pesquisadas.
   * @param dadosPesquisa Data de submissão a ser pesquisada.
   * @return string com a listagem das obras encontradas, ou null em caso de erro.
   */
  public String pesquisarObrasDeAutorDataSubmissao(Statement st, int idAutor, String dadosPesquisa) {
  String listagemObras = "";

  try {
    ResultSet rs = st.executeQuery(" SELECT *\r\n"
      + " FROM obrasliterarias, utilizadores_obrasliterarias uo, utilizadores, autores a\r\n"
      + " WHERE uo.obrasliterarias_idobra = idobra\r\n"
      + " AND uo.utilizadores_idutilizador = idutilizador\r\n"
      + " AND idutilizador = a.utilizadores_idutilizador\r\n"
      + " AND idutilizador = " + idAutor + "\r\n"
      + " AND datasubmissao = '" + dadosPesquisa + "'");

      while (rs.next()) {
        
        Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));
        
      Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
            rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
            rs.getString("datasubmissao"),rs.getString("dataaprovacao"));

      obras.setId(rs.getInt("idobra"));

      listagemObras += obras + "\n";
      }
      return listagemObras;
    }catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Pesquisa obras de um determinado autor por título.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param idAutor       Id do autor cujas obras serão pesquisadas.
   * @param aTitulo       Título a ser pesquisado.
   * @return string com a listagem das obras encontradas, ou null em caso de erro.
   */
  public String pesquisarObrasDeAutorTitulo(Statement st, int idAutor, String aTitulo){
    String listagemObras = "";
    try {
      ResultSet rs = st.executeQuery(" SELECT *\r\n"
        + " FROM obrasliterarias, utilizadores_obrasliterarias uo, utilizadores, autores a\r\n"
        + " WHERE uo.obrasliterarias_idobra = idobra\r\n"
        + " AND uo.utilizadores_idutilizador = idutilizador\r\n"
        + " AND idutilizador = a.utilizadores_idutilizador\r\n"
        + " AND idutilizador = " + idAutor + "\r\n"
        + " AND titulo = '" + aTitulo + "'");
      while (rs.next()) {
        Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));
        Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
            rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
            rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
        obras.setId(rs.getInt("idobra"));
        listagemObras += obras + " | ";
      }
      return listagemObras;
    } catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }
  

  /**
   * Adiciona uma nova obra à base de dados.
   *
   * @param st                      Uma instância de Statement para executar queries na base de dados.
   * @param titulo                  Título da obra.
   * @param subTitulo               Subtítulo da obra.
   * @param estiloLiterario         Estilo literário da obra.
   * @param tipopublicacao          Tipo de publicação da obra.
   * @param nPaginas                Número de páginas da obra.
   * @param codISBN                 Código ISBN da obra.
   * @param nEdicao                 Número da edição da obra.
   * @param dataSubmissaoConvertida Data de submissão da obra convertida para o formato adequado.
   * @param dataAprovacao           Data de aprovação da obra.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean adicionarObra(Statement st, String titulo, String subTitulo, String estiloLiterario, int tipopublicacao, int nPaginas, int codISBN, int nEdicao, Date dataSubmissaoConvertida, LocalDate dataAprovacao) {
    try {
      st.execute("INSERT INTO obrasliterarias (titulo, subtitulo, estiloliterario, tipopublicacao, npaginas, codisbn, nedicao, datasubmissao, dataaprovacao) " +
         " VALUES ('" + titulo + "', '" + subTitulo + "', '" + estiloLiterario + "', " + tipopublicacao + ", " + nPaginas + ", " + codISBN + ", " + nEdicao + ", '" + dataSubmissaoConvertida + "', " + dataAprovacao + ")");

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return false;
    }
  }

  /**
   * Obtém o Id da última obra inserida e presente na base de dados.
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return ultimoId, isto é, o Id da última obra inserida na base de dados, ou -1 em caso de erro.
   */
  public int ultimoIdObra (Statement st) {
    try {
      ResultSet rs = st.executeQuery(" SELECT MAX(idobra) AS 'ultimoid' FROM obrasliterarias");

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
   * Atribui um autor a uma obra na base de dados.
   *
   * @param st       Uma instância de Statement para executar queries na base de dados.
   * @param aIdAutor Id do autor a ser atribuído à obra.
   * @param aIdObra  Id da obra.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean atribuirAutorObra(Statement st, int aIdAutor, int aIdObra) {
    try {
        st.executeUpdate(" INSERT INTO utilizadores_obrasliterarias (utilizadores_idutilizador, obrasliterarias_idobra) VALUES (" + aIdAutor + "," + aIdObra + ")");

        return true;

      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return false;
      }
  }
}
