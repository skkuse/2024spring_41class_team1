import React from 'react';
import { AppBar, Toolbar, Button, Box } from '@mui/material';  // Box 컴포넌트 추가
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
  const { userRole, logout } = useAuth(); //useAuth를 사용해 session 관리

  const handleMyPageClick = () => {
    window.location.href = userRole === 'ROLE_ADMIN' ? '/admin' : '/mypage'; // 경로 변경에 따른 조건 처리
  };

  return (
    <AppBar position="static">
      <ToolbarContainer>  {/* 상단 header 툴바*/}
        <Logo src="/logo.png" alt="Logo" onClick={() => window.location.href = '/'} />
        <Box sx={{ flexGrow: 1, display: 'flex', justifyContent: 'flex-end' }}> {/* 오른쪽 정렬을 위한 Box */}
          {userRole === 'ROLE_USER' && (  //사용자로 로그인 되어있는 경우 로그인/회원가입 버튼 대신 마이페이지 버튼과 로그아웃 버튼 표시
            <>
              <Button color="inherit" onClick={handleMyPageClick}>마이페이지</Button>
              <Button color="inherit" onClick={logout}>로그아웃</Button>
            </>
          )}
          {userRole === 'ROLE_ADMIN' && ( //관리자로 로그인 되어 있는 경우, 관리자 페이지 버튼과 로그아웃 버튼 표시
            <>
              <Button color="inherit" onClick={handleMyPageClick}>관리자페이지</Button>
              <Button color="inherit" onClick={logout}>로그아웃</Button>
            </>
          )}
          {!userRole && ( //로그인 세션이 존재하지 않는 경우, 로그인/회원가입 버튼 표시
            <Button color="inherit" onClick={() => window.location.href = '/login'}>로그인/회원가입</Button>
          )}
        </Box>
      </ToolbarContainer>
    </AppBar>
  );
};

export default Header;
