<script>
    import { goto } from '$app/navigation';
    import { beforeUpdate } from 'svelte';	
    
    beforeUpdate( async () => {

        let token = sessionStorage.getItem("token");

        if(token === undefined || token === null){
            goto("/autenticar");
        }
        else {
            var url = new URL('http://127.0.0.1:8080/api/users/logout/');

            data = await fetch(url, {
                method : 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
            })
            .then((x) => {
                console.log(x);

                if (x.ok){
                    console.log("Logout feito com sucesso!");
                    sessionStorage.removeItem("token");
                    goto("/autenticar");
                }
                if(x.status == 401){
                    goto("/autenticar");
                }
            })
            .catch((error) => console.error("--->" + error));

            
        }  
    });	
</script>

