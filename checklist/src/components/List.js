import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode"; // jwt-decode 라이브러리에서 jwtDecode를 임포트
import { useNavigate } from "react-router-dom"; // useNavigate 임포트
import "../css/List.css"; // CSS 파일 임포트

const List = () => {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState("");
  const [isDarkMode, setIsDarkMode] = useState(false);

  const navigate = useNavigate(); // navigate 훅 생성

  // 로컬 스토리지에서 JWT 토큰 가져오기
  const token = localStorage.getItem("jwt");

  // JWT 토큰이 없으면 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!token) {
      navigate("/"); // 로그인 페이지로 리다이렉트
    }
  }, [token, navigate]);
  
  // JWT 토큰에서 username 추출 함수
  const getUsernameFromToken = (token) => {
    if (!token) return null;
    try {
      const decodedToken = jwtDecode(token); // JWT 디코딩
      return decodedToken.username || decodedToken.sub; // username 또는 sub 필드에서 username 추출
    } catch (error) {
      console.error("Invalid token:", error);
      return null;
    }
  };

  // username 추출
  const username = getUsernameFromToken(token);

  useEffect(() => {
    if (!username) return;

    // username에 해당하는 할 일 목록 불러오기
    axios
      .get(`http://localhost:8080/api/list?username=${username}`)
      .then((response) => setTasks(response.data))
      .catch((error) => console.error("Error fetching data:", error));
  }, [username]);

  const handleCheckboxChange = (task) => {
    const updatedTask = { ...task, checked: !task.checked };
    axios
      .post("http://localhost:8080/api/list/update", updatedTask)
      .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t)))); // 완료 상태 변경
  };

  const handleShareChange = (task) => {
    const updatedTask = { ...task, shared: !task.shared }; // 'isShared' → 'shared'로 변경
    axios
      .post("http://localhost:8080/api/list/update", updatedTask)
      .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t)))); // 공유 상태 변경
  };

  const handleEdit = (task) => {
    const newTitle = prompt("새 제목을 입력하세요", task.title);
    if (newTitle) {
      const updatedTask = { ...task, title: newTitle };
      axios
        .put("http://localhost:8080/api/list/edit", updatedTask)
        .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t)))); // 제목 수정
    }
  };

  const handleDelete = (id) => {
    axios
      .delete(`http://localhost:8080/api/list/delete/${id}`)
      .then(() => setTasks(tasks.filter((t) => t.id !== id))); // 할 일 삭제
  };

  const handleAddTask = () => {
    if (newTask.trim() === "") {
      alert("할 일을 입력하세요!");
      return;
    }

    const newTaskItem = { title: newTask, checked: false, username }; // username 포함
    axios
      .post("http://localhost:8080/api/list/add", newTaskItem)
      .then((response) => {
        setTasks([...tasks, response.data]); // 새 할 일 추가
      })
      .catch((error) => {
        console.error("Error adding task:", error);
      });
    setNewTask("");
  };

  // 다크모드 토글 함수
  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
    document.body.classList.toggle("dark-mode", !isDarkMode);
  };

  // 로그아웃 함수
  const handleLogout = () => {
    localStorage.removeItem("jwt"); // 로컬 스토리지에서 JWT 토큰 삭제
    window.location.href = "/"; // 홈페이지로 리다이렉트
  };

  // 공유업무 페이지로 이동
  const goToShare = () => {
    navigate("/share"); // /share 페이지로 이동
  };

  return (
    <div>
      <h1 className="text-center">할 일 목록</h1>

      {/* 왼쪽 상단 로그아웃 버튼 */}
      <button
        style={{
          position: "absolute",
          top: "20px",
          left: "20px",
          padding: "10px 15px",
          backgroundColor: "#f44336",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
        }}
        onClick={handleLogout}
      >
        로그아웃
      </button>

      {/* 공유업무 버튼 */}
      <button
        style={{
          position: "absolute",
          top: "60px",
          left: "20px",
          padding: "10px 15px",
          backgroundColor: "#4CAF50",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
        }}
        onClick={goToShare}
      >
        공유업무
      </button>

      <div>
        <input
          type="text"
          value={newTask}
          onChange={(e) => setNewTask(e.target.value)}
          placeholder="새 할 일을 입력하세요"
        />
        <button onClick={handleAddTask}>추가</button>
      </div>

      {/* 테이블 형태로 목록 표시 */}
      <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "20px" }}>
        <thead>
          <tr>
            <th>공유하기</th>
            <th>완료</th>
            <th>할 일</th>
            <th>수정</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((task) => (
            <tr key={task.id}>
              <td>
                <input
                  type="checkbox"
                  checked={task.shared} // 'isShared' → 'shared'로 변경
                  onChange={() => handleShareChange(task)} // 공유 상태 변경
                />
              </td>
              <td>
                <input
                  type="checkbox"
                  checked={task.checked}
                  onChange={() => handleCheckboxChange(task)} // 완료 상태 변경
                />
              </td>
              <td>{task.title}</td>
              <td>
                <button onClick={() => handleEdit(task)}>수정</button>
              </td>
              <td>
                <button onClick={() => handleDelete(task.id)}>삭제</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 다크모드 토글 버튼 */}
      <button
        style={{
          position: "fixed",
          top: "20px",
          right: "20px",
          padding: "10px 15px",
          backgroundColor: "#333",
          color: "#fff",
          border: "none",
          borderRadius: "50%",
          cursor: "pointer",
        }}
        onClick={toggleDarkMode}
      >
        {isDarkMode ? "☀️" : "🌙"}
      </button>
    </div>
  );
};

export default List;
