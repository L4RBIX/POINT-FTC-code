import AnimateIn from './AnimateIn';

const TEAM = [
  { name: 'Ibrahim Mukan', role: 'Founder' },
  { name: 'Ratmir Oliza', role: 'PR Management' },
  { name: 'Temirlan Kurmet', role: 'Logistics Manager' },
  { name: 'Adi Karimzhan', role: 'Logistics Manager' },
  { name: 'Kaisar Nurlan', role: 'Tech Support' },
];

export default function Team() {
  return (
    <section className="section team" id="team">
      <div className="container">
        <AnimateIn>
          <p className="section-label">Team</p>
          <h2 className="section-title">The People Behind BigO</h2>
        </AnimateIn>

        <div className="team__grid">
          {TEAM.map((member, i) => (
            <AnimateIn
              key={member.name}
              as="article"
              className="team-card"
              delay={1 * (i + 1)}
            >
              <div className="team-card__avatar" aria-hidden="true">
                {member.name
                  .split(' ')
                  .map((n) => n[0])
                  .join('')}
              </div>
              <h3 className="team-card__name">{member.name}</h3>
              {member.role && <p className="team-card__role">{member.role}</p>}
            </AnimateIn>
          ))}
        </div>
      </div>
    </section>
  );
}
