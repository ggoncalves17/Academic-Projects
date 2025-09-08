import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.io.*;

class Main {

  // Inicializar gestores
  private static GereUtilizadores gereUtilizadores = new GereUtilizadores();
  private static GereMedicamentos gereMedicamentos = new GereMedicamentos();
  private static GereServicos gereServicos = new GereServicos();
  private static ManipulaFicheirosTexto manipulaFicheirosTexto = new ManipulaFicheirosTexto();
  private static ManipulaFicheirosObjectos ficheiro = new ManipulaFicheirosObjectos();

  public static void main(String[] args) {

    carregarDadosIniciais();

    // Utilizador autenticado (exemplo)
    Utilizadores utilizadorAutenticado = null;
    int infoFicheiro = 0;

    while (true) {

      if (infoFicheiro != 0) 
        limpaTerminal();
      
      infoFicheiro++;
      
      apresentaMenu(utilizadorAutenticado);

      int opcao = lerDadosInt("Por favor selecione uma opcao:");

      if (opcao == 2 && utilizadorAutenticado == null) {
        utilizadorAutenticado = opcao2(utilizadorAutenticado);
      } else {
        executar(utilizadorAutenticado, opcao);
      }
    }
  }

  public static void limpaTerminal() {  
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
   }

  private static void limpaBuffer() {
    new Scanner(System.in).nextLine();
  }

  private static int lerDadosInt(String aMensagem) {
    System.out.println(aMensagem);
    return new Scanner(System.in).nextInt();
  }

  private static String lerDadosString(String aMensagem) {
    System.out.println(aMensagem);
    return new Scanner(System.in).nextLine();
  }

  private static Float lerDadosFloat(String aMensagem) {
    System.out.println(aMensagem);
    return new Scanner(System.in).nextFloat();
  }

  private static void apresentaMenu(Utilizadores utilizadorAutenticado) {

    System.out.println("------------------------------------ ");
    System.out.println("|               MENU               | ");
    System.out.println("------------------------------------ ");

    if (utilizadorAutenticado == null) {
      System.out.println("1. Registar Utilizador");
      System.out.println("2. Autenticar Utilizador");
    } else {
      System.out.println("3. Editar informacao pessoal");
      System.out.println("4. Consultar servicos realizados");
      System.out.println("5. Pesquisar servico realizado");

      if (utilizadorAutenticado.getTipo() == 1) {
        System.out.println("6. Aprovar pedidos de registo");
        System.out.println("7. Aprovar servicos Pendentes");
        System.out.println("8. Encerrar servico");
      }
      if (utilizadorAutenticado.getTipo() == 2) {
        System.out.println("9. Adicionar Medicamento na farmacia");
        System.out.println("10. Gerir servico de um cliente");
        System.out.println("12. Listar medicamentos ordenados");
      }

      if (utilizadorAutenticado.getTipo() == 3)
        System.out.println("11. Solicitar pedido de servico");
    }

    System.out.println("0. Sair");
    System.out.println("------------------------------------ ");

  }

  private static void executar(Utilizadores utilizadorAutenticado, int aOpcao) {

    if (utilizadorAutenticado != null) {
      Logger.logAcao(utilizadorAutenticado.getNome(), "opcao " + aOpcao);
      atualizarInfoSistema(utilizadorAutenticado);
    }

    if (utilizadorAutenticado == null) {
      switch (aOpcao) {
        case 0:
          opcao0(utilizadorAutenticado);
          System.exit(0);
          break;
        case 1:
          opcao1();
          break;
        default:
          erro();
          break;
      }
    } else {
      switch (aOpcao) {
        case 0:
          opcao0(utilizadorAutenticado);
          System.exit(0);
          break;
        case 3:
          opcao3(utilizadorAutenticado);
          break;
        case 4:
          opcao4(utilizadorAutenticado);
          break;
        case 5:
          opcao5(utilizadorAutenticado);
          break;
        default:
          if (utilizadorAutenticado != null && utilizadorAutenticado.getTipo() == 1) {
            switch (aOpcao) {
              case 6:
                opcao6();
                break;
              case 7:
                opcao7();
                break;
              case 8:
                opcao8();
                break;
              default:
                erro();
                break;
            }
          }
          if (utilizadorAutenticado != null && utilizadorAutenticado.getTipo() == 2) {
            switch (aOpcao) {
              case 9:
                opcao9();
                break;
              case 10:
                opcao10(utilizadorAutenticado);
                break;
              case 12:
                opcao12();
                break;
              default:
                erro();
                break;
            }
          }
          if (utilizadorAutenticado != null && utilizadorAutenticado.getTipo() == 3) {
            switch (aOpcao) {
              case 11:
                opcao11(utilizadorAutenticado);
                break;
              default:
                erro();
                break;
            }
          }
      }
    }
  }

  private static void erro() {
    System.out.println("opcao Invalida!");
  }

  private static void opcao0(Utilizadores utilizadorAutenticado) {
    String caminhoFicheiro = "dados_apl.dat";

    if (ficheiro.abrirFicheiroEscrita(caminhoFicheiro)) {
      if (ficheiro.escreverFicheiroUtilizadores(gereUtilizadores)
          && ficheiro.escreverFicheiroMedicamentos(gereMedicamentos)
          && ficheiro.escreverFicheiroServicos(gereServicos)) {
        if (ficheiro.fecharFicheiroEscrita()) {
          System.out.println("Dados do utilizador guardados com sucesso no ficheiro '" + caminhoFicheiro + "'");
        } else {
          System.out.println(
              "Nao foi possivel gravar dados do utilizador no ficheiro por falha de fecho '" + caminhoFicheiro + "'");
        }
      } else {
        System.out.println("Nao foi possivel gravar dados de utilizador no ficheiro: '" +
            caminhoFicheiro + "'");
      }
    } else {
      System.out.println("Nao foi possivel abrir o ficheiro '" + caminhoFicheiro + "'");
    }

    if (utilizadorAutenticado != null)
      System.out.println("Adeus " + utilizadorAutenticado.getNome());
    else {
      System.out.println("Adeus ");
    }
  }

  // Registar Utilizador
  private static void opcao1() {

    int opcao = lerDadosInt("Escolheu [Registar]. Pretende continuar? (1 - Sim / 0 - Nao)");
    
    if (opcao == 1){
      String nome = lerDadosString("Por favor introduza o nome: ");
      String password = lerDadosString("Por favor introduza a password: ");
      String login, email;
      int id = 0; 
      id = gereUtilizadores.ultimoIdUtilizador(id) + 1;
  
      Clientes cliente = null;
      Utilizadores utilizador = null;
  
      do {
        login = lerDadosString("Por favor introduza o login (username): ");
  
        if (gereUtilizadores.verificaLogin(login))
          System.out.println("O login ja se encontra registado! Por favor, tente outro.");
  
      } while (gereUtilizadores.verificaLogin(login));
  
      do {
        email = lerDadosString("Por favor introduza o email: ");
  
        if (gereUtilizadores.verificaEmail(email))
          System.out.println("O email ja se encontra registado! Por favor, tente outro.");
  
      } while (gereUtilizadores.verificaEmail(email));
  
      int tipo;
      boolean estado;
  
      if (gereUtilizadores.listarUtilizadores() == null) {
        tipo = 1;
        estado = true;
      } else {
        System.out.println("------------------------------- ");
        System.out.println(" 1 - Gestor");
        System.out.println(" 2 - Farmaceutico");
        System.out.println(" 3 - Cliente");
        System.out.println("------------------------------- ");
  
        tipo = lerDadosInt("Por favor introduza o numero relativo ao tipo de utilizador que deseja: ");
        estado = false;
      }
  
      if (tipo == 2 || tipo == 3) {
        int NIF;
        do {
          NIF = lerDadosInt("Por favor introduza o NIF: ");
  
          if (gereUtilizadores.verificaNIF(NIF))
            System.out.println("O NIF ja se encontra registado! Por favor, tente outro.");
  
        } while (gereUtilizadores.verificaNIF(NIF));
  
        int telefone;
        do {
          telefone = lerDadosInt("Por favor introduza o telefone: ");
  
          if (gereUtilizadores.verificaTelefone(telefone))
            System.out.println("O telefone ja se encontra registado! Por favor, tente outro.");
  
        } while (gereUtilizadores.verificaTelefone(telefone));
  
        String morada = lerDadosString("Por favor introduza a morada: ");
  
        cliente = new Clientes(login, password, nome, estado, email, tipo, id, NIF, morada, telefone);
      } else {
        utilizador = new Utilizadores(login, password, nome, estado, email, tipo, id);
      }
  
      if (utilizador != null) {
        if (gereUtilizadores.adicionarUtilizadores(utilizador)) {
          System.out.println("Utilizador adicionado com sucesso!");
          if (manipulaFicheirosTexto.abrirFicheiroEscrita("credenciais.txt", true)) {
            if (manipulaFicheirosTexto.adicionarCredenciais(login, password)) {
              System.out.println("Dados guardados com sucesso!");
            }
          }
          manipulaFicheirosTexto.fecharFicheiroEscrita();
        }
      }
      if (cliente != null) {
        if (gereUtilizadores.adicionarUtilizadores(cliente)) {
          System.out.println("Utilizador adicionado com sucesso!");
          if (manipulaFicheirosTexto.abrirFicheiroEscrita("credenciais.txt", true)) {
            if (manipulaFicheirosTexto.adicionarCredenciais(login, password)) {
              System.out.println("Dados guardados com sucesso!");
            }
          }
          manipulaFicheirosTexto.fecharFicheiroEscrita();
        }
      }
      if (cliente == null && utilizador == null) {
        System.out.println("Erro ao adicionar utilizador!");
      }
    }else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Autenticar Utilizador
  private static Utilizadores opcao2(Utilizadores utilizadorAutenticado) {

    int opcao = lerDadosInt("Escolheu [Autenticar]. Pretende continuar? (1 - Sim / 0 - Nao)");
    
    if (opcao == 1){
      String login, password;
      Utilizadores utilizador = null;
      do {
        login = lerDadosString("Por favor introduza o login (username): ");
        password = lerDadosString("Por favor introduza a password: ");

        if (manipulaFicheirosTexto.abrirFicheiroLeitura("credenciais.txt")) {
          if (manipulaFicheirosTexto.verificarCredenciais(login, password)) {
            if (gereUtilizadores.verificaUtilizador(login, password) != null) {

              utilizador = gereUtilizadores.verificaUtilizador(login, password);
              
              if (utilizador.getEstado() == true) {
                utilizadorAutenticado = utilizador;
                System.out.println("Bem-vindo " + utilizadorAutenticado.getNome());
              } 
              else
                System.out.println("Utilizador nao encontrado (Inativo)!");
            }
            else 
              System.out.println("Login ou password incorretos!");
          } 
          else {
            System.out.println("Login ou password incorretos!");
          }
          manipulaFicheirosTexto.fecharFicheiroLeitura();
        } 
        else {
          System.out.println("Erro a abrir o ficheiro!");
        }
      } while (utilizador == null);
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
      return utilizadorAutenticado;
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
    return null;
  }

  // Editar informacao pessoal
  private static void opcao3(Utilizadores utilizadorAutenticado) {

    int opcao = lerDadosInt("Escolheu [Editar informacao]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){

      String password = lerDadosString("Por favor introduza a nova password: ");
      String nome = lerDadosString("Por favor introduza o novo nome: ");
      String email;

      do {
        email = lerDadosString("Por favor introduza o novo email: ");

        if (gereUtilizadores.verificaEmail(email))
          System.out.println("O email ja se encontra registado! Por favor, tente outro.");

      } while (gereUtilizadores.verificaEmail(email));

      String login = utilizadorAutenticado.getLogin();

      if (manipulaFicheirosTexto.abrirFicheiroEscrita("credenciais.txt", true)) {
        if (manipulaFicheirosTexto.adicionarCredenciais(login, password)) {

          utilizadorAutenticado.setPassword(password);
          utilizadorAutenticado.setNome(nome);
          utilizadorAutenticado.setEmail(email);

          System.out.println("Dados pessoais atualizados com sucesso!");
        }
        manipulaFicheirosTexto.fecharFicheiroEscrita();
      }
      else {
        System.out.println("Erro a abrir o ficheiro!");
      }
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Consultar os servicos realizados
  private static void opcao4(Utilizadores utilizadorAutenticado) {

    int opcao = lerDadosInt("Escolheu [Consultar Servicos]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){

      int idUtilizador = utilizadorAutenticado.getId();
      int tipo = utilizadorAutenticado.getTipo();

      String listagem = gereServicos.listarServicos(idUtilizador, tipo);

      if (listagem != null && listagem != "") {
        System.out.println("------------------------------------ ");
        System.out.println("Listagem dos Servicos realizados: ");
        System.out.println("------------------------------------ ");
        System.out.println(listagem);
      }
      else
        System.out.println("Lista de servicos realizados esta vazia.");  
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Pesquisar servico realizado
  private static void opcao5(Utilizadores utilizadorAutenticado) {

    int opcao = lerDadosInt("Escolheu [Pesquisar Servico]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){

      int idUtilizador = utilizadorAutenticado.getId();
      int tipo = utilizadorAutenticado.getTipo();

      String listagemServicos = gereServicos.listarServicos(idUtilizador, tipo);

      if (listagemServicos != null && listagemServicos != "") {
        int codigo = lerDadosInt("Introduza o codigo do servico");

        String listagem = gereServicos.pesquisarServicosPorCodigoUtilizador(codigo, idUtilizador, tipo);

        if (listagem != null && listagem != "")
          System.out.println(listagem);
        else
          System.out.println("Servico nao encontrado.");
      }
      else
        System.out.println("Lista de servicos esta vazia.");
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Aprovar pedidos de registo
  private static void opcao6() {

    int opcao = lerDadosInt("Escolheu [Aprovar Pedido Registo]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){

      String listagem = gereUtilizadores.listarUtilizadoresPendentes();

      if (listagem != null && listagem != "") {
        System.out.println("------------------------------------ ");
        System.out.println("Listagem dos Utilizadores Pendentes: ");
        System.out.println("------------------------------------ ");

        System.out.println(listagem);

        int idUtilizador = lerDadosInt("Por favor introduza o id do utilizador que pretende aprovar: ");

        if (gereUtilizadores.aprovarUtilizador(idUtilizador)) {
          System.out.println("Utilizador aprovado com sucesso!");
        } else {
          System.out.println("Erro ao aprovar utilizador!");
        }
      } else
        System.out.println("Nao existem registos de utilizadores pendentes. \n");
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Aprovar pedidos de servico Pendentes
  private static void opcao7() {

    int opcao = lerDadosInt("Escolheu [Aprovar Servicos Pendentes]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){

      String listagem = gereServicos.listarServicosEstadoPendente();
      if (listagem != null && listagem != "") {
        System.out.println("------------------------------------ ");
        System.out.println("Listagem dos Servicos Pendentes: ");
        System.out.println("------------------------------------ ");
        System.out.println(listagem);

        String listagemFarmaceuticos = gereUtilizadores.listarUtilizadoresPorTipo(2);

        if (listagemFarmaceuticos != null && listagemFarmaceuticos != "") {

          int codigoServico = lerDadosInt("Por favor introduza o codigo do servico que pretende aprovar: ");

          if (gereServicos.alterarEstadoServico(codigoServico, 2)) {

            System.out.println("Servico aprovado com sucesso!");

            System.out.println("------------------------------------ ");
            System.out.println("Listagem dos Farmaceuticos: ");
            System.out.println("------------------------------------ ");
            System.out.println(listagemFarmaceuticos);

            int idFarmaceutico = lerDadosInt("Por favor introduza o id do farmaceutico a atribuir: ");

            if (gereServicos.atribuirFarmaceutico(codigoServico, idFarmaceutico)) {
              System.out.println("Farmaceutico adicionado com sucesso!");
            } 
            else {
              System.out.println("Erro ao adicionar um farmacêutico!");
            }
          }
        }
        else {
          System.out.println("Nao existem farmaceuticos registados para se poder aprovar o servico!");
        }
      } 
      else
        System.out.println("Nao existem servicos pendentes.");
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Encerrar pedidos de servico
  private static void opcao8() {

    int opcao = lerDadosInt("Escolheu [Encerrar Servico]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){
      String listagem = gereServicos.listarServicosEstadoConcluido();
      if (listagem != null && listagem != "") {
        System.out.println("----------------------------------------------- ");
        System.out.println("Listagem dos Servicos Concluidos para encerrar: ");
        System.out.println("----------------------------------------------- ");
        System.out.println(listagem);

        int codigoServico = lerDadosInt("Por favor introduza o codigo do servico que pretende encerrar: ");

        if (gereServicos.alterarEstadoServico(codigoServico, 5)) {
          String data = gereServicos.pesquisarServicosPorCodigo(codigoServico).getData();
          if (gereServicos.alterarTempoServico(codigoServico, data)) {
            System.out.println("Servico encerrado com sucesso!");
          }
        } else {
          System.out.println("Erro ao encerrar o servico!");
        }
      } else
        System.out.println("Nao existem servicos pendentes para encerrar.");
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Adicionar Medicamentos a farmacia
  private static void opcao9() {

    int opcao = lerDadosInt("Escolheu [Adicionar Medicamento Farmacia]. Pretende continuar? (1 - Sim / 0 - Nao)");
    
    if (opcao == 1) {
      String nome = lerDadosString("Por favor introduza o nome do medicamento");
      String marca = lerDadosString("Por favor introduza a marca do medicamento");
      String lote = lerDadosString("Por favor introduza o lote do medicamento");
      String designacaoComponenteAtiva = lerDadosString("Por favor introduza a designacao da componente ativa");
      int codigo = lerDadosInt("Por favor introduza o codigo da componente ativa");
      int quantidade = lerDadosInt("Por favor introduza a quantidade da componente ativa");
      ComponenteAtiva componenteAtiva = new ComponenteAtiva(designacaoComponenteAtiva, codigo, quantidade);
      int dosagem = lerDadosInt("Por favor introduza a dosagem do medicamento");
      int stock = lerDadosInt("Por favor introduza o stock do medicamento que adicionar");
      float preco = lerDadosFloat("Por favor introduza o preco do medicamento");
      int anoFabrico = lerDadosInt("Por favor introduza o ano de fabrico do medicamento");
      int selecao, gen;
      boolean autorizacaoMedica, medicamentoGenerico;
      do {
        selecao = lerDadosInt("O medicamento necessita de autorizacao medica? 1 - Sim / 0 - Nao");
        if (selecao == 1) {
          autorizacaoMedica = true;
        } else {
          autorizacaoMedica = false;
        }
      } while (selecao != 0 && selecao != 1);
      do {
        gen = lerDadosInt("O medicamento e generico? 1 - Sim / 0 - Nao");
        if (gen == 1) {
          medicamentoGenerico = true;
        } else {
          medicamentoGenerico = false;
        }
      } while (gen != 0 && gen != 1);
      Medicamentos medicamento = new Medicamentos(nome, marca, lote, componenteAtiva, dosagem, stock, preco, anoFabrico,
          autorizacaoMedica, medicamentoGenerico);
      if (gereMedicamentos.adicionarMedicamento(medicamento)) {
        System.out.println("Medicamento adicionado com sucesso!");
      } else {
        System.out.println("Erro ao adicionar medicamento!");
      }
      int maxExcipiente = 0;
      int introduzir;
      do {
        introduzir = lerDadosInt("Pretende introduzir excipientes ao medicamento? 1 - Sim / 0 - Nao");
        if (introduzir == 1 && maxExcipiente < 5) {
          String designacaoExcipiente = lerDadosString("Por favor introduza a designacao do excipiente");
          int classificacao = lerDadosInt("Por favor introduza a classificacao do excipiente");
          int numero = lerDadosInt("Por favor introduza a quantidade do excipiente");
          Excipiente excipiente = new Excipiente(designacaoExcipiente, classificacao, numero);
          medicamento.addExcipiente(excipiente);
          maxExcipiente++;
        }
      } while (introduzir == 0 && maxExcipiente == 5);
      int maxCategoria = 0;
      do {
        introduzir = lerDadosInt("Pretende introduzir categorias ao medicamento? 1 - Sim / 0 - Nao");
        if (introduzir == 1 && maxCategoria < 3) {
          String designacaoCategoria = lerDadosString("Por favor introduza a designacao da categoria");
          int classificacao = lerDadosInt("Por favor introduza a classificacao da categoria");
          String fornecedor = lerDadosString("Por favor introduza o fornecedor da categoria");
          Categoria categoria = new Categoria(designacaoCategoria, classificacao, fornecedor);
          medicamento.addCategoria(categoria);
          maxCategoria++;
        }
      } while (introduzir == 0 && maxCategoria == 3);
    } else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Gerir servico de um cliente
  private static void opcao10(Utilizadores utilizadorAutenticado) {

    int opcao = lerDadosInt("Escolheu [Gerir servico de cliente]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){

      int idFarmaceutico = utilizadorAutenticado.getId();
      String listagem = gereServicos.listarServicosEstadoAceite(idFarmaceutico);

      if (listagem != null && listagem !="") {
        System.out.println("------------------------------------ ");
        System.out.println("Listagem dos Servicos a iniciar: ");
        System.out.println("------------------------------------ ");
        System.out.println(listagem);
        System.out.println("------------------------------------ ");


        int codigoServico = lerDadosInt("Por favor introduza o codigo do servico que pretende iniciar: ");

        if (gereServicos.alterarEstadoServico(codigoServico, 3)) {
          System.out.println("Servico iniciado com sucesso!");

          Servicos servico = gereServicos.pesquisarServicosPorCodigo(codigoServico);

          if (servico.getDescricao().equals("Medicamentos")) {

            Vector<Medicamentos> listaMedicamentos = servico.getListaMedicamentos();
            Enumeration<Medicamentos> indice = listaMedicamentos.elements();

            float precoTotal = 0;

            while (indice.hasMoreElements()) {
              Medicamentos medicamento = indice.nextElement();
              precoTotal += medicamento.getPreco();
              gereMedicamentos.alterarStock(medicamento.getNome());
            }

            if (gereServicos.alterarPrecoServico(codigoServico, precoTotal)) {
              System.out.println("Preco do servico atualizado com sucesso!");

              if (gereServicos.alterarEstadoServico(codigoServico, 4)) {
                System.out.println("Servico concluido com sucesso!");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dataAtual = new Date();
                String data = dateFormat.format(dataAtual);

                gereServicos.alterarTempoServico(codigoServico, data);
              }
            } else {
              System.out.println("Erro ao atualizar o preco do servico!");
            }
          }
        } else {
          System.out.println("Erro ao iniciar o servico!");
        }
      } else
        System.out.println("Nao existem servicos disponiveis atribuidos.");
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

  // Solicitar pedido de servico (Cliente)
  private static void opcao11(Utilizadores utilizadorAutenticado) {

    int opcao = lerDadosInt("Escolheu [Solicitar pedido de servico]. Pretende continuar? (1 - Sim / 0 - Nao)");
        
    if (opcao == 1){

      int escolhaPedido = lerDadosInt("Por favor introduza o tipo de pedido (1 - Medicamento, 2 - Componentes Ativas)");

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date dataAtual = new Date();
      String dataAtualString = dateFormat.format(dataAtual);
      int codigoServico = 0; 
      codigoServico = gereServicos.ultimoCodigoServico(codigoServico) + 1;

      if (escolhaPedido == 1) {

        Vector<Medicamentos> medicamentosPretendidos = new Vector<>();

        boolean continuar = true;

        while (continuar) {

          String designacaoMedicamento = lerDadosString("Por favor introduza o nome do medicamento pretendido");

          Medicamentos medicamentoPretendido = gereMedicamentos.pesquisarPorDesignacao(designacaoMedicamento);

          if (medicamentoPretendido != null) {
            medicamentosPretendidos.add(medicamentoPretendido);
            System.out.println("Medicamento adicionado a encomenda.");
          } 
          else {
            System.out.println("Medicamento nao encontrado. Por esta razao nao foi inserido.");
          }

          int continuarMedicamentos = lerDadosInt("Pretende adicionar mais medicamentos? (1 - Sim, 0 - Nao)");

          if (continuarMedicamentos == 0)
            continuar = false;
        }

        if (!medicamentosPretendidos.isEmpty()) {

          int idUtilizador = utilizadorAutenticado.getId();

          String desc = "Medicamentos";

          Servicos servico = new Servicos(idUtilizador, -1, medicamentosPretendidos, 0, dataAtualString, -1, desc, false, 1, codigoServico);
          gereServicos.adicionarServico(servico);
          System.out.println("Pedido de servico adicionado com sucesso!");
        }
      } 
      else {

        String componentePretendida = lerDadosString("Por favor introduza o nome da componente ativa pretendida");

        Vector<Medicamentos> listaMedicamentos = gereMedicamentos.pesquisarPorComponenteAtiva(componentePretendida);

        if (listaMedicamentos != null) {
          System.out.println("Medicamentos com a componente ativa adicionada a encomenda.");
        } else {
          System.out.println("Medicamentos nao encontrados com essa componente ativa.");
        }

        int idUtilizador = utilizadorAutenticado.getId();

        String desc = "ComponentesAtivas";

        Servicos servico = new Servicos(idUtilizador, -1, listaMedicamentos, 0, dataAtualString, -1, desc, false, 1, codigoServico);
        gereServicos.adicionarServico(servico);
        System.out.println("Pedido de servico para componentes ativas realizado com sucesso.");
      }
    }
    else {
      if (opcao != 0) {
        System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
      }
    }
    System.out.println("Pressione alguma tecla para continuar!");
    limpaBuffer();
  }

    // Lista de medicamentos ordenados
    private static void opcao12() {

      int opcao = lerDadosInt("Escolheu [Listar medicamentos ordenados]. Pretende continuar? (1 - Sim / 0 - Nao)");
          
      if (opcao == 1){
        if(gereMedicamentos.ordenar()) {

          String listagem = gereMedicamentos.listarMedicamentos();

          if (listagem != null && listagem !="") {
            System.out.println("--------------------------------------------------- ");
            System.out.println("Listagem dos Medicamentos ordenados por designacao: ");
            System.out.println("--------------------------------------------------- ");
            System.out.println(listagem);
            System.out.println("--------------------------------------------------- ");
          } else
            System.out.println("Nao existem medicamentos disponiveis atribuidos.");
        }
        else {
          System.out.println("Erro no ordenamento dos medicamentos!");
        }
      }
      else {
        if (opcao != 0) {
          System.out.println("Operacao Cancelada. Opcao escolhida invalida!");
        }
      }
      System.out.println("Pressione alguma tecla para continuar!");
      limpaBuffer();
    }

  // Carregar Dados do Ficheiro
  private static void carregarDadosIniciais() {
    String caminhoFicheiro = "dados_apl.dat";
    File ficheiroLeitura = new File(caminhoFicheiro);

    if (ficheiroLeitura.exists() && ficheiroLeitura.length() > 0) {
      if (ficheiro.abrirFicheiroLeitura(caminhoFicheiro)) {
        GereUtilizadores utilizadoresLidos = ficheiro.lerFicheiroUtilizadores();
        GereMedicamentos medicamentosLidos = ficheiro.lerFicheiroMedicamentos();
        GereServicos servicosLidos = ficheiro.lerFicheiroServicos();

        ficheiro.fecharFicheiroLeitura();

        if (utilizadoresLidos != null && medicamentosLidos != null && servicosLidos != null) {
          gereUtilizadores = utilizadoresLidos;
          gereMedicamentos = medicamentosLidos;
          gereServicos = servicosLidos;

          System.out.println("Dados carregados com sucesso do ficheiro '" + caminhoFicheiro + "'");
        } else {
          System.out.println("Falha ao ler dados do ficheiro '" + caminhoFicheiro + "'");
        }
      } else {
        System.out.println("Nao foi possivel abrir o ficheiro '" + caminhoFicheiro + "'");
      }
    } else {
      System.out.println("O ficheiro '" + caminhoFicheiro + "' nao existe ou esta vazio.");
    }
  }

  // Logger
  private static void atualizarInfoSistema(Utilizadores utilizadorAutenticado) {
    SistemaInfo sistemaInfo = Logger.lerInfoSistema();

    int totalExecucoes = (sistemaInfo != null) ? sistemaInfo.getTotalExecucoes() + 1 : 1;

    String ultimoUtilizador = utilizadorAutenticado != null ? utilizadorAutenticado.getNome() : "Nenhum";

    Logger.registaInfoSistema(totalExecucoes, ultimoUtilizador);
  }
}