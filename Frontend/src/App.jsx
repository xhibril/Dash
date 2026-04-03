import { BrowserRouter, Routes, Route } from "react-router-dom";
import Landing from "./pages/landing/Landing.jsx";
import Dashboard from "./pages/dashboard/Dashboard.jsx";
import Support from "./pages/support/Support.jsx";
import Layout from "./components/layout/AppLayout.jsx";
import Signup from "./pages/auth/signup/Signup.jsx";
import Login from "./pages/auth/login/Login.jsx";
import AuthLayout from "./components/layout/AuthLayout.jsx";
import ForgotPassword from "./pages/auth/forgot-password/FrogotPassword.jsx";
import VerifyEmail from "./pages/auth/verify-email/VerifyEmail.jsx";
import { useState, useRef, useEffect } from "react";
import Notification from "./components/ui/Notification.jsx";
import DeleteAccount from "./pages/account-settings//delete-account/DeleteAccount.jsx";
import UpdatePassword from "./pages/account-settings/update-password/UpdatePassword.jsx";
import UpdateEmail from "./pages/account-settings/update-email/UpdateEmail.jsx";
import ProtectedRoutes from "./components/layout/ProtectedRoutes.jsx";

export default function App() {

  const [message, setMessage] = useState("")
  const [type, setType] = useState("")
  const [id, setId] = useState("")
  const [isAuth, setIsAuth] = useState(null);
  const [checkedAuth, setCheckedAuth] = useState(false);

  let messageTimeout = useRef(null);
  function notify(msg, t) {

    if (messageTimeout.current !== null) {
      clearTimeout(messageTimeout.current);
    }

    setMessage(msg);
    setType(t);
    setId(Date.now());

    messageTimeout.current = setTimeout(() => {
      setMessage("");
      messageTimeout.current = null;

    }, 3000)
  }


  useEffect(() => {
    fetch("/api/auth/status", {
      credentials: "include"
    })
      .then(res => setIsAuth(res.ok))
      .catch(() => setIsAuth(false))
      .finally(() => setCheckedAuth(true))
  }, [checkedAuth])


  return (
    <>
      <Notification message={message} type={type} key={id} />

      <BrowserRouter>
        <Routes>

          <Route path="/" element={<Landing notify={notify} />} />

          <Route element={<ProtectedRoutes isAuth={isAuth} />}>
            <Route path="/delete-account" element={<DeleteAccount notify={notify} />} />
            <Route path="/update-password" element={<UpdatePassword notify={notify} />} />
            <Route path="/update-email" element={<UpdateEmail notify={notify} />} />


            <Route element={<Layout notify={notify} setIsAuth={setIsAuth} />}>
              <Route path="/dashboard" element={<Dashboard notify={notify} />} />
              <Route path="/support" element={<Support notify={notify} />} />
            </Route>
          </Route>

          <Route element={<AuthLayout isAuth={isAuth} />}>
            <Route path="/signup" element={<Signup notify={notify} />} />
            <Route path="/login" element={<Login notify={notify} setIsAuth={setIsAuth} isAuth={isAuth} />} />
            <Route path="/forget" element={<ForgotPassword notify={notify} />} />
            <Route path="/verify/email" element={<VerifyEmail notify={notify} />} />
          </Route>

        </Routes>
      </BrowserRouter>
    </>
  );
}
