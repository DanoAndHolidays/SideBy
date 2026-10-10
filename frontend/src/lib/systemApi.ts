import { request } from './http'

export interface SystemStatus {
  application: string
  status: 'UP'
  database: 'UP'
}

export async function getSystemStatus(signal?: AbortSignal) {
  const response = await request<SystemStatus>('/api/system/status', { signal })
  return response.data
}
