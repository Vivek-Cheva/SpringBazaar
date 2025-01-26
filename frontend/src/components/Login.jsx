import React, { useState } from "react";
import axios from "axios";
import { redirect } from "react-router-dom";
import { useNavigate } from 'react-router-dom';

const Login = ({email,setEmail,password,setPassword,error,setError}) => {
  
  const [message, setMessage] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post("http://localhost:8080/api/auth/login", {
          email: email,
          password: password,
        });
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("email", email);
       
 window.location.assign("/")

      } catch (err) {
        // alert("hello")
        if (err.response && err.response.status === 401) {
            setError("Invalid email or password");
        } else {
            setError("Unexpected error occurred. Please try again later.");
        }
      }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleLogin}>
        <h2>Login</h2>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        {error && <p className="error">{error}</p>}
        <button type="submit">Login</button>
        <p>
          Don't have an account? <a href="/signup">Sign Up</a>
        </p>
      </form>
    </div>
  );
};

export default Login;
