import { useState } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../features/auth/authStore'
import styles from './AppLayout.module.css'

const navigation = [
  { to: '/activities', label: '发现', symbol: '⌁' },
  { to: '/activities/create', label: '发布', symbol: '+' },
  { to: '/notifications', label: '消息', symbol: '◇' },
  { to: '/me', label: '我的', symbol: '○' },
]

function Navigation() {
  return (
    <nav className={styles.navigation} aria-label="主要导航">
      {navigation.map((item) => (
        <NavLink
          key={item.to}
          to={item.to}
          className={({ isActive }) =>
            `${styles.navLink} ${isActive ? styles.active : ''}`
          }
        >
          <span className={styles.navSymbol} aria-hidden="true">
            {item.symbol}
          </span>
          <span>{item.label}</span>
        </NavLink>
      ))}
    </nav>
  )
}

export function AppLayout() {
  const navigate = useNavigate()
  const { status, currentUser, logout } = useAuth()
  const [loggingOut, setLoggingOut] = useState(false)

  async function handleLogout() {
    setLoggingOut(true)

    try {
      await logout()
      navigate('/login', { replace: true })
    } finally {
      setLoggingOut(false)
    }
  }

  return (
    <div className={styles.shell}>
      <aside className={styles.sidebar}>
        <NavLink
          to="/activities"
          className={styles.brand}
          aria-label="SideBy 首页"
        >
          <span className={styles.brandMark} aria-hidden="true">
            S
          </span>
          <span>
            <strong>SideBy</strong>
            <small>一起去做点什么</small>
          </span>
        </NavLink>
        <Navigation />

        <div className={styles.sidebarAuth}>
          {status === 'authenticated' && currentUser ? (
            <>
              <span
                className={styles.sidebarAuthLabel}
                title={currentUser.email}
              >
                {currentUser.displayName || currentUser.username}
              </span>
              <button
                type="button"
                className={styles.sidebarLogout}
                onClick={handleLogout}
                disabled={loggingOut}
              >
                {loggingOut ? '退出中……' : '退出'}
              </button>
            </>
          ) : status === 'loading' ? (
            <span className={styles.sidebarAuthLabel}>检查登录状态…</span>
          ) : (
            <NavLink to="/login" className={styles.sidebarAuthLabel}>
              登录
            </NavLink>
          )}
        </div>

        <p className={styles.sidebarNote}>在陌生城市，也能找到同行的人。</p>
      </aside>

      <div className={styles.contentFrame}>
        <header className={styles.mobileHeader}>
          <NavLink to="/activities" className={styles.mobileBrand}>
            <span className={styles.brandMark}>S</span>
            <strong>SideBy</strong>
          </NavLink>

          {status === 'authenticated' && currentUser ? (
            <button
              type="button"
              className={styles.loginLink}
              onClick={handleLogout}
              disabled={loggingOut}
              style={{
                padding: 0,
                font: 'inherit',
                background: 'transparent',
                border: 0,
                cursor: loggingOut ? 'wait' : 'pointer',
              }}
            >
              {loggingOut
                ? '退出中……'
                : `${currentUser.displayName || currentUser.username} · 退出`}
            </button>
          ) : status === 'loading' ? (
            <span className={styles.loginLink}>检查登录状态…</span>
          ) : (
            <NavLink to="/login" className={styles.loginLink}>
              登录
            </NavLink>
          )}
        </header>

        <main className={styles.main}>
          <Outlet />
        </main>

        <div className={styles.mobileNav}>
          <Navigation />
        </div>
      </div>
    </div>
  )
}
