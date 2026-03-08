import "../css/Notification.css"

import { FiBell } from "react-icons/fi";
export default function Notification({ message, type }) {

    const name = `notification ${message !== "" ? "show" : ""}`;
    const bellName = `notificationBell ${type === "SUCCESS" ? "success" : "error"}`;


    return (
        <div className={name}>
            <FiBell className={bellName}/>{message}</div>
    );

}