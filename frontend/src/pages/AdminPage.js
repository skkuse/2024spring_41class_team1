import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import { Container, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import { styled } from '@mui/material/styles';
import { useAuth } from '../contexts/AuthContext';

const AdminContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: '1px solid #dee2e6',
  borderRadius: 5,
  backgroundColor: '#ffffff',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
});

const Title = styled(Typography)({
  marginBottom: 20,
  textAlign: 'left', // 왼쪽 정렬
});

const AdminPage = () => {
  const [open, setOpen] = useState(false);
  const [advertisements, setAdvertisements] = useState([]);
  const [selectedImage, setSelectedImage] = useState('');
  const { setRole } = useAuth();

  const handleClickOpen = (imageUrl) => {
    setSelectedImage(imageUrl);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };


  const OnPageLoad = async()=>{ //페이지 시작할 때 로드하는 내용
    try{
      const response = await fetch("/advertisements?status=applied",
        {
          method: "GET",
        }
      );
      if (!response.ok){
        throw new Error("Network response was not ok");
      }
      const adListData = await response.json();
      setAdvertisements(adListData);
      try{  //세션 획득
        const session_response = await fetch("/session",{
          method: "GET",
        });
        if(session_response.ok){
          const session_data = await session_response.json();
          //세션 정보 확인.
          if (session_data.authorities[0].authority === 'ROLE_ADMIN'){ //ADMIN 권한 확인 후 HEADER 형태 수정
            setRole('ROLE_ADMIN');
          }
        }
        else{
          throw new Error('session response error');
        }
      }catch{
        console.log("session connection error");
      }
    }catch{
      console.log("Error fetching advertisements");
    }
  }

  useEffect(() => {
    OnPageLoad();
  }, []);

  return (
    <div>
      <Header />
      <AdminContainer>
        <Title variant="h4" component="h2">
          Hello, Admin
        </Title>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>광고명</TableCell>
                <TableCell>광고 신청자</TableCell>
                <TableCell>광고 이미지 미리보기</TableCell>
                <TableCell>승인</TableCell>
                <TableCell>거절</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {advertisements.map(advertisement => (
                <TableRow key={advertisement.key}>
                  <TableCell>{advertisement.message}</TableCell>
                  <TableCell>{advertisement.username}</TableCell>
                  <TableCell>
                    <Button variant="outlined" onClick={() => handleClickOpen(advertisement.imageUrl)}>
                      미리보기
                    </Button>
                  </TableCell>
                  <TableCell>
                    <Button variant="contained" color="primary">
                      승인
                    </Button>
                  </TableCell>
                  <TableCell>
                    <Button variant="contained" color="secondary">
                      거절
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </AdminContainer>

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>광고 이미지 미리보기</DialogTitle>
        <DialogContent>
          <img src={selectedImage} alt="광고 이미지 미리보기" style={{ width: '100%' }} />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            닫기
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default AdminPage;
