import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Auth from "../components/Auth.jsx";

export default function ForgotPassword({notify}) {

    return (
        <Auth mode = {"FORGET"} notify={notify}/>
    );
       
}

