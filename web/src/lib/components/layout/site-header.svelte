<script lang="ts">
	import { getCurrentUser } from '#lib/current-user.js';
	import MainNav from './main-nav.svelte';
	import PageContainer from './page-container.svelte';
	import UserMenu from './user-menu.svelte';

	const user = $derived(getCurrentUser());
</script>

<header class="border-b">
	<PageContainer class="flex h-14 items-center gap-6">
		<a href="/" class="font-semibold tracking-tight">Commit</a>
		<MainNav />
		<div class="ml-auto">
			<UserMenu {user} />
		</div>
	</PageContainer>
</header>
