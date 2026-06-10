import AnimateIn from './AnimateIn';

export default function Events() {
  return (
    <section className="section events" id="events">
      <div className="container">
        <AnimateIn>
          <p className="section-label">Events</p>
          <h2 className="section-title">Our Events</h2>
        </AnimateIn>

        <div className="events__grid">
          <AnimateIn className="event-card" delay={100}>
            <div className="event-card__header">
              <span className="event-card__badge event-card__badge--done">COMPLETED</span>
              <time className="event-card__date" dateTime="2025-12">
                December 2025
              </time>
            </div>
            <h3 className="event-card__title">LeetCode League</h3>
            <p className="event-card__desc">
              Our inaugural sport programming contest. Participants solved algorithmic problems
              head-to-head in a competitive LeetCode format.
            </p>
          </AnimateIn>

          <AnimateIn className="event-card event-card--upcoming" delay={200}>
            <div className="event-card__header">
              <span className="event-card__badge event-card__badge--upcoming">Live Now</span>
              <time className="event-card__date" dateTime="2026-06">
                June 11–12, 2026
              </time>
            </div>
            <h3 className="event-card__title">Haiku Hackathon</h3>
            <p className="event-card__desc">
              A hackathon at Satbayev University, Almaty. Teams build, iterate, and present —
              three checkpoints, three deadlines.
            </p>
            <a href="#/hackathon" className="event-card__link">
              Checkpoint submissions →
            </a>
          </AnimateIn>
        </div>
      </div>
    </section>
  );
}
