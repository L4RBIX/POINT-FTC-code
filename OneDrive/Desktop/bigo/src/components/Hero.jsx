import { useMemo } from 'react';
import { useTypingEffect } from '../hooks/useTypingEffect';

const PHRASES = [
  'We make coders.',
  'We run contests.',
  'We are BigO.',
];

const SYMBOLS = ['{}', '[]', '()', 'O(n)', 'O(1)', 'O(log n)', '<>', '=>', ';;', '/**/'];

function FloatingSymbols() {
  const particles = useMemo(() => {
    return Array.from({ length: 28 }, (_, i) => ({
      id: i,
      symbol: SYMBOLS[i % SYMBOLS.length],
      left: `${(i * 17 + 7) % 100}%`,
      top: `${(i * 23 + 11) % 100}%`,
      size: 0.75 + (i % 5) * 0.15,
      duration: 18 + (i % 12) * 3,
      delay: -(i % 20) * 1.2,
    }));
  }, []);

  return (
    <div className="hero__particles" aria-hidden="true">
      {particles.map((p) => (
        <span
          key={p.id}
          className="hero__particle"
          style={{
            left: p.left,
            top: p.top,
            fontSize: `${p.size}rem`,
            animationDuration: `${p.duration}s`,
            animationDelay: `${p.delay}s`,
          }}
        >
          {p.symbol}
        </span>
      ))}
    </div>
  );
}

export default function Hero() {
  const typed = useTypingEffect(PHRASES);

  const scrollToAbout = () => {
    document.getElementById('about')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <section className="hero" id="hero">
      <FloatingSymbols />
      <div className="hero__inner">
        <a href="#hero" className="hero__logo" aria-label="BigO home">
          <img src="/logo.png" alt="BigO" width={160} height={48} />
        </a>

        <div className="hero__content">
          <p className="hero__label">Competitive Programming Community</p>
          <h1 className="hero__headline">
            <span className="hero__typed">{typed}</span>
            <span className="hero__cursor" aria-hidden="true" />
          </h1>
          <p className="hero__sub">
            Sport programming contests, hackathons, and a culture built for coders who compete.
          </p>
          <a href="#join" className="btn btn--outline hero__cta">
            Join the Community
          </a>
        </div>
      </div>

      <button
        type="button"
        className="hero__scroll"
        onClick={scrollToAbout}
        aria-label="Scroll to about section"
      >
        <span className="hero__scroll-arrow" />
      </button>
    </section>
  );
}
