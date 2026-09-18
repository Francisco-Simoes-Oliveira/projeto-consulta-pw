import api from './api'

export async function login(email, password) {
  const { data } = await api.post('/auth/login', { email, password })
  localStorage.setItem('token', data.token)
  localStorage.setItem('user', JSON.stringify(data))
  return data
}

export async function register(name, email, password) {
  const { data } = await api.post('/auth/register', { name, email, password })
  localStorage.setItem('token', data.token)
  localStorage.setItem('user', JSON.stringify(data))
  return data
}

export function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}

export function currentUser() {
  try { return JSON.parse(localStorage.getItem('user')) } catch { return null }
}
