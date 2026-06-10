import { useEffect, useState } from 'react';
import { CHECKPOINTS, DEADLINES } from './config';
import { isPast } from './deadlines';
import { loadState, saveSubmission } from './storage';
import CheckpointCard from './CheckpointCard';
import './hackathon.css';

export default function HackathonPage() {
  const [state, setState] = useState(loadState);
  const [now, setNow] = useState(Date.now());

  useEffect(() => {
    window.scrollTo(0, 0);
    const id = setInterval(() => setNow(Date.now()), 1000);
    return () => clearInterval(id);
  }, []);

  const getStatus = (id) => {
    if (state.checkpoints[id]) return 'done';
    if (isPast(DEADLINES[id], now)) return 'locked';
    const prevDone = id === 1 || !!state.checkpoints[id - 1];
    return prevDone ? 'active' : 'future';
  };

  const handleSubmitted = (id) => (data) => {
    setState(saveSubmission(id, data.team, data));
  };

  return (
    <div className="hk-page">
      <header className="hk-header">
        <div className="container hk-header__inner">
          <a href="#/" className="hk-header__logo" aria-label="BigO home">
            <img src="/logo.png" alt="BigO" width={100} height={30} />
          </a>
          <a href="#/" className="hk-header__back">
            ← На главную
          </a>
        </div>
      </header>

      <main className="container hk-main">
        <p className="section-label hk-label">Haiku Hackathon · Almaty · Satbayev University</p>
        <h1 className="hk-title">Сдача чекпоинтов</h1>
        <p className="hk-intro">
          Три чекпоинта — три дедлайна. Каждый следующий открывается после сдачи предыдущего.
          После дедлайна форма закрывается автоматически. Все времена указаны по Алматы (UTC+5).
        </p>

        <ol className="hk-timeline">
          {CHECKPOINTS.map((cp) => (
            <CheckpointCard
              key={cp.id}
              checkpoint={cp}
              deadline={DEADLINES[cp.id]}
              status={getStatus(cp.id)}
              submission={state.checkpoints[cp.id]}
              savedTeam={state.team}
              onSubmitted={handleSubmitted(cp.id)}
            />
          ))}
        </ol>
      </main>
    </div>
  );
}
