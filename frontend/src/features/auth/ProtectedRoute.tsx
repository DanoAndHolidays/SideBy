import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from './authStore'

function getReturnTo(location: ReturnType<typeof useLocation>) {
  return `${location.pathname}${location.search}${location.hash}`
}

export function ProtectedRoute() {
  const location = useLocation()
  const { status, error } = useAuth()

  if (status === 'loading') {
    return (
      <main style={{ padding: '2rem' }}>
        <p>正在恢复登录状态，请稍候……</p>
      </main>
    )
  }

  if (status === 'error') {
    return (
      <main style={{ padding: '2rem' }}>
        <h1>暂时无法确认登录状态</h1>
        <p>{error ? error.message : '请检查后端是否启动，然后刷新页面。'}</p>
      </main>
    )
  }

  if (status === 'anonymous') {
    return (
      <Navigate
        to="/login"
        replace
        state={{ returnTo: getReturnTo(location) }}
      />
    )
  }

  return <Outlet />
}
