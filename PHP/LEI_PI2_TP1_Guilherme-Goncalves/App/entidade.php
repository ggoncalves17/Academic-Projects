<?php

class entidade {

    private $nome;
    private $descricao;
    private $logo;
    private $morada;
    private $telefone;
    private $email;
    private $password;
    private $ativo;

    public function __construct($nome, $descricao, $logo, $morada, $telefone, $email, $password, $ativo = 1) {
        $this->nome = $nome;
        $this->descricao = $descricao;
        $this->logo = $logo;
        $this->morada = $morada;
        $this->telefone = $telefone;
        $this->email = $email;
        $this->password = $password;
        $this->ativo = 1;
    }

    public function toArray() {
        return get_object_vars($this);
    }

    public function verificarEmailExiste($DBH) {
        $STH = $DBH->prepare("SELECT * FROM entidade WHERE email = :email");
        $STH->execute([':email' => $this->email]);
        $entidade = $STH->fetchColumn();
        return $entidade;
    }
}

?>
