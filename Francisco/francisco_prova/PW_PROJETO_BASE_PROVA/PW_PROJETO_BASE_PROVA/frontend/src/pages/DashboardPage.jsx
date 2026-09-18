import { useMemo } from 'react'
import { useItems } from '../hooks/useItems'

function money(value) {
  return Number(value || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

export default function DashboardPage() {
  const { items, loading, error, reload } = useItems()

  const stats = useMemo(() => {
    const active = items.filter(i => i.active).length
    const inactive = items.length - active
    const totalValue = items.reduce((sum, i) => sum + Number(i.price || 0), 0)
    const average = items.length ? totalValue / items.length : 0
    const highest = [...items].sort((a, b) => Number(b.price) - Number(a.price))[0]

    return { active, inactive, totalValue, average, highest }
  }, [items])

  const activePercent = items.length ? Math.round((stats.active / items.length) * 100) : 0
  const recent = [...items].sort((a, b) => Number(b.id) - Number(a.id)).slice(0, 5)
  const topPrices = [...items].sort((a, b) => Number(b.price) - Number(a.price)).slice(0, 5)
  const maxPrice = Math.max(...topPrices.map(i => Number(i.price || 0)), 1)

  return <main className="page-content">
    <section className="page-title">
      <div>
        <h1>Dashboard</h1>
        <p className="muted">Visão geral construída com dados reais da API.</p>
      </div>
      <button className="secondary" onClick={reload}>Atualizar</button>
    </section>

    {error && <div className="error-box">{error}</div>}
    {loading && <div className="card">Carregando indicadores...</div>}

    {!loading && <>
      <section className="stats-grid">
        <article className="stat-card">
          <span>Total de itens</span>
          <strong>{items.length}</strong>
          <small>Registros cadastrados</small>
        </article>
        <article className="stat-card">
          <span>Itens ativos</span>
          <strong>{stats.active}</strong>
          <small>{activePercent}% do total</small>
        </article>
        <article className="stat-card">
          <span>Valor total</span>
          <strong>{money(stats.totalValue)}</strong>
          <small>Soma dos preços</small>
        </article>
        <article className="stat-card">
          <span>Preço médio</span>
          <strong>{money(stats.average)}</strong>
          <small>Média dos registros</small>
        </article>
      </section>

      <section className="dashboard-grid">
        <article className="card">
          <div className="section-title">
            <div>
              <h2>Status dos itens</h2>
              <p className="muted">Distribuição entre ativos e inativos.</p>
            </div>
          </div>

          <div className="status-overview">
            <div className="donut" style={{ '--active-percent': `${activePercent}%` }}>
              <div><strong>{activePercent}%</strong><span>ativos</span></div>
            </div>
            <div className="legend">
              <div><span className="legend-dot active-dot"></span><strong>{stats.active}</strong> Ativos</div>
              <div><span className="legend-dot inactive-dot"></span><strong>{stats.inactive}</strong> Inativos</div>
            </div>
          </div>
        </article>

        <article className="card">
          <h2>Maiores preços</h2>
          <p className="muted">Exemplo de gráfico sem biblioteca externa.</p>
          <div className="bar-chart">
            {topPrices.length ? topPrices.map(item => <div className="bar-row" key={item.id}>
              <div className="bar-label"><span>{item.name}</span><strong>{money(item.price)}</strong></div>
              <div className="bar-track"><div className="bar-value" style={{ width: `${(Number(item.price) / maxPrice) * 100}%` }}></div></div>
            </div>) : <p className="muted">Cadastre itens para visualizar o gráfico.</p>}
          </div>
        </article>
      </section>

      <section className="card">
        <div className="section-title">
          <div>
            <h2>Últimos registros</h2>
            <p className="muted">Os cinco maiores IDs cadastrados.</p>
          </div>
          {stats.highest && <span className="highlight-pill">Maior preço: {stats.highest.name} • {money(stats.highest.price)}</span>}
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Nome</th><th>Preço</th><th>Status</th></tr></thead>
            <tbody>
              {recent.map(item => <tr key={item.id}>
                <td>{item.id}</td>
                <td>{item.name}</td>
                <td>{money(item.price)}</td>
                <td><span className={`badge ${item.active ? 'badge-success' : 'badge-muted'}`}>{item.active ? 'Ativo' : 'Inativo'}</span></td>
              </tr>)}
              {!recent.length && <tr><td colSpan="4">Nenhum item cadastrado.</td></tr>}
            </tbody>
          </table>
        </div>
      </section>
    </>}
  </main>
}
