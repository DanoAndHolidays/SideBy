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
  const response = await fetch(path, {
    ...init,
    headers: {
      Accept: 'application/json',
      ...init.headers,
    },
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
