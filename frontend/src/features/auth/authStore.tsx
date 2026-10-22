import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type PropsWithChildren,
} from 'react'
import {
  getCurrentUser,
  isUnauthorizedError,
  login as loginRequest,
  logout as logoutRequest,
} from '../../lib/authApi'
import type { AuthStatus, CurrentUser, LoginCommand } from './authTypes'

interface AuthContextValue {
  status: AuthStatus
  currentUser: CurrentUser | null
  error: Error | null
  login: (command: LoginCommand) => Promise<CurrentUser>
  logout: () => Promise<void>
  refresh: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: PropsWithChildren) {
  const [status, setStatus] = useState<AuthStatus>('loading')
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null)
  const [error, setError] = useState<Error | null>(null)

  const refresh = useCallback(() => {
    return getCurrentUser().then(
      (user) => {
        setCurrentUser(user)
        setStatus('authenticated')
        setError(null)
      },
      (refreshError: unknown) => {
        if (isUnauthorizedError(refreshError)) {
          setCurrentUser(null)
          setStatus('anonymous')
          return
        }

        setCurrentUser(null)
        setStatus('error')
        setError(
          refreshError instanceof Error
            ? refreshError
            : new Error('Failed to restore authentication'),
        )
      },
    )
  }, [])

  useEffect(() => {
    void refresh()
  }, [refresh])

  const login = useCallback(async (command: LoginCommand) => {
    const user = await loginRequest(command)
    setCurrentUser(user)
    setStatus('authenticated')
    setError(null)
    return user
  }, [])

  const logout = useCallback(async () => {
    await logoutRequest()
    setCurrentUser(null)
    setStatus('anonymous')
    setError(null)
  }, [])

  const value = useMemo<AuthContextValue>(
    () => ({
      status,
      currentUser,
      error,
      login,
      logout,
      refresh,
    }),
    [currentUser, error, login, logout, refresh, status],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error('useAuth must be used inside AuthProvider')
  }

  return context
}
