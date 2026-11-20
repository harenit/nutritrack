// ===== NUTRITRACK SHARED UTILITIES =====

function showToast(msg, type = 'success') {
  const t = document.getElementById('toast');
  if (!t) return;
  t.textContent = msg;
  t.className = 'show ' + type;
  setTimeout(() => t.className = '', 3000);
}

async function apiFetch(url, options = {}) {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options
  });
  return res;
}

async function checkAuth() {
  const res = await fetch('/api/me');
  const data = await res.json();
  if (data === 'not_logged_in') {
    window.location.href = '/login';
    return null;
  }
  return data;
}

async function logout() {
  await fetch('/api/logout', { method: 'POST' });
  window.location.href = '/login';
}

async function deleteAccount() {
  if (!confirm('Are you sure you want to permanently delete your account? This cannot be undone.')) return;
  const res = await fetch('/api/account', { method: 'DELETE' });
  const msg = await res.text();
  if (msg === 'ok') window.location.href = '/login';
  else showToast('Error: ' + msg, 'error');
}

function setActiveNav() {
  const path = window.location.pathname;
  document.querySelectorAll('.nav-link').forEach(a => {
    a.classList.toggle('active', a.getAttribute('href') === path);
  });
}

function getBMIColor(bmi) {
  if (bmi < 18.5) return '#3498db';
  if (bmi < 25) return '#2ecc71';
  if (bmi < 30) return '#f39c12';
  return '#e74c3c';
}

function getWorkoutTip(excess) {
  if (excess <= 0) return null;
  if (excess < 200) return '🚶 Walk for 20 minutes to burn the extra calories.';
  if (excess < 400) return '🏃 Jog for 30 minutes to get back on track.';
  return '💪 Do a 45-minute workout session today!';
}

function formatDate(dateStr) {
  const d = new Date(dateStr + 'T00:00:00');
  return d.toLocaleDateString('en-IN', { weekday: 'short', day: 'numeric', month: 'short' });
}
