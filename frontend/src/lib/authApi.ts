import { HttpError, request } from './http'
import type {
  CurrentUser,
  CsrfTokenResponse,
  LoginCommand,
  RegisterCommand,
} from '../features/auth/authTypes'

let csrfToken: string | null = null

export async function getCsrfToken(): Promise<CsrfTokenResponse> {
  const response = await request<CsrfTokenResponse>('/api/auth/csrf')
  csrfToken = response.data.token
  return response.data
}

// 后端（Spring Security 7）通过接口暴露给应用的 token 是掩码后的表现，
// XSRF-TOKEN Cookie 里保存的原始值不能直接当作请求头使用（实测 403），
// 所以必须始终通过 /api/auth/csrf 获取，并缓存在内存中。
async function ensureCsrfToken(): Promise<string> {
  if (csrfToken) {
    return csrfToken
  }

  const response = await getCsrfToken()
  return response.token
}

async function requestWithCsrf<T>(path: string, init: RequestInit): Promise<T> {
  const token = await ensureCsrfToken()
  const headers = new Headers(init.headers)
  headers.set('X-CSRF-TOKEN', token)

  const response = await request<T>(path, {
    ...init,
    headers,
  })

  return response.data
}

export async function register(command: RegisterCommand): Promise<CurrentUser> {
  return requestWithCsrf<CurrentUser>('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(command),
  })
}

export async function login(command: LoginCommand): Promise<CurrentUser> {
  return requestWithCsrf<CurrentUser>('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(command),
  })
}

export async function getCurrentUser(): Promise<CurrentUser> {
  const response = await request<CurrentUser>('/api/users/me')
  return response.data
}

export async function logout(): Promise<void> {
  await requestWithCsrf<null>('/api/auth/logout', {
    method: 'POST',
  })
}

export function isUnauthorizedError(error: unknown): boolean {
  return error instanceof HttpError && error.status === 401
}
