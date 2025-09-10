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

    include_once "conexao.php";

    if (isset($_POST["submeter"])){

        $nome = $_POST["nome"];
        $username = $_POST["username"];
        $palavra_passe = $_POST["password"];
        $passwordHash = password_hash($palavra_passe, PASSWORD_DEFAULT);

        $STH = $DBH->prepare("SELECT * FROM admin WHERE username = ?");
        $STH->execute([$username]);
        $user = $STH->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            $mensagemErro = "Erro: O username já existe. Tente outro.";
        }
        else {
            $STH = $DBH->prepare("INSERT INTO admin (username, nome, password) values (?, ?, ?)");
            $STH->execute([$username, $nome, $passwordHash]);
    
            header("Location: formularioLogin.php");
        }
    }
    ?>

    <form class="form" action="" method="post">
        <h1 class="registo_login">Registo Conta Administrador<hr></h1>

        <?= !empty($mensagemErro) ? '<div class="mensagemErro">' . $mensagemErro . '</div>' : '' ?>

        <input type="text" class="dados_registo_login" name="nome" placeholder="Nome" required>
        <input type="text" class="dados_registo_login" name="username" placeholder="Username" required />
        <input type="password" class="dados_registo_login" name="password" placeholder="Password" required>
        <input type="submit" name="submeter" value="Registar" class="botao_registo_login">
        <p class="referencia"><a href="formularioLogin.php">Já tem conta? Clique aqui para fazer Login</a></p>
    </form>

    

</body>
</html>