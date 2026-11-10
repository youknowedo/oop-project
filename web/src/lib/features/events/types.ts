export const eventCategories = ['Sittning', 'Pub & häng', 'Studieliv'] as const;

export type EventCategory = (typeof eventCategories)[number];
export type RegistrationStatus = 'open' | 'full' | 'closed';

export interface StudentEvent {
	id: string;
	slug: string;
	title: string;
	summary: string;
	description: string[];
	category: EventCategory;
	startsAt: string;
	endsAt: string;
	location: string;
	organizer: string;
	price: number;
	registrationStatus: RegistrationStatus;
	spotsLeft: number;
	registrationDeadline: string;
	dressCode: string;
	includesFood: boolean;
}
