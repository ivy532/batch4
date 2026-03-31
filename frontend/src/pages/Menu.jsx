import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
 
const Menu = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [activeCategory, setActiveCategory] = useState(null);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const { addToCart } = useCart();
  const { token } = useAuth();
  const navigate = useNavigate();
 
  useEffect(() => {
    Promise.all([api.get('/products'), api.get('/categories')]).then(([p, c]) => {
      setProducts(p.data.data);
      setCategories(c.data.data);
      setLoading(false);
    });
  }, []);
 
  const filtered = products.filter(p => {
    const matchCat = !activeCategory || p.categoryId === activeCategory;
    const matchSearch = !search || p.name.toLowerCase().includes(search.toLowerCase());
    return matchCat && matchSearch;
  });
 
  const minPrice = (p) => p.packagingOptions?.length ? Math.min(...p.packagingOptions.map(o => o.price)) : 0;
 
  return (
    <div className="container">
      <div style={{padding:'32px 0 0'}}>
        <h2 className="section-title">Our Menu</h2>
        <p className="section-sub">Fresh, delicious, delivered fast</p>
        <input className="form-input" placeholder="🔍 Search pizza, drinks, breads..."
          value={search} onChange={e => setSearch(e.target.value)}
          style={{maxWidth:400,marginBottom:8}} />
      </div>
 
      <div className="category-tabs">
        <button className={`tab ${!activeCategory ? 'active' : ''}`} onClick={() => setActiveCategory(null)}>All</button>
        {categories.map(c => (
          <button key={c.id} className={`tab ${activeCategory === c.id ? 'active' : ''}`}
            onClick={() => setActiveCategory(c.id)}>{c.name}</button>
        ))}
      </div>
 
      {loading ? <p>Loading...</p> : (
        <div className="product-grid">
          {filtered.map(product => (
            <div key={product.id} className="product-card" onClick={() => navigate(`/product/${product.id}`)}>
              <div style={{height:200,background:'linear-gradient(135deg,#f1f3f5,#dee2e6)',display:'flex',alignItems:'center',justifyContent:'center',fontSize:'4rem'}}>
                {product.categoryName === 'Pizza' ? '🍕' : product.categoryName === 'Cold Drinks' ? '🥤' : '🍞'}
              </div>
              <div className="product-card-body">
                <span className={`badge badge-${product.categoryName === 'Pizza' ? 'pizza' : product.categoryName === 'Cold Drinks' ? 'drink' : 'bread'}`}>
                  {product.brandName}
                </span>
                <h3>{product.name}</h3>
                <p>{product.description}</p>
                <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
                  <span className="price-tag">from ₹{minPrice(product)}</span>
                  <button className="btn btn-primary btn-sm"
                    onClick={e => { e.stopPropagation(); token ? navigate(`/product/${product.id}`) : navigate('/login'); }}>
                    Order
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
 
export default Menu;