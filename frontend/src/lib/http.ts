export interface ApiResponse<T> {
  code: string
  message: string
  data: T
}

interface ErrorResponse {
  code?: string
  message?: string
}

export class HttpError extends Error {
  readonly status: number
  readonly code?: string

  constructor(status: number, message: string, code?: string) {
    super(message)
    this.name = 'HttpError'
    this.status = status
    this.code = code
  }
}

export async function request<T>(
  path: string,
  init: RequestInit = {},
): Promise<ApiResponse<T>> {
  const headers = new Headers(init.headers)
  headers.set('Accept', 'application/json')

  const response = await fetch(path, {
    ...init,
    credentials: init.credentials ?? 'include',
    headers,
  })

  const body = (await response.json().catch(() => null)) as
    ApiResponse<T> | ErrorResponse | null

  if (!response.ok) {
    const error = body as ErrorResponse | null
    throw new HttpError(
      response.status,
      error?.message ?? `Request failed with status ${response.status}`,
      error?.code,
    )
  }

  if (!body) {
    throw new HttpError(response.status, 'Server returned an empty response')
  }

  return body as ApiResponse<T>
}
