import { error, fail } from '@sveltejs/kit';
import { findEvent } from '#lib/server/events.ts';
import { validateRegistration } from '#lib/features/registration/validation.ts';
import type { Actions } from './$types';

export const actions = {
	default: async ({ params, request }) => {
		const event = await findEvent(params.slug);
		if (!event) error(404, 'Eventet kunde inte hittas.');

		const { values, errors, valid } = validateRegistration(
			await request.formData(),
			event.includesFood
		);
		if (event.registrationStatus !== 'open') {
			return fail(409, {
				success: false,
				values,
				errors,
				message: 'Det går inte att anmäla sig till detta event.'
			});
		}
		if (!valid) {
			return fail(400, {
				success: false,
				values,
				errors,
				message: 'Kontrollera de markerade fälten och försök igen.'
			});
		}

		// Prototype only: validate without saving, reserving a seat or sending email.
		// The future Spring Boot registration endpoint belongs here.
		return { success: true, values: undefined, errors: {}, message: 'Testanmälan klar!' };
	}
} satisfies Actions;
