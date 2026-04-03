import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Auth.module.css";
import global from "../../css/Global.module.css"

import { ValidateEmail, ValidatePassword } from "../utils/Validation.jsx";
import { PasswordField, EmailField, BrandHeader } from "../ui/SmallComponents.jsx";
import apiFetch from "../utils/Api.jsx";

export default function Auth({ mode, notify, setIsAuth }) {

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




    async function submitCredentials(path) {

        // save email incase user is not verified n we have to send verification email
        localStorage.setItem("email", email);

        const emailRes = ValidateEmail(email);

        if (emailRes !== "VALID") {
            notify(emailRes, "ERROR");
            return;
        }

        const passwordRes = ValidatePassword(password);

        if (passwordRes !== "VALID") {
            notify(passwordRes, "ERROR");
            return;
        }


        setIsLoading(true);

        try {
            const res = await apiFetch(
                path,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password, rememberMe })
                },
                nav, notify
            );
            if (!res) return;

            if (res.status === 403) return;

            if (!res.ok) {
                const data = await res.json();
                notify(data.message || "Something went wrong, please try again", "ERROR");
                return;
            }


            if (isLogin) {
                localStorage.removeItem("email");
                setIsAuth(true);
                nav("/dashboard");
            } else {
                setEmail("");
                setPassword("");
                nav("/verify/email");
            }
        }
        catch (err) {
            notify("Something went wrong. Please try again.", "ERROR");
        } finally {
            setIsLoading(false);
        }
    }


    return (
        <div className={global.mainContainer}>

            <form className={global.inputContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    submitCredentials(isLogin ? "/api/login" : "/api/signup");
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







