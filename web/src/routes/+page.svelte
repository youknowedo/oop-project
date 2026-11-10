<script lang="ts">
	import { resolve } from '$app/paths';
	import EventList from '#lib/features/events/components/EventList.svelte';
	import Icon from '#lib/components/ui/Icon.svelte';
	import type { PageProps } from './$types';
	let { data }: PageProps = $props();
</script>

<svelte:head>
	<title>IT event — Lite mer studentliv</title>
	<meta
		name="description"
		content="Hitta sittningar, häng och andra event för IT på Chalmers. En första prototyp för att upptäcka event och testa en anmälan."
	/>
</svelte:head>

<div class="container">
	<section class="hero" aria-labelledby="hero-heading">
		<div class="hero-copy">
			<p class="eyebrow"><span class="tiny-square"></span>IT-sektionen · Chalmers</p>
			<h1 id="hero-heading">Mindre FOMO.<br />Mer <span class="underlined">studentliv.</span></h1>
			<p class="hero-description">
				Sittningar, spontant häng och allt däremellan.<br class="desktop-break" /> Hitta ditt nästa event
				— vi ses där.
			</p>
			<a class="button" href="#events">Hitta ditt nästa event <Icon name="arrow" size={18} /></a>
			<p class="hero-note">För oss som vill få lite mer ut av tiden på IT.</p>
		</div>
		<div class="hero-sketch" aria-hidden="true">
			<svg
				class="sketch-spark"
				width="64"
				height="64"
				viewBox="0 0 64 64"
				fill="none"
				stroke="currentColor"
				stroke-width="2"
				><path
					d="m32 3 3 20 19-10-14 17 20 5-21 3 10 19-17-15-7 19-1-22-20 7 17-14L5 20l22 6Z"
				/></svg
			>
			<div class="ticket">
				<div class="ticket-main">
					<span class="eyebrow">Din biljett till</span><strong
						>livet efter<br />föreläsningen.</strong
					><span class="ticket-bottom"
						>GÖTEBORG, SVERIGE <Icon name="arrow-up-right" size={24} /></span
					>
				</div>
				<div class="ticket-stub">
					<span>PLATS FÖR DIG</span><span class="ticket-number">IT / 001</span>
				</div>
			</div>
			<div class="sketch-caption">
				<svg
					width="46"
					height="41"
					viewBox="0 0 46 41"
					fill="none"
					stroke="currentColor"
					stroke-width="1.5"><path d="M40 35C17 35 5 18 13 5m-8 5 8-5 5 8" /></svg
				><span>Ses i Hubben?</span>
			</div>
		</div>
	</section>

	<section class="events-section" id="events" aria-labelledby="events-heading">
		<div class="section-heading">
			<div>
				<p class="eyebrow">Skriv in något kul i kalendern</p>
				<h2 id="events-heading">På gång på IT<span>.</span></h2>
			</div>
			<span class="demo-label">Skiss · exempel-event</span>
		</div>
		<EventList events={data.events} />
		<p class="demo-note">
			Alla event är exempel. Du kan testa flödet, men ingen riktig plats bokas.
		</p>
	</section>

	<section class="how-section" id="sa-funkar-det" aria-labelledby="how-heading">
		<div class="how-intro">
			<p class="eyebrow">Från ”kanske” till ”vi ses”</p>
			<h2 id="how-heading">Enkelt att hänga på.</h2>
		</div>
		<ol>
			<li>
				<span class="step-number">01</span>
				<div>
					<h3>Hitta något kul</h3>
					<p>Bläddra bland event och välj det som passar dig.</p>
				</div>
			</li>
			<li>
				<span class="step-number">02</span>
				<div>
					<h3>Kolla läget</h3>
					<p>Läs om tid, plats, pris och vad som gäller.</p>
				</div>
			</li>
			<li>
				<span class="step-number">03</span>
				<div>
					<h3>Anmäl dig</h3>
					<p>Fyll i formuläret. I skissen testar du bara flödet.</p>
				</div>
			</li>
		</ol>
		<a class="text-link" href={`${resolve('/')}#events`}
			>Till eventen <Icon name="arrow" size={17} /></a
		>
	</section>
</div>

<style>
	.hero {
		display: grid;
		grid-template-columns: 1.2fr 1fr;
		gap: 48px;
		align-items: center;
		padding-block: 78px 74px;
	}
	.hero-copy > .eyebrow {
		display: flex;
		align-items: center;
		gap: 9px;
		margin-bottom: 24px;
	}
	.tiny-square {
		width: 7px;
		height: 7px;
		background: var(--brand);
		border: 1px solid var(--brand-dark);
	}
	h1 {
		font-size: clamp(42px, 5.5vw, 68px);
		font-weight: 700;
		line-height: 1.07;
		letter-spacing: -0.055em;
	}
	.underlined {
		position: relative;
		z-index: 0;
		white-space: nowrap;
	}
	.underlined::after {
		content: '';
		position: absolute;
		left: 0;
		right: -2px;
		bottom: 3px;
		height: 10px;
		background: var(--brand);
		z-index: -1;
		transform: rotate(-1deg);
		border-radius: 40% 60% 30% 60%;
	}
	.hero-description {
		margin-top: 25px;
		color: var(--muted);
		font-size: 16px;
	}
	.hero .button {
		margin-top: 28px;
	}
	.hero-note {
		font-size: 12px;
		color: var(--muted);
		margin-top: 16px;
	}
	.hero-sketch {
		position: relative;
		max-width: 390px;
		justify-self: center;
		width: 100%;
		padding: 24px 10px 48px;
	}
	.ticket {
		display: flex;
		background: var(--brand);
		border: 1.5px solid var(--ink);
		border-radius: 7px;
		transform: rotate(-5deg);
		box-shadow:
			7px 7px 0 var(--background),
			8px 8px 0 var(--ink);
	}
	.ticket-main {
		padding: 28px;
		flex: 1;
	}
	.ticket-main .eyebrow {
		font-size: 9px;
	}
	.ticket-main strong {
		display: block;
		margin-block: 28px 30px;
		font-size: 33px;
		letter-spacing: -0.04em;
		line-height: 1.15;
		font-weight: 650;
	}
	.ticket-bottom {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 10px;
		font-size: 8px;
		letter-spacing: 0.12em;
	}
	.ticket-stub {
		width: 55px;
		border-left: 1.5px dashed var(--ink);
		padding: 20px 14px;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-between;
		font-size: 9px;
		letter-spacing: 0.15em;
	}
	.ticket-stub span {
		writing-mode: vertical-rl;
	}
	.ticket-number {
		font-weight: 700;
	}
	.sketch-spark {
		position: absolute;
		right: -12px;
		top: -20px;
		transform: rotate(12deg);
	}
	.sketch-caption {
		position: absolute;
		bottom: -8px;
		right: 8px;
		display: flex;
		align-items: flex-end;
		gap: 6px;
		font:
			italic 19px Georgia,
			serif;
		transform: rotate(-5deg);
	}
	.events-section {
		padding-block: 34px 64px;
		border-top: 1px solid var(--border);
		scroll-margin-top: 8px;
	}
	.section-heading {
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
		gap: 20px;
		margin-bottom: 28px;
	}
	.section-heading .eyebrow {
		color: var(--muted);
		font-size: 10px;
		margin-bottom: 10px;
	}
	h2 {
		font-size: 32px;
		font-weight: 650;
		letter-spacing: -0.04em;
		line-height: 1.2;
	}
	.section-heading h2 span {
		color: var(--brand-dark);
	}
	.demo-label {
		color: var(--muted);
		font-size: 11px;
		padding: 6px 10px;
		border: 1px dashed var(--border);
		border-radius: 4px;
		white-space: nowrap;
	}
	.demo-note {
		font-size: 12px;
		color: var(--muted);
		margin-top: 22px;
	}
	.how-section {
		border-top: 1px solid var(--border);
		padding-block: 42px 56px;
	}
	.how-intro .eyebrow {
		color: var(--muted);
		font-size: 10px;
		margin-bottom: 12px;
	}
	.how-intro h2 {
		font-size: 26px;
	}
	ol {
		list-style: none;
		padding: 0;
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 32px;
		margin: 30px 0 26px;
	}
	li {
		display: flex;
		gap: 16px;
	}
	.step-number {
		font-size: 13px;
		color: var(--brand-dark);
		font-variant-numeric: tabular-nums;
		padding-top: 2px;
	}
	li h3 {
		font-size: 15px;
		font-weight: 600;
		margin-bottom: 6px;
	}
	li p {
		color: var(--muted);
		font-size: 13px;
	}
	@media (max-width: 900px) {
		.hero {
			gap: 30px;
			padding-block: 60px;
		}
		.ticket-main {
			padding: 22px;
		}
		.ticket-main strong {
			font-size: 27px;
		}
		.ticket-stub {
			width: 44px;
			padding-inline: 10px;
		}
		.hero-description {
			font-size: 15px;
		}
		.desktop-break {
			display: none;
		}
	}
	@media (max-width: 720px) {
		.hero {
			grid-template-columns: 1fr;
			gap: 24px;
			padding-top: 44px;
			padding-bottom: 38px;
		}
		.hero-sketch {
			max-width: 300px;
			padding-bottom: 36px;
			margin-block: 22px 30px;
		}
		.ticket-main strong {
			font-size: 28px;
			margin-block: 20px;
		}
		h1 {
			font-size: 52px;
		}
		ol {
			grid-template-columns: 1fr;
			gap: 22px;
		}
		.hero-description {
			max-width: 380px;
		}
	}
	@media (max-width: 440px) {
		.sketch-spark {
			right: 0;
		}
		h1 {
			font-size: 44px;
		}
		.section-heading {
			align-items: flex-start;
			flex-direction: column;
			gap: 14px;
		}
		h2 {
			font-size: 29px;
		}
	}
</style>
