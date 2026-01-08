import { apiFetch } from "./auth.js";

document.getElementById("loginBtn").addEventListener("click", async () => {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();

  const res = await apiFetch("/api/users/login", {
    method: "POST",
    body: JSON.stringify({ username, password }),
  });

  if (!res) return;

  if (!res.ok) {
    alert("로그인 실패");
    return;
  }

  const data = await res.json();

  // 무조건 accessToken에 저장 (서버 응답 필드명에 맞춰 하나로)
  // 서버가 {token: "..."} 이면 data.token
  // 서버가 {accessToken: "..."} 이면 data.accessToken
  const token = data.accessToken || data.token;
  if (!token) {
    alert("토큰이 없습니다. 로그인 API 응답을 확인하세요.");
    return;
  }

  localStorage.setItem("accessToken", token);
  window.location.href = "/main.html";
});