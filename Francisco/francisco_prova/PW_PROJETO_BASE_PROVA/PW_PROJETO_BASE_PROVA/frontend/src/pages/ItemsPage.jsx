import { useEffect, useMemo, useState } from 'react'
import { createItem, deleteItem, listItems, updateItem } from '../services/itemService'
import { subscribeItems } from '../services/socketService'

const empty = { name: '', description: '', price: '', active: true }

const initialFilters = {
  text: '',
  status: 'all',
  minPrice: '',
  maxPrice: '',
  sort: 'id-desc'
}

export default function ItemsPage({ user }) {
  const [items, setItems] = useState([])
  const [form, setForm] = useState(empty)
  const [editingId, setEditingId] = useState(null)
  const [filters, setFilters] = useState(initialFilters)
  const [errors, setErrors] = useState({})
  const [status, setStatus] = useState('')

  async function load() {
    try {
      setItems(await listItems())
    } catch {
      setStatus('Erro ao carregar. Confira backend/JWT.')
    }
  }

  useEffect(() => {
    load()
    return subscribeItems(() => load())
  }, [])

  const filteredItems = useMemo(() => {
    const text = filters.text.trim().toLowerCase()
    const min = filters.minPrice === '' ? null : Number(filters.minPrice)
    const max = filters.maxPrice === '' ? null : Number(filters.maxPrice)

    const result = items.filter((item) => {
      const searchable = `${item.id} ${item.name ?? ''} ${item.description ?? ''}`.toLowerCase()
      const price = Number(item.price)

      if (text && !searchable.includes(text)) return false
      if (filters.status === 'active' && !item.active) return false
      if (filters.status === 'inactive' && item.active) return false
      if (min !== null && !Number.isNaN(min) && price < min) return false
      if (max !== null && !Number.isNaN(max) && price > max) return false
      return true
    })

    return [...result].sort((a, b) => {
      switch (filters.sort) {
        case 'id-asc': return Number(a.id) - Number(b.id)
        case 'id-desc': return Number(b.id) - Number(a.id)
        case 'name-asc': return String(a.name).localeCompare(String(b.name), 'pt-BR')
        case 'name-desc': return String(b.name).localeCompare(String(a.name), 'pt-BR')
        case 'price-asc': return Number(a.price) - Number(b.price)
        case 'price-desc': return Number(b.price) - Number(a.price)
        case 'status-active': return Number(b.active) - Number(a.active)
        case 'status-inactive': return Number(a.active) - Number(b.active)
        default: return 0
      }
    })
  }, [items, filters])

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
    setForm({
      name: item.name,
      description: item.description || '',
      price: item.price,
      active: item.active
    })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  async function remove(id) {
    if (!confirm('Excluir este item?')) return
    await deleteItem(id)
    await load()
  }

  function updateFilter(field, value) {
    setFilters((current) => ({ ...current, [field]: value }))
  }

  function clearFilters() {
    setFilters(initialFilters)
  }

  return <main className="page-content">
    <section className="page-title">
      <div>
        <h1>Itens</h1>
        <p className="muted">CRUD genérico • logado como {user?.name} ({user?.role})</p>
      </div>
    </section>

    <section className="card">
      <h2>{editingId ? `Editar Item #${editingId}` : 'Novo Item'}</h2>
      <form className="form-grid" onSubmit={submit}>
        <label>
          Nome
          <input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })}/>
          <small>{errors.name}</small>
        </label>

        <label>
          Preço
          <input type="number" step="0.01" value={form.price} onChange={e => setForm({ ...form, price: e.target.value })}/>
          <small>{errors.price}</small>
        </label>

        <label className="wide">
          Descrição
          <textarea value={form.description} onChange={e => setForm({ ...form, description: e.target.value })}/>
          <small>{errors.description}</small>
        </label>

        <label className="check">
          <input type="checkbox" checked={form.active} onChange={e => setForm({ ...form, active: e.target.checked })}/>
          Ativo
        </label>

        <div className="actions">
          <button>{editingId ? 'Salvar alteração' : 'Cadastrar'}</button>
          {editingId && <button type="button" className="secondary" onClick={() => { setEditingId(null); setForm(empty) }}>Cancelar</button>}
        </div>
      </form>
      {status && <div className="error">{status}</div>}
    </section>

    <section className="card">
      <div className="list-head">
        <div>
          <h2>Tabela de itens</h2>
          <p className="muted">Exibindo {filteredItems.length} de {items.length} itens</p>
        </div>
        <button className="secondary" type="button" onClick={load}>Atualizar</button>
      </div>

      <div className="filter-panel">
        <label className="filter-search">
          Buscar
          <input
            placeholder="Nome, descrição ou ID..."
            value={filters.text}
            onChange={e => updateFilter('text', e.target.value)}
          />
        </label>

        <label>
          Status
          <select value={filters.status} onChange={e => updateFilter('status', e.target.value)}>
            <option value="all">Todos</option>
            <option value="active">Somente ativos</option>
            <option value="inactive">Somente inativos</option>
          </select>
        </label>

        <label>
          Preço mínimo
          <input
            type="number"
            step="0.01"
            min="0"
            placeholder="0,00"
            value={filters.minPrice}
            onChange={e => updateFilter('minPrice', e.target.value)}
          />
        </label>

        <label>
          Preço máximo
          <input
            type="number"
            step="0.01"
            min="0"
            placeholder="999,99"
            value={filters.maxPrice}
            onChange={e => updateFilter('maxPrice', e.target.value)}
          />
        </label>

        <label>
          Ordenar por
          <select value={filters.sort} onChange={e => updateFilter('sort', e.target.value)}>
            <option value="id-desc">ID: maior primeiro</option>
            <option value="id-asc">ID: menor primeiro</option>
            <option value="name-asc">Nome: A → Z</option>
            <option value="name-desc">Nome: Z → A</option>
            <option value="price-asc">Preço: menor primeiro</option>
            <option value="price-desc">Preço: maior primeiro</option>
            <option value="status-active">Ativos primeiro</option>
            <option value="status-inactive">Inativos primeiro</option>
          </select>
        </label>

        <div className="filter-actions">
          <button type="button" className="secondary" onClick={clearFilters}>Limpar filtros</button>
        </div>
      </div>

      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Preço</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {filteredItems.map(item => <tr key={item.id}>
              <td>{item.id}</td>
              <td>
                <strong>{item.name}</strong>
                <div className="muted">{item.description}</div>
              </td>
              <td>R$ {Number(item.price).toFixed(2)}</td>
              <td><span className={`badge ${item.active ? 'badge-success' : 'badge-muted'}`}>{item.active ? 'Ativo' : 'Inativo'}</span></td>
              <td className="row-actions">
                <button onClick={() => edit(item)}>Editar</button>
                <button className="danger" onClick={() => remove(item.id)}>Excluir</button>
              </td>
            </tr>)}
            {!filteredItems.length && <tr><td colSpan="5">Nenhum item corresponde aos filtros.</td></tr>}
          </tbody>
        </table>
      </div>

      <p className="muted">WebSocket: alterações feitas em outra aba atualizam esta tabela automaticamente.</p>
    </section>
  </main>
}
