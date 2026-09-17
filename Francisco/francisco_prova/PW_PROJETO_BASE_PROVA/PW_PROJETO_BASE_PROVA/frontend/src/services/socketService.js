import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { API_URL } from './api'

export function subscribeItems(onMessage) {
  const client = new Client({
    webSocketFactory: () => new SockJS(`${API_URL}/ws`),
    reconnectDelay: 3000,
    onConnect: () => {
      client.subscribe('/topic/items', (message) => {
        onMessage(JSON.parse(message.body))
      })
    }
  })
  client.activate()
  return () => client.deactivate()
}
