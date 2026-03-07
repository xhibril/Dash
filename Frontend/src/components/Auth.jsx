import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "../css/Auth.module.css";

export default function Auth({ mode }) {
    const nav = useNavigate();

    // turn is login into a bool
    const isLogin = mode === "LOGIN";

    const [email, setEmail] = useState("");
    const [pass, setPassword] = useState("")

    const [step, setStep] = useState("EMAIL")
    const [code, setCode] = useState("")
    const [resetToken, setResetToken] = useState("")
    const [newPassword, setNewPassword] = useState("")

    async function ForgotPassword() {

        if (step === "EMAIL") {
            const res = await fetch("api/password/reset", {

                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email })
            })


            if (!res.ok) {
                console.log("error sending email");
                return;
            }

            setStep("VERIFY");
        }


        if (step === "VERIFY") {
            const res = await fetch("api/password/reset/verify", {

                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, code })
            })


            if (!res.ok) {
                console.log("error verifying");
                      return;
            }

            const data = await res.json();

            setResetToken(data.resetToken);
            
                 setStep("RESET");
        }


        if (step === "RESET") {
            const res = await fetch("api/password/reset/new", {

                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, newPassword, resetToken })
            })


            if (!res.ok) {
                console.log("error changing");
                      return;
            }

        }


    }

    return (

        <div className={styles.wrapper}>

            <div className={styles.inputContainer}>

                <h1 className={styles.siteName}>DASH</h1>



                {mode !== "FORGET" ? (
                    <h1 className={styles.pageTitle}>
                        {isLogin ? "Login" : "Sign up"}
                    </h1>
                ) :
                    <h1 className={styles.pageTitle}>
                        Reset Password
                    </h1>
                }




                {mode !== "FORGET" && (
                    <div className={styles.fields}>


                        <label className={styles.label}> Email
                            <input className={styles.input}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="you@email.com"
                            ></input>
                        </label>



                        <label className={styles.label} > Password
                            <input className={styles.input}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="•••••"
                            ></input>
                        </label>
                    </div>

                )}


                {mode === "FORGET" && step === "EMAIL" && (
                    <label className={styles.label}> Email
                        <input className={styles.input}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="you@email.com"
                        ></input>
                    </label>
                )}

                {mode === "FORGET" && step === "VERIFY" && (
                    <label className={styles.label}> 6 digit code
                        <input className={styles.input}
                            onChange={(e) => setCode(e.target.value)}
                            placeholder="123456"
                        ></input>
                    </label>
                )}


                {mode === "FORGET" && step === "RESET" && (
                    <>
                        <label className={styles.label}> Old Password
                            <input className={styles.input}
                                onChange={(e) => setOldPassword(e.target.value)}
                                placeholder="•••••"
                            ></input>
                        </label>

                        <label className={styles.label}> New Password
                            <input className={styles.input}
                                onChange={(e) => setNewPassword(e.target.value)}
                                placeholder="•••••"
                            ></input>
                        </label>
                    </>
                )}







                {
                    mode == "LOGIN" ? (
                        <div className={styles.remember}>
                            <input type="checkbox"></input>
                            <p>Remember me</p>

                        </div>
                    ) : null
                }


                {mode !== "FORGET" ? (
                    <button className={styles.submit}
                        style={!isLogin ? { marginTop: "1rem" } : {}}
                        onClick={() => submitCredentials(mode, email, pass, nav, isLogin ? "api/login" : "api/signup")} >
                        {isLogin ? "Login" : "Sign up"}
                    </button>
                ) :

                    <button className={styles.submit}
                        style={!isLogin ? { marginTop: "1rem" } : {}}
                        onClick={() => ForgotPassword(email)} >
                        Continue
                    </button>
                }







                {
                    mode == "LOGIN" ? (
                        <label className={styles.forgot}
                        onClick={() => nav("/forget")}>
                            <a>Forgot your password?</a>
                        </label>
                    ) : null
                }




                {mode !== "FORGET" ? (
                    mode === "LOGIN" ? (
                        <span className={styles.redirect}>
                            <p>Don't have an account?</p>
                            <a>Sign up</a>
                        </span>
                    ) : (
                        <span className={styles.redirect}>
                            <p>Already have an account?</p>
                            <a>Login</a>
                        </span>
                    )
                ) : null}





            </div>
        </div>
    );
}

async function submitCredentials(mode, email, pass, nav, path) {


    const res = await fetch(path, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, pass })
    })


    if (!res.ok) {
        console.log("Sum went wrong");
        return;
    }


    const data = await res.text();


    console.log(data);


    if (data === "SUCCESS" && mode == "LOGIN") {
        nav("/dashboard");
    } else {
        nav("/verify/email")
    }

}





