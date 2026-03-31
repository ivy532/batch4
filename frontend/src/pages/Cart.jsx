import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
 
const Cart = () => {
  const { cart, updateItem, removeItem } = useCart();
  const navigate = useNavigate();
 
  if (!cart.items?.length) return (
    <div className="container" style={{padding:'80px 0',textAlign:'center'}}>
      <div style={{fontSize:'5rem',marginBottom:20}}>🛒</div>
      <h2>Your cart is empty</h2>
      <p style={{color:'var(--text-muted)',marginBottom:32}}>Add some delicious items from our menu!</p>
      <Link to="/menu" className="btn btn-primary">Browse Menu</Link>
    </div>
  );
 
  return (
    <div className="container cart-page">
      <h2 className="section-title">Shopping Cart</h2>
      <p className="section-sub">{cart.items.length} item(s) in your cart</p>
      <div style={{display:'grid',gridTemplateColumns:'1fr 360px',gap:32}}>
        <div>
          {cart.items.map(item => (
            <div key={item.cartItemId} className="cart-item">
              <div style={{width:80,height:80,background:'linear-gradient(135deg,#f1f3f5,#dee2e6)',borderRadius:8,display:'flex',alignItems:'center',justifyContent:'center',fontSize:'2.5rem',flexShrink:0}}>
                🍕
              </div>
              <div style={{flex:1}}>
                <h4 style={{fontFamily:'DM Sans',fontWeight:700}}>{item.productName}</h4>
                <p style={{color:'var(--text-muted)',fontSize:'0.875rem'}}>{item.size} • {item.type}</p>
                <p style={{color:'var(--primary)',fontWeight:700}}>₹{item.price} each</p>
              </div>
              <div className="qty-controls">
                <button className="qty-btn" onClick={() => updateItem(item.cartItemId, item.quantity - 1)}>−</button>
                <span style={{fontWeight:700}}>{item.quantity}</span>
                <button className="qty-btn" onClick={() => updateItem(item.cartItemId, item.quantity + 1)}>+</button>
              </div>
              <div style={{textAlign:'right',minWidth:80}}>
                <p style={{fontWeight:700,fontSize:'1.1rem'}}>₹{item.subtotal?.toFixed(2)}</p>
                <button onClick={() => removeItem(item.cartItemId)} style={{color:'var(--primary)',background:'none',border:'none',cursor:'pointer',fontSize:'0.8rem',marginTop:4}}>Remove</button>
              </div>
            </div>
          ))}
        </div>
        <div>
          <div className="checkout-card" style={{position:'sticky',top:90}}>
            <h3 style={{fontFamily:'DM Sans',fontWeight:700,marginBottom:20}}>Order Summary</h3>
            <div style={{display:'flex',justifyContent:'space-between',marginBottom:12,color:'var(--text-muted)'}}>
              <span>Subtotal</span><span>₹{cart.totalAmount?.toFixed(2)}</span>
            </div>
            <div style={{display:'flex',justifyContent:'space-between',marginBottom:12,color:'var(--text-muted)'}}>
              <span>Delivery</span><span style={{color:'var(--secondary)'}}>Free</span>
            </div>
            <hr style={{margin:'16px 0'}} />
            <div style={{display:'flex',justifyContent:'space-between',fontWeight:700,fontSize:'1.2rem',marginBottom:24}}>
              <span>Total</span><span style={{color:'var(--primary)'}}>₹{cart.totalAmount?.toFixed(2)}</span>
            </div>
            <button className="btn btn-primary" style={{width:'100%',justifyContent:'center'}} onClick={() => navigate('/checkout')}>
              Proceed to Checkout →
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
 
export default Cart;