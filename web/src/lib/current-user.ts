/**
 * PLACEHOLDER: the only place the shell learns who is signed in.
 *
 * Real auth (Gamma login, #30) and the web auth state (#33) are not in yet, so this
 * always reports "signed out". When #33 lands, replace the body of `getCurrentUser`
 * (or this whole module) with the real auth state; the header only depends on the
 * `CurrentUser` shape and this function.
 *
 * To preview the signed-in header locally, return `{ id: 'dev', name: 'Dev User' }`.
 */
export type CurrentUser = {
	id: string;
	name: string;
};

export function getCurrentUser(): CurrentUser | null {
	return null;
}
