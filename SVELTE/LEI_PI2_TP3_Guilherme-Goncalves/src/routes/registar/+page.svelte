<script>
    import {goto} from '$app/navigation';

    let nome = '';
    let email = '';
    let telefone = '';
    let password = '';
    let erro = '';

    function regista() {
        var url = new URL('http://127.0.0.1:8080/api/users/');
        fetch(url, {
            method : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
            nome, email, telefone, password
            })
        })
        .then((res) => res.json())
        .then(function (data){

            console.log(data);

            if(data.res == "Erro" || data.res == "Error") {
                erro = data.message;
                console.log('Erro ao registar cliente');
            }
            else{
                console.log('Cliente registado com sucesso');
                goto("/autenticar");
            }
        })
        .catch((error) => console.log(error));
    }

</script>

<style>
    .form {
        margin: 20px auto;
        width: 30%;
        padding: 20px 20px;
        background: white;
        border-radius: 10px;
        border: 1px solid #000000;
    }

    .dados_registo_login {
        font-size: 15px;
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 25px;
        height: 25px;
        width: 93%;
        background-color: #dddddd;
    }

    .botao_registo_login {
        color: #fff;
        background: rgb(60, 142, 209);
        border: 1px solid #4749ce;
        text-align: center;
        width: 100%;
        height: 50px;
        font-size: 18px;
        text-align: center;
        cursor: pointer;
    }
</style>

<form on:submit|preventDefault={regista} class="form">
    <h1 class="registo_login">Registo Conta Cliente<hr></h1>
    <span style="color:red">{erro}</span><br>
    <input type="text" class="dados_registo_login" name="nome" bind:value={nome} placeholder="Nome" required>
    <input type="email" class="dados_registo_login" name="email" bind:value={email} placeholder="Email" required />
    <input type="text" class="dados_registo_login" name="telefone" bind:value={telefone} placeholder="Telefone" required>
    <input type="password" class="dados_registo_login" name="password" bind:value={password} placeholder="Password" required>
    <input type="submit" name="submeter" value="Registar" class="botao_registo_login">
    <p class="referencia"><a href="/autenticar">Já tem conta? Clique aqui para fazer Login</a></p>

    
</form>


