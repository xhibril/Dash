import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "../css/Login.module.css";

export default function Login() {
        const nav = useNavigate();



    const [email, Email] = useState("");
    const [pass, Password] = useState("")

    return (

        <div className = {styles.wrapper}>

        <div className = {styles.inputContainer}>

<h1 className = {styles.siteName}>DASH</h1>
<h1 className = {styles.pageTitle}>Login</h1>


<label className = {styles.label}> Email:
            <input className = {styles.input}
            onChange={(e) => Email(e.target.value)}
            placeholder = "Email"
            ></input>
</label>

<label > Password
            <input className = {styles.input}
            onChange={(e) => Password(e.target.value)}
            placeholder = "Password"
            ></input>
</label>


<label className = {styles.label}>
    <input className = {styles.rememeber}
    type = "checkbox"></input>
    <p>Remember me</p>
    </label>


    <label className = {styles.label}>
        <a>Forgot your password?</a>
    </label>

            <button className = {styles.submit}
            onClick = {()=> submitCredentials(email, pass, nav)}>Login</button>


   <span className = {styles.label}>
    <p>Don't have an account?</p>
    <a>Sign up</a>
    </span>


        </div>
        </div>
);
}

async function submitCredentials(email, pass, nav){


    const res = await fetch("api/login",{
        method: "POST", 
        headers: {"Content-Type": "application/json" },
        body: JSON.stringify({email, pass})
    })


    if(!res.ok){
        console.log("Sum went wrong");
        return;
    }


    const data = await res.text();


    console.log(data);
    if(data === "SUCCESS"){

        console.log("worked");
    nav("/dashboard");
    }

}