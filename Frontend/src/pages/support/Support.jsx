import styles from "./Support.module.css"

import { FiArrowDown, FiArrowUp } from "react-icons/fi";

import { useState } from "react";
import { HandleError } from "../../components/Utils/ErrorHandler.jsx";
import { ValidateEmail, ValidateInput } from "../../components/Utils/Validation.jsx";



export default function Support({ notify }) {
    const faqs = [{
        question: "What Is a URL Shortener?", answer: "A URL shortener turns a long link into a shorter one that’s easier to share." +
            "When someone opens the short link, it redirects them to the original website."
    },

    {
        question: "How Does a URL Shortener Work?", answer: "A URL shortener works by taking a long link, creating a short alias for it, and storing both in a database." +
            "When someone visits the short link, the service looks up the alias and redirects the user to the original URL."
    },

    {
        question: "Are Shortened Links Permanent?", answer: "Not always. Shortened links usually stay active as long as the service keeps them stored," +
            "but they can be deleted, expire, or stop working if the service shuts down."
    },

    {
        question: "Are Shortened Links Safe?", answer: "Shortened links are generally safe, but you can’t see the full" +
            "destination before clicking, so they can sometimes hide malicious websites."
    }]

    return (

        <div className={styles.supportContainer}>
            <FAQ faqs={faqs} />
            <TicketSubmit notify={notify} />
        </div>

    );
}


function FAQ({ faqs }) {

    const [activeIndex, setActiveIndex] = useState(null);

    return (

        <div className={styles.faqContainer}>
            <div className={styles.title}>
                <h1>Help & Support</h1>
                <p>Find answers to common questions or contact us directly.</p>
            </div>

            {faqs.map((faq, index) => (
                <div className={styles.question} key={index}>

                    <h2
                        onClick={() => setActiveIndex(activeIndex === index ? null : index)}>
                        {faq.question}


                        {activeIndex === index ? (
                            <FiArrowUp className={styles.arrowUp} />
                        ) :
                            <FiArrowDown className={styles.arrowDown}
                            />}
                    </h2>

                    {activeIndex === index && (
                        <p className={`${styles.answer} ${styles.show}`}>{faq.answer}</p>
                    )
                    }
                </div>
            ))}
        </div>
    );
}

function TicketSubmit({ notify }) {

    const [email, setEmail] = useState("")
    const [subject, setSubject] = useState("")
    const [message, setMessage] = useState("")

    async function submitTicket() {


    if(!email || !subject || !message) {
        notify("All fields are required", "ERROR");
        return;
    }

        const emailRes = ValidateEmail(email);

        if(emailRes !== "VALID"){
            notify(emailRes, "ERROR"); 
            return;
        }


        const subjectRes =  ValidateInput(subject, "GENERAL");

        if(subjectRes !== "VALID"){
            notify(subjectRes, "ERROR");
            return;
        }

        const messageRes = ValidateInput(message, "MESSAGE");
        if(messageRes !== "VALID"){
            notify(messageRes, "ERROR");
            return;
        }

        const res = await fetch("/api/support", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, subject, message })
        })

        if (!res.ok) {
            HandleError(res.status);
            notify("Something went wrong, please try again", "ERROR");
            return;
        }


        notify("Message sent successfully", "SUCCESS");
        setEmail("");
        setSubject("");
        setMessage("");
        return;
    }

    return (

        <div className={styles.inputContainer}>

            <div className={styles.inputWrapper}>

                <h2>Get in Touch</h2>
                <p className = {styles.supportText}>Leave your message and we'll get back to you shortly.</p>

<div className = {styles.supportTop}>
                <label className={styles.supportInput}>
                    <p>Email</p>
                    <input
                        type="email"
                        className={styles.input}
                        placeholder="you@email.com"
                        value = {email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </label>

                <label className={styles.supportInput}>
                    <p>Subject</p>
                    <input
                        className={styles.input}
                        placeholder="URL not working"
                        value = {subject}
                        onChange={(e) => setSubject(e.target.value)}
                    />
                </label>
</div>
                <label className={styles.supportInput}>
                    <p>Message</p>
                    <textarea
                        className={styles.textArea}
                        placeholder="Tell us briefly about your needs"
                        value = {message}
                        onChange={(e) => setMessage(e.target.value)}
                    />
                </label>

                <button className={styles.submit} onClick={submitTicket}>
                    Submit
                </button>

            </div>
        </div>
    )
}



