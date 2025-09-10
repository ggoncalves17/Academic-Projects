<?php 
    session_start();
    if ((!isset($_SESSION['usernameLogin']))) {
        unset($_SESSION['usernameLogin']);
        unset($_SESSION['passwordLogin']);
        header('Location: formularioLogin.php');
    }

    $userLogin = $_SESSION['usernameLogin'];

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TooGoodToGo</title>
    <link rel="stylesheet" href="menu.css">
    <style>
        #Pass {
            background-color: rgb(82,104,118);
        }
    </style>
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
                <li><a href="listaEntidades.php">Entidades Disponíveis</a></li>
                <li><a href="inserirEntidade.php">Inserir Nova Entidade</a></li>
                <li id="Pass"><a href="alterarPassword.php">Alterar Password</a></li>
            </ul>
        </nav>

        <?php 
        
            include_once "conexao.php";

            if (isset($_POST["submeter"])){

                $passAntiga = $_POST["passAntiga"];
                $novaPass = $_POST["novaPass"];
                $confirmarNovaPass = $_POST["confirmarNovaPass"];

                if ($novaPass === $confirmarNovaPass) {
            
                    $STH = $DBH->prepare("SELECT password FROM admin WHERE username = ?");
                    $STH->execute([$userLogin]);
                    $user = $STH->fetch(PDO::FETCH_ASSOC);
            
                    if ($user && password_verify($passAntiga, $user['password'])) {

                        $novaPassHash = password_hash($novaPass, PASSWORD_DEFAULT);

                        $STH = $DBH->prepare("UPDATE admin SET password = ? WHERE username = ?");
                        $STH->execute([$novaPassHash, $userLogin]);
            
                        $mensagem = "Palavra-Passe alterada com sucesso!";

                    } 
                    else {
                        $erro = "Palavra-Passe antiga incorreta. Tente novamente.";
                    }
                } 
                else {
                    $erro = "A nova Palavra-Passe e a confirmação da mesma não coincidem. Tente novamente.";
                }

        
            }

        ?>

        <div class="formulario">
            <h1 class="registo_login">Alterar Password</h1>
            <form class="form" action="" method="post">

                <?= !empty($erro) ? '<div class="mensagemErro">' . $erro . '</div>' : '' ?>
                <?= !empty($mensagem) ? '<div class="mensagem">' . $mensagem . '</div>' : '' ?>

                <input type="password" class="dados_registo_login" name="passAntiga" placeholder="Palavra-Passe Antiga" required />
                <input type="password" class="dados_registo_login" name="novaPass" placeholder="Nova Palavra-Passe" required>
                <input type="password" class="dados_registo_login" name="confirmarNovaPass" placeholder="Confirmar Nova Palavra-Passe" required>
                <input type="submit" name="submeter" value="Alterar" class="botao_registo_login">

            </form>
        </div>
    </div>

    
    
</body>
</html>