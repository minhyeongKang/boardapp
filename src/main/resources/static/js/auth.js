// /js/auth.js (ES Module export 제거 버전)

function getToken() {
  return localStorage.getItem("boardapp_token");
}

function logout() {
  localStorage.removeItem("boardapp_token");
  location.href = "/index.html";
}

function requireAuth() {
  const token = getToken();
  if (!token) {
    location.href = "/index.html";
    return false;
  }
  return true;
}

async function apiFetch(url, options = {}) {
  const token = getToken();
  if (!token) {
    location.href = "/index.html";
    return null;
  }

  const headers = new Headers(options.headers || {});
  headers.set("Authorization", `Bearer ${token}`);

  if (options.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  const res = await fetch(url, { ...options, headers });

  if (res.status === 401 || res.status === 403) {
    localStorage.removeItem("boardapp_token");
    location.href = "/index.html";
    return null;
  }

  return res;
}

// ✅ 전역으로도 쓰고 있으니 window에 등록
window.getToken = getToken;
window.logout = logout;
window.requireAuth = requireAuth;
window.apiFetch = apiFetch;