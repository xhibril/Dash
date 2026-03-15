import styles from "./UpdatePassword.module.css";
import icon from "../../../../public/favicon.png"
import { PasswordField } from "../../../components/UI/SmallComponents.jsx";
import { ValidatePassword } from "../../../components/utils/Validation.jsx";

import { useState } from "react";


export default function UpdatePassword({ notify }) {

    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");


    async function update() {


        const oldPassRes = ValidatePassword(oldPassword);

        if (oldPassRes !== "VALID") {
            notify(oldPassRes, "ERROR"); return;
        }

        const newPassRes = ValidatePassword(newPassword);

        if (newPassRes !== "VALID") {
            notify(newPassRes, "ERROR"); return;
        }


        const confirmPassRes = ValidatePassword(confirmPassword)

        if (confirmPassRes !== "VALID") {
            notify(newPassRes, "ERROR"); return;
        }


   if (newPassword !== confirmPassword) {
    notify("Passwords do not match", "ERROR");
    return;
}



        const res = await fetch("/api/update/password", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ oldPassword, newPassword, confirmPassword })
        })


        if (!res.ok) {
            const data = await res.text();
            notify(data || "Could not update password", "ERROR");
            return;
        }

        notify("Password updated successfully", "SUCCESS");
        setOldPassword("")
        setNewPassword("")
        setConfirmPassword("")
    }




    return (


        <div className={styles.mainContainer}>

            <form className={styles.inputContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    update()}}>

                <div className={styles.titleAndIcon}>
                    <img src={icon}></img>
                    <h1 className={styles.siteName}>DASH</h1>
                </div>

                <h1 className={styles.pageTitle}>
                    Update Password
                </h1>

                <PasswordField
                    password={oldPassword}
                    setPassword={setOldPassword}
                    title={"Old Password"} />

                <PasswordField
                    password={newPassword}
                    setPassword={setNewPassword}
                    title={"New Password"} />

                <PasswordField
                    password={confirmPassword}
                    setPassword={setConfirmPassword}
                    title={"Confirm Password"} />


                <button type="submit"
                    className={styles.submit}>
                    Continue
                </button>

            </form>
        </div>



    )




}