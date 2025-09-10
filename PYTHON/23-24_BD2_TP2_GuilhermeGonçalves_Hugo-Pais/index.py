import os
from dotenv import load_dotenv
from datetime import datetime, timezone, timedelta
from functools import wraps
import jwt
from jwt import encode
from flask import Flask, jsonify, request
import bd

load_dotenv()

app = Flask(__name__)
app.debug = True

app.config['SECRET_KEY'] = os.getenv("SECRET_KEY")

tokens_revogados = []

NOT_FOUND_CODE = 401
OK_CODE = 200
SUCCESS_CODE = 201
NO_CONTENT_CODE = 204
BAD_REQUEST_CODE = 400
UNAUTHORIZED_CODE = 401
FORBIDDEN_CODE = 403
NOT_FOUND = 404
SERVER_ERROR = 500


# ROTA REGISTO [CONCLUIDO]
@app.route("/registar", methods=['POST'])
def registar():
    dados = request.get_json()
    if "username" not in dados or "password" not in dados or "tipoutilizador" not in dados:
        return jsonify({"error": "Parâmetros inválidos"}), BAD_REQUEST_CODE
    if (bd.verificaUtilizador(dados['username'])):
        return jsonify({"error": "Utilizador já existe"}), BAD_REQUEST_CODE
    utilizador = bd.adicionarUtilizador(dados)
    return jsonify(utilizador), SUCCESS_CODE


# ROTA AUTENTICAÇÃO [CONCLUIDO]
@app.route("/autenticar", methods=['GET'])
def autenticar():
    dados = request.get_json()
    if "username" not in dados or "password" not in dados:
        return jsonify({"error": "Parâmetros inválidos"}), BAD_REQUEST_CODE
    utilizador = bd.autenticar(dados['username'], dados["password"])
    if utilizador is None:
        return jsonify({"error": "Utilizador inexistente"}), NOT_FOUND_CODE
    token_exp = (datetime.now(timezone.utc) + timedelta(hours=1))
    token = jwt.encode(
        {'idutilizador': utilizador['idutilizador'], 'exp': token_exp}, app.config['SECRET_KEY'], 'HS256')
    utilizador["token"] = token
    return jsonify(utilizador), OK_CODE


# AUTENTICAÇÃO NECESSÁRIA [CONCLUÍDO]
def auth_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        if "Authorization" not in request.headers:
            return jsonify({"error": "Token não fornecido"}), FORBIDDEN_CODE
        token = request.headers['Authorization']
        if token in tokens_revogados:
            return jsonify({"error": "Token inválido"}), FORBIDDEN_CODE
        try:
            dados = jwt.decode(
                token, app.config['SECRET_KEY'], algorithms=["HS256"])
        except jwt.ExpiredSignatureError:
            return jsonify({"error": "Token expirado", "exp": True}), UNAUTHORIZED_CODE
        except jwt.InvalidTokenError:
            return jsonify({"error": "Token inválido"}), FORBIDDEN_CODE
        request.user = bd.listarUtilizador(dados['idutilizador'])
        kwargs['idutilizador'] = dados['idutilizador']
        return f(*args, **kwargs)
    return decorated


# ROTA LISTAR TODOS OS EVENTOS [CONCLUÍDO]
@app.route('/listarEventos', methods=['GET'])
@auth_required
def listarEventos(idutilizador):
    eventos = bd.listarEventos()
    return jsonify(eventos), OK_CODE


# ROTA CONSULTAR SALDO [CONCLUÍDO]
@app.route('/consultarSaldo', methods=['GET'])
@auth_required
def verSaldo(idutilizador):
    saldo = bd.consultarSaldo(idutilizador)
    return jsonify(saldo), OK_CODE


# ROTA PESQUISAR EVENTO (NOME / TIPO / DATA) [CONCLUÍDO]
@app.route('/pesquisarEventos', methods=['GET'])
@auth_required
def pesquisarEventos(idutilizador):
    dados = request.get_json()
    if "tipo" in dados:
        eventos = bd.pesquisarEventos(dados['tipo'], 1)
    elif "nome" in dados:
        eventos = bd.pesquisarEventos(dados['nome'], 2)
    elif "datainicio" in dados:
        eventos = bd.pesquisarEventos(dados['datainicio'], 3)
    else:
        return jsonify({"error": "Informe o nome, tipo ou data do evento"}), BAD_REQUEST_CODE
    return jsonify(eventos), OK_CODE


# ROTA LISTAR EVENTOS DO UTILIZADOR [CONCLUÍDO]
@app.route('/listarEventosUtilizador', methods=['GET'])
@auth_required
def listarEventosUtilizador(idutilizador):
    eventos = bd.listarEventosUtilizador(idutilizador)
    return jsonify(eventos), OK_CODE


# ROTA ALTERAR SALDO UTILIZADOR [CONCLUÍDO]
@app.route('/alterarSaldo', methods=['PUT'])
@auth_required
def alterarSaldo(idutilizador):
    dados = request.get_json()
    if "valor" not in dados:
        return jsonify({"error": "Parâmetros inválidos"}), BAD_REQUEST_CODE
    bd.carregarSaldo(idutilizador, dados['valor'])
    return jsonify({"message": "Saldo carregado com sucesso"}), OK_CODE


# ROTA CRIAR EVENTO [CONCLUÍDO]
@app.route('/criarEvento', methods=['POST'])
@auth_required
def criarEvento(idutilizador):
    dados = request.get_json()
    if "nome" not in dados or "tipo" not in dados or "localevento" not in dados or "datainicio" not in dados or "datafim" not in dados or "datainscricaoinicio" not in dados or "datainscricaofim" not in dados or "lugares" not in dados or "precoentrada" not in dados:
        return jsonify({"Erro": "Parâmetros inválidos"}), BAD_REQUEST_CODE
    evento = bd.criarEvento(dados, idutilizador)
    return jsonify(evento), SUCCESS_CODE


# ROTA ALTERAR EVENTO [CONCLUÍDO]
@app.route('/evento/<int:idevento>', methods=['POST'])
@auth_required
def alterarEvento(idutilizador, idevento):
    dados = request.get_json()
    if "nome" not in dados or "tipo" not in dados or "localevento" not in dados or "datainicio" not in dados or "datafim" not in dados or "datainscricaoinicio" not in dados or "datainscricaofim" not in dados or "lugares" not in dados or "precoentrada" not in dados:
        return jsonify({"Erro": "Parâmetros inválidos"}), BAD_REQUEST_CODE
    evento = bd.alterarEvento(idevento, dados, idutilizador)
    if evento is None:
        return jsonify({"Erro": "Evento não encontrado"}), NOT_FOUND
    return jsonify(evento), SUCCESS_CODE


# ROTA CONSULTAR DETALHES EVENTO [CONCLUÍDO]
@app.route('/evento/<int:idevento>', methods=['GET'])
@auth_required
def consultarDetalhesEvento(idutilizador, idevento):
    evento = bd.consultarDetalhesEvento(idevento)
    if evento is None:
        return jsonify({"Erro": "Evento não encontrado"}), NOT_FOUND
    return jsonify(evento), OK_CODE


# ROTA LOGOUT DA APLICAÇÃO [CONCLUÍDO]
@app.route('/logout', methods=['GET'])
@auth_required
def logout(idutilizador):
    token = request.headers['Authorization']
    tokens_revogados.append(token)
    return jsonify({"message": "Logout efetuado com sucesso"}), OK_CODE


# ROTA INSERIR COMENTÁRIO [CONCLUÍDO]
@app.route('/comentarEvento/<int:idevento>', methods=['POST'])
@auth_required
def comentarEvento(idutilizador, idevento):
    if (bd.inscricaoValida(idutilizador, idevento)):
        dados = request.get_json()
        if "comentario" not in dados:
            return jsonify({"Erro": "Parâmetros inválidos"}), BAD_REQUEST_CODE
        comentario = bd.inserirComentario(
            idevento, idutilizador, dados['comentario'])
        if comentario is None:
            return jsonify({"Erro": "Evento não encontrado"}), NOT_FOUND
        return jsonify({"Mensagem": "Comentário efetuado com sucesso!"}), SUCCESS_CODE
    else:
        return jsonify({"Erro": "Inscrição inválida"}), NOT_FOUND


# ROTA CONSULTAR COMENTÁRIOS NUM EVENTO [CONCLUÍDO]
@app.route('/comentariosEvento/<int:idevento>', methods=['GET'])
@auth_required
def consultarComentariosEvento(idutilizador, idevento):
    comentarios = bd.consultarComentarios(idevento, idutilizador)
    if comentarios is None:
        return jsonify({"Erro": "Evento não encontrado"}), NOT_FOUND
    return jsonify(comentarios), OK_CODE


# ROTA SUBSCREVER COMENTÁRIOS DE UM EVENTO [CONCLUÍDO]
@app.route('/subscrevercomentariosevento/<int:idevento>', methods=['PUT'])
@auth_required
def subscreverComentariosEvento(idutilizador, idevento):
    subscricaocomentario = bd.subscreverComentario(idevento, idutilizador)
    if subscricaocomentario is False:
        return jsonify({"Erro": "Evento não encontrado"}), NOT_FOUND
    if subscricaocomentario is None:
        return jsonify({"Erro": "Comentários de evento não foram subscritos ou anulados"}), NOT_FOUND
    return jsonify({"Mensagem": "Estado da subscrição alterado com sucesso!"}), SUCCESS_CODE


# ROTA CONSULTAR EVENTOS COM MAIS INSCRIÇÕES [CONCLUÍDO]
@app.route('/eventoscommaisinscricoes', methods=['GET'])
@auth_required
def maisinscricoes(idutilizador):
    eventos = bd.eventoMaisInscricoes()
    if eventos is None:
        return jsonify({"Erro": "Evento não encontrado"}), NOT_FOUND
    return jsonify(eventos), OK_CODE


# ROTA COMPRAR ENTRADA EM EVENTO [CONCLUÍDO]
@app.route('/comprarEntrada/<int:idevento>', methods=['POST'])
@auth_required
def comprarEntradaEvento(idutilizador, idevento):
    saldoAtual = bd.saldoUtilizador(idutilizador)
    if saldoAtual is None:
        return jsonify({"Erro": "Saldo não encontrado"}), NOT_FOUND
    precoEvento = bd.precoAtualEvento(idevento)
    if precoEvento is None:
        return jsonify({"Erro": "Evento não encontrado"}), NOT_FOUND
    if (saldoAtual < precoEvento):
        return jsonify({"Erro": "Saldo insuficiente"}), NOT_FOUND
    else:
        entrada = bd.comprarEntrada(idevento, idutilizador, precoEvento)
        if entrada is None:
            return jsonify({"Erro": "Erro ao comprar entrada"}), NOT_FOUND
    return jsonify({"Mensagem": "Bilhete comprado com sucesso!"}), SUCCESS_CODE


# ROTA ATIVAR / INATIVAR UTILIZADOR (BANIR) [CONCLUÍDO]
@app.route('/ativarDesativarUtilizador', methods=['PUT'])
@auth_required
def ativarDesativarUtilizador(idutilizador):
    dados = request.get_json()
    if "idbanir" not in dados:
        return jsonify({"Erro": "Parâmetros inválidos"}), BAD_REQUEST_CODE
    ativo = bd.ativarDesativarUtilizador(idutilizador, dados['idbanir'])
    if ativo is None:
        return jsonify({"Erro": "Utilizador não encontrado"}), NOT_FOUND
    if ativo is False:
        return jsonify({"Erro": "Utilizador sem permissões"}), FORBIDDEN_CODE
    return jsonify({"Mensagem": "Estado de utilizador alterado com sucesso!"}), SUCCESS_CODE


# ROTA VER NOTIFICAÇÕES DO UTILIZADOR
@app.route('/verNotificacoes', methods=['GET'])
@auth_required
def verNotificacoes(idutilizador):
    notificacoes = bd.verNotificacoes(idutilizador)
    if notificacoes is None:
        return jsonify({"Erro": "Utilizador não encontrado"}), NOT_FOUND
    return jsonify(notificacoes), OK_CODE


# ROTA CANCELAR EVENTO
@app.route('/cancelarEvento', methods=['PUT'])
@auth_required
def cancelarEvento(idutilizador):
    dados = request.get_json()
    if "idEvento" not in dados:
        return jsonify({"Erro": "Parâmetros inválidos"}), BAD_REQUEST_CODE
    cancelar = bd.cancelarEventoAtivo(idutilizador, dados['idEvento'])
    if cancelar is None:
        return jsonify({"Erro": "Evento não encontrado / Sem permissões"}), NOT_FOUND
    return jsonify({"Mensagem": "Evento cancelado com sucesso!"}), SUCCESS_CODE


if __name__ == "__main__":
    app.run()
