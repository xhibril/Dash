export default async function apiFetch(url, options = {}, nav){
    const res = await fetch(url, {
        credentials: "include",
        ...options
    });


    if(res.status == 401){
        nav("/login");
    }


    if(res.status == 403){
        nav("/verify/email");
    }
 
    return res;
}