<?php

declare(strict_types=1);

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\App;

use Psr\Log\LoggerInterface;

use Slim\Interfaces\RouteCollectorProxyInterface as Group;

return function (App $app) {

    $app->options('/{routes:.*}', function (Request $request, Response $response) {
        // CORS Pre-Flight OPTIONS Request Handler
        return $response;
    });

    # REQUISITO 1 - Registar Novo Cliente    
    $app->post('/api/users/', function (Request $request, Response $response, $params) {
        $params = (array) $request->getParsedBody();
        $nome = $params['nome'];
        $email = $params['email'];
        $telefone = $params['telefone'];
        $password = $params['password'];
        $passwordHash = password_hash($password, PASSWORD_DEFAULT);
        $res = [];
        $dbh = $this->get(PDO::class);

        if (empty(empty($params['nome']) || empty($params['email']) || $params['telefone']) || empty($params['password'])) {
            $res["res"] = "Erro";
            $res["message"] = "Campos obrigatorios ausentes";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }

        #Verifica se existe algum utilizador com o email
        $STH = $dbh->prepare("SELECT * FROM cliente WHERE email = ?");
        $STH->execute([$email]);
        $entidade = $STH->fetchColumn();

        if ($entidade) {
            $res["res"] = "Erro";
            $res["message"] = "Email ja se encontra em uso. Tente outro.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
        else {
            $sth = $dbh->prepare("INSERT INTO cliente (nome, email, telefone, password, ativo) VALUES (?, ?, ?, ?, 1)");
            $sth->execute([$nome, $email, $telefone, $passwordHash]);
            
            // Verifica se a inserção foi bem-sucedida
            if ($sth->rowCount() > 0) {
                $res["res"] = "OK";
                $res["message"] = "Cliente inserido com sucesso";
                $payload = json_encode($res);
                $response->getBody()->write($payload);
                return $response->withHeader('Content-Type', 'application/json');
            } 
            else {
                $res["res"] = "Erro";
                $res["message"] = "Falha a inserir o cliente";
                $payload = json_encode($res);
                $response->getBody()->write($payload);
                return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
            }
        }
    })->setArgument('auth', 'false');

    # REQUISITO 2 - Autenticação dos clientes
    $app->patch('/api/users/', function (Request $request, Response $response, $params) { 
        $params = (array) $request->getParsedBody();
        $email = $params['email'];
        $password = $params['password'];
        $res = [];
        $dbh = $this->get(PDO::class);
    
        // Verifica se o email do cliente já existe e se está ativo
        $existingUser = $dbh->prepare("SELECT * FROM cliente WHERE email = ? AND ativo = 1");
        $existingUser->execute([$email]);
        $cliente = $existingUser->fetch(PDO::FETCH_ASSOC);
    
        if ($cliente) {
            if(password_verify($password, $cliente['password'])){
                $token = md5($email);
                $idCliente = $cliente['id'];

                $sth = $dbh->prepare("UPDATE cliente SET token = ? WHERE id = ?");
                $sth->execute([$token, $idCliente]);
                
                $res["res"] = "OK";
                $res["message"] = "Cliente autenticado com sucesso";
                $res["token"] = $token;
                $payload = json_encode($res);
                $response->getBody()->write($payload);
                return $response->withHeader('Content-Type', 'application/json');
            } 
            else{
                $res["res"] = "Error";
                $res["message"] = "Dados de autenticacao incorretos.";
                $payload = json_encode($res);
                $response->getBody()->write($payload);
                return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
            }
        } 
        else{
            $res["res"] = "Error";
            $res["message"] = "Dados de acesso invalidos.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }   
    })->setArgument('auth', 'false');

    # REQUISITO 3 - Terminar Sessão de um cliente
    $app->patch('/api/users/logout/', function (Request $request, Response $response, $params) { 
        
        $res = [];

        if ($request->hasHeader('Authorization')) {
            $headers = $request->getHeader('Authorization');
            $tokenAuth = $headers[0];

            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);
            $sth = $dbh->prepare("UPDATE cliente SET token = NULL WHERE token = ?");
            $sth->execute([$tokenAuth]);
            $data = $sth->fetchAll(PDO::FETCH_ASSOC);

            $res["res"] = "OK";
            $res["message"] = "Sessao terminada com sucesso";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }          
    })->setArgument('auth', 'true');

    # REQUISITO 4 - Obter lista das entidades ativas
    $app->get('/api/entidades/', function (Request $request, Response $response, $params){
        
        $res = [];

        if ($request->hasHeader('Authorization')) {
            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);
            $sth = $dbh->prepare("SELECT id, nome, logotipo, descricao, morada, telefone, email FROM entidade WHERE ativo = 1");
            $sth->execute([]);
            $data = $sth->fetchAll(PDO::FETCH_ASSOC);
            $logger->info("Devolvida a lista das entidades");
            $res["res"] = "OK";
            $res["data"] = $data;
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
       
    })->setArgument('auth', 'true');

    # REQUISITO 5 - Obter lista das entidades ativas com ofertas para o dia
    $app->get('/api/entidades/comOfertas/', function (Request $request, Response $response, $params){
        $res = [];

        if ($request->hasHeader('Authorization')) {
            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);

            $dataHoje = date("Y-m-d"); 

            $sth = $dbh->prepare("SELECT e.id, e.nome, logotipo, e.descricao, morada, telefone, email 
        FROM entidade e, oferta WHERE e.id = entidade_id AND ativo = 1 AND disponivel = 1 AND data = ?");
            $sth->execute([$dataHoje]);
            $data = $sth->fetchAll(PDO::FETCH_ASSOC);
            $logger->info("Devolvida a lista das entidades com ofertas para o dia");
            $res["res"] = "OK";
            $res["data"] = $data;
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
    })->setArgument('auth', 'true');

    # REQUISITO 8 - Obter lista de todas as ofertas ainda disponíveis para aquele dia
    $app->get('/api/entidades/ofertas/', function (Request $request, Response $response, $params){
        $res = [];

        if ($request->hasHeader('Authorization')) {
            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);

            $dataHoje = date("Y-m-d"); 

            $sth = $dbh->prepare("SELECT o.id, o.entidade_id, o.nome AS Nome_Oferta, o.descricao AS Descricao_Oferta, foto, preço, 
            e.nome AS Nome_Entidade, e.descricao AS Descricao_Entidade, logotipo, morada, telefone, email, data
            FROM oferta o, entidade e  WHERE e.id = entidade_id AND ativo = 1 AND disponivel = 1 AND data = ?");

            $sth->execute([$dataHoje]);
            $data = $sth->fetchAll(PDO::FETCH_ASSOC);
            $logger->info("Lista de todas as ofertas disponiveis");
            $res["res"] = "OK";
            $res["data"] = $data;
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
    })->setArgument('auth', 'true');

    # REQUISITO 9 - Registar a compra de uma oferta
    $app->post('/api/entidades/ofertas/comprar', function (Request $request, Response $response, $params){

        $res = [];

        if ($request->hasHeader('Authorization')) {
            $params = (array) $request->getParsedBody();
            $headers = $request->getHeader('Authorization');
            $tokenAuth = $headers[0];
            $id = $params['id_oferta'];

            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);

            $uid = $dbh->prepare("SELECT * FROM cliente WHERE token = ? AND ativo = 1");
            $uid->execute([$tokenAuth]);
            $cliente = $uid->fetch(PDO::FETCH_ASSOC);

            if ($cliente) {

                $dataHoje = date("Y-m-d"); 

                $sth = $dbh->prepare("SELECT * FROM oferta WHERE id = ? AND disponivel = 1 AND data = ?");
                $sth->execute([$id, $dataHoje]);
                $oferta = $sth->fetch(PDO::FETCH_ASSOC);
    
                if($oferta) {
                    
                    $sth = $dbh->prepare("INSERT INTO compra (oferta_id, cliente_id, pago, levantado, ativo) VALUES (?, ?, 1, 1, 1)");
                    $sth->execute([$id, $cliente['id']]);

                    $ofertaDisponibilidade = $dbh->prepare("UPDATE oferta SET disponivel = 0 WHERE id = ?");
                    $ofertaDisponibilidade->execute([$id]);
    
                    $res["res"] = "OK";
                    $res["message"] = "Compra realizada com sucesso!";
                    $payload = json_encode($res);
                    $response->getBody()->write($payload);
                    return $response->withHeader('Content-Type', 'application/json');
                }
                else{
                    $res["res"] = "Error";
                    $res["message"] = "Oferta nao encontrada.";
                    $payload = json_encode($res);
                    $response->getBody()->write($payload);
                    return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
                }
            }
            else {
                $res["res"] = "Error";
                $res["message"] = "Utilizador não encontrado.";
                $payload = json_encode($res);
                $response->getBody()->write($payload);
                return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
            } 
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
    })->setArgument('auth', 'true');

    # REQUISITO 10 - Obter lista compras efetuadas pelo utilizador
    $app->get('/api/users/compras/', function (Request $request, Response $response, $params){
        $res = [];

        if ($request->hasHeader('Authorization')) {
            $params = (array) $request->getParsedBody();
            $headers = $request->getHeader('Authorization');
            $tokenAuth = $headers[0];

            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);

            $uid = $dbh->prepare("SELECT id FROM cliente WHERE token = ?");
            $uid->execute([$tokenAuth]);
            $dados = $uid->fetch(PDO::FETCH_ASSOC);
            $idCliente = $dados['id'];

            $sth = $dbh->prepare("SELECT id_compra, o.id AS id_oferta, o.entidade_id, o.nome AS Nome_Oferta, o.descricao AS Descricao_Oferta, foto, preço, 
            e.nome AS Nome_Entidade, e.descricao AS Descricao_Entidade, logotipo, morada, telefone, email, data 
        FROM compra c, oferta o, entidade e WHERE e.id = entidade_id AND oferta_id = o.id AND cliente_id = ? AND c.ativo = 1 ORDER BY Nome_Oferta desc ");
            $sth->execute([$idCliente]);
            $data = $sth->fetchAll(PDO::FETCH_ASSOC);

            $logger->info("Lista de todas as compras efetuadas");
            $res["res"] = "OK";
            $res["data"] = $data;
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
    })->setArgument('auth', 'true');

    # REQUISITO 11 - Cancelar uma compra
    $app->patch('/api/users/compras/', function (Request $request, Response $response, $params){
        $res = [];

        if ($request->hasHeader('Authorization')) {
            $params = (array) $request->getParsedBody();
            $headers = $request->getHeader('Authorization');
            $tokenAuth = $headers[0];
            $id_compra = $params['id_compra'];

            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);

            $dataHoje = date("Y-m-d"); 

            $sth = $dbh->prepare("SELECT * FROM oferta o, compra co, cliente c WHERE oferta_id = o.id AND cliente_id = c.id 
        AND id_compra = ? AND token = ? AND co.ativo = 1 AND data = ?");
            $sth->execute([$id_compra, $tokenAuth, $dataHoje]);
            $oferta = $sth->fetch(PDO::FETCH_ASSOC);

            if($oferta) {
                $compra = $dbh->prepare("UPDATE compra SET pago = 0, levantado = 0, ativo = 0 WHERE id_compra = ?");
                $compra->execute([$id_compra]);

                $updateOferta = $dbh->prepare("UPDATE oferta SET disponivel = 1 WHERE id = ?");
                $updateOferta->execute([$oferta['oferta_id']]);

                $res["res"] = "OK";
                $res["message"] = "Compra cancelada com sucesso!";
                $payload = json_encode($res);
                $response->getBody()->write($payload);
                return $response->withHeader('Content-Type', 'application/json');
            }
            else{
                $res["res"] = "Error";
                $res["message"] = "Compra nao encontrada.";
                $payload = json_encode($res);
                $response->getBody()->write($payload);
                return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
            }
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
    })->setArgument('auth', 'true');

    # REQUISITO 6 - Obter detalhes de uma entidade
    $app->get('/api/entidades/{id_entidade}/', function (Request $request, Response $response, $params){

        $res = [];

        if ($request->hasHeader('Authorization')) {

            $idEntidade = $params['id_entidade'];

            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);
            $sth = $dbh->prepare("SELECT id, nome, logotipo, descricao, morada, telefone, email FROM entidade WHERE id = ?");
            $sth->execute([$idEntidade]);
            $data = $sth->fetchAll(PDO::FETCH_ASSOC);
            $logger->info("Detalhes da entidade");
            $res["res"] = "OK";
            $res["data"] = $data;
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
    })->setArgument('auth', 'true');

    # REQUISITO 7 - Obter lista de ofertas disponiveis de uma entidade para aquele dia
    $app->get('/api/entidades/ofertas/{id_entidade}/', function (Request $request, Response $response, $params){

        $res = [];

        if ($request->hasHeader('Authorization')) {

            $idEntidade = $params['id_entidade'];

            $dbh = $this->get(PDO::class); // A ligação à base de dados
            $logger = $this->get(LoggerInterface::class);

            $dataHoje = date("Y-m-d"); 

            $sth = $dbh->prepare("SELECT * FROM oferta WHERE entidade_id = ? AND disponivel = 1 AND data = ?");
            $sth->execute([$idEntidade, $dataHoje]);
            $data = $sth->fetchAll(PDO::FETCH_ASSOC);
            $logger->info("Ofertas da entidade disponíveis para hoje");
            $res["res"] = "OK";
            $res["data"] = $data;
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json');
        }
        else {
            $res["res"] = "Error";
            $res["message"] = "Nao tem autorizacao.";
            $payload = json_encode($res);
            $response->getBody()->write($payload);
            return $response->withHeader('Content-Type', 'application/json')->withStatus(401);
        }
    })->setArgument('auth', 'true');

    # Para devolver um 404 caso se tente aceder a um endpoint não existente
    $app->map(['GET', 'POST', 'PUT', 'DELETE', 'PATCH'], '/{routes:.+}', function($request, $response, $args) {
        return $response->withStatus(404);
    });
};
