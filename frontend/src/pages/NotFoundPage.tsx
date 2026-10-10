import { Link } from 'react-router-dom'
import styles from './Pages.module.css'

export function NotFoundPage() {
  return (
    <div className={styles.placeholder}>
      <p className={styles.eyebrow}>404</p>
      <h1>这里暂时没有活动</h1>
      <p>你访问的页面不存在，或者已经被移动。</p>
      <Link to="/activities" className={styles.primaryLink}>
        回到活动广场
      </Link>
    </div>
  )
}
