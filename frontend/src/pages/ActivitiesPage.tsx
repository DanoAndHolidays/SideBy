import { SystemStatusCard } from '../components/SystemStatusCard'
import styles from './Pages.module.css'

export function ActivitiesPage() {
  return (
    <div className={styles.page}>
      <header className={styles.hero}>
        <p className={styles.eyebrow}>EXPLORE TOGETHER</p>
        <h1>
          找点想做的事，
          <span>认识同频的人。</span>
        </h1>
        <p>
          SideBy
          为刚到一座城市的人连接真实、轻松的线下活动。工程骨架已就绪，活动内容等你来实现。
        </p>
      </header>

      <SystemStatusCard />

      <section
        className={styles.emptyState}
        aria-labelledby="activity-list-title"
      >
        <div className={styles.emptyIcon} aria-hidden="true">
          ✦
        </div>
        <div>
          <p className={styles.eyebrow}>ACTIVITIES</p>
          <h2 id="activity-list-title">活动广场还是一张白纸</h2>
          <p>活动查询接口尚未实现。下一步可以从发布活动和活动列表开始。</p>
        </div>
      </section>
    </div>
  )
}
