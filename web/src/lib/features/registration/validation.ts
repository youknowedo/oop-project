export const studyYears = ['IT 1', 'IT 2', 'IT 3', 'Master', 'Annat'] as const;

export interface RegistrationValues {
	name: string;
	email: string;
	studyYear: string;
	dietaryRequirements: string;
}

export type RegistrationErrors = Partial<Record<keyof RegistrationValues, string>>;

export function validateRegistration(formData: FormData, includesFood: boolean) {
	const text = (key: keyof RegistrationValues) => {
		const value = formData.get(key);
		return typeof value === 'string' ? value.trim() : '';
	};
	const values: RegistrationValues = {
		name: text('name'),
		email: text('email'),
		studyYear: text('studyYear'),
		dietaryRequirements: includesFood ? text('dietaryRequirements') : ''
	};
	const errors: RegistrationErrors = {};

	if (values.name.length < 2 || values.name.length > 100) {
		errors.name = 'Ange ditt namn, mellan 2 och 100 tecken.';
	}
	if (values.email.length > 254 || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email)) {
		errors.email = 'Ange en giltig e-postadress.';
	}
	if (values.studyYear && !studyYears.some((year) => year === values.studyYear)) {
		errors.studyYear = 'Välj ett av alternativen i listan.';
	}
	if (values.dietaryRequirements.length > 500) {
		errors.dietaryRequirements = 'Beskriv specialkost med högst 500 tecken.';
	}

	return { values, errors, valid: Object.keys(errors).length === 0 };
}
