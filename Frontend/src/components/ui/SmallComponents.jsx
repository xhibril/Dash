import { FiInfo, FiEye } from "react-icons/fi";
import styles from "./SmallComponents.module.css"
import { useState } from "react";
import icon from "../../../public/favicon.svg"

export function NoDataAvailable() {
    return (
        <div className="empty">
            <FiInfo className="explinationIcon" />
            <p>No data available</p>
        </div>
    )
}


export function PasswordField({ password, setPassword, title }) {
    const [showPass, setShowPass] = useState(false);

    return (
        <label className={styles.label} > {title}
            <input className={styles.input}
                value={password}

                type={showPass ? "text" : "password"}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="•••••"

            ></input>

            <FiEye className={styles.eye}
                onClick={() => setShowPass(!showPass)} />

        </label>
    )
}


export function EmailField({ email, setEmail, title }) {
    return (
        <label className={styles.label}> {title}
            <input className={styles.input}
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="you@email.com"
            ></input>
        </label>
    )

}


export function CodeField({ code, setCode, title }) {
    return (
        <label className={styles.label}> {title}
            <input className={styles.input}
                value={code}
                onChange={(e) => setCode(e.target.value)}
                placeholder="123456"
            ></input>
        </label>
    )
}


export function BrandHeader({ title }) {
    return (
        <>
            <div className={styles.titleAndIcon}>
                <img src={icon} />
                <h1 className={styles.siteName}>DASH</h1>
            </div>
            <h1 className={styles.pageTitle}>
                {title}
            </h1>
        </>
    )
}