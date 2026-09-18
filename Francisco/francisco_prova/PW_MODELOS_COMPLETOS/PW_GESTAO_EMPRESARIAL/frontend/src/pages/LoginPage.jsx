import { useState } from 'react'
import { login, register } from '../services/authService'

export default function LoginPage({ onLogged }) {
  const [mode, setMode] = useState('login')
  const [form, setForm] = useState({ name: '', email: 'admin@teste.com', password: '123456' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function submit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const data = mode === 'login'
        ? await login(form.email, form.password)
        : await register(form.name, form.email, form.password)
      onLogged(data)
    } catch (err) {
      setError(err.response?.data?.message || 'Não foi possível autenticar')
    } finally {
      setLoading(false)
    }
  }

  return <main className="center-page">
    <form className="card auth-card" onSubmit={submit}>
      <h1>{mode === 'login' ? 'Entrar' : 'Cadastrar'}</h1>
      <p className="muted">Gestão completa: estoque, vendas, financeiro, relatórios e permissões.</p>
      {mode === 'register' && <label>Nome<input value={form.name} onChange={e => setForm({...form, name:e.target.value})}/></label>}
      <label>E-mail<input type="email" value={form.email} onChange={e => setForm({...form, email:e.target.value})}/></label>
      <label>Senha<input type="password" value={form.password} onChange={e => setForm({...form, password:e.target.value})}/></label>
      {error && <div className="error">{error}</div>}
      <button disabled={loading}>{loading ? 'Carregando...' : mode === 'login' ? 'Entrar' : 'Cadastrar'}</button>
      <button type="button" className="secondary" onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>
        {mode === 'login' ? 'Criar conta' : 'Já tenho conta'}
      </button>
      <div className="hint">ADMIN: admin@teste.com / 123456 • USER: usuario@teste.com / 123456</div>
    </form>
  </main>
}
