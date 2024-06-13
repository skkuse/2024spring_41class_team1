import { styled, Box, Button } from '@mui/material';
import React, { forwardRef, useImperativeHandle, useState, useEffect } from 'react';

const Container = styled(Box)({
  margin: 50,
  width: 150,
  height: 640,
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
});

const AdContent = styled('img')({
  width: 150,
  height: 600,
  background: 'lightgray',
});

const ControlButton = styled(Button)({
  height: 20,
  border: '2px solid #dee2e6',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
});



const AdBanner = forwardRef(({}, ref) => {
  const [adList, setList] = useState(["/logo.png"]);
  const [pointer, setPointer] = useState(0);
  const [imgUrl, setUrl] = useState("/logo.png");

  const prevButtonAction = () => {
    setPointer((currentPointer) => (currentPointer + adList.length - 1) % adList.length);
  }

  const nextButtonAction = () => {
    setPointer((currentPointer) => (currentPointer + 1) % adList.length);
  }

  const initialize = (urls, initialPointer) => {
    setList(urls);
    setPointer(initialPointer);
  };

  useEffect(() => {
    setUrl(adList[pointer]);
  }, [pointer, adList]); // adList 변경 시 imgUrl 업데이트

  useImperativeHandle(ref, () => ({
    initialize,
  }));

  useEffect(() => {
    const interval = setInterval(() => {
      const newPointer = (pointer + 1) % adList.length;
      setPointer(newPointer);
    }, 5000);
    return () => clearInterval(interval);
  }, [pointer, adList]);


  return (
    <Container>
        <AdContent src={imgUrl} ref={ref}></AdContent>
        <div>
            <ControlButton onClick={prevButtonAction}>&lt; prev</ControlButton>
            <ControlButton onClick={nextButtonAction}>next &gt;</ControlButton>
        </div>
    </Container>
  );
});

export default AdBanner;