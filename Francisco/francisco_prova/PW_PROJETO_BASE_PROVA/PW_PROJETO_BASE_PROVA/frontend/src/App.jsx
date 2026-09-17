import { useState } from 'react'
import LoginPage from './pages/LoginPage'
import ItemsPage from './pages/ItemsPage'
import { currentUser, logout } from './services/authService'

export default function App() {
  const [user, setUser] = useState(currentUser())
  if (!user || !localStorage.getItem('token')) return <LoginPage onLogged={setUser}/>
  return <ItemsPage user={user} onLogout={() => { logout(); setUser(null) }}/>
}
