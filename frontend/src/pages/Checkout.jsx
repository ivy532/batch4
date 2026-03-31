import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
 
const Checkout = () => {
  const { cart, fetchCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ deliveryAddress: '', couponCode: '', loyaltyPointsToUse: 0 });
  const [couponResult, setCouponResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
 
  const validateCoupon = async () => {
    try {
      const res = await api.get(`/coupons/validate?code=${form.couponCode}&amount=${cart.totalAmount}`);
      setCouponResult(res.data.data);
    } catch { setCouponResult({ valid: false, message: 'Invalid coupon' }); }
  };
 
  const finalTotal = () => {
    let t = cart.totalAmount || 0;
    if (couponResult?.valid) t -= couponResult.discountAmount;
    t -= (form.loyaltyPointsToUse || 0) / 10;
    return Math.max(0, t).toFixed(2);
  };
 
  const handleOrder = async () => {
    if (!form.deliveryAddress) { setError('Please enter delivery address'); return; }
    setLoading(true);
    try {
      await api.post('/orders/place', form);
      fetchCart();
      navigate('/orders');
    } catch (e) {
      setError(e.response?.data?.message || 'Order failed');
    }
    setLoading(false);
  };
 
  return (
    <div className="container">
      <div className="checkout-grid">
        <div>
          <h2 className="section-title">Checkout</h2>
          <div className="checkout-card" style={{marginTop:20}}>
            <h3 style={{fontFamily:'DM Sans',fontWeight:700,marginBottom:20}}>Delivery Details</h3>
            <div className="form-group">
              <label className="form-label">Delivery Address *</label>
              <textarea className="form-input" rows={3} placeholder="Enter your full delivery address..."
                value={form.deliveryAddress} onChange={e => setForm({...form, deliveryAddress: e.target.value})} />
            </div>
            <div className="form-group">
              <label className="form-label">Coupon Code</label>
              <div style={{display:'flex',gap:12}}>
                <input className="form-input" placeholder="Enter coupon code"
                  value={form.couponCode} onChange={e => setForm({...form, couponCode: e.target.value})} />
                <button className="btn btn-success btn-sm" onClick={validateCoupon}>Apply</button>
              </div>
              {couponResult && <p style={{marginTop:8,color:couponResult.valid ? 'var(--secondary)' : 'var(--primary)',fontWeight:500}}>{couponResult.message}</p>}
            </div>
            {user?.loyaltyPoints > 0 && (
              <div className="loyalty-banner">
                <span>🌟</span>
                <div>
                  <strong>You have {user.loyaltyPoints} loyalty points!</strong>
                  <p style={{fontSize:'0.85rem',opacity:0.9}}>Use {form.loyaltyPointsToUse} points = ₹{(form.loyaltyPointsToUse/10).toFixed(2)} off</p>
                  <input type="range" min={0} max={user.loyaltyPoints} value={form.loyaltyPointsToUse}
                    onChange={e => setForm({...form, loyaltyPointsToUse: parseInt(e.target.value)})}
                    style={{width:'100%',marginTop:8}} />
                </div>
              </div>
            )}
            {error && <p style={{color:'var(--primary)',fontWeight:500,marginBottom:16}}>{error}</p>}
            <button className="btn btn-primary" style={{width:'100%',justifyContent:'center'}} onClick={handleOrder} disabled={loading}>
              {loading ? 'Placing Order...' : `🎉 Place Order — ₹${finalTotal()}`}
            </button>
          </div>
        </div>
        <div className="order-summary-sticky">
          <div className="checkout-card">
            <h3 style={{fontFamily:'DM Sans',fontWeight:700,marginBottom:20}}>Order Items</h3>
            {cart.items?.map(item => (
              <div key={item.cartItemId} style={{display:'flex',justifyContent:'space-between',marginBottom:12}}>
                <div>
                  <p style={{fontWeight:600}}>{item.productName}</p>
                  <p style={{fontSize:'0.8rem',color:'var(--text-muted)'}}>{item.size} × {item.quantity}</p>
                </div>
                <p style={{fontWeight:700}}>₹{item.subtotal?.toFixed(2)}</p>
              </div>
            ))}
            <hr style={{margin:'16px 0'}} />
            {couponResult?.valid && (
              <div style={{display:'flex',justifyContent:'space-between',color:'var(--secondary)',fontWeight:600,marginBottom:8}}>
                <span>Coupon Discount</span><span>−₹{couponResult.discountAmount}</span>
              </div>
            )}
            {form.loyaltyPointsToUse > 0 && (
              <div style={{display:'flex',justifyContent:'space-between',color:'var(--secondary)',fontWeight:600,marginBottom:8}}>
                <span>Loyalty Discount</span><span>−₹{(form.loyaltyPointsToUse/10).toFixed(2)}</span>
              </div>
            )}
            <div style={{display:'flex',justifyContent:'space-between',fontWeight:700,fontSize:'1.2rem'}}>
              <span>Total</span><span style={{color:'var(--primary)'}}>₹{finalTotal()}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Checkout;