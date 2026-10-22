import js from '@eslint/js'
import prettier from 'eslint-config-prettier'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import globals from 'globals'
import tseslint from 'typescript-eslint'

export default tseslint.config(
  { ignores: ['dist'] },
  js.configs.recommended,
  ...tseslint.configs.recommended,
  reactHooks.configs.flat.recommended,
  reactRefresh.configs.vite,
  prettier,
  {
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2023,
      globals: globals.browser,
    },
  },
  {
    // authStore 按惯例同时导出 Provider 组件和 useAuth hook，
    // 这不会破坏 Fast Refresh（组件与 hook 都在本文件中定义）。
    files: ['src/features/auth/authStore.tsx'],
    rules: {
      'react-refresh/only-export-components': [
        'warn',
        { allowExportNames: ['useAuth'] },
      ],
    },
  },
)
