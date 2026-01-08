import { apiFetch, requireTokenOrRedirect } from "./auth.js";

const token = requireTokenOrRedirect();
if (!token) throw new Error("No token");

// ✅ 버튼 존재 여부 방어
const myPageBtn = document.getElementById("myPageBtn");
if (myPageBtn) {
  myPageBtn.addEventListener("click", () => {
    window.location.href = "/mypage.html";
  });
}

// 전체 피드 불러오기
async function loadFeed() {
  const res = await apiFetch("/api/boards");
  if (!res) return;

  const list = await res.json();
  renderFeed(list);
}

function renderFeed(list) {
  // TODO: 기존 로직 유지/확장
}

loadFeed();