import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Register.css';

const STEP_LABELS = ['Personal Info', 'Address', 'Credentials'];

/* ---------- password validation helpers ---------- */
const PASSWORD_RULES = [
    { key: 'length', label: 'At least 8 characters', test: (pw) => pw.length >= 8 },
    { key: 'uppercase', label: 'One uppercase letter (A-Z)', test: (pw) => /[A-Z]/.test(pw) },
    { key: 'lowercase', label: 'One lowercase letter (a-z)', test: (pw) => /[a-z]/.test(pw) },
    { key: 'number', label: 'One number (0-9)', test: (pw) => /[0-9]/.test(pw) },
    { key: 'special', label: 'One special character (!@#$…)', test: (pw) => /[^A-Za-z0-9]/.test(pw) },
];

function Register() {
    const navigate = useNavigate();
    const [step, setStep] = useState(1);

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

    const [confirmPassword, setConfirmPassword] = useState('');
    const [errors, setErrors] = useState({});

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
        // Clear related errors on change
        if (e.target.name === 'password') {
            setErrors((prev) => ({ ...prev, password: '', confirmPassword: '' }));
        }
    };

    /* ---- validation ---- */
    const validatePassword = () => {
        const newErrors = {};
        const pw = formData.password;

        const failedRules = PASSWORD_RULES.filter((r) => !r.test(pw));
        if (failedRules.length > 0) {
            newErrors.password = 'Password does not meet all requirements';
        }

        if (confirmPassword !== pw) {
            newErrors.confirmPassword = 'Passwords do not match';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const [toast, setToast] = useState(null);

    const showToast = (type, message) => {
        setToast({ type, message });
        setTimeout(() => setToast(null), 3000);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validatePassword()) return;

        try {
            await axios.post('http://localhost:8080/api/auth/register', formData);
            navigate('/login', { state: { registered: true } });
        } catch (error) {
            console.error("Error registering:", error);
            showToast('error', 'Registration failed. Email might be taken.');
        }
    };

    const next = () => setStep((s) => Math.min(s + 1, 3));
    const back = () => setStep((s) => Math.max(s - 1, 1));

    /* ---- step indicator ---- */
    const StepIndicator = () => (
        <div className="step-indicator">
            {STEP_LABELS.map((_, i) => {
                const num = i + 1;
                let circleClass = 'step-indicator__circle';
                if (num < step) circleClass += ' step-indicator__circle--completed';
                else if (num === step) circleClass += ' step-indicator__circle--active';

                return (
                    <React.Fragment key={num}>
                        <div className={circleClass}>
                            {num < step ? '✓' : num}
                        </div>
                        {num < STEP_LABELS.length && (
                            <div
                                className={
                                    'step-indicator__line' +
                                    (num < step ? ' step-indicator__line--completed' : '')
                                }
                            />
                        )}
                    </React.Fragment>
                );
            })}
        </div>
    );

    return (
        <div className="register-page">
            {/* ---- toast notification ---- */}
            {toast && (
                <div className={`toast toast--${toast.type}`} key={Date.now()}>
                    <span className="toast__icon">
                        {toast.type === 'success' ? '✓' : '✗'}
                    </span>
                    {toast.message}
                </div>
            )}

            {/* ---- decorative shapes ---- */}
            <div className="reg-shape reg-shape--magenta" />
            <div className="reg-shape reg-shape--cyan-1" />
            <div className="reg-shape reg-shape--cyan-2" />
            <div className="reg-shape reg-shape--lime-1" />
            <div className="reg-shape reg-shape--lime-2" />
            <div className="reg-shape reg-shape--blue" />

            {/* ---- card ---- */}
            <div className="register-card">
                <button
                    className="register-card__back"
                    onClick={() => navigate('/')}
                    type="button"
                    aria-label="Back to home"
                >
                    ←
                </button>
                <img
                    className="register-card__logo"
                    src="/miniapplogo.png"
                    alt="App Logo"
                />
                <h1 className="register-card__title">Create Account</h1>
                <p className="register-card__subtitle">{STEP_LABELS[step - 1]}</p>

                <StepIndicator />

                <form className="register-card__form" onSubmit={handleSubmit}>
                    <div className="register-card__steps">
                        {/* ===== STEP 1 — Personal Info ===== */}
                        <div
                            className={
                                'register-card__step' +
                                (step === 1 ? ' register-card__step--active' : '')
                            }
                            key={'step-1-' + step}
                        >
                            <div className="register-card__field">
                                <label className="register-card__label" htmlFor="reg-firstname">
                                    First Name
                                </label>
                                <input
                                    id="reg-firstname"
                                    className="register-card__input"
                                    name="firstname"
                                    placeholder="Juan"
                                    value={formData.firstname}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="register-card__field">
                                <label className="register-card__label" htmlFor="reg-middlename">
                                    Middle Name
                                </label>
                                <input
                                    id="reg-middlename"
                                    className="register-card__input"
                                    name="middlename"
                                    placeholder="Santos"
                                    value={formData.middlename}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="register-card__field">
                                <label className="register-card__label" htmlFor="reg-lastname">
                                    Last Name
                                </label>
                                <input
                                    id="reg-lastname"
                                    className="register-card__input"
                                    name="lastname"
                                    placeholder="Dela Cruz"
                                    value={formData.lastname}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                        </div>

                        {/* ===== STEP 2 — Address ===== */}
                        <div
                            className={
                                'register-card__step' +
                                (step === 2 ? ' register-card__step--active' : '')
                            }
                            key={'step-2-' + step}
                        >
                            <div className="register-card__grid">
                                <div className="register-card__field register-card__field--full">
                                    <label className="register-card__label" htmlFor="reg-street">
                                        Street
                                    </label>
                                    <input
                                        id="reg-street"
                                        className="register-card__input"
                                        name="street"
                                        placeholder="123 Main St."
                                        value={formData.street}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="register-card__field">
                                    <label className="register-card__label" htmlFor="reg-barangay">
                                        Barangay
                                    </label>
                                    <input
                                        id="reg-barangay"
                                        className="register-card__input"
                                        name="barangay"
                                        placeholder="Barangay"
                                        value={formData.barangay}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="register-card__field">
                                    <label className="register-card__label" htmlFor="reg-municipality">
                                        Municipality
                                    </label>
                                    <input
                                        id="reg-municipality"
                                        className="register-card__input"
                                        name="municipality"
                                        placeholder="Municipality"
                                        value={formData.municipality}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="register-card__field">
                                    <label className="register-card__label" htmlFor="reg-province">
                                        Province
                                    </label>
                                    <input
                                        id="reg-province"
                                        className="register-card__input"
                                        name="province"
                                        placeholder="Province"
                                        value={formData.province}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                                <div className="register-card__field">
                                    <label className="register-card__label" htmlFor="reg-country">
                                        Country
                                    </label>
                                    <input
                                        id="reg-country"
                                        className="register-card__input"
                                        name="country"
                                        placeholder="Philippines"
                                        value={formData.country}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>
                        </div>

                        {/* ===== STEP 3 — Credentials ===== */}
                        <div
                            className={
                                'register-card__step' +
                                (step === 3 ? ' register-card__step--active' : '')
                            }
                            key={'step-3-' + step}
                        >
                            <div className="register-card__field">
                                <label className="register-card__label" htmlFor="reg-contact">
                                    Contact Number
                                </label>
                                <input
                                    id="reg-contact"
                                    className="register-card__input"
                                    name="contactNumber"
                                    placeholder="+63 912 345 6789"
                                    value={formData.contactNumber}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="register-card__field">
                                <label className="register-card__label" htmlFor="reg-email">
                                    Email
                                </label>
                                <input
                                    id="reg-email"
                                    className="register-card__input"
                                    name="email"
                                    type="email"
                                    placeholder="username@email.com"
                                    value={formData.email}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            {/* Password */}
                            <div className="register-card__field">
                                <label className="register-card__label" htmlFor="reg-password">
                                    Password
                                </label>
                                <input
                                    id="reg-password"
                                    className={
                                        'register-card__input' +
                                        (errors.password ? ' register-card__input--error' : '')
                                    }
                                    name="password"
                                    type="password"
                                    placeholder="Create a strong password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    required
                                />

                                {/* Password strength rules */}
                                {formData.password.length > 0 && (
                                    <ul className="register-card__pw-rules">
                                        {PASSWORD_RULES.map((rule) => {
                                            const passed = rule.test(formData.password);
                                            return (
                                                <li
                                                    key={rule.key}
                                                    className={
                                                        'register-card__pw-rule' +
                                                        (passed ? ' register-card__pw-rule--pass' : '')
                                                    }
                                                >
                                                    <span className="register-card__pw-rule-icon">
                                                        {passed ? '✓' : '✗'}
                                                    </span>
                                                    {rule.label}
                                                </li>
                                            );
                                        })}
                                    </ul>
                                )}
                                {errors.password && (
                                    <p className="register-card__error">{errors.password}</p>
                                )}
                            </div>

                            {/* Confirm Password */}
                            <div className="register-card__field">
                                <label className="register-card__label" htmlFor="reg-confirm-password">
                                    Confirm Password
                                </label>
                                <input
                                    id="reg-confirm-password"
                                    className={
                                        'register-card__input' +
                                        (errors.confirmPassword ? ' register-card__input--error' : '')
                                    }
                                    type="password"
                                    placeholder="Re-enter your password"
                                    value={confirmPassword}
                                    onChange={(e) => {
                                        setConfirmPassword(e.target.value);
                                        setErrors((prev) => ({ ...prev, confirmPassword: '' }));
                                    }}
                                    required
                                />
                                {errors.confirmPassword && (
                                    <p className="register-card__error">{errors.confirmPassword}</p>
                                )}
                            </div>
                        </div>
                    </div>

                    {/* ---- buttons ---- */}
                    <div className="register-card__buttons">
                        {step > 1 && (
                            <button
                                id="reg-back"
                                className="register-card__btn-secondary"
                                type="button"
                                onClick={back}
                            >
                                Back
                            </button>
                        )}
                        {step < 3 ? (
                            <button
                                id="reg-next"
                                className="register-card__btn-primary"
                                type="button"
                                onClick={next}
                            >
                                Next
                            </button>
                        ) : (
                            <button
                                id="reg-submit"
                                className="register-card__btn-primary"
                                type="submit"
                            >
                                Register
                            </button>
                        )}
                    </div>
                </form>

                <p className="register-card__login">
                    Already have an account?{' '}
                    <button
                        id="reg-login-link"
                        className="register-card__login-link"
                        onClick={() => navigate('/login')}
                        type="button"
                    >
                        Login here
                    </button>
                </p>
            </div>
        </div>
    );
}

export default Register;