//Guilherme Afonso Ferreira Gonçalves a2022156457
//Estruturas de Dados

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct 
{
    int valor;
    struct Vales * proxVale;
} Vales;

typedef struct
{
    float valor;
    int num_cliente, loja, dia, mes, ano;
    struct Compra * proxCompra;
} Compra;

typedef struct
{
    char nome[50], contacto[10], email[50], nif[10];
    int num_cliente, num_compras, num_vales, vales_usados, ativo;
    double valorTotalCompras;
    struct Vales * vale;
    struct Compra * compras;
} Cliente;

int registarNovoClienteComCartao(Cliente clientes[50], int ncliente)
{

    printf("**Pressione enter para adicionar cliente**\n");
    fflush(stdin);
    getchar();

    do
    {
        printf("Nome: ");
        fgets(clientes[ncliente].nome, 50, stdin);
        clientes[ncliente].nome[strlen(clientes[ncliente].nome) - 1] = 0;
    } while (strlen(clientes[ncliente].nome) == 0);

    do
    {
        printf("Contacto: ");
        scanf("%s", clientes[ncliente].contacto);
    } while (strlen(clientes[ncliente].contacto) < 9 || strlen(clientes[ncliente].contacto) > 9);

    fflush(stdin);

    do
    {
        printf("Email: ");
        fgets(clientes[ncliente].email, 50, stdin);
        clientes[ncliente].email[strlen(clientes[ncliente].email) - 1] = 0;
    } while (strlen(clientes[ncliente].email) == 0);

    do
    {
        printf("NIF: ");
        scanf("%s", clientes[ncliente].nif);
    } while (strlen(clientes[ncliente].nif) < 9 || strlen(clientes[ncliente].nif) > 9);

    clientes[ncliente].ativo = 1;

    clientes[ncliente].num_cliente = ncliente;
    printf("Numero de cliente: %d\n", ncliente);

    printf ("\n");

    ncliente++;

    printf("*Registo realizado com sucesso!*\n");
    printf("\n");

    return ncliente;
}

void removerCliente(Cliente clientes[50], int num_clientes)
{
    int numCliente;
    printf("Introduza o numero do cliente que pretende remover: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < num_clientes; i++)
    {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo)
        {
            clientes[i].ativo = 0;
            printf("Cliente removido com sucesso.\n");
            return;
        }
    }

    printf("Cliente nao encontrado. Verifique se inseriu o numero correto ou se o cliente existe.\n");
    printf("\n");
}

void listarClientes(Cliente clientes[50], int numCliente)
{
  int contador = 0;

    for (int i = 0; i < numCliente - 1; i++)
    {
        for (int j = 0; j < numCliente - i - 1; j++)
        {
            if (clientes[j].num_cliente > clientes[j + 1].num_cliente)
            {
                Cliente aux = clientes[j];
                clientes[j] = clientes[j + 1];
                clientes[j + 1] = aux;
            }
        }
    }

    printf("\nClientes:\n");
    printf("------------------------------ \n");

    for (int i = 0; i < numCliente; i++)
    {
        if (clientes[i].ativo == 1)
        {
            printf("Numero de cliente: %d\n", clientes[i].num_cliente);
            printf("Nome: %s\n", clientes[i].nome);
            printf("Contacto: %s\n", clientes[i].contacto);
            printf("Email: %s\n", clientes[i].email);
            printf("NIF: %s\n", clientes[i].nif);
            printf("\n");
            contador++;
        }
    }

    if (contador == 0)
        printf("(Lista vazia ou nenhum cliente ativo)\n");     
}

void editarDadosCliente(Cliente clientes[50], int nClientes)
{
    int numCliente;

    printf("Introduza o numero do cliente a ser editado: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < nClientes; i++)
    {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo == 1)
        {
            fflush(stdin);

            do
            {
                printf("Nome: ");
                fgets(clientes[i].nome, 50, stdin);
                clientes[i].nome[strlen(clientes[i].nome) - 1] = 0;
            } while (strlen(clientes[i].nome) == 0);

            do
            {
                printf("Contacto: ");
                scanf("%s", clientes[i].contacto);
            } while (strlen(clientes[i].contacto) < 9 || strlen(clientes[i].contacto) > 9);

            fflush(stdin);

            do
            {
                printf("Email: ");
                fgets(clientes[i].email, 50, stdin);
                clientes[i].email[strlen(clientes[i].email) - 1] = 0;
            } while (strlen(clientes[i].email) == 0);

            do
            {
                printf("NIF: ");
                scanf("%s", clientes[i].nif);
            } while (strlen(clientes[i].nif) < 9 || strlen(clientes[i].nif) > 9);

            printf("Dados do cliente atualizados com sucesso.\n");
            return;
        }
    }

    printf("(Lista vazia ou nenhum cliente ativo)\n");
}


void visualizarComprasCliente(Cliente clientes[50], int num_clientes)
{
    int numCliente;
    printf("Introduza o numero do cliente que pretende exibir as compras: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < num_clientes; i++)
    {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo == 1)
        {
            printf("\nCompras realizadas pelo cliente:\n");
            printf ("-----------------------------------------\n");

            Compra* compra = clientes[i].compras;

            if (compra == NULL)
            {
                printf ("Nenhuma compra realizada");
                printf ("\n");
            }

            while (compra != NULL)
            {
                printf("Valor: %.2f euros\n", compra->valor);
                printf("Data: %02d/%02d/%04d\n", compra->dia, compra->mes, compra->ano);
                printf("Loja: %d\n", compra->loja);
                printf("\n");

                compra = compra->proxCompra;
            }
            return;
        }
    }

    printf("(Lista vazia ou cliente inativo ou invalido)\n");
    printf("\n");

}

void registarCompraCliente(Cliente* clientes, int num_clientes)
{
    int numCliente;
    printf("Introduza o numero do cliente que pretende registar a compra: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < num_clientes; i++)
    {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo == 1)
        {
            Compra* novaCompra = (Compra*)malloc(sizeof(Compra));

            printf("Introduza o valor da compra: ");
            scanf("%f", &novaCompra->valor);

            fflush(stdin);

            printf("Introduza a data da compra (DD MM AAAA): ");
            scanf("%d %d %d", &novaCompra->dia, &novaCompra->mes, &novaCompra->ano);

            fflush(stdin);

            printf("Introduza o numero da loja: ");
            scanf("%d", &novaCompra->loja);

            novaCompra->proxCompra = clientes[i].compras;
            clientes[i].compras = novaCompra;

            novaCompra->valor -= (novaCompra->valor * 0.05);
            printf ("Desconto por ter cartao, aplicado na compra (5 porcento)!\n");

            clientes[i].valorTotalCompras += novaCompra->valor;
            clientes[i].num_compras++;

            int valorCompras = clientes[i].valorTotalCompras;
            int numVales = valorCompras / 100;
            clientes[i].num_vales = numVales;

            Vales* novoVale = (Vales*)malloc(sizeof(Vales));

            if (clientes[i].num_vales > 0) {
                int escolhaVale;

                for (int j = 0; j < clientes[i].num_vales; j++) {
                    novoVale->valor = 10;

                    novoVale->proxVale = clientes[i].vale;
                    clientes[i].vale = novoVale;
                }

                do
                {
                    printf("Deseja utilizar um vale de desconto? (1 - Sim, 0 - Nao): ");
                    scanf("%d", &escolhaVale);
                } while (escolhaVale < 0 || escolhaVale > 1);

                if (escolhaVale == 1 && clientes[i].vale != NULL) {
                    Vales* primeiroVale = clientes[i].vale;
                    clientes[i].vale = primeiroVale->proxVale;

                    novaCompra->valor -= primeiroVale->valor;
                    clientes[i].valorTotalCompras -= primeiroVale->valor;
                    printf("Vale de 10 euros utilizado com sucesso.\n");

                    free(primeiroVale);

                    clientes[i].vales_usados++;
                    clientes[i].num_vales--;
                }
            }
            printf("Compra registada com sucesso.\n");
            return;
        }
    }

    printf("(Lista vazia ou cliente inativo ou invalido)\n");
    printf("\n");

}

void informacoesCliente(Cliente clientes[50], int num_clientes)
{
    int numCliente;
    printf("Introduza o numero do cliente: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < num_clientes; i++)
    {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo == 1)
        {
            printf("\nValor total das compras realizadas: %.2lf euros\n", clientes[i].valorTotalCompras);
            if (clientes[i].num_compras > 0)
                printf("Media de cada compra realizada: %.2f euros\n", clientes[i].valorTotalCompras / clientes[i].num_compras);
            else 
                printf("Media das compras realizadas: 0.00 euros\n");
            printf("Quantidade de vales usados: %d \n", clientes[i].vales_usados);
            if (clientes[i].num_vales > 0)
                printf("Existe(m) %d vale(s) disponiveis\n", clientes[i].num_vales);
            else
                printf("Nao existem vales disponiveis\n");
            return;
        }
    }

    printf("(Lista vazia ou cliente inativo ou invalido)\n");
}

void ordenarComprasCliente(Cliente clientes[50], int num_clientes){
    int numCliente;
    printf("Introduza o numero do cliente: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < num_clientes; i++) {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo == 1){
            
        Compra* compra = clientes[i].compras;
        Compra* lista = compra;
        Compra* maximo;
        Compra* anterior;

        if (compra == NULL){
            printf("Nenhuma compra realizada\n");
            return;
        }

        while (lista != NULL) {
            maximo = lista;
            Compra* atual = lista->proxCompra;
            anterior = lista;

            while (atual != NULL) {
                if (atual->valor > maximo->valor) {
                    maximo = atual;
                    anterior = lista;
                }
                atual = atual->proxCompra;
                lista = lista->proxCompra;
            }

            if (maximo != lista) {
                anterior->proxCompra = lista;
                lista->proxCompra = maximo->proxCompra;
                maximo->proxCompra = compra;

                lista = maximo;
            } else {
                lista = lista->proxCompra;
            }
        }

        printf ("Lista ordenada com sucesso\n");
        return;
        }
    }
    printf("(Lista vazia ou nenhum cliente ativo)\n");
}

void criarCSVLojasIntervalo(Cliente clientes [50], int num_clientes) {
    char nomeFicheiro[50];
    int num_loja;
    int diaInicial, mesInicial, anoInicial, diaFinal, mesFinal, anoFinal;

    printf("Introduza o numero da loja: ");
    scanf("%d", &num_loja);

    printf ("Introduza a data inicial (DD MM AAAA): ");
    scanf ("%d %d %d", &diaInicial, &mesInicial, &anoInicial);

    printf ("Introduza a data final (DD MM AAAA): ");
    scanf ("%d %d %d", &diaFinal, &mesFinal, &anoFinal);

    sprintf(nomeFicheiro, "%d_%02d-%02d-%04d_%02d-%02d-%04d.csv",num_loja,diaInicial, mesInicial, anoInicial, diaFinal, mesFinal, anoFinal);

    FILE* ficheiro = fopen(nomeFicheiro, "w");
    if (ficheiro == NULL) {
        printf ("ERRO - O ficheiro para ler nao existe ou nao pode ser lido de momento");
        printf ("\n");
        return;
    }

    int cabecalho = 0;

    if (cabecalho == 0) {
    fprintf(ficheiro, "Data;Compra (euros);Numero Cliente\n");
    fprintf(ficheiro, "-------------------------------------------------\n");
    cabecalho = 1;
    }

    for (int i = 0; i < num_clientes; i++) {

        if (clientes[i].compras == NULL)
            continue;

        Compra* compra = clientes[i].compras;

        while (compra != NULL) {
            if ((compra->dia >= diaInicial && compra->mes >= mesInicial && compra->ano >= anoInicial)&&(compra->mes <= mesFinal && compra->ano <= anoFinal)){
                if (compra->loja == num_loja) {
                    fprintf(ficheiro, "%04d%02d%02d;%.2f;%d\n", compra->dia, compra->mes, compra->ano, compra->valor, clientes[i].num_cliente);
                }
            }
            compra = compra->proxCompra;
        }
    }

    fclose(ficheiro);
    printf("Ficheiro CSV criado com sucesso\n");
}

void guardarDados (Cliente clientes [50], int totalClientes) {

    FILE* ficheiroClientes = fopen("dadosClientes.dat", "wb");
    if (ficheiroClientes == NULL) {
        printf("Erro ao guardar dados dos clientes.\n");
        return;
    }

    fwrite (&totalClientes, sizeof(int), 1, ficheiroClientes);
    fwrite(clientes, sizeof(Cliente), 50, ficheiroClientes);

    fclose(ficheiroClientes);

    printf("Dados guardados com sucesso\n");
}

int carregarDadosFicheiro (Cliente clientes [50]) {

    int totalClientes;

    FILE* ficheiroClientes = fopen("dadosClientes.dat", "rb");
    if (ficheiroClientes == NULL) {
        printf("Erro ao carregar dados dos clientes.\n");
        return 0;
    }

    fread (&totalClientes, sizeof(int), 1, ficheiroClientes);
    fread(clientes, sizeof(Cliente), 50, ficheiroClientes);


    fclose(ficheiroClientes);

    return totalClientes;
}

int main()
{

    int nClientes = 0, vales = 0;
    int opcao;
    char opcaoCliente;

    Cliente clientes[50];

    nClientes = carregarDadosFicheiro (clientes);

    do
    {
        system("cls"); // limpa o terminal (Windows)

        puts("***********************************");
        printf("1 - Registar novo cliente\n");
        printf("2 - Remover cliente\n");
        printf("3 - Listar clientes\n");
        printf("4 - Cliente em particular\n");
        printf("5 - Criar arquivo CSV por loja e intervalo de tempo definido\n");
        printf("6 - Guardar dados\n");
        printf("7 - Sair\n");
        puts("***********************************");
        printf("Opcao: ");

        fflush(stdin);
        scanf("%d", &opcao);

        switch (opcao)
        {
        case 1:
            printf("Opcao 1 selecionada\n");
            nClientes = registarNovoClienteComCartao(clientes, nClientes);
            break;
        case 2:
            printf("Opcao 2 selecionada\n");
            removerCliente(clientes, nClientes);
            break;
        case 3:
            printf("Opcao 3 selecionada\n");
            listarClientes(clientes, nClientes);
            break;
        case 4:

            system("cls");
            printf("Opcao 4 selecionada\n");

            puts("*************Cliente*************");
            printf("a. Editar os dados do cliente\n");
            printf("b. Visualizar todas as compras registadas\n");
            printf("c. Registar uma nova compra\n");
            printf("d. Ver valor total e media das compras, numero de vales e verificar vale disponivel\n");
            printf("e. Ordenar compras por ordem decrescente\n");
            printf("f. Voltar ao menu principal\n");
            puts("***********************************");
            printf("Opcao: ");

            fflush(stdin);

            scanf("%c", &opcaoCliente);

            switch (opcaoCliente)
            {
            case 'a':
            case 'A':
                printf("Opcao 'a' selecionada\n");
                editarDadosCliente(clientes, nClientes);
            break;

            case 'b':
            case 'B':
                printf("Opcao 'b' selecionada\n");
                visualizarComprasCliente(clientes,nClientes);
            break;
            case 'c':
            case 'C':
                printf("Opcao 'c' selecionada\n");
                registarCompraCliente(clientes, nClientes);
                break;
            case 'd':
            case 'D':
                printf("Opcao 'd' selecionada\n");
                informacoesCliente(clientes, nClientes);
                break;\
            case 'e':
            case 'E':
                printf("Opcao 'e' selecionada\n");
                ordenarComprasCliente(clientes, nClientes);
                break;
            case 'f':
            case 'F':
                printf("Voltar ao menu principal\n");
            break;

            default:
                printf("Opcao invalida\n");
                break;
            }
            break;
        case 5:
            printf("Opcao 5 selecionada\n");
            criarCSVLojasIntervalo(clientes, nClientes);
            break;
        case 6:
            printf("Opcao 6 selecionada\n");
            guardarDados (clientes, nClientes);
            break;
        case 7:
            printf("Sair do programa...\n");
            carregarDadosFicheiro (clientes);
            break;
        default:
            printf("Opcao invalida. Tente novamente.\n");
            break;
        }
        printf("**Pressione enter para continuar**\n");
        fflush(stdin);
        getchar();
    } while (opcao != 7);

    return 0;
}