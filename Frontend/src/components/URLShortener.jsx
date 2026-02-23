import styles from "../css/URLShortener.module.css";
import { useState } from "react";

import { FiLink, FiPenTool } from "react-icons/fi";



function URLShortener() {

    const [originalUrl, url] = useState("");
    const [alias, al] = useState("");



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


async function generateNewUrl( originalUrl, shortUrl ) {


    // if user entered an alias
    if (shortUrl?.length > 0) {

        const response = await fetch("api/url", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ originalUrl, shortUrl })
        })

        if(!response.ok){
            console.log("error, already exists")
        }
    } else {


        const res = await fetch("/api/generate/url",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({originalUrl})
        })

        if(!res.ok){
            console.log("error, already exists")
        }

        const data = await res.text();
        console.log(data);





    }




}






