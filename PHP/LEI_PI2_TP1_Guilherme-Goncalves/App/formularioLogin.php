<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TooGoodToGo</title>
    <link rel="stylesheet" href="formulariocss.css">
</head>
<body>

    <header>
        <h1>TooGoodToGo</h1>
    </header>

    <?php 

        session_start();

        $erro = "";

        include_once "conexao.php";

        if (isset($_POST["submeter"])){
            $username = $_POST["usernameLogin"];
            $palavra_passe = $_POST["passwordLogin"];
            $STH = $DBH->prepare("SELECT * FROM admin WHERE username = ?");
            $STH->execute([$username]);
            $user = $STH->fetch(PDO::FETCH_ASSOC);

            if ($user) {
                if (password_verify($palavra_passe, $user["password"])) {
                    $_SESSION['usernameLogin'] = $username;
                    $_SESSION['passwordLogin'] = $palavra_passe;

                    header("Location: listaEntidades.php");
                } 
                else {
                    $erro = "Dados incorretos. <br> Por favor, tente novamente.";
                }
            } 
            else {
                unset($_SESSION['usernameLogin']);
                unset($_SESSION['passwordLogin']);
                $erro = "Utilizador não encontrado. <br> Por favor, verifique os dados introduzidos.";
            }
        }

    ?>

    <form class="form" action="" method="post">
        <h1 class="registo_login">Login Conta Admin<hr></h1>

        <?= !empty($erro) ? '<div class="mensagemErro">' . $erro . '</div>' : '' ?>

        <input type="text" class="dados_registo_login" name="usernameLogin" placeholder="Username" required />
        <input type="password" class="dados_registo_login" name="passwordLogin" placeholder="Password" required>
        <input type="submit" name="submeter" value="Login" class="botao_registo_login">
        <p class="referencia"><a href="formularioRegisto.php">Ainda não tem conta? Clique aqui para se Registar</a></p>
    </form>
    
</body>
</html>