import "../css/FeatureSection.css"
import activity from "../assets/stats.png"
import manage from "../assets/manage.png"

function FeatureSection() {
    return (

        <div className="second-container">
            <div className="content-wrapper">

                <p className="second-title">LINKLITE</p>

                <div className="card-wrapper">
                    <div className="card">
                        <h1>Link Activity Tracking</h1>
                        <p>Track link activity with simple click tracking and see
                            how often your URLs are accessed.</p>
                        <img src={activity}></img>
                    </div>

                    <div className="card">
                        <h1>Link Management</h1>
                        <p>Keep all your shortened links organized in a single
                            dashboard for quick access and easy management.</p>
                        <img src={manage}></img>
                    </div>

                    <div className="card">
                        <h1>Statistics</h1>
                        <p>URLS Created</p>
                        <h2 className="url-count">0</h2>


                    </div>

                </div>
            </div>

        </div>
    )
}


export default FeatureSection;