<?php

class gereOferta {

    public function inserirOferta($nova_oferta, $DBH) {
        $STH = $DBH->prepare("INSERT INTO oferta (entidade_id, nome, descricao, foto, preço, data, disponivel) VALUES (:idEntidade, :nome, :descricao, :foto, :preco, :data, :disponivel)");
        $STH->execute($nova_oferta->toArray());
    }

    public function detalhesOferta($id, $DBH) {
        $STH = $DBH->prepare("SELECT * FROM oferta WHERE id = ?");
        $STH->execute([$id]);
        $oferta = $STH->fetch(PDO::FETCH_ASSOC);
        return $oferta;
    }

    public function editarDetalhesOferta($novosDetalhes,$DBH) {
        $STH = $DBH->prepare("UPDATE oferta SET nome = :nome, descricao = :descricao, foto = :foto, preço =:preco WHERE id = :id");
        $STH->execute($novosDetalhes);
    }

    public function removerOferta($id, $DBH) {
        $disponivel = 0;

        $STH = $DBH->prepare("UPDATE oferta SET disponivel = ? WHERE id = ?");
        $STH->execute([$disponivel, $id]);
    }

}

?>
