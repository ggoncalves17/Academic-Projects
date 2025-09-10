/*
Aluno: Guilherme Afonso Ferreira Gonçalves - a2022156457
Unidade Curricular: Sistemas Operativos (LINUX)
*/

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <signal.h>
#include <pthread.h>
#include <string.h>
#include <semaphore.h>

struct argumentosThread {
    char nomeFicheiro[30];
    int numeroColuna;
    int numeroRepeticoes;
    int existenciaLinha;
};

pthread_mutex_t mutex;
sem_t semaforoMutex; 


//PROTÓTIPOS DAS FUNÇÕES
void limpaBuffer();
void listagemProcessosUtilizador();
void listagemProcessosGrep();
void estatisticaDescritiva();
void estatisticaDescritivaAWK();
void calculoMedianaDesvio ();
void apagarFicheiros();
void infoFicheiro();
void criacaoFilhos();
void sigusr1_filhoA();
void sigusr2_filhoB();
void sigterm_filhoC();
void sigusr1_pai();
void sigusr2_pai();
void sigterm_pai();
void* thread_transformaColunaLinha();
void* thread_transforma3ColunasLinhas();
void* threadMutex_transforma3ColunasLinhas();
void* threadSemMutex_transforma3ColunasLinhas();
void inicializaArgumentosThread();

int main () {

    int escolha, opcao;


    do {
        system("clear");
        printf("************************ MENU *************************\n");
        printf("1. Ficha 1 - Processos\n");
        printf("2. Ficha 2 - Criacao de Processos\n");
        printf("3. Ficha 3 - Sinais\n");
        printf("4. Ficha 4 - Threads\n");
        printf("5. Ficha 5 - Sincronizacao\n");
        printf("0. Sair\n");
        printf("*******************************************************\n");

        printf("Escolha uma opcao: ");
        scanf("%d", &escolha);
        printf("\n");

        switch (escolha) {
            case 1:
                do {
                    system("clear");
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
                    printf("0. Voltar ao menu principal\n");
                    printf("------------------------------------------------------\n");

                    printf("Escolha uma opcao: ");
                    scanf("%d", &opcao);
                    printf("\n");

                    switch (opcao) {
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
                        case 0:
                            break;
                        default:
                            printf("Opcao invalida!\n");
                    }
                } while (opcao != 0);
                break;
            case 2:
                do {
                    system("clear");
                    printf("----------------------- Ficha 2 ----------------------\n");
                    printf("1. Calculo da mediana e desvio padrao\n");
                    printf("2. Apagar ficheiros de resultados\n");
                    printf("0. Voltar ao menu principal\n");
                    printf("------------------------------------------------------\n");

                    printf("Escolha uma opcao: ");
                    scanf("%d", &opcao);
                    printf("\n");

                    switch (opcao) {
                        case 1:
                            calculoMedianaDesvio ();
                            break;
                        case 2:
                            apagarFicheiros();
                            break;
                        case 0:
                            break;
                        default:
                            printf("Opcao invalida!\n");
                    }
                } while (opcao != 0);
                break;
            case 3:

                pid_t pid_filhoA, pid_filhoB, pid_filhoC;

                do {
                    system("clear");
                    printf("----------------------- Ficha 3 ----------------------\n");
                    printf("1. Calculo da mediana e desvio padrao\n");
                    printf("2. Apagar ficheiros de resultados\n");
                    printf("0. Voltar ao menu principal\n");
                    printf("------------------------------------------------------\n");

                    pid_filhoA = fork();
                    if (pid_filhoA == 0) {
                        signal(SIGUSR1, sigusr1_filhoA);
                        printf("PROCESSO FILHO A (PID %d): aguarda receção de sinal!\n", getpid());
                        while (1){
                            pause();
                        } 
                    }
                      
                    pid_filhoB = fork();
                    if (pid_filhoB == 0) {
                    signal(SIGUSR2,sigusr2_filhoB);
                        printf("PROCESSO FILHO B (PID %d): aguarda receção de sinal!\n", getpid());
                        while (1){
                            pause();
                        }
                    }
                    
                    pid_filhoC = fork();
                    if (pid_filhoC == 0) {
                        signal(SIGTERM, sigterm_filhoC);
                        printf("PROCESSO FILHO C (PID %d): aguarda receção de sinal!\n", getpid());
                        while (1){
                            pause();
                        }
                    }
                
                    printf("PROCESSO PAI (PID %d): criei os processos A (PID %d), B (PID %d) e C (PID %d).\n", getpid(), pid_filhoA, pid_filhoB, pid_filhoC);
                    printf("Por favor introduza a opção pretendida: \n");                

                    scanf("%d", &opcao);
                    printf("\n");

                    switch (opcao) {
                        case 1:
                            signal(SIGUSR1, sigusr1_pai);             
                            kill(pid_filhoA, SIGUSR1);
                            printf("PROCESSO PAI (PID %d): sinal SIGUSR1 enviado ao processo filho A (PID %d).\n", getpid(), pid_filhoA);
                            pause();     
                            printf("PROCESSO PAI (PID %d): sinal SIGUSR1 recebido do processo filho A (PID %d). Sinal SIGUSR2 será de seguida enviado ao processo filho B (PID %d) para apresentação do conteúdo dos ficheiros gerados.Pressione uma tecla para continuar…\n", getpid(), pid_filhoA, pid_filhoB);
                            limpaBuffer();
                            getchar();

                            signal(SIGUSR2, sigusr2_pai);             
                            kill(pid_filhoB, SIGUSR2);
                            printf("PROCESSO PAI (PID %d): sinal SIGUSR2 enviado ao processo filho B (PID %d).\n", getpid(), pid_filhoB);
                            pause(); 
                            printf("PROCESSO PAI (PID %d): sinal SIGUSR2 recebido do processo filho B (PID %d). Sinal SIGTERM será de seguida enviado ao processo filho C(PID %d) para geracao do histograma.Pressione uma tecla para continuar…\n", getpid(), pid_filhoB, pid_filhoC);
                            limpaBuffer();
                            getchar();


                            signal(SIGTERM, sigterm_pai);             
                            kill(pid_filhoC, SIGTERM);
                            printf("PROCESSO PAI (PID %d): sinal SIGTERM enviado ao processo filho C (PID %d).\n", getpid(), pid_filhoC);
                            pause(); 
                            printf("PROCESSO PAI (PID %d): sinal SIGTERM recebido do processo filho C (PID %d).\n", getpid(), pid_filhoC);

                            printf("\nPID: %d - **Pressione enter para voltar ao menu**\n", getpid());
                            limpaBuffer();
                            getchar();
                            break;
                        case 2:
                            system("rm ficha03_mediana.dat");
                            system("rm ficha03_coluna.dat");
                            printf("PROCESSO PAI (PID %d): Eliminacao com sucesso dos ficheiros .dat.", getpid());

                            printf("\nPID: %d - **Pressione enter para voltar ao menu**\n", getpid());
                            limpaBuffer();
                            getchar();
                            break;
                        case 0:
                            kill(pid_filhoA, SIGKILL);
                            kill(pid_filhoB, SIGKILL);
                            kill(pid_filhoC, SIGKILL);
                            printf("PROCESSO PAI (PID %d): Sinal SIGKILL enviado aos filhos com PIDs %d, %d e %d. Retornando ao menu...\n", getpid(), pid_filhoA, pid_filhoB, pid_filhoC);

                            printf("\nPID: %d - **Pressione enter para voltar ao menu**\n", getpid());
                            limpaBuffer();
                            getchar();
                            break;
                        default:
                            printf("Opcao invalida!\n");
                    }
                } while (opcao != 0);
                break;
            case 4:
                do {
                    system("clear");
                    printf("----------------------- Ficha 4 ----------------------\n");
                    printf("1. Transformacao de coluna em linha com repeticoes\n");
                    printf("2. Extracao e gravacao de colunas\n");
                    printf("0. Voltar ao menu principal\n");
                    printf("------------------------------------------------------\n");

                    printf("Escolha uma opcao: ");
                    scanf("%d", &opcao);
                    printf("\n");

                    switch (opcao) {
                        case 1:
                            pthread_t thread_tid;

                            pthread_create(&thread_tid, NULL, thread_transformaColunaLinha, NULL);
                            pthread_join(thread_tid, NULL); 
                            break;
                        case 2:
                            pthread_t threadA_tid, threadB_tid, threadC_tid;
                            struct argumentosThread argsThreadA, argsThreadB, argsThreadC;

                            system("awk '' > PL_Programacao_04_Threads.csv");
                            printf("Geracao do ficheiro PL_Programacao_04_Threads.csv efetuada com sucesso.\n");

                            inicializaArgumentosThread(&argsThreadA, &argsThreadB, &argsThreadC); 

                            pthread_create(&threadA_tid, NULL, thread_transforma3ColunasLinhas, (void*)&argsThreadA);
                            pthread_create(&threadB_tid, NULL, thread_transforma3ColunasLinhas, (void*)&argsThreadB);
                            pthread_create(&threadC_tid, NULL, thread_transforma3ColunasLinhas, (void*)&argsThreadC);
                            pthread_join(threadA_tid, NULL);
                            pthread_join(threadB_tid, NULL);
                            pthread_join(threadC_tid, NULL);

                            printf("\nPID: %d - **Pressione enter para voltar ao menu**\n", getpid());
                            limpaBuffer();
                            getchar();
                            break;
                        case 0:
                            break;
                        default:
                            printf("Opcao invalida!\n");
                    }
                } while (opcao != 0);
                break;
            case 5:
                do {
                    system("clear");
                    printf("----------------------- Ficha 5 ----------------------\n");
                    printf("1. Extração e gravação de colunas (pthread_mutex)\n");
                    printf("2. Extracao e gravacao de colunas (sem)\n");
                    printf("0. Voltar ao menu principal\n");
                    printf("------------------------------------------------------\n");

                    printf("Escolha uma opcao: ");
                    scanf("%d", &opcao);
                    printf("\n");

                    pthread_t threadA_tid, threadB_tid, threadC_tid;
                    struct argumentosThread argsThreadA, argsThreadB, argsThreadC;

                    switch (opcao) {
                        case 1:
                            system("awk '' > PL_Programacao_05_Sinc.csv");
                            printf("Geracao do ficheiro PL_Programacao_05_Sinc.csv efetuada com sucesso.\n");

                            if (pthread_mutex_init(&mutex, NULL) != 0) { 
                                printf("\n Inicializacao do mutex falhou!\n"); 
                                return 1; 
                            } 

                            inicializaArgumentosThread(&argsThreadA, &argsThreadB, &argsThreadC); 

                            pthread_create(&threadA_tid, NULL, threadMutex_transforma3ColunasLinhas, (void*)&argsThreadA);
                            pthread_create(&threadB_tid, NULL, threadMutex_transforma3ColunasLinhas, (void*)&argsThreadB);
                            pthread_create(&threadC_tid, NULL, threadMutex_transforma3ColunasLinhas, (void*)&argsThreadC);
                            pthread_join(threadA_tid, NULL);
                            pthread_join(threadB_tid, NULL);
                            pthread_join(threadC_tid, NULL);
                            pthread_mutex_destroy(&mutex);

                            printf("\nPID: %d - **Pressione enter para voltar ao menu**\n", getpid());
                            limpaBuffer();
                            getchar();
                            break;
                        case 2:
                            system("awk '' > PL_Programacao_05_Sinc.csv");
                            printf("Geracao do ficheiro PL_Programacao_05_Sinc.csv efetuada com sucesso.\n");

                            sem_init(&semaforoMutex, 0, 1);

                            inicializaArgumentosThread(&argsThreadA, &argsThreadB, &argsThreadC); 

                            pthread_create(&threadA_tid, NULL, threadSemMutex_transforma3ColunasLinhas, (void*)&argsThreadA);
                            pthread_create(&threadB_tid, NULL, threadSemMutex_transforma3ColunasLinhas, (void*)&argsThreadB);
                            pthread_create(&threadC_tid, NULL, threadSemMutex_transforma3ColunasLinhas, (void*)&argsThreadC);
                            pthread_join(threadA_tid, NULL);
                            pthread_join(threadB_tid, NULL);
                            pthread_join(threadC_tid, NULL);
                            sem_destroy(&semaforoMutex);
                            break;
                        case 0:
                            break;
                        default:
                            printf("Opcao invalida!\n");
                    }
                } while (opcao != 0);
                break;
            case 0:
                printf("A sair...\n");
                break;
            default:
                printf("Opcao invalida!\n");
                printf("\n**Pressione enter para voltar ao menu**\n");
                limpaBuffer();
                getchar();
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
    system(comando);

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

            printf("\nPID: %d - Conteudo da ficha02_mediana.dat:\n", getpid());
            system("cat ficha02_mediana.dat");
            printf("\n");
            printf("\nPID: %d - Conteudo da ficha02_ordenacao.dat:\n", getpid());
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

void sigusr1_filhoA(int numeroSinal){
    printf("PROCESSO FILHO A (PID %d): sinal SIGUSR1 recebido!\n", getpid());
    
    char nomeFicheiro[30], comandoAwk[500], linhaCabecalho[100];
    int existenciaLinha, coluna;
    printf("Introduza o nome do ficheiro que pretende analisar: ");
    scanf("%s", nomeFicheiro);

    infoFicheiro(&existenciaLinha, &coluna);

    //Mediana
    if (existenciaLinha) {
        sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {numValores=asort(a); meioNum=int(numValores/2); if (numValores%%2) printf(\"Mediana: \" a[meioNum] \"\\t\"); else printf(\"Mediana: \"(a[meioNum]+a[meioNum+1])/2 \"\\t\");}' %s >> ficha03_mediana.dat", coluna, nomeFicheiro);
    } 
    else {
        sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {numValores=asort(a); meioNum=int(numValores/2); if (numValores%%2) printf(\"Mediana: \" a[meioNum] \"\\t\"); else printf(\"Mediana: \" (a[meioNum]+a[meioNum+1])/2 \"\\t\");}' %s >> ficha03_mediana.dat", coluna, nomeFicheiro);
    }

    system(comandoAwk);

    //Desvio Padrão
    if (existenciaLinha) {
        sprintf(comandoAwk, "awk -F',' 'NR > 1 { soma+=$%d; somaValorQuadrado+=($%d*$%d)} END {variancia=(somaValorQuadrado/(NR-1));media=(soma/(NR-1));variancia2 = (variancia-(media*media)) ; printf(\"Desvio: \" sqrt(variancia2) \"\\n\") }' %s >> ficha03_mediana.dat", coluna,coluna,coluna, nomeFicheiro);
    } 
    else {
        sprintf(comandoAwk, "awk -F',' '{ soma+=$%d; somaValorQuadrado+=($%d*$%d)} END {variancia=(somaValorQuadrado/NR);media=(soma/NR);variancia2 = (variancia-(media*media)) ; printf(\"Desvio: \" sqrt(variancia2) \"\\n\") }' %s >> ficha03_mediana.dat", coluna,coluna,coluna, nomeFicheiro);
    }

    system(comandoAwk);

    printf("PROCESSO FILHO A (PID %d): ficheiro ficha03_mediana.dat atualizado com sucesso!\n", getpid());

    //Valores da coluna
    if (existenciaLinha) {
        sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {for (i=1; i<=NR-1; i++) {printf(a[i]); printf(\",\");}}' %s > ficha03_coluna.dat", coluna, nomeFicheiro);
    } 
    else {
        sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {for (i=0; i<=NR; i++) {printf(a[i]); printf(\",\");}}' %s > ficha03_coluna.dat", coluna, nomeFicheiro);
    }

    system(comandoAwk);

    printf("PROCESSO FILHO A (PID %d): ficheiro ficha03_coluna.dat atualizado com sucesso!\n", getpid());
    printf("PROCESSO FILHO A (PID %d): Operacoes realizadas com sucesso! \nPressione uma tecla para continuar...\n", getpid());
    limpaBuffer();
    getchar();

    printf("PROCESSO FILHO A (PID %d): SIGUSR1 enviado ao processo pai (PID %d)\n", getpid(), getppid());
    kill(getppid(), SIGUSR1);
}

void sigusr2_filhoB(int numeroSinal){
    printf("PROCESSO FILHO B (PID %d): sinal SIGUSR2 recebido! Pressione uma tecla para continuar...\n", getpid());
    limpaBuffer();
    getchar();

    printf("\nPID: %d - Conteudo da ficha03_mediana.dat:\n", getpid());
    system("cat ficha03_mediana.dat");
    printf("\n");
    printf("\nPID: %d - Conteudo da ficha03_coluna.dat:\n", getpid());
    system("cat ficha03_coluna.dat");

    printf("\nPID: %d - **Tarefa executada com sucesso - Pressione enter para prosseguir**\n", getpid());
    limpaBuffer();
    getchar();

    printf("PROCESSO FILHO B (PID %d): SIGUSR2 enviado ao processo pai (PID %d)\n", getpid(), getppid());
    kill(getppid(), SIGUSR2);
}

void sigterm_filhoC(int numeroSinal){
    printf("PROCESSO FILHO C (PID %d): sinal SIGTERM recebido! Pressione uma tecla para continuar...\n", getpid());
    limpaBuffer();
    getchar();

    printf("\n   Histograma Manual \n");
    printf("----------------------\n");
    printf("| Número  Quantidade |\n");
    printf("----------------------\n");
    system("awk -F ',' '{for(i=1; i<=NF-1; i++) count[$i]++} END {for (num in count) printf \"| %s\\t\\t%d    |\\n\", num, count[num]}' ficha03_coluna.dat");
    printf("----------------------\n");

    printf("PROCESSO FILHO C (PID %d): SIGTERM enviado ao processo pai (PID %d)\n", getpid(), getppid());
    kill(getppid(), SIGTERM);
}

void sigusr1_pai(int numeroSinal) {
    printf("PROCESSO PAI (PID %d): sinal SIGUSR1 recebido!\n", getpid());
}

void sigusr2_pai(int numeroSinal) {
    printf("PROCESSO PAI (PID %d): sinal SIGUSR2 recebido!\n", getpid());
}

void sigterm_pai(int numeroSinal) {
    printf("PROCESSO PAI (PID %d): sinal SIGTERM recebido!\n", getpid());
}

void* thread_transformaColunaLinha () {
    char nomeFicheiro[30], comandoAwk[500], linhaCabecalho[100];
    int existenciaLinha, coluna, numeroRepeticoes;

    printf("PID: %d - Introduza o nome do ficheiro que pretende analisar: ", getpid());
    scanf("%s", nomeFicheiro);

    infoFicheiro(&existenciaLinha, &coluna);

    printf("PID: %d - Introduza o numero de repeticoes que pretende: ", getpid());
    scanf("%d", &numeroRepeticoes);  

    for(int i=0; i<numeroRepeticoes;i++){
        if (existenciaLinha) {
            if (i == 0)
                sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {for (i=1; i<=NR-1; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s > %s_inv.dat", coluna, nomeFicheiro, nomeFicheiro);
            else
                sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {for (i=1; i<=NR-1; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> %s_inv.dat", coluna, nomeFicheiro, nomeFicheiro);
        } 
        else {
            if (i == 0)
                sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {for (i=0; i<=NR; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s > %s_inv.dat", coluna, nomeFicheiro, nomeFicheiro);
            else
                sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {for (i=0; i<=NR; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> %s_inv.dat", coluna, nomeFicheiro, nomeFicheiro);
        }

        system(comandoAwk);
    }

    printf("PID: %d - Geração do ficheiro com troca de coluna por linha com repeticoes efetuada com sucesso. Pressione qualquer tecla para continuar…\n", getpid());
    limpaBuffer();
    getchar();

    pthread_exit(NULL);
    return NULL;
}

void* thread_transforma3ColunasLinhas (void* argumentos) {
    char comandoAwk[500];

    struct argumentosThread* dados = ((struct argumentosThread*)argumentos);
    int coluna = dados->numeroColuna;
    int numeroRepeticoes = dados->numeroRepeticoes;
    int existenciaLinha = dados->existenciaLinha;
    char nomeFicheiro[50];
    strcpy(nomeFicheiro, dados->nomeFicheiro);

    for(int i=0; i<numeroRepeticoes;i++){
        if (existenciaLinha) {
                sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {for (i=1; i<=NR-1; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> PL_Programacao_04_Threads.csv", coluna, nomeFicheiro);
        } 
        else {
                sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {for (i=0; i<=NR; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> PL_Programacao_04_Threads.csv", coluna, nomeFicheiro);
        }

        system(comandoAwk);
    }
    
    printf("Thread %d do processo %d escreveu com sucesso no ficheiro PL_Programacao_04_Threads.csv, a coluna %d repetida %d vezes\n", pthread_self(), getpid(), coluna, numeroRepeticoes);

    pthread_exit(NULL);
    return NULL;
}

void* threadMutex_transforma3ColunasLinhas (void* argumentos) {
    char comandoAwk[500];

    struct argumentosThread* dados = ((struct argumentosThread*)argumentos);
    int coluna = dados->numeroColuna;
    int numeroRepeticoes = dados->numeroRepeticoes;
    int existenciaLinha = dados->existenciaLinha;
    char nomeFicheiro[50];
    strcpy(nomeFicheiro, dados->nomeFicheiro);

    pthread_mutex_lock(&mutex);
    printf("Thread %d entrou na seccao critica.\n", pthread_self());

    for(int i=0; i<numeroRepeticoes;i++){
        if (existenciaLinha) {
                sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {for (i=1; i<=NR-1; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> PL_Programacao_05_Sinc.csv", coluna, nomeFicheiro);
        } 
        else {
                sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {for (i=0; i<=NR; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> PL_Programacao_05_Sinc.csv", coluna, nomeFicheiro);
        }

        system(comandoAwk);
    }

    pthread_mutex_unlock(&mutex); 
    printf("Thread %d saiu da seccao critica.\n", pthread_self());

    printf("Thread %d do processo %d escreveu com sucesso no ficheiro PL_Programacao_05_Sinc.csv, a coluna %d repetida %d vezes\n", pthread_self(), getpid(), coluna, numeroRepeticoes);

    pthread_exit(NULL);
    return NULL;
}

void* threadSemMutex_transforma3ColunasLinhas (void* argumentos) {
    char comandoAwk[500];

    struct argumentosThread* dados = ((struct argumentosThread*)argumentos);
    int coluna = dados->numeroColuna;
    int numeroRepeticoes = dados->numeroRepeticoes;
    int existenciaLinha = dados->existenciaLinha;
    char nomeFicheiro[50];
    strcpy(nomeFicheiro, dados->nomeFicheiro);

    sem_wait(&semaforoMutex);    
    printf("Thread %d entrou na seccao critica.\n", pthread_self());

    for(int i=0; i<numeroRepeticoes;i++){
        if (existenciaLinha) {
                sprintf(comandoAwk, "awk -F',' 'NR > 1 {a[NR-1]=$%d} END {for (i=1; i<=NR-1; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> PL_Programacao_05_Sinc.csv", coluna, nomeFicheiro);
        } 
        else {
                sprintf(comandoAwk, "awk -F',' '{a[NR]=$%d} END {for (i=0; i<=NR; i++) {printf(a[i]); printf(\",\");}printf(\"\\n\");}' %s >> PL_Programacao_05_Sinc.csv", coluna, nomeFicheiro);
        }

        system(comandoAwk);
    }

    sem_post(&semaforoMutex);
    printf("Thread %d saiu da seccao critica.\n", pthread_self());

    printf("Thread %d do processo %d escreveu com sucesso no ficheiro PL_Programacao_05_Sinc.csv, a coluna %d repetida %d vezes\n", pthread_self(), getpid(), coluna, numeroRepeticoes);

    pthread_exit(NULL);
    return NULL;
}

void inicializaArgumentosThread(struct argumentosThread* argsThreadA, struct argumentosThread* argsThreadB, struct argumentosThread* argsThreadC) {
    printf("PID: %d - Introduza o nome do ficheiro que pretende analisar: ", getpid());
    scanf("%s", argsThreadA->nomeFicheiro);
    strcpy(argsThreadB->nomeFicheiro, argsThreadA->nomeFicheiro);
    strcpy(argsThreadC->nomeFicheiro, argsThreadA->nomeFicheiro);

    printf("Existe uma linha de cabecalho no ficheiro? (1-Sim / 0-Nao): ");
    scanf("%d", &argsThreadA->existenciaLinha);
    argsThreadB->existenciaLinha = argsThreadA->existenciaLinha;
    argsThreadC->existenciaLinha = argsThreadA->existenciaLinha;

    printf("Introduza 3 colunas a analisar: ");
    scanf("%d %d %d", &argsThreadA->numeroColuna, &argsThreadB->numeroColuna, &argsThreadC->numeroColuna);

    printf("PID: %d - Introduza o numero de repeticoes que pretende: ", getpid());
    scanf("%d", &argsThreadA->numeroRepeticoes);  
    argsThreadB->numeroRepeticoes = argsThreadA->numeroRepeticoes;
    argsThreadC->numeroRepeticoes = argsThreadA->numeroRepeticoes; 
}