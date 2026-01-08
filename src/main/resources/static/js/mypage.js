import { apiFetch, requireTokenOrRedirect } from "./auth.js";

const token = requireTokenOrRedirect();
if (!token) throw new Error("No token");

async function loadMe() {
  const res = await apiFetch("/api/users/me");
  if (!res) return;

  const me = await res.json();

  // ✅ nickname / email 정확히 매핑
  document.getElementById("nickname").innerText = me.nickname || "-";
  document.getElementById("email").innerText = me.email || "-";
}

async function loadMyBoards() {
  const res = await apiFetch("/api/boards/mine");
  if (!res) return;

  const list = await res.json();
  renderMyBoards(list);
}

function renderMyBoards(list) {
  const wrap = document.getElementById("myBoards");
  wrap.innerHTML = "";

  if (!list || list.length === 0) {
    wrap.innerHTML = `<div class="empty">아직 작성한 게시글이 없습니다.</div>`;
    return;
  }

  for (const b of list) {
    const el = document.createElement("div");
    el.className = "post-card";
    el.innerHTML = `
      <div class="post-title">${escapeHtml(b.title)}</div>
      <div class="post-content">${escapeHtml(b.content)}</div>
      <div class="post-date">${b.createdAt ? escapeHtml(String(b.createdAt)) : ""}</div>
    `;
    wrap.appendChild(el);
  }
}

function escapeHtml(str = "") {
  return String(str)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

loadMe();
loadMyBoards();