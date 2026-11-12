<script lang="ts">
	import { page } from '$app/state';
	import { buttonVariants } from '#lib/components/ui/button/index.js';
	import { cn } from '#lib/utils.js';
	import { isActive, navItems } from '#lib/navigation.js';
</script>

<nav aria-label="Main" class="flex items-center gap-1">
	{#each navItems as item (item.href)}
		{@const active = isActive(item, page.url.pathname)}
		<a
			href={item.href}
			aria-current={active ? 'page' : undefined}
			class={cn(
				buttonVariants({ variant: active ? 'secondary' : 'ghost', size: 'sm' }),
				!active && 'text-muted-foreground'
			)}
		>
			{item.label}
		</a>
	{/each}
</nav>
