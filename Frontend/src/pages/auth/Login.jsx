import Auth from "../../components/auth/Auth.jsx";

export default function Login({notify}) {

    return (
        <Auth mode = {"LOGIN"} notify = {notify}/>
    );
       
}

