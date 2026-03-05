import styles from "../css/Support.module.css"

import { FiArrowDown, FiArrowUp } from "react-icons/fi";

import {useState } from "react";


export default function Support() {


    return (

        <div className={styles.supportContainer}>
            <FAQ />
            <TicketSubmit />
        </div>

    );
}


function FAQ() {
    return (

        <div className={styles.faqContainer}>
            <div className={styles.title}>
                <h1>Help & Support</h1>
                <p>Find answers to common questions or contact us directly.</p>
            </div>

            <span className={styles.question}>
                <h2>What Is a URL Shortener?
                    <FiArrowDown className={styles.arrowDown} />
                    <FiArrowUp className={styles.arrowUp} />
                </h2>
                <p className={styles.answer}>Shortens links</p>
            </span>

            <span className={styles.question}>
                <h2>How Does a URL Shortener Work?
                    <FiArrowDown className={styles.arrowDown} />
                    <FiArrowUp className={styles.arrowUp} />
                </h2>
                <p className={styles.answer}>Shortens links</p>
            </span>


            <span className={styles.question}>
                <h2>Are Shortened Links Permanent?
                    <FiArrowDown className={styles.arrowDown} />
                    <FiArrowUp className={styles.arrowUp} />
                </h2>
                <p className={styles.answer}>Shortens links</p>

            </span>

            <span className={styles.question}>
                <h2>Are Shortened Links Safe?
                    <FiArrowDown className={styles.arrowDown} />
                    <FiArrowUp className={styles.arrowUp} />
                </h2>
                <p className={styles.answer}>Shortens links</p>
            </span>

        </div>

    );
}

function TicketSubmit() {

    const [email, setEmail] = useState("")
    const [subject, setSubject] = useState("")
    const [message, setMessage] = useState("")

    async function submitTicket() {

        const res = await fetch("/api/support", {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, subject, message })
        })

        if (!res.ok) {
            console.log("error submitting ticket")
        }
    }

    return (

        <div className={styles.inputContainer}>

            <div className={styles.inputWrapper}>

                <label className={styles.supportInput}>
                    <p>Email:</p>
                    <input
                        type="email"
                        className={styles.input}
                        placeholder="Example@email.com"
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </label>

                <label className={styles.supportInput}>
                    <p>Subject:</p>
                    <input
                        className={styles.input}
                        placeholder="URL not working"
                        onChange={(e) => setSubject(e.target.value)}
                    />
                </label>

                <label className={styles.supportInput}>
                    <p>Message:</p>
                    <textarea
                        className={styles.textArea}
                        onChange={(e) => setMessage(e.target.value)}
                    />
                </label>

            </div>

            <button className={styles.submit} onClick={submitTicket}>
                Submit
            </button>

        </div>
    )
}



