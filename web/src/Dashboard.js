import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    useEffect(() => {
        // 1. Check if user is authenticated
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login'); // Redirect if no token found
        } else {
            // Optional: If you implemented the GET /user/me endpoint, fetch data here.
            // For now, we will just set a dummy state to show the page is working.
            setUser({ email: "User" }); 
        }
    }, [navigate]);

    const handleLogout = () => {
        // Logout Logic from your Activity Diagram
        const confirmLogout = window.confirm("Are you sure you want to logout?");
        if (confirmLogout) {
            localStorage.removeItem('token'); // Clear the session
            navigate('/login'); // Redirect to public page
        }
    };

    return (
        <div style={{ padding: '20px', textAlign: 'center' }}>
            <h1>Welcome to the Dashboard</h1>
            <p>You have successfully authenticated.</p>
            
            <div style={{ marginTop: '20px', padding: '20px', border: '1px solid #ccc' }}>
                <h3>User Profile</h3>
                <p>Status: <span style={{ color: 'green', fontWeight: 'bold' }}>Active</span></p>
                {/* You can display more user info here later */}
            </div>

            <br />
            <button 
                onClick={handleLogout} 
                style={{ padding: '10px 20px', backgroundColor: 'red', color: 'white', border: 'none', cursor: 'pointer' }}
            >
                Logout
            </button>
        </div>
    );
}

export default Dashboard;