import { useState } from "react";

export default function Signup() {


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
            onClick = {()=> submitCredentials(email, pass)}>Signup</button>

        </div>
);
}

async function submitCredentials(email, pass){


    const res = await fetch("api/signup",{
        method: "POST", 
        headers: {"Content-Type": "application/json" },
        body: JSON.stringify({email, pass})
    })


    if(!res.ok){
        console.log("Sum went wrong");
        return;
    }
}