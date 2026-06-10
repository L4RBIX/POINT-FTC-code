import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';

dayjs.extend(utc);

const ALMATY_OFFSET_HOURS = 5;

/** Format a deadline in Almaty time (UTC+5), e.g. "11.06, 18:00" */
export function formatDeadline(date) {
  return dayjs(date).utcOffset(ALMATY_OFFSET_HOURS).format('DD.MM, HH:mm');
}

export function isPast(deadline, now = Date.now()) {
  return dayjs(now).isAfter(dayjs(deadline));
}

/** Milliseconds remaining until deadline (>= 0) */
export function timeLeft(deadline, now = Date.now()) {
  return Math.max(0, dayjs(deadline).diff(dayjs(now)));
}

/** Format ms as HH:MM:SS (hours can exceed 24) */
export function formatCountdown(ms) {
  const totalSeconds = Math.floor(ms / 1000);
  const h = Math.floor(totalSeconds / 3600);
  const m = Math.floor((totalSeconds % 3600) / 60);
  const s = totalSeconds % 60;
  const pad = (n) => String(n).padStart(2, '0');
  return `${pad(h)}:${pad(m)}:${pad(s)}`;
}
