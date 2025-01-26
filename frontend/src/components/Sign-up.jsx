import React, { useState } from "react";
import axios from "axios";

const Signup = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post("http://localhost:8080/api/auth/signup", {
            name,
            email,
            password,
          });
      console.log(response.data); // Log response for debugging
      setMessage("Account created successfully! You can now log in.");
    } catch (err) {
      console.error("Signup error:", err); // Log error for debugging
      if (err.response) {
        if (err.response.status === 400) {
          setMessage("Invalid input. Please check your details.");
        } else if (err.response.status === 409) {
          setMessage("Email already in use. Try logging in.");
        } else {
          setMessage("Unexpected error occurred. Please try again later.");
        }
      } else {
        setMessage("Cannot connect to the server. Please check your connection.");
      }
    }
  };
  return (
    <div className="signup-container">
      <form onSubmit={handleSignup}>
        <h2>Sign Up</h2>
        <input
          type="text"
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
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
        <button type="submit">Sign Up</button>
        <p>{message}</p>
        <p>
          Already have an account? <a href="/login">Log In</a>
        </p>
      </form>
    </div>
  );
};

export default Signup;
