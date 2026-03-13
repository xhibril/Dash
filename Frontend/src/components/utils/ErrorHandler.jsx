export function HandleError(error){


    if(error === 401){
        window.location.href = "/login";
        return;
    }


    if(error == 403){
        window.location.href = "/verify/email"
        return;
    }

}