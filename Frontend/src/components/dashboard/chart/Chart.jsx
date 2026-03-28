import styles from "./Chart.module.css";
import { NoDataAvailable } from "../../UI/SmallComponents.jsx";

import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
} from "recharts";


export default function Chart({ chartData, selectedUrl, domain, period, setSelectedPeriod }) {

  return (
    <div className={styles.chartContainer}>
      <div className={styles.chartSettings}>

        {selectedUrl && chartData.length !== 0 && (
          <p className={styles.viewing}>Currently viewing: {domain}{selectedUrl}</p>
        )}

        <button className={`${styles.period} ${period === "DAILY" ? styles.selected : ""}`} onClick={() => setSelectedPeriod("DAILY")}>Daily</button>
        <button className={`${styles.period} ${period === "WEEKLY" ? styles.selected : ""}`} onClick={() => setSelectedPeriod("WEEKLY")}>Weekly</button>
        <button className={`${styles.period} ${period === "MONTHLY" ? styles.selected : ""}`} onClick={() => setSelectedPeriod("MONTHLY")}>Monthly</button>
      </div>

      <div className={styles.chartCard}>
        {chartData.length === 0 ? (
          <NoDataAvailable />
        ) : (
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={chartData}>
              <XAxis dataKey="period" axisLine tickLine={false} />
              <YAxis dataKey="visits" axisLine tickLine={false} />
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
        )}
      </div>



    </div>
  );
}
