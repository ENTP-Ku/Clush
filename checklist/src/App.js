import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./components/Login"; // Login 컴포넌트 import
import Register from "./components/Register"; // Register 컴포넌트 import
import List from "./components/List"; // List 컴포넌트 import
import Share from "./components/Share"; // Share 컴포넌트 import 추가

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

            {/* Share 페이지로 가는 경로 설정 */}
            <Route path="/share" element={<Share />} /> {/* 새로운 경로 추가 */}
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
