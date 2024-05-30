import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import SignUpPage from './pages/SignUpPage';
import AdminPage from './pages/AdminPage';
import MyPage from './pages/MyPage';
import UploadAdPage from './pages/UploadAdPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/admin" element={<AdminPage />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/uploadad" element={<UploadAdPage />} />
      </Routes>
    </Router>
  );
}

export default App;
