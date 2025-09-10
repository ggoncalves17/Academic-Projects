import os
from dotenv import load_dotenv
from re import I
from functools import wraps
import psycopg2

load_dotenv()

DATABASE_URL = f"host={os.getenv("DB_HOST")} dbname={os.getenv("DB_NAME")} user={os.getenv("DB_USER")} password={os.getenv("DB_PASSWORD")}"

OK_CODE = 200
NOT_FOUND = 404
SERVER_ERROR = 500

# Conexão BD
def ligacao():
    return psycopg2.connect(DATABASE_URL)

# Verificar se Existe Utilizador
def verificaUtilizador(username):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute(
            "SELECT * FROM utilizadores WHERE username = %s", [username])
        utilizador_tuple = cur.fetchone()
        if utilizador_tuple is None:
            return False
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)
        return False

# Registo Utilizador
def adicionarUtilizador(user):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("select registarutilizador(%s, crypt(%s, gen_salt('bf')), %s)",
                    (user["username"], user["password"], user["tipoutilizador"],))
        conn.commit()
        cur.execute(
            "SELECT idutilizador, username FROM utilizadores WHERE username = %s", (user["username"],))
        user_tuple = cur.fetchone()
        if user_tuple is None:
            return None
        utilizador = {
            "idutilizador": user_tuple[0],
            "username": user_tuple[1],
        }
        cur.close()
        conn.close()
        return utilizador
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Autenticação
def autenticar(username, password):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT * FROM utilizadores WHERE username = %s AND password = crypt(%s, password) AND ativo = %s",
                    [username, password, True])
        utilizador_tuple = cur.fetchone()
        if utilizador_tuple is None:
            return None

        utilizador = {
            "idutilizador": utilizador_tuple[0],
            "username": utilizador_tuple[1],
        }
        cur.execute("CALL notificacao_fim_fase_inscricoes()")
        conn.commit()
        cur.close()
        conn.close()
        return utilizador
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)

# Listar Utilizador


def listarUtilizador(user_id):
    utilizador = None
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute(
            "SELECT idutilizador, username FROM utilizadores WHERE idUtilizador = %s", [user_id])
        utilizador_tuple = cur.fetchone()
        if utilizador_tuple:
            utilizador = {
                "id": utilizador_tuple[0],
                "username": utilizador_tuple[1],
            }
        cur.close()
        conn.close()
        return utilizador
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Consultar saldo
def consultarSaldo(idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("select getSaldo(%s)", (idUtilizador,))
        saldo_tuple = cur.fetchone()

        if saldoUtilizador is None:
            return None
        saldo = {
            "saldo": saldo_tuple[0],
        }
        cur.close()
        conn.close()
        return saldo
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Update do saldo do utilizador
def carregarSaldo(idUtilizador, somaSaldo):
    try:
        conn = ligacao()
        cur = conn.cursor()
        # Update do saldo
        cur.execute("CALL atualizarSaldo(%s,%s)", (idUtilizador, somaSaldo))
        conn.commit()
        cur.close()
        conn.close()
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Criar um evento
def criarEvento(evento, idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("CALL criarEvento(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", (evento["nome"], evento["tipo"], evento["localevento"], evento["datainicio"],
                    evento["datafim"], evento["datainscricaoinicio"], evento["datainscricaofim"], evento["lugares"], evento["precoentrada"], idUtilizador,))
        conn.commit()
        cur.execute("SELECT * FROM eventos, utilizadores_eventos WHERE eventos_idevento = idevento AND nomeevento = %s AND utilizadores_idutilizador = %s",
                    (evento["nome"], idUtilizador))
        evento_tuple = cur.fetchone()
        if evento_tuple is None:
            return None
        evento = {
            "idevento": evento_tuple[0],
            "nome": evento_tuple[1],
            "tipo": evento_tuple[2],
            "localevento": evento_tuple[3],
            "datainicio": evento_tuple[4],
            "datafim": evento_tuple[5],
            "datainscricaoinicio": evento_tuple[6],
            "datainscricaofim": evento_tuple[7],
            "lugares": evento_tuple[8],
            "precoentrada": evento_tuple[9],
            "eventoativo": evento_tuple[10],
        }
        cur.close()
        conn.close()
        return evento
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Alterar evento
def alterarEvento(idevento, evento, idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()

        cur.execute("select getPrecoEntrada(%s)", (idevento,))
        precoEvento = cur.fetchone()[0]

        cur.execute("SELECT idevento FROM eventos, utilizadores_eventos WHERE eventos_idevento = idevento AND idevento = %s AND eventoativo = %s AND utilizadores_idutilizador = %s", [idevento,
                    True, idUtilizador])
        ide = cur.fetchone()
        cur.execute("SELECT idutilizador FROM utilizadores WHERE tipoutilizador = %s AND idutilizador = %s", [
                    True, idUtilizador])
        idu = cur.fetchone()
        if idu is None and ide is None:
            return None

        cur.execute("CALL atualizarEvento(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", (idevento, evento["nome"], evento["tipo"], evento["localevento"], evento[
                    "datainicio"], evento["datafim"], evento["datainscricaoinicio"], evento["datainscricaofim"], evento["lugares"], evento["precoentrada"], idUtilizador,))
        conn.commit()
        if precoEvento != evento["precoentrada"]:
            cur.execute("CALL inserirAlteracaoPreco(%s,%s)",
                        (precoEvento, idevento))
            conn.commit()
        cur.execute("SELECT * FROM eventos WHERE nomeevento = %s AND tipo = %s AND datainicio = %s",
                    (evento["nome"], evento["tipo"], evento["datainicio"], ))
        evento_tuple = cur.fetchone()
        if evento_tuple is None:
            return None
        evento = {
            "idevento": evento_tuple[0],
            "nome": evento_tuple[1],
            "tipo": evento_tuple[2],
            "localevento": evento_tuple[3],
            "datainicio": evento_tuple[4],
            "datafim": evento_tuple[5],
            "datainscricaoinicio": evento_tuple[6],
            "datainscricaofim": evento_tuple[7],
            "lugares": evento_tuple[8],
            "precoentrada": evento_tuple[9],
            "eventoativo": evento_tuple[10],
        }
        cur.close()
        conn.close()
        return evento
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Listar todos os eventos
def listarEventos():
    eventos = []
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT * FROM eventos WHERE eventoativo = %s", [True])
        eventos_tuples = cur.fetchall()
        for evento_tuple in eventos_tuples:
            evento = {
                "idevento": evento_tuple[0],
                "nome": evento_tuple[1],
                "tipo": evento_tuple[2],
                "localevento": evento_tuple[3],
                "datainicio": evento_tuple[4],
                "datafim": evento_tuple[5],
                "datainscricaoinicio": evento_tuple[6],
                "datainscricaofim": evento_tuple[7],
                "lugares": evento_tuple[8],
                "precoentrada": evento_tuple[9],
                "eventoativo": evento_tuple[10],
            }
            eventos.append(evento)
        cur.close()
        conn.close()
        return eventos
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)
        return None


# Pesquisar eventos (tipo)
def pesquisarEventos(dados, tipoPesquisa):
    try:
        conn = ligacao()
        cur = conn.cursor()

        if tipoPesquisa == 1:
            cur.execute("SELECT * FROM eventos WHERE tipo = %s", [dados])
        if tipoPesquisa == 2:
            cur.execute("SELECT * FROM eventos WHERE nomeevento = %s", [dados])
        if tipoPesquisa == 3:
            cur.execute("SELECT * FROM eventos WHERE datainicio = %s", [dados])

        eventos = []
        eventos_tuples = cur.fetchall()
        for evento_tuple in eventos_tuples:
            evento = {
                "idevento": evento_tuple[0],
                "nome": evento_tuple[1],
                "tipo": evento_tuple[2],
                "localevento": evento_tuple[3],
                "datainicio": evento_tuple[4],
                "datafim": evento_tuple[5],
                "datainscricaoinicio": evento_tuple[6],
                "datainscricaofim": evento_tuple[7],
                "lugares": evento_tuple[8],
                "precoentrada": evento_tuple[9],
                "eventoativo": evento_tuple[10],
            }
            eventos.append(evento)
        cur.close()
        conn.close()
        return eventos
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Consultar detalhes de evento
def consultarDetalhesEvento(idEvento):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT * FROM eventos WHERE idevento = %s", [idEvento])
        evento_tuple = cur.fetchone()
        if evento_tuple is None:
            return None
        evento = {
            "idevento": evento_tuple[0],
            "nome": evento_tuple[1],
            "tipo": evento_tuple[2],
            "localevento": evento_tuple[3],
            "datainicio": evento_tuple[4],
            "datafim": evento_tuple[5],
            "datainscricaoinicio": evento_tuple[6],
            "datainscricaofim": evento_tuple[7],
            "lugares": evento_tuple[8],
            "precoentrada": evento_tuple[9],
            "eventoativo": evento_tuple[10],
        }
        cur.close()
        conn.close()
        return evento
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Listar eventos do utilizador
def listarEventosUtilizador(idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute(
            "SELECT * FROM eventos WHERE idevento IN (SELECT eventos_idevento FROM utilizadores_eventos WHERE utilizadores_idutilizador = %s)", [idUtilizador])
        eventos = []
        eventos_tuples = cur.fetchall()
        for evento_tuple in eventos_tuples:
            evento = {
                "idevento": evento_tuple[0],
                "nome": evento_tuple[1],
                "tipo": evento_tuple[2],
                "localevento": evento_tuple[3],
                "datainicio": evento_tuple[4],
                "datafim": evento_tuple[5],
                "datainscricaoinicio": evento_tuple[6],
                "datainscricaofim": evento_tuple[7],
                "lugares": evento_tuple[8],
                "precoentrada": evento_tuple[9],
                "eventoativo": evento_tuple[10],
            }
            eventos.append(evento)
        cur.close()
        conn.close()
        return eventos
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Apresentar notificações do utilizador
def notificacoes(idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute(
            "SELECT * FROM eventosNotificacoes WHERE idU = %s AND ativaN = %s", [idUtilizador, True])
        notificacoes = []
        notificacoes_tuples = cur.fetchall()
        for notificacao_tuple in notificacoes_tuples:
            notificacao = {
                "idU": notificacao_tuple[0],
                "nomeU": notificacao_tuple[1],
                "idE": notificacao_tuple[2],
                "nomeE": notificacao_tuple[3],
                "mensagemN": notificacao_tuple[4],
                "dataN": notificacao_tuple[5],
            }
            notificacoes.append(notificacao)
        cur.close()
        conn.close()
        return notificacoes
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Validar se está inscrito em evento
def inscricaoValida(idUtilizador, idEvento):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute(
            "SELECT * FROM inscricoes WHERE utilizadores_idutilizador = %s AND eventos_idevento = %s", [idUtilizador, idEvento])
        inscricao = cur.fetchone()
        if inscricao is None:
            return False
        cur.close()
        conn.close()
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Inserir comentários
def inserirComentario(idEvento, idUtilizador, comentario):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("CALL inserirComentario(%s,%s,%s)",
                    (comentario, idUtilizador, idEvento))
        conn.commit()
        cur.close()
        conn.close()
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Consultar comentários de um evento
def consultarComentarios(idEvento, idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT idcomentario, comentario, idutilizador, username FROM comentarios, utilizadores WHERE idutilizador = utilizadores_idutilizador AND eventos_idevento = %s AND comentarioativo = %s", [
                    idEvento, True])
        comentarios = []
        comentarios_tuples = cur.fetchall()
        for comentario_tuple in comentarios_tuples:
            comentario = {
                "idcomentario": comentario_tuple[0],
                "comentario": comentario_tuple[1],
                "idUtilizador": comentario_tuple[2],
                "username": comentario_tuple[3],
            }
            comentarios.append(comentario)
        cur.close()
        conn.close()
        return comentarios
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Subscrever comentários de evento
def subscreverComentario(idevento, idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute(
            "SELECT idevento FROM eventos WHERE idevento = %s", [idevento])
        existe = cur.fetchone()
        if existe is None:
            return False

        cur.execute("SELECT subscricaocomentarios FROM inscricoes WHERE eventos_idevento = %s AND utilizadores_idutilizador = %s", [
                    idevento, idUtilizador])

        tipoSubscricao = cur.fetchone()
        if tipoSubscricao is None:
            return None

        tipo = not tipoSubscricao[0]

        cur.execute("CALL subscreverComentario(%s,%s,%s)",
                    (tipo, idUtilizador, idevento))
        conn.commit()
        cur.close()
        conn.close()
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Consultar 3 eventos com mais inscrições
def eventoMaisInscricoes():
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT * FROM topEventos")
        eventos = []
        eventos_tuples = cur.fetchall()
        for evento_tuple in eventos_tuples:
            evento = {
                "idEvento": evento_tuple[0],
                "nomeEvento": evento_tuple[1],
                "totalInscricoes": evento_tuple[2],
            }
            eventos.append(evento)
        cur.close()
        conn.close()
        return eventos
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Comprar bilhete de evento
def comprarEntrada(idEvento, idUtilizador, preco):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT * FROM eventos WHERE idevento = %s", [idEvento])
        evento = cur.fetchone()
        if evento is None:
            return None
        cur.execute("SELECT * FROM inscricoes WHERE utilizadores_idutilizador = %s AND eventos_idevento = %s AND inscricaoAtiva = %s",
                    [idUtilizador, idEvento, True])
        inscricao = cur.fetchone()
        if inscricao:
            return None
        cur.execute("select getNumeroLugares(%s)", (idEvento,))
        lugaresOcupados = cur.fetchone()[0]
        cur.execute(
            "SELECT lugares FROM eventos WHERE idevento = %s", [idEvento])
        lotacaoEvento = cur.fetchone()[0]
        if lugaresOcupados == lotacaoEvento:
            return None
        cur.execute("CALL inscricao_evento(%s,%s,%s)",
                    (idUtilizador, idEvento, preco))
        conn.commit()
        cur.close()
        conn.close()
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Retornar Saldo Utilizador
def saldoUtilizador(aIdUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("select getSaldo(%s)", (aIdUtilizador,))
        saldo = cur.fetchone()
        if saldo is None:
            return None
        cur.close()
        conn.close()
        return saldo[0]
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Retornar Preço Evento
def precoAtualEvento(aIdEvento):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("select getPrecoEntrada(%s)", (aIdEvento,))
        preco = cur.fetchone()
        if preco is None:
            return None
        cur.close()
        conn.close()
        return preco[0]
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Ativar / Inativar Utilizador (Banir)
def ativarDesativarUtilizador(idUtilizador, idBanir):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT tipoutilizador FROM utilizadores WHERE idUtilizador = %s", [
                    idUtilizador])
        tipo = cur.fetchone()[0]
        if tipo is None:
            return None
        if tipo is True:
            cur.execute(
                "SELECT ativo FROM utilizadores WHERE idutilizador = %s", [idBanir])
            ativoTeste = cur.fetchone()[0]
            cur.execute("CALL ativaDesativaUtilizador(%s,%s)",
                        (ativoTeste, idBanir,))
            conn.commit()
            cur.close()
            conn.close()
            return True
        else:
            return False
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Cancelar Evento
def cancelarEventoAtivo(idUtilizador, idEvento):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute(
            "SELECT * FROM eventos, utilizadores_eventos WHERE idevento = eventos_idevento AND idevento = %s AND utilizadores_idutilizador = %s", [idEvento, idUtilizador])
        evento = cur.fetchone()[0]
        if evento is None:
            return None
        cur.execute("select cancelaEventoId(%s)", (idEvento,))
        conn.commit()
        cur.close()
        conn.close()
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)


# Ver as minhas notificações
def verNotificacoes(idUtilizador):
    try:
        conn = ligacao()
        cur = conn.cursor()
        cur.execute("SELECT DISTINCT idn, idE, nomeE, mensagemN, dataN FROM eventosNotificacoes WHERE idE IN (SELECT eventos_idevento FROM inscricoes WHERE utilizadores_idutilizador = %s) AND ativaN = %s AND datan >= datai", [
                    idUtilizador, True])
        notificacoes = []
        notificacoes_tuples = cur.fetchall()
        for notificacao_tuple in notificacoes_tuples:
            notificacao = {
                "idNotificacao": notificacao_tuple[0],
                "nomeEvento": notificacao_tuple[1],
                "Mensagem": notificacao_tuple[2],
                "dataNotificacao": notificacao_tuple[3],
            }
            notificacoes.append(notificacao)
        cur.close()
        conn.close()
        return notificacoes
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL", error)
