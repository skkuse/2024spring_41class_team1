import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import { Container, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import { styled } from '@mui/material/styles';

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

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  useEffect(() => {
    fetch('http://localhost:8080/advertisements?status=applied', {
        method: 'GET',
        mode: 'no-cors',
      })
      .then(response => response.json())
      .then(data => {
        setAdvertisements(data);
      })
      .catch(error => {
        console.error('Error fetching advertisements:', error);
      });
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
                    <Button variant="outlined" onClick={handleClickOpen}>
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
          <img src="/ad_test.png" alt="광고 이미지 미리보기" style={{ width: '100%' }} />
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
