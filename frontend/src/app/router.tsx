import { Navigate, createBrowserRouter } from 'react-router-dom'
import { AppLayout } from '../layouts/AppLayout'
import { ActivityDetailPage } from '../pages/ActivityDetailPage'
import { ActivitiesPage } from '../pages/ActivitiesPage'
import { NotFoundPage } from '../pages/NotFoundPage'
import { PlaceholderPage } from '../pages/PlaceholderPage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <AppLayout />,
    children: [
      { index: true, element: <Navigate to="/activities" replace /> },
      { path: 'activities', element: <ActivitiesPage /> },
      {
        path: 'activities/create',
        element: (
          <PlaceholderPage
            eyebrow="CREATE"
            title="发布活动"
            description="活动表单、地点和时间选择将在后续业务开发中完成。"
          />
        ),
      },
      { path: 'activities/:activityId', element: <ActivityDetailPage /> },
      {
        path: 'login',
        element: (
          <PlaceholderPage
            eyebrow="ACCOUNT"
            title="登录 SideBy"
            description="注册登录和身份认证尚未接入。"
          />
        ),
      },
      {
        path: 'me',
        element: (
          <PlaceholderPage
            eyebrow="PROFILE"
            title="个人中心"
            description="个人资料与我参加的活动将在后续实现。"
          />
        ),
      },
      {
        path: 'notifications',
        element: (
          <PlaceholderPage
            eyebrow="INBOX"
            title="消息通知"
            description="通知中心尚未接入，这里不会展示伪造消息。"
          />
        ),
      },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
])
