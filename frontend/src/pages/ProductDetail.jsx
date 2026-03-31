import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
 
const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [selected, setSelected] = useState(null);
  const [qty, setQty] = useState(1);
  const [added, setAdded] = useState(false);
  const { addToCart } = useCart();
  const { token } = useAuth();
  const navigate = useNavigate();
 
  useEffect(() => {
    api.get(`/products/${id}`).then(r => {
      setProduct(r.data.data);
      setSelected(r.data.data.packagingOptions?.[0]);
    });
  }, [id]);
 
  const handleAdd = async () => {
    if (!token) { navigate('/login'); return; }
    if (!selected) return;
    await addToCart(selected.id, qty);
    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };
 
  if (!product) return <div className="container" style={{padding:40}}>Loading...</div>;
 
  return (
    <div className="container" style={{padding:'40px 0'}}>
      <button onClick={() => navigate(-1)} className="btn btn-outline" style={{background:'transparent',color:'var(--dark)',border:'2px solid var(--border)',marginBottom:32}}>← Back</button>
      <div style={{display:'grid',gridTemplateColumns:'1fr 1fr',gap:48,background:'white',borderRadius:16,padding:48,boxShadow:'var(--shadow)'}}>
        <div style={{background:'linear-gradient(135deg,#f1f3f5,#dee2e6)',borderRadius:12,display:'flex',alignItems:'center',justifyContent:'center',fontSize:'8rem',minHeight:300}}>
          {product.categoryName === 'Pizza' ? '🍕' : product.categoryName === 'Cold Drinks' ? '🥤' : '🍞'}
        </div>
        <div>
          <span className="badge" style={{background:'#fff0f0',color:'var(--primary)'}}>{product.categoryName} • {product.brandName}</span>
          <h1 style={{fontSize:'2.2rem',margin:'12px 0'}}>{product.name}</h1>
          <p style={{color:'var(--text-muted)',marginBottom:24,lineHeight:1.7}}>{product.description}</p>
 
          <h3 style={{fontFamily:'DM Sans',fontWeight:700,marginBottom:12}}>Choose Size & Packaging</h3>
          <div className="packaging-grid">
            {product.packagingOptions?.map(pkg => (
              <div key={pkg.id} className={`packaging-option ${selected?.id === pkg.id ? 'selected' : ''}`}
                onClick={() => setSelected(pkg)}>
                <div className="size">{pkg.size}</div>
                <div className="type">{pkg.type}</div>
                <div className="price">₹{pkg.price}</div>
                {pkg.stockQuantity < 10 && <div style={{color:'orange',fontSize:'0.7rem',marginTop:4}}>Only {pkg.stockQuantity} left!</div>}
              </div>
            ))}
          </div>
 
          <div style={{display:'flex',alignItems:'center',gap:16,marginTop:24}}>
            <div className="qty-controls">
              <button className="qty-btn" onClick={() => setQty(Math.max(1, qty - 1))}>−</button>
              <span style={{fontWeight:700,fontSize:'1.2rem',minWidth:32,textAlign:'center'}}>{qty}</span>
              <button className="qty-btn" onClick={() => setQty(qty + 1)}>+</button>
            </div>
            {selected && <span style={{fontWeight:700,fontSize:'1.3rem',color:'var(--primary)'}}>₹{(selected.price * qty).toFixed(2)}</span>}
          </div>
 
          <button className="btn btn-primary" style={{marginTop:24,width:'100%',justifyContent:'center'}} onClick={handleAdd}>
            {added ? '✅ Added to Cart!' : '🛒 Add to Cart'}
          </button>
        </div>
      </div>
    </div>
  );
};
 
export default ProductDetail;