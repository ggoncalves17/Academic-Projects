import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paginacao extends JPanel {
    private static final int linhasPagina = 10;
    private JList<String> lista;
    private DefaultListModel<String> listaModel;
    private JButton botaoAnterior, botaoProxima;
    private int paginaAtual = 0;
    private int totalPaginas = 0;
    private ArrayList<String> dados;
    private Dashboard dashboard;

    public Paginacao(String aDados, Dashboard aDashboard) {
        dados = new ArrayList<>();
        
        for (String linha : aDados.split("\n")) {
            dados.add(linha);
        }
                
        dashboard = aDashboard;
        totalPaginas = (dados.size() + linhasPagina - 1) / linhasPagina;
        setLayout(new BorderLayout());

        listaModel = new DefaultListModel<>();
        lista = new JList<>(listaModel);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //https://docs.oracle.com/javase%2F8%2Fdocs%2Fapi%2F%2F/java/awt/event/MouseListener.html#mouseClicked-java.awt.event.MouseEvent-
        lista.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = lista.getSelectedIndex();
                if (index != -1) {
                    String linhaSelecionada = listaModel.getElementAt(index);
                    String userId = getIdUtilizadorLinha(linhaSelecionada);
                    dashboard.setIdListagensSelecionado(userId);
                }
            }
        });
        add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        botaoAnterior = new JButton("Anterior");
        botaoAnterior.addActionListener(e -> {
            if (paginaAtual > 0) {
                paginaAtual--;
                atualizarLista();
            }
        });
        painelBotoes.add(botaoAnterior);

        botaoProxima = new JButton("Próxima");
        botaoProxima.addActionListener(e -> {
            if (paginaAtual < totalPaginas - 1) {
                paginaAtual++;
                atualizarLista();
            }
        });
        painelBotoes.add(botaoProxima);

        add(painelBotoes, BorderLayout.SOUTH);

        atualizarLista();
    }

    private void atualizarLista() {
        listaModel.clear();
        int inicio = paginaAtual * linhasPagina;
        int fim = Math.min(inicio + linhasPagina, dados.size());
        for (int i = inicio; i < fim; i++) {
            listaModel.addElement(dados.get(i));
        }
        botaoAnterior.setEnabled(paginaAtual > 0);
        botaoProxima.setEnabled(paginaAtual < totalPaginas - 1);
    }
    
    public void atualizarConteudo(String novaListagem) {
        dados.clear();
        for (String linha : novaListagem.split("\n")) {
            dados.add(linha);
        }
        totalPaginas = (dados.size() + linhasPagina - 1) / linhasPagina;
        paginaAtual = 0; 
        atualizarLista();
    }

    private String getIdUtilizadorLinha(String line) {
        String[] parts = line.split(" ");
        return parts[2]; 
    }
}
