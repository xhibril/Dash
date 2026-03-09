import styles from "../css/URLShortener.module.css";
import { useState } from "react";

import { FiLink, FiPenTool } from "react-icons/fi";
import { HandleError } from "./ErrorHandler.jsx";


function URLShortener({notify}) {

    const [originalUrl, url] = useState("");
    const [alias, al] = useState("");

    async function generateNewUrl( originalUrl, shortUrl ) {


    notify("CREATED", "error");


    // if user entered an alias
    if (shortUrl?.length > 0) {

        const res = await fetch("api/url", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ originalUrl, shortUrl })
        })

        
        
        if (!res.ok) {
              HandleError(res.status);
              notify("Something went wrong, please try again", "ERROR");
              return;
            }
      
    } else {


        const res = await fetch("/api/generate/url",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({originalUrl})
        })

  
      if (!res.ok) {
              HandleError(res.status);
              notify("Something went wrong, please try again", "ERROR");
              return;
            }
    }




}






    return (
        <div className={styles.inputContainer}>

            <label className={styles.inputTitle}>
                <FiLink />
                <p>Long URL</p>
            </label>

            <input
                onChange={(e) => url(e.target.value)}
                placeholder='Paste Long URL'
            ></input>


            <label className={styles.inputTitle}>
                <FiPenTool />
                <p>Alias</p>
            </label>

            <input
                onChange={(e) => al(e.target.value)}
                placeholder='Add Alias'
            ></input>

            <p className={styles.aliasDesc}>Must be at least 5 characters</p>

            <button
            onClick={()=> generateNewUrl(originalUrl, alias)}
             className={styles.shortenBtn}

            >Shorten</button>
        </div>

    );
}

export default URLShortener;








