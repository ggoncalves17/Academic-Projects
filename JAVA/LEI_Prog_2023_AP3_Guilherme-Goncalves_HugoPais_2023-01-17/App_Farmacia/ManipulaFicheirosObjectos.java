import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

class ManipulaFicheirosObjectos {

  // aceder ficheiros
  File ficheiroLeitura;
  File ficheiroEscrita;

  // leitura ficheiro
  FileInputStream fis;
  ObjectInputStream ois;

  // escrita ficheiro
  FileOutputStream fos;
  ObjectOutputStream oos;

  public boolean abrirFicheiroLeitura(String aCaminho) {

    if (aCaminho != null && aCaminho.length() > 0) {
      ficheiroLeitura = new File(aCaminho);

      if (ficheiroLeitura.exists()) {
        try {
          fis = new FileInputStream(ficheiroLeitura);
          ois = new ObjectInputStream(fis);
          return true;
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }
    return false;
  }

  public boolean abrirFicheiroEscrita(String aCaminho) {
    if (aCaminho != null && aCaminho.length() > 0) {
      ficheiroEscrita = new File(aCaminho);

      try {
          fos = new FileOutputStream(aCaminho);
          oos = new ObjectOutputStream(fos);
          return true;   
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    return false;
  }

  public boolean fecharFicheiroLeitura() {
    try {
      if (ois != null) {
        ois.close();
      }
      if (fis != null) {
        fis.close();
      }
      return true;
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return false;
  }

  public boolean fecharFicheiroEscrita() {
    try {
      if (oos != null) {
        oos.close();
      }
      if (fos != null) {
        fos.close();
      }
      return true;
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return false;
  }

  public GereUtilizadores lerFicheiroUtilizadores() {

    if (ois != null) {
      try {
        if (fis.available() > 0) {
          return (GereUtilizadores) ois.readObject();
        } else {
          System.out.println("O ficheiro está vazio.");
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      }
    }
    return null;
  }

  public boolean escreverFicheiroUtilizadores(GereUtilizadores aConteudo) {

    if (oos != null) {
      if (aConteudo != null) {
        try {
          oos.writeObject(aConteudo);
          oos.flush();
          return true;
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }

    return false;
  }

  public GereServicos lerFicheiroServicos() {

    if (ois != null) {
      try {
        if (fis.available() > 0) {
          return (GereServicos) ois.readObject();
        } else {
          System.out.println("O ficheiro está vazio.");
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      }
    }
    return null;
  }

  public boolean escreverFicheiroServicos(GereServicos aConteudo) {

    if (oos != null) {
      if (aConteudo != null) {
        try {
          oos.writeObject(aConteudo);
          oos.flush();
          return true;
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }

    return false;
  }

  public GereMedicamentos lerFicheiroMedicamentos() {

    if (ois != null) {
      try {
        if (fis.available() > 0) {
          return (GereMedicamentos) ois.readObject();
        } else {
          System.out.println("O ficheiro está vezio.");
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      }
    }
    return null;
  }

  public boolean escreverFicheiroMedicamentos(GereMedicamentos aConteudo) {

    if (oos != null) {
      if (aConteudo != null) {
        try {
          oos.writeObject(aConteudo);
          oos.flush();
          return true;
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }

    return false;
  }
}