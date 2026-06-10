// NOTE: the original spec said 2025, but the hackathon runs June 11-12, 2026
// (today's event). Change the year here if that's wrong.
export const DEADLINES = {
  1: new Date('2026-06-11T18:00:00+05:00'),
  2: new Date('2026-06-12T03:00:00+05:00'),
  3: new Date('2026-06-12T12:30:00+05:00'),
};

// TODO: replace with the real list of registered teams
export const TEAMS = [
  'Stack Overflowers',
  'Null Pointers',
  'Big O Notation',
  'Ctrl+Alt+Defeat',
  'Segfault Squad',
];

export const CHECKPOINTS = [
  {
    id: 1,
    title: 'Чекпоинт 1 — Идея',
    subtitle: 'Команда, репозиторий и описание идеи',
  },
  {
    id: 2,
    title: 'Чекпоинт 2 — Прогресс',
    subtitle: 'Что изменилось с момента старта',
  },
  {
    id: 3,
    title: 'Чекпоинт 3 — Финал',
    subtitle: 'Презентация и описание для жюри',
  },
];

export const MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
export const ALLOWED_EXTENSIONS = ['pdf', 'pptx', 'key'];
