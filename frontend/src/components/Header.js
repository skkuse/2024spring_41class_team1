import React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';

const Logo = styled('img')({
  height: 40,
  cursor: 'pointer', // 마우스 커서 모양 변경
});

const ToolbarContainer = styled(Toolbar)({
  display: 'flex',
  justifyContent: 'space-between',
});

const Header = () => {
  const navigate = useNavigate();

  const handleLoginClick = () => {
    navigate('/login');
  };

  const handleLogoClick = () => {
    navigate('/');
  };

  return (
    <AppBar position="static">
      <ToolbarContainer>
        <Logo src="/logo.png" alt="Logo" onClick={handleLogoClick} />
        <Button color="inherit" onClick={handleLoginClick}>
          로그인/회원가입
        </Button>
      </ToolbarContainer>
    </AppBar>
  );
};

export default Header;
