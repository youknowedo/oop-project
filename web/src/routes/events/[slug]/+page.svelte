<script lang="ts">
	import { resolve } from '$app/paths';
	import Icon from '#lib/components/ui/Icon.svelte';
	import EventFacts from '#lib/features/events/components/EventFacts.svelte';
	import { formatDate, formatTime, registrationLabels } from '#lib/features/events/format.ts';
	import type { PageProps } from './$types';
	let { data }: PageProps = $props();
	let event = $derived(data.event);
</script>

<svelte:head
	><title>{event.title} — IT event</title><meta
		name="description"
		content={event.summary}
	/></svelte:head
>

<div class="container page-container">
	<a class="text-link back-link" href={`${resolve('/')}#events`}
		><Icon name="arrow-left" size={17} />Alla event</a
	>
	<div class="detail-grid">
		<article>
			<div class="event-labels">
				<span class="pill">{event.category}</span><span class="example-label">Exempel-event</span>
			</div>
			<h1 class="page-title">{event.title}</h1>
			<p class="page-intro">{event.summary}</p>
			<div class="event-description">
				<h2>Det här händer</h2>
				{#each event.description as paragraph}<p>{paragraph}</p>{/each}
			</div>
			<dl class="extra-facts">
				<div>
					<dt>Arrangör</dt>
					<dd>{event.organizer}</dd>
				</div>
				<div>
					<dt>Klädsel</dt>
					<dd>{event.dressCode}</dd>
				</div>
				{#if event.includesFood}<div>
						<dt>Mat</dt>
						<dd>Ange specialkost i anmälningsformuläret.</dd>
					</div>{/if}
			</dl>
		</article>
		<aside class="panel signup-panel" aria-label="Eventinformation och anmälan">
			<p class="eyebrow">Bra att veta</p>
			<EventFacts {event} />
			<div class="registration-status">
				<span class="status-dot" class:unavailable={event.registrationStatus !== 'open'}
				></span>{registrationLabels[
					event.registrationStatus
				]}{#if event.registrationStatus === 'open'}<span class="spots"
						>{event.spotsLeft} platser kvar</span
					>{/if}
			</div>
			{#if event.registrationStatus === 'open'}
				<a
					class="button signup-button"
					href={resolve('/events/[slug]/anmalan', { slug: event.slug })}
					>Anmäl dig <Icon name="arrow" size={18} /></a
				>
			{:else}<button class="button signup-button" disabled
					>{registrationLabels[event.registrationStatus]}</button
				>{/if}
			<p class="deadline">
				Sista anmälan: {formatDate(event.registrationDeadline)} kl. {formatTime(
					event.registrationDeadline
				)}.
			</p>
			<p class="prototype-note">Du testar en prototyp. Ingen riktig plats bokas.</p>
		</aside>
	</div>
</div>

<style>
	.detail-grid {
		display: grid;
		grid-template-columns: minmax(0, 1fr) 360px;
		gap: 72px;
		align-items: start;
	}
	.event-labels {
		display: flex;
		align-items: center;
		gap: 14px;
		margin-bottom: 22px;
	}
	.example-label {
		font-size: 12px;
		color: var(--muted);
	}
	.event-description {
		margin-top: 40px;
	}
	h2 {
		font-size: 21px;
		letter-spacing: -0.5px;
		margin-bottom: 18px;
	}
	.event-description p {
		color: var(--muted);
		margin-block: 14px;
		font-size: 15px;
	}
	.extra-facts {
		margin: 28px 0 0;
		padding-top: 22px;
		border-top: 1px solid var(--border);
		display: grid;
		gap: 16px;
		font-size: 14px;
	}
	.extra-facts div {
		display: grid;
		grid-template-columns: 90px 1fr;
		gap: 20px;
	}
	.extra-facts dt {
		color: var(--muted);
	}
	.extra-facts dd {
		margin: 0;
	}
	.signup-panel {
		border-top: 3px solid var(--brand);
	}
	.signup-panel > .eyebrow {
		margin-bottom: 14px;
	}
	.registration-status {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		gap: 7px;
		margin-top: 23px;
		font-size: 12px;
	}
	.status-dot {
		width: 6px;
		height: 6px;
		background: var(--brand-dark);
		border-radius: 50%;
	}
	.status-dot.unavailable {
		background: var(--muted);
	}
	.spots {
		margin-left: auto;
		color: var(--muted);
	}
	.signup-button {
		width: 100%;
		margin-top: 18px;
	}
	.deadline {
		font-size: 11px;
		color: var(--muted);
		margin-top: 14px;
	}
	.prototype-note {
		margin-top: 20px;
		padding-top: 16px;
		border-top: 1px dashed var(--border);
		font-size: 12px;
		color: var(--muted);
	}
	@media (max-width: 900px) {
		.detail-grid {
			gap: 32px;
			grid-template-columns: minmax(0, 1fr) 330px;
		}
	}
	@media (max-width: 720px) {
		.detail-grid {
			grid-template-columns: 1fr;
		}
	}
</style>
