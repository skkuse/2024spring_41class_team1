import React from 'react';
import Header from '../components/Header';
import CodeField from '../components/CodeField';
import { Button, styled, Box } from '@mui/material';

const Section = styled(Box)({
  minHeight: 30,
  padding: 20,
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
});

const Container = styled(Box)({
  display: 'flex',
  alignItems: 'center',
});

const RefactoringArea = styled(Box)({
  width: 1000,
  display: 'flex',
  flexDirection: 'column',
});

const DropBox = styled(Box)({
  height: 240,
  border: '2px dashed #dee2e6',
  borderRadius: 5,
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  
});

const UploadButton = styled(Button)({
  marginTop: 4,
  marginBottom: 8,
  padding: 20,
  border: '2px solid #dee2e6',
  borderRadius: 5,
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
});

const ConvertButton = styled(Button)({
  padding: 20,
  border: '1px solid #dee2e6',
  borderRadius: 5,
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
});

const CopyButton = styled(Button)({
  padding: 20,
  border: '1px solid #dee2e6',
  borderRadius: 5,
  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
});

const MainPage = () => {
  return (
    <div>
      <Header />
        <Section>
          {/*Still developing...*/}
        </Section>

      <Section>
        <Container>
          <RefactoringArea>
            <DropBox>Drag and drop your source code!</DropBox>
            <UploadButton>...or browse your file</UploadButton>
            <CodeField readOnly={false} theme='terminal' placeholder='Input your source code here.'></CodeField>
            <ConvertButton fullWidth variant="contained" color="primary">Convert!</ConvertButton>
            <CodeField readOnly={true} theme='mono_industrial' placeholder='Refactored code will be here.'></CodeField>
            <CopyButton fullWidth variant="contained" color="primary">Copy to your clipboard</CopyButton>
          </RefactoringArea>
          {/*<AdBanner></AdBanner>*/}
        </Container>
      </Section>
      
      <div className='section'>
        {/*<InformationArea></InformationArea>*/}
      </div>
      
      {/*<Footer>*Copyright, email, info, etc. comes here.*"</Footer>*/}
    </div>
  );
};

export default MainPage;