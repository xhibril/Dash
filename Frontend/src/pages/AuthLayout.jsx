import { Outlet } from "react-router-dom";
import { useEffect, useState } from "react";
import SideBar from "../components/SideBar";
import "../index.css";


export default function Layout() {

  useEffect(() => {
    AuthStatus();
    async function AuthStatus() {
      const res = await fetch("/api/auth/status");

      if (!res.ok) {
        return;
      }

      const data = await res.json();

      if (data) {
        window.location.href = "/dashboard"
      }

    }


  })


  return (
    <>

      <Outlet />

    </>
  );
}