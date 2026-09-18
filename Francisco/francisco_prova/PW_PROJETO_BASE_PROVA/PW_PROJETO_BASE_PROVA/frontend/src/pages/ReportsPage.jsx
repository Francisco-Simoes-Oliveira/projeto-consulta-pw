import { useMemo, useState } from 'react'
import { useItems } from '../hooks/useItems'

function money(value) {
  return Number(value || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

function csvCell(value) {
  return `"${String(value ?? '').replaceAll('"', '""')}"`
}

export default function ReportsPage() {
  const { items, loading, error, reload } = useItems()
  const [filters, setFilters] = useState({ text: '', status: 'all', minPrice: '', maxPrice: '' })
  const [reportTitle, setReportTitle] = useState('Relatório de Itens')
  const [notes, setNotes] = useState('')

  const result = useMemo(() => {
    const text = filters.text.trim().toLowerCase()
    const min = filters.minPrice === '' ? null : Number(filters.minPrice)
    const max = filters.maxPrice === '' ? null : Number(filters.maxPrice)

    return items.filter(item => {
      const content = `${item.id} ${item.name ?? ''} ${item.description ?? ''}`.toLowerCase()
      const price = Number(item.price)
      if (text && !content.includes(text)) return false
      if (filters.status === 'active' && !item.active) return false
      if (filters.status === 'inactive' && item.active) return false
      if (min !== null && price < min) return false
      if (max !== null && price > max) return false
      return true
    })
  }, [items, filters])

  const summary = useMemo(() => {
    const total = result.reduce((sum, item) => sum + Number(item.price || 0), 0)
    const active = result.filter(item => item.active).length
    return {
      count: result.length,
      active,
      inactive: result.length - active,
      total,
      average: result.length ? total / result.length : 0
    }
  }, [result])

  function setFilter(field, value) {
    setFilters(current => ({ ...current, [field]: value }))
  }

  function exportCsv() {
    const rows = [
      ['ID', 'Nome', 'Descrição', 'Preço', 'Status'],
      ...result.map(i => [i.id, i.name, i.description || '', Number(i.price).toFixed(2), i.active ? 'Ativo' : 'Inativo'])
    ]

    const csv = '\uFEFF' + rows.map(row => row.map(csvCell).join(';')).join('\r\n')
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `relatorio-itens-${new Date().toISOString().slice(0, 10)}.csv`
    link.click()
    URL.revokeObjectURL(url)
  }

  return <main className="page-content report-page">
    <section className="page-title no-print">
      <div>
        <h1>Gerador de relatório</h1>
        <p className="muted">Filtre os dados, exporte CSV ou use Imprimir → Salvar como PDF.</p>
      </div>
      <button className="secondary" onClick={reload}>Atualizar dados</button>
    </section>

    {error && <div className="error-box no-print">{error}</div>}

    <section className="card no-print">
      <h2>Configurar relatório</h2>
      <div className="report-config-grid">
        <label className="wide">
          Título do relatório
          <input value={reportTitle} onChange={e => setReportTitle(e.target.value)}/>
        </label>
        <label>
          Buscar
          <input placeholder="Nome, descrição ou ID" value={filters.text} onChange={e => setFilter('text', e.target.value)}/>
        </label>
        <label>
          Status
          <select value={filters.status} onChange={e => setFilter('status', e.target.value)}>
            <option value="all">Todos</option>
            <option value="active">Ativos</option>
            <option value="inactive">Inativos</option>
          </select>
        </label>
        <label>
          Preço mínimo
          <input type="number" min="0" step="0.01" value={filters.minPrice} onChange={e => setFilter('minPrice', e.target.value)}/>
        </label>
        <label>
          Preço máximo
          <input type="number" min="0" step="0.01" value={filters.maxPrice} onChange={e => setFilter('maxPrice', e.target.value)}/>
        </label>
        <label className="wide">
          Observações
          <textarea placeholder="Observações que devem aparecer no relatório..." value={notes} onChange={e => setNotes(e.target.value)}/>
        </label>
      </div>
      <div className="actions">
        <button type="button" onClick={exportCsv} disabled={!result.length}>Exportar CSV</button>
        <button type="button" className="secondary" onClick={() => window.print()}>Imprimir / Salvar PDF</button>
        <button type="button" className="secondary" onClick={() => setFilters({ text: '', status: 'all', minPrice: '', maxPrice: '' })}>Limpar filtros</button>
      </div>
    </section>

    <section className="report-sheet">
      <header className="report-header">
        <div>
          <span className="report-kicker">PROJETO BASE PW</span>
          <h1>{reportTitle || 'Relatório de Itens'}</h1>
          <p>Gerado em {new Date().toLocaleString('pt-BR')}</p>
        </div>
        <div className="report-count">{summary.count}<span>registros</span></div>
      </header>

      <section className="report-summary">
        <div><span>Registros</span><strong>{summary.count}</strong></div>
        <div><span>Ativos</span><strong>{summary.active}</strong></div>
        <div><span>Inativos</span><strong>{summary.inactive}</strong></div>
        <div><span>Valor total</span><strong>{money(summary.total)}</strong></div>
        <div><span>Preço médio</span><strong>{money(summary.average)}</strong></div>
      </section>

      {notes && <section className="report-notes"><strong>Observações</strong><p>{notes}</p></section>}

      {loading ? <p>Carregando...</p> : <div className="table-wrap">
        <table className="report-table">
          <thead><tr><th>ID</th><th>Nome</th><th>Descrição</th><th>Preço</th><th>Status</th></tr></thead>
          <tbody>
            {result.map(item => <tr key={item.id}>
              <td>{item.id}</td>
              <td>{item.name}</td>
              <td>{item.description || '—'}</td>
              <td>{money(item.price)}</td>
              <td>{item.active ? 'Ativo' : 'Inativo'}</td>
            </tr>)}
            {!result.length && <tr><td colSpan="5">Nenhum registro para os filtros informados.</td></tr>}
          </tbody>
        </table>
      </div>}
    </section>
  </main>
}
