import java.sql.*;
import java.util.Properties;
import java.io.*;

class LigacaoBD {

  private Statement st;
  private Connection conn;
  private Properties properties = new Properties();
  private String aCaminho = "src/BaseDados.properties";

  // aceder ficheiros
  File ficheiroLeitura;
  File ficheiroEscrita;

  // leitura ficheiro
  FileInputStream fis;
  FileOutputStream fos;

  /**
   * Construtor da classe LigacaoBD.
   */
  public LigacaoBD() {

  }

  /**
   * Carrega as propriedades do ficheiro de configuração.
   * @return true se as propriedades forem carregadas com sucesso, caso contrário false.
   */
  public boolean carregarPropriedades() {
      try{
        ficheiroLeitura = new File(aCaminho);

        if(ficheiroLeitura.exists()){
          try {
            fis = new FileInputStream(ficheiroLeitura);
            properties.load(fis);
            return true;
          } catch (IOException ioe) {
            ioe.printStackTrace();
          } finally {
              if (fis != null) {
                  fis.close();
              }
          }
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      return false;
  }

  /**
   * Estabelece a ligação com a base de dados.
   * @return true se a ligação for estabelecida com sucesso, caso contrário false.
   */
  public boolean conectar() {
    try {  
      Class.forName("com.mysql.cj.jdbc.Driver");
      String connURL = properties.getProperty("db.url");
      String user = properties.getProperty("db.user");
      String password = properties.getProperty("db.password");
      conn = DriverManager.getConnection(connURL, user, password);
      st = conn.createStatement();
      return true;
    } catch (SQLException e) {
      System.out.println("Erro ao ligar à base de dados.");
      System.out.println("Erro: " + e);
      return false;
    } catch (ClassNotFoundException e) {
      System.out.println("Classe não encontrada.");
      return false;
    }
  }

  /**
   * Termina a conexão com a base de dados.
   * @return true se a ligação for fechada com sucesso, caso contrário false.
   */
  public boolean desconectar() {
    try {
      if (st != null) {
        st.close();
      }
      if (conn != null) {
        conn.close();
      }
      return true;
    } catch (SQLException e) {
        System.out.println("Erro ao ligar à base de dados.");
        return false;
    }
  }
  
  /**
   * Atualiza as propriedades de conexão.
   * @param url Nova URL de conexão.
   * @param user Novo nome de usuário.
   * @param password Nova senha.
   * @return true se as propriedades foram atualizadas com sucesso, caso contrário false.
   */
  public boolean atualizarPropriedades(String url, String user, String password) {
      properties.setProperty("db.url", url);
      properties.setProperty("db.user", user);
      properties.setProperty("db.password", password);
      return true;
  }

  /**
   * Escreve as propriedades atualizadas no arquivo de configuração.
   * @return true se as propriedades foram escritas com sucesso no arquivo, caso contrário false.
   */
  public boolean escreverPropriedades(){
      ficheiroEscrita = new File(aCaminho);
      if(ficheiroEscrita.exists()){
        try {
          fos = new FileOutputStream(ficheiroEscrita);
          properties.store(fos, "Arquivo de configuração");
          return true;
        } catch (IOException ioe) {
          ioe.printStackTrace();
          return false;
        }
      } else {
        return false;
      }
    
  }

  /**
   * Devolve a instância de Statement.
   * @return st, instância de Statement.
   */
  public Statement getStatement() {
    return st;
  }
}