import AnimateIn from './AnimateIn';
import { useInView } from '../hooks/useInView';
import { useCountUp } from '../hooks/useCountUp';

function StatCard({ label, value, suffix = '', isText = false, delay = 0 }) {
  const [ref, inView] = useInView({ threshold: 0.3 });
  const count = useCountUp(isText ? 0 : value, inView && !isText);

  return (
    <AnimateIn as="article" className="stat-card" delay={delay}>
      <div ref={ref} className="stat-card__value">
        {isText ? (
          <span className={`stat-card__text ${inView ? 'stat-card__text--visible' : ''}`}>
            {value}
          </span>
        ) : (
          <>
            {count}
            {suffix}
          </>
        )}
      </div>
      {label ? <p className="stat-card__label">{label}</p> : null}
    </AnimateIn>
  );
}

export default function About() {
  return (
    <section className="section about" id="about">
      <div className="container">
        <AnimateIn>
          <p className="section-label">About</p>
          <h2 className="section-title">
            Popularizing Programming.
            <br />
            One Contest at a Time.
          </h2>
        </AnimateIn>

        <div className="about__stats">
          <StatCard label="Contest Held" value={1} delay={100} />
          <StatCard label="" value="December 2025" isText delay={200} />
          <StatCard label="Core Members" value={5} delay={300} />
        </div>

        <AnimateIn className="about__copy" delay={150}>
          <p>
            BigO is a student-led initiative bringing competitive programming culture to life
            through LeetCode contests, hackathons, and real-world challenges.
          </p>
        </AnimateIn>
      </div>
    </section>
  );
}
