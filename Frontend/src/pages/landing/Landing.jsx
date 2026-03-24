import styles from "./Landing.module.css";
import siteIcon from "../../../public/favicon.svg";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom"
import arrowDown from "../../../src/assets/pointingDownArrow.svg"
import statisticsIcon from "../../../src/assets/statisticsIcon.svg"

export default function Landing() {

    const [text, setText] = useState("");
    // two i's cuz reacts strict mode skips
    const textToDisplay = "Liinks, but smarter.";

    const nav = useNavigate();




    useEffect(() => {
        let i = 0;
        const interval = setInterval(() => {
            if (i >= textToDisplay.length - 1) {
                clearInterval(interval);
                return;
            }
            setText(prev => prev + textToDisplay[i]);
            i++;
        }, 100);

        return () => clearInterval(interval);
    }, []);


    return (
        <>
            <div className={styles.mainContainer}>

                <div className={styles.leftContainer}>

                    <div className={styles.topRow}>
                        <div className={styles.brandHeader}>
                            <img src={siteIcon}></img>
                            <h1>DASH</h1>
                        </div>

                        <nav className={styles.navBar}>
                            <Link to="/">Home</Link>
                            <Link to="/">Features</Link>
                            <a href="https://github.com/xhibril" target="_blank">GitHub</a>

                        </nav>
                    </div>


                    <div className={styles.brandDesc}>
                        <h1>{text}</h1>
                        <p> Shorten, share, and track how your links are doing with clean, easy to read stats.</p>
                        <button className={styles.tryNow}>Try now</button>
                    </div>


                </div>



                <div className={styles.rightContainer}>
                    <div className={styles.authRow}>
                        <button className={styles.loginBtn}
                            onClick={() => nav("/login")}>Login</button>
                        <button className={styles.signUpBtn}
                            onClick={() => nav("/signup")}>Sign up</button>
                    </div>


                    <div className={styles.beforeAfterContainer}>


                        <div className={styles.beforeAfterWrapper}>

                            <p className={styles.before}>https://randomsite.com/very-long-link...</p>
                            <img src={arrowDown} />
                            <p className={styles.after}>dash.com/track123</p>
                        </div>


                        <p className={styles.miniOne}>+1.2k clicks</p>
                        <p className={styles.miniTwo}>38% CTR</p>
                        <p className={styles.miniThree}>Active now</p>
                    </div>

                </div>
            </div>


            <div className={styles.secondContainer}>
                <h1 className = {styles.featuresText}>FEATURES</h1>
                <div className={styles.features}>


                    <div className={styles.card}></div>
                    <div className={styles.card}></div>
                    <div className={styles.card}></div>
                </div>
            </div>
        </>
    )
}





