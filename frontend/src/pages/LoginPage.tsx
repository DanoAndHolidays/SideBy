import { useState, type FormEvent } from 'react'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../features/auth/authStore'
import styles from './LoginPage.module.css'

interface LoginLocationState {
  returnTo?: string
}

function readSafeReturnTo(state: unknown) {
  const returnTo = (state as LoginLocationState | null)?.returnTo

  if (!returnTo || !returnTo.startsWith('/') || returnTo.startsWith('//')) {
    return '/activities'
  }

  return returnTo
}

export function LoginPage() {
  const location = useLocation()
  const navigate = useNavigate()
  const { status, error, login } = useAuth()
  const [identifier, setIdentifier] = useState('')
  const [password, setPassword] = useState('')
  const [formError, setFormError] = useState<string | null>(null)
  const [submitting, setSubmitting] = useState(false)

  const returnTo = readSafeReturnTo(location.state)

  if (status === 'authenticated') {
    return <Navigate to={returnTo} replace />
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setFormError(null)

    if (!identifier.trim() || !password) {
      setFormError('请输入用户名或邮箱，以及密码。')
      return
    }

    setSubmitting(true)

    try {
      await login({ identifier: identifier.trim(), password })
      navigate(returnTo, { replace: true })
    } catch (submitError) {
      setFormError(
        submitError instanceof Error
          ? submitError.message
          : '登录失败，请稍后重试。',
      )
    } finally {
      setSubmitting(false)
    }
  }

  const displayError = formError ?? (error ? error.message : null)

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <p className={styles.eyebrow}>ACCOUNT</p>
        <h1>登录 SideBy</h1>
        <p className={styles.description}>
          登录后才能发布活动、查看个人中心和使用其他需要身份的功能。
        </p>

        <form className={styles.form} onSubmit={handleSubmit}>
          <label className={styles.field}>
            <span>用户名或邮箱</span>
            <input
              autoComplete="username"
              value={identifier}
              onChange={(event) => setIdentifier(event.target.value)}
              placeholder="例如 alice 或 alice@example.com"
            />
          </label>

          <label className={styles.field}>
            <span>密码</span>
            <input
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="请输入注册时的密码"
            />
          </label>

          {displayError && (
            <p className={styles.error} role="alert">
              {displayError}
            </p>
          )}

          <button
            className={styles.submit}
            type="submit"
            disabled={submitting || status === 'loading'}
          >
            {submitting ? '登录中……' : '登录'}
          </button>
        </form>

        <p className={styles.hint}>
          还没有账号？注册接口属于前面的注册阶段；完成注册后回到这里登录。
        </p>
        <Link className={styles.backLink} to="/activities">
          暂时回到活动广场
        </Link>
      </div>
    </div>
  )
}
