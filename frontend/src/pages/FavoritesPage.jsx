import React, { useEffect, useState } from "react";
import axios from "axios";

function FavoritesPage() {
  const [favoriteQuizzes, setFavoriteQuizzes] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/quiz/favorites", { withCredentials: true })
      .then((res) => {
        setFavoriteQuizzes(res.data);
      });
  }, []);

  if (favoriteQuizzes.length === 0) {
    return <div>⭐ 즐겨찾기한 문제가 없습니다.</div>;
  }

  return (
    <div>
      <h2>⭐ 즐겨찾기한 퀴즈</h2>
      <ul>
        {favoriteQuizzes.map((quiz) => (
          <li key={quiz.id} style={{ marginBottom: "2rem" }}>
            <strong>{quiz.sentence}</strong>
            <ul>
              {quiz.choices.map((choice) => (
                <li key={choice}>{choice}</li>
              ))}
            </ul>
            <p>정답: {quiz.answer}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default FavoritesPage;
