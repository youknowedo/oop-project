<script lang="ts">
	import { page } from '$app/state';
	import { resolve } from '$app/paths';
	import Icon from '#lib/components/ui/Icon.svelte';
</script>

<svelte:head><title>{page.status} — IT event</title></svelte:head>

<div class="container page-container">
	<section class="panel error-panel">
		<p class="eyebrow">{page.status}</p>
		<h1 class="page-title">{page.status === 404 ? 'Här var det tomt.' : 'Något gick fel.'}</h1>
		<p>
			{page.status === 404
				? 'Sidan eller eventet du letar efter kunde inte hittas.'
				: 'Försök igen om en stund.'}
		</p>
		<a class="button" href={resolve('/')}>Till startsidan <Icon name="arrow" size={18} /></a>
	</section>
</div>

<style>
	.error-panel {
		max-width: 600px;
		margin: 32px auto;
		text-align: center;
		padding-block: 48px;
	}
	.eyebrow {
		color: var(--brand-dark);
		margin-bottom: 18px;
	}
	h1 {
		margin-bottom: 20px;
	}
	p:not(.eyebrow) {
		color: var(--muted);
		margin-bottom: 28px;
	}
</style>
