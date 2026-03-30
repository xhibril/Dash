import Auth from "../../../components/auth/Auth.jsx"

export default function Login({notify, setIsAuth, isAuth}) {

    return (
        <Auth mode = {"LOGIN"} notify = {notify} setIsAuth={setIsAuth}/>
    );
       
}

