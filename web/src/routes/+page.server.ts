import { listEvents } from '#lib/server/events.ts';
import type { PageServerLoad } from './$types';

export const load = (async () => ({ events: await listEvents() })) satisfies PageServerLoad;
