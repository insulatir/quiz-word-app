import React, { useEffect, useState } from "react";
import axios from "axios";

function QuizPage() {
  const [quizList, setQuizList] = useState([]);
  const [currentQuizIndex, setCurrentQuizIndex] = useState(0);
  const [score, setScore] = useState(0);
  const [result, setResult] = useState(null);
  const [isFinished, setIsFinished] = useState(false);
  const [wrongList, setWrongList] = useState([]);
  const [totalQuizCount, setTotalQuizCount] = useState(10);
  const [favorites, setFavorites] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/quiz/favorites", { withCredentials: true })
      .then((res) => {
        const ids = res.data.map((q) => q.id);
        setFavorites(ids);
      });
  }, []);

  const toggleFavorite = (quizId) => {
    axios
      .post(
        "http://localhost:8080/quiz/favorite/toggle",
        { quizId },
        { withCredentials: true }
      )
      .then(() => {
        setFavorites((prev) =>
          prev.includes(quizId)
            ? prev.filter((id) => id !== quizId)
            : [...prev, quizId]
        );
      });
  };

  // 새로고침 시 퀴즈 복원
  useEffect(() => {
    axios
      .get("http://localhost:8080/quiz/session/state", {
        withCredentials: true,
      })
      .then((res) => {
        if (res.data.finished) {
          setIsFinished(true);
          setTotalQuizCount(res.data.totalCount);
        } else {
          const { quizId, sentence, choices, currentIndex, totalCount } =
            res.data;

          const newList = new Array(totalCount).fill(null);
          newList[currentIndex] = { quizId, sentence, choices };

          setQuizList(newList);
          setCurrentQuizIndex(currentIndex);
          setTotalQuizCount(totalCount);
        }
      })
      .catch(() => {
        // 세션 없으면 새로 시작
        axios
          .post(
            "http://localhost:8080/quiz/session/start",
            {},
            { withCredentials: true }
          )
          .then(() => window.location.reload());
      });
  }, []);

  // 정답 선택
  const handleAnswer = (choice) => {
    const currentQuiz = quizList[currentQuizIndex];
    if (!currentQuiz) return;

    axios
      .post(
        "http://localhost:8080/quiz/answer",
        {
          quizId: currentQuiz.quizId,
          userChoice: choice,
        },
        { withCredentials: true }
      )
      .then((res) => {
        setResult(res.data);

        if (res.data.correct) {
          setScore((prev) => prev + 1);
        } else {
          setWrongList((prev) => [
            ...prev,
            {
              sentence: currentQuiz.sentence,
              correctAnswer: res.data.message
                .split("정답은 ")[1]
                ?.replace("입니다。", ""),
              userChoice: choice,
            },
          ]);
        }

        // 다음 문제 요청
        setTimeout(() => {
          axios
            .get("http://localhost:8080/quiz/", { withCredentials: true })
            .then((res) => {
              if (res.data.finished) {
                setIsFinished(true);
              } else {
                setQuizList((prev) => {
                  const updated = [...prev];
                  updated[currentQuizIndex + 1] = res.data;
                  return updated;
                });

                setCurrentQuizIndex((prev) => prev + 1);
                setResult(null);
              }
            });
        }, 1000);
      });
  };

  // 다시 시작
  const handleRestart = () => {
    axios
      .post(
        "http://localhost:8080/quiz/session/start",
        {},
        { withCredentials: true }
      )
      .then(() => window.location.reload());
  };

  const currentQuiz = quizList[currentQuizIndex];

  if (isFinished) {
    return (
      <div>
        <h2>퀴즈 완료!</h2>
        <p>
          정답 수: {score} / {totalQuizCount}
        </p>

        <h3>오답 노트</h3>
        {wrongList.length === 0 ? (
          <p>모든 문제를 맞혔어요!</p>
        ) : (
          <ul>
            {wrongList.map((item, idx) => (
              <li key={idx}>
                <strong>{item.sentence}</strong>
                <br />❌ 당신의 선택: {item.userChoice}
                <br />✅ 정답: {item.correctAnswer}
              </li>
            ))}
          </ul>
        )}

        <button onClick={handleRestart}>다시 시작</button>
      </div>
    );
  }

  if (!currentQuiz) {
    return <div>로딩 중...</div>;
  }

  return (
    <div>
      <h3>
        문제 {currentQuizIndex + 1} / {totalQuizCount}
      </h3>
      <p>
        진행률: {(((currentQuizIndex + 1) / totalQuizCount) * 100).toFixed(0)}%
      </p>
      <button onClick={() => toggleFavorite(currentQuiz.quizId)}>
        {favorites.includes(currentQuiz.quizId) ? "⭐" : "☆"}
      </button>
      <p>{currentQuiz.sentence}</p>
      <ul>
        {Array.isArray(currentQuiz?.choices) &&
          currentQuiz.choices.map((choice) => (
            <li key={choice}>
              <button onClick={() => handleAnswer(choice)}>{choice}</button>
            </li>
          ))}
      </ul>

      {result && (
        <div>
          <h4>{result.correct ? "✅ 정답" : "❌ 오답"}</h4>
          <p>{result.message}</p>
        </div>
      )}
    </div>
  );
}

export default QuizPage;
