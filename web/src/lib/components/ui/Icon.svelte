<script lang="ts">
	type IconName =
		| 'arrow'
		| 'arrow-left'
		| 'arrow-up-right'
		| 'calendar'
		| 'pin'
		| 'clock'
		| 'search'
		| 'check'
		| 'ticket'
		| 'close';
	let { name, size = 20 }: { name: IconName; size?: number } = $props();
	const paths: Record<IconName, string> = {
		arrow: 'M4 12h16m-6-6 6 6-6 6',
		'arrow-left': 'M20 12H4m6-6-6 6 6 6',
		'arrow-up-right': 'M6 18 18 6M6 6h12v12',
		calendar:
			'M8 2v4m8-4v4M3 10h18M5 4h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2Z',
		pin: 'M20 10c0 6-8 12-8 12S4 16 4 10a8 8 0 1 1 16 0ZM15 10a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z',
		clock: 'M12 8v5l3 2M22 12a10 10 0 1 1-20 0 10 10 0 0 1 20 0Z',
		search: 'm21 21-5-5M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0Z',
		check: 'm5 12 4 4L19 6',
		ticket: 'M3 7h18v4a2 2 0 0 0 0 4v4H3v-4a2 2 0 0 0 0-4V7Zm13 0v3m0 4v5',
		close: 'm6 6 12 12M6 18 18 6'
	};
</script>

<svg
	width={size}
	height={size}
	viewBox="0 0 24 24"
	fill="none"
	stroke="currentColor"
	stroke-width="1.7"
	stroke-linecap="round"
	stroke-linejoin="round"
	aria-hidden="true"
>
	<path d={paths[name]} />
</svg>
