import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Auth.module.css";

import { ValidateEmail, ValidatePassword, ValidateCode } from "../Utils/Validation.jsx";
import { HandleError } from "../Utils/ErrorHandler.jsx";
import { FiEye } from "react-icons/fi";

export default function Auth({ mode, notify }) {

    const nav = useNavigate();

    // turn is login into a bool
    const isLogin = mode === "LOGIN";

    const [email, setEmail] = useState("");
    const [pass, setPassword] = useState("")

    const [step, setStep] = useState("EMAIL")
    const [code, setCode] = useState("")
    const [resetToken, setResetToken] = useState("")

    const [newPassword, setNewPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")

    const [rememberMe, setRememberMe] = useState(false);

    const [showPass, setShowPass] = useState(false)


    function checkIfVerified() {

        const params = new URLSearchParams(window.location.search);
        const isVerified = params.get("verified");


        if (isVerified === "true") {
            notify("Successfully verified", "SUCCESS");
        }

        if (isVerified === "false") {
            notify("Verification failed", "ERROR");
        }
    }

    useEffect(() => {
        checkIfVerified();
    }, [])


    async function ForgotPassword() {

        if (step === "EMAIL") {


            const emailRes = ValidateEmail(email)

            if (emailRes !== "VALID") {
                notify(emailRes, "ERROR");
                return;
            }


            const res = await fetch("/api/password/reset", {

                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email })
            })

            

            if (!res.ok) {
                const data = await res.json();

                if(data.message !== ""){
                    notify(data.message, "ERROR");
                    return;
                } 
                notify("Something went wrong, please try again", "ERROR");

                return;
            }

            notify("Verification code sent", "SUCCESS");
            setStep("VERIFY");

        }


        if (step === "VERIFY") {

            const codeRes = ValidateCode(code, 6);

            if (codeRes !== "VALID") {
                notify(codeRes, "ERROR");
                return;
            }


            const res = await fetch("/api/password/reset/verify", {

                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, code })
            })

             const data = await res.json();


            if (!res.ok) {


                if(data.message !== ""){
                    notify(data.message, "ERROR");
                    return;
                }
               

                notify("Something went wrplease try again", "ERROR");
              
                return;
            }


           

            setResetToken(data.resetToken);

            setCode("");
            notify("Verification successful", "SUCCESS");
            setStep("RESET");
        }


        if (step === "RESET") {

            if (newPassword !== confirmPassword) {
                notify("Passwords do not match");
                return;
            }

            const passRes = ValidatePassword(confirmPassword);

            if (passRes !== "VALID") {
                notify(passRes, "ERROR");
                return;
            }

            const res = await fetch("/api/password/reset/new", {

                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, newPassword, resetToken })
            })



            if (!res.ok) {
                const data = await res.json();
                if(data.message !== ""){
                    notify(data, "ERROR");
                    return;
                }
                notify("Something went wrong, please try again", "ERROR");

                return;
            }

            setNewPassword("")
            setConfirmPassword("")
            notify("Password changed successfully", "SUCCESS");
        }


    }


    async function submitCredentials(mode, email, pass, rememberMe, path) {
        localStorage.setItem("email", email);


        const emailRes = ValidateEmail(email);


        if (emailRes !== "VALID") {
            notify(emailRes, "ERROR");
            return;
        }

        const passwordRes = ValidatePassword(pass);

        if (passwordRes !== "VALID") {
            notify(passwordRes, "ERROR");
            return;
        }



        const res = await fetch(path, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, pass, rememberMe })
        })


        if (!res.ok) {

          HandleError(res.status);

            const data = await res.text();

            if (data !== "") {
                notify(data, "ERROR")

            } else {
                notify("Something went wrong, please try again");
            };
            return;
        }



        if (mode === "LOGIN") {

            window.location.href = "/dashboard";
            localStorage.removeItem("email");

        } else {
            setEmail("");
            setPassword("");
            window.location.href = "/verify/email"

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
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="you@email.com"
                            ></input>
                        </label>



                        <label className={styles.label} > Password
                            <input className={styles.input}
                                value={pass}
                                    type= {showPass ? "text" : "password"}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="•••••"
                              
                            ></input>
                                 <FiEye className = {styles.eye}
                                 onClick={()=> setShowPass(!showPass)}/>
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
                    <label className={styles.label}> 6-digit code
                        <input className={styles.input}

                            onChange={(e) => setCode(e.target.value)}
                            placeholder="123456"
                        ></input>
                    </label>
                )}


                {mode === "FORGET" && step === "RESET" && (
                    <>
                        <label className={styles.label}> New Password
                            <input className={styles.input}
                                type= {showPass ? "text" : "password"}
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                placeholder="•••••"
                            ></input>
                          
                        </label>

                        <label className={styles.label}> Confirm New Password
                            <input className={styles.input}
                             type= {showPass ? "text" : "password"}
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                placeholder="•••••"
                            ></input>
                                  <FiEye className = {styles.eye}
                                     onClick={()=> setShowPass(!showPass)}/>
                        </label>
                    </>
                )}







                {
                    mode == "LOGIN" ? (
                        <div className={styles.remember}>
                            <input type="checkbox"
                            onChange={(e) => setRememberMe(e.target.checked)}></input>
                            <p>Remember me</p>

                        </div>
                    ) : null
                }


                {mode !== "FORGET" ? (
                    <button className={styles.submit}
                        style={!isLogin ? { marginTop: "1rem" } : {}}
                        onClick={() => submitCredentials(mode, email, pass, rememberMe,  isLogin ? "api/login" : "api/signup")} >
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
                            <a
                            onClick={()=> nav("/signup")}>Sign up</a>
                        </span>
                    ) : (
                        <span className={styles.redirect}>
                            <p>Already have an account?</p>
                            <a
                            onClick={()=>nav("/login")}>Login</a>
                        </span>
                    )
                ) : null}





            </div>
        </div>
    );
}







