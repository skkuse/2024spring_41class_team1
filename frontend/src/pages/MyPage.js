import React from 'react';
import Header from '../components/Header';
import { Container, Typography, Box, Table, TableBody, TableCell, TableContainer, TableRow, Button } from '@mui/material';
import { styled } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';

const MyPageContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: '1px solid #dee2e6',
  borderRadius: 5,
  backgroundColor: '#ffffff',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  maxWidth: 400,
  margin: '50px auto',
});

const GreetingContainer = styled(Box)({
  display: 'flex',
  alignItems: 'center',
  marginBottom: 20,
});

const RankImage = styled('img')({
  marginLeft: 10,
  width: '1em',
  height: '1em',
});

const StyledTable = styled(Table)({
  marginBottom: 20,
});

const StyledTableCell = styled(TableCell)({
  textAlign: 'center',
  fontSize: '1.2rem', 
});

const MyPage = () => {
  const navigate = useNavigate();
  const USER_NAME = 'USER_NAME'; // Replace with actual user name
  const RANK = 1;
  const TOTAL_RANK = 100;
  const ACCUMULATED_BITS = 100;
  const OWNED_BITS = 40;

  const handleUploadClick = () => {
    navigate('/uploadad');
  };

  return (
    <div>
      <Header />
      <MyPageContainer>
        <GreetingContainer>
          <Typography variant="h5">
            hello, {USER_NAME}
          </Typography>
          <RankImage src="/rank.png" alt="Rank" />
        </GreetingContainer>
        <TableContainer>
          <StyledTable>
            <TableBody>
              <TableRow>
                <StyledTableCell>ranking</StyledTableCell>
                <StyledTableCell># {RANK} / # {TOTAL_RANK}</StyledTableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell>누적 bits</StyledTableCell>
                <StyledTableCell>{ACCUMULATED_BITS} bits</StyledTableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell>소유 bits</StyledTableCell>
                <StyledTableCell>{OWNED_BITS} bits</StyledTableCell>
              </TableRow>
            </TableBody>
          </StyledTable>
        </TableContainer>
        <Button variant="contained" color="primary" fullWidth onClick={handleUploadClick}>
          광고권 구매
        </Button>
      </MyPageContainer>
    </div>
  );
};

export default MyPage;
