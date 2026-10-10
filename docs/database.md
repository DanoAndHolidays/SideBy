# Database conventions

## Core tables

- `app_user`: identity and profile fields. `password_hash` is reserved for a future password-login flow; no plaintext password or seeded real account is included.
- `activity`: publisher, content, location, schedule, capacity, participant count, and lifecycle status.
- `activity_participant`: one durable relationship row per user and activity.

The schema uses MySQL 8.4, InnoDB, and `utf8mb4`. Primary keys are database-generated `BIGINT AUTO_INCREMENT` values, matching MyBatis-Plus `id-type: auto`.

## Identity rules

Both username and normalized email are globally unique. Registration must trim and normalize them before insertion. Email comparison behavior must be made explicit when registration is implemented; the current case-insensitive collation already prevents common case-only duplicates.

Java stores IDs as `Long`. Public DTOs/VOs must serialize IDs as JSON strings, and the frontend must type them as `string`, because JavaScript cannot precisely represent every 64-bit integer.

## Participation rules

`activity_participant.status` is one of:

- `REGISTERED`: currently occupies a place.
- `WITHDRAWN`: previously joined and later withdrew; does not occupy a place.

`UNIQUE(activity_id, user_id)` keeps one durable relationship row. A later rejoin should update that row back to `REGISTERED` and refresh `registered_at`, rather than insert a duplicate. The exact audit policy can be expanded later if a separate history table becomes necessary.

The publisher is not included in `participant_count` and must not join their own activity. `participant_count` counts only participant rows currently in `REGISTERED` state.

Joining and withdrawing must be transactional. Joining should combine:

1. validation that the activity is published and registration is still open;
2. validation that the requester is not the publisher;
3. the unique relationship constraint;
4. a conditional count update such as `participant_count < capacity`;
5. insertion or state transition of the participant row in the same transaction.

The service-layer checks provide understandable errors; database constraints and conditional updates provide correctness under concurrent requests.

## Time rules

MySQL `DATETIME(6)` fields contain UTC wall-clock values. The JDBC connection forces the session time zone to UTC. Backend APIs should expose instants as ISO 8601 strings with an explicit offset, normally `Z`, for example `2026-10-10T08:00:00Z`. The frontend converts those instants to the user's local time zone for display.

Do not store an unexplained local time in these fields. If a future feature needs the activity's named IANA zone for editing or daylight-saving behavior, add a separate zone column such as `Asia/Shanghai`.

