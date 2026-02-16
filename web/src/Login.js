import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';
import './Login.css';

function Login() {
    const navigate = useNavigate();
    const location = useLocation();
    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });

    // Toast state: { type: 'success' | 'error', message: string } | null
    const [toast, setToast] = useState(null);

    // Show toasts from navigation state (logout / registration)
    useEffect(() => {
        if (location.state?.loggedOut) {
            showToast('success', 'You have been logged out successfully');
        } else if (location.state?.registered) {
            showToast('success', 'Registration successful! Please login.');
        }
        // Clear state so toast doesn't reappear on refresh
        if (location.state) {
            window.history.replaceState({}, document.title);
        }
    }, [location.state]);

    const showToast = (type, message) => {
        setToast({ type, message });
        setTimeout(() => setToast(null), 3000);
    };

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', credentials);
            const token = response.data.token || response.data;
            localStorage.setItem('token', token);
            navigate('/dashboard');
        } catch (error) {
            console.error("Login error:", error);
            showToast('error', 'Invalid credentials. Please try again.');
        }
    };

    return (
        <div className="login-page">
            {/* ---- toast notification ---- */}
            {toast && (
                <div className={`toast toast--${toast.type}`} key={Date.now()}>
                    <span className="toast__icon">
                        {toast.type === 'success' ? '✓' : '✗'}
                    </span>
                    {toast.message}
                </div>
            )}

            {/* ---- decorative background shapes ---- */}
            <div className="shape shape--magenta" />
            <div className="shape shape--cyan-1" />
            <div className="shape shape--cyan-2" />
            <div className="shape shape--cyan-3" />
            <div className="shape shape--lime-1" />
            <div className="shape shape--lime-2" />
            <div className="shape shape--blue" />

            {/* ---- login card ---- */}
            <div className="login-card">
                <button
                    className="login-card__back"
                    onClick={() => navigate('/')}
                    type="button"
                    aria-label="Back to home"
                >
                    ←
                </button>
                <img className="login-card__logo" src="/miniapplogo.png" alt="App Logo" />
                <h1 className="login-card__title">Login</h1>

                <form className="login-card__form" onSubmit={handleSubmit}>
                    {/* Email */}
                    <div className="login-card__field">
                        <label className="login-card__label" htmlFor="login-email">
                            Email
                        </label>
                        <input
                            id="login-email"
                            className="login-card__input"
                            name="email"
                            type="email"
                            placeholder="username@email.com"
                            onChange={handleChange}
                            required
                        />
                    </div>

                    {/* Password */}
                    <div className="login-card__field">
                        <label className="login-card__label" htmlFor="login-password">
                            Password
                        </label>
                        <input
                            id="login-password"
                            className="login-card__input"
                            name="password"
                            type="password"
                            placeholder="Password"
                            onChange={handleChange}
                            required
                        />
                    </div>

                    {/* Submit */}
                    <button id="login-submit" className="login-card__button" type="submit">
                        Sign in
                    </button>
                </form>

                <p className="login-card__register">
                    Don't have an account yet?{' '}
                    <button
                        id="login-register-link"
                        className="login-card__register-link"
                        onClick={() => navigate('/register')}
                        type="button"
                    >
                        Register for free
                    </button>
                </p>
            </div>
        </div>
    );
}

export default Login;