<script lang="ts">
	import { enhance } from '$app/forms';
	import { resolve } from '$app/paths';
	import Icon from '#lib/components/ui/Icon.svelte';
	import EventFacts from '#lib/features/events/components/EventFacts.svelte';
	import { registrationLabels } from '#lib/features/events/format.ts';
	import { studyYears, type RegistrationErrors } from '#lib/features/registration/validation.ts';
	import type { PageProps } from './$types';
	let { data, form }: PageProps = $props();
	let event = $derived(data.event);
	let errors: RegistrationErrors = $derived(form?.errors ?? {});
	let submitting = $state(false);
</script>

<svelte:head
	><title>Anmälan till {event.title} — IT event</title><meta
		name="description"
		content={`Testa att anmäla dig till ${event.title}. Det här är en prototyp och ingen riktig plats bokas.`}
	/></svelte:head
>

<div class="container page-container">
	<a class="text-link back-link" href={resolve('/events/[slug]', { slug: event.slug })}
		><Icon name="arrow-left" size={17} />Tillbaka till eventet</a
	>
	{#if form?.success}
		<section class="panel confirmation" aria-labelledby="confirmation-heading">
			<span class="confirmation-icon"><Icon name="check" size={32} /></span>
			<p class="eyebrow">Flödet är testat</p>
			<h1 id="confirmation-heading" class="page-title" tabindex="-1">Testanmälan klar!</h1>
			<p>Du har testat anmälan till <strong>{event.title}</strong>.</p>
			<p class="confirmation-note">
				Ingen plats har bokats, inga uppgifter har sparats och inget bekräftelsemejl skickas i
				prototypen.
			</p>
			<a class="button" href={`${resolve('/')}#events`}
				>Upptäck fler event <Icon name="arrow" size={18} /></a
			>
		</section>
	{:else if event.registrationStatus !== 'open'}
		<section class="panel unavailable-panel">
			<p class="eyebrow">{event.title}</p>
			<h1 class="page-title">{registrationLabels[event.registrationStatus]}</h1>
			<p>Det går inte att anmäla sig till det här eventet. Kika gärna på vad mer som händer.</p>
			<a class="button" href={`${resolve('/')}#events`}
				>Se andra event <Icon name="arrow" size={18} /></a
			>
		</section>
	{:else}
		<div class="registration-grid">
			<section aria-labelledby="registration-heading">
				<p class="eyebrow">Anmälan · {event.category}</p>
				<h1 id="registration-heading" class="page-title">Klart du ska med.</h1>
				<p class="page-intro">Fyll i dina uppgifter för <strong>{event.title}</strong>.</p>
				<div class="notice demo-notice">
					Det här är en testanmälan. Använd påhittade uppgifter. Ingenting sparas och ingen plats
					bokas.
				</div>
				<form
					method="POST"
					use:enhance={() => {
						submitting = true;
						return async ({ update }) => {
							try {
								await update();
							} finally {
								submitting = false;
							}
							if (form?.success) document.getElementById('confirmation-heading')?.focus();
							else document.getElementById('form-errors')?.focus();
						};
					}}
					aria-busy={submitting}
				>
					{#if form?.message}<div id="form-errors" class="form-errors" role="alert" tabindex="-1">
							{form.message}
						</div>{/if}
					<p class="required-note">Fält markerade med * är obligatoriska.</p>
					<div class="field">
						<label for="name">Namn *</label><input
							id="name"
							name="name"
							autocomplete="name"
							placeholder="För- och efternamn"
							required
							minlength="2"
							maxlength="100"
							value={form?.values?.name ?? ''}
							aria-invalid={errors.name ? 'true' : undefined}
							aria-describedby={errors.name ? 'name-error' : undefined}
						/>{#if errors.name}<p class="field-error" id="name-error">{errors.name}</p>{/if}
					</div>
					<div class="field">
						<label for="email">E-post *</label><input
							id="email"
							name="email"
							type="email"
							autocomplete="email"
							placeholder="namn@example.com"
							required
							maxlength="254"
							value={form?.values?.email ?? ''}
							aria-invalid={errors.email ? 'true' : undefined}
							aria-describedby={errors.email ? 'email-error' : undefined}
						/>{#if errors.email}<p class="field-error" id="email-error">{errors.email}</p>{/if}
					</div>
					<div class="field">
						<label for="studyYear">Årskurs <span>(valfritt)</span></label><select
							id="studyYear"
							name="studyYear"
							value={form?.values?.studyYear ?? ''}
							aria-invalid={errors.studyYear ? 'true' : undefined}
							aria-describedby={errors.studyYear ? 'studyYear-error' : undefined}
							><option value="">Välj årskurs</option>{#each studyYears as year}<option value={year}
									>{year}</option
								>{/each}</select
						>{#if errors.studyYear}<p class="field-error" id="studyYear-error">
								{errors.studyYear}
							</p>{/if}
					</div>
					{#if event.includesFood}<div class="field">
							<label for="dietaryRequirements"
								>Allergier och specialkost <span>(valfritt)</span></label
							><textarea
								id="dietaryRequirements"
								name="dietaryRequirements"
								rows="3"
								maxlength="500"
								placeholder="Till exempel vegetarisk kost eller glutenfritt"
								value={form?.values?.dietaryRequirements ?? ''}
								aria-invalid={errors.dietaryRequirements ? 'true' : undefined}
								aria-describedby={errors.dietaryRequirements ? 'dietary-error' : undefined}
							></textarea>{#if errors.dietaryRequirements}<p class="field-error" id="dietary-error">
									{errors.dietaryRequirements}
								</p>{/if}
						</div>{/if}
					<div class="form-actions">
						<button class="button" type="submit" disabled={submitting}
							>{submitting ? 'Kontrollerar…' : 'Testa anmälan'}<Icon
								name="arrow"
								size={18}
							/></button
						><a class="text-link" href={resolve('/events/[slug]', { slug: event.slug })}>Avbryt</a>
					</div>
				</form>
			</section>
			<aside class="panel event-summary" aria-label="Ditt valda event">
				<p class="eyebrow">Du anmäler dig till</p>
				<h2>{event.title}</h2>
				<EventFacts {event} />
				<p class="summary-note">{event.spotsLeft} platser kvar i exempel-eventet.</p>
			</aside>
		</div>
	{/if}
</div>

<style>
	.registration-grid {
		display: grid;
		grid-template-columns: minmax(0, 1fr) 350px;
		gap: 80px;
		align-items: start;
	}
	.registration-grid section > .eyebrow {
		margin-bottom: 18px;
		color: var(--brand-dark);
	}
	.page-intro {
		font-size: 15px;
	}
	.page-intro strong {
		font-weight: 550;
	}
	.demo-notice {
		margin-block: 24px 28px;
	}
	.required-note {
		color: var(--muted);
		font-size: 12px;
		margin-bottom: 20px;
	}
	.field {
		display: grid;
		gap: 8px;
		margin-bottom: 21px;
	}
	label {
		font-size: 14px;
		font-weight: 550;
	}
	label span {
		color: var(--muted);
		font-weight: 400;
	}
	input,
	select,
	textarea {
		width: 100%;
		min-height: 46px;
		background: var(--surface);
		border: 1px solid #bcc9c5;
		border-radius: 5px;
		padding: 12px 14px;
		font-size: 14px;
	}
	textarea {
		resize: vertical;
		line-height: 1.6;
	}
	[aria-invalid='true'] {
		border-color: var(--danger);
	}
	.field-error {
		font-size: 12px;
		color: var(--danger);
	}
	.form-errors {
		margin-bottom: 20px;
		padding: 14px;
		border: 1px solid #e5b8bc;
		border-radius: 5px;
		background: #fff5f5;
		color: var(--danger);
		font-size: 14px;
	}
	.form-actions {
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		gap: 24px;
		margin-top: 30px;
	}
	.event-summary {
		border-top: 3px solid var(--brand);
	}
	.event-summary h2 {
		font-size: 23px;
		line-height: 1.25;
		letter-spacing: -0.7px;
		margin-block: 16px 20px;
	}
	.summary-note {
		color: var(--muted);
		font-size: 12px;
		border-top: 1px dashed var(--border);
		padding-top: 18px;
		margin-top: 8px;
	}
	.confirmation {
		max-width: 620px;
		margin: 16px auto 0;
		text-align: center;
		padding-block: 45px;
	}
	.confirmation-icon {
		width: 66px;
		height: 66px;
		margin: 0 auto 24px;
		border: 1px solid var(--brand-dark);
		background: var(--brand-soft);
		display: grid;
		place-items: center;
		border-radius: 50%;
		color: var(--brand-dark);
	}
	.confirmation .eyebrow {
		color: var(--brand-dark);
		margin-bottom: 14px;
	}
	.confirmation h1 {
		font-size: 38px;
		margin-bottom: 22px;
	}
	.confirmation-note {
		color: var(--muted);
		margin: 16px auto 26px;
		max-width: 410px;
		font-size: 14px;
	}
	.unavailable-panel {
		max-width: 620px;
		margin-inline: auto;
	}
	.unavailable-panel h1 {
		margin-block: 16px 20px;
	}
	.unavailable-panel .button {
		margin-top: 24px;
	}
	@media (max-width: 900px) {
		.registration-grid {
			gap: 32px;
			grid-template-columns: minmax(0, 1fr) 310px;
		}
	}
	@media (max-width: 720px) {
		.registration-grid {
			grid-template-columns: 1fr;
		}
		.event-summary {
			order: -1;
		}
	}
</style>
