/** Programa para gerir obras e respetivas revisões literária
  * @author Guilherme Gonçalves
  * @author Hugo Pais
  * @version 1.0
*/

/**
 * Classe responsável pela gestão das revisões no sistema.
 * Esta classe oferece métodos para listar, pesquisar e alterar informações relativas às Revisões na base de dados.
 */
import java.io.ObjectInputStream.GetField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class GereRevisoes {
  
    //GESTORES --------------------------------------------------------

  /**
   * Lista todos os pedidos de revisão presentes na base de dados, permitindo ordenar os resultados por data de realização, título ou nome.
   *
   * @param st        Uma instância de Statement para executar queries na base de dados.
   * @param aEscolha  A escolha do critério de ordenação (1 para data de realização, 2 para título, 3 para nome).
   * @return string com a listagem dos pedidos de revisão, ou null em caso de erro.
   */
  public String listarPedidosRevisao(Statement st, int aEscolha) {
      String listagemRevisoes = "";
      String ordenacao = null;

      if (aEscolha == 1)
        ordenacao = "datarealizacao";
      else {	
        if (aEscolha == 2)
          ordenacao = "titulo";
        else {
          ordenacao = "nome";
        }
      }
    
    try {
        ResultSet rs = st.executeQuery(" SELECT * \r\n"
          + "FROM revisao\r\n"
          + "JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + "JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + "JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + "LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + "LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador"
          + " WHERE tipo = 3 "                             
          + " ORDER BY " + ordenacao);

        while (rs.next()) {
          Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

          Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
  
          obras.setId(rs.getInt("idobra"));

          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return null;
      }
    }

  /**
   * Lista todos os pedidos de revisão não finalizados presentes na base de dados.
   *
   * @param st   Uma instância de Statement para executar queries na base de dados.
   * @return string com a listagem dos pedidos de revisão não finalizados, ou null em caso de erro.
   */
  public String listarPedidosRevisaoNaoFinalizados(Statement st) {
    String listagemRevisoes = "";

    //Estados revisão 1 - 3 não finalizados, 4 finalizado e 5 arquivado
    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM revisao, obrasliterarias WHERE obrasliterarias_idobra = idobra AND estado BETWEEN 1 AND 3" +
                                     " ORDER BY datarealizacao");
      
      while (rs.next()){

          Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

          Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
  
          obras.setId(rs.getInt("idobra"));
        
        Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
          rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

        revisoes.setId(rs.getInt("idrevisao"));
        listagemRevisoes += revisoes + "\n";
      }
      return listagemRevisoes;
    }catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Pesquisa pedidos de revisão por Id de revisão ou estado.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param tipoPesquisa  Tipo de pesquisa a ser realizado (1 para pesquisa por id de revisão, 2 para pesquisa por estado).
   * @param dadosPesquisa Dados a serem pesquisados (ID de revisão ou estado).
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String PesquisarPedidosRevisaoIdEstado(Statement st, int tipoPesquisa, int dadosPesquisa) {
    String listagemRevisoes = "";
      String pesquisa = null;

      if (tipoPesquisa == 1)
        pesquisa = "idrevisao";
      else {
        pesquisa = "estadoRevisao";
      }

    try {
        ResultSet rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE " + pesquisa + "='" + dadosPesquisa + "' AND tipo=3");

        while (rs.next()) {
            Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

            Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                  rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                  rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
    
            obras.setId(rs.getInt("idobra"));

          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return null;
      }
  }

  /**
   * Pesquisa pedidos de revisão por autor.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param dadosPesquisa Nome do autor a ser pesquisado.
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String PesquisarPedidosRevisaoAutor(Statement st, String dadosPesquisa) {
    String listagemRevisoes = "";
  
    try {
        ResultSet rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE nome LIKE '" + dadosPesquisa + "%' AND tipo = 3");
  
        while (rs.next()) {
  
            Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

            Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                  rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                  rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
    
            obras.setId(rs.getInt("idobra"));
  
          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);
  
          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
      } catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return null;
      }
  }

  /**
   * Pesquisa pedidos de revisão por data de realização, dentro de um intervalo específico.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param dataInicioStr Data de início do intervalo de pesquisa (formato: "YYYY-MM-DD").
   * @param dataFimStr    Data de fim do intervalo de pesquisa (formato: "YYYY-MM-DD").
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String PesquisarPedidosRevisaoEntreData(Statement st, String dataInicioStr, String dataFimStr) {
    String listagemRevisoes = "";

    try {
        LocalDate dataInicio = LocalDate.parse(dataInicioStr);
        LocalDate dataFim = LocalDate.parse(dataFimStr);

        ResultSet rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE datarealizacao BETWEEN '" + dataInicio + "' AND '" + dataFim + "' AND tipo = 3");

        while (rs.next()) {
            Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

            Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                  rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                  rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
    
            obras.setId(rs.getInt("idobra"));

          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return null;
      }
  }

  /**
   * Lista todos os pedidos de revisão associados ao titulo de uma determinada obra.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param dadosPesquisa Título da obra associado a uma revisao e a ser pesquisado.
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String listarPedidosRevisaoTitulo(Statement st, String dadosPesquisa) {
    String listagemRevisoes = "";

    try {
        ResultSet rs = st.executeQuery(" SELECT * \r\n"
          + "FROM revisao\r\n"
          + "JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + "JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + "JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + "LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + "LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador"
          + " WHERE titulo LIKE '" + dadosPesquisa + "%' AND tipo = 3 ");

        while (rs.next()) {
            Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

            Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                  rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                  rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
    
            obras.setId(rs.getInt("idobra"));

          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return null;
      }
  }

  // LISTAR PEDIDOS REVISAO ESTADO 2,3 E 8
  public String listarPedidosRevisaoEstados(Statement st) {
    String listagemRevisoes = "";

    try {
        ResultSet rs = st.executeQuery(" SELECT * \r\n"
          + "FROM revisao\r\n"
          + "JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + "JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + "JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + "LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + "LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador"
          + " WHERE estadorevisao IN (2,3,8) AND tipo = 3 ");

        while (rs.next()) {
            Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

            Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                  rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                  rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
    
            obras.setId(rs.getInt("idobra"));

          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return null;
      }
  }

  /**
   * Altera o estado de um pedido de revisão na base de dados.
   *
   * @param st          Uma instância de Statement para executar queries na base de dados.
   * @param aIdRevisao  Id do pedido de revisão a ter o estado alterado.
   * @param aEstado     Novo estado a ser atribuído ao pedido de revisão.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean alteraEstadoRevisao(Statement st, int aIdRevisao, int aEstado) {
    try {
        st.executeUpdate(" UPDATE revisao SET estadorevisao = " + aEstado + " WHERE idrevisao = " + aIdRevisao);

        return true;
      
      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados ");
        return false;
      }
  }

  /**
   * Altera os dados e o estado de um pedido de revisão para finalizado (estado = 4).
   *
   * @param st             Uma instância de Statement para executar queries na base de dados.
   * @param aDataFinal     Data de conclusão do pedido de revisão.
   * @param aObservacoes   Observações sobre o pedido de revisão.
   * @param aCustoProcesso Custo associado ao processo de revisão.
   * @param aIdRevisao     Id do pedido de revisão a ser concluído.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean concluirPedidoRevisao(Statement st, Date aDataFinal, String aObservacoes, float aCustoProcesso, int aIdRevisao) {
    try {
        st.executeUpdate(" UPDATE revisao SET datafinal = '" + aDataFinal + "', observacoes = '" + aObservacoes + "', custoprocesso ="+ aCustoProcesso + ", estadorevisao = 4 WHERE idrevisao = " + aIdRevisao);

        return true;

      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return false;
      }
  }

  
  //AUTORES --------------------------------------------------------

  
  /**
   * Lista todos os pedidos de revisão de um determinado autor presentes na base de dados.
   *
   * @param st        Uma instância de Statement para executar queries na base de dados.
   * @param idAutor   Id do autor cujos pedidos de revisão serão listados.
   * @param aEscolha  Escolha do critério de ordenação (1 para data de realização, 2 para número de série).
   * @return string com a listagem dos pedidos de revisão do autor, ou null em caso de erro.
   */
  public String listarPedidosRevisaoDeAutor(Statement st, int idAutor, int aEscolha) {
  String listagemRevisoes = "";

  String ordenacao = null;

  if (aEscolha == 1)
    ordenacao = "datarealizacao";
  else {
    if (aEscolha == 2)
      ordenacao = "nserie";
  }

  try {
      ResultSet rs = st.executeQuery(" SELECT * \r\n"
        + " FROM revisao\r\n"
        + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
        + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
        + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
        + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
        + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
        + " WHERE idutilizador ='" + idAutor + "' AND tipo=3"
        + " ORDER BY " + ordenacao);

      while (rs.next()) {
          Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

          Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
  
          obras.setId(rs.getInt("idobra"));

        Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
            rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"), obras);

        revisoes.setId(rs.getInt("idrevisao"));
        listagemRevisoes += revisoes + " | ";
      }
      return listagemRevisoes;
    }catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados" + e.getMessage());
      return null;
    }
  }

  /**
   * Pesquisa pedidos de revisão de um determinado autor por data de realização ou título da obra.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param idAutor       Id do autor cujos pedidos de revisão serão pesquisados.
   * @param tipoPesquisa  Tipo de pesquisa a ser realizado (1 para pesquisa por data de realização, 2 para pesquisa por título).
   * @param dadosPesquisa Dados a serem pesquisados (data de realização ou título da obra).
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String pesquisarPedidosRevisaoDeAutorDataTitulo(Statement st, int idAutor, int tipoPesquisa, String dadosPesquisa) {
  String listagemRevisoes = "";

  String pesquisa = null;
  ResultSet rs = null;

  try {

      //Pesquisa pela data da criacao
      if (tipoPesquisa == 1){
        pesquisa = "datarealizacao";

        LocalDate dadosPesquisa2 = LocalDate.parse(dadosPesquisa);

        rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE '" + pesquisa + "'='" + dadosPesquisa2 + "' AND idutilizador ='"+ idAutor +"' AND tipo=3");
      }
      //Pesquisa pelo titulo da obra
      else if (tipoPesquisa == 2){
        pesquisa = "titulo";
        
        rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE " + pesquisa + "='" + dadosPesquisa + "' AND idutilizador ='"+ idAutor +"' AND tipo=3");
      }
      //Pesquisa pelo nserie da revisao
      else {
        pesquisa = "nserie";

        rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE " + pesquisa + "='" + dadosPesquisa + "' AND idutilizador ='"+ idAutor +"' AND tipo=3");

      }

      while (rs.next()) {
          Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

          Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
  
          obras.setId(rs.getInt("idobra"));

        Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
            rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

        revisoes.setId(rs.getInt("idrevisao"));
        listagemRevisoes += revisoes + " | ";
      }
      return listagemRevisoes;
    }catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Pesquisa pedidos de revisão de um determinado autor por estado.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param idAutor       Id do autor cujos pedidos de revisão serão pesquisados.
   * @param dadosPesquisa Estado dos pedidos de revisão a ser considerado.
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String pesquisarPedidosRevisaoDeAutorEstado(Statement st, int idAutor, int dadosPesquisa) {
    String listagemRevisoes = "";
  
    try {
      ResultSet rs = st.executeQuery(" SELECT * \r\n"
            + " FROM revisao\r\n"
            + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
            + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
            + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
            + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
            + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
            + " WHERE estadoRevisao ='" + dadosPesquisa + "' AND idutilizador ='"+ idAutor +"' AND tipo=3");
  
      while (rs.next()) {
          Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

          Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
  
          obras.setId(rs.getInt("idobra"));
  
        Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);
  
        revisoes.setId(rs.getInt("idrevisao"));
        listagemRevisoes += revisoes + "\n";
      }
      return listagemRevisoes;
    } catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  
  //REVISORES ------------------------------------------------------

  
  /**
   * Lista todos os pedidos de revisão de um determinado revisor presentes na base de dados.
   *
   * @param st        Uma instância de Statement para executar queries na base de dados.
   * @param idRevisor Id do revisor cujos pedidos de revisão serão listados.
   * @param aEscolha  Escolha do critério de ordenação (1 para data de realização, 2 para título da obra).
   * @return string com a listagem dos pedidos de revisão do revisor, ou null em caso de erro.
   */
  public String listarPedidosRevisaoDeRevisor(Statement st, int idRevisor, int aEscolha) {
    String listagemRevisoes = "";
  
    String ordenacao = null;
  
    if (aEscolha == 1)
      ordenacao = "datarealizacao";
    else {	
      ordenacao = "titulo";
    }
  
    try {
        ResultSet rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE idutilizador ='" + idRevisor + "' AND tipo=2"
          + " ORDER BY " + ordenacao);
  
        while (rs.next()) {
  
            Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

            Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                  rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                  rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
    
            obras.setId(rs.getInt("idobra"));
  
          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);
  
          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados");
        return null;
      }
    }

  /**
   * Pesquisa pedidos de revisão de um determinado revisor por data de realização ou título da obra.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param idRevisor     Id do revisor cujos pedidos de revisão serão pesquisados.
   * @param tipoPesquisa  Tipo de pesquisa a ser realizado (1 para pesquisa por data de realização, 2 para pesquisa por título).
   * @param dadosPesquisa Dados a serem pesquisados (data de realização ou título da obra).
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String pesquisarPedidosRevisaoDeRevisorDataTitulo(Statement st, int idRevisor, int tipoPesquisa, String dadosPesquisa) {
  String listagemRevisoes = "";

  String pesquisa = null;
  ResultSet rs = null;

  try {

      //Pesquisa pela data da criacao
      if (tipoPesquisa == 1){
        pesquisa = "datarealizacao";

        LocalDate dadosPesquisa2 = LocalDate.parse(dadosPesquisa);

        rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE '" + pesquisa + "'='" + dadosPesquisa2 + "' AND idutilizador ='"+ idRevisor +"' AND tipo=2");
      }
      //Pesquisa pelo titulo da obra
      else {
        pesquisa = "titulo";

        rs = st.executeQuery(" SELECT * \r\n"
          + " FROM revisao\r\n"
          + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
          + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
          + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
          + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
          + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
          + " WHERE " + pesquisa + "='" + dadosPesquisa + "' AND idutilizador ='"+ idRevisor +"' AND tipo=2");
      }

      while (rs.next()) {
          Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

          Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
  
          obras.setId(rs.getInt("idobra"));

        Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
            rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);

        revisoes.setId(rs.getInt("idrevisao"));
        listagemRevisoes += revisoes + "\n";
      }
      return listagemRevisoes;
    }catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  /**
   * Pesquisa pedidos de revisão de um determinado revisor por estado.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param idRevisor     Id do revisor cujos pedidos de revisão serão pesquisados.
   * @param dadosPesquis  Estado dos pedidos de revisão a ser considerado.
   * @return string com a listagem dos pedidos de revisão encontrados, ou null em caso de erro.
   */
  public String pesquisarPedidosRevisaoDeRevisorEstado(Statement st, int idRevisor, int dadosPesquisa) {
    String listagemRevisoes = "";
  
    try {
      ResultSet rs = st.executeQuery(" SELECT * \r\n"
            + " FROM revisao\r\n"
            + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
            + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
            + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
            + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
            + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
            + " WHERE estadoRevisao ='" + dadosPesquisa + "' AND idutilizador ='"+ idRevisor +"' AND tipo=2");
  
        while (rs.next()) {
            Autores autor = new Autores(rs.getString("login"), rs.getString("password"), rs.getString("nome"), rs.getBoolean("estado"), rs.getString("email"), rs.getInt("tipo"), rs.getInt("nif"), rs.getString("morada"), rs.getInt("telefone"), rs.getString("estiloliterario"), rs.getString("inicioatividade"));

            Obras obras = new Obras(autor, rs.getString("titulo"), rs.getString("subTitulo"),rs.getString("estiloliterario"),
                  rs.getInt("tipopublicacao"),rs.getInt("npaginas"),rs.getInt("codisbn"),rs.getInt("nedicao"),
                  rs.getString("datasubmissao"),rs.getString("dataaprovacao"));
    
            obras.setId(rs.getInt("idobra"));
  
          Revisao revisoes = new Revisao(rs.getString("nSerie"), rs.getString("datarealizacao"),
              rs.getString("datafinal"), rs.getString("observacoes"), rs.getFloat("custoprocesso"), rs.getInt("estadorevisao"),obras);
  
          revisoes.setId(rs.getInt("idrevisao"));
          listagemRevisoes += revisoes + "\n";
        }
        return listagemRevisoes;
    }catch (SQLException e){
      System.out.println("Erro ao ligar a base de dados");
      return null;
    }
  }

  //------------------------------------------------------------------

  /**
   * Obtém o Id do último de pedido de revisão inserido na base de dados.
   *
   * @param st Uma instância de Statement para executar queries na base de dados.
   * @return ultimoId, isto é, o Id do último pedido de revisão inserido na base de dados, ou -1 em caso de erro.
   */
  public int idUltimoPedido(Statement st) {
    try {
      ResultSet rs = st.executeQuery(" SELECT MAX(idrevisao) AS 'ultimoid' FROM revisao");

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
   * Gera um número de série para um pedido de revisão com base num número sequencial e na data atual.
   *
   * @param nSequencial Número sequencial a ser utilizado na criação do número de série.
   * @return numeroSerie, isto é, do número de série gerado.
   */
  public String geraNSerie(int nSequencial) {
	  
	SimpleDateFormat dataFormato = new SimpleDateFormat("yyyyMMddHHmmss");
    Date dataAtual = new Date();
    String dataFormatada = dataFormato.format(dataAtual);

    nSequencial = nSequencial + 1;
        
    String numeroSerie = nSequencial + dataFormatada;
        
    return numeroSerie;
  }

  /**
   * Adiciona um novo pedido de revisão à base de dados.
   *
   * @param st              Uma instância de Statement para executar queries na base de dados.
   * @param nSerie          Número de série do pedido de revisão.
   * @param dataSubmissao   Data de submissão do pedido de revisão.
   * @param dataFinal       Data final do pedido de revisão.
   * @param aObservacoes    Observações associadas ao pedido de revisão.
   * @param aCustoProcesso  Custo associado ao processo de revisão.
   * @param aEstadoRevisao  Estado do pedido de revisão.
   * @param aIdObra         Id da obra associada ao pedido de revisão.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean adicionarPedidoRevisao(Statement st, String nSerie, Date dataSubmissao, LocalDate dataFinal, String aObservacoes, int aCustoProcesso, int aEstadoRevisao, int aIdObra) {
    try {
      st.execute("INSERT INTO revisao (nserie, datarealizacao, datafinal, observacoes, custoprocesso, estadorevisao, obrasliterarias_idobra) " +
         " VALUES ('" + nSerie + "', '" + dataSubmissao + "', " + dataFinal + ", " + aObservacoes + ", " + aCustoProcesso + ", " + aEstadoRevisao + ", " + aIdObra + ")");

      return true;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados " + e.getMessage());
      return false;
    }
  }

  /**
   * Atribui um utilizador (autor ou revisor) a um pedido de revisão na base de dados.
   *
   * @param st            Uma instância de Statement para executar queries na base de dados.
   * @param aIdUtilizador Id do utilizador a ser atribuído ao pedido de revisão.
   * @param aIdRevisao    Id do pedido de revisão.
   * @return true se a operação for bem-sucedida, false caso contrário.
   */
  public boolean atribuirUtilizadorRevisao(Statement st, int aIdUtilizador, int aIdRevisao) {
    try {
        st.executeUpdate(" INSERT INTO utilizadores_revisao (utilizadores_idutilizador, revisao_idrevisao) VALUES (" + aIdUtilizador + " , " + aIdRevisao + ")");

        return true;

      }catch (SQLException e){
        System.out.println("Erro ao ligar a base de dados " + e.getMessage());
        return false;
      }
  }

  /**
   * Obtém o ID do autor associado a um pedido de revisão na base de dados.
   *
   * @param st                Uma instância de Statement para executar queries na base de dados.
   * @param aIdRevisao        Id do pedido de revisão.
   * @param aTipoUtilizador   Tipo de utilizador (autor ou revisor).
   * @return Id do autor associado ao pedido de revisão, ou -1 em caso de erro.
   */
  public int obterIdAutorPorRevisao(Statement st, int aIdRevisao, int aTipoUtilizador) {
    try {

      ResultSet rs = st.executeQuery(" SELECT idutilizador \r\n"
        + " FROM revisao\r\n"
        + " JOIN obrasliterarias ON obrasliterarias_idobra = idobra\r\n"
        + " JOIN utilizadores_revisao ur ON idrevisao = ur.revisao_idrevisao\r\n"
        + " JOIN utilizadores u ON u.idutilizador = ur.utilizadores_idutilizador\r\n"
        + " LEFT JOIN autores a ON a.utilizadores_idutilizador = u.idutilizador\r\n"
        + " LEFT JOIN revisores r ON r.utilizadores_idutilizador = u.idutilizador\r\n"
        + " WHERE idrevisao =" + aIdRevisao + " AND tipo=" + aTipoUtilizador);

        while (rs.next()) {
          return rs.getInt("idutilizador");
        }
        return -1;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return -1;
    }
  }

  /**
   * Pesquisa revisores disponíveis para atribuição a um pedido de revisão na base de dados.
   *
   * @param st         Uma instância de Statement para executar queries na base de dados.
   * @param aIdRevisao Id do pedido de revisão.
   * @return string com a listagem de revisores disponíveis, ou null em caso de erro.
   */
  public String pesquisaRevisoresDisponiveis(Statement st, int aIdRevisao) {
    String listagemUtilizadores = "";

    try {
      ResultSet rs = st.executeQuery(" SELECT * FROM utilizadores WHERE idutilizador NOT IN (SELECT utilizadores_idutilizador FROM utilizadores_revisao WHERE revisao_idrevisao = " + aIdRevisao + ") AND tipo = 2");

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
   * Obtém o valor a ser pago por um determinado pedido de revisão por parte do autor.
   *
   * @param st         Uma instância de Statement para executar queries na base de dados.
   * @param aIdRevisao Id do pedido de revisão.
   * @return valor a ser pago pelo pedido de revisão, ou -1 em caso de erro.
   */
  public float valorAPagar(Statement st, int aIdRevisao) {
    try {

      ResultSet rs = st.executeQuery(" SELECT custoprocesso FROM revisao WHERE idrevisao =" + aIdRevisao);

        while (rs.next()) {
          return rs.getFloat("custoprocesso");
        }
        return -1;

    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados");
      return -1;
    }
  }
}
