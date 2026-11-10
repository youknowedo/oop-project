<script lang="ts">
	import { resolve } from '$app/paths';
	import Icon from '#lib/components/ui/Icon.svelte';
</script>

<header class="site-header">
	<div class="container header-inner">
		<a class="brand" href={resolve('/')} aria-label="IT event — till startsidan">
			<span class="brand-mark">it<span>_</span></span>
			<span>event<span class="brand-dot">.</span></span>
		</a>
		<nav aria-label="Huvudmeny">
			<a class="nav-link" href={`${resolve('/')}#events`}>Alla event</a>
			<a class="nav-link how-link" href={`${resolve('/')}#sa-funkar-det`}>Så funkar det</a>
			<a class="section-link" href="https://chalmers.it/"
				>IT-sektionen <Icon name="arrow-up-right" size={16} /></a
			>
		</nav>
	</div>
</header>

<style>
	.site-header {
		border-bottom: 1px solid var(--border);
		background: var(--surface);
	}
	.header-inner {
		min-height: 84px;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 24px;
	}
	.brand {
		display: inline-flex;
		align-items: center;
		gap: 11px;
		font-size: 26px;
		font-weight: 800;
		letter-spacing: -1px;
		text-decoration: none;
	}
	.brand-mark {
		width: 42px;
		height: 42px;
		display: grid;
		place-content: center;
		grid-auto-flow: column;
		background: var(--brand);
		border: 1px solid var(--ink);
		border-radius: 7px;
		font-size: 25px;
	}
	.brand-mark span {
		font-weight: 400;
	}
	.brand-dot {
		color: var(--brand-dark);
	}
	nav {
		display: flex;
		align-items: center;
		gap: 28px;
		font-size: 14px;
		font-weight: 550;
	}
	nav a {
		text-decoration: none;
	}
	nav a:hover {
		text-decoration: underline;
		text-underline-offset: 5px;
	}
	.section-link {
		display: flex;
		align-items: center;
		gap: 8px;
		border-left: 1px solid var(--border);
		padding: 10px 0 10px 24px;
	}
	@media (max-width: 640px) {
		.header-inner {
			min-height: 72px;
			gap: 16px;
		}
		nav {
			gap: 16px;
			font-size: 13px;
		}
		.how-link {
			display: none;
		}
		.section-link {
			padding-left: 16px;
		}
		.brand {
			font-size: 22px;
			gap: 8px;
		}
		.brand-mark {
			width: 35px;
			height: 35px;
			font-size: 22px;
		}
	}
	@media (max-width: 380px) {
		.section-link {
			display: none;
		}
	}
</style>
