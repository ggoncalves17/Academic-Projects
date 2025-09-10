<?php
session_start();

if ((!isset($_SESSION['email']))) {
    unset($_SESSION['email']);
    unset($_SESSION['passwordLogin']);
    header('Location: loginEntidade.php');
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
        #oferta {
            background-color: rgb(82,104,118);
        }
    </style>
<body>

    <header>
        <a href="listaOfertas.php">
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
                <li><a href="listaOfertas.php">Ofertas Disponíveis</a></li>
                <li id="oferta"><a href="inserirOferta.php">Inserir Nova Oferta</a></li>
            </ul>
        </nav>

        <?php

        include_once "conexao.php"; 
        include_once "oferta.php";
        include_once "gereOferta.php";

        if (isset($_POST["submeter"])) {

            $emailEntidade = $_SESSION['email'];
            $STH = $DBH->prepare("SELECT id FROM entidade WHERE email = ?");
            $STH->execute([$emailEntidade]);
            $entidade = $STH->fetch(PDO::FETCH_ASSOC);
            $idEntidade = $entidade['id'];

            $nome = $_POST["nome"];
            $descricao = $_POST["descricao"];
            $preco = $_POST["preco"];
            $data = date("Y-m-d");

            $ext = [
                'image/jpeg' => 'jpg',
                'image/png'  => 'png',
            ];

            $novaFotoNome = md5($_FILES["foto"]["name"]). '.' . $ext[$_FILES["foto"]["type"]];
            $tname = $_FILES["foto"]["tmp_name"];
            $pastaImagens = "./imagens";

            $nova_oferta = new Oferta($idEntidade, $nome, $descricao, $novaFotoNome, $preco, $data);

            move_uploaded_file($tname, $pastaImagens.'/'.$novaFotoNome);

            $gerirOferta = new gereOferta();
            $gerirOferta->inserirOferta($nova_oferta, $DBH);
    
            $mensagem = "Oferta adicionada com sucesso";
        }

        ?>
        
        <div class="formulario">
            <h1>Inserir Oferta</h1>
            <form class="form" action="" method="post" enctype="multipart/form-data">

                <?= !empty($mensagem) ? '<div class="mensagem">' . $mensagem . '</div>' : '' ?>
                
                <input type="text" class="dados_registo_login" name="nome" placeholder="Nome" required>
                <input type="text" class="dados_registo_login" name="descricao" placeholder="Descrição" required />
                <input type="number" step=0.01 class="dados_registo_login" name="preco" placeholder="Preço" required />
                <input type="file" class="dados_registo_login" name="foto" required>
                <input type="submit" name="submeter" value="Inserir" class="botao_registo_login">
            </form>
        </div> 

    </div>

</body>
</html>