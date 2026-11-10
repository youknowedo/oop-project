# IT event — frontend

SvelteKit 3.1, Svelte 5, TypeScript 6 och Vite 8. Använd Node 24 och pnpm 12.18.0.

Fullständiga instruktioner för **Linux, Windows och macOS**, samt valfri serverstart, finns i [projektets README](../README.md).

Kör i den här mappen:

```bash
pnpm install --frozen-lockfile
pnpm dev
```

Öppna http://localhost:5173. På Windows kan du använda `pnpm.cmd`; om pnpm inte är globalt installerat kan du använda `corepack pnpm`.

```bash
pnpm check
pnpm build
```

Formatering: `pnpm format`. Kontrollera formatering: `pnpm format:check`.

Flöde: `/` → `/events/[slug]` → `/events/[slug]/anmalan`.

Detta är en prototyp med exempeldata och validerad testanmälan. Inget sparas och frontend är ännu inte kopplat till Java-servern. `adapter-auto` behålls tills deploymål är bestämt. Inga tester är tillagda.
