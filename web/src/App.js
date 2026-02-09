import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import Dashboard from './Dashboard';
import LandingPage from './LandingPage'; // Import the new page

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* ROOT ROUTE: Show Landing Page first */}
        <Route path="/" element={<LandingPage />} />
        
        {/* Other Routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;