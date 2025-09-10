<?php
    session_start();

    if ((!isset($_SESSION['email']))) {
        unset($_SESSION['usernameLogin']);
        unset($_SESSION['passwordLogin']);
        header('Location: loginEntidade.php');
    }

    include_once "conexao.php"; 
    include_once "entidade.php";
    include_once "gereOferta.php";

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
        #listaOfertas {
            background-color: rgb(82,104,118);
        }
    </style>
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
            <div class="superior">
                <h2>Lista das Ofertas Disponíveis - (Apenas as de hoje)</h2>
                <div class="inserirEnti">
                    <a href="inserirOferta.php" id="inserir">Inserir nova oferta</a>
                </div>
            </div>
            <table class="tabela">
                <tbody>
                    <?php 
                    
                        $emailEntidade = $_SESSION['email'];
                        $STH = $DBH->prepare("SELECT id FROM entidade WHERE email = ?");
                        $STH->execute([$emailEntidade]);
                        $entidade = $STH->fetch(PDO::FETCH_ASSOC);
                        $idEntidade = $entidade['id'];

                        $RES = $DBH->prepare('SELECT * FROM oferta WHERE entidade_id = ?');
                        $RES->execute([$idEntidade]);

                        $RES->setFetchMode(PDO::FETCH_ASSOC);

                        $ofertasDisponiveis = 0;

                        while($oferta = $RES->fetch()) {

                            $data = date("Y-m-d");

                            if($data === $oferta['data'] && $oferta['disponivel'] === 1) {

                                $ofertasDisponiveis++;

                                echo"
                                    <tr>
                                        <td>$oferta[nome]</td>
                                        <td><a href='detalhesOferta.php?id=$oferta[id]' class='botão'>Ver Detalhes</a></td>
                                        <td><a href='editarOferta.php?id=$oferta[id]' class='botão'>Editar</a></td>
                                        <td><form action='' method='post'>
                                                <input type='hidden' name='id' value='$oferta[id]'>
                                                <input type='submit' class='Desativar' name='remover' value='Remover'>
                                            </form></td>
                                    </tr> ";

                                if (isset($_POST['remover'])) {
            
                                    $idOferta = $_POST['id'];
                                    $gerirOferta = new gereOferta();
                                    $gerirOferta->removerOferta($idOferta, $DBH);
                            
                                    header("Location: listaOfertas.php");
                                }
                            }
                        }
                        if ($ofertasDisponiveis == 0) {
                            echo "<p>Nenhuma oferta disponível para hoje.</p>";
                        }
                    ?>
                </tbody>
            </table>
        </div> 

                  
    </div>

</body>
</html>