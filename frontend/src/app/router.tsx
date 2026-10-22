import { Navigate, createBrowserRouter } from 'react-router-dom'
import { ProtectedRoute } from '../features/auth/ProtectedRoute'
import { AppLayout } from '../layouts/AppLayout'
import { ActivityDetailPage } from '../pages/ActivityDetailPage'
import { ActivitiesPage } from '../pages/ActivitiesPage'
import { LoginPage } from '../pages/LoginPage'
import { NotFoundPage } from '../pages/NotFoundPage'
import { PlaceholderPage } from '../pages/PlaceholderPage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <AppLayout />,
    children: [
      { index: true, element: <Navigate to="/activities" replace /> },

      // 公开页面：不登录也可以访问
      { path: 'activities', element: <ActivitiesPage /> },
      { path: 'activities/:activityId', element: <ActivityDetailPage /> },
      { path: 'login', element: <LoginPage /> },

      // 受保护页面：先经过 ProtectedRoute，再渲染真正的页面
      {
        element: <ProtectedRoute />,
        children: [
          {
            path: 'activities/create',
            element: (
              <PlaceholderPage
                eyebrow="CREATE"
                title="发布活动"
                description="你已经通过登录检查。活动表单将在后续业务开发中完成。"
              />
            ),
          },
          {
            path: 'me',
            element: (
              <PlaceholderPage
                eyebrow="PROFILE"
                title="个人中心"
                description="你已经通过登录检查。个人资料和参加记录将在后续业务开发中完成。"
              />
            ),
          },
          {
            path: 'notifications',
            element: (
              <PlaceholderPage
                eyebrow="INBOX"
                title="消息通知"
                description="你已经通过登录检查。通知中心将在后续业务开发中完成。"
              />
            ),
          },
        ],
      },

      { path: '*', element: <NotFoundPage /> },
    ],
  },
])
