# frontend/Dockerfile
FROM node:20

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사
COPY package*.json ./

# 의존성 설치
RUN npm install

# 나머지 앱 파일 복사
COPY . .

EXPOSE 5173

# Vite 프로젝트라면 보통 이렇게 실행
CMD ["npm", "run", "dev", "--", "--host"]
