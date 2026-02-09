import React from 'react';
import { useNavigate } from 'react-router-dom';

function LandingPage() {
    const navigate = useNavigate();

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={styles.title}>Welcome to Mini App</h1>
                <p style={styles.subtitle}>
                    Secure User Registration & Authentication System
                </p>
                
                <div style={styles.buttonGroup}>
                    <button 
                        style={styles.loginButton} 
                        onClick={() => navigate('/login')}
                    >
                        Login
                    </button>
                    
                    <button 
                        style={styles.registerButton} 
                        onClick={() => navigate('/register')}
                    >
                        Register
                    </button>
                </div>
            </div>
        </div>
    );
}

// Simple internal CSS for a clean look
const styles = {
    container: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#f0f2f5',
        fontFamily: 'Arial, sans-serif'
    },
    card: {
        backgroundColor: 'white',
        padding: '40px',
        borderRadius: '10px',
        boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
        textAlign: 'center',
        maxWidth: '400px',
        width: '100%'
    },
    title: {
        color: '#333',
        marginBottom: '10px'
    },
    subtitle: {
        color: '#666',
        marginBottom: '30px'
    },
    buttonGroup: {
        display: 'flex',
        flexDirection: 'column',
        gap: '15px'
    },
    loginButton: {
        padding: '12px',
        fontSize: '16px',
        backgroundColor: '#007bff',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer'
    },
    registerButton: {
        padding: '12px',
        fontSize: '16px',
        backgroundColor: '#28a745',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer'
    }
};

export default LandingPage;