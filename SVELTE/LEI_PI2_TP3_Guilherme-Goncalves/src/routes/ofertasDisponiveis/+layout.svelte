<style>
    header {
        width: 100%;
        background: #71da9d;
        padding: 3px;
        box-sizing: border-box;
        margin: 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    header h1 {
        font-size: 35px;
        margin: 10px;
    }

    header a {
        text-decoration: none;
        color: black;
    }

    .logout {
        background-color: rgb(255, 115, 115);
        width: 8%;
        height: 20px;
        margin-right: 10px;
        padding: 5px;
        text-align: center;
        border: 1px solid black;
        font-weight: bold;
    }

    #sair {
        text-decoration: none;
        color: black;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
    }

    nav {
        background-color: rgb(50, 76, 97);
        padding: 15px;
    }

    nav a {
        color: white;
        text-decoration: none;
        padding: 15px;
        font-family: 'Arial';
    }

    nav a:hover {
        background-color: rgb(82,104,118);
    }
</style>

<header>
    <a href="/ofertasDisponiveis">
        <div class="logo">
            <h1>TooGoodToGo</h1>
        </div>
    </a>
    <div class="logout">
        <a href="/logout" id="sair">Sair</a>
    </div>
</header>

<nav>
    <a href="/ofertasDisponiveis">Lista das Ofertas</a>
    <a href="/comprasRealizadas">Histórico de Compras</a>
</nav>

<slot />