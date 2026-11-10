import type { RegistrationStatus } from './types';

// Use the same timezone during SSR and in the browser.
const timeZone = 'Europe/Stockholm';
const dateFormatter = new Intl.DateTimeFormat('sv-SE', {
	day: 'numeric',
	month: 'long',
	year: 'numeric',
	timeZone
});
const dayFormatter = new Intl.DateTimeFormat('sv-SE', { day: '2-digit', timeZone });
const monthFormatter = new Intl.DateTimeFormat('sv-SE', { month: 'short', timeZone });
const timeFormatter = new Intl.DateTimeFormat('sv-SE', {
	hour: '2-digit',
	minute: '2-digit',
	timeZone
});
const priceFormatter = new Intl.NumberFormat('sv-SE', {
	style: 'currency',
	currency: 'SEK',
	maximumFractionDigits: 0
});

export const formatDate = (value: string) => dateFormatter.format(new Date(value));
export const formatDay = (value: string) => dayFormatter.format(new Date(value));
export const formatMonth = (value: string) =>
	monthFormatter.format(new Date(value)).replace('.', '');
export const formatTime = (value: string) => timeFormatter.format(new Date(value));
export const formatPrice = (price: number) =>
	price === 0 ? 'Gratis' : priceFormatter.format(price);

export const registrationLabels: Record<RegistrationStatus, string> = {
	open: 'Anmälan öppen',
	full: 'Fullbokat',
	closed: 'Anmälan stängd'
};
