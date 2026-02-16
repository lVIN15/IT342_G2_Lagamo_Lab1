import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Dashboard.css';

function Dashboard() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        // Fetch user profile from backend
        axios
            .get('http://localhost:8080/api/auth/me', {
                headers: { Authorization: `Bearer ${token}` }
            })
            .then((res) => {
                setUser(res.data);
                setLoading(false);
            })
            .catch(() => {
                localStorage.removeItem('token');
                navigate('/login');
            });
    }, [navigate]);

    const confirmLogout = () => {
        localStorage.removeItem('token');
        navigate('/login', { state: { loggedOut: true } });
    };

    // Build full name
    const fullName = user
        ? [user.firstname, user.middlename, user.lastname].filter(Boolean).join(' ')
        : '';

    // Build full address
    const fullAddress = user
        ? [user.street, user.barangay, user.municipality, user.province, user.country]
            .filter(Boolean)
            .join(', ')
        : '';

    // Avatar initials (first letter of first + last name)
    const initials = user
        ? `${(user.firstname || '')[0] || ''}${(user.lastname || '')[0] || ''}`.toUpperCase()
        : '';

    return (
        <div className="dashboard-page">
            {/* ---- decorative shapes ---- */}
            <div className="dash-shape dash-shape--magenta" />
            <div className="dash-shape dash-shape--cyan-1" />
            <div className="dash-shape dash-shape--cyan-2" />
            <div className="dash-shape dash-shape--lime-1" />
            <div className="dash-shape dash-shape--lime-2" />
            <div className="dash-shape dash-shape--blue" />

            {/* ---- profile card ---- */}
            <div className="dashboard-card">
                {loading ? (
                    <p className="dashboard-card__loading">Loading profile…</p>
                ) : (
                    <>
                        {/* Avatar */}
                        <div className="dashboard-card__avatar">{initials}</div>

                        {/* Name & Email */}
                        <h1 className="dashboard-card__name">{fullName}</h1>
                        <p className="dashboard-card__email">{user.email}</p>

                        {/* Status badge */}
                        <div className="dashboard-card__status">
                            <span className="dashboard-card__status-dot" />
                            Active
                        </div>

                        {/* Profile info rows */}
                        <div className="dashboard-card__info">
                            <div className="dashboard-card__info-row">
                                <span className="dashboard-card__info-label">Email</span>
                                <span className="dashboard-card__info-value">{user.email}</span>
                            </div>
                            <div className="dashboard-card__info-row">
                                <span className="dashboard-card__info-label">Contact</span>
                                <span className="dashboard-card__info-value">
                                    {user.contactNumber || '—'}
                                </span>
                            </div>
                            <div className="dashboard-card__info-row">
                                <span className="dashboard-card__info-label">Address</span>
                                <span className="dashboard-card__info-value">
                                    {fullAddress || '—'}
                                </span>
                            </div>
                        </div>

                        {/* Logout */}
                        <button
                            id="dashboard-logout"
                            className="dashboard-card__logout"
                            onClick={() => setShowModal(true)}
                        >
                            Logout
                        </button>
                    </>
                )}
            </div>

            {/* ---- logout confirmation modal ---- */}
            {showModal && (
                <div className="modal-overlay" onClick={() => setShowModal(false)}>
                    <div className="modal-card" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-card__icon">👋</div>
                        <h2 className="modal-card__title">Logout</h2>
                        <p className="modal-card__message">
                            Are you sure you want to logout?<br />
                            You'll need to sign in again to access your profile.
                        </p>
                        <div className="modal-card__buttons">
                            <button
                                className="modal-card__btn-cancel"
                                onClick={() => setShowModal(false)}
                            >
                                Cancel
                            </button>
                            <button
                                className="modal-card__btn-confirm"
                                onClick={confirmLogout}
                            >
                                Yes, Logout
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Dashboard;