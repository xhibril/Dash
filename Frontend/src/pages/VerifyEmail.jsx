import styles from "../css/Verify.module.css";
import { FiEdit, FiMail } from 'react-icons/fi';

export default function VerifyEmail(){

    const email = localStorage.getItem("email");


    async function resendEmail(){

        const res = await fetch("/api/email/resend", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email})
        });


        if(!res.ok){
            console.log("sum went wrong resending verification email");
            return;
        }

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