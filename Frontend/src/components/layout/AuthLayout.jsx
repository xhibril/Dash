import { Outlet } from "react-router-dom";
import { useEffect } from "react";

import "../../index.css";


export default function Layout() {

  // if user is auth redirect to dashboard
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