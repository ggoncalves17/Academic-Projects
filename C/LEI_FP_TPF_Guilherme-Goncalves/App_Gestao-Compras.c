/*
Aluno: Guilherme Afonso Ferreira Gonçalves
Unidade Curricular: Fundamentos de programacao
Objetivo: Implementar um programa para gestão de listas de compras
*/

#include <stdio.h>
#include <string.h>

#define max_listas 10
#define max_itens 100
#define descricao 50

//Protótipos das funções
void adicionarlista();
int verificar_datalimite();
void adicionaritem();
void alterardadoslista ();
void alterardadositem ();
void apresentarlistas();
void consultarlista();


char listas[max_listas][descricao];
char itens[max_listas][max_itens][descricao];
int quantidade[max_listas][max_itens];
char unidade[max_listas][max_itens];
int itens_comprados[max_listas][max_itens];
int tamanho_lista[max_listas];
int lista_dia [max_listas];
int lista_mes [max_listas];
int lista_ano [max_listas];

int num_listas = 0;

int main() {
  
  char opcao;
  int id_lista, id_item;

  do{
        system("cls"); // limpa o terminal (Windows)
        
        puts("***********************************");
        puts("Gestao de lista de compras     ");
        puts("1 - Adicionar nova lista       ");
        puts("2 - Adicionar item a lista     ");
        puts("3 - Informar compra de um item ");
        puts("4 - Alterar dados de uma lista ");
        puts("5 - Alterar dados de um item   ");
        puts("6 - Apresentar listas          ");
        puts("7 - Consulta lista de compras  ");
        puts("8 - Guardar dados em ficheiros ");
        puts("9 - Sair do programa           ");
        puts("***********************************");
        printf("Opcao: ");

        fflush(stdin);
        scanf("%c", &opcao);

        switch(opcao){
        
          case '1': //Opção para poder adicionar nova lista
            printf("Opcao 1 selecionada\n");

            if (num_listas < max_listas) {
              adicionarlista();
            } 
            else {
              printf("Numero máximo de listas atingido (10)\n");
            }
          break;

          case '2': //Opção para poder adicionar novo item
            printf("Opcao 2 selecionada\n");

            if (num_listas == 0) {
              printf("Nao existem listas inseridas\n");
            } 
            else {
              printf("Introduza o id da lista: ");
              scanf("%d", &id_lista);
              id_lista--;
              if (id_lista >= 0 && id_lista < num_listas) 
                  adicionaritem(id_lista);
              else 
                printf("ERRO - Id da lista invalido\n");
            }
          break;

          case '3': //Opção para poder informar compra de um item
            printf("Opcao 3 selecionada\n");

            if (num_listas == 0) 
            printf("Nao existem listas inseridas\n");
            else {
              printf("Introduza o id da lista: ");
              scanf("%d", &id_lista);
              id_lista--;
              consultarlista(id_lista);
              if (id_lista >= 0 && id_lista < num_listas) {
                if (tamanho_lista[id_lista] == 0) {
                  printf("Nao existem itens nesta lista\n");
                } 
                else {
                  printf("Introduza o id do item: ");
                  scanf("%d", &id_item);
                  id_item--;
                  if (id_item >= 0 && id_item < tamanho_lista[id_lista]) {
                      itens_comprados[id_lista][id_item] = 1;;
                  } 
                  else
                    printf("ERRO - Id do item invalido\n");
                }
              } 
              else
                printf("ERRO - Id da lista inválido\n");
            }
          break;

          case '4': //Opção para poder alterar dados de uma lista
            printf("Opcao 4 selecionada\n"); 

            if (num_listas == 0) 
            printf("Nao existem listas inseridas\n");
            else {
              printf("Introduza o id da lista que pretende alterar: ");
              scanf("%d", &id_lista);
              id_lista--;
              if (id_lista >= 0 && id_lista < num_listas)
                  alterardadoslista(id_lista);
              else 
                printf("ERRO - Id da lista invalido\n");
            }
            break;

          case '5': //Opção para poder alterar dados de um item
            printf("Opcao 5 selecionada\n"); 

            if (num_listas == 0) 
            printf("Nao existem listas inseridas\n");
            else {
              printf("Introduza o id da lista que pretende alterar: ");
              scanf("%d", &id_lista);
              id_lista--;
              consultarlista(id_lista);
              if (id_lista >= 0 && id_lista < num_listas) {
                if (tamanho_lista[id_lista] == 0) {
                  printf("Nao existem itens nesta lista\n");
                } 
                else {
                  printf("Introduza o id do item que pretende alterar: ");
                  scanf("%d", &id_item);
                  id_item--;
                  if (id_item >= 0 && id_item < tamanho_lista[id_lista]) {
                    alterardadositem(id_lista, id_item);
                  } 
                  else
                    printf("ERRO - Id do item invalido\n");
                }
              } 
              else
                printf("ERRO - Id da lista inválido\n");
            }
            break;

          case '6': //Opção para poder apresentar todas as listas existentes
            if (num_listas == 0)
              printf("Nao existem listas inseridas\n"); 
            else 
              apresentarlistas();
            break;

          case '7': //Opção para apresentar uma determinada lista com as informações detalhadas com os itens
            printf("Opcao 7 selecionada\n");

            if (num_listas == 0) {
              printf("Nao existem listas inseridas\n");
            } 
            else {

              printf("Introduza o id da lista: ");
              scanf("%d", &id_lista);
              id_lista--;
          
              if (id_lista >= 0 && id_lista < num_listas) {
                consultarlista(id_lista);
              } 
              else {
                printf("ERRO - Id da lista invalido\n");
              }
            }
            break;

          case '8': printf("Opcao 8 selecionada\n"); break;

          case '9': printf("Opcao 9 selecionada - Sair do programa\n"); break; //Opção para sair do programa
          
          default:  printf("Opcao invalida\n");
        }

        printf("**Pressione enter para continuar**\n"); fflush(stdin); getchar();
    }
    while(opcao != '9');
}



    //Função para adicionar/criar uma lista
    void adicionarlista() {
      char nomelista[descricao];
      int dia, mes, ano;

      printf("**Pressione enter para adicionar lista**\n"); fflush(stdin); getchar();

      while (1) {
        printf ("Introduza o nome da lista: ");
        fgets (nomelista,50,stdin);
        nomelista [strlen(nomelista)-1] = 0;
        if (strlen(nomelista) != 0)
          break;
        else    
          printf ("ERRO - Nome da lista nao inserido, por favor adicione um nome a lista\n");
      }

      while (1) {
        printf ("Introduza uma data (no formato dd-mm-aaaa): ");
        scanf ("%d-%d-%d", &dia, &mes, &ano);

        if (verificar_datalimite(dia,mes,ano))
            break;
            
        printf ("ERRO - A data inserida e invalida, insira uma valida!\n");
      }

      lista_dia[num_listas] = dia;

      lista_mes[num_listas] = mes;

      lista_ano[num_listas] = ano;

      strcpy(listas[num_listas], nomelista);
      num_listas++;
    }

    //Função para verificar a data limite
    int verificar_datalimite(int dia, int mes, int ano) {
        if (mes < 1 || mes > 12) {
            return 0;
        }
        if (dia < 1) {
            return 0;
        }
        if (mes == 2) {
            if (dia > 29) {
                return 0;
            }
            if (dia > 28 && (ano % 4 != 0 || (ano % 100 == 0 && ano % 400 != 0))) {
                return 0;
            }
        }
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            if (dia > 30) {
                return 0;
            }
        }
        if (dia > 31) {
            return 0;
        }
        return 1;
    }

    //Função para adicionar/criar um item
    void adicionaritem(int id_lista) {
      char nomeitem[descricao];
      int quantidade_item;

      printf("**Pressione enter para adicionar lista**\n"); fflush(stdin); getchar();

      while (1) {
        printf ("Introduza o nome do item: ");
        fgets (nomeitem,50,stdin);
        nomeitem [strlen(nomeitem)-1] = 0;
        if (strlen(nomeitem) != 0)
          break;
        else    
          printf ("ERRO - Nome da lista nao inserido, por favor adicione um nome a lista\n");
      }

      while (1) {
        printf ("Introduza a quantidade do item: ");
        scanf ("%d", &quantidade_item);

        if (quantidade_item > 1)
          break;
        else  
          printf ("ERRO - Quantidade nao inserida ou invalida\n");
      }
      

      strcpy(itens[id_lista][tamanho_lista[id_lista]], nomeitem);

      quantidade[id_lista][tamanho_lista[id_lista]] = quantidade_item;

      itens_comprados[id_lista][tamanho_lista[id_lista]] = 0;

      tamanho_lista[id_lista]++;
    }

    //Função para alterar os dados de uma lista
    void alterardadoslista (int id_lista) {
      char nomelista[descricao];
      int dia, mes, ano;

      printf("**Pressione enter para alterar dados da lista**\n"); fflush(stdin); getchar();

      while (1) {
        printf ("Introduza um novo nome para a lista: ");
        fgets (nomelista,50,stdin);
        nomelista [strlen(nomelista)-1] = 0;
        if (strlen(nomelista) != 0)
          break;
        else    
          printf ("ERRO - Nome da lista nao inserido, por favor adicione um nome a lista\n");
      }

      while (1) {
        printf ("Introduza uma nova data para a lista(no formato dd-mm-aaaa): ");
        scanf ("%d-%d-%d", &dia, &mes, &ano);

        if (verificar_datalimite(dia,mes,ano))
            break;
            
        printf ("ERRO - A data inserida e invalida, insira uma valida!\n");
      }

      lista_dia[id_lista] = dia;

      lista_mes[id_lista] = mes;

      lista_ano[id_lista] = ano;

      strcpy(listas[id_lista], nomelista);
    }

    //Função para alterar dados de um item
    void alterardadositem (int id_lista, int id_item) {
      char nomeitem[descricao];
      int quantidade_item;
      char unidade[10];

      printf("**Pressione enter para alterar dados de um item**\n"); fflush(stdin); getchar();

      while (1) {
        printf ("Introduza o novo nome do item: ");
        fgets (nomeitem,50,stdin);
        nomeitem [strlen(nomeitem)-1] = 0;
        if (strlen(nomeitem) != 0)
          break;
        else    
          printf ("ERRO - Nome da lista nao inserido, por favor adicione um nome a lista\n");
      }

      printf("Introduza a quantidade do item: ");
      scanf("%d", &quantidade_item);

      strcpy(itens[id_lista][id_item], nomeitem);

      quantidade[id_lista][id_item] = quantidade_item;
    }

    //Função para apresentar todas as listas disponíveis
    void apresentarlistas() {
      printf ("\n");
      printf ("Identif.    lista          data-limite     num_itens    comprados\n");
      printf ("------------------------------------------------------------------\n");
      for (int i = 0; i < num_listas; i++) {
        int itenscomprados = 0;
        for (int j = 0; j < tamanho_lista[i]; j++) {
          if (itens_comprados[i][j])
            itenscomprados++;
        }
        printf(" %3d\t   %-12s\t  %d-%d-%d\t %7d\t %3d\n", i + 1, listas[i], lista_dia[i], lista_mes[i], lista_ano[i], tamanho_lista[i], itenscomprados);
      }
      printf ("------------------------------------------------------------------\n");
    }

    //Função para apresentar um lista especifica introduzida pelo utilizador
    void consultarlista(int id_lista) {
      printf ("\n");
      printf ("Lista: %d\n", id_lista + 1);
      printf("Nome: %s\n", listas[id_lista]);
      printf ("\nIdentif.   descr_item   quantidade   comprado\n");
      printf ("------------------------------------------------\n");
      for (int i = 0; i < tamanho_lista[id_lista]; i++) {
        printf(" %3d\t  %-10s\t %d\t %5d\n", i + 1, itens[id_lista][i],quantidade[id_lista][i],itens_comprados[id_lista][i]);
      }
      printf ("------------------------------------------------\n");
    }