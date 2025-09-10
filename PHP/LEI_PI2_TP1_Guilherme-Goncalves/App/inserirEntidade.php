<?php
session_start();

if ((!isset($_SESSION['usernameLogin']))) {
    unset($_SESSION['usernameLogin']);
    unset($_SESSION['passwordLogin']);
    header('Location: formularioLogin.php');
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TooGoodToGo</title>
    <link rel="stylesheet" href="menu.css">
</head>
<style>
        #novaEntidade {
            background-color: rgb(82,104,118);
        }
    </style>
<body>

    <header>
        <a href="listaEntidades.php">
            <div class="logo">
                <img src="logoTooGoodToGo.png" alt="tooGoodToGoLogo" width=60 height=60>
                <h1>TooGoodToGo</h1>
            </div>
        </a>
        <div class="logout">
            <a href="logout.php" id="sair">Sair</a>
        </div>
    </header>

    <div class="menu">
        <nav class="barra_lateral">
            <ul>
                <li><a href="listaEntidades.php">Entidades Disponíveis</a></li>
                <li id="novaEntidade"><a href="inserirEntidade.php">Inserir Nova Entidade</a></li>
                <li><a href="alterarPassword.php">Alterar Password</a></li>
            </ul>
        </nav>

        <?php

        include_once "conexao.php"; 
        include_once "entidade.php";
        include_once "gereEntidade.php";


        if (isset($_POST["submeter"])) {

            $nome = $_POST["nome"];
            $descricao = $_POST["descricao"];
            $morada = $_POST["morada"];
            $telefone = $_POST["telefone"];
            $email = $_POST["email"];
            $password = $_POST["password"];

            $ext = [
                'image/jpeg' => 'jpg',
                'image/png'  => 'png',
            ];

            $novoLogoNome = md5($_FILES["logo"]["name"]). '.' . $ext[$_FILES["logo"]["type"]];
            $tname = $_FILES["logo"]["tmp_name"];
            $pastaImagens = "./imagens";

            $passwordHash = password_hash($password, PASSWORD_DEFAULT);

            $nova_entidade = new Entidade($nome, $descricao, $novoLogoNome, $morada, $telefone, $email, $passwordHash);

            if ($nova_entidade->verificarEmailExiste($DBH)) {
                $mensagemErro = "Erro: O email já se encontra registado.";
            } 
            else {
                move_uploaded_file($tname, $pastaImagens.'/'.$novoLogoNome);

                $gerirEntidade = new gereEntidade();
                $gerirEntidade->inserirEntidade($nova_entidade, $DBH);

                $mensagem = "Entidade adicionada com sucesso";
            }
        }
        ?>
        
        <div class="formulario">
            <h1>Inserir Nova Entidade</h1>
            <form class="form" action="" method="post" enctype="multipart/form-data">

                <?= !empty($mensagemErro) ? '<div class="mensagemErro">' . $mensagemErro . '</div>' : '' ?>
                <?= !empty($mensagem) ? '<div class="mensagem">' . $mensagem . '</div>' : '' ?>
                
                <input type="text" class="dados_registo_login" name="nome" placeholder="Nome" required>
                <input type="text" class="dados_registo_login" name="descricao" placeholder="Descrição" required />
                <input type="text" class="dados_registo_login" name="morada" placeholder="Morada" required />
                <input type="text" class="dados_registo_login" name="telefone" placeholder="Número de Telefone" required />
                <input type="email" class="dados_registo_login" name="email" placeholder="Email" required />
                <input type="password" class="dados_registo_login" name="password" placeholder="Palavra-Passe" required />
                <input type="file" class="dados_registo_login" name="logo" required>
                <input type="submit" name="submeter" value="Inserir" class="botao_registo_login">
            </form>
        </div>  
    </div>

</body>
</html>