import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {
        const nav = useNavigate();



    const [email, Email] = useState("");
    const [pass, Password] = useState("")

    return (

        <div>
            <input
            onChange={(e) => Email(e.target.value)}
            placeholder = "Email"
            ></input>

            <input
            onChange={(e) => Password(e.target.value)}
            placeholder = "Password"
            ></input>


            <button style={{ background: "black", width:"500px"}}
            onClick = {()=> submitCredentials(email, pass, nav)}>Login</button>

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