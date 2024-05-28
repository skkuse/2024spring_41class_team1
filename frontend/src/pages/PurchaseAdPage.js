import React, { useState } from 'react';
import Header from '../components/Header';
import { Container, Typography, Box, Radio, RadioGroup, FormControlLabel, FormControl, FormLabel, Button } from '@mui/material';
import { styled } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';

const PurchaseContainer = styled(Container)({
  marginTop: 50,
  padding: 20,
  border: '1px solid #dee2e6',
  borderRadius: 5,
  backgroundColor: '#ffffff',
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  width: 600,
  margin: '50px auto',
});

const InfoBox = styled(Box)({
  display: 'flex',
  justifyContent: 'space-between',
  marginBottom: 20,
});

const PurchaseAdPage = () => {
  const [selectedValue, setSelectedValue] = useState('');
  const navigate = useNavigate();
  
  const handleChange = (event) => {
    setSelectedValue(event.target.value);
  };

  const handlePurchaseClick = () => {
    if (selectedValue) {
      alert(`광고권 ${selectedValue} 구매 완료!`);
      navigate('/uploadad');
    } else {
      alert('광고권을 선택해주세요.');
    }
  };

  const USER_NAME = 'USER_NAME';
  const OWNED_BITS = 40;

  return (
    <div>
      <Header />
      <PurchaseContainer>
        <InfoBox>
          <Typography variant="h6" sx={{ marginRight: 'auto', fontSize: '1.2rem' }}>hello, {USER_NAME}</Typography>
          <Typography variant="h6" sx={{ marginLeft: 'auto', fontSize: '1.2rem' }}>현재 소유하고 있는 bit: {OWNED_BITS}bits</Typography>
        </InfoBox>
        <FormControl component="fieldset">
          <FormLabel component="legend" sx={{ fontSize: '1.2rem' }}>광고권 선택</FormLabel>
          <RadioGroup value={selectedValue} onChange={handleChange}>
            <FormControlLabel value="1일권, 30bits" control={<Radio />} label="1일권, 30bits" sx={{ fontSize: '1.2rem' }} />
            <FormControlLabel value="2일권, 60bits" control={<Radio />} label="2일권, 60bits" sx={{ fontSize: '1.2rem' }} />
            <FormControlLabel value="3일권, 90bits" control={<Radio />} label="3일권, 90bits" sx={{ fontSize: '1.2rem' }} />
            <FormControlLabel value="7일권, 210bits" control={<Radio />} label="7일권, 210bits" sx={{ fontSize: '1.2rem' }} />
          </RadioGroup>
        </FormControl>
        <Button variant="contained" color="primary" fullWidth onClick={handlePurchaseClick} style={{ marginTop: 20 }}>
          광고권 구매
        </Button>
      </PurchaseContainer>
    </div>
  );
};

export default PurchaseAdPage;
