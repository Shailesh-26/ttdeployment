import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Main.css";

function Login() {
  const [loginUser, setLoginUser] = useState({
    email: "",
    password: ""
  });

  const navigate = useNavigate();

  function HandleChange(e) {
    setLoginUser({
      ...loginUser,
      [e.target.name]: e.target.value
    });
  }

  async function handleLogin() {
    try {
      const res = await axios.post("https://ttdeployment-2t9z.onrender.com/login", loginUser);

      localStorage.setItem("user", JSON.stringify(res.data.user));
      localStorage.setItem("token", res.data.token);

      alert("Login successful");
      navigate("/Dashboard");
    } catch (err) {
      alert("Invalid credentials");
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1>Login Page</h1>
        <input type="text" name="email" placeholder="Email" onChange={HandleChange} />
        <input type="password" name="password" placeholder="Password" onChange={HandleChange} />
        <button onClick={handleLogin}>Login</button><br /><br />
        <a href="/register">Create an account</a>
      </div>
    </div>
  );
}

export default Login;
