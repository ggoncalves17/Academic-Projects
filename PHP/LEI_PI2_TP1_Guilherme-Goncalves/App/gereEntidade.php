<?php

class gereEntidade {

    public function inserirEntidade($nova_entidade, $DBH) {
        $STH = $DBH->prepare("INSERT INTO entidade (nome, descricao, logotipo, morada, telefone, email, password, ativo) VALUES (:nome, :descricao, :logo, :morada, :telefone, :email, :password, :ativo)");
        $STH->execute($nova_entidade->toArray());
    }

    public function detalhesEntidade($id, $DBH) {
        $STH = $DBH->prepare("SELECT * FROM entidade WHERE id = ?");
        $STH->execute([$id]);
        $entidade = $STH->fetch(PDO::FETCH_ASSOC);
        return $entidade;
    }

    public function editarDetalhesEntidade($novosDetalhes,$DBH) {
        $STH = $DBH->prepare("UPDATE entidade SET nome = :nome, descricao = :descricao, logotipo =:logotipo, morada = :morada, telefone = :telefone, password = :password WHERE id = :id");
        $STH->execute($novosDetalhes);
    }

    public function alterarEstadoEntidade($id, $estadoEntidade,$DBH) {

        if ($estadoEntidade == 1) {
            $estadoEntidade = 0;
        }
        else {
            $estadoEntidade = 1;
        }

        $STH = $DBH->prepare("UPDATE entidade SET ativo = ? WHERE id = ?");
        $STH->execute([$estadoEntidade, $id]);
    }
}

?>
