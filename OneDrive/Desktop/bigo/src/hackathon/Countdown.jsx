import { useEffect, useState } from 'react';
import { timeLeft, formatCountdown } from './deadlines';

export default function Countdown({ deadline }) {
  const [ms, setMs] = useState(() => timeLeft(deadline));

  useEffect(() => {
    const id = setInterval(() => setMs(timeLeft(deadline)), 1000);
    return () => clearInterval(id);
  }, [deadline]);

  return (
    <div className="hk-countdown" role="timer" aria-label="Время до дедлайна">
      <span className="hk-countdown__label">До дедлайна</span>
      <span className="hk-countdown__time">{formatCountdown(ms)}</span>
    </div>
  );
}
