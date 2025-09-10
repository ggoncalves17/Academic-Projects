<?php
    session_start();

    if ((!isset($_SESSION['email']))) {
        unset($_SESSION['usernameLogin']);
        unset($_SESSION['passwordLogin']);
        header('Location: loginEntidade.php');
    }

    include_once "conexao.php"; 
    include_once "oferta.php";
    include_once "gereOferta.php";

    $idOferta = $_GET['id'];

    $gerirOferta = new gereOferta();
    $oferta = $gerirOferta->detalhesOferta($idOferta, $DBH);

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TooGoodToGo</title>
    <link rel="stylesheet" href="menu.css">
</head>
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

        <div class="formulario">
            <div>
                <h2>Detalhes da Oferta</h2>
                <a href="listaOfertas.php"><- Voltar para a Lista de Ofertas</a>
            </div>

            <div class="detalhes">
                <div class="detalhes2">
                    <p><h3>Nome:</h3><?php echo $oferta['nome'] ?></p>
                    <p><h3>Descrição:</h3><?php echo $oferta['descricao'] ?></p>
                    <p><h3>Preço:</h3><?php echo $oferta['preço'] ?>€</p>
                </div>
                <div class="imagemEntidade">
                    <img src="imagens/<?php echo $oferta['foto'] ?>" alt="Foto da Oferta" width="300px" height="200px">
                </div>
            </div>
        </div>  
    </div>

</body>
</html> 
