import styles from "./Dashboard.module.css";
import { useEffect, useState } from "react";
import apiFetch from "../../components/utils/Api.jsx";
import Chart from "../../components/dashboard/chart/Chart";
import Widgets from "../../components/dashboard/widgets/Widgets.jsx";
import URLShortener from "../../components/dashboard/url-shortener/URLShortener.jsx";
import Urls from "../../components/dashboard/urls/Urls.jsx";
import { useNavigate } from "react-router-dom";

export default function Dashboard({ notify }) {
  const [visitsToday, setVisitsToday] = useState("");
  const [trend, setTrend] = useState("");
  const [mostPopular, setMostPopular] = useState(null);
  const [chartData, setChartData] = useState([]);

  const [selectedUrlId, setSelectedUrlId] = useState("");
  const [selectedPeriod, setSelectedPeriod] = useState("DAILY");
  const [selectedUrl, setSelectedUrl] = useState("");

  const domain = "dash.com/";
  const [urls, setUrls] = useState([]);
  const nav = useNavigate();

  async function fetchVisits() {
    try {
      const res = await apiFetch("/api/analytics/visits", {}, nav, notify);
      if (!res) return;

      if (!res.ok) {
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();

      setVisitsToday(data);
    } catch (err) {
      notify("Something went wrong, please try again", "ERROR");
    }
  }

  async function fetchTrend() {
    try {
      const res = await apiFetch("/api/analytics/trend", {}, nav, notify);
      if (!res) return;

      if (!res.ok) {
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setTrend(data);
    } catch (err) {
      notify("Something went wrong, please try again", "ERROR");
    }
  }

  async function fetchUrls() {
    try {
      const res = await apiFetch("/api/urls", {}, nav, notify);
      if (!res) return;

      if (!res.ok) {
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setUrls(data);
    } catch (err) {
      notify("Something went wrong, please try again", "ERROR");
    }
  }

  async function fetchMostPopular() {
    try {
      const res = await apiFetch("/api/analytics/popular", {}, nav, notify);
      if (!res) return;

      if (!res.ok) {
        notify("Something went wrong, please try again", "ERROR");
        return;
      }


      const data = await res.json().catch(() => null);

      if (!data) return;

      setSelectedUrlId(data.id);
      setSelectedUrl(data.shortUrl);
      setMostPopular(data);
    } catch (err) {
      notify("Something went wrong, please try again most popuylar", "ERROR");
    }
  }

  async function fetchChartData() {
    try {
      const res = await apiFetch(
        `/api/analytics/chart?id=${selectedUrlId}&period=${selectedPeriod}`,
        {},
        nav, notify
      );

      if (!res) return;
      if (!res.ok) {
        notify("Something went wrong, please try again", "ERROR");
        return;
      }

      const data = await res.json();
      setChartData(data);
    } catch (err) {
      notify("Something went wrong, please try again chart", "ERROR");
    }
  }


  useEffect(() => {
    fetchUrls();
    fetchVisits();
    fetchTrend();
    fetchMostPopular();
  }, []);


  useEffect(() => {
    if (!selectedUrlId) return;
    fetchChartData();
  }, [selectedUrlId, selectedPeriod]);

  return (
    <>
      <div className={styles.mainContainer}>
        <div className={styles.leftContainer}>
          <div className={styles.dashboardTop}>

            <Widgets
              visitsToday={visitsToday}
              trend={trend}
              mostPopular={mostPopular}
              domain={domain}
              setMostPopular={setMostPopular}
              urls={urls}
            />
            <URLShortener
              notify={notify}
              setUrls={setUrls}
              setMostPopular={setMostPopular}
              mostPopular={mostPopular}
            />
          </div>


          <Chart
            chartData={chartData}
            selectedUrl={selectedUrl}
            domain={domain}
            period={selectedPeriod}
            setSelectedPeriod={setSelectedPeriod}
          />
        </div>

        <div className={styles.rightContainer}>
          <Urls
            urls={urls}
            mostPopular={mostPopular}
            setMostPopular={setMostPopular}
            fetchVisits={fetchVisits}
            setVisitsToday={setVisitsToday}
            setChartData={setChartData}
            fetchChartData={fetchChartData}
            setSelectedPeriod={setSelectedPeriod}
            setSelectedUrl={setSelectedUrl}
            fetchTrend={fetchTrend}
            setUrls={setUrls}
            domain={domain}
            setSelectedUrlId={setSelectedUrlId}
            selectedUrlId={selectedUrlId}
            notify={notify}
          />
        </div>
      </div>
    </>
  );
}