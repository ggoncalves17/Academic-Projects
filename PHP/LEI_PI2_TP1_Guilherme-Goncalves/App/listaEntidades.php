<?php
    session_start();

    if ((!isset($_SESSION['usernameLogin']))) {
        unset($_SESSION['usernameLogin']);
        unset($_SESSION['passwordLogin']);
        header('Location: formularioLogin.php');
    }

    include_once "conexao.php"; 
    include_once "entidade.php";
    include_once "gereEntidade.php";

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
        #listaEntidades {
            background-color: rgb(82,104,118);
        }
    </style>
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
            <div class="superior">
                <h2>Lista das Entidades</h2>
                <div class="inserirEnti">
                    <a href="inserirEntidade.php" id="inserir">Inserir nova entidade</a>
                </div>
            </div>
            <table class="tabela">
                <tbody>
                    <?php 
                    
                        $RES = $DBH->query('SELECT * FROM entidade');
                        $RES->setFetchMode(PDO::FETCH_ASSOC);
                        while($entidade = $RES->fetch()) {

                            if ($entidade['ativo'] == 1) {
                                $estadoBotao = 'Desativar';
                            }
                            else {
                                $estadoBotao = 'Ativar';
                            }

                            echo"
                                <tr>
                                    <td>$entidade[nome]  </td>
                                    <td><a href='detalhes.php?id=$entidade[id]' class='botão'>Ver Detalhes</a></td>
                                    <td><a href='editarDetalhes.php?id=$entidade[id]' class='botão'>Editar</a></td>
                                    <td><form action='' method='post'>
                                            <input type='hidden' name='id' value='$entidade[id]'>
                                            <input type='hidden' name='estadoAtual' value='$entidade[ativo]'>
                                            <input type='submit' class='$estadoBotao' name='estado' value='$estadoBotao'>
                                        </form></td>
                                </tr> ";

                            if (isset($_POST['estado'])) {
                                $idEntidade = $_POST['id'];
                                $estadoEntidade = $_POST['estadoAtual'];
                        
                                $gerirEntidade = new gereEntidade();
                                $gerirEntidade->alterarEstadoEntidade($idEntidade, $estadoEntidade, $DBH);
                        
                                header("Location: listaEntidades.php");
                            }
                        }
                    ?>
                </tbody>
            </table>
        </div>  
    </div>

    

</body>
</html>