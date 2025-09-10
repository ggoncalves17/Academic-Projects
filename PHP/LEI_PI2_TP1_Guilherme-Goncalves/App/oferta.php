<?php

class oferta {

    private $idEntidade;
    private $nome;
    private $descricao;
    private $foto;
    private $preco;
    private $data;
    private $disponivel;

    public function __construct($idEntidade, $nome, $descricao,$foto, $preco, $data, $disponivel = 1) {
        $this->idEntidade = $idEntidade;
        $this->nome = $nome;
        $this->descricao = $descricao;
        $this->foto = $foto;
        $this->preco = $preco;
        $this->data = $data;
        $this->disponivel = $disponivel;
    }

    public function toArray() {
        return get_object_vars($this);
    }
}

?>
