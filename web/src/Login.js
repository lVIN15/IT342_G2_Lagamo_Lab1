import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Login() {
    const navigate = useNavigate();
    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Matches Backend Controller: @PostMapping("/login")
            const response = await axios.post('http://localhost:8080/api/auth/login', credentials);
            
            // Assuming your backend returns a plain string or object with token
            // Adjust 'response.data' based on what your backend actually returns
            const token = response.data.token || response.data; 

            // 1. Store Token in LocalStorage (Critical for Session Management)
            localStorage.setItem('token', token);
            
            // 2. Redirect to Dashboard
            navigate('/dashboard');
        } catch (error) {
            console.error("Login error:", error);
            alert("Invalid Credentials");
        }
    };

    return (
        <div style={{ padding: '50px', maxWidth: '300px', margin: 'auto', textAlign: 'center' }}>
            <h2>Login</h2>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                <input 
                    name="email" 
                    type="email" 
                    placeholder="Email Address" 
                    onChange={handleChange} 
                    required 
                />
                <input 
                    name="password" 
                    type="password" 
                    placeholder="Password" 
                    onChange={handleChange} 
                    required 
                />
                <button type="submit" style={{ padding: '10px', cursor: 'pointer' }}>Login</button>
            </form>
            <p>Don't have an account? <button onClick={() => navigate('/register')}>Register</button></p>
        </div>
    );
}

export default Login;