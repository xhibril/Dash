import { useState } from "react";
import SideBar from "./SideBar.jsx";
import { Outlet } from "react-router-dom";

import "../../index.css";

export default function Layout({ notify, setIsAuth }) {
  const [active, setActive] = useState(false);
  const toggleSidebar = () => setActive(!active);

  return (
    <>

      <div className="layout">
        <SideBar active={active} toggleSidebar={toggleSidebar} notify={notify} setIsAuth = {setIsAuth} />
        <div className="content">
          <Outlet />
        </div>
      </div>
    </>
  );
}