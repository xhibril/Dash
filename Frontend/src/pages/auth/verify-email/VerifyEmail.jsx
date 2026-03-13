import styles from "./VerifyEmail.module.css";
import { FiMail } from 'react-icons/fi';

export default function VerifyEmail({notify}){

    const email = localStorage.getItem("email");


    async function resendEmail(){

        const res = await fetch("/api/email/resend", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email})
        });


        if(!res.ok){
           notify("Failed to send verification email. Please try again", "ERROR");
           return;
        }

        notify("Verification email sent", "SUCCESS");
    }

    return (

        <div className = {styles.mainContainer}>
            <div className = {styles.verifyContainer}>
               <h2 className = {styles.title}>Verify your Email</h2>

            <p>Activation link has been sent to the e-mail address you provided</p>
            <FiMail className = {styles.mail}/>
            <p>Didn't get the email?</p>

            <a className = {styles.resend}
            
            onClick = {() => resendEmail()}
            
            >Send it again</a>
            </div>
        </div>

    );
}