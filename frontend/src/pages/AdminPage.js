import React, { useState, useEffect } from "react";
import Header from "../components/Header";
import {
  Container,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import { useAuth } from "../contexts/AuthContext";
import { useNavigate } from "react-router-dom";

const AdminContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: "1px solid #dee2e6",
  borderRadius: 5,
  backgroundColor: "#ffffff",
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
});

const Title = styled(Typography)({
  marginBottom: 20,
  textAlign: "left", // 왼쪽 정렬
});

const AdminPage = () => {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [advertisements, setAdvertisements] = useState([]);
  const [selectedImage, setSelectedImage] = useState("");
  const { userRole, setRole } = useAuth();

  const handleClickOpen = (imageUrl) => {
    //광고 미리보기 버튼 클릭시
    setSelectedImage(imageUrl); //광고 이미지 표시
    setOpen(true);
  };

  const handleClose = () => {
    //미리보기 닫기
    setOpen(false);
  };

  const OnPageLoad = async () => {
    //페이지 시작할 때 로드하는 내용

    try {
      //세션 획득
      const session_response = await fetch("/session", {
        method: "GET",
      });
      if (session_response.ok) {
        const session_data = await session_response.json();
        //세션 정보 확인.
        if (session_data.authorities[0].authority === "ROLE_ADMIN") {
          //ADMIN 권한 확인 후 HEADER 형태 수정
          setRole("ROLE_ADMIN");
        } else {
          setRole("ROLE_USER");
        }
      } else {
        throw new Error("session response error");
      }
    } catch {
      console.log("session connection error");
    }
  };

  const handleReview = async (adId, status) => {
    //관리자가 광고를 승인하거나 거절하는 함수
    const formData = new FormData();
    formData.append("adId", adId); //관리자가 선택한 광고id
    formData.append("status", status); //승인 버튼 클릭 시 "approved", 거절 버튼 클릭시 "rejected"

    const response = await fetch("/review", {
      // review request 보내기
      method: "POST",
      body: formData,
    });
    if (response.ok) {
      // review 성공
      alert("Review successful");
      fetchAdvertisements(); // 광고 목록 새로고침
    } else {
      //review 실패시 에러 alert
      alert("Review failed. User doesn't have enough bits.");
      console.error("Failed to update advertisement status");
    }
  };

  const fetchAdvertisements = async () => {
    // 사용자들이 신청한 광고 리스트 받아오기
    try {
      const response = await fetch("/advertisements?status=applied", {
        //status가 "applied"인 광고 받아오기
        method: "GET",
      });
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const adListData = await response.json(); // 받아온 광고 리스트 파싱
      setAdvertisements(adListData);
    } catch (error) {
      console.error("Error fetching advertisements", error);
    }
  };

  useEffect(() => {
    //페이지 로드 시 신청한 ad list 불러오기
    fetchAdvertisements();
  }, []);

  useEffect(() => {
    OnPageLoad();
  }, []);

  useEffect(() => {
    if (userRole === null || userRole === "ROLE_ADMIN") {
    } else {
      navigate("/");
    }
  }, [userRole]);

  return (
    <div>
      {userRole && (
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
                  {advertisements.map((advertisement) => (
                    <TableRow key={advertisement.key}>
                      <TableCell>{advertisement.message}</TableCell>
                      <TableCell>{advertisement.user.username}</TableCell>
                      <TableCell>
                        <Button
                          variant="outlined"
                          onClick={() =>
                            handleClickOpen(advertisement.imageUrl)
                          }
                        >
                          미리보기
                        </Button>
                      </TableCell>
                      <TableCell>
                        <Button
                          variant="contained"
                          color="primary"
                          onClick={() =>
                            handleReview(advertisement.id, "approved")
                          }
                        >
                          승인
                        </Button>
                      </TableCell>
                      <TableCell>
                        <Button
                          variant="contained"
                          color="secondary"
                          onClick={() =>
                            handleReview(advertisement.id, "rejected")
                          }
                        >
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
              <img
                src={selectedImage}
                alt="광고 이미지 미리보기"
                style={{ width: "100%" }}
              />
            </DialogContent>
            <DialogActions>
              <Button onClick={handleClose} color="primary">
                닫기
              </Button>
            </DialogActions>
          </Dialog>
        </div>
      )}
    </div>
  );
};

export default AdminPage;
