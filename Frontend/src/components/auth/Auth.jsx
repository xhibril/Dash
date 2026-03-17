import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Auth.module.css";
import global from "../../css/Global.module.css"

import { ValidateEmail, ValidatePassword } from "../Utils/Validation.jsx";
import { HandleError } from "../Utils/ErrorHandler.jsx";
import { PasswordField, EmailField, BrandHeader } from "../UI/SmallComponents.jsx";

export default function Auth({ mode, notify }) {

    const nav = useNavigate();

    // turn is login into a bool
    const isLogin = mode === "LOGIN";

    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [rememberMe, setRememberMe] = useState(false)
    const [isLoading, setIsLoading] = useState(false)


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




    async function submitCredentials(email, pass, rememberMe, path) {

        // save email incase user is not verified n we have to send verification email
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



        setIsLoading(true);

        try {
            const res = await fetch(path, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, pass, rememberMe })
            })


            if (!res.ok) {
                HandleError(res.status);
                const data = await res.text();
                notify(data || "Something went wrong, please try again", "ERROR");
                return;
            }


            if (isLogin) {
                window.location.href = "/dashboard";
                localStorage.removeItem("email");

            } else {
                setEmail("");
                setPassword("");
                window.location.href = "/verify/email"
            }

        } finally {
            setIsLoading(false);
        }
    }




    return (
        <div className={global.mainContainer}>

            <form className={global.inputContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    submitCredentials(email, password, rememberMe, isLogin ? "/api/login" : "/api/signup");
                }}>

                <BrandHeader title={isLogin ? "Login" : "Sign up"} />
                <EmailField title="Email" email={email} setEmail={setEmail} />
                <PasswordField title="Password" password={password} setPassword={setPassword} />


                {isLogin ? (
                        <div className={styles.remember}>
                            <input
                                type="checkbox"
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                            <p>Remember me</p>

                        </div>
                    ) : null
                }


                <button className={global.submit}
                    type="submit"
                    disabled={isLoading}>
                    {isLoading ? "Loading..." : (isLogin ? "Login" : "Sign up")}
                </button>


                {isLogin ? (
                        <span className={styles.forgot} onClick={() => nav("/forget")}>
                            Forgot your password?
                        </span>
                    ) : null
                }

                {isLogin ? (
                    <span className={styles.redirect}>
                        <p>Don't have an account?</p>
                        <a
                            onClick={() => nav("/signup")}>Sign up
                        </a>
                    </span>
                ) : (
                    <span className={styles.redirect}>
                        <p>Already have an account?</p>
                        <a
                            onClick={() => nav("/login")}>Login
                        </a>
                    </span>
                )}
            </form>
        </div>
    );
}







