import React, { useEffect, useState } from "react";
import axios from "axios";
import "../css/List.css"; // CSS 파일 임포트

const List = () => {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState("");
  const [isDarkMode, setIsDarkMode] = useState(false);

  useEffect(() => {
    // 데이터 불러오기
    axios.get("http://localhost:8080/api/list")
      .then(response => setTasks(response.data))
      .catch(error => console.error("Error fetching data:", error));
  }, []);

  const handleCheckboxChange = (task) => {
    const updatedTask = { ...task, checked: !task.checked };
    axios.post("http://localhost:8080/api/list/update", updatedTask)
      .then(() => setTasks(tasks.map(t => (t.id === task.id ? updatedTask : t))));
  };

  const handleEdit = (task) => {
    const newTitle = prompt("새 제목을 입력하세요", task.title);
    if (newTitle) {
      const updatedTask = { ...task, title: newTitle };
      axios.put("http://localhost:8080/api/list/edit", updatedTask)
        .then(() => setTasks(tasks.map(t => (t.id === task.id ? updatedTask : t))));
    }
  };

  const handleDelete = (id) => {
    axios.delete(`http://localhost:8080/api/list/delete/${id}`)
      .then(() => setTasks(tasks.filter(t => t.id !== id)));
  };

  const handleAddTask = () => {
    if (newTask.trim() === "") {
      alert("할 일을 입력하세요!");
      return;
    }
  
    const newTaskItem = { title: newTask, checked: false };
    axios.post("http://localhost:8080/api/list/add", newTaskItem)
      .then(response => {
        setTasks([...tasks, response.data]);
      })
      .catch(error => {
        console.error("Error adding task:", error);
      });
    setNewTask("");
  };

  // 다크모드 토글 함수
  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
    document.body.classList.toggle("dark-mode", !isDarkMode);
  };

  return (
    <div>
      <h1 className="text-center">할 일 목록</h1>
      
      <div>
        <input
          type="text"
          value={newTask}
          onChange={(e) => setNewTask(e.target.value)}
          placeholder="새 할 일을 입력하세요"
        />
        <button onClick={handleAddTask}>추가</button>
      </div>

      <ul>
        {tasks.map(task => (
          <li key={task.id}>
            <input
              type="checkbox"
              checked={task.checked}
              onChange={() => handleCheckboxChange(task)}
            />
            {task.title}
            <button onClick={() => handleEdit(task)}>수정</button>
            <button onClick={() => handleDelete(task.id)}>삭제</button>
          </li>
        ))}
      </ul>

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
