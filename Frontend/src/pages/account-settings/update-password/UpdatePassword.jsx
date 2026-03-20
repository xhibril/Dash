import global from "../../../css/Global.module.css";
import { PasswordField, BrandHeader } from "../../../components/UI/SmallComponents.jsx";
import { ValidatePassword } from "../../../components/utils/Validation.jsx";
import apiFetch from "../../../components/utils/Api.jsx";
import { useState } from "react";
import { useNavigate } from "react-router-dom";


export default function UpdatePassword({ notify }) {

    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const nav = useNavigate();


    async function update() {
        const validateOldPass = ValidatePassword(oldPassword);

        if (validateOldPass !== "VALID") {
            notify(validateOldPass, "ERROR"); return;
        }

        const validateNewPass = ValidatePassword(newPassword);

        if (validateNewPass !== "VALID") {
            notify(validateNewPass, "ERROR"); return;
        }

        const validateConfirmPass = ValidatePassword(confirmPassword)

        if (validateConfirmPass !== "VALID") {
            notify(validateConfirmPass, "ERROR"); return;
        }


        if (newPassword !== confirmPassword) {
            notify("Passwords do not match", "ERROR");
            return;
        }


        if(oldPassword === newPassword){
            notify("New password must be different from the old password", "ERROR");
            return;
        }


        setIsLoading(true);

        try {
            const res = await apiFetch(
                "/api/update/password", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ oldPassword, newPassword, confirmPassword })
            }, nav)


            if (!res.ok) {
                const data = await res.text();
                notify(data || "Could not update password", "ERROR");
                return;
            }

            notify("Password updated successfully", "SUCCESS");
            setOldPassword("");
            setNewPassword("");
            setConfirmPassword("");

        }catch (err){
            notify("Something went wrong, please try again", "ERROR");
        } finally {
            setIsLoading(false);
        }
    }




    return (
        <div className={global.mainContainer}>

            <form className={global.inputContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    update()
                }}>

              
              <BrandHeader title = "Update Password"/>

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
                    className={global.submit}
                    disabled={isLoading}>

                    {isLoading ? "Loading..." : "Continue"}
                </button>
            </form>
        </div>
    )
}