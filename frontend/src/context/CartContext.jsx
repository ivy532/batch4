import React, { createContext, useContext, useState, useEffect } from 'react';
import { useAuth } from './AuthContext';
import api from '../services/api';
 
const CartContext = createContext();
 
export const CartProvider = ({ children }) => {
  const { token } = useAuth();
  const [cart, setCart] = useState({ items: [], totalAmount: 0 });
  const [cartCount, setCartCount] = useState(0);
 
  const fetchCart = async () => {
    if (!token) return;
    try {
      const res = await api.get('/cart');
      setCart(res.data.data);
      setCartCount(res.data.data.items?.length || 0);
    } catch (e) {}
  };
 
  useEffect(() => { fetchCart(); }, [token]);
 
  const addToCart = async (packagingId, quantity = 1) => {
    await api.post('/cart/add', { packagingId, quantity });
    await fetchCart();
  };
 
  const updateItem = async (cartItemId, quantity) => {
    await api.put(`/cart/item/${cartItemId}?quantity=${quantity}`);
    await fetchCart();
  };
 
  const removeItem = async (cartItemId) => {
    await api.delete(`/cart/item/${cartItemId}`);
    await fetchCart();
  };
 
  return (
    <CartContext.Provider value={{ cart, cartCount, addToCart, updateItem, removeItem, fetchCart }}>
      {children}
    </CartContext.Provider>
  );
};
 
export const useCart = () => useContext(CartContext);