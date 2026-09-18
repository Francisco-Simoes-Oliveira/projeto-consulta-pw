import { useState } from 'react'
import LoginPage from './pages/LoginPage'
import ItemsPage from './pages/ItemsPage'
import DashboardPage from './pages/DashboardPage'
import ReportsPage from './pages/ReportsPage'
import { currentUser, logout } from './services/authService'

const pages = {
  dashboard: DashboardPage,
  items: ItemsPage,
  reports: ReportsPage
}

export default function App() {
  const [user, setUser] = useState(currentUser())
  const [page, setPage] = useState('dashboard')

  if (!user || !localStorage.getItem('token')) {
    return <LoginPage onLogged={setUser}/>
  }

  const CurrentPage = pages[page] || DashboardPage

  function handleLogout() {
    logout()
    setUser(null)
  }

  return <div className="app-shell">
    <aside className="sidebar no-print">
      <div className="brand">
        <div className="brand-mark">PW</div>
        <div><strong>Projeto Base</strong><span>React + Spring</span></div>
      </div>

      <nav className="nav-menu">
        <button className={page === 'dashboard' ? 'active' : ''} onClick={() => setPage('dashboard')}>
          <span>▦</span> Dashboard
        </button>
        <button className={page === 'items' ? 'active' : ''} onClick={() => setPage('items')}>
          <span>☷</span> Itens / CRUD
        </button>
        <button className={page === 'reports' ? 'active' : ''} onClick={() => setPage('reports')}>
          <span>▤</span> Relatórios
        </button>
      </nav>

      <div className="sidebar-user">
        <strong>{user?.name}</strong>
        <span>{user?.email}</span>
        <span className="role-pill">{user?.role}</span>
        <button className="danger" onClick={handleLogout}>Sair</button>
      </div>
    </aside>

    <div className="main-area">
      <header className="mobile-header no-print">
        <strong>PW Projeto Base</strong>
        <select value={page} onChange={e => setPage(e.target.value)}>
          <option value="dashboard">Dashboard</option>
          <option value="items">Itens / CRUD</option>
          <option value="reports">Relatórios</option>
        </select>
      </header>
      <CurrentPage user={user}/>
    </div>
  </div>
}
