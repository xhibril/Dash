import Header from "../components/Header.jsx";
import FeatureSection from "../components/FeatureSection.jsx";
import Footer from "../components/Footer.jsx";


export default function Home() {
  return (
    <>

      <Header
        adMsg={`Fast, Simple URL shortening built for everyday use.
        Create an account to save up to 10 links.`}
      />

      <PublicDashboard />
      <FeatureSection />
      <Footer />
    </>
  );
}


import URLShortener from '../components/URLShortener.jsx';

function PublicDashboard() {
    return (
        <div className="main-container">

            <div className="main-wrapper">

                <div class="form-wrapper">

                    <h2 className="title">
                        Fast, Simple URL shortening built for everyday use.
                    </h2>

                   
                   <URLShortener/>
                </div>


                <div className="recent-container">

                    <h3>Your recent links:</h3>

                    <div className="links-container">
                        <p className="links-text">No links in your history</p>
                    </div>
                </div>
            </div>

        </div>
    )
}



