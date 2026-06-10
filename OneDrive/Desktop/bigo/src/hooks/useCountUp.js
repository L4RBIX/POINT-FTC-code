import { useEffect, useState } from 'react';

function easeOutCubic(t) {
  return 1 - Math.pow(1 - t, 3);
}

export function useCountUp(target, active, { duration = 1400, decimals = 0 } = {}) {
  const [value, setValue] = useState(0);

  useEffect(() => {
    if (!active) return;

    const numericTarget = typeof target === 'number' ? target : 0;
    if (numericTarget === 0) {
      setValue(0);
      return;
    }

    let start = null;
    let frame;

    const step = (timestamp) => {
      if (!start) start = timestamp;
      const progress = Math.min((timestamp - start) / duration, 1);
      const eased = easeOutCubic(progress);
      setValue(numericTarget * eased);
      if (progress < 1) frame = requestAnimationFrame(step);
    };

    frame = requestAnimationFrame(step);
    return () => cancelAnimationFrame(frame);
  }, [target, active, duration]);

  if (decimals > 0) return value.toFixed(decimals);
  return Math.round(value);
}
