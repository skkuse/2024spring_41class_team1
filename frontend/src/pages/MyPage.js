import React, { useState, useEffect } from "react";
import Header from "../components/Header";
import {
  Container,
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Button,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import { useNavigate } from "react-router-dom";
import { useAuth } from '../contexts/AuthContext';

const MyPageContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: "1px solid #dee2e6",
  borderRadius: 5,
  backgroundColor: "#ffffff",
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  maxWidth: 400,
  margin: "50px auto",
});

const GreetingContainer = styled(Box)({
  display: "flex",
  alignItems: "center",
  marginBottom: 20,
});

const RankImage = styled("img")({
  marginLeft: 10,
  width: "50px",
  height: "50px",
});

const StyledTable = styled(Table)({
  marginBottom: 20,
});

const StyledTableCell = styled(TableCell)({
  textAlign: "center",
  fontSize: "1.2rem",
});

const MyPage = () => {
  const navigate = useNavigate();
  const { setRole } = useAuth();
  const [userName, setUserName] = useState("");
  const [userId, setUserId] = useState("");
  const [totalBits, setTotalBits] = useState(0);
  const [ownedBits, setOwnedBits] = useState(0);
  const [rankImagePath, setRankImagePath] = useState("");
  const [ranking, setRanking] = useState("");

  useEffect(() => { // 마이페이지 로드 시 실행
    const fetchSessionData = async () => {
      try {
        const response = await fetch("/session", { method: "GET" });  //세션 받아오기
        if (response.ok) {
          const sessionData = await response.json();  //세션 데이터 기반으로 마이페이지에서 사용될 사용자 데이터 설정
          setUserName(sessionData.username);
          setUserId(sessionData.id);
          setTotalBits(sessionData.bit.total_bit);
          setOwnedBits(sessionData.bit.current_bit);
          updateRankImage(sessionData.bit.total_bit);
          if (sessionData.authorities[0].authority === 'ROLE_USER') { //회원 세션인지 확인
            setRole('ROLE_USER');
          } else {    //회원 세션 아닌 경우 초기 페이지로 이동
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

    const fetchUsers = async () => {
      try {
        const response = await fetch('/users?limit=300', { method: 'GET' });
        if (response.ok) {
          const users = await response.json();
          const sortedUsers = users.sort((a, b) => b.bit.total_bit - a.bit.total_bit); // total_bit에 따라 정렬
          const userRank = sortedUsers.findIndex(user => user.id === userId) + 1; // 현재 사용자의 순위 계산
          const totalUsers = sortedUsers.length;
          setRanking(`#${userRank} / #${totalUsers}`); // 순위 정보 업데이트
        } else {
          throw new Error('Failed to fetch user data');
        }
      } catch (error) {
        console.log("Error fetching user data", error);
      }
    };

    fetchSessionData();
    fetchUsers();
  }, [navigate, setRole]);

  const updateRankImage = (totalBits) => {    //소유중인 bit에 따라 나무 이미지 표시
    if (totalBits >= 801) { //200 단위로 성장
      setRankImagePath("/5.jpg");
    } else if (totalBits >= 601) {
      setRankImagePath("/4.jpg");
    } else if (totalBits >= 401) {
      setRankImagePath("/3.jpg");
    } else if (totalBits >= 201) {
      setRankImagePath("/2.jpg");
    } else {
      setRankImagePath("/1.jpg");
    }
  };

  return (
    <div>
      <Header />
      <MyPageContainer>
        <GreetingContainer>
          <Typography variant="h5">hello, {userName}</Typography>
          <RankImage src={rankImagePath} alt="Rank" />
        </GreetingContainer>
        <TableContainer>
          <StyledTable>
            <TableBody>
              <TableRow>
                <StyledTableCell>ranking</StyledTableCell>
                <StyledTableCell>
                 {ranking} {/* Example values */}
                </StyledTableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell>누적 bits</StyledTableCell>
                <StyledTableCell>{totalBits} bits</StyledTableCell>
              </TableRow>
              <TableRow>
                <StyledTableCell>소유 bits</StyledTableCell>
                <StyledTableCell>{ownedBits} bits</StyledTableCell>
              </TableRow>
            </TableBody>
          </StyledTable>
        </TableContainer>
        <Button
          variant="contained"
          color="primary"
          fullWidth
          onClick={() => navigate("/uploadad")}
        >
          광고권 구매
        </Button>
      </MyPageContainer>
    </div>
  );
};

export default MyPage;
