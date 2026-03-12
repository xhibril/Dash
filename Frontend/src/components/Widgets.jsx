import styles from "../css/Widgets.module.css";
import { FiInfo, FiZap, FiEye } from "react-icons/fi";

export default function Widgets({ visitsToday, trend, mostPopular, domain }) {

  return (
    <>
      <div className={styles.widgetsContainer}>
        <div className={styles.widgets}>

          <div className={styles.visits}>
            <p className={styles.visitsText}>
              <FiEye className={styles.eyeIcon} />
              Visits today</p>
              
              {visitsToday === "" ? (
                  <div className={styles.empty}>
                <FiInfo className={styles.explinationIcon} />
                <p >No data available</p>
              </div>
              ):

              <>
               <h1 className={styles.visitsToday}>{visitsToday}</h1>

            <p className={`${styles.trend} ${trend > 0 ? styles.up : trend === 0 ? styles.neutral : styles.down}`}>
              {trend !== 0 && trend}
              {`${trend > 0 ? "% up today" : trend === 0 ? "No change" : "% down today"}`}</p>
            </>
            }
           
          </div>

          <div className={styles.popular}>

            <p className={styles.popularText}>
              <FiZap className={styles.zapIcon} />Most popular</p>

            {mostPopular === null ? (
              <div className={styles.empty}>
                <FiInfo className={styles.explinationIcon} />
                <p >No data available</p>
              </div>
            ) :
              <>
                <p className={styles.mostPopular}>{domain}{mostPopular.shortUrl}</p>

                <span className={styles.visitsWrapper}>
                  <p >Visits:</p>
                  <p className={styles.mostPopularVisits}>{mostPopular.visits}</p>
                </span>
              </>
            }
          </div>
        </div>
      </div>
    </>
  );
}
