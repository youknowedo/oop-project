<script lang="ts">
	import Icon from '#lib/components/ui/Icon.svelte';
	import { formatDate, formatTime, formatPrice } from '../format';
	import type { StudentEvent } from '../types';
	let { event }: { event: StudentEvent } = $props();
</script>

<dl class="event-facts">
	<div>
		<dt><Icon name="calendar" size={18} />Datum</dt>
		<dd><time datetime={event.startsAt}>{formatDate(event.startsAt)}</time></dd>
	</div>
	<div>
		<dt><Icon name="clock" size={18} />Tid</dt>
		<dd>{formatTime(event.startsAt)}–{formatTime(event.endsAt)}</dd>
	</div>
	<div>
		<dt><Icon name="pin" size={18} />Plats</dt>
		<dd>{event.location}</dd>
	</div>
	<div>
		<dt><Icon name="ticket" size={18} />Pris</dt>
		<dd>{formatPrice(event.price)}</dd>
	</div>
</dl>

<style>
	.event-facts {
		margin: 0;
	}
	.event-facts div {
		padding-block: 15px;
		display: grid;
		grid-template-columns: 88px 1fr;
		gap: 16px;
		border-bottom: 1px solid var(--border);
		font-size: 14px;
	}
	.event-facts div:last-child {
		border-bottom: 0;
	}
	dt {
		display: flex;
		align-items: center;
		gap: 10px;
		color: var(--muted);
	}
	dd {
		margin: 0;
		text-align: right;
		font-weight: 550;
	}
</style>
