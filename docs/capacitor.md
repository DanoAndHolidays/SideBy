# Future Capacitor integration

Capacitor is intentionally not installed in this foundation. The web application should be completed and its authentication/session strategy settled first.

When native packaging becomes useful:

1. add `@capacitor/core` and the Capacitor CLI to `frontend`;
2. initialize Capacitor with the production web directory set to `dist`;
3. add Android and/or iOS platforms;
4. replace assumptions about `/api` with an environment-aware API origin for native builds;
5. review secure token storage, deep links, permissions, push notifications, and network security per platform;
6. keep browser behavior working instead of placing ordinary business logic in native-only code.

