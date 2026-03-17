import Register from "./Components/Register";
import Login from "./Components/Login";
import { Routes, Route, Link } from "react-router-dom";
import "./App.css";
import Dashboard from "./Components/Dashboard";

function App() {
  return (
    <>
      <nav>
        <Link to="/Register">Register</Link>
        <Link to="/Login">Login</Link>
      </nav>

      <Routes>
  <Route path="/" element={<h2 className="page">Welcome Home</h2>} />
  <Route path="/Register" element={<Register />} />
  <Route path="/Login" element={<Login />} />
  <Route path="/Dashboard" element={<Dashboard />} />
</Routes>

    </>
  );
}

export default App;
