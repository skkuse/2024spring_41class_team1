import React, { useState } from "react";
import Header from "../components/Header";
import {
  Container,
  Typography,
  TextField,
  Button,
  Box,
  Link,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import { useNavigate } from "react-router-dom";

const LoginContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: "1px solid #dee2e6",
  borderRadius: 5,
  backgroundColor: "#ffffff",
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  maxWidth: 400,
  margin: "50px auto",
});

const Form = styled(Box)({
  display: "flex",
  flexDirection: "column",
  gap: 15,
});

const SubmitButton = styled(Button)({
  marginTop: 15,
});

const LoginPage = () => {
  const navigate = useNavigate();
  const [id, setId] = useState("");
  const [pw, setPw] = useState("");

  const handleLoginClick = async (event) => {
    event.preventDefault();

    const formData = new FormData();
    formData.append("username", id);
    formData.append("password", pw);

    try {
      const response = await fetch("/login", {
        method: "POST",
        mode: "no-cors",
        body: formData,
      });
      console.log(response.status);
      if (response.ok && response.status === 200) {
        //로그인 성공시
        alert("login success");

        try{  //세션 획득
          const session_response = await fetch("/session",{
            method: "GET",
          });
          if(session_response.ok){
            const session_data = await session_response.json();
            //세션 정보 확인. user, admin 구분
            const isAdmin = session_data.authorities[0].authority;
            if(isAdmin === "ROLE_ADMIN"){ //로그인한 사용자가 admin인 경우
              navigate("/admin");
            }
            else{ //로그인한 사용자가 일반 유저인 경우
              console.log("this is user");
              navigate('/');
            }
          }
          else{
            throw new Error('session response error');
          }
        }catch{
          console.log("session connection error");
        }
        
      } else {
        //로그인 실패시
        alert("login failed");
        //navigate('/login?error');
      }
    } catch (error) {
      console.error("Login error:", error);
      alert("Login failed. Please check your connection.");
    }
  };

  const handleSignUpClick = () => {
    navigate("/signup");
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
