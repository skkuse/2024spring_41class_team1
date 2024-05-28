import React, { useState } from 'react';
import Header from '../components/Header';
import { Container, Typography, Box, TextField, Button, InputLabel, Input } from '@mui/material';
import { styled } from '@mui/material/styles';
import { useDropzone } from 'react-dropzone';

const UploadContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: '1px solid #dee2e6',
  borderRadius: 5,
  backgroundColor: '#ffffff',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  width: 700,
  margin: '50px auto',
});

const InfoBox = styled(Box)({
  marginBottom: 20,
  padding: 10,
  border: '1px solid black',
  borderRadius: 5,
  backgroundColor: '#f0f0f0',
});

const UploadBox = styled(Box)({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  marginY: 2,
  padding: 10,
});

const DropzoneContainer = styled(Box)({
  width: '100%',
  height: 100,
  border: '2px dashed #cccccc',
  borderRadius: 5,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  backgroundColor: '#fafafa',
  flexDirection: 'column',
});

const UploadAdPage = () => {
  const USER_NAME = 'USER_NAME'; // Replace with actual user name
  const [selectedFile, setSelectedFile] = useState(null);

  const { getRootProps, getInputProps } = useDropzone({
    onDrop: (acceptedFiles) => {
      setSelectedFile(acceptedFiles[0]);
    },
  });

  const handleFileSelect = (event) => {
    const file = event.target.files[0];
    setSelectedFile(file);
  };

  return (
    <div>
      <Header />
      <UploadContainer>
        <Typography variant="h6">hello, {USER_NAME}</Typography>
        <InfoBox marginTop="20px">
          <Typography variant="body1">
            <b>알림</b> <br />
            - 비영리적 광고만 업로드 가능합니다 <br />
            - 업로드하신 광고는 관리자 승인을 거쳐 bitCO2e 홈페이지 광고배너에 게시됩니다 <br />
            - 광고 이미지는 100px*600px 사이즈이어야합니다
          </Typography>
        </InfoBox>
        <TextField
          label="광고 이름"
          variant="outlined"
          fullWidth
          margin="normal"
        />
        <UploadBox>
          <Typography variant="body1">광고 이미지 업로드. (규격: 100px*600px)</Typography>
          <InputLabel htmlFor="upload-button-file">
            <Input
              id="upload-button-file"
              type="file"
              style={{ display: 'none' }}
              inputProps={{ accept: 'image/*' }}
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
        <Button variant="contained" color="primary" fullWidth style={{ marginTop: 20 }}>
          확인
        </Button>
      </UploadContainer>
    </div>
  );
};

export default UploadAdPage;
