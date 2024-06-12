import React, { createContext, useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({children} = {}) => {
  const [userRole, setUserRole] = useState(null); // 사용자의 역할 상태
  const navigate = useNavigate();

  const logout = () => {
    fetch('/logout', { method: 'GET' })
      .then(() => {
        setUserRole(null); // 로그아웃 시 역할 초기화
        navigate('/');
      })
      .catch(error => console.error('Logout failed', error));
  };

  const setRole = (role) => {
    setUserRole(role);
  };

  return (
    <AuthContext.Provider value={{ userRole, setRole, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
