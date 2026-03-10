import styles from "../css/Dashboard.module.css";

import URLShortener from "../components/URLShortener.jsx";
import { FiTrash, FiInfo } from 'react-icons/fi';

import { useEffect, useState } from "react";
import { HandleError } from "../components/ErrorHandler.jsx";


import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
} from "recharts";


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
            <URLShortener notify={notify} />
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
          setSelectedUrlId={setSelectedUrlId} />
        </div>

      </div>


    </>
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


function Urls({ urls, domain, setSelectedUrl, setSelectedUrlId }) {
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


function Chart({chartData, selectedUrl, domain, period, setSelectedPeriod}) {

  return (
    <div className={styles.chartContainer}>

      <div className={styles.chartSettings}>
        <p className={styles.viewing}>Currently viewing: {domain}{selectedUrl}</p>
        <button className={`${styles.period} ${period === "DAILY" ? styles.selected : ""}`} onClick={() => setSelectedPeriod("DAILY")}>Daily</button>
        <button className={`${styles.period} ${period === "WEEKLY" ? styles.selected : ""}`} onClick={() => setSelectedPeriod("WEEKLY")}>Weekly</button>
        <button className={`${styles.period} ${period === "MONTHLY" ? styles.selected : ""}`} onClick={() => setSelectedPeriod("MONTHLY")}>Monthly</button>
      </div>


      {chartData.length === 0 ? (
        <div className={styles.empty}>
          <FiInfo className={styles.explinationIcon} />
          <p >No data available</p>
        </div>
      ) :

        <div className={styles.chartCard}>
          <ResponsiveContainer padding="2rem" width="100%" height="100%">
            <LineChart data={chartData}>

              <XAxis
                dataKey="period"
                axisLine={true}
                tickLine={false}
              />

              <YAxis
                dataKey="visits"
                axisLine={true}
                tickLine={false}
              />

              <Tooltip />

              <Line
                type="monotone"
                dataKey="visits"
                stroke="#6366f1"
                strokeWidth={3}
                dot={{ r: 4 }}
                activeDot={{ r: 8 }}
              />

            </LineChart>
          </ResponsiveContainer>
        </div>

      }

    </div>
  );
}




function Widgets({ visits, trend, mostPopular, domain }) {

  let trendClass;
  let trendText;

  if (trend > 0) {
    trendClass = `${styles.trend} ${styles.up}`;
    trendText = "% up today";
  } else if (trend === 0) {
    trendClass = `${styles.trend} ${styles.neutral}`;
    trendText = "No change";
    trend = "";
  } else {
    trendClass = `${styles.trend} ${styles.down}`;
    trendText = "% down today";
  }

  return (
    <>
      <div className={styles.widgetsContainer}>
        <div className={styles.widgets}>
          <div className={styles.visits}>
            <p>Visits today</p>
            <h1>{visits}</h1>

            <p className={trendClass}> {trend}{trendText}</p>

          </div>

          <div className={styles.popularContainer}>
            <p className={styles.popularText}>Most popular</p>

            {mostPopular.length === 0 ? (
              <div className={styles.empty}>
                <FiInfo className={styles.explinationIcon} />
                <p >No data available</p>
              </div>
            ) :
              <>

                <p className={styles.mostPopular}>{domain}
                  {mostPopular.shortUrl}</p>
                <span className={styles.visitsWrapper}>
                  <p >Visits:</p>
                  <p className={styles.mostVisits}>{mostPopular.visits}</p>
                </span>
              </>
            }
          </div>
        </div>
      </div>
    </>
  );
}

