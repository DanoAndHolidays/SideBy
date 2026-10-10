import { Link, useParams } from 'react-router-dom'
import styles from './Pages.module.css'

export function ActivityDetailPage() {
  const { activityId } = useParams()

  return (
    <div className={styles.placeholder}>
      <p className={styles.eyebrow}>ACTIVITY · {activityId}</p>
      <h1>活动详情</h1>
      <p>详情查询与报名能力尚未实现。</p>
      <Link to="/activities" className={styles.primaryLink}>
        返回活动广场
      </Link>
    </div>
  )
}
