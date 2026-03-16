import styles from "./DeleteAccount.module.css"
import { FiAlertTriangle, FiEye } from "react-icons/fi";

import { ValidatePassword } from "../../../components/utils/Validation.jsx"

import { HandleError } from "../../../components/Utils/ErrorHandler.jsx";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function DeleteAccount({ notify }) {

    const nav = useNavigate();
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);


    async function deleteAccount() {
        const passwordRes = ValidatePassword(password);

        if (passwordRes !== "VALID") {
            notify(passwordRes, "ERROR");
            return;
        }


        const res = await fetch("/api/delete/account", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ password })
        });


        if (!res.ok) {
            HandleError(res.status);
            const data = await res.text();
            notify(data || "Something went wrong, please try again", "ERROR");
            return;
        }


        window.location.href = "/";
    }

    return (
        <div className={styles.mainContainer}>

            <form className={styles.deleteAccountContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    deleteAccount()
                }}>
                <FiAlertTriangle className={styles.deleteAccountIcon} />

                <p className={styles.warningTop}>This action cannot be undone</p>
                <p className={styles.warningBottom}> Deleting your account will permanently remove all your data.</p>


                <label className={styles.label}>
                    <p className={styles.passwordText}>Password</p>


                    <input
                        className={styles.input}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        type={showPassword ? "text" : "password"}
                        placeholder="••••">

                    </input>

                    <FiEye className={styles.showPasswordIcon}
                        onClick={() => setShowPassword(!showPassword)} />
                </label>

                <button className={styles.deleteAccount}
                    type="submit">Delete account
                </button>

                <button className={styles.cancelDeletion}
                    type="button"
                    onClick={() => nav("/dashboard")}>
                    Cancel
                </button>

            </form>
        </div>
    );



}