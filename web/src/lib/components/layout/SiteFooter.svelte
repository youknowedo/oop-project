<script lang="ts">
	import Icon from '#lib/components/ui/Icon.svelte';
</script>

<footer>
	<div class="container footer-inner">
		<div>
			<strong>it event.</strong>
			<p>Lite mer liv mellan föreläsningarna.</p>
		</div>
		<div class="footer-meta">
			<span>En prototyp för IT på Chalmers</span><a href="https://chalmers.it/"
				>Till sektionens hemsida <Icon name="arrow-up-right" size={16} /></a
			>
		</div>
	</div>
</footer>

<style>
	footer {
		margin-top: auto;
		border-top: 1px solid var(--border);
	}
	.footer-inner {
		display: flex;
		justify-content: space-between;
		gap: 24px;
		padding-block: 32px;
		font-size: 13px;
	}
	strong {
		font-size: 19px;
		letter-spacing: -0.7px;
	}
	p {
		color: var(--muted);
		margin: 7px 0 0;
	}
	.footer-meta {
		display: grid;
		gap: 8px;
		color: var(--muted);
		text-align: right;
	}
	a {
		display: inline-flex;
		align-items: center;
		justify-content: flex-end;
		gap: 5px;
		text-decoration: none;
		color: var(--ink);
	}
	a:hover {
		text-decoration: underline;
	}
	@media (max-width: 560px) {
		.footer-inner {
			flex-direction: column;
		}
		.footer-meta {
			text-align: left;
		}
		a {
			justify-content: flex-start;
		}
	}
</style>
