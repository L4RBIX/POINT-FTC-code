import { useEffect, useState } from 'react';

export function useTypingEffect(phrases, { typeSpeed = 70, deleteSpeed = 40, pauseMs = 2200 } = {}) {
  const [display, setDisplay] = useState('');
  const [phraseIndex, setPhraseIndex] = useState(0);
  const [isDeleting, setIsDeleting] = useState(false);

  useEffect(() => {
    const current = phrases[phraseIndex] ?? '';
    let timeout;

    if (!isDeleting && display.length < current.length) {
      timeout = setTimeout(() => {
        setDisplay(current.slice(0, display.length + 1));
      }, typeSpeed);
    } else if (!isDeleting && display.length === current.length) {
      timeout = setTimeout(() => setIsDeleting(true), pauseMs);
    } else if (isDeleting && display.length > 0) {
      timeout = setTimeout(() => {
        setDisplay(current.slice(0, display.length - 1));
      }, deleteSpeed);
    } else if (isDeleting && display.length === 0) {
      setIsDeleting(false);
      setPhraseIndex((i) => (i + 1) % phrases.length);
    }

    return () => clearTimeout(timeout);
  }, [display, isDeleting, phraseIndex, phrases, typeSpeed, deleteSpeed, pauseMs]);

  return display;
}
