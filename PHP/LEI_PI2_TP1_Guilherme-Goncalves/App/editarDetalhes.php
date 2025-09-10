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

    if (isset($_POST["submeter"])) {

        $nome = $_POST["nome"];
        $descricao = $_POST["descricao"];
        $morada = $_POST["morada"];
        $telefone = $_POST["telefone"];
        $password = $_POST["password"];

        if (!empty($password)) {
            $passwordHash = password_hash($password, PASSWORD_DEFAULT);
        } 
        else {
            $passwordHash = $entidade['password'];
        }

        $novosDetalhes = [
            'nome' => $nome,
            'descricao' => $descricao,
            'logotipo' => $entidade['logotipo'],
            'morada' => $morada,
            'telefone' => $telefone,
            'password' => $passwordHash,
            'id' => $idEntidade,
        ];

        $ext = [
            'image/jpeg' => 'jpg',
            'image/png'  => 'png',
        ];

        if ($_FILES['logo']['size'] > 0) {

            $novoLogoNome = md5($_FILES["logo"]["name"]). '.' . $ext[$_FILES["logo"]["type"]];
            $tname = $_FILES["logo"]["tmp_name"];
            $pastaImagens = "./imagens";

            move_uploaded_file($tname, $pastaImagens.'/'.$novoLogoNome);

            $novosDetalhes['logotipo'] = $novoLogoNome;
        }

        $gerirEntidade->editarDetalhesEntidade($novosDetalhes, $DBH);

        $mensagem = "Entidade atualizada com sucesso";
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
                <h2>Editar detalhes</h2>
                <a href="listaEntidades.php"><- Voltar para a Lista de Entidades</a>
            </div>
            <form class="form" action="" method="post" enctype="multipart/form-data">

                <?= !empty($mensagem) ? '<div class="mensagem">' . $mensagem . '</div>' : '' ?>
                
                <input type="text" class="dados_registo_login" name="nome" value="<?php echo $entidade['nome'] ?>" required>
                <input type="text" class="dados_registo_login" name="descricao" value="<?php echo $entidade['descricao']; ?>" required />
                <input type="text" class="dados_registo_login" name="morada" value="<?php echo $entidade['morada'] ?>" required />
                <input type="text" class="dados_registo_login" name="telefone" value="<?php echo $entidade['telefone'] ?>" required />
                <input type="email" class="dados_registo_login" name="email" value="<?php echo $entidade['email'] ?>" readonly required />
                <input type="password" class="dados_registo_login" name="password" placeholder="Nova Palavra-Passe (Opcional)" />
                <input type="file" class="dados_registo_login" name="logo" value="<?php echo $entidade['logotipo']?>">
                <img src="imagens/<?php echo $entidade['logotipo'] ?>" width = 200px height=100px alt="Logotipo">
                <input type="submit" name="submeter" value="Atualizar" class="botao_registo_login">
            </form>
        </div> 

    </div>
    
</body>
</html>
