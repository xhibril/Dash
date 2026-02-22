import styles from "../css/Support.module.css"

import { FiArrowDown, FiArrowUp } from "react-icons/fi";

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
            <div className = {styles.title}>
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


    return (

        <div className={styles.inputContainer}>

            <div className={styles.inputWrapper}>

                <label className={styles.supportInput}>
                    <p>Email:</p>
                    <input type="email"
                        className={styles.input}
                        placeholder="Example@email.com"
                    ></input>
                </label>



                <label className={styles.supportInput}>
                    <p>Subject:</p>

                    <input
                        className={styles.input}
                        placeholder="URL not working"
                    ></input>
                </label>


                <label className={styles.supportInput}>
                    <p>Message:</p>

                    <textarea
                        className={styles.textArea}
                    ></textarea>
                </label>




            </div>


<button className = {styles.submit}>Submit</button>
        </div >


    )




}


