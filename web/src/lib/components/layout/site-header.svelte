<script lang="ts">
	import MainNav from './main-nav.svelte';
	import PageContainer from './page-container.svelte';
</script>

<header class="border-b">
	<PageContainer class="flex h-14 items-center gap-6">
		<a href="/" class="font-semibold tracking-tight">Commit</a>
		<MainNav />
	</PageContainer>
</header>
