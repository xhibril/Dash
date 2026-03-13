import { FiInfo } from "react-icons/fi";

export function NoDataAvailable(){
    return(
        <div className="empty">
          <FiInfo className="explinationIcon"/>
          <p>No data available</p>
        </div>
    )
}