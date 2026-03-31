import React, { createContext, useContext, useState, useEffect } from 'react';
 
const AuthContext = createContext();
 
export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(() => {
    const u = localStorage.getItem('user');
    return u ? JSON.parse(u) : null;
  });
 
  const login = (authData) => {
    setToken(authData.token);
    setUser({ name: authData.name, email: authData.email, role: authData.role, loyaltyPoints: authData.loyaltyPoints });
    localStorage.setItem('token', authData.token);
    localStorage.setItem('user', JSON.stringify(authData));
  };
 
  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  };
 
  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
 
export const useAuth = () => useContext(AuthContext);
 