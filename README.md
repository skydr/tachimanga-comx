# tachimanga-comx

Custom build of the Com-X extension (com-x.life) for Tachimanga.

Based on upstream `keiyoushi/extensions-source` (v1.6.49+) with stability fixes for image
loading timeouts:

- Explicit client timeouts: connect 30s, read 60s (upstream relies on app defaults).
- Rate limit applies to the site host only; image hosts (`img.com-x.life`, `rus.com-x.life`)
  are no longer throttled by the 3 req/s limiter, which previously queued parallel page
  downloads into timeouts.
- DLE Guard bypass (`DleGuardResolver`): a failed WebView solve no longer disables the bypass
  for the whole session; it retries after a 60s cooldown.
- Soft retry on HTTP 429/503: up to 2 retries, honoring the `Retry-After` header
  (clamped to 1-30s), otherwise 2s then 5s.

## How to add this repository in Tachimanga

Add the repo by URL, the same way as the reference keiyoushi repo
(`https://github.com/keiyoushi/extensions/blob/repo/index.pb`) — the URL must point to
`index.pb` on the `repo` branch:

```
https://github.com/skydr/tachimanga-comx/blob/repo/index.pb
```

Steps: Browse → Settings (gear icon) → Extension Repositories → Add → paste the URL above →
confirm the security prompt → install / update **Com-X** in the Browse tab.

If the app reports a signature conflict with a previously installed Com-X from another
repository, remove that extension first.

## Releases

Both `tachiyomi-ru.comx-*.apk` and `tachiyomi-ru.comx-*.jar` are attached to each release.
The extension is signed with a dedicated keystore; the SHA-256 fingerprint is published in
`repo.json` and in `index.pb`.

## Source code

Patched sources are under `src/` (package `eu.kanade.tachiyomi.extension.ru.comx`).
To rebuild, copy these files over `src/ru/comx/` in a checkout of
`keiyoushi/extensions-source`, place `signingkey.jks` in the repo root and build with:

```bash
export KEY_STORE_PASSWORD=<store password>
export ALIAS=<alias>
export KEY_PASSWORD=<key password>
./gradlew :src:ru:comx:assembleRelease
```

The build outputs a signed APK and a signed extension JAR (both are required for Tachimanga).
