<?php
    session_start();

    if ((!isset($_SESSION['usernameLogin']))) {
        unset($_SESSION['usernameLogin']);
        unset($_SESSION['passwordLogin']);
        header('Location: formularioLogin.php');
    }

    include_once "conexao.php"; 
    include_once "gereEntidade.php";

    $idEntidade = $_GET['id'];

    $gerirEntidade = new gereEntidade();
    $entidade = $gerirEntidade->detalhesEntidade($idEntidade, $DBH);

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
                <li id="listaEntidades"><a href="listaEntidades.php">Entidades Disponíveis</a></li>
                <li><a href="inserirEntidade.php">Inserir Nova Entidade</a></li>
                <li><a href="alterarPassword.php">Alterar Password</a></li>
            </ul>
        </nav>

        <div class="formulario">
            <div>
                <h2>Detalhes da Entidade - <?php echo $entidade['nome']?></h2>
                <a href="listaEntidades.php"><- Voltar para a Lista de Entidades</a>
            </div>

            <div class="detalhes">
                <div class="detalhes2">
                    <p><h3>Nome:</h3><?php echo $entidade['nome'] ?></p>
                    <p><h3>Email:</h3><?php echo $entidade['email'] ?></p>
                    <p><h3>Descrição:</h3><?php echo $entidade['descricao'] ?></p>
                    <p><h3>Morada:</h3><?php echo $entidade['morada'] ?></p>
                    <p><h3>Telefone:</h3><?php echo $entidade['telefone'] ?></p>
                </div>
                <div class="imagemEntidade">
                    <img src="imagens/<?php echo $entidade['logotipo'] ?>" alt="Logo da Entidade" width="300px" height="200px">
                </div>
            </div>
        </div>  
    </div>

</body>
</html> 
