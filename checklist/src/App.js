import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import List from "./components/List";

function App() {
  return (
    <Router>
      <div className="app-container">
        <header className="bg-blue-500 text-white text-center p-4">
          <h1>할 일 체크리스트</h1>
        </header>
        <main className="p-4">
          <Routes>
            <Route path="/" element={<List />} />
          </Routes>
        </main>
        <footer className="bg-gray-200 text-center p-2">
          <p>© 2025 CheckList App</p>
        </footer>
      </div>
    </Router>
  );
}

export default App;
