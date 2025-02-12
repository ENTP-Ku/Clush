import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import "../css/List.css";
import { useNavigate } from "react-router-dom";

const ShareList = () => {
  const [tasks, setTasks] = useState([]);
  const [isDarkMode, setIsDarkMode] = useState(false);
  
  const navigate = useNavigate();

  const token = localStorage.getItem("jwt");

  useEffect(() => {
    if (!token) {
      navigate("/"); // 로그인 페이지로 리다이렉트
    }
  }, [token, navigate]);

  const getUsernameFromToken = (token) => {
    if (!token) return null;
    try {
      const decodedToken = jwtDecode(token);
      return decodedToken.username || decodedToken.sub;
    } catch (error) {
      console.error("Invalid token:", error);
      return null;
    }
  };

  const username = getUsernameFromToken(token);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/list/shared", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      .then((response) => setTasks(response.data))
      .catch((error) => console.error("Error fetching data:", error));
  }, [token]);

  const handleCheckboxChange = (task) => {
    const updatedTask = { ...task, checked: !task.checked };
    axios
      .post("http://localhost:8080/api/list/update", updatedTask, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t))));
  };

  const handleEdit = (task) => {
    const newTitle = prompt("새 제목을 입력하세요", task.title);
    if (newTitle) {
      const updatedTask = { ...task, title: newTitle };
      axios
        .put("http://localhost:8080/api/list/edit", updatedTask, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        .then(() => setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t))));
    }
  };

  const handleDelete = (id) => {
    axios
      .delete(`http://localhost:8080/api/list/delete/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      .then(() => setTasks(tasks.filter((t) => t.id !== id)));
  };

  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
    document.body.classList.toggle("dark-mode", !isDarkMode);
  };

  const handleLogout = () => {
    localStorage.removeItem("jwt");
    window.location.href = "/";
  };

  const goToMyTasks = () => {
    navigate("/list");
  };

  return (
    <div>
      <h1 className="text-center">공유된 할 일 목록</h1>

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
          top: "80px",
          left: "20px",
          padding: "10px 15px",
          backgroundColor: "#4CAF50",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
        }}
        onClick={goToMyTasks}
      >
        나의 할일
      </button>

      <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "20px" }}>
        <thead>
          <tr>
            <th>완료</th>
            <th>할 일</th>
            <th>작성자</th>
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
                  checked={task.checked}
                  onChange={() => handleCheckboxChange(task)}
                />
              </td>
              <td>{task.title}</td>
              <td>{task.username}</td>
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

export default ShareList;
