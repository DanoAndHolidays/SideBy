import { NavLink, Outlet } from 'react-router-dom'
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
        <p className={styles.sidebarNote}>在陌生城市，也能找到同行的人。</p>
      </aside>

      <div className={styles.contentFrame}>
        <header className={styles.mobileHeader}>
          <NavLink to="/activities" className={styles.mobileBrand}>
            <span className={styles.brandMark}>S</span>
            <strong>SideBy</strong>
          </NavLink>
          <NavLink to="/login" className={styles.loginLink}>
            登录
          </NavLink>
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
