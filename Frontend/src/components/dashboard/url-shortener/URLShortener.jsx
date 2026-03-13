import styles from "./URLShortener.module.css";
import { useState } from "react";

import { FiLink, FiPenTool } from "react-icons/fi";
import { HandleError } from "../../Utils/ErrorHandler.jsx";
import { ValidateAlias, ValidateURL } from "../../Utils/Validation.jsx";



export default function URLShortener({notify, setUrls, setMostPopular, mostPopular }) {

    const [originalUrl, setOriginalUrl] = useState("");
    const [alias, setAlias] = useState("");
    const [loading, setLoading] = useState(false);

    async function generateNewUrl() {
        if (loading) return;

        const urlRes = ValidateURL(originalUrl);
        if (urlRes !== "VALID") {
            notify(urlRes, "ERROR");
            return;
        }

        const aliasRes = ValidateAlias(alias);

        if (aliasRes !== "VALID") {
            notify(aliasRes, "ERROR");
            return;
        }

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

            notify(data.message || "Unable to create URL. Please try again later", "ERROR");
            return;
        }

        setUrls(prev => [...prev, data]);

        if (!mostPopular) {
            setMostPopular(data);
        }

        setAlias("");
        setOriginalUrl("");

        notify("URL Successfully created", "SUCCESS");
        setLoading(false);
    }



    return (

        <div className={styles.inputContainer}>

            <label className={styles.inputTitle}>
                <FiLink />
                <p>Long URL</p>
            </label>

            <input className={styles.inputField}
                onChange={(e) => setOriginalUrl(e.target.value)}
                placeholder='Paste Long URL'
                value={originalUrl}

            ></input>


            <label className={styles.inputTitle}>
                <FiPenTool />
                <p>Alias</p>
            </label>

            <input className={styles.inputField}
                onChange={(e) => setAlias(e.target.value)}
                value={alias}
                placeholder='Add Alias'

            ></input>

            <p className={styles.aliasDesc}>Must be at least 5 characters</p>

            <button
                onClick={() => generateNewUrl()}
                className={`${styles.shortenBtn} ${loading ? styles.disabled : ""}`}
                disabled={loading}

            >{loading ? "Generating..." : "Shorten"}</button>

        </div>

    );
}