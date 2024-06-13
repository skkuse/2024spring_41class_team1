import React, { useState } from 'react';
import MainTopPage from './MainTopPage';
import MainBottomPage from './MainBottomPage';

function MainPage() {
  const [code, setCode] = useState("");
  return (
    <div>
      <MainTopPage setCode={setCode} /> {/* setCode 함수를 통해 code를 bottompage에 전달 */}
      <MainBottomPage code={code} /> 
    </div>
  );
}

export default MainPage;
