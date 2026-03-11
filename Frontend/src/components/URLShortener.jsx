import styles from "../css/URLShortener.module.css";
import { useState } from "react";

import { FiLink, FiPenTool, FiLoader } from "react-icons/fi";
import { HandleError } from "./ErrorHandler.jsx";



export default function URLShortener({ notify, setUrls }) {

    const [originalUrl, setOriginalUrl] = useState("");
    const [alias, setAlias] = useState("");
    const [loading, setLoading] = useState(false);

    async function generateNewUrl(originalUrl, alias) {


        setLoading(true);

        const res = await fetch("/api/generate/url", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ originalUrl, alias })
        })

        const data = await res.json();

        if (!res.ok) {
            HandleError(res.status);
            setLoading(false);

            if (data.message !== "") {
                notify(data, "ERROR");
                return;
            }

            notify("Unable to create URL. Please try again later", "ERROR");
            return;
        }



setUrls(prev => [...prev, data]);


        notify("URL Successfully created", "SUCCESS");

        setLoading(false);
        setOriginalUrl("");
        setAlias("");



    }






    return (

        <div className={styles.inputContainer}>

            <label className={styles.inputTitle}>
                <FiLink />
                <p>Long URL</p>
            </label>

            <input
                onChange={(e) => setOriginalUrl(e.target.value)}
                placeholder='Paste Long URL'
                values={originalUrl}
            ></input>


            <label className={styles.inputTitle}>
                <FiPenTool />
                <p>Alias</p>
            </label>

            <input
                onChange={(e) => setAlias(e.target.value)}
                placeholder='Add Alias'
                values={alias}
            ></input>

            <p className={styles.aliasDesc}>Must be at least 5 characters</p>

            <button
                onClick={() => generateNewUrl(originalUrl, alias)}
                className={`${styles.shortenBtn} ${loading ? styles.disabled : ""}`}


                disabled={loading}


            >{loading ? "Generating..." : "Shorten"}</button>

        </div>

    );
}










