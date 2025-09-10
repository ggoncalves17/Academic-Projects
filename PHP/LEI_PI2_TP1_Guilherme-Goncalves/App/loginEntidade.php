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
            $email = $_POST["email"];
            $palavra_passe = $_POST["passwordLogin"];
            $STH = $DBH->prepare("SELECT * FROM entidade WHERE email = ?");
            $STH->execute([$email]);
            $user = $STH->fetch(PDO::FETCH_ASSOC);

            if ($user && $user['ativo'] == 1) {
                if (password_verify($palavra_passe, $user["password"])) {
                    $_SESSION['email'] = $email;
                    $_SESSION['passwordLogin'] = $palavra_passe;

                    header("Location: listaOfertas.php");
                } 
                else {
                    $erro = "Dados incorretos. <br> Por favor, tente novamente.";
                }
            } 
            else {
                unset($_SESSION['email']);
                unset($_SESSION['passwordLogin']);
                $erro = "Utilizador não encontrado. <br> Por favor, verifique os dados introduzidos.";
            }
        }

    ?>

    <form class="form" action="" method="post">
        <h1 class="registo_login">Login Conta Entidade<hr></h1>

        <?= !empty($erro) ? '<div class="mensagemErro">' . $erro . '</div>' : '' ?>

        <input type="email" class="dados_registo_login" name="email" placeholder="Email" required />
        <input type="password" class="dados_registo_login" name="passwordLogin" placeholder="Password" required>
        <input type="submit" name="submeter" value="Login" class="botao_registo_login">
    </form>
    
</body>
</html>