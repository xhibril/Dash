import { useSearchParams } from "react-router-dom";
import styles from "../css/Dashboard.module.css";
import { FiTrash, FiInfo } from "react-icons/fi";
import { useState } from "react";

export default function Urls({ urls,
  domain,
  setSelectedUrl,
  setSelectedUrlId,
  selectedUrlId,
  notify,
  setUrls,
  mostPopular,
  setMostPopular,
  fetchVisits,
  setVisitsToday,
  fetchTrend,
  
  setChartData,
}) {



  function refreshComponents(){

      if(!mostPopular){

    let max = -1;
    let successorUrl;


    for (const url of urls){
      if(url.visits >= max){
        max = url.visits;
        successorUrl = url;
      }
    }

    console.log("SUCCESSOR URL " + successorUrl.shortUrl);

    setMostPopular(successorUrl);
  }
    
  }

  async function deleteUrl(urlId) {

    urlId = Number(urlId);

    const oldUrls = urls;

    console.log("URL ID " + urlId);
    console.log("MOST POPULAR ID " + mostPopular.id);
    console.log("SELECTED URL ID " + selectedUrlId);

 

    if(selectedUrlId === urlId){
      setChartData([]);
    }


    const newUrls = urls.filter(url => url.id !== urlId);

       if(mostPopular.id === urlId){
      setMostPopular(null);

          let max = -1;
    let successorUrl;


    for (const url of newUrls){
      if(url.visits >= max){
        max = url.visits;
        successorUrl = url;
      }
    }

    setMostPopular(successorUrl);
    }






    setUrls(newUrls)

    const res = await fetch(`/api/delete/url?urlId=${urlId}`, { method: "POST" })

    if (!res.ok) {
      HandleError(res.status);
      notify("Could not delete URL, please try again", "ERROR");
      setUrls(oldUrls);
      return;
    }

    notify("URL Successfully deleted", "SUCCESS");
    fetchVisits();
    fetchTrend();
    refreshComponents();
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


