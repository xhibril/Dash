import styles from "./URLShortener.module.css";
import { useState } from "react";
import global from "../../../css/Global.module.css";
import { FiLink, FiPenTool } from "react-icons/fi";
import { ValidateAlias, ValidateURL } from "../../utils/Validation.jsx";
import apiFetch from "../../utils/Api.jsx";
import { useNavigate } from "react-router-dom";


export default function URLShortener({ notify, setUrls, setMostPopular, mostPopular }) {

    const [originalUrl, setOriginalUrl] = useState("");
    const [alias, setAlias] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const nav = useNavigate();

    async function generateNewUrl() {
        if (isLoading) return;

        const urlRes = ValidateURL(originalUrl);
        if (urlRes !== "VALID") {
            notify(urlRes, "ERROR");
            return;
        }

        if (alias !== "") {
            const aliasRes = ValidateAlias(alias);

            if (aliasRes !== "VALID") {
                notify(aliasRes, "ERROR");
                return;
            }
        }

        setIsLoading(true);

        try {
            const res = await apiFetch("/api/urls", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ originalUrl, alias })
            },
                nav
            )

            const data = await res.json();
            if (!res.ok) {
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
            setIsLoading(false);
        } catch (err) {
            notify("Something went wrong, please try again", "ERROR");
        } finally {
            setIsLoading(false);
        }
    }



    return (

        <form className={`${styles.inputContainer} ${global.glassyBackground}`}
            onSubmit={(e) => {
                e.preventDefault();
                generateNewUrl();
            }}>

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
                type="submit"
                className={`${styles.shortenBtn} ${isLoading ? styles.disabled : ""}`}
                disabled={isLoading}

            >{isLoading ? "Generating..." : "Shorten"}</button>

        </form>
    );
}