<script lang="ts">
	import './layout.css';
	import favicon from '#lib/assets/favicon.svg';
	import PageContainer from '#lib/components/layout/page-container.svelte';
	import SiteFooter from '#lib/components/layout/site-footer.svelte';
	import SiteHeader from '#lib/components/layout/site-header.svelte';
	import type { LayoutProps } from './$types';

	let { children }: LayoutProps = $props();
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
</svelte:head>

<div class="flex min-h-svh flex-col">
	<SiteHeader />
	<main class="flex-1 py-8">
		<PageContainer>
			{@render children()}
		</PageContainer>
	</main>
	<SiteFooter />
</div>
