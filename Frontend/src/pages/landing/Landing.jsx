import styles from "./Landing.module.css";
import siteIcon from "../../../public/favicon.svg";
import { Link, useNavigate } from "react-router-dom"
import arrowDown from "../../../src/assets/pointingDownArrow.svg"
import statisticsIcon from "../../../src/assets/statisticsIcon.svg"
import folderIcon from "../../../src/assets/folder.svg"
import linkIcon from "../../../src/assets/link.svg"


export default function Landing() {
    const nav = useNavigate();

    return (
        <>
            <div className={styles.mainContainer}>
                <div className={styles.topRow}>
                    <div className={styles.brandHeader}>
                        <img src={siteIcon}></img>
                        <h1>DASH</h1>
                    </div>

                    <nav className={styles.navBar}>
                        <Link to="/">Home</Link>
                        <a href="#features">Features</a>
                        <a href="https://github.com/xhibril" target="_blank">GitHub</a>

                    </nav>

                    <div className={styles.authRow}>
                        <button className={styles.loginBtn}
                            onClick={() => nav("/login")}>Login</button>
                        <button className={styles.signUpBtn}
                            onClick={() => nav("/signup")}>Sign up</button>
                    </div>
                </div>

                <div className={styles.content}>
                    <div className={styles.leftContainer}>
                        <div className={styles.brandDesc}>
                            <h1>Links, but smarter.</h1>
                            <p> Shorten, share, and track how your links are doing with clean, easy to read stats.</p>
                            <button className={styles.tryNow}
                                onClick={() => nav("/login")}>Try now</button>
                        </div>
                    </div>



                    <div className={styles.rightContainer}>
                        <div className={styles.beforeAfterContainer}>
                            <div className={styles.beforeAfterWrapper}>

                                <p className={styles.before}>https://randomsite.com/very-long-link...</p>
                                <img src={arrowDown} />
                                <p className={styles.after}>dash.com/track123</p>

                                <p className={styles.miniOne}>+1.2k clicks</p>
                                <p className={styles.miniTwo}>38% CTR</p>
                                <p className={styles.miniThree}>Stats</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className={styles.secondContainer}>
                <h1 id="features" className={styles.featuresText}>FEATURES</h1>
                <div className={styles.features}>


                    <div className={styles.card}>
                        <img src={statisticsIcon} />
                        <h3>Track clicks</h3>
                        <p>See what’s working instantly.</p>
                    </div>
                    <div className={styles.card}>
                        <img src={linkIcon} />
                        <h3>Create short links fast</h3>
                        <p>Paste, customize, done.</p>
                    </div>
                    <div className={styles.card}>
                        <img src={folderIcon} />

                        <h3>Keep everything in one place</h3>
                        <p>All your links, clean and organized.</p>
                    </div>
                </div>
            </div>


            <div className={styles.footerContainer}>

                <footer> © 2026 Xhibril </footer>
                <a href="https://github.com/xhibril" target="_blank">GitHub</a>
                <a href="https://www.linkedin.com/in/xhibril-lleshi/" target="_blank">LinkedIn </a>
                <a href="mailto:xhibril.dev@gmail.com" target="_blank">Email</a>
            </div>
        </>
    )
}





