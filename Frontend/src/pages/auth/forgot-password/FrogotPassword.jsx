import global from "../../../css/Global.module.css";
import { useState } from "react";
import { ValidateEmail, ValidateCode, ValidatePassword } from "../../../components/utils/Validation.jsx";
import { PasswordField, EmailField, CodeField, BrandHeader } from "../../../components/ui/SmallComponents.jsx";
import apiFetch from "../../../components/utils/Api.jsx";
import { useNavigate } from "react-router-dom";

export default function ForgotPassword({ notify }) {

    const jsonHeaders = { "Content-Type": "application/json" };

    const [step, setStep] = useState("EMAIL")
    const [code, setCode] = useState("")
    const [email, setEmail] = useState("")
    const [resetToken, setResetToken] = useState("")
    const [newPassword, setNewPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [isLoading, setIsLoading] = useState(false);
    const nav = useNavigate();


    async function forgotPassword() {

        if (step === "EMAIL") {
            const emailRes = ValidateEmail(email)

            if (emailRes !== "VALID") {
                notify(emailRes, "ERROR");
                return;
            }

            setIsLoading(true);
            try {
                const res = await apiFetch(
                    "/api/password/reset-request", {
                    method: "POST",
                    headers: jsonHeaders,
                    body: JSON.stringify({ email })
                }, nav, notify)
                if (!res) return;


                if (!res.ok) {
                    const data = await res.json();
                    notify(data.message || "Something went wrong, please try again", "ERROR")
                    return;
                }

                notify("Verification code sent", "SUCCESS");
                setStep("VERIFY");
                return;

            } catch (err) {
                notify("Something went wrong, please try again", "ERROR");
            } finally {
                setIsLoading(false);
            }
        }


        if (step === "VERIFY") {
            const codeRes = ValidateCode(code, 6);

            if (codeRes !== "VALID") {
                notify(codeRes, "ERROR");
                return;
            }

            setIsLoading(true);
            try {
                const res = await apiFetch(
                    "/api/password/reset/verify", {
                    method: "POST",
                    headers: jsonHeaders,
                    body: JSON.stringify({ email, code })
                }, nav, notify)
                if (!res) return;


                if (!res.ok) {
                    const data = await res.json();
                    notify(data.message || "Something went wrong, please try again", "ERROR");
                    return;
                }

                const data = await res.json();
                setResetToken(data.resetToken);
                setCode("");
                notify("Verification successful", "SUCCESS");
                setStep("RESET");
                return;
            } catch (err) {
                notify("Something went wrong, please try again", "ERROR");
            } finally {
                setIsLoading(false);
            }
        }


        if (step === "RESET") {
            const passRes = ValidatePassword(confirmPassword);

            if (passRes !== "VALID") {
                notify(passRes, "ERROR");
                return;
            }

            if (newPassword !== confirmPassword) {
                notify("Passwords do not match", "ERROR");
                return;
            }

            setIsLoading(true);

            try {
                const res = await apiFetch(
                    "/api/password/reset/reset", {
                    method: "POST",
                    headers: jsonHeaders,
                    body: JSON.stringify({ email, newPassword, resetToken })
                }, nav, notify)
                if (!res) return;


                if (!res.ok) {
                    const data = await res.json();
                    notify(data.message || "Something went wrong, please try again", "ERROR");
                    return;
                }

                setNewPassword("")
                setConfirmPassword("")
                notify("Password changed successfully", "SUCCESS");
                return;
            } catch (err) {
                notify("Something went wrong, please try again", "ERROR");
            } finally {
                setIsLoading(false);
            }
        }
    }



    return (
        <div className={global.mainContainer}>
            <form className={global.inputContainer}
                onSubmit={(e) => {
                    e.preventDefault();
                    forgotPassword();
                }}>

                <BrandHeader title="Forgot Password"></BrandHeader>

                {step === "EMAIL" && (
                    <EmailField title="Email" email={email} setEmail={setEmail} />
                )}

                {step === "VERIFY" && (
                    <CodeField title="6-digit code" code={code} setCode={setCode} />
                )}


                {step === "RESET" && (
                    <>
                        <PasswordField title="New Password" password={newPassword} setPassword={setNewPassword} />
                        <PasswordField title="Confirm New Password" password={confirmPassword} setPassword={setConfirmPassword} />
                    </>
                )}

                <button className={global.submit}
                    type="submit"
                    disabled={isLoading}>
                    {isLoading ? "Loading..." : "Continue"}
                </button>
            </form>
        </div>
    );

}

