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
  const [totalBits, setTotalBits] = useState(0);
  const [ownedBits, setOwnedBits] = useState(0);
  const [rankImagePath, setRankImagePath] = useState("");

  useEffect(() => {
    const fetchSessionData = async () => {
      try {
        const response = await fetch("/session", { method: "GET" });
        if (response.ok) {
          const sessionData = await response.json();
          setUserName(sessionData.username);
          setTotalBits(sessionData.bit.total_bit);
          setOwnedBits(sessionData.bit.current_bit);
          updateRankImage(sessionData.bit.total_bit);
          if (sessionData.authorities[0].authority === 'ROLE_USER') {
            setRole('ROLE_USER');
          } else {
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

  const updateRankImage = (totalBits) => {
    if (totalBits >= 801) {
      setRankImagePath("/5.jpg");
    } else if (totalBits >= 601) {
      setRankImagePath("/4.jpg");
    } else if (totalBits >= 401) {
      setRankImagePath("/3.jpg");
    } else if (totalBits >= 201) {
      setRankImagePath("/2.jpg");
    } else {
      setRankImagePath("/5.jpg");
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
                  #1 / #100 {/* Example values */}
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
