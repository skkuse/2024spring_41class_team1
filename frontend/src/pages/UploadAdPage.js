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
import useAuth from "../utils/useAuth";
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
  const { isLoggedIn, userData, handleLogout } = useAuth();

  const USER_NAME = "USER_NAME"; // Replace with actual user name
  const OWNED_BITS = 40;
  const [username, setUserName] = useState(null);
  const [ownedBits, setOwnedBits] = useState(null);
  const [adName, setAdName] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);

  //인증되지 않은 경우 login으로 redirect
  useEffect(() => {
    if (isLoggedIn === false) {
      navigate("/login");
    }
  }, [isLoggedIn, navigate]);

  useEffect(() => {
    if (userData) {
      setUserName(userData.username);
      setOwnedBits(userData.bit.current_bit);
    }
  }, [userData]);

  const { getRootProps, getInputProps } = useDropzone({
    onDrop: (acceptedFiles) => {
      setSelectedFile(acceptedFiles[0]);
    },
  });

  const handleFileSelect = (event) => {
    const file = event.target.files[0];
    setSelectedFile(file);
  };

  const handleConfirmation = () => {
    if (!adName) {
      alert("광고 이름을 입력해주세요.");
    } else if (!selectedFile) {
      alert("이미지를 업로드해주세요.");
    } else if (selectedFile.size > 20 * 1024 * 1024) {
      alert("이미지 용량이 20MB를 초과합니다.");
    } else {
      const formData = new FormData();
      formData.append("username", username);
      formData.append("current_bit", OWNED_BITS);
      formData.append("used_bit", ""); // Fill this with the appropriate value
      formData.append("message", adName);
      formData.append("image", selectedFile);

      // Send POST request
      fetch("/advertisements", {
        method: "POST",
        mode: "no-cors",
        body: formData,
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to upload advertisement.");
          }
          alert("광고가 성공적으로 업로드되었습니다.");
          // Perform any additional actions after successful upload
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
            - 광고는 익일 00시부터 24시간 동안 게시되며 30bit가 차감됩니다.
            <br />
            - 비영리적 광고만 업로드 가능합니다 <br />
            - 업로드하신 광고는 관리자 승인을 거쳐 bitCO2e 홈페이지 광고배너에
            게시됩니다 <br />- 광고 이미지 용량은 20MB 이하여야합니다
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
