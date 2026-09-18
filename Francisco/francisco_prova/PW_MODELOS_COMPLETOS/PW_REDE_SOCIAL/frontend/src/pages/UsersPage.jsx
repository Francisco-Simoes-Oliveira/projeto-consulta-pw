import {useEffect,useState} from 'react'
import {listUsers,changeRole} from '../services/userService'
export default function UsersPage(){
  const [users,setUsers]=useState([]); const [error,setError]=useState('')
  async function load(){try{setUsers(await listUsers())}catch(e){setError(e.response?.data?.message||'Erro ao carregar usuários')}}
  useEffect(()=>{load()},[])
  async function role(id, value){try{await changeRole(id,value); await load()}catch(e){alert(e.response?.data?.message||'Erro ao alterar perfil')}}
  return <main className="page-content"><section className="page-title"><div><h1>Usuários</h1><p className="muted">Área exclusiva do administrador.</p></div></section>{error&&<div className="error-box">{error}</div>}<section className="card admin-only"><div className="table-wrap"><table><thead><tr><th>ID</th><th>Nome</th><th>E-mail</th><th>Perfil</th></tr></thead><tbody>{users.map(u=><tr key={u.id}><td>{u.id}</td><td>{u.name}</td><td>{u.email}</td><td><select value={u.role} onChange={e=>role(u.id,e.target.value)}><option value="USER">USER</option><option value="ADMIN">ADMIN</option></select></td></tr>)}</tbody></table></div></section></main>
}
