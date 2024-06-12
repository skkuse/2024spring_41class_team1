import React, { useState, useEffect } from 'react';
import { styled, Box } from "@mui/material";

const Section = styled(Box)({
    minHeight: 30,
    padding: 20,
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
});

const MainBottomPage = ({ code }) => {
    const [refactoredCode, setRefactoredCode] = useState("");  // 변환된 코드를 저장할 상태
    const [isLoading, setIsLoading] = useState(false);         // 로딩 상태

    useEffect(() => {
        const fetchRefactoredCode = async () => {
            if (!code) return;  // 코드가 없다면 요청을 보내지 않음

            setIsLoading(true);  // 로딩 시작
            try {
                const response = await fetch('/refactoring', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'text/plain',
                    },
                    body: code
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json(); 
                setRefactoredCode(data);  // 변환된 코드 상태 업데이트
            } catch (error) {
                console.error('Failed to fetch refactored code:', error);
                setRefactoredCode("Error fetching refactored code.");
            } finally {
                setIsLoading(false);  // 로딩 상태 해제
            }
        };

        fetchRefactoredCode();
    }, [code]);  // code가 변경될 때마다 요청

    return (
        <Section style={{ display: "flex", justifyContent: "center", alignItems: "center", width: "1000px", height: "600px", margin: "20px auto" }}>
            <div style={{ flex: 1, height: "600px", backgroundColor: "#E6F7FF", padding: "20px", fontSize: "16px", fontFamily: "Arial, sans-serif", alignItems: "start" }}>
                <h2 style={{ textAlign: "center", fontWeight: "bold" }}>시스템 정보</h2>
                <p style={{ fontWeight: "bold" }}>CPU</p>
                <ul>
                    <li>Intel Core i7-10700K</li>
                    <li>8코어</li>
                    <li>16스레드</li>
                    <li>기본 클럭 속도: 3.8 GHz</li>
                    <li>최대 클럭 속도: 5.1 GHz</li>
                </ul>
                <p style={{ fontWeight: "bold" }}>Memory</p>
                <ul>
                    <li>32GB</li>
                    <li>DDR4</li>
                    <li>3200MHz</li>
                </ul>
                <p style={{ fontWeight: "bold" }}>GPU</p>
                <ul>
                    <li>GeForce RTX 3080</li>
                    <li>10GB GDDR6X</li>
                    <li>CUDA 코어 수: 8704개</li>
                    <li>부스트 클럭: 1710 MHz</li>
                    <li>메모리 속도: 19 Gbps</li>
                </ul>
                <p style={{ fontWeight: "bold" }}>Power</p>
                <ul>
                    <li>전력 용량: 850W</li>
                </ul>
            </div>
            <div style={{ width: "30px", backgroundColor: "white" }}></div>
            <div style={{ flex: 3, height: "600px", backgroundColor: "#E6F7FF", padding: "20px", display: "flex", flexDirection: "column", alignItems: "center" }}>
                <h2>Analysis</h2>


             {/*
                {isLoading ? <p>Loading...</p> : <pre>{refactoredCode}</pre>} 
                <pre>{code}</pre> 
            */}
            </div>
        </Section>
    );
};

export default MainBottomPage;