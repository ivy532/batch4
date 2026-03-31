import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useCart } from '../context/CartContext';
 
const Orders = () => {
  const [orders, setOrders] = useState([]);
  const { fetchCart } = useCart();
 
  useEffect(() => { api.get('/orders/my').then(r => setOrders(r.data.data)); }, []);
 
  const reorder = async (orderId) => {
    await api.post(`/orders/${orderId}/reorder`);
    fetchCart();
    alert('Items added to cart!');
  };
 
  if (!orders.length) return (
    <div className="container" style={{padding:'80px 0',textAlign:'center'}}>
      <div style={{fontSize:'4rem',marginBottom:16}}>📦</div>
      <h2>No orders yet</h2>
      <p style={{color:'var(--text-muted)'}}>Your order history will appear here</p>
    </div>
  );
 
  return (
    <div className="container" style={{padding:'40px 0'}}>
      <h2 className="section-title">My Orders</h2>
      <p className="section-sub">Your complete order history</p>
      {orders.map(order => (
        <div key={order.id} className="order-card">
          <div style={{display:'flex',justifyContent:'space-between',alignItems:'flex-start',marginBottom:16}}>
            <div>
              <h3 style={{fontFamily:'DM Sans',fontWeight:700}}>Order #{order.id}</h3>
              <p style={{color:'var(--text-muted)',fontSize:'0.875rem'}}>{new Date(order.createdAt).toLocaleString()}</p>
            </div>
            <div style={{display:'flex',gap:12,alignItems:'center'}}>
              <span className={`status-badge status-${order.status}`}>{order.status}</span>
              <button className="btn btn-outline btn-sm" style={{background:'transparent',color:'var(--primary)',border:'2px solid var(--primary)'}} onClick={() => reorder(order.id)}>🔁 Reorder</button>
            </div>
          </div>
          <div style={{borderTop:'1px solid var(--border)',paddingTop:16}}>
            {order.items?.map((item, i) => (
              <div key={i} style={{display:'flex',justifyContent:'space-between',marginBottom:8}}>
                <span>{item.productName} ({item.size}) × {item.quantity}</span>
                <span style={{fontWeight:600}}>₹{item.subtotal?.toFixed(2)}</span>
              </div>
            ))}
            <div style={{display:'flex',justifyContent:'space-between',marginTop:12,paddingTop:12,borderTop:'1px solid var(--border)',fontWeight:700}}>
              <span>Total</span>
              <span style={{color:'var(--primary)',fontSize:'1.1rem'}}>₹{order.totalAmount}</span>
            </div>
            {order.loyaltyPointsEarned > 0 && (
              <p style={{color:'var(--secondary)',fontSize:'0.875rem',marginTop:8}}>🌟 You earned {order.loyaltyPointsEarned} loyalty points on this order</p>
            )}
          </div>
        </div>
      ))}
    </div>
  );
};
 
export default Orders;