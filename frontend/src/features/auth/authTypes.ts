export interface CurrentUser {
  id: string
  username: string
  email: string
  displayName: string
  avatarUrl: string | null
  bio: string | null
}

export interface CsrfTokenResponse {
  headerName: string
  token: string
}

export interface LoginCommand {
  identifier: string
  password: string
}

export interface RegisterCommand {
  username: string
  email: string
  password: string
  displayName: string
}

export type AuthStatus = 'loading' | 'authenticated' | 'anonymous' | 'error'
