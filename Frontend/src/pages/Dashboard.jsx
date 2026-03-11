import styles from "../css/Dashboard.module.css";

import { useEffect, useState } from "react";
import { HandleError } from "../components/ErrorHandler.jsx";

import Chart from "../components/Chart.jsx";
import Urls from "../components/Urls.jsx";
import Widgets from "../components/Widgets.jsx";
import URLShortener from "../components/URLShortener.jsx";




export default function Dashboard({ notify }) {

  const [visits, setVisits] = useState("");
  const [trend, setTrend] = useState("");
  const [mostPopular, setMostPopular] = useState([]);
  const [chartData, setChartData] = useState([]);


  const [id, setSelectedUrlId] = useState("");
  const [period, setSelectedPeriod] = useState("DAILY");
  const [selectedUrl, setSelectedUrl] = useState("");

  const domain = "dash.com/"

  const [urls, setUrls] = useState([]);
  



  useEffect(() => {

    async function fetchUrls() {
      const res = await fetch("/api/urls");

      if (!res.ok) {
        HandleError(res.status);
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setUrls(data);
      console.log(data);
    }

 
    async function fetchVisits() {
      const res = await fetch("/api/visits");


      if (!res.ok) {
        HandleError(res.status);
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setVisits(data);
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

    fetchUrls();
    fetchVisits();
    fetchTrend();
    fetchMostPopular();

  }, []);



  async function fetchChartData() {

    const res = await fetch(
      `/api/chart?id=${id}&period=${period}`
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
    if (!id) return;

    fetchChartData();
  }, [id, period])




  return (
    <>
      <div className={styles.mainContainer}>
        <div className={styles.leftContainer}>

          <div className={styles.widgetsPanel}>
            <Widgets
              visits={visits}
              trend={trend}
              mostPopular = {mostPopular}
              domain={domain}
            />
            <URLShortener 
            notify={notify}
            setUrls = {setUrls}
             />
          </div>

          <Chart 
          chartData={chartData} 
          selectedUrl = {selectedUrl} 
          domain = {domain}
          period = {period}
          setSelectedPeriod = {setSelectedPeriod}/>
        </div>

        <div className={styles.rightContainer}>
          <Urls 
          urls={urls} 
          domain={domain}
          setSelectedUrl={setSelectedUrl}
          setSelectedUrlId={setSelectedUrlId}
          notify={notify} />
        </div>
      </div>
    </>
  );
}







