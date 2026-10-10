# SideBy API

API responses use the following envelope:

```json
{
  "code": "OK",
  "message": "success",
  "data": {}
}
```

HTTP status codes remain meaningful. An error response does not use HTTP 200 merely to fit the envelope.

## Available

### `GET /api/system/status`

Runs a lightweight `SELECT 1` against MySQL. A successful response is HTTP 200:

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "application": "SideBy",
    "status": "UP",
    "database": "UP"
  }
}
```

If the database probe fails after the application has started, the endpoint returns HTTP 503 without exposing SQL, credentials, or a stack trace:

```json
{
  "code": "DATABASE_UNAVAILABLE",
  "message": "Database is currently unavailable",
  "data": null
}
```

## Planned — not implemented

- Registration, login, refresh, and logout endpoints
- Activity creation, listing, detail, update, and cancellation endpoints
- Activity join and withdrawal endpoints
- Comment endpoints
- Notification endpoints
- File upload endpoints

Do not build clients against these planned groups until their request and response contracts are documented here.

