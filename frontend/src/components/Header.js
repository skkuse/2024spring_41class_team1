// src/components/Header.js
import React from 'react';
import { AppBar, Toolbar, Button } from '@mui/material';
import { styled } from '@mui/material/styles';
import { useAuth } from '../contexts/AuthContext';

const Logo = styled('img')({
  height: 40,
  cursor: 'pointer',
});

const ToolbarContainer = styled(Toolbar)({
  display: 'flex',
  justifyContent: 'space-between',
});

const Header = () => {
  const { userRole, logout } = useAuth();
  const handleMyPageClick = () => {
    // 마이페이지로 이동
    window.location.href = '/mypage';
  };

  return (
    <AppBar position="static">
      <ToolbarContainer>
        <Logo src="/logo.png" alt="Logo" onClick={() => window.location.href = '/'} />
        {userRole === 'ROLE_USER' && (
          <>
            <Button color="inherit" onClick={handleMyPageClick}>마이페이지</Button>
            <Button color="inherit" onClick={logout}>로그아웃</Button>
          </>
        )}
        {userRole === 'ROLE_ADMIN' && (
          <Button color="inherit" onClick={logout}>로그아웃</Button>
        )}
        {!userRole && (
          <Button color="inherit" onClick={() => window.location.href = '/login'}>로그인/회원가입</Button>
        )}
      </ToolbarContainer>
    </AppBar>
  );
};

export default Header;
