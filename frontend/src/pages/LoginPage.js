import React, { useState } from 'react';
import Header from '../components/Header';
import { Container, Typography, TextField, Button, Box, Link } from '@mui/material';
import { styled } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';

const LoginContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: '1px solid #dee2e6',
  borderRadius: 5,
  backgroundColor: '#ffffff',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  maxWidth: 400,
  margin: '50px auto',
});

const Form = styled(Box)({
  display: 'flex',
  flexDirection: 'column',
  gap: 15,
});

const SubmitButton = styled(Button)({
  marginTop: 15,
});

const LoginPage = () => {
  const navigate = useNavigate();
  const [id, setId] = useState('');
  const [pw, setPw] = useState('');

  const handleLoginClick = (event) => {
    event.preventDefault();
    if (id === 'admin' && pw === '1111') {
      navigate('/admin');
    } else if (id === 'user' && pw === '1111') {
      navigate('/mypage');
    } else {
      alert('Invalid ID or password');
    }
  };

  const handleSignUpClick = () => {
    navigate('/signup');
  };

  return (
    <div>
      <Header />
      <LoginContainer>
        <Typography variant="h4" component="h2" align="center">
          로그인
        </Typography>
        <Form component="form" onSubmit={handleLoginClick}>
          <TextField
            label="ID"
            variant="outlined"
            fullWidth
            value={id}
            onChange={(e) => setId(e.target.value)}
          />
          <TextField
            label="PW"
            type="password"
            variant="outlined"
            fullWidth
            value={pw}
            onChange={(e) => setPw(e.target.value)}
          />
          <SubmitButton variant="contained" color="primary" type="submit">
            로그인
          </SubmitButton>
          <Typography variant="body2" align="center">
            <Link href="#" onClick={handleSignUpClick}>
              회원가입
            </Link>
          </Typography>
        </Form>
      </LoginContainer>
    </div>
  );
};

export default LoginPage;
