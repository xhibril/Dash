import styles from "./UpdateEmail.module.css"

import { EmailField, PasswordField, CodeField } from "../../../components/UI/SmallComponents.jsx"
import { ValidateEmail } from "../../../components/Utils/Validation.jsx"
import { ValidatePassword } from "../../../components/Utils/Validation.jsx"
import { ValidateCode } from "../../../components/Utils/Validation.jsx"
import { HandleError } from "../../../components/Utils/ErrorHandler.jsx"

import icon from "../../../../public/favicon.png"
import { useState } from "react"
import { useNavigate } from "react-router-dom"
export default function UpdateEmail({ notify }) {

    const [pendingEmail, setPendingEmail] = useState("")
    const [password, setPassword] = useState("")
    const [code, setCode] = useState("")
    const [step, setStep] = useState("INIT")
    const [oldEmail, setOldEmail] = useState("");


    const nav = useNavigate();


    async function update() {



        if (step === "INIT") {

            const validateEmail = ValidateEmail(pendingEmail);

            if (validateEmail !== "VALID") {
                notify(validateEmail, "ERROR"); return;
            }

            const passwordRes = ValidatePassword(password);

            if (passwordRes !== "VALID") {
                notify(passwordRes, "ERROR"); return;
            }

            const emailRes = await fetch("/api/update-email/request", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ pendingEmail, password })
            })


       


            if (!emailRes.ok) {
              
                HandleError(emailRes.status);
                  const data = await emailRes.json().catch(()=> ({}));
                notify(data.message || "Something went wrong, please try again", "ERROR");
                return;
            }


            notify("Verification code sent", "SUCCESS");
            setStep("VERIFY");

        }



        if (step === "VERIFY") {

            const validateCode = ValidateCode(code);


            if (validateCode !== "VALID") {
                notify(validateCode, "ERROR"); return;
            }


            const codeRes = await fetch("/api/update-email/verify", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ code })
            })



            if (!codeRes.ok) {
                
                    HandleError(codeRes.status);
                      const codeResData = await codeRes.json().catch(()=> ({}));
                    notify(codeResData.message || "Something went wrong, please try again", "ERROR");
                    return;
                
            }


           const codeResData = await codeRes.json();
         const token = codeResData.resetToken;
setResetToken(token);


            // change password if code is correct
            const changeRes = await fetch("/api/update-email/change", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ pendingEmail, resetToken: token })
            })

        


            if (!changeRes.ok) {
                HandleError(changeRes.status);
                const changeResData = await changeRes.json().catch(()=> ({}));
                notify(changeResData.message || "Something went wrong, please try again", "ERROR");
                return;
            }


           const changeResData = await changeRes.json();

            setOldEmail(changeResData.oldEmail);
            setStep("CHANGED");
            return;
        }



        if (step === "CHANGED") {
            notify("Email updated successfully", "SUCCESS");
            return;
        }



    }




    return (

        <div className={styles.mainContainer}>



            <form className={styles.inputContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    update();
                }}>


                <div className={styles.titleAndIcon}>
                    <img src={icon}></img>
                    <h1 className={styles.siteName}>DASH</h1>
                </div>
                <h1 className={styles.pageTitle}>
                    Change Email
                </h1>




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
                            onClick={() => nav("/dashboard")}
                            className={styles.submit}>
                            Back
                        </button>
                    ) :



                        <button type="submit"
                            className={styles.submit}>
                            Continue
                        </button>

                }



            </form>
        </div>
    )
}