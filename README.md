# tachimanga-comx

Custom build of the Com-X extension (com-x.life) for Tachimanga.

Based on upstream `keiyoushi/extensions-source` (v1.6.44, versionCode 44) with stability fixes
for image loading timeouts:

- Explicit client timeouts: connect 30s, read 60s (upstream relies on app defaults).
- Rate limit applies to the site host only; image hosts (`img.com-x.life`, `rus.com-x.life`)
  are no longer throttled by the 3 req/s limiter, which previously queued parallel page downloads
  into timeouts.
- DLE Guard bypass (`DleGuardResolver`): a failed WebView solve no longer disables the bypass for
  the whole session; it retries after a 60s cooldown.
- `BackoffInterceptor`: soft retry on HTTP 429/503 — up to 2 retries, honoring the `Retry-After`
  header (clamped to 1-30s), otherwise 2s then 5s.

## How to add this repository in Tachimanga

1. Browse → Settings (gear icon) → Extension Repositories → Add.
2. Paste the repository URL (must end with `index.pb` or `index.min.json`):
   `https://github.com/skydr/tachimanga-comx/raw/main/index.pb`
3. Confirm the security prompt.
4. Go back to Browse and install / update **Com-X**.

Requires removing a previously installed Com-X extension signed with a different key, if the app
reports a signature conflict.

## Source code

Patched sources are under `src/` (package `eu.kanade.tachiyomi.extension.ru.comx`).
Build with the `keiyoushi/extensions-source` toolchain by copying these files over
`src/ru/comx/`.
