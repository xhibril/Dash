import { Outlet, Navigate } from "react-router-dom";

export default function ({ isAuth }) {

    // if user is not logged redirect to login


    if(isAuth === null) {
        return;
    }
    if (isAuth === false) {
        return <Navigate to="/login" replace />;
    }

    return <Outlet />;
}