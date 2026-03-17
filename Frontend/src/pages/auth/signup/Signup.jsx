import Auth from "../../../components/auth/Auth.jsx";

export default function Signup({notify}) {

    return (
        <Auth mode = {"SIGNUP"} notify= {notify}/>
    );
       
}

