import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode"; 
import { useNavigate } from "react-router-dom"; 
import "../css/List.css"; 

const List = () => {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState("");
  const [isDarkMode, setIsDarkMode] = useState(false);

  const navigate = useNavigate(); 

  // 로컬 스토리지에서 JWT 토큰 가져오기
  const token = localStorage.getItem("jwt");

  // JWT 토큰이 없으면 로그인 페이지로 리다이렉트
  useEffect(() => {
    if (!token) {
      navigate("/"); 
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

  const username = getUsernameFromToken(token);

  useEffect(() => {
    if (!username) return;

    // JWT 토큰을 Authorization 헤더에 포함하여 요청 보내기
    axios
      .get(`http://localhost:8080/api/list?username=${username}`, {
        headers: {
          Authorization: `Bearer ${token}`, // JWT 토큰을 Authorization 헤더에 추가
        },
      })
      .then((response) => setTasks(response.data))
      .catch((error) => console.error("Error fetching data:", error));
  }, [username]);

  const handleCheckboxChange = (task) => {
    const updatedTask = { ...task, checked: !task.checked };
    axios
      .post(
        "http://localhost:8080/api/list/update",
        updatedTask,
        {
          headers: {
            Authorization: `Bearer ${token}`, // JWT 토큰을 Authorization 헤더에 추가
          },
        }
      )
      .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t))));
  };

  const handleShareChange = (task) => {
    const updatedTask = { ...task, shared: !task.shared };
    axios
      .post(
        "http://localhost:8080/api/list/update",
        updatedTask,
        {
          headers: {
            Authorization: `Bearer ${token}`, // JWT 토큰을 Authorization 헤더에 추가
          },
        }
      )
      .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t))));
  };

  const handleEdit = (task) => {
    const newTitle = prompt("새 제목을 입력하세요", task.title);
    if (newTitle) {
      const updatedTask = { ...task, title: newTitle };
      axios
        .put(
          "http://localhost:8080/api/list/edit",
          updatedTask,
          {
            headers: {
              Authorization: `Bearer ${token}`, // JWT 토큰을 Authorization 헤더에 추가
            },
          }
        )
        .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t))));
    }
  };

  const handleDelete = (id) => {
    axios
      .delete(`http://localhost:8080/api/list/delete/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`, // JWT 토큰을 Authorization 헤더에 추가
        },
      })
      .then(() => setTasks(tasks.filter((t) => t.id !== id)));
  };

  const handleAddTask = () => {
    if (newTask.trim() === "") {
      alert("할 일을 입력하세요!");
      return;
    }

    const newTaskItem = { title: newTask, checked: false, username };
    axios
      .post(
        "http://localhost:8080/api/list/add",
        newTaskItem,
        {
          headers: {
            Authorization: `Bearer ${token}`, // JWT 토큰을 Authorization 헤더에 추가
          },
        }
      )
      .then((response) => {
        setTasks([...tasks, response.data]);
      })
      .catch((error) => {
        console.error("Error adding task:", error);
      });
    setNewTask("");
  };

  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
    document.body.classList.toggle("dark-mode", !isDarkMode);
  };

  const handleLogout = () => {
    localStorage.removeItem("jwt"); 
    window.location.href = "/"; 
  };

  const goToShare = () => {
    navigate("/share");
  };

  return (
    <div>
      <h1 className="text-center">할 일 목록</h1>

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
                  checked={task.shared}
                  onChange={() => handleShareChange(task)}
                />
              </td>
              <td>
                <input
                  type="checkbox"
                  checked={task.checked}
                  onChange={() => handleCheckboxChange(task)}
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
