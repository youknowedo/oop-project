<script lang="ts">
	import { resolve } from '$app/paths';
	import Icon from '#lib/components/ui/Icon.svelte';
	import { formatDay, formatMonth, formatTime, formatPrice, registrationLabels } from '../format';
	import type { StudentEvent } from '../types';
	let { event }: { event: StudentEvent } = $props();
</script>

<a class="event-card" href={resolve('/events/[slug]', { slug: event.slug })}>
	<div class="card-top">
		<time class="date-stamp" datetime={event.startsAt}
			><strong>{formatDay(event.startsAt)}</strong><span>{formatMonth(event.startsAt)}</span></time
		>
		<span class="category">{event.category}</span>
	</div>
	<h3>{event.title}</h3>
	<p class="summary">{event.summary}</p>
	<div class="event-meta">
		<span><Icon name="clock" size={15} />{formatTime(event.startsAt)}</span><span
			><Icon name="pin" size={15} />{event.location}</span
		>
	</div>
	<div class="card-bottom">
		<div class="availability" class:unavailable={event.registrationStatus !== 'open'}>
			<span class="status-dot"></span>
			{event.registrationStatus === 'open'
				? `${event.spotsLeft} platser kvar`
				: registrationLabels[event.registrationStatus]}
		</div>
		<span class="price">{formatPrice(event.price)} <Icon name="arrow-up-right" size={18} /></span>
	</div>
</a>

<style>
	.event-card {
		display: flex;
		flex-direction: column;
		padding: 24px;
		background: var(--surface);
		border: 1px solid var(--border);
		border-radius: var(--radius);
		text-decoration: none;
		transition:
			border-color 150ms,
			transform 150ms;
	}
	.event-card:hover {
		border-color: var(--ink);
		transform: translateY(-3px);
	}
	.card-top {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		gap: 16px;
		margin-bottom: 26px;
	}
	.date-stamp {
		width: 58px;
		padding: 9px 4px 7px;
		display: grid;
		gap: 1px;
		background: var(--brand-soft);
		border: 1px solid var(--brand-border);
		border-radius: 5px;
		text-align: center;
	}
	.date-stamp strong {
		font-size: 26px;
		line-height: 1;
		letter-spacing: -1px;
		font-weight: 650;
	}
	.date-stamp span {
		font-size: 11px;
		text-transform: uppercase;
		letter-spacing: 0.08em;
	}
	.category {
		color: var(--muted);
		font-size: 12px;
		border: 1px solid var(--border);
		border-radius: 4px;
		padding: 4px 8px;
	}
	h3 {
		font-size: 21px;
		line-height: 1.3;
		letter-spacing: -0.6px;
		margin-bottom: 10px;
	}
	.summary {
		color: var(--muted);
		font-size: 14px;
		line-height: 1.7;
		margin-bottom: 20px;
	}
	.event-meta {
		display: flex;
		flex-wrap: wrap;
		gap: 16px;
		margin-top: auto;
		color: var(--muted);
		font-size: 12px;
	}
	.event-meta span,
	.price {
		display: inline-flex;
		align-items: center;
		gap: 6px;
	}
	.card-bottom {
		display: flex;
		justify-content: space-between;
		gap: 8px;
		align-items: center;
		padding-top: 16px;
		margin-top: 20px;
		border-top: 1px solid var(--border);
		font-size: 12px;
	}
	.availability {
		display: flex;
		align-items: center;
		gap: 6px;
		color: var(--brand-dark);
	}
	.status-dot {
		width: 6px;
		height: 6px;
		border-radius: 50%;
		background: currentColor;
		flex-shrink: 0;
	}
	.unavailable {
		color: var(--muted);
	}
	.price {
		font-weight: 650;
		white-space: nowrap;
		gap: 12px;
	}
</style>
