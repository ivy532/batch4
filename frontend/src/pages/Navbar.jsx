import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
 
const Navbar = () => {
  const { user, logout } = useAuth();
  const { cartCount } = useCart();
  const navigate = useNavigate();
 
  const handleLogout = () => { logout(); navigate('/'); };
 
  return (
    <nav className="navbar">
      <div className="container navbar-inner">
        <Link to="/" className="navbar-brand">🍕 Retail<span>Store</span></Link>
        <div className="nav-links">
          <Link to="/menu" className="nav-link">Menu</Link>
          {user && <Link to="/orders" className="nav-link">My Orders</Link>}
          {user?.role === 'ADMIN' && <Link to="/admin" className="nav-link">Admin</Link>}
          {user ? (
            <>
              <span className="nav-link" style={{color:'rgba(255,255,255,0.6)'}}>Hi, {user.name.split(' ')[0]} 👋</span>
              <button onClick={handleLogout} className="nav-link" style={{background:'none',border:'none',cursor:'pointer',color:'rgba(255,255,255,0.8)'}}>Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-link">Login</Link>
              <Link to="/register" className="nav-link">Register</Link>
            </>
          )}
          {user && (
            <Link to="/cart" className="cart-btn">
              🛒 Cart <span className="cart-badge">{cartCount}</span>
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};
 
export default Navbar;