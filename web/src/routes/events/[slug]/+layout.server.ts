import { error } from '@sveltejs/kit';
import { findEvent } from '#lib/server/events.ts';
import type { LayoutServerLoad } from './$types';

export const load = (async ({ params }) => {
	const event = await findEvent(params.slug);
	if (!event) error(404, 'Eventet kunde inte hittas.');
	return { event };
}) satisfies LayoutServerLoad;
