export default function DeleteAccount(){



    async function deleteAccount(){
        const res = await fetch("/api/delete/account", {method:"POST"});

        if(!res.ok){
            console.log("error deleting");
            return;
        }


        window.location.href = "/";
    }

    return (
        <div>
        <button
        onClick={()=> deleteAccount()}>delete account</button>
        </div>
    );



}