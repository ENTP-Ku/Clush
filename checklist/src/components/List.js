import React, { useEffect, useState } from "react";
import axios from "axios";

const List = () => {
  const [tasks, setTasks] = useState([]);

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

  return (
    <div>
      <h1 className="text-center">할 일 목록</h1>
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
    </div>
  );
};

export default List;
