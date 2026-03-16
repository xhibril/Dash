import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/landing/Landing.jsx";
import Dashboard from "./pages/dashboard/Dashboard.jsx";
import Support from "./pages/support/Support.jsx";
import Layout from "./components/layout/Layout.jsx";
import Signup from "./pages/auth/Signup.jsx";
import Login from "./pages/auth/Login.jsx";
import AuthLayout from "./components/layout/AuthLayout.jsx";
import ForgotPassword from "./pages/auth/FrogotPassword.jsx";
import VerifyEmail from "./pages/auth/verify-email/VerifyEmail.jsx";
import { useState, useRef } from "react";
import Notification from "./components/UI/Notification.jsx";
import DeleteAccount from "./pages/account-settings//delete-account/DeleteAccount.jsx";
import UpdatePassword from "./pages/account-settings/update-password/UpdatePassword.jsx";
import UpdateEmail from "./pages/account-settings/update-email/UpdateEmail.jsx";


export default function App() {

  const [message, setMessage] = useState("")
  const [type, setType] = useState("")
  const [id, setId] = useState("")
  let messageTimeout = useRef(null);

  function notify(msg, t) {

    if (messageTimeout.current !== null) {

      clearTimeout(messageTimeout.current);
    }

    setMessage(msg);
    setType(t);
    setId(Date.now());

    console.log("message:", message);

    messageTimeout.current = setTimeout(() => {
      setMessage("");
      messageTimeout.current = null;

    }, 3000)
  }


  return (
    <>

      <Notification message={message} type={type} key={id} />


      <BrowserRouter>
        <Routes>

          <Route path="/" element={<Home notify={notify} />} />
           <Route path="/delete-account" element={<DeleteAccount notify = {notify}/>} />
           <Route path = "/update-password" element = {<UpdatePassword notify={notify}/>}/>
           <Route path = "/update-email" element={<UpdateEmail notify = {notify}/>}/>


          <Route element={<Layout notify={notify} />}>
            <Route path="/dashboard" element={<Dashboard notify={notify} />} />
            <Route path="/support" element={<Support notify={notify} />} />
          </Route>

          <Route element={<AuthLayout />}>
            <Route path="/signup" element={<Signup notify={notify} />} />
            <Route path="/login" element={<Login notify={notify} />} />
            <Route path="/forget" element={<ForgotPassword notify={notify} />} />
            <Route path="/verify/email" element={<VerifyEmail notify={notify} />} />
          </Route>

        </Routes>
      </BrowserRouter>
    </>
  );
}
