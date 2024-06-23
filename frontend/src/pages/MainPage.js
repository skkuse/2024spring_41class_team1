import React, { useState } from 'react';
import MainTopPage from './MainTopPage';
import MainBottomPage from './MainBottomPage';

function MainPage() {
  const [code, setCode] = useState("");
  return (
    <div>
      <MainTopPage setCode={setCode} /> {/* 입력 및 광고배너 파트, setCode 함수를 통해 code를 bottompage에 전달 */}
      <MainBottomPage code={code} /> {/* 결과 분석 파트 */}
    </div>
  );
}

export default MainPage;
