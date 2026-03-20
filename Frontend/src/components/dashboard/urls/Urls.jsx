import styles from "./Urls.module.css";
import { FiTrash } from "react-icons/fi";
import { useState } from "react";
import { NoDataAvailable } from "../../UI/SmallComponents.jsx";
import apiFetch from "../../utils/Api.jsx";
import { useNavigate } from "react-router-dom";

export default function Urls({
  urls,
  domain,
  setSelectedUrl,
  setSelectedUrlId,
  selectedUrlId,
  notify,
  setUrls,
  mostPopular,
  setMostPopular,
  fetchVisits,
  fetchTrend,
  fetchChartData,
  setChartData,
}) {


  const [isLoading, setIsLoading] = useState(false);
  const nav = useNavigate();


  // find successor url if deleted url was displayed in mostPopular n chart
  function successorUrl(filteredUrls) {
    let max = -1;
    let successor;

    for (const url of filteredUrls) {
      if (url.visits >= max) {
        max = url.visits;
        successor = url;
      }
    }
    return successor;
  }


  async function deleteUrl(urlId) {
    if (isLoading) return;

    setIsLoading(true);

    urlId = Number(urlId);

    const oldUrls = urls;

    const filteredUrls = urls.filter(url => url.id !== urlId);

    const succUrl = successorUrl(filteredUrls);

    setUrls(filteredUrls)

    try {
      const res = await apiFetch(
        `/api/delete/url?urlId=${urlId}`,
        { method: "POST" },
        nav
      )

      if (!res.ok) {
        notify("Could not delete URL, please try again", "ERROR");
        setUrls(oldUrls);
        return;
      }

      notify("URL Successfully deleted", "SUCCESS");

      if (succUrl) {
        // if deleted url was in most popular widget / chart data, update it if succ url is found
        if (mostPopular?.id === urlId) setMostPopular(succUrl);

        if (selectedUrlId === urlId) {
          setSelectedUrlId(succUrl.id);
          setSelectedUrl(succUrl.shortUrl);
          fetchChartData();
        }

      } else {

        if (mostPopular?.id === urlId) setMostPopular(null);
        if (selectedUrlId === urlId) setChartData([]);
      }

      // refetch visits and trend
      fetchVisits();
      fetchTrend();
    }
    catch (err) {
      notify("Something went wrong, please try again", "ERROR");
    }
    finally {
      setIsLoading(false);
    }
    return;
  }


  return (
    <div className={styles.urlContainer}>
      <FiTrash className={styles.deleteUrl}

        onDragOver={(e) => e.preventDefault()}

        onDrop={(e) => {
          const id = e.dataTransfer.getData("id");
          deleteUrl(id);
        }}
      />

      <div className={styles.urlList}>

        {urls.length === 0 ? (
          <NoDataAvailable />
        ) : (

          urls.map(item => (
            <div key={item.id} className={styles.urlItem}>

              <div data-id={item.id} className={styles.urlRow}>

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


