import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.Enumeration;

class ManipulaFicheirosTexto {

  // aceder ficheiros
  File ficheiroLeitura;
  File ficheiroEscrita;

  // leitura ficheiro
  FileReader fr;
  BufferedReader br;

  // escrita ficheiro
  FileWriter fw;
  BufferedWriter bw;

  public boolean abrirFicheiroLeitura(String aCaminho) {

    if (aCaminho != null && aCaminho.length() > 0) {
      ficheiroLeitura = new File(aCaminho);

      if (ficheiroLeitura.exists()) {
        try {
          fr = new FileReader(ficheiroLeitura);
          br = new BufferedReader(fr);
          return true;
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }
    return false;
  }

  public boolean abrirFicheiroEscrita(String aCaminho, boolean aAppend) {
    if (aCaminho != null && aCaminho.length() > 0) {
      ficheiroEscrita = new File(aCaminho);

      try {
        fw = new FileWriter(ficheiroEscrita, aAppend);
        bw = new BufferedWriter(fw);
        return true;
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

    }
    return false;
  }

  public boolean fecharFicheiroLeitura() {
    try {
      if (br != null) {
        br.close();
      }
      if (fr != null) {
        fr.close();
      }
      return true;
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return false;
  }

  public boolean fecharFicheiroEscrita() {
    try {
      if (bw != null) {
        bw.close();
      }
      if (fw != null) {
        fw.close();
      }
      return true;
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return false;
  }

  public Vector<String> lerFicheiro() {

    if (br != null) {

      String linha = "";
      Vector<String> conteudo = new Vector<String>();
      do {
        try {
          linha = br.readLine();
          if (linha != null)
            conteudo.addElement(linha);
        } catch (IOException ioe) {
          ioe.printStackTrace();
          return null;
        }

      } while (linha != null);
      return conteudo;

    }
    return null;
  }

  public boolean escreverFicheiro(Vector<String> aConteudo) {

    if (bw != null) {
      if (aConteudo != null && aConteudo.size() > 0) {

        Enumeration<String> indices = aConteudo.elements();
        while (indices.hasMoreElements()) {
          try {
            bw.write(indices.nextElement());
          } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
          }
        }
        return true;
      }

    }
    return false;
  }

  public boolean adicionarCredenciais(String aLogin, String aPassword) {
    Vector<String> vetor = new Vector<>();
    vetor.add(aLogin + "," + aPassword + "\n");
    if (escreverFicheiro(vetor)) {
      return true;
    } else {
      return false;
    }
  }

  // Verificar credenciais do ficheiro credenciais.txt
  public boolean verificarCredenciais(String aLogin, String aPassword) {
    if (br != null) {
      try {
        String st = br.readLine();
        while (st != null) {
          if ((aLogin + "," + aPassword).equals(st)) {
            return true;
          }
          st = br.readLine();
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    return false;
  }
}