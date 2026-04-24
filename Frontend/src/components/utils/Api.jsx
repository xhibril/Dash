export default async function apiFetch(url, options = {}, nav, notify) {
    const res = await fetch(`https://api.xhibril.dev${url}`, {
        credentials: "include",
        ...options
    });


    if (res.status == 401) {
        nav("/login");
        return null;
    }

    if (res.status == 403) {
        nav("/verify/email");
        return null;
    }

    if (res.status == 429) {
        notify("Too many requests. Please try again later", "ERROR");
        return null;
    }

    return res;
}