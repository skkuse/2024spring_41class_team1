import React, { useState } from "react";
import Header from "../components/Header";
import { Container, Typography, TextField, Button, Box } from "@mui/material";
import { styled } from "@mui/material/styles";
import { SettingsPower } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";

const SignUpContainer = styled(Container)({
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

const SignUpPage = () => {
  const [email, setEmail] = useState("");
  const [id, setId] = useState("");
  const [pw, setPw] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const navigate = useNavigate();

  const handleSignUp = async () => {
    //회원가입 버튼 클릭시
    if (!email || !id || !pw || !confirmPassword) {
      //비어있는 필드 존재하는 경우
      alert("모든 필드를 입력해주세요.");
    } else if (pw !== confirmPassword) {
      //pw와 pw확인의 value가 일치하지 않는 경우
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    } else {
      //정상 flow
      // 서버로 POST 요청 보내기
      const formData = new FormData();
      formData.append("username", id);
      formData.append("email", email);
      formData.append("password", pw);

      try {
        const response = await fetch("/signup", {
          // signup에 사용자가 입력한 데이터를 포함하여 request
          method: "POST",
          mode: "no-cors",
          body: formData,
        });
        if (response.ok && response.status === 200) {
          //회원 가입  성공
          alert("Signup successfully");
          navigate("/login");
        } else {
          //console.log(response.status);
          alert("signup failed");
        }
      } catch (error) {
        alert("signup failed. Please check your connection.");
      }
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
            label="ID"
            variant="outlined"
            fullWidth
            value={id}
            onChange={(e) => setId(e.target.value)}
          />
          <TextField
            label="Email"
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
            value={pw}
            onChange={(e) => setPw(e.target.value)}
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
