import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
 
const Home = () => {
  const { user } = useAuth();
  return (
    <div>
      <section className="hero">
        <div className="container">
          <h1>🍕 Freshness Delivered<br/>Right to Your Door</h1>
          <p>Order Pizza, Cold Drinks & Fresh Breads from your favorite brands.<br/>Multiple sizes, real-time inventory, loyalty rewards.</p>
          <div className="hero-btns">
            <Link to="/menu" className="btn btn-primary">🛒 Order Now</Link>
            {!user && <Link to="/register" className="btn btn-outline">Join Free</Link>}
          </div>
        </div>
      </section>
 
      <section className="section">
        <div className="container">
          <h2 className="section-title" style={{textAlign:'center'}}>Why Choose Us?</h2>
          <p className="section-sub" style={{textAlign:'center'}}>Everything you love, one platform</p>
          <div style={{display:'grid',gridTemplateColumns:'repeat(auto-fit,minmax(220px,1fr))',gap:24,marginTop:40}}>
            {[
              {icon:'📦',title:'Smart Packaging',desc:'Choose your size — Small, Medium, or Large with transparent pricing'},
              {icon:'⚡',title:'Real-Time Inventory',desc:'Always up-to-date stock counts. No surprise cancellations'},
              {icon:'🌟',title:'Loyalty Rewards',desc:'Earn points on every order, redeem on your next purchase'},
              {icon:'🏷️',title:'Exclusive Coupons',desc:'Save more with seasonal deals, coupon codes, and offers'},
            ].map((f,i) => (
              <div key={i} style={{background:'white',padding:28,borderRadius:12,boxShadow:'0 4px 20px rgba(0,0,0,0.06)',textAlign:'center'}}>
                <div style={{fontSize:'2.5rem',marginBottom:12}}>{f.icon}</div>
                <h3 style={{fontFamily:'DM Sans',fontWeight:700,marginBottom:8}}>{f.title}</h3>
                <p style={{color:'#6c757d',fontSize:'0.9rem'}}>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
 
      <section className="section" style={{background:'linear-gradient(135deg,#1a1a2e,#16213e)',color:'white',textAlign:'center'}}>
        <div className="container">
          <h2 style={{fontSize:'2rem',marginBottom:16}}>Ready to Order?</h2>
          <p style={{opacity:0.8,marginBottom:32}}>Browse 20+ products from top brands. Free delivery on orders above ₹300.</p>
          <Link to="/menu" className="btn btn-primary">Explore Menu →</Link>
        </div>
      </section>
    </div>
  );
};
 
export default Home;