import styles from "./UpdateEmail.module.css"
import global from "../../../css/Global.module.css"
import { EmailField, PasswordField, CodeField, BrandHeader } from "../../../components/uI/SmallComponents.jsx"
import { ValidateEmail, ValidatePassword, ValidateCode } from "../../../components/utils/Validation.jsx"
import apiFetch from "../../../components/utils/Api.jsx"
import { useState } from "react"
import { useNavigate } from "react-router-dom"


export default function UpdateEmail({ notify }) {

    const [pendingEmail, setPendingEmail] = useState("")
    const [password, setPassword] = useState("")
    const [code, setCode] = useState("")
    const [step, setStep] = useState("INIT")
    const [oldEmail, setOldEmail] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const jsonHeaders = { "Content-Type": "application/json" };
    const nav = useNavigate();


    async function update() {
        if (step === "INIT") {
            const emailValidation = ValidateEmail(pendingEmail);

            if (emailValidation !== "VALID") {
                notify(emailValidation, "ERROR"); return;
            }

            const passwordValidation = ValidatePassword(password);

            if (passwordValidation !== "VALID") {
                notify(passwordValidation, "ERROR"); return;
            }

            setIsLoading(true);

            try {
                const emailRes = await apiFetch(
                    "/api/update-email/request", {
                    method: "POST",
                    headers: jsonHeaders,
                    body: JSON.stringify({ pendingEmail, password })
                }, nav
                )

                if (!emailRes.ok) {
                    const data = await emailRes.json();
                    notify(data.message || "Something went wrong, please try again", "ERROR");
                    return;
                }

                notify("Verification code sent", "SUCCESS");
                setStep("VERIFY");
            } catch (err){
                notify("Something went wrong, please try again", "ERROR");
            } finally {
                setIsLoading(false);
            }
            return;
        }



        if (step === "VERIFY") {
            const codeValidation = ValidateCode(code);

            if (codeValidation !== "VALID") {
                notify(codeValidation, "ERROR"); return;
            }

            setIsLoading(true);

            try {
                const codeRes = await apiFetch(
                    "/api/update-email/verify",
                    {
                        method: "POST",
                        headers: jsonHeaders,
                        body: JSON.stringify({ code })
                    },
                    nav
                )

                const codeResData = await codeRes.json();

                if (!codeRes.ok) {
                    notify(codeResData.message || "Something went wrong, please try again", "ERROR");
                    return;
                }
                const token = codeResData.resetToken;

                // change password if code is correct
                const changeRes = await apiFetch(
                    "/api/update-email/change", {
                    method: "POST",
                    headers: jsonHeaders,
                    body: JSON.stringify({ pendingEmail, resetToken: token })
                }, nav)

                const changeResData = await changeRes.json();

                if (!changeRes.ok) {
                    notify(changeResData.message || "Something went wrong, please try again", "ERROR");
                    return;
                }

                setOldEmail(changeResData.oldEmail);
                setPassword("")
                setCode("");
                notify("Email updated successfully", "SUCCESS");
                setStep("CHANGED");
            } catch {
                notify("Something went wrong, please try again", "ERROR");
            } finally {
                setIsLoading(false);
            }
            return;
        }
    }


    return (

        <div className={global.mainContainer}>
            <form className={global.inputContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    update();
                }}>

                <BrandHeader title="Update Email" />


                {step === "INIT" &&
                    <>
                        <EmailField email={pendingEmail} setEmail={setPendingEmail} title="New Email" />
                        <PasswordField password={password} setPassword={setPassword} title="Password" />
                    </>
                }

                {step === "VERIFY" &&
                    <>
                        <CodeField code={code} setCode={setCode} title="6-digit code" />
                    </>
                }

                {step === "CHANGED" &&

                    <>
                        <p className={styles.emailUpdatedText}>Email updated successfully</p>
                        <span className={styles.span}>
                            <p className={styles.newEmail}>New Email </p>
                            <p>{pendingEmail}</p>
                        </span>

                        <span className={styles.span}>
                            <p className={styles.oldEmail}>Old Email </p>
                            <p>{oldEmail}</p>
                        </span>

                    </>
                }


                {
                    step === "CHANGED" ? (
                        <button type="button"
                            disabled={isLoading}
                            onClick={() => nav("/dashboard")}
                            className={global.submit}>
                            Back
                        </button>
                    ) :
                        <button type="submit"
                            disabled={isLoading}
                            className={global.submit}>
                            {isLoading ? "Loading..." : "Continue"}
                        </button>
                }
            </form>
        </div>
    )
}