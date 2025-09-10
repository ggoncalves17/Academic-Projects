/*
Aluno: Guilherme Afonso Ferreira Gonçalves - a2022156457
Unidade Curricular: Sistemas Operativos (LINUX)
*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>


//PROTÓTIPOS DAS FUNÇÕES
void limpaBuffer();
void listagemProcessosUtilizador();
void listagemProcessosGrep();
void estatisticaDescritiva();
void estatisticaDescritivaAWK();
void calculoMedianaDesvio ();
void apagarFicheiros();
void infoFicheiro();

int main () {

    int escolha;

    do {
        system("clear");
        printf("\n**************** Ficha 1 - Processos: ****************\n");
        printf("--------------- Listagem de Processos: ---------------\n");
        printf("1. Listagem de Processos\n");
        printf("2. Listagem de Processos com parametros definidos pelo utilizador\n");
        printf("3. Listagem de Processo a escolha do utilizador\n");
        printf("4. Listagem de Processos em tempo real\n");
        printf("------------------ Consultar PIDs: -------------------\n");
        printf("5. PID do processo atual\n");
        printf("6. PID do processo pai\n");
        printf("------------------------------------------------------\n");
        printf("7. Estatistica Descritiva (Media e Variancia)\n");
        printf("8. Estatistica Descritiva (Media e Variancia) com awk\n");
        printf("\n*********** Ficha 2 - Criacao de Processos: ***********\n");
        printf("9. Calculo da mediana e desvio padrao\n");
        printf("10. Apagar ficheiros de resultados\n");
        printf("*******************************************************\n");
        printf("0. Sair\n");
        printf("Escolha uma opcao: ");
        scanf("%d", &escolha);
        printf("\n");
        
        switch (escolha) {
            case 1:
                system("ps aux");
                printf("\n**Pressione enter para voltar ao menu**\n");
                limpaBuffer();
                getchar();
                break;
            case 2:
                listagemProcessosUtilizador();
                break;
            case 3:
                listagemProcessosGrep();
                break;
            case 4:
                system("top");
                break;
            case 5:
                printf("PID do processo atual: %d\n", getpid());
                printf("\n**Pressione enter para voltar ao menu**\n");
                limpaBuffer();
                getchar();
                break;
            case 6:
                printf("PID do processo pai: %d\n", getppid());
                printf("\n**Pressione enter para voltar ao menu**\n");
                limpaBuffer();
                getchar();
                break;
            case 7:
                estatisticaDescritiva();
                break;
            case 8:
                estatisticaDescritivaAWK();
                break;
            case 9:
                calculoMedianaDesvio ();
                break;
            case 10:
                apagarFicheiros();
                break;
            case 0:
                printf("A sair...\n");
                break;
            default:
                printf("Opcao invalida!\n");
        }
    } while (escolha != 0);
}

void limpaBuffer() {
    int c;
    while ((c = getchar()) != '\n' && c != EOF) { }
}

void listagemProcessosUtilizador() {
    char parametro [30], comando [50];
    printf("Introduza os parâmetros que pretende: ");
    scanf("%s", parametro);
    sprintf(comando, "ps %s", parametro);
    system(comando);

    printf("\n**Pressione enter para voltar ao menu**\n");
    limpaBuffer();
    getchar();
}

void listagemProcessosGrep() {
    char processo[30], comando [50];
    printf("Introduza o nome do processo que pretende: ");
    scanf("%s", processo);
    sprintf(comando, "ps aux | grep %s", processo);

    printf("\n**Pressione enter para voltar ao menu**\n");
    limpaBuffer();
    getchar();
}

void estatisticaDescritiva() {
    char nomeFicheiro[100], linhaCabecalho[100];
    int existenciaLinha, coluna;

    printf("Introduza o nome do ficheiro que pretende analisar: ");
    scanf("%s", nomeFicheiro);

    FILE *ficheiro = fopen(nomeFicheiro, "r");
    if (ficheiro == NULL) {
        printf("Erro ao abrir o ficheiro.\n");
        return;
    }
    else {
        printf("Ficheiro aberto com sucesso.\n");
    }

    infoFicheiro(&existenciaLinha, &coluna);

    if (existenciaLinha)
        fgets(linhaCabecalho, sizeof(linhaCabecalho), ficheiro);

    double soma = 0, somaValorQuadrado = 0, valorColuna;
    int contador = 0;

    while (!feof(ficheiro)) {
        for (int i = 1; i < coluna; i++) {
            fscanf(ficheiro, "%*[^,],");
        }
        fscanf(ficheiro, "%lf", &valorColuna);
        
        fscanf(ficheiro, "%*[^\n]\n");

        soma += valorColuna;  
        somaValorQuadrado += (valorColuna * valorColuna);
        contador++;
    }

    fclose(ficheiro);

    double media = soma / contador;
    double variancia = (somaValorQuadrado / contador);
    double variancia2 = (variancia - (media*media));
    
    printf("\n---------- Resultados: ----------\n");
    printf("Media: %.2lf\n", media);
    printf("Variancia: %.2lf\n", variancia2);
    printf("---------------------------------\n");

    printf("\n**Pressione enter para voltar ao menu**\n");
    limpaBuffer();
    getchar();
}

void estatisticaDescritivaAWK() {
    char nomeFicheiro[30], comandoAwk[500], linhaCabecalho[100];
    int existenciaLinha, coluna;
    printf("Introduza o nome do ficheiro que pretende analisar: ");
    scanf("%s", nomeFicheiro);

    infoFicheiro(&existenciaLinha, &coluna);

    printf("\n---------- Resultados: ----------\n");
    if (existenciaLinha) {
        sprintf(comandoAwk, "awk -F',' 'NR > 1 { soma+=$%d} END { printf(\"Media: \" soma/(NR-1)) }' %s", coluna, nomeFicheiro);
    } 
    else {
        sprintf(comandoAwk, "awk -F',' '{ soma+=$%d} END { printf(\"Media: \" soma/NR) }' %s", coluna, nomeFicheiro);
    }
    system(comandoAwk);
    
    if (existenciaLinha) {
        sprintf(comandoAwk, "awk -F',' 'NR > 1 { soma+=$%d; somaValorQuadrado+=($%d*$%d)} END {variancia=(somaValorQuadrado/(NR-1));media=(soma/(NR-1));variancia2 = (variancia-(media*media)) ; printf(\"Variancia: \" variancia2) }' %s", coluna,coluna,coluna, nomeFicheiro);
    } 
    else {
        sprintf(comandoAwk, "awk -F',' '{ soma+=$%d; somaValorQuadrado+=($%d*$%d)} END {variancia=(somaValorQuadrado/NR);media=(soma/NR);variancia2 = (variancia-(media*media)) ; printf(\"Variancia: \" variancia2) }' %s", coluna,coluna,coluna, nomeFicheiro);
    }
    printf("\n");
    system(comandoAwk);
    printf("\n---------------------------------\n");

    printf("\n**Pressione enter para voltar ao menu**\n");
    limpaBuffer();
    getchar();
}

void calculoMedianaDesvio () {
    char nomeFicheiro[30], comandoAwk[500], linhaCabecalho[100];
    int existenciaLinha, coluna;

    printf("PID: %d - Introduza o nome do ficheiro que pretende analisar: ", getpid());
    scanf("%s", nomeFicheiro);

    infoFicheiro(&existenciaLinha, &coluna);

    pid_t pid_filho1 = fork();

    if (pid_filho1 < 0) {
        printf("PID: %d - Erro ao criar o filho", getpid());
        exit(-1);
    }
    else if (pid_filho1 == 0) {

        if (existenciaLinha) {
            sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {numValores=asort(a); meioNum=int(numValores/2); if (numValores%%2) printf(\"Mediana: \" a[meioNum] \"\\t\"); else printf(\"Mediana: \"(a[meioNum]+a[meioNum+1])/2 \"\\t\");}' %s >> ficha02_mediana.dat", coluna, nomeFicheiro);
        } 
        else {
            sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {numValores=asort(a); meioNum=int(numValores/2); if (numValores%%2) printf(\"Mediana: \" a[meioNum] \"\\t\"); else printf(\"Mediana: \" (a[meioNum]+a[meioNum+1])/2 \"\\t\");}' %s >> ficha02_mediana.dat", coluna, nomeFicheiro);
        }

        system(comandoAwk);

        if (existenciaLinha) {
            sprintf(comandoAwk, "awk -F',' 'NR > 1 { soma+=$%d; somaValorQuadrado+=($%d*$%d)} END {variancia=(somaValorQuadrado/(NR-1));media=(soma/(NR-1));variancia2 = (variancia-(media*media)) ; printf(\"Desvio: \" sqrt(variancia2) \"\\n\") }' %s >> ficha02_mediana.dat", coluna,coluna,coluna, nomeFicheiro);
        } 
        
        else {
            sprintf(comandoAwk, "awk -F',' '{ soma+=$%d; somaValorQuadrado+=($%d*$%d)} END {variancia=(somaValorQuadrado/NR);media=(soma/NR);variancia2 = (variancia-(media*media)) ; printf(\"Desvio: \" sqrt(variancia2) \"\\n\") }' %s >> ficha02_mediana.dat", coluna,coluna,coluna, nomeFicheiro);
        }

        system(comandoAwk);

        printf("\nPID: %d - **Tarefa executada com sucesso - Pressione enter para prosseguir**\n", getpid());
        limpaBuffer();
        getchar();
        exit(0);
    
    }
    else {
        wait(NULL);

        pid_t pid_filho2 = fork();

        if (pid_filho2 < 0) {
            printf("PID: %d - Erro ao criar o filho", getpid());
            exit(-1);
        } 
        else if (pid_filho2 == 0) {
            sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {nValores=asort(a); for (i=1; i<=nValores; i++) {printf(a[i]); printf(\",\");} printf(\"\\n\")}' %s >> ficha02_ordenacao.dat", coluna, nomeFicheiro);
            system(comandoAwk);

            printf("\nPID: %d - Conteudo do ficha02_mediana.dat:\n", getpid());
            system("cat ficha02_mediana.dat");
            printf("\n");
            printf("\nPID: %d - Conteudo do ficha02_ordenacao.dat:\n", getpid());
            system("cat ficha02_ordenacao.dat");

            printf("\nPID: %d - **Tarefa executada com sucesso - Pressione enter para prosseguir**\n", getpid());
            limpaBuffer();
            getchar();
            exit(0);
        } 
    }

    wait(NULL);

    printf("\nPID: %d - **Pressione enter para voltar ao menu**\n", getpid());
    limpaBuffer();
    getchar();
}

void apagarFicheiros(){
    pid_t filho1, filho2;

    filho1 = fork();

    if (filho1 < 0) {
        printf("PID: %d - Erro ao criar o filho", getpid());
        exit(-1);
    }
    else if (filho1 == 0) {        
        system("rm ficha02_mediana.dat");
        exit(0);
    }

    filho2 = fork();

    if (filho2 < 0) {
        printf("PID: %d - Erro ao criar o filho", getpid());
        exit(-1);
    }
    else if (filho2 == 0) {        
        system("rm ficha02_ordenacao.dat");
        exit(0);
    }

    wait(NULL);
    wait(NULL);

    printf("PID: %d - Os processos filho terminaram!\n", getpid());

    printf("\nPID: %d - **Pressione enter para voltar ao menu**\n", getpid());
    limpaBuffer();
    getchar();
}


void infoFicheiro(int *existenciaLinha, int *coluna) {
    printf("Existe uma linha de cabecalho no ficheiro? (1-Sim / 0-Nao): ");
    scanf("%d", existenciaLinha);

    printf("Introduza o numero da coluna a analisar: ");
    scanf("%d", coluna);
}