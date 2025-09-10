<?php 

    $host = 'localhost';
    $user = 'root';
    $pass = 'teste';
    $dbname = 'mydb';

    try {
        $DBH = new PDO("mysql:host=$host; dbname=$dbname", $user, $pass);
        $DBH->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    } catch (PDOException $e) {
        echo "Erro na conexão com o banco de dados: " . $e->getMessage();
    }

?>

