import { FiInfo } from "react-icons/fi";
import styles from "./SmallComponents.module.css"
import { useState } from "react";
import { FiEye } from "react-icons/fi";
export function NoDataAvailable() {
    return (
        <div className="empty">
            <FiInfo className="explinationIcon" />
            <p>No data available</p>
        </div>
    )
}


export function PasswordField({ password, setPassword, title}) {

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