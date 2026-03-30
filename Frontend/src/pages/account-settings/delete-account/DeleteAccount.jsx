import styles from "./DeleteAccount.module.css"
import global from "../../../css/Global.module.css"
import { PasswordField } from "../../../components/ui/SmallComponents.jsx"
import { FiAlertTriangle } from "react-icons/fi"
import { ValidatePassword } from "../../../components/utils/Validation.jsx"
import apiFetch from "../../../components/utils/Api.jsx"
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function DeleteAccount({ notify }) {

    const nav = useNavigate();
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);


    async function deleteAccount() {
        const passwordRes = ValidatePassword(password);

        if (passwordRes !== "VALID") {
            notify(passwordRes, "ERROR");
            return;
        }

        setIsLoading(true);
        try {
            const res = await apiFetch(
                "/api/delete/account",
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ password })
                },
                nav, notify
            );

            if (!res) return;

            if (!res.ok) {
                const data = await res.text();
                notify(data || "Something went wrong, please try again", "ERROR");
                return;
            }

            nav("/");
        } catch (err) {
            notify("Something went wrong, please try again", "ERROR");

        } finally {
            setIsLoading(false);
        }
    }

    return (
        <div className={global.mainContainer}>
            <form className={styles.deleteAccountContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    deleteAccount()
                }}>
                <FiAlertTriangle className={styles.deleteAccountIcon} />

                <p className={styles.warningTop}>This action cannot be undone</p>
                <p className={styles.warningBottom}> Deleting your account will permanently remove all your data.</p>

                <PasswordField title="Password" password={password} setPassword={setPassword} />

                <button className={styles.deleteAccount}
                    type="submit"
                    disabled={isLoading}>
                    {isLoading ? "Deleting..." : "Delete account"}
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