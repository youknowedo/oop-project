<script lang="ts">
	import { Button } from '#lib/components/ui/button/index.js';
	import type { CurrentUser } from '#lib/current-user.js';

	let { user }: { user: CurrentUser | null } = $props();
</script>

{#if user}
	<span class="text-sm font-medium">{user.name}</span>
{:else}
	<Button href="/login" size="sm">Sign in</Button>
{/if}
