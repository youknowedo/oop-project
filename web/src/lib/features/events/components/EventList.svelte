<script lang="ts">
	import Icon from '#lib/components/ui/Icon.svelte';
	import EventCard from './EventCard.svelte';
	import { eventCategories, type EventCategory, type StudentEvent } from '../types';
	let { events }: { events: StudentEvent[] } = $props();
	let category = $state<EventCategory | 'Alla'>('Alla');
	let search = $state('');
	const categories = ['Alla', ...eventCategories] as const;
	let filteredEvents = $derived(
		events.filter((event) => {
			const matchesCategory = category === 'Alla' || event.category === category;
			const query = search.trim().toLocaleLowerCase('sv-SE');
			const matchesSearch = `${event.title} ${event.summary} ${event.location} ${event.category}`
				.toLocaleLowerCase('sv-SE')
				.includes(query);
			return matchesCategory && matchesSearch;
		})
	);
</script>

<div class="filter-bar">
	<div class="filters" role="group" aria-label="Filtrera efter typ av event">
		{#each categories as item}<button
				class:active={category === item}
				aria-pressed={category === item}
				onclick={() => (category = item)}>{item}</button
			>{/each}
	</div>
	<div class="search-field">
		<Icon name="search" size={18} /><label class="sr-only" for="event-search">Sök event</label
		><input id="event-search" type="search" placeholder="Sök event…" bind:value={search} />
	</div>
</div>
<p class="result-count" aria-live="polite">
	Visar {filteredEvents.length} av {events.length} event
</p>
{#if filteredEvents.length}
	<div class="event-grid">
		{#each filteredEvents as event (event.id)}<EventCard {event} />{/each}
	</div>
{:else}
	<div class="empty-state">
		<Icon name="search" size={28} />
		<h3>Lite tomt här just nu.</h3>
		<p>Prova en annan sökning eller visa alla event.</p>
		<button
			class="button button-secondary"
			onclick={() => {
				category = 'Alla';
				search = '';
			}}>Rensa filter</button
		>
	</div>
{/if}

<style>
	.filter-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		gap: 20px;
	}
	.filters {
		display: flex;
		flex-wrap: wrap;
		gap: 6px;
	}
	.filters button {
		border: 1px solid transparent;
		border-radius: 5px;
		padding: 9px 15px;
		background: transparent;
		font-size: 13px;
		color: var(--muted);
		cursor: pointer;
	}
	.filters button:hover {
		color: var(--ink);
		background: var(--surface);
	}
	.filters button.active {
		color: var(--ink);
		background: var(--surface);
		border-color: var(--ink);
	}
	.search-field {
		display: flex;
		align-items: center;
		gap: 9px;
		padding: 0 13px;
		background: var(--surface);
		border: 1px solid var(--border);
		border-radius: 5px;
		color: var(--muted);
	}
	.search-field input {
		width: 175px;
		padding: 10px 0;
		background: transparent;
		border: 0;
		border-radius: 0;
		font-size: 13px;
	}
	.search-field:focus-within {
		outline: 2px solid var(--brand-dark);
		outline-offset: 2px;
	}
	.search-field input:focus-visible {
		outline: none;
	}
	.result-count {
		color: var(--muted);
		font-size: 12px;
		margin: 18px 0 16px;
	}
	.event-grid {
		display: grid;
		grid-template-columns: repeat(3, minmax(0, 1fr));
		gap: 20px;
	}
	.empty-state {
		display: grid;
		justify-items: center;
		text-align: center;
		padding: 56px 24px;
		border: 1px dashed var(--border);
		border-radius: var(--radius);
	}
	.empty-state h3 {
		margin: 12px 0 8px;
	}
	.empty-state p {
		color: var(--muted);
		margin-bottom: 20px;
	}
	@media (max-width: 980px) {
		.event-grid {
			grid-template-columns: repeat(2, minmax(0, 1fr));
		}
	}
	@media (max-width: 720px) {
		.filter-bar {
			align-items: stretch;
			flex-direction: column;
			gap: 14px;
		}
		.search-field input {
			width: 100%;
		}
	}
	@media (max-width: 560px) {
		.event-grid {
			grid-template-columns: 1fr;
		}
		.filters {
			gap: 3px;
		}
		.filters button {
			padding-inline: 12px;
		}
	}
</style>
