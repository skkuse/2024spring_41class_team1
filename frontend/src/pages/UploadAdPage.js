import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import {
  Container,
  Typography,
  Box,
  TextField,
  Button,
  InputLabel,
  Input,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import { useDropzone } from "react-dropzone";
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from "react-router-dom";

const UploadContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: "1px solid #dee2e6",
  borderRadius: 5,
  backgroundColor: "#ffffff",
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  width: 700,
  margin: "50px auto",
});

const UserBox = styled(Box)({
  display: "flex",
  justifyContent: "space-between",
  marginBottom: 20,
});

const InfoBox = styled(Box)({
  marginBottom: 20,
  padding: 10,
  border: "1px solid black",
  borderRadius: 5,
  backgroundColor: "#f0f0f0",
});

const UploadBox = styled(Box)({
  display: "flex",
  alignItems: "center",
  justifyContent: "space-between",
  marginY: 2,
  padding: 10,
});

const DropzoneContainer = styled(Box)({
  width: "100%",
  height: 100,
  border: "2px dashed #cccccc",
  borderRadius: 5,
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  backgroundColor: "#fafafa",
  flexDirection: "column",
  userSelect: "none",
});

const UploadAdPage = () => {
  const navigate = useNavigate();
  const { setRole } = useAuth();

  const [username, setUserName] = useState(null);
  const [ownedBits, setOwnedBits] = useState(null);
  const [adName, setAdName] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);

  useEffect(() => {
    const fetchSessionData = async () => {
      try {
        const response = await fetch("/session", { method: "GET" });  //세션 확인
        if (response.ok) {
          const sessionData = await response.json();
          setUserName(sessionData.username);
          setOwnedBits(sessionData.bit.current_bit);
          if (sessionData.authorities[0].authority === 'ROLE_USER') { //회원 권한인지 확인
            setRole('ROLE_USER');
          } else { //회원 아닌 경우 홈페이지로 redirect
            navigate('/');
          }
        } else {
          throw new Error('Session response error');
        }
      } catch (error) {
        console.log("Session connection error", error);
        navigate('/login');
      }
    };

    fetchSessionData();
  }, [navigate, setRole]);


  const { getRootProps, getInputProps } = useDropzone({ //광고이미지 드래그앤드롭으로 upload할 수 있는 파트
    onDrop: (acceptedFiles) => {
      setSelectedFile(acceptedFiles[0]);
    },
  });

  const handleFileSelect = (event) => {
    const file = event.target.files[0];
    setSelectedFile(file);
  };

  const handleConfirmation = () => {   //광고 신청 버튼 클릭 시
    if (!adName) {
      alert("광고 이름을 입력해주세요.");
    } else if (!selectedFile) {
      alert("이미지를 업로드해주세요.");
    } else if (selectedFile.size > 10 * 1024 * 1024) {
      alert("이미지 용량이 20MB를 초과합니다.");
    } else {  //정상 flow
      const formData = new FormData();
      formData.append("message", adName);
      formData.append("image", selectedFile);

      // Send POST request
      fetch("/advertisement", { //광고 name과 광고 이미지 POST request
        method: "POST",
        body: formData,
      })
        .then((response) => {
          if (!response.ok) {
            if(response.status===405){
              throw new Error("405 error");
            }
            else if(response.status===400){
              throw new Error("Request body is missed");
            }
            else if(response.status===405){
              throw new Error("Image file size is too big");
            }
          }
          alert("광고가 성공적으로 업로드되었습니다.");
          navigate("/mypage");
        })
        .catch((error) => {
          alert(error.message);
        });
    }
  };

  return (
    <div>
      <Header />
      <UploadContainer>
        <UserBox>
          <Typography
            variant="h6"
            sx={{ marginRight: "auto", fontSize: "1.2rem" }}
          >
            hello, {username}
          </Typography>
          <Typography
            variant="h6"
            sx={{ marginLeft: "auto", fontSize: "1.2rem" }}
          >
            현재 소유하고 있는 bit: {ownedBits}bits
          </Typography>
        </UserBox>
        <InfoBox marginTop="20px">
          <Typography variant="body1">
            <b>알림</b> <br />
            - 광고는 익일 00시부터 24시간 동안 게시되며 50bit가 차감됩니다.
            <br />
            - 비영리적 광고만 업로드 가능합니다 <br />
            - 업로드하신 광고는 관리자 승인을 거쳐 bitCO2e 홈페이지 광고배너에
            게시됩니다 <br />- 광고 이미지 용량은 10MB 이하여야합니다
          </Typography>
        </InfoBox>
        <TextField
          label="광고 이름"
          variant="outlined"
          fullWidth
          margin="normal"
          value={adName}
          onChange={(e) => setAdName(e.target.value)}
        />
        <UploadBox>
          <Typography variant="body1">
            광고 이미지 업로드. (용량: 20MB)
          </Typography>
          <InputLabel htmlFor="upload-button-file">
            <Input
              id="upload-button-file"
              type="file"
              style={{ display: "none" }}
              inputProps={{ accept: "image/*" }}
              onChange={handleFileSelect}
            />
          </InputLabel>
        </UploadBox>
        <DropzoneContainer {...getRootProps()}>
          <input {...getInputProps()} />
          <Typography variant="body2" color="textSecondary">
            Drag & drop files here, or click to select files
          </Typography>
          {selectedFile && (
            <Typography variant="body2" color="textPrimary" marginTop={2}>
              {selectedFile.name} ({(selectedFile.size / 1024).toFixed(2)} KB)
            </Typography>
          )}
        </DropzoneContainer>
        <Button
          variant="contained"
          color="primary"
          fullWidth
          style={{ marginTop: 20 }}
          onClick={handleConfirmation}
        >
          확인
        </Button>
      </UploadContainer>
    </div>
  );
};

export default UploadAdPage;
