import { Outlet, Navigate } from "react-router-dom";
import "../../index.css";


export default function AuthLayout({ isAuth }) {
  // if user is auth redirect to dashboard

      if(isAuth === null) {
        return;
    }

  if (isAuth === true) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
}