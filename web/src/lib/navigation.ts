export type NavItem = {
	label: string;
	href: string;
};

/**
 * Primary navigation. Feature PRs add their entry here when they add the route,
 * so the header never links to a page that does not exist.
 */
export const navItems: NavItem[] = [{ label: 'Events', href: '/' }];

export function isActive(item: NavItem, pathname: string): boolean {
	if (item.href === '/') return pathname === '/' || pathname.startsWith('/events');
	return pathname === item.href || pathname.startsWith(`${item.href}/`);
}
