import { demoEvents } from './data/events.ts';
import type { StudentEvent } from '#lib/features/events/types.ts';

// Replace this boundary with calls to the Spring Boot API when it is available.
// No server state is mutated or shared between visitors.
export async function listEvents(): Promise<StudentEvent[]> {
	return demoEvents.toSorted((a, b) => Date.parse(a.startsAt) - Date.parse(b.startsAt));
}

export async function findEvent(slug: string): Promise<StudentEvent | undefined> {
	return demoEvents.find((event) => event.slug === slug);
}
