import java.io.*; 
import java.util.Date;

class Logger {
    private static final String LogFile = "log.txt";
    private static final String SystemInfoFile = "info_sistema.dat";

    public static void logAcao(String utilizador, String acao) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LogFile, true))) {
            writer.println(utilizador + " " + acao + " " + new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lê o log de ações e retorna como uma String
    public static String lerLog() {
        StringBuilder log = new StringBuilder(); // Baseado em: https://www.simplilearn.com/tutorials/java-tutorial/stringbuilder-in-java
        try (BufferedReader reader = new BufferedReader(new FileReader(LogFile))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                log.insert(0, linha + "\n"); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return log.toString();
    }

    // Regista informações sobre execuções do sistema
    public static void registaInfoSistema(int totalExecucoes, String ultimoUtilizador) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SystemInfoFile))) {
            SistemaInfo sistemaInfo = new SistemaInfo(totalExecucoes, ultimoUtilizador);
            objectOutputStream.writeObject(sistemaInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lê as informações sobre execuções do sistema
    public static SistemaInfo lerInfoSistema() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SystemInfoFile))) {
            return (SistemaInfo) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
