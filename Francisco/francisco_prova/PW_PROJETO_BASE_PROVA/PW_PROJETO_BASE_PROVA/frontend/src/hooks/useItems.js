import { useEffect, useState } from 'react'
import { listItems } from '../services/itemService'
import { subscribeItems } from '../services/socketService'

export function useItems() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  async function reload() {
    try {
      setError('')
      setItems(await listItems())
    } catch (err) {
      setError(err.response?.data?.message || 'Erro ao carregar os itens')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    reload()
    return subscribeItems(() => reload())
  }, [])

  return { items, loading, error, reload }
}
