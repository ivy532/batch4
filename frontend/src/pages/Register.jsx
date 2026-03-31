import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
 
const Register = () => {
  const [form, setForm] = useState({ name: '', email: '', password: '', phone: '' });
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post('/auth/register', form);
      login(res.data.data);
      navigate('/menu');
    } catch (e) { setError(e.response?.data?.message || 'Registration failed'); }
  };
 
  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2 className="auth-title">Join Us! 🎉</h2>
        <p style={{color:'var(--text-muted)',marginBottom:32}}>Create your account and start ordering</p>
        {error && <div style={{background:'#fff0f0',color:'var(--primary)',padding:'12px 16px',borderRadius:8,marginBottom:20}}>{error}</div>}
        <form onSubmit={handleSubmit}>
          {[['name','Full Name','text','John Doe'],['email','Email','email','you@example.com'],['password','Password','password','Min 6 characters'],['phone','Phone (optional)','tel','+91 98765 43210']].map(([field,label,type,ph]) => (
            <div className="form-group" key={field}>
              <label className="form-label">{label}</label>
              <input className="form-input" type={type} placeholder={ph}
                value={form[field]} onChange={e => setForm({...form, [field]: e.target.value})}
                required={field !== 'phone'} />
            </div>
          ))}
          <button type="submit" className="btn btn-primary" style={{width:'100%',justifyContent:'center',marginTop:8}}>Create Account →</button>
        </form>
        <p style={{textAlign:'center',marginTop:24,color:'var(--text-muted)'}}>
          Already have an account? <Link to="/login" style={{color:'var(--primary)',fontWeight:600}}>Login</Link>
        </p>
      </div>
    </div>
  );
};
 
export default Register;