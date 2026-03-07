import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Auth from "../components/Auth.jsx";

export default function Login() {

    return (
        <Auth mode = {"LOGIN"}/>
    );
       
}

