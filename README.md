# TaskFlow API

TaskFlow is a small Spring Boot backend project I built to practice REST API design and backend structure.

The idea is simple: create a task, break it into steps, and track progress.
Each task can have multiple steps, and each step can be marked as done or undone.
You can also complete all steps at once, reset them, and check the overall progress percentage.

I built this project to better understand how backend layers work together
(controller → service → model) and how to design clean and consistent REST endpoints.

---

## Tech Stack

- Java 17
- Spring Boot
- Maven
- Docker

---

## Features

- Create and delete tasks
- Auto-generate steps for a task
- Toggle individual steps
- Complete all / uncomplete all
- Reset a task
- Track progress percentage
- RESTful API design

---

## Run Locally

mvn spring-boot:run

Server runs at:
http://localhost:8080

---

## Run with Docker

docker build -t taskflow-api .
docker run -p 8080:8080 taskflow-api

---

## What I Learned

- How to structure a layered backend application
- How to design REST APIs clearly
- How to manage state inside the service layer
- Basic containerization using Docker

---

## Future Improvements

- Add a real database (PostgreSQL)
- Add authentication
- Add unit tests
- Deploy to a cloud platform

---

Qida Wang
