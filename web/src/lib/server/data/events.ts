import type { StudentEvent } from '#lib/features/events/types.ts';

// Fictional fixtures for the prototype, not the section's real event calendar.
// Statuses are explicit so all UI states remain available when reviewing the sketch.
export const demoEvents: readonly StudentEvent[] = [
	{
		id: '1',
		slug: 'hostens-sittning',
		title: 'En helt vanlig IT-sittning',
		summary: 'Sånghäfte, god mat och ett bord fullt av nya och gamla vänner.',
		description: [
			'En kväll för att lägga plugget åt sidan och samlas runt långborden. Ta med en kompis eller kom själv — det finns alltid någon att sjunga med.',
			'I det här exempel-eventet ingår middag och alkoholfri dryck. Ange eventuell specialkost i anmälan. Dörrarna öppnar 30 minuter innan sittningen börjar.'
		],
		category: 'Sittning',
		startsAt: '2026-11-19T18:00:00+01:00',
		endsAt: '2026-11-19T22:00:00+01:00',
		location: 'Hubben 2.2',
		organizer: 'Sittningsgruppen (demo)',
		price: 120,
		registrationStatus: 'open',
		spotsLeft: 18,
		registrationDeadline: '2026-11-17T23:59:00+01:00',
		dressCode: 'Ovve eller något du trivs i',
		includesFood: true
	},
	{
		id: '2',
		slug: 'fredag-i-hubben',
		title: 'Fredag i Hubben',
		summary: 'Veckan är slut. Kom förbi på quiz, snacks och skönt häng.',
		description: [
			'Vi rundar av veckan tillsammans. Samla ett quizlag eller hitta ett på plats. Du behöver inga förkunskaper, bara lite fredagsenergi.',
			'Kvällen är ett exempel på hur ett kostnadsfritt event kan visas. Alkoholfritt finns för alla och quizet börjar klockan 18.30.'
		],
		category: 'Pub & häng',
		startsAt: '2026-11-20T17:00:00+01:00',
		endsAt: '2026-11-20T21:00:00+01:00',
		location: 'Hubben 2.2',
		organizer: 'Eventgruppen (demo)',
		price: 0,
		registrationStatus: 'open',
		spotsLeft: 32,
		registrationDeadline: '2026-11-20T12:00:00+01:00',
		dressCode: 'Kom som du är',
		includesFood: false
	},
	{
		id: '3',
		slug: 'plugg-och-kaffe',
		title: 'Plugg, kaffe & sällskap',
		summary: 'En eftermiddag för att knäcka uppgifter och fylla på kaffekoppen.',
		description: [
			'Ta med det du läser just nu och slå dig ner. Vi varvar eget arbete med pauser och hjälps åt när någon kör fast.',
			'Du väljer själv hur länge du stannar. Ta med dator, kursmaterial och din favoritmugg. Det här är ett exempel-event.'
		],
		category: 'Studieliv',
		startsAt: '2026-11-24T15:00:00+01:00',
		endsAt: '2026-11-24T18:00:00+01:00',
		location: 'Hubben 2.2',
		organizer: 'Studiegruppen (demo)',
		price: 0,
		registrationStatus: 'open',
		spotsLeft: 24,
		registrationDeadline: '2026-11-24T12:00:00+01:00',
		dressCode: 'Kom som du är',
		includesFood: false
	},
	{
		id: '4',
		slug: 'bradspelskvall',
		title: 'Bara en runda till',
		summary: 'Brädspel, kortspel och lagom mycket tävlingsinstinkt.',
		description: [
			'En spelkväll med plats för både nya spelare och vana strateger. Vi har spel på plats, men ta gärna med en egen favorit.',
			'Detta exempel visar ett fullbokat event. Någon köfunktion finns ännu inte i prototypen.'
		],
		category: 'Pub & häng',
		startsAt: '2026-11-27T18:00:00+01:00',
		endsAt: '2026-11-27T22:00:00+01:00',
		location: 'Hubben 2.2',
		organizer: 'Spelgruppen (demo)',
		price: 0,
		registrationStatus: 'full',
		spotsLeft: 0,
		registrationDeadline: '2026-11-26T18:00:00+01:00',
		dressCode: 'Kom som du är',
		includesFood: false
	},
	{
		id: '5',
		slug: 'frukost-pa-sektionen',
		title: 'En bättre start på dagen',
		summary: 'Frukost och en lugn stund tillsammans före första föreläsningen.',
		description: [
			'Börja dagen med frukost och sällskap. Vi dukar fram smörgåsar, kaffe och te innan det är dags att gå till föreläsningen.',
			'Detta exempel visar hur ett event ser ut när anmälan har stängt.'
		],
		category: 'Studieliv',
		startsAt: '2026-12-03T07:30:00+01:00',
		endsAt: '2026-12-03T09:00:00+01:00',
		location: 'Hubben 2.2',
		organizer: 'Eventgruppen (demo)',
		price: 25,
		registrationStatus: 'closed',
		spotsLeft: 8,
		registrationDeadline: '2026-11-09T18:00:00+01:00',
		dressCode: 'Kom som du är',
		includesFood: true
	},
	{
		id: '6',
		slug: 'vintersittning',
		title: 'En sista skål för terminen',
		summary: 'Vi sjunger ut läsperioden och firar in lite välförtjänt ledighet.',
		description: [
			'En vintrig sittning för att avrunda terminen. Vi samlas för middag, sång och historier från höstens alla äventyr.',
			'I det här exempel-eventet ingår middag och alkoholfri dryck. Meddela specialkost vid anmälan och kom gärna i något festligt.'
		],
		category: 'Sittning',
		startsAt: '2026-12-11T18:00:00+01:00',
		endsAt: '2026-12-11T23:00:00+01:00',
		location: 'Hubben 2.2',
		organizer: 'Sittningsgruppen (demo)',
		price: 150,
		registrationStatus: 'open',
		spotsLeft: 40,
		registrationDeadline: '2026-12-08T23:59:00+01:00',
		dressCode: 'Något festligt',
		includesFood: true
	}
];
