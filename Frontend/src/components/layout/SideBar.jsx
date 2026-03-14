import styles from "./SideBar.module.css";
import { FiHome, FiMail, FiLogOut, FiMenu, FiUser, FiLock, FiAlertTriangle } from "react-icons/fi";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function SideBar({ active, toggleSidebar, notify }) {

  const [profileActive, setProfileActive] = useState(false);

    async function logout(){
    const res = await fetch("/api/logout");
    
    if(!res.ok){

      HandleError(res.status);
      notify("Logout failed. Please try again", "ERROR");
      return;
    }

    window.location.href = "/login";
  }



 const navigate = useNavigate();

  function go(route){
    navigate(route);
    setProfileActive(false);
  }

 
  return (
    <>
    <div className = {styles.sideBarContainer}>
      <button onClick={toggleSidebar} className={`${styles.sideBarBtn} ${active ? styles.active : ""}`}>
        <FiMenu />
      </button>

      <div className={`${styles.sideBar} ${active ? styles.active : ""}`}>
     
        <div className={styles.sideBarTop}>
            <h1 className = {styles.siteTitle}>DASH</h1>

          <button className={styles.sideBarIcon} 
          onClick ={() => go("/dashboard")}><FiHome />
          </button>

            <button className={styles.sideBarIcon} 
          onClick = {() => go("/support")}
          ><FiMail />
          </button>
        </div>

        <div className={styles.sideBarBottom}>

          <button className={styles.profile}
          onClick= {() => setProfileActive(!profileActive)}>

            <FiUser className = {styles.profileIcon}/> 

            <div className = {`${styles.profileSettings} ${profileActive ? styles.active : ""}`}>
              
              <button className = {styles.changePassword}
              onClick={() => navigate("/update-password")}
              >
                <FiLock className = {styles.changePasswordIcon}/>
                Change Password
                </button>


                <button className = {styles.changeEmail}
                onClick={() => navigate("/update-email")}>
                  <FiMail className = {styles.changeEmailIcon}/>
                  Change Email</button>



                  <button className = {styles.deleteAccount}
                  onClick ={() => navigate("/delete-account")}>
                    <FiAlertTriangle className = {styles.deleteAccountIcon}
                    />Delete Account</button>
              </div>

          </button>

          <button className={styles.sideBarIcon} 
          
          onClick={()=> logout()}
          
          ><FiLogOut /></button>

        </div>

      </div>
      </div>
    </>
  );
}


