import styles from "./Widgets.module.css";
import { NoDataAvailable } from "../../UI/SmallComponents.jsx";
import { FiZap, FiEye } from "react-icons/fi";
import global from "../../../css/Global.module.css";

export default function Widgets({ visitsToday, trend, mostPopular, domain }) {

  return (
    <>
      <div className={styles.widgetsContainer}>
        <div className={styles.widgetsRow}>

          <div className={`${styles.visitsWidget} ${global.glassyBackground}` }>
            <p className={styles.visitsText}><FiEye className={styles.eyeIcon}/> Visits today</p>

            {visitsToday === "" ? (
              <NoDataAvailable />

            ) :
              <>
                <h1 className={styles.visitsToday}>{visitsToday}</h1>

                <p className={`${styles.trend} ${trend > 0 ? styles.up : trend === 0 ? styles.neutral : styles.down}`}>
                  {trend !== 0 && trend}
                  {`${trend > 0 ? "% up today" : trend === 0 ? "No change" : "% down today"}`}</p>
              </>
            }

          </div>

          <div className={`${styles.popularWidget} ${global.glassyBackground}` }>

            <p className={styles.popularText}>
              <FiZap className={styles.zapIcon} />Most popular</p>

            {mostPopular === null ? (
             <NoDataAvailable/>
         
            ) :
              <>
                <p className={styles.mostPopular}>{domain}{mostPopular?.shortUrl}</p>

                <span className={styles.visitsRow}>
                  <p >Visits:</p>
                  <p className={styles.mostPopularVisits}>{mostPopular?.visits}</p>
                </span>
              </>
            }
          </div>
        </div>
      </div>
    </>
  );
}
