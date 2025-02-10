import React, { useState } from 'react'; // React와 상태 훅 가져오기
import { useNavigate } from 'react-router-dom'; // 내비게이션 훅 가져오기
import axios from 'axios'; // axios 가져오기
import '../css/Login.css'; // CSS 스타일 시트 경로

const Login = () => {
  const [username, setUsername] = useState(''); // 사용자 이름 상태
  const [password, setPassword] = useState(''); // 비밀번호 상태
  const navigate = useNavigate(); // 내비게이션 훅 초기화

  // 로그인 처리 함수
  const handleLogin = () => {
    axios.post('http://localhost:8080/api/users/login', { username, password }) // 로그인 요청
      .then(res => {
        localStorage.setItem('jwt', res.data.token); // 성공 시 토큰을 로컬 스토리지에 저장
        navigate('/list'); // 로그인 성공 시 홈으로 이동
      })
      .catch(err => {
        // 에러 처리
        if (err.response && err.response.data && err.response.data.message) {
          alert(err.response.data.message); // 에러 메시지 표시
        } else {
          alert('로그인에 실패했습니다. 다시 시도해주세요.'); // 일반 에러 메시지 표시
        }
      });
  };

  // 회원가입 페이지로 이동
  const handleRegisterRedirect = () => {
    navigate('/register'); // 회원가입 페이지로 이동
  };

  return (
    <div className="login-page">
      <div className="login-form-container">
        <input
          type="text"
          placeholder="아이디"
          value={username}
          onChange={e => setUsername(e.target.value)} // 사용자 이름 입력 처리
          className="login-input"
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={e => setPassword(e.target.value)} // 비밀번호 입력 처리
          className="login-input"
        />
        <button className="submit-btn" onClick={handleLogin}>
          로그인
        </button>
        <button className="signup-btn" onClick={handleRegisterRedirect}>
          회원가입
        </button>
      </div>
    </div>
  );
};

export default Login; // Login 컴포넌트 내보내기
