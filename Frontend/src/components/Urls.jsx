import styles from "../css/Dashboard.module.css";
import { FiTrash, FiInfo } from "react-icons/fi";

export default function Urls({ urls, domain, setSelectedUrl, setSelectedUrlId, notify }) {
  return (
    <div className={styles.urlContainer}>

      <FiTrash className={styles.deleteUrl}

        onDragOver={(e) => e.preventDefault()}

        onDrop={(e) => {
          const id = e.dataTransfer.getData("id");
          deleteUrl(id);
        }}
      />

      <div className={styles.urlWrapper}>

        {urls.length === 0 ? (

          <div className={styles.empty}>

            <FiInfo className={styles.explinationIcon} />
            <p>No links created yet</p>
          </div>

        ) : (

          urls.map(item => (
            <div key={item.id} className={styles.created}>

              <div data-id={item.id} className={styles.newUrlWrapper}>

                <p className={styles.url} draggable

                  onDragStart={(e) =>
                    e.dataTransfer.setData("id", item.id)
                  }

                  onClick={() => {
                    setSelectedUrlId(item.id),
                    setSelectedUrl(item.shortUrl);
                    navigator.clipboard.writeText(`${domain}${item.shortUrl}`);
                    notify("Copied", "SUCCESS");
               
                  }}
                > {domain}{item.shortUrl}
                </p>

                <p className={styles.original}>{item.originalUrl}</p>
              </div>
              <p className={styles.urlVisits}>{item.visits}</p>
            </div>

          ))
        )}
      </div>
    </div>
  );
}


  async function deleteUrl(urlId) {

  const res = await fetch(`/api/delete/url?urlId=${urlId}`, {method: "POST"})

  if (!res.ok) {
    HandleError(res.status);
    notify("Could not delete URL, please try again", "ERROR");
    return;
  }
}
