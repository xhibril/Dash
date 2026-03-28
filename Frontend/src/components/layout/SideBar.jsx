import styles from "./SideBar.module.css";
import { FiHome, FiMail, FiLogOut, FiMenu, FiUser, FiLock, FiAlertTriangle } from "react-icons/fi";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import apiFetch from "../utils/Api.jsx";

export default function SideBar({ active, toggleSidebar, notify }) {

  const [profileActive, setProfileActive] = useState(false);
  const nav = useNavigate();
  const [isLoading, setIsLoading] = useState(false);

  async function logout() {

    if (isLoading) return;

    setIsLoading(true);
    const res = await apiFetch("/api/logout", {}, nav, notify);
    if (!res) return;

    try {
      if (!res.ok) {
        notify("Logout failed. Please try again", "ERROR");
        return;
      }

      window.location.href = "/login"

    } catch (err) {
      notify("Something went wrong, please try again", "ERROR");

    } finally {
      setIsLoading(true);
    }
  }


  function go(route) {
    nav(route);
    setProfileActive(false);
  }


  return (
    <>
      <div className={styles.sideBarContainer}>
        <button onClick={toggleSidebar} className={`${styles.sideBarBtn} ${active ? styles.active : ""}`}>
          <FiMenu className={styles.sideBarBtnIcon} />
        </button>

        <div className={`${styles.sideBar} ${active ? styles.active : ""}`}>
          <div className={styles.sideBarTop}>
            <h1 className={styles.siteTitle}>DASH</h1>

            <button className={styles.sideBarIcon}
              onClick={() => go("/dashboard")}><FiHome />
            </button>

            <button className={styles.sideBarIcon}
              onClick={() => go("/support")}
            ><FiMail />
            </button>
          </div>

          <div className={styles.sideBarBottom}>

            <div className={styles.profile}
              onClick={() => setProfileActive(!profileActive)}>

              <FiUser className={styles.profileIcon} />

              <div className={`${styles.profileSettings} ${profileActive ? styles.active : ""}`}>

                <button className={styles.changePassword}
                  onClick={() => nav("/update-password")}
                >
                  <FiLock className={styles.changePasswordIcon} />
                  Change Password
                </button>

                <button className={styles.changeEmail}
                  onClick={() => nav("/update-email")}>
                  <FiMail className={styles.changeEmailIcon} />
                  Change Email</button>

                <button className={styles.deleteAccount}
                  onClick={() => nav("/delete-account")}>
                  <FiAlertTriangle className={styles.deleteAccountIcon}
                  />Delete Account</button>
              </div>
            </div>

            <button className={styles.sideBarIcon}
              onClick={() => logout()}
            ><FiLogOut /></button>
          </div>
        </div>
      </div>
    </>
  );
}


