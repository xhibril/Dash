import "../css/SideBar.css";
import { FiHome, FiMail, FiLogOut, FiMenu } from "react-icons/fi";
import { useNavigate } from "react-router-dom";


function SideBar({ active, toggleSidebar }) {
  const navigate = useNavigate();

  return (
    <>
    <div className = "sideBarContainer">
      <button onClick={toggleSidebar} className={active ? "sidebar-btn active" : "sidebar-btn"}>
        <FiMenu />
      </button>

      <div className={active ? "sidebar active" : "sidebar"}>
     
        <div className="sidebar-top">
            <h1 className = "site-title">DASH</h1>

          <button className="sidebar-icon" 
          onClick ={() => navigate("/dashboard")}><FiHome />
          </button>

          <button className="sidebar-icon"
          onClick = {() => navigate("/support")}
          ><FiMail />
          </button>
        </div>

        <div className="sidebar-bottom">
          <button className="profile"> </button>
          <button className="sidebar-icon"><FiLogOut /></button>

        </div>

      </div>
      </div>
    </>
  );
}

export default SideBar;
