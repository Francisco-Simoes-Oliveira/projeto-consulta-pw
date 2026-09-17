import { useEffect, useState } from 'react'
import { createItem, deleteItem, listItems, updateItem } from '../services/itemService'
import { subscribeItems } from '../services/socketService'

const empty = { name: '', description: '', price: '', active: true }

export default function ItemsPage({ user, onLogout }) {
  const [items, setItems] = useState([])
  const [form, setForm] = useState(empty)
  const [editingId, setEditingId] = useState(null)
  const [search, setSearch] = useState('')
  const [errors, setErrors] = useState({})
  const [status, setStatus] = useState('')

  async function load(term = search) {
    try { setItems(await listItems(term)) }
    catch { setStatus('Erro ao carregar. Confira backend/JWT.') }
  }

  useEffect(() => {
    load('')
    return subscribeItems(() => load())
  }, [])

  async function submit(e) {
    e.preventDefault()
    setErrors({})
    setStatus('')
    const payload = { ...form, price: Number(form.price) }
    try {
      if (editingId) await updateItem(editingId, payload)
      else await createItem(payload)
      setForm(empty)
      setEditingId(null)
      await load()
    } catch (err) {
      setErrors(err.response?.data?.errors || {})
      setStatus(err.response?.data?.message || 'Erro ao salvar')
    }
  }

  function edit(item) {
    setEditingId(item.id)
    setForm({ name:item.name, description:item.description || '', price:item.price, active:item.active })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  async function remove(id) {
    if (!confirm('Excluir este item?')) return
    await deleteItem(id)
    await load()
  }

  return <main className="container">
    <header className="topbar">
      <div><h1>CRUD genérico</h1><span className="muted">Logado como {user?.name} ({user?.role})</span></div>
      <button className="danger" onClick={onLogout}>Sair</button>
    </header>

    <section className="card">
      <h2>{editingId ? `Editar Item #${editingId}` : 'Novo Item'}</h2>
      <form className="form-grid" onSubmit={submit}>
        <label>Nome<input value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/><small>{errors.name}</small></label>
        <label>Preço<input type="number" step="0.01" value={form.price} onChange={e=>setForm({...form,price:e.target.value})}/><small>{errors.price}</small></label>
        <label className="wide">Descrição<textarea value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/><small>{errors.description}</small></label>
        <label className="check"><input type="checkbox" checked={form.active} onChange={e=>setForm({...form,active:e.target.checked})}/> Ativo</label>
        <div className="actions"><button>{editingId ? 'Salvar alteração' : 'Cadastrar'}</button>{editingId && <button type="button" className="secondary" onClick={()=>{setEditingId(null);setForm(empty)}}>Cancelar</button>}</div>
      </form>
      {status && <div className="error">{status}</div>}
    </section>

    <section className="card">
      <div className="list-head"><h2>Itens</h2><form onSubmit={e=>{e.preventDefault();load()}}><input placeholder="Pesquisar por nome" value={search} onChange={e=>setSearch(e.target.value)}/><button>Buscar</button></form></div>
      <div className="table-wrap"><table><thead><tr><th>ID</th><th>Nome</th><th>Preço</th><th>Ativo</th><th>Ações</th></tr></thead><tbody>
        {items.map(item => <tr key={item.id}><td>{item.id}</td><td><strong>{item.name}</strong><div className="muted">{item.description}</div></td><td>R$ {Number(item.price).toFixed(2)}</td><td>{item.active ? 'Sim' : 'Não'}</td><td className="row-actions"><button onClick={()=>edit(item)}>Editar</button><button className="danger" onClick={()=>remove(item.id)}>Excluir</button></td></tr>)}
        {!items.length && <tr><td colSpan="5">Nenhum item encontrado.</td></tr>}
      </tbody></table></div>
      <p className="muted">WebSocket: alterações feitas em outra aba atualizam esta tabela automaticamente.</p>
    </section>
  </main>
}
