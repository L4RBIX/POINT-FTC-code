import AnimateIn from './AnimateIn';

export default function Join() {
  return (
    <section className="section join" id="join">
      <div className="join__glow" aria-hidden="true" />
      <div className="container join__inner">
        <AnimateIn>
          <h2 className="join__title">Ready to level up your coding?</h2>
          <p className="join__sub">
            Join BigO and compete, build, and grow with a community of passionate programmers.
          </p>
          <a href="mailto:hello@bigo.dev" className="btn btn--primary">
            Get Involved
          </a>
        </AnimateIn>
      </div>
    </section>
  );
}
