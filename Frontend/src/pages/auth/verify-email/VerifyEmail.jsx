import styles from "./VerifyEmail.module.css";
import global from "../../../css/Global.module.css";
import { FiMail } from 'react-icons/fi';
import { useState } from "react";
import apiFetch from "../../../components/utils/Api";
import { useNavigate } from "react-router-dom";

export default function VerifyEmail({ notify }) {

    const [isLoading, setIsLoading] = useState(false)
    const email = localStorage.getItem("email");
    const nav = useNavigate();

    async function resendEmail() {

        if (!email) {
            notify("No email found", "ERROR");
            return;
        }

        setIsLoading(true)
        try {
            const res = await apiFetch(
                "/api/email/resend", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email })
            }, nav, notify);

            if (!res) return;

            if (!res.ok) {
                notify("Failed to send verification email, please try again", "ERROR");
                return;
            }

            notify("Verification email sent", "SUCCESS");
            return;

        } catch (err) {
            notify("Something went wrong, please try again", "ERROR");
        } finally {
            setIsLoading(false);
        }
    }

    return (

        <div className={global.mainContainer}>
            <div className={styles.verifyContainer}>
                <h2 className={styles.title}>Verify your Email</h2>

                <p>Activation link has been sent to the e-mail address you provided</p>
                <FiMail className={styles.mail} />
                <p>Didn't get the email?</p>

                <a className={styles.resend}

                    onClick={() => {
                        if (isLoading) return;
                        resendEmail()
                    }}

                >{isLoading ? "Sending..." : "Send it again"}</a>
            </div>
        </div>

    );
}