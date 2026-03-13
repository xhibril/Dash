
import { useState } from "react";
import SideBar from "./SideBar.jsx";
import { Outlet } from "react-router-dom";

import "../../index.css";

export default function Layout({notify}) {
  const [active, setActive] = useState(true);


  const toggleSidebar = () => setActive(!active);

  return (
    <>

      <div className="layout">
        <SideBar active={active} toggleSidebar={toggleSidebar} notify={notify} />
        <div className="content">
          <Outlet/>
        </div>
      </div>
    </>
  );
}