import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
 
const Login = () => {
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post('/auth/login', form);
      login(res.data.data);
      navigate('/menu');
    } catch (e) { setError(e.response?.data?.message || 'Login failed'); }
  };
 
  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2 className="auth-title">Welcome Back! 👋</h2>
        <p style={{color:'var(--text-muted)',marginBottom:32}}>Login to continue ordering</p>
        {error && <div style={{background:'#fff0f0',color:'var(--primary)',padding:'12px 16px',borderRadius:8,marginBottom:20}}>{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-input" type="email" placeholder="you@example.com"
              value={form.email} onChange={e => setForm({...form, email: e.target.value})} required />
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <input className="form-input" type="password" placeholder="••••••••"
              value={form.password} onChange={e => setForm({...form, password: e.target.value})} required />
          </div>
          <button type="submit" className="btn btn-primary" style={{width:'100%',justifyContent:'center',marginTop:8}}>Login →</button>
        </form>
        <p style={{textAlign:'center',marginTop:24,color:'var(--text-muted)'}}>
          Don't have an account? <Link to="/register" style={{color:'var(--primary)',fontWeight:600}}>Register</Link>
        </p>
      </div>
    </div>
  );
};
 
export default Login;
