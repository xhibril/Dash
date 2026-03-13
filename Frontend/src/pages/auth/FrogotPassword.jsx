import Auth from "../../components/auth/Auth.jsx";

export default function ForgotPassword({notify}) {

    return (
        <Auth mode = {"FORGET"} notify={notify}/>
    );
       
}

