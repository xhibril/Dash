import styles from "../css/Dashboard.module.css";
import { FiInfo } from "react-icons/fi";
export default function Widgets({ visits, trend, mostPopular, domain }) {

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
