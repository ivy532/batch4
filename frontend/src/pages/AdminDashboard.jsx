import React, { useEffect, useState } from 'react';
import api from '../services/api';
 
const AdminDashboard = () => {
  const [orders, setOrders] = useState([]);
  const [tab, setTab] = useState('orders');
 
  useEffect(() => {
    if (tab === 'orders') api.get('/admin/orders').then(r => setOrders(r.data.data));
  }, [tab]);
 
  const updateStatus = async (id, status) => {
    await api.put(`/admin/orders/${id}/status?status=${status}`);
    const res = await api.get('/admin/orders');
    setOrders(res.data.data);
  };
 
  const statuses = ['PENDING','CONFIRMED','PREPARING','OUT_FOR_DELIVERY','DELIVERED','CANCELLED'];
  const total = orders.reduce((s, o) => s + (o.totalAmount || 0), 0);
  const delivered = orders.filter(o => o.status === 'DELIVERED').length;
 
  return (
    <div className="admin-layout">
      <div className="admin-sidebar">
        <div style={{padding:'0 24px 32px',fontSize:'1.2rem',fontWeight:700,fontFamily:'Playfair Display,serif',color:'white'}}>
          🛠️ Admin Panel
        </div>
        {[['orders','📦 Orders'],['products','🍕 Products'],['coupons','🏷️ Coupons']].map(([t,label]) => (
          <a key={t} href="#" className={tab === t ? 'active' : ''} onClick={e => {e.preventDefault(); setTab(t);}}>{label}</a>
        ))}
      </div>
      <div className="admin-content">
        <h2 className="section-title">Dashboard</h2>
        <div className="stats-grid">
          {[['Total Orders', orders.length,'📦'],['Revenue','₹'+total.toFixed(2),'💰'],['Delivered',delivered,'✅'],['Pending',orders.filter(o=>o.status==='PENDING').length,'⏳']].map(([l,v,icon]) => (
            <div key={l} className="stat-card">
              <div style={{fontSize:'2rem',marginBottom:8}}>{icon}</div>
              <div className="stat-value">{v}</div>
              <div className="stat-label">{l}</div>
            </div>
          ))}
        </div>
 
        {tab === 'orders' && (
          <div style={{background:'white',borderRadius:12,padding:24,boxShadow:'var(--shadow)'}}>
            <h3 style={{fontFamily:'DM Sans',fontWeight:700,marginBottom:20}}>All Orders</h3>
            <table style={{width:'100%',borderCollapse:'collapse'}}>
              <thead>
                <tr style={{borderBottom:'2px solid var(--border)'}}>
                  {['Order ID','Customer','Amount','Status','Update Status'].map(h => (
                    <th key={h} style={{padding:'12px 16px',textAlign:'left',fontWeight:700,fontSize:'0.875rem',color:'var(--text-muted)'}}>{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {orders.map(order => (
                  <tr key={order.id} style={{borderBottom:'1px solid var(--border)'}}>
                    <td style={{padding:'12px 16px',fontWeight:700}}>#{order.id}</td>
                    <td style={{padding:'12px 16px'}}>{order.items?.[0]?.productName || '-'}</td>
                    <td style={{padding:'12px 16px',fontWeight:700,color:'var(--primary)'}}>₹{order.totalAmount}</td>
                    <td style={{padding:'12px 16px'}}><span className={`status-badge status-${order.status}`}>{order.status}</span></td>
                    <td style={{padding:'12px 16px'}}>
                      <select style={{padding:'6px 12px',borderRadius:6,border:'2px solid var(--border)',fontFamily:'DM Sans'}}
                        value={order.status} onChange={e => updateStatus(order.id, e.target.value)}>
                        {statuses.map(s => <option key={s} value={s}>{s}</option>)}
                      </select>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};
 
export default AdminDashboard;