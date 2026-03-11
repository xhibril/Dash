import "../css/Notification.css"

import { FiBell } from "react-icons/fi";
import { useEffect, useState } from "react";
export default function Notification({ message, type, key }) {


    const [visible, setVisible] = useState(false);




    useEffect(()=>{
        if(message !== ""){
            setVisible(false)


            setTimeout(()=>{
                setVisible(true);
            }, 10)

        } else {
            setVisible(false);
        }
        
    }, [message])



    const name = `notification ${visible === true ? "show" : ""}`;
    const bellName = `notificationBell ${type === "SUCCESS" ? "success" : "error"}`;


    return (
        
        <div className={name}>
        <FiBell className={bellName}/>
        {message}
        </div>
    );

}