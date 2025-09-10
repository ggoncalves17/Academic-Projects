<?php 

    session_start();

    if($_SESSION['usernameLogin']) {
        unset($_SESSION['usernameLogin']);
        unset($_SESSION['passwordLogin']);
        header("Location: formularioLogin.php");
    }
    else {
        unset($_SESSION['email']);
        unset($_SESSION['passwordLogin']);
        header("Location: loginEntidade.php");
    }


?>