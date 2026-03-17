import Auth from "../../../components/auth/auth.jsx"

export default function Login({notify}) {

    return (
        <Auth mode = {"LOGIN"} notify = {notify}/>
    );
       
}

