import api from './api'

export async function listItems(search = '') {
  const { data } = await api.get('/items', { params: search ? { search } : {} })
  return data
}

export async function createItem(item) {
  const { data } = await api.post('/items', item)
  return data
}

export async function updateItem(id, item) {
  const { data } = await api.put(`/items/${id}`, item)
  return data
}

export async function deleteItem(id) {
  await api.delete(`/items/${id}`)
}
