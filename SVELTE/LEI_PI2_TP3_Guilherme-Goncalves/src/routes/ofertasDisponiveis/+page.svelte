<script>
    import { onMount, beforeUpdate } from 'svelte';
    import { goto } from '$app/navigation';

    let token = '';

    beforeUpdate( () => {
        let tokenAux = sessionStorage.getItem("token");

        if(tokenAux === undefined || tokenAux === null){
            goto("/autenticar");
        } 
        else {
            token = tokenAux;
        }
    });

    let dados;
    onMount(async () => {
        dados = await fetch('http://127.0.0.1:8080/api/entidades/ofertas/', 
        {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': token
            }
        })
        .then((x) => {
            if(x.ok) {
                console.log(x);
                return x.json();
            }
            if(x.status == 401) {
                goto("/autenticar");
            }
        })
        .catch((error) => console.error ("-->" + error));    
    });

    function registaCompra (id_oferta) {
        var url = new URL('http://127.0.0.1:8080/api/entidades/ofertas/comprar');
        fetch(url, {
            method : 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            },
            body: JSON.stringify({
                id_oferta
            })
        })
        .then((res) => res.json())
        .then(function (data){

            console.log(data);

            if(data.res == "Erro" || data.res == "Error") {
                console.log('Erro ao realizar a compra');
            }
            else {
                console.log("Compra realizada com sucesso!")
                window.location.reload();
            }
        })
        .catch((error) => console.log(error));
    }
</script>

<style>
    h1 {
        text-align: center;
    }

    h4 {
        display: inline-block;
        margin-bottom: 5px;
    }
    
    .ofertas {
        display: flex;
        flex-wrap: wrap;
    }
    .dados {
        display: flex;
        flex-direction: column;
        background: rgb(224, 224, 224);
        width: 18%;
        margin: 10px;
        padding: 20px;
        position:relative;
        border: 1px solid rgb(0, 0, 0);
        border-radius: 5px;
    }
    .nome {
        font-weight: bold;
        font-family: Arial;
        position: absolute;
        text-align: center;
        top: 10px;
        left: 0;
        right: 0;
    }

    .detalhes {
        flex-grow: 1;
    }

    .divBotao {
        text-align: center;
        margin-top: 10px;
    }

    .botão {
        padding: 10px;
        background-color: #3a99ff;
        border: 1px solid black ;
        border-radius: 5px;
        cursor: pointer;
    }

</style>

<h1>Lista das Ofertas Disponiveis</h1>

{#await dados}
    <p>...waiting</p>
{:then dadosOferta}
    <div class="ofertas">
        {#if dadosOferta != undefined && (dadosOferta.data).length > 0}
            {#each dadosOferta.data as oferta}
                <div class="dados">
                    <div class="nome">
                        {oferta.Nome_Oferta}<hr>
                    </div>
                    <div class="detalhes">
                        <br>
                        <h4>Entidade:</h4> {oferta.Nome_Entidade} <br> 
                        <h4>Descrição Entidade:</h4> {oferta.Descricao_Entidade} <br> 
                        <h4>Morada:</h4> {oferta.morada} <br> 
                        <h4>Telefone:</h4> {oferta.telefone} <br> 
                        <h4>Email:</h4> {oferta.email} <br> 
                        <h4>Preço:</h4> {oferta.preço}€ <br>
                        <h4>Descrição Oferta:</h4> {oferta.Descricao_Oferta} <br> 
                    </div>
                    <div class="divBotao">
                        <button class="botão" on:click={ () => registaCompra(oferta.id)}>Comprar</button>
                    </div>
                </div>
            {/each}
        {:else}
            Lista vazia ou problemas a comunicar com servidor!
        {/if}
    </div>
{:catch error}
    <p style="color: red">{error.message}</p>
{/await}

