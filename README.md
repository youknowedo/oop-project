# IT event

En första, enkel eventwebb för IT-sektionen på Chalmers. Frontend ligger i `web/` och den befintliga Spring Boot-servern i `server/`.

**För att se och testa skissen behöver du bara starta frontend.** Databas, Java och server behövs inte för det.

## Vad fungerar nu?

- Responsiv startsida med exempel-event, sökning och kategorifilter.
- Klickbara event med datum, tid, plats, pris och anmälningsstatus.
- Separat anmälningsformulär med validering i webbläsaren och på SvelteKit-servern.
- Bekräftelse för en testanmälan, samt lägen för fullbokade/stängda event och okända adresser.
- Svenska texter och IT-sektionens turkosa färg `#09CDDA`, enligt [stadgan §14.2](https://docs.chalmers.it/stadga.pdf).

Alla event, arrangörer och platser i listan är **exempeldata**. Formuläret kontrollerar uppgifter men sparar dem inte, bokar ingen plats och skickar inget mejl. Använd påhittade uppgifter. Gamma-inloggning, betalning, kö och koppling till Spring Boot är ännu inte implementerade.

## Verktyg och versioner

| Verktyg | Version | Behövs för |
| --- | --- | --- |
| Git | Installerat | Hämta och versionshantera projektet |
| Node.js | **24.x** | Frontend; låst till huvudversionen i `web/.nvmrc` och `engines` |
| pnpm | **12.18.0** | Frontend; låst i `web/package.json` |
| JDK | **17** | Endast Java-servern |
| PostgreSQL | Lokal installation eller Docker | Endast Java-servern |

Frontend använder SvelteKit 3.1, Svelte 5, TypeScript 6 och Vite 8. Exakta beroendeversioner finns i `web/pnpm-lock.yaml`. Använd pnpm och checka in ändringar i låsfilen tillsammans med `package.json`.

Projektet använder Node 24 även om SvelteKit 3:s minimikrav är Node 22.17. Node 20 fungerar inte. Läs mer i [SvelteKits migrationsguide](https://svelte.dev/docs/kit/migrating-to-sveltekit-3#Updated-dependencies).

## Linux

Installera Git och [nvm](https://github.com/nvm-sh/nvm#installing-and-updating) om de saknas. Öppna sedan en terminal **i projektmappen `oop-project/`**, där denna README ligger:

```bash
cd web
nvm install
nvm use
npm install --global pnpm@12.18.0
pnpm install --frozen-lockfile
pnpm dev
```

`nvm install` och `nvm use` läser `web/.nvmrc` och väljer Node 24. Har du redan Node 24 och rätt pnpm-version kan du börja med `pnpm install --frozen-lockfile`.

Öppna **http://localhost:5173**. Stoppa utvecklingsservern med `Ctrl+C`.

## macOS

Installera Git om det saknas; Apples kommandoradsverktyg kan installeras med `xcode-select --install`. Installera [nvm](https://github.com/nvm-sh/nvm#installing-and-updating) enligt dess instruktioner och öppna en ny terminal.

Kör från **projektmappen `oop-project/`**:

```bash
cd web
nvm install
nvm use
npm install --global pnpm@12.18.0
pnpm install --frozen-lockfile
pnpm dev
```

Detta fungerar med både Apple Silicon och Intel. Öppna **http://localhost:5173**. Stoppa med `Ctrl+C`.

## Windows (PowerShell)

Installera [Git för Windows](https://git-scm.com/downloads/win) och [Node.js](https://nodejs.org/en/download). **Välj Node 24**, även om webbplatsen föreslår en senare huvudversion. Öppna därefter en ny PowerShell-terminal så att PATH uppdateras.

Kör från **projektmappen `oop-project/`**:

```powershell
node --version
cd web
npm.cmd install --global pnpm@12.18.0
pnpm.cmd install --frozen-lockfile
pnpm.cmd dev
```

`node --version` ska börja med `v24.`. Öppna **http://localhost:5173**. Stoppa med `Ctrl+C`.

Vi använder `.cmd` i PowerShell för att undvika fel som `npm.ps1 cannot be loaded` eller `running scripts is disabled`. Du behöver inte ändra datorns execution policy. I Kommandotolken eller Git Bash kan vanliga `npm` och `pnpm` användas.

Om du använder [nvm-windows](https://github.com/coreybutler/nvm-windows) i stället för Node-installationsprogrammet: installera och välj en Node 24-version manuellt. nvm-windows läser inte `.nvmrc` på samma sätt som nvm på Linux/macOS.

## Alternativ: kör pnpm via Corepack

Om Node 24-installationen har Corepack kan du använda den utan att installera pnpm globalt eller aktivera shims. Kör i `web/`, på valfritt operativsystem:

```text
corepack pnpm install --frozen-lockfile
corepack pnpm dev
```

Corepack läser den låsta pnpm-versionen från `package.json`. Första körningen hämtar pnpm och kräver internet. Alla `pnpm`-kommandon nedan kan också köras som `corepack pnpm ...`.

## Vanliga frontend-kommandon

Kör i `web/`. Använd `pnpm.cmd` i PowerShell om skript blockeras.

| Kommando | Gör |
| --- | --- |
| `pnpm dev` | Startar utvecklingsservern med automatisk omladdning |
| `pnpm check` | Kontrollerar TypeScript, Svelte och tillgänglighetsdiagnostik |
| `pnpm check:watch` | Kör typkontrollen kontinuerligt |
| `pnpm build` | Bygger frontend för produktion |
| `pnpm preview` | Förhandsvisar en färdig build, normalt på port 4173 |
| `pnpm format` | Formaterar frontend med Prettier |
| `pnpm format:check` | Kontrollerar formatering utan att ändra filer |

Efter en ändring:

```bash
pnpm check
pnpm build
```

Inga nya tester är tillagda. `check` är en typ- och Sveltekontroll, inte en testsuite. Serverns befintliga tester ligger kvar i `server/src/test/`.

## Starta Java-servern (valfritt för skissen)

Installera JDK 17, exempelvis [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=17), och kontrollera `java -version`. Sätt `JAVA_HOME` till JDK-installationen om Maven inte hittar Java. Maven behöver inte installeras separat: projektet innehåller Maven Wrapper.

Servern använder PostgreSQL. Om du redan har PostgreSQL, skapa databasen `oop` och använd miljövariablerna nedan för dina anslutningsuppgifter. Alternativt kan du starta en lokal utvecklingsdatabas med Docker. Följande enradskommando fungerar på Linux, macOS och Windows:

```text
docker run --name it-events-db -e POSTGRES_DB=oop -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 127.0.0.1:5432:5432 -v it-events-postgres:/var/lib/postgresql/data -d postgres:17
```

Kör `docker run` bara första gången. Vid senare tillfällen: `docker start it-events-db`. Stoppa med `docker stop it-events-db`. Uppgifterna ovan är för lokal utveckling.

Öppna en **andra terminal i `oop-project/`**.

**Linux och macOS:**

```bash
cd server
chmod +x mvnw
./mvnw spring-boot:run
```

**Windows PowerShell:**

```powershell
cd server
.\mvnw.cmd spring-boot:run
```

Servern använder normalt **http://localhost:8080**. Den innehåller ännu inget event-API som frontend anropar. Spring Security är aktiverat, så ett svar som kräver inloggning är förväntat för skyddade adresser. Serverns start och databasanslutning behöver verifieras i din lokala databasinstallation.

### Databasinställningar

Standardvärdena kommer från `server/src/main/resources/application.properties`:

| Miljövariabel | Standardvärde |
| --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/oop` |
| `DB_USER` | `postgres` |
| `DB_PASSWORD` | `postgres` |

För andra uppgifter, sätt variablerna i samma terminal innan du startar servern. Exempel på Linux/macOS:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/oop'
export DB_USER='din_anvandare'
export DB_PASSWORD='ditt_lokala_losenord'
```

Motsvarande i PowerShell:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/oop'
$env:DB_USER = 'din_anvandare'
$env:DB_PASSWORD = 'ditt_lokala_losenord'
```

En `.env`-fil läses inte automatiskt av den befintliga Java-konfigurationen. Frontend behöver inga miljövariabler i prototypen.

## Kodstruktur

```text
oop-project/
├── README.md
├── ARCHITECTURE.md                 # Övergripande projektidé och backendplan
├── server/                         # Befintlig Java/Spring Boot-applikation
└── web/
    ├── .nvmrc                     # Node 24
    ├── package.json               # Skript, beroenden och pnpm-version
    ├── pnpm-lock.yaml
    ├── vite.config.ts             # SvelteKit 3-konfiguration och adapter-auto
    └── src/
        ├── app.css                # Färgtokens, reset och gemensamma stilar
        ├── lib/
        │   ├── components/
        │   │   ├── layout/        # Sidhuvud och sidfot
        │   │   └── ui/            # Generella UI-delar, t.ex. ikoner
        │   ├── features/
        │   │   ├── events/        # Eventtyper, datumformat och eventkomponenter
        │   │   └── registration/  # Formulärmodell och validering
        │   └── server/
        │       ├── events.ts      # Gräns för framtida API-anrop
        │       └── data/events.ts # Tydligt avskild exempeldata
        └── routes/
            ├── +layout.svelte     # Gemensamt sidramverk
            ├── +page.server.ts    # Hämtar startsidans eventdata
            ├── +page.svelte       # Startsida
            ├── +error.svelte      # 404 och andra felsidor
            └── events/[slug]/
                ├── +layout.server.ts  # Hämtar event; 404 om det saknas
                ├── +page.svelte       # Eventdetaljer
                └── anmalan/
                    ├── +page.svelte    # Formulär och bekräftelse
                    └── +page.server.ts # Validerar testanmälan
```

Sidorna ansvarar för routing och sammansättning. Återanvändbara komponenter och domänlogik ligger i `lib/`. Data hämtas via `lib/server/events.ts`, så att exempeldata senare kan ersättas med API-anrop utan att eventkorten byggs om. Inget muterbart register över användare eller anmälningar hålls i SvelteKit-processen.

SvelteKit 3 använder `#lib`-importer och konfiguration i `vite.config.ts`. Ange filändelse för `#lib`-importer. Svelte 5-komponenterna använder runes (`$props`, `$state`, `$derived`). Datum visas alltid i `Europe/Stockholm` för att server och klient ska visa samma tid.

## Testa flödet manuellt

1. Öppna startsidan och prova sökning och kategorifilter.
2. Klicka på **En helt vanlig IT-sittning** och därefter **Anmäl dig**.
3. Fyll i exempelvis `Test Student` och `test@example.com`, välj eventuell specialkost och tryck **Testa anmälan**.
4. Kontrollera bekräftelsen. Gå tillbaka och prova tomma/ogiltiga fält.
5. Prova ett fullbokat eller stängt event och en okänd eventadress.
6. Kontrollera sidorna med smalt fönster och tangentbord.

Formulärpostning fungerar även utan JavaScript. Sökning och kategorifilter kräver JavaScript.

## Deployment och nästa steg

`adapter-auto` används tills driftsmiljön är bestämd. Välj en konkret adapter innan deployment, exempelvis `adapter-node` för en Node-server. Ett meddelande om att ingen produktionsmiljö kunde identifieras vid lokal build är väntat med `adapter-auto`.

Anmälningsformuläret använder SvelteKit form actions och behöver en serverruntime; en helt statisk export räcker inte för nuvarande implementation. Ingen publicering eller produktionskonfiguration är gjord. Prototypen har `noindex` i sidornas metadata; ändra det när en riktig tjänst ska bli sökbar.

Nästa funktionella steg är att koppla `lib/server/events.ts` och formulärets action till Spring Boot, införa Gamma-inloggning samt spara anmälningar och hantera kapacitet på backend. [ARCHITECTURE.md](ARCHITECTURE.md) beskriver den större projektplanen.

## Felsökning

| Problem | Lösning |
| --- | --- |
| `pnpm` hittas inte | Öppna en ny terminal efter installationen, eller använd `corepack pnpm` i `web/`. |
| Fel Node-version | Kör `node --version`; välj Node 24. På Linux/macOS: `nvm use` i `web/`. |
| PowerShell blockerar `.ps1` | Använd `npm.cmd` och `pnpm.cmd` enligt Windows-instruktionerna. |
| Port 5173 upptagen | Använd adressen Vite skriver ut, eller kör `pnpm dev --port 5174`. |
| `./$types` saknas i editorn | Kör `pnpm install` eller `pnpm check`, som genererar SvelteKits typer. |
| Låsfilen matchar inte `package.json` | Efter en avsiktlig beroendeändring: kör `pnpm install` och checka in båda filerna. |
| Maven hittar inte Java | Kontrollera `java -version` och `JAVA_HOME`; använd JDK 17. |
| Servern kan inte ansluta till databasen | Kontrollera att PostgreSQL körs, att `oop` finns och att `DB_*` stämmer. |
| Port 5432 upptagen | Använd din befintliga PostgreSQL, eller välj annan värdport och ändra `DB_URL`. |
| `mvnw` saknar körbehörighet | Kör `chmod +x mvnw` i `server/` på Linux/macOS. |
| Nedladdning av paket misslyckas | Första installationen kräver internet till npm-registret och, för servern, Maven Central. |
