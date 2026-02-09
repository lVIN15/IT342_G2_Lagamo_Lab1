import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Register() {
    const navigate = useNavigate();
    
    // State matching your ERD fields exactly
    const [formData, setFormData] = useState({
        firstname: '',
        middlename: '',
        lastname: '',
        street: '',
        barangay: '',
        municipality: '',
        province: '',
        country: '',
        contactNumber: '',
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Matches Backend Controller: @PostMapping("/register")
            await axios.post('http://localhost:8080/api/auth/register', formData);
            alert("Registration Successful! Please Login.");
            navigate('/login'); // Redirect to Login as per Activity Diagram
        } catch (error) {
            console.error("Error registering:", error);
            alert("Registration Failed. Email might be taken.");
        }
    };

    return (
        <div style={{ padding: '20px', maxWidth: '400px', margin: 'auto' }}>
            <h2>Register Account</h2>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                <input name="firstname" placeholder="First Name" onChange={handleChange} required />
                <input name="middlename" placeholder="Middle Name" onChange={handleChange} />
                <input name="lastname" placeholder="Last Name" onChange={handleChange} required />
                
                <h3>Address</h3>
                <input name="street" placeholder="Street" onChange={handleChange} required />
                <input name="barangay" placeholder="Barangay" onChange={handleChange} required />
                <input name="municipality" placeholder="Municipality" onChange={handleChange} required />
                <input name="province" placeholder="Province" onChange={handleChange} required />
                <input name="country" placeholder="Country" onChange={handleChange} required />

                <h3>Credentials</h3>
                <input name="contactNumber" placeholder="Contact Number" onChange={handleChange} required />
                <input name="email" type="email" placeholder="Email" onChange={handleChange} required />
                <input name="password" type="password" placeholder="Password" onChange={handleChange} required />

                <button type="submit" style={{ padding: '10px', background: 'blue', color: 'white' }}>Register</button>
            </form>
            <p>Already have an account? <button onClick={() => navigate('/login')}>Login here</button></p>
        </div>
    );
}

export default Register;