import styles from "../css/Dashboard.module.css";

import URLShortener from "../components/URLShortener.jsx";
import {FiTrash} from 'react-icons/fi';

import { useEffect, useState } from "react";


import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
} from "recharts";

export default function Dashboard() {


  const domain = "dash.com/"

const [urls, setUrls] = useState([]);
useEffect(() => {
  async function fetchUrls() {
    const res = await fetch("api/urls");

    if (!res.ok) {
      console.log("Error getting urls");
      return;
    }

    const data = await res.json();
    setUrls(data); 
  }

  fetchUrls();
}, []);


  return (
    <>
    <div className = {styles.mainContainer}>
      <div className={styles.leftContainer}>

        <div className = {styles.leftWrapper}>
        <Widgets
          visits={10}
          trend={20}
          mostPopular="google.com"
          mostPopularClicks={120}
        />
          <URLShortener />
          </div>
        <div className = {styles.chartAndUrlWrapper}>
        <Chart />
      
        </div>
      </div>


      <div className={styles.rightContainer}>
        <Urls urls={urls} domain = {domain}/>
      </div>

</div>


    </>
  );
}




function Widgets({ visits, trend, mostPopular, mostPopularClicks }) {
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

            <p className={styles.mostPopular}>{mostPopular}</p>

            <span className={styles.visitsWrapper}>
              <p >Visits:</p>
              <p className={styles.mostVisits}>{mostPopularClicks}</p>
            </span>

          </div>
        </div>
      </div>
    </>
  );
}



function Urls({urls, domain}) {
  return (
    <div className={styles.urlContainer}>
      <FiTrash className = {styles.deleteUrl}/>
      
      <div className={styles.urlWrapper}>


        {urls.length === 0 ? (
          <p>No links created yet</p>
        ) : (
          urls.map(item => (
            <div key={item.id} className={styles.created}>

<div className = {styles.newUrlWrapper}>
              <p className={styles.url}> {domain}{item.shortUrl}</p>
              <p className = {styles.original}>{item.originalUrl}</p>
              </div>
              <p className={styles.urlVisits}>{item.visits}</p>
            </div>

          ))
        )}

      </div>
    </div>
  );
}



function Chart() {
  const data = [
    { day: "Tue", clicks: 100 },
    { day: "Wed", clicks: 340 },
    { day: "Thu", clicks: 630 },
    { day: "Fri", clicks: 1200 },
    { day: "Mon", clicks: 20 },
    { day: "Tue", clicks: 45 },
    { day: "Wed", clicks: -340 },
    { day: "Thu", clicks: -10 },
  ];

  return (
    <div className={styles.chartContainer}>
  


<div className = {styles.chartSettings}> 
      <p className = {styles.viewing}>Currently viewing</p>
        <button className = {styles.period}>Daily</button>
      <button className = {styles.period}>Weekly</button>
      <button className = {styles.period}>Monthly</button>
  
</div>






      <div className={styles.chartCard}>

<ResponsiveContainer padding = "2rem" width="100%" height={400}>
  <LineChart data={data}>

    <XAxis 
      dataKey="day"
      axisLine={false}
      tickLine={false}
    />

    <YAxis 
      axisLine={false}
      tickLine={false}
    />

    <Tooltip />

    <Line
      type="monotone"
      dataKey="clicks"
      stroke="#6366f1"
      strokeWidth={3}
      dot={{ r: 4 }}
      activeDot={{ r: 8 }}
    />

  </LineChart>
</ResponsiveContainer>
      </div>

    </div>
  );
}


