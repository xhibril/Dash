import styles from "../css/Dashboard.module.css";

import { useEffect, useState } from "react";
import { HandleError } from "../components/ErrorHandler.jsx";

import Chart from "../components/Chart.jsx";
import Urls from "../components/Urls.jsx";
import Widgets from "../components/Widgets.jsx";
import URLShortener from "../components/URLShortener.jsx";




export default function Dashboard({ notify }) {

  const [visitsToday, setVisitsToday] = useState("");
  const [trend, setTrend] = useState("");
  const [mostPopular, setMostPopular] = useState(null);
  const [chartData, setChartData] = useState([]);


  const [selectedUrlId, setSelectedUrlId] = useState("");
  const [selectedPeriod, setSelectedPeriod] = useState("DAILY");
  const [selectedUrl, setSelectedUrl] = useState("");

  const domain = "dash.com/"

  const [urls, setUrls] = useState([]);
  

  
 
    async function fetchVisits() {
      const res = await fetch("/api/visits");


      if (!res.ok) {
        HandleError(res.status);
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setVisitsToday(data);
    }

    
    async function fetchTrend() {
     const res = await fetch("/api/trend");

      if (!res.ok) {
        HandleError(res.status);
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setTrend(data);
    }

           async function fetchUrls() {
      const res = await fetch("/api/urls");

      if (!res.ok) {
        HandleError(res.status);
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setUrls(data);
    }


    
    async function fetchMostPopular() {
      const res = await fetch("/api/popular");

      if (!res.ok) {
        HandleError(res.status);
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();

      setSelectedUrlId(data.id);
      setSelectedUrl(data.shortUrl);
      setMostPopular(data);
    }



  useEffect(() => {

    fetchUrls();
    fetchVisits();
    fetchTrend();
    fetchMostPopular();

  }, []);



  async function fetchChartData() {

    const res = await fetch(
      `/api/chart?id=${selectedUrlId}&period=${selectedPeriod}`
    );

    const data = await res.json();

    if (!res.ok) {
      HandleError(data.status);
      notify("Something went wrong, please try again", "ERROR");
      return;
    }

    setChartData(data);
  }


  useEffect(() => {
    if (!selectedUrlId) return;

    fetchChartData();
  }, [selectedUrlId, selectedPeriod])




  return (
    <>
      <div className={styles.mainContainer}>
        <div className={styles.leftContainer}>

          <div className={styles.widgetsPanel}>
            <Widgets
              visitsToday={visitsToday}
              trend={trend}
              mostPopular = {mostPopular}
              domain={domain}
              setMostPopular={setMostPopular}
              urls = {urls}
            />
            <URLShortener 
            notify={notify}
            setUrls = {setUrls}
            setMostPopular = {setMostPopular}
            mostPopular = {mostPopular}
             />
          </div>

          <Chart 
          chartData={chartData} 
          selectedUrl = {selectedUrl} 
          domain = {domain}
          period = {selectedPeriod}
          setSelectedPeriod = {setSelectedPeriod}/>
        </div>

        <div className={styles.rightContainer}>
          <Urls 
          urls={urls} 
          mostPopular = {mostPopular}
          setMostPopular = {setMostPopular}

         
          fetchVisits = {fetchVisits}

    
          setVisitsToday={setVisitsToday}
          setChartData = {setChartData}

fetchTrend = {fetchTrend}
          setUrls={setUrls}
          domain={domain}
          setSelectedUrl={setSelectedUrl}
          setSelectedUrlId={setSelectedUrlId}
          selectedUrlId = {selectedUrlId}
          notify={notify} />
        </div>
      </div>
    </>
  );
}







