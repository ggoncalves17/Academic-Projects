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

    if (isset($_POST["submeter"])) {

        $nome = $_POST["nome"];
        $descricao = $_POST["descricao"];
        $preço = $_POST["preco"];
        
        $novosDetalhes = [
            'nome' => $nome,
            'descricao' => $descricao,
            'preco' => $preço,
            'foto' => $oferta['foto'],
            'id' => $idOferta,
        ];

        $ext = [
            'image/jpeg' => 'jpg',
            'image/png'  => 'png',
        ];

        if ($_FILES['foto']['size'] > 0) {

            $novoLogoNome = md5($_FILES["foto"]["name"]) . '.' . $ext[$_FILES["foto"]["type"]];
            $tname = $_FILES["foto"]["tmp_name"];
            $pastaImagens = "./imagens";

            move_uploaded_file($tname, $pastaImagens.'/'.$novoLogoNome);

            $novosDetalhes['foto'] = $novoLogoNome;
        }

        $gerirOferta->editarDetalhesOferta($novosDetalhes, $DBH);

        $mensagem = "Oferta atualizada com sucesso";
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
                <li id="listaOfertas"><a href="listaOfertas.php">Ofertas Disponíveis</a></li>
                <li><a href="inserirOferta.php">Inserir Nova Oferta</a></li>
            </ul>
        </nav>

        <div class="formulario">
            <div>
                <h2>Editar Oferta</h2>
                <a href="listaOfertas.php"><- Voltar para a Lista de Ofertas</a>
            </div>
            <form class="form" action="" method="post" enctype="multipart/form-data">

                <?= !empty($mensagem) ? '<div class="mensagem">' . $mensagem . '</div>' : '' ?>
                
                <input type="text" class="dados_registo_login" name="nome" value="<?php echo $oferta['nome'] ?>" required>
                <input type="text" class="dados_registo_login" name="descricao" value="<?php echo $oferta['descricao'] ?>" required />
                <input type="number" step=0.01 class="dados_registo_login" name="preco" value="<?php echo $oferta['preço'] ?>" required />
                <input type="file" class="dados_registo_login" name="foto" value="<?php echo $oferta['foto']?>">
                <img src="imagens/<?php echo $oferta['foto'] ?>" width = 200px height=100px alt="Foto">
                <input type="submit" name="submeter" value="Atualizar" class="botao_registo_login">
            </form>
        </div> 
    </div>

</body>
</html> 
