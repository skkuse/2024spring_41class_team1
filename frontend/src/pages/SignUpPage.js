import React, { useState } from 'react';
import Header from '../components/Header';
import { Container, Typography, TextField, Button, Box } from '@mui/material';
import { styled } from '@mui/material/styles';

const SignUpContainer = styled(Container)({
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

const SignUpPage = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleSignUp = () => {
    if (!username || !email || !password || !confirmPassword) {
      alert('모든 필드를 입력해주세요.');
    } else if (password !== confirmPassword) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
    } else {
      // 서버로 POST 요청 보내기
      const formData = new FormData();
      formData.append('username', username);
      formData.append('email', email);
      formData.append('password', password);

      fetch('http://localhost:8080/signup', {
        method: 'POST',
        mode: 'no-cors',
        body: formData,
      })
      .then(response => {
        if(response.ok){
          console.log(response.state);
        }
      })
      
    }
  };

  return (
    <div>
      <Header />
      <SignUpContainer>
        <Typography variant="h4" component="h2" align="center">
          회원가입
        </Typography>
        <Form component="form">
          <TextField
            label="USER NAME"
            variant="outlined"
            fullWidth
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label="ID"
            variant="outlined"
            fullWidth
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <TextField
            label="PW"
            type="password"
            variant="outlined"
            fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <TextField
            label="PW 확인"
            type="password"
            variant="outlined"
            fullWidth
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
          <SubmitButton
            variant="contained"
            color="primary"
            onClick={handleSignUp}
          >
            회원가입
          </SubmitButton>
        </Form>
      </SignUpContainer>
    </div>
  );
};

export default SignUpPage;
