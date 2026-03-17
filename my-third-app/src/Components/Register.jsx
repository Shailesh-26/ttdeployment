import axios from "axios"
import { useState } from "react"
import "./Main.css"

function Register() {
  const [data, setData] = useState({
    username: "",
    email: "",
    password: ""
  })

  const [message, setMessage] = useState("")
  const [error, setError] = useState("")

  function handleChange(e) {
    setData({ ...data, [e.target.name]: e.target.value })
  }

  async function submitForm(e) {
    e.preventDefault()
    setMessage("")
    setError("")

    try {
      const res = await axios.post("https://ttdeployment-2t9z.onrender.com/register", data)
      setMessage(res.data)   
      setData({ username: "", email: "", password: "" }) 
    } catch (err) {
      setError(err.response?.data || "Registration failed")
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1>Register Here</h1>

        {message && <p className="success-msg">{message}</p>}
        {error && <p className="error-msg">{error}</p>}

        <form onSubmit={submitForm}>
          <input
            type="text"
            name="username"
            placeholder="Enter your name"
            value={data.username}
            onChange={handleChange}
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Enter your email"
            value={data.email}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Enter your password"
            value={data.password}
            onChange={handleChange}
            required
          />
          <button type="submit">Register</button>
        </form>

        <br />
        <a href="/login">Already have an account? Login</a>
      </div>
    </div>
  )
}

export default Register


