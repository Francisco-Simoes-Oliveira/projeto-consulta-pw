import api from './api'
export const dashboard=async()=> (await api.get('/dashboard')).data
export const products=async()=> (await api.get('/products')).data
export const saveProduct=async(id,p)=> (await (id?api.put(`/products/${id}`,p):api.post('/products',p))).data
export const deleteProduct=async(id)=> api.delete(`/products/${id}`)
export const moveStock=async(id,p)=> (await api.post(`/products/${id}/stock`,{...p,productId:id})).data
export const movements=async()=> (await api.get('/stock/movements')).data
export const finance=async()=> (await api.get('/finance')).data
export const saveFinance=async(id,p)=> (await (id?api.put(`/finance/${id}`,p):api.post('/finance',p))).data
export const deleteFinance=async(id)=> api.delete(`/finance/${id}`)
export const sales=async()=> (await api.get('/sales')).data
export const createSale=async(p)=> (await api.post('/sales',p)).data
