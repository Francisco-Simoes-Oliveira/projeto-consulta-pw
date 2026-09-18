import api from './api'
export async function listUsers(){ const {data}=await api.get('/users'); return data }
export async function changeRole(id, role){ const {data}=await api.patch(`/users/${id}/role`, {role}); return data }
