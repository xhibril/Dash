import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Landing.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import Support from "./pages/Support.jsx";
import Layout from "./pages/Layout.jsx";
import Signup from "./pages/Signup.jsx";
import Login from "./pages/Login.jsx";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/support" element={<Support />} />
                   <Route path="/signup" element={<Signup />} />
                     <Route path="/login" element={<Login />} />
        </Route>
      </Routes>

    </BrowserRouter>
  );
}
