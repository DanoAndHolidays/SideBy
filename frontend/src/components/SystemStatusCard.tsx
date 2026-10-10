import { useQuery } from '@tanstack/react-query'
import { getSystemStatus } from '../lib/systemApi'
import styles from './SystemStatusCard.module.css'

export function SystemStatusCard() {
  const statusQuery = useQuery({
    queryKey: ['system-status'],
    queryFn: ({ signal }) => getSystemStatus(signal),
    retry: 1,
  })

  return (
    <section className={styles.card} aria-labelledby="system-status-title">
      <div>
        <p className={styles.eyebrow}>LIVE CONNECTION</p>
        <h2 id="system-status-title">服务连接状态</h2>
        <p className={styles.description}>
          这不是演示数据，它会经过 Vite 代理、Spring Boot，再实际查询 MySQL。
        </p>
      </div>

      {statusQuery.isPending && (
        <div className={styles.state} role="status">
          <span className={styles.spinner} aria-hidden="true" />
          正在检查后端与数据库…
        </div>
      )}

      {statusQuery.isError && (
        <div className={`${styles.state} ${styles.error}`} role="alert">
          <span>
            <strong>连接失败</strong>
            <small>{statusQuery.error.message}</small>
          </span>
          <button type="button" onClick={() => statusQuery.refetch()}>
            重新检查
          </button>
        </div>
      )}

      {statusQuery.isSuccess && (
        <div className={`${styles.state} ${styles.success}`} role="status">
          <span className={styles.pulse} aria-hidden="true" />
          <span>
            <strong>{statusQuery.data.application} 服务正常</strong>
            <small>
              API {statusQuery.data.status} · Database{' '}
              {statusQuery.data.database}
            </small>
          </span>
        </div>
      )}
    </section>
  )
}
