import Auth from "../../../components/auth/auth.jsx"

export default function Login({notify, setIsAuth, isAuth}) {

    return (
        <Auth mode = {"LOGIN"} notify = {notify} setIsAuth={setIsAuth} isAuth = {isAuth}/>
    );
       
}

