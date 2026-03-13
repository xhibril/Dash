import styles from "./Notification.module.css"
import { FiBell } from "react-icons/fi";
import { useEffect, useState } from "react";

export default function Notification({ message, type, key }) {

    const [visible, setVisible] = useState(false);

    useEffect(() => {
        if (message !== "") {
            setVisible(false)

            setTimeout(() => {
                setVisible(true);
            }, 10)

        } else {
            setVisible(false);
        }

    }, [message])

    return (
        <div className={`${styles.notification}  ${visible ? styles.show : ""}`}>
            <FiBell className={`${styles.bellIcon} ${type === "SUCCESS" ? styles.success : styles.error}`} />
            {message}
        </div>
    );

}