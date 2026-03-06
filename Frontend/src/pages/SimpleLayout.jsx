import { Outlet } from "react-router-dom";
import { useState } from "react";
import SideBar from "../components/SideBar";
import "../index.css";

import Footer from "../components/Footer.jsx";

export default function Layout() {


  return (
    <>

          <Outlet />
  
    </>
  );
}