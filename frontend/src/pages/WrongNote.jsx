import React, { useEffect, useState } from "react";
import axios from "axios";

function WrongNote() {
  const [wrongList, setWrongList] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/quiz/wrong")
      .then(res => setWrongList(res.data))
      .catch(err => console.error("오답 불러오기 실패", err));
  }, []);

  if (wrongList.length === 0) return <p>오답이 없습니다!</p>;

  return (
    <div>
      <h2>오답 노트</h2>
      <ul>
        {wrongList.map((quiz, index) => (
          <li key={quiz.id}>
            <p>{index + 1}. {quiz.sentence}</p>
            <ul>
              {quiz.choices.map(choice => (
                <li key={choice}>{choice}</li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default WrongNote;
