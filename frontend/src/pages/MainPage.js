import Header from "../components/Header";
import AdBanner from "../components/AdBanner";
import CodeField from "../components/CodeField";
import { Button, styled, Box } from "@mui/material";
import { FileUpload } from "@mui/icons-material";
import React, { useState, useRef, useEffect } from "react";
import useAuth from "../utils/useAuth";
import { useNavigate } from "react-router-dom";

const Section = styled(Box)({
  minHeight: 30,
  padding: 20,
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
});

const Bulletin = styled("img")({
  margin: 10,
  width: 1000,
  height: 250,
  background: "lightgray",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
});

const Container = styled(Box)({
  display: "flex",
  alignItems: "center",
});

const RefactoringArea = styled(Box)({
  width: 1000,
  display: "flex",
  flexDirection: "column",
});

const DropBox = styled(Box)({
  height: 240,
  border: "2px dashed #dee2e6",
  borderRadius: 5,
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  userSelect: "none",
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "center",
});

const UploadButton = styled(Button)({
  marginTop: 4,
  marginBottom: 8,
  padding: 20,
  border: "2px solid #dee2e6",
  borderRadius: 5,
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  fullWidth: true,
});

const ConvertButton = styled(Button)({
  padding: 20,
  border: "1px solid #dee2e6",
  borderRadius: 5,
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  fullWidth: true,
});

const CopyButton = styled(Button)({
  padding: 20,
  border: "1px solid #dee2e6",
  borderRadius: 5,
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  fullWidth: true,
});

const MainPage = () => {
  const { isLoggedIn, userData, handleLogout } = useAuth();

  const inputRef = useRef(null);
  const resultRef = useRef(null);
  const AdRef = [useRef(null), useRef(null)];

  var AdUrls = [];

  const defaultPageSet = () => {
    AdUrls = ["/logo.png"];
    AdRef[0].current.initialize(AdUrls, 0);
    AdRef[1].current.initialize(AdUrls, parseInt(AdUrls.length / 2));
  };

  const OnPageLoad = async () => {  //페이지 로드할 때 광고리스트 받아오기
    try {
      const response = await fetch("/advertisements?status=approved",
        {
          method: "GET",
        }
      );
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const img_data = await response.json(); //json parsing 완료된 이후에 사용 가능. 비동기로 처리
      img_data.forEach((i) => {
        if (!AdUrls.includes(i.imageUrl)) { //imageUrl 중복처리
          AdUrls.push(i.imageUrl);
        }
      });
    }
    catch{
      console.log("img load error"); 
      defaultPageSet();
    }
    AdRef[0].current.initialize(AdUrls, 0);
    AdRef[1].current.initialize(AdUrls, parseInt(AdUrls.length / 2));

  };

  const SetEditor = (files) => {
    if (files.length !== 1) {
      alert("파일은 한 번에 하나씩 업로드해야 합니다.");
      return -1;
    }

    const file = files[0];

    let fileReader = new FileReader();
    fileReader.onload = () => {
      if (!file.name.match(/(.*?)\.(txt|java)$/)) {
        if (
          !window.confirm(
            "Java 소스코드 형식의 파일이 아닙니다.\n그래도 여시겠습니까?"
          )
        ) {
          return -1;
        }
      }
      inputRef.current.editor.setValue(fileReader.result, -1);
    };
    fileReader.readAsText(file);
  };

  const HandleDragOver = (e) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const FileDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();

    SetEditor(e.dataTransfer.files);
  };

  const BrowseFile = () => {
    var input = document.createElement("input");
    input.type = "file";
    input.onchange = function (e) {
      SetEditor(e.target.files);
    };
    input.click();
  };

  const Convert = async () => {
    const body = inputRef.current.editor.getValue();
    try {
      const response = await fetch("/refactoring", {
        method: "POST",
        headers: {
          "Content-Type": "text/plain",
        },
        body: body,
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.text();
      resultRef.current.editor.setValue(data);
    } catch{
      alert("Refactoring에 실패하였습니다.\n잠시 후 다시 시도해주세요.");
    }
  };

  const CopyToClipboard = (text) => {
    navigator.clipboard
      .writeText(resultRef.current.editor.getValue())
      .then(() => {
        alert("클립보드에 성공적으로 복사하였습니다.");
      })
      .catch((error) => {
        alert("클립보드 복사에 실패하였습니다. 잠시 후 다시 시도해주세요.");
      });
  };

  useEffect(() => {
    OnPageLoad();
  }, []); //페이지 로드시 일회성으로 실행되는 코드: 광고 정보 등 페이지 구성

  return (
    <div>
      <Header />
      <Section>
        <Bulletin src={"/logo.png"}></Bulletin>
      </Section>

      <Section>
        <AdBanner ref={AdRef[0]}>Ad_1</AdBanner>
        <Container>
          <RefactoringArea>
            <DropBox onDrop={FileDrop} onDragOver={HandleDragOver}>
              <FileUpload fontSize="large"></FileUpload>
              <p>Drag and drop your source code!</p>
            </DropBox>
            <UploadButton onClick={BrowseFile}>
              ...or browse your file
            </UploadButton>
            <CodeField
              readOnly={false}
              theme="terminal"
              placeholder="Input your source code here."
              ref={inputRef}
            ></CodeField>
            <ConvertButton
              variant="contained"
              color="primary"
              onClick={Convert}
            >
              Convert!
            </ConvertButton>
            <CodeField
              readOnly={true}
              theme="mono_industrial"
              placeholder="Refactored code will be here."
              ref={resultRef}
            ></CodeField>
            <CopyButton
              variant="contained"
              color="primary"
              onClick={CopyToClipboard}
            >
              Copy to your clipboard
            </CopyButton>
          </RefactoringArea>
        </Container>
        <AdBanner ref={AdRef[1]}>Ad_2</AdBanner>
      </Section>

      <Section>
        <div
          style={{ width: 1000, height: 600, background: "lightgray" }}
        ></div>{" "}
        {/*임시 자리 표시*/}
        {/*<ServerInfo></ServerInfo><Analysis></Analysis>*/}
      </Section>

      {/*<Footer>*Copyright, email, info, etc. comes here.*"</Footer> ..is this needed?*/}
    </div>
  );
};

export default MainPage;
