import { Outlet } from "react-router-dom";
import { useState } from "react";
import SideBar from "../components/SideBar";
import "../index.css";

export default function Layout() {
  const [active, setActive] = useState(true);
  const toggleSidebar = () => setActive(!active);

  return (
    <>

      <div className="layout">
        <SideBar active={active} toggleSidebar={toggleSidebar} />
        <div className="content">
          <Outlet />
        </div>
      </div>
    </>
  );
}