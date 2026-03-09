import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Landing.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import Support from "./pages/Support.jsx";
import Layout from "./pages/Layout.jsx";
import Signup from "./pages/Signup.jsx";
import Login from "./pages/Login.jsx";
import AuthLayout from "./pages/AuthLayout.jsx";
import ForgotPassword from "./pages/FrogotPassword.jsx";
import VerifyEmail from "./pages/VerifyEmail.jsx";
import { useState } from "react";
import Notification from "./components/Notification.jsx";


export default function App() {

  const [message, setMessage ] = useState("")
  const [type, setType] = useState("")

  function notify(msg, t){
    setMessage(msg);
    setType(t);


    setTimeout(()=>{
       
      setMessage("");
    
    },3000)
  }
  

  return (
  <>
 
 <Notification message = {message} type = {type}/>
 

    <BrowserRouter>
      <Routes>
    
        <Route element={<Layout/>}>
          <Route path="/" element={<Home notify = {notify}/>} />
          <Route path="/dashboard" element={<Dashboard notify = {notify}/>} />
          <Route path="/support" element={<Support notify = {notify}/>} />
        </Route>

        <Route element={<AuthLayout />}>
          <Route path="/signup" element={<Signup notify = {notify}/>} />
          <Route path="/login" element={<Login notify = {notify}/>} />
                  <Route path="/forget" element={<ForgotPassword notify = {notify}/>} />
                             <Route path="/verify/email" element={<VerifyEmail notify = {notify}/>} />
        </Route>

      </Routes>
    </BrowserRouter>
    </>
  );
}
