<script lang="ts">
	import '../app.css';
	import favicon from '#lib/assets/favicon.svg';
	import SiteHeader from '#lib/components/layout/SiteHeader.svelte';
	import SiteFooter from '#lib/components/layout/SiteFooter.svelte';
	import type { LayoutProps } from './$types';

	let { children }: LayoutProps = $props();
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
	<meta name="theme-color" content="#09cdda" />
	<meta name="robots" content="noindex, nofollow" />
</svelte:head>

<div class="app-shell">
	<a class="skip-link" href="#main-content">Hoppa till innehållet</a>
	<SiteHeader />
	<main id="main-content" tabindex="-1">{@render children()}</main>
	<SiteFooter />
</div>
