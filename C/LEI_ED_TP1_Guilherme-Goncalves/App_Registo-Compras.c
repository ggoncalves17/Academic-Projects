//Guilherme Afonso Ferreira Gonçalves a2022156457
//Estruturas de Dados

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct
{
    char nome[50], contacto[10], email[50], nif[10];
    int num_cliente, cartao, num_compras, num_vales, vales_usados, ativo;
    double valorTotalCompras;
} Cliente;

typedef struct
{
    float valor;
    int num_cliente, loja, dia, mes, ano;
} Compra;

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
    //printf("Total de clientes (Ativos ou Inativos): %d\n", ncliente);
    printf("\n");


    return ncliente;
}

int registarCompraClienteSemCartao (Compra compras[150], int num_compras) {
    printf("Introduza o numero da loja: ");
    scanf("%d", &compras[num_compras].loja);

    printf("Introduza o valor da compra: ");
    scanf("%f", &compras[num_compras].valor);

    printf("Introduza a data da compra (DD MM AAAA): ");
    scanf("%d %d %d", &compras[num_compras].dia, &compras[num_compras].mes, &compras[num_compras].ano);

    compras[num_compras].num_cliente = -1;

    num_compras++;
    printf("Compra anonima registada com sucesso.\n");
    return num_compras;
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

void visualizarComprasCliente(Cliente clientes[50], Compra compras[150], int num_cliente, int num_compras)
{
    int numCliente;
    printf("Introduza o numero do cliente que pretende verificar: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < num_cliente; i++)
    {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo == 1)
        {
            printf("\nCompras realizadas pelo cliente:\n");
            printf ("-----------------------------------------\n");
            
            if (clientes[i].num_compras == 0){
                printf ("Nenhuma compra realizada");
                printf ("\n");
            }
            else {
                for (int j = 0; j < num_compras; j++)
                    {
                        if (compras[j].num_cliente == numCliente)
                        {
                            printf("Valor: %.2f euros\n", compras[j].valor);
                            printf("Data: %02d/%02d/%04d\n", compras[j].dia, compras[j].mes, compras[j].ano);
                            printf("Loja: %d\n", compras[j].loja);
                            printf("\n");
                        }
                    }
            }
            return;
        }
    }

    printf("(Lista vazia ou cliente inativo ou invalido)\n");
    printf("\n");

}

int registarCompraCliente(Cliente clientes[50], Compra compras[150],int num_clientes, int num_compras, int vales)
{
    int numCliente;
    printf("Introduza o numero do cliente que pretende registar a compra: ");
    scanf("%d", &numCliente);

    for (int i = 0; i < num_clientes; i++)
    {
        if (clientes[i].num_cliente == numCliente && clientes[i].ativo == 1)
        {
            int escolhaVale;
            printf("Introduza o valor da compra: ");
            scanf("%f", &compras[num_compras].valor);

            fflush(stdin);

            printf("Introduza a data da compra (DD MM AAAA): ");
            scanf("%d %d %d", &compras[num_compras].dia, &compras[num_compras].mes, &compras[num_compras].ano);

            fflush(stdin);
            
            printf("Introduza o numero da loja: ");
            scanf("%d", &compras[num_compras].loja);
 
            compras[num_compras].num_cliente = numCliente;

            compras[num_compras].valor -= (compras[num_compras].valor * 0.05);
            printf ("Desconto por ter cartao, aplicado na compra (5 porcento)!\n");

            clientes[i].valorTotalCompras += compras[num_compras].valor;
            clientes[i].num_compras++;

            int valorCompras = clientes[i].valorTotalCompras;
            int numVales = valorCompras / 100;
            clientes[i].num_vales = numVales;

            if (clientes[i].num_vales > 0){
                do
                {
                    printf("Deseja utilizar um vale de desconto? (1 - Sim, 0 - Nao): ");
                    scanf("%d", &escolhaVale);
                } while (escolhaVale < 0 || escolhaVale > 1);

                if (escolhaVale == 1) {
                    compras[num_compras].valor = compras[num_compras].valor - 10;
                    clientes[i].valorTotalCompras -= 10;
                    printf ("Vale de 10 euros utilizado com sucesso.\n");
                    clientes[i].vales_usados++;
                    clientes[i].num_vales--;
                }
            }
            num_compras++;
            printf("Compra registada com sucesso.\n");
            return num_compras;
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

void ordenarClientes(Cliente clientes[50], int num_clientes)
{
    int contador = 0;

    for (int i = 0; i < num_clientes - 1; i++)
    {
        for (int j = 0; j < num_clientes - i - 1; j++)
        {
            if (clientes[j].valorTotalCompras < clientes[j + 1].valorTotalCompras)
            {
                Cliente aux = clientes[j];
                clientes[j] = clientes[j + 1];
                clientes[j + 1] = aux;
            }
        }
    }

    printf("\nClientes ordenados por valor total de compras:\n");
    printf ("-----------------------------------------------\n");

    for (int i = 0; i < num_clientes; i++)
    {
        if (clientes[i].ativo == 1)
        {
            printf("Numero de cliente: %d\n", clientes[i].num_cliente);
            printf("Nome: %s\n", clientes[i].nome);
            printf("Contacto: %s\n", clientes[i].contacto);
            printf("Email: %s\n", clientes[i].email);
            printf("NIF: %s\n", clientes[i].nif);
            printf("Valor total das compras realizadas: %.2lf\n", clientes[i].valorTotalCompras);
            printf("\n");
            contador++;
        }
    }

    if (contador == 0)
        printf("(Lista vazia ou nenhum cliente ativo)\n");
}

void criarCSV1(Cliente clientes[50], Compra compras [150], int num_compras) {
    char nomeFicheiro[50];
    char dataInicio [] = "01-06-2023";
    char dataFim []= "30-06-2023";
    int num_loja;

    printf("Introduza o numero da loja: ");
    scanf("%d", &num_loja);

    sprintf(nomeFicheiro, "%d_%s_%s.csv", num_loja, dataInicio, dataFim);

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

    for (int i = 0; i < num_compras; i++) {
        if (compras[i].loja == num_loja) {
            if (clientes[compras[i].num_cliente].ativo == 1){
                if (compras[i].mes == 6 && compras[i].ano == 2023) {
                    fprintf(ficheiro, "%02d%02d%04d;%.2f;%d\n",compras[i].dia, compras[i].mes, compras[i].ano,compras[i].valor, compras[i].num_cliente);
                }
            }
            else
                if (compras[i].mes == 6 && compras[i].ano == 2023) {
                    fprintf(ficheiro, "%02d%02d%04d;%.2f\n",compras[i].dia, compras[i].mes, compras[i].ano,compras[i].valor);
                }
        }
    }
                                                                                                                                                                                                         
    fclose(ficheiro);
    printf("Ficheiro CSV criado com sucesso\n");
}

void criarCSVLojasIntervalo(Cliente clientes [50], Compra compras[150], int num_compras) {
    char nomeFicheiro[50];

    int diaInicial, mesInicial, anoInicial, diaFinal, mesFinal, anoFinal;
    printf ("Introduza a data inicial (DD MM AAAA): ");
    scanf ("%d %d %d", &diaInicial, &mesInicial, &anoInicial);

    printf ("Introduza a data final (DD MM AAAA): ");
    scanf ("%d %d %d", &diaFinal, &mesFinal, &anoFinal);

    sprintf(nomeFicheiro, "%02d-%02d-%04d_%02d-%02d-%04d.csv",diaInicial, mesInicial, anoInicial, diaFinal, mesFinal, anoFinal);

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

    for (int i = 0; i < num_compras; i++) {
        if (clientes[compras[i].num_cliente].ativo == 1){
            if ((compras[i].dia >= diaInicial && compras[i].mes >= mesInicial && compras[i].ano >= anoInicial) && (compras[i].dia <= diaFinal && compras[i].mes <= mesFinal && compras[i].ano <= anoFinal)) {
                fprintf(ficheiro, "%04d%02d%02d;%.2f;%d\n", compras[i].ano, compras[i].mes, compras[i].dia, compras[i].valor, compras[i].num_cliente);
            }
        }
        else   
            if ((compras[i].dia >= diaInicial && compras[i].mes >= mesInicial && compras[i].ano >= anoInicial) && (compras[i].dia <= diaFinal && compras[i].mes <= mesFinal && compras[i].ano <= anoFinal)) {
                fprintf(ficheiro, "%04d%02d%02d;%.2f\n", compras[i].ano, compras[i].mes, compras[i].dia, compras[i].valor);
            }
    }

    fclose(ficheiro);
    printf("Ficheiro CSV criado com sucesso\n");
}

void guardarDados (Cliente clientes [50], Compra compras[150], int totalClientes, int totalCompras) {

    FILE* ficheiroClientes = fopen("dadosClientes.dat", "wb");
    if (ficheiroClientes == NULL) {
        printf("Erro ao guardar dados dos clientes.\n");
        return;
    }

    FILE* ficheiroCompras = fopen("dadosCompras.dat", "wb");
    if (ficheiroCompras == NULL) {
        printf("Erro ao guardar dados das compras.\n");
        return;
    }

    fwrite (&totalClientes, sizeof(int), 1, ficheiroClientes);
    fwrite(clientes, sizeof(Cliente), 50, ficheiroClientes);
    fwrite (&totalCompras, sizeof(int), 1, ficheiroCompras);
    fwrite(compras, sizeof(Compra), 150, ficheiroCompras);

    fclose(ficheiroClientes);
    fclose(ficheiroCompras);

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

int carregarDadosCompras (Compra compras[150]) {

    int totalCompras;

    FILE* ficheiroCompras = fopen("dadosCompras.dat", "rb");
    if (ficheiroCompras == NULL) {
        printf("Erro ao carregar dados das compras.\n");
        return 0;
    }

    fread (&totalCompras, sizeof(int), 1, ficheiroCompras);
    fread(compras, sizeof(Compra), 150, ficheiroCompras);

    fclose(ficheiroCompras);

    return totalCompras;

}

int main()
{

    int nClientes = 0, num_compras = 0, vales = 0;
    int opcao;
    char opcaoCliente;

    Cliente clientes[50];
    Compra compras[150];

    nClientes = carregarDadosFicheiro (clientes);
    num_compras = carregarDadosCompras  (compras);

    do
    {
        system("cls"); // limpa o terminal (Windows)

        puts("***********************************");
        printf("1 - Registar novo cliente\n");
        printf("2 - Registar novo cliente sem cartao\n");
        printf("3 - Remover cliente\n");
        printf("4 - Listar clientes\n");
        printf("5 - Cliente em particular\n");
        printf("6 - Ordenar clientes por valor total de compras\n");
        printf("7 - Criar arquivo CSV por loja e intervalo de tempo definido\n");
        printf("8 - Criar arquivos CSV por loja e intervalo de tempo pedido\n");
        printf("9 - Guardar dados\n");
        printf("10 - Sair\n");
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
            num_compras = registarCompraClienteSemCartao(compras, num_compras);
            break;
        case 3:
            printf("Opcao 3 selecionada\n");
            removerCliente(clientes, nClientes);
            break;
        case 4:
            printf("Opcao 4 selecionada\n");
            listarClientes(clientes, nClientes);
            break;
        case 5:

            system("cls");
            printf("Opcao 5 selecionada\n");

            puts("*************Cliente*************");
            printf("a. Editar os dados do cliente\n");
            printf("b. Visualizar todas as compras registadas\n");
            printf("c. Registar uma nova compra\n");
            printf("d. Ver valor total e media das compras, numero de vales e verificar vale disponivel\n");
            printf("e. Voltar ao menu principal\n");
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
                visualizarComprasCliente(clientes, compras, nClientes, num_compras);
            break;

            case 'c':
            case 'C':
                printf("Opcao 'c' selecionada\n");
                num_compras = registarCompraCliente(clientes, compras, nClientes,num_compras, vales);
                break;
            case 'd':
            case 'D':
                printf("Opcao 'd' selecionada\n");
                informacoesCliente(clientes, nClientes);
            break;

            case 'e':
            case 'E':
                printf("Voltar ao menu principal\n");
            break;

            default:
                printf("Opcao invalida\n");
                break;
            }
            break;
        case 6:
            printf("Opcao 6 selecionada\n");
            ordenarClientes(clientes, nClientes);
            break;
        case 7:
            printf("Opcao 7 selecionada\n");
            criarCSV1(clientes, compras, num_compras);
            break;
        case 8:
            printf("Opcao 8 selecionada\n");
            criarCSVLojasIntervalo(clientes, compras, num_compras);
            break;
        case 9:
            printf("Opcao 9 selecionada\n");
            guardarDados(clientes, compras,nClientes,num_compras);
            break;
        case 10:
            printf("Sair do programa...\n");
            break;
        default:
            printf("Opcao invalida. Tente novamente.\n");
            break;
        }
        printf("**Pressione enter para continuar**\n");
        fflush(stdin);
        getchar();
    } while (opcao != 10);

    return 0;
}
