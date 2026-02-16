import React from 'react';
import { useNavigate } from 'react-router-dom';
import './LandingPage.css';

function LandingPage() {
    const navigate = useNavigate();

    return (
        <div className="landing-page">
            {/* ---- decorative shapes ---- */}
            <div className="land-shape land-shape--magenta" />
            <div className="land-shape land-shape--cyan-1" />
            <div className="land-shape land-shape--cyan-2" />
            <div className="land-shape land-shape--lime-1" />
            <div className="land-shape land-shape--lime-2" />
            <div className="land-shape land-shape--blue" />

            {/* ---- card ---- */}
            <div className="landing-card">
                <img
                    className="landing-card__logo"
                    src="/miniapplogo.png"
                    alt="App Logo"
                />
                <h1 className="landing-card__title">Welcome to Mini App</h1>
                <p className="landing-card__subtitle">
                    Secure User Registration &amp; Authentication System
                </p>

                <div className="landing-card__buttons">
                    <button
                        id="landing-login"
                        className="landing-card__btn-login"
                        onClick={() => navigate('/login')}
                    >
                        Login
                    </button>
                    <button
                        id="landing-register"
                        className="landing-card__btn-register"
                        onClick={() => navigate('/register')}
                    >
                        Register
                    </button>
                </div>
            </div>
        </div>
    );
}

export default LandingPage;