import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./components/Login"; // Login 컴포넌트 import
import Register from "./components/Register"; // Register 컴포넌트 import
import List from "./components/List"; // List 컴포넌트 import

function App() {
  return (
    <Router>
      <div className="app-container">
        <main className="p-4">
          <Routes>
            {/* 첫 화면을 Login.js로 설정 */}
            <Route path="/" element={<Login />} /> 
            
            {/* Register 페이지로 가는 경로 설정 */}
            <Route path="/register" element={<Register />} />
            
            {/* List 페이지로 가는 경로 설정 */}
            <Route path="/list" element={<List />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
