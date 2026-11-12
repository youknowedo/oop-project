<script lang="ts">
	import type { HTMLAttributes } from 'svelte/elements';
	import { cn } from '#lib/utils.js';

	let { class: className, children, ...rest }: HTMLAttributes<HTMLDivElement> = $props();
</script>

<!-- Shared width and gutters so header, content and footer line up. -->
<div class={cn('mx-auto w-full max-w-5xl px-4 sm:px-6', className)} {...rest}>
	{@render children?.()}
</div>
