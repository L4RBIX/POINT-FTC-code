import { useEffect, useState } from 'react';

const LINKS = [
  { label: 'About', href: '#about' },
  { label: 'Events', href: '#events' },
  { label: 'Team', href: '#team' },
  { label: 'Contact', href: '#join' },
  { label: 'Hackathon', href: '#/hackathon' },
];

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 40);
    window.addEventListener('scroll', onScroll, { passive: true });
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  useEffect(() => {
    document.body.style.overflow = open ? 'hidden' : '';
    return () => {
      document.body.style.overflow = '';
    };
  }, [open]);

  const close = () => setOpen(false);

  return (
    <header className={`nav ${scrolled ? 'nav--scrolled' : ''}`}>
      <div className="container nav__inner">
        <a href="#hero" className="nav__logo" onClick={close} aria-label="BigO home">
          <img src="/logo.png" alt="" width={100} height={30} />
        </a>

        <nav className={`nav__links ${open ? 'nav__links--open' : ''}`} aria-label="Main">
          {LINKS.map((link) => (
            <a key={link.href} href={link.href} onClick={close}>
              {link.label}
            </a>
          ))}
          <a href="#join" className="btn btn--outline btn--sm nav__cta" onClick={close}>
            Join
          </a>
        </nav>

        <button
          type="button"
          className={`nav__toggle ${open ? 'nav__toggle--open' : ''}`}
          aria-expanded={open}
          aria-label={open ? 'Close menu' : 'Open menu'}
          onClick={() => setOpen((v) => !v)}
        >
          <span />
          <span />
          <span />
        </button>
      </div>
    </header>
  );
}
