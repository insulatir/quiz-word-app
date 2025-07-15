import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import QuizPage from "./pages/QuizPage";
import FavoritesPage from "./pages/FavoritesPage";

function App() {
  return (
    <Router>
      <nav>
        <Link to="/">퀴즈</Link> | <Link to="/favorites">⭐ 즐겨찾기</Link>
      </nav>

      <Routes>
        <Route path="/" element={<QuizPage />} />
        <Route path="/favorites" element={<FavoritesPage />} />
      </Routes>
    </Router>
  );
}

export default App;
