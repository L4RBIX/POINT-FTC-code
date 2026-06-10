const KEY = 'haiku_hackathon_submissions';

/** Shape: { team: string, checkpoints: { [id]: { ...data, submittedAt } } } */
export function loadState() {
  try {
    const raw = localStorage.getItem(KEY);
    const parsed = raw ? JSON.parse(raw) : null;
    if (parsed && typeof parsed === 'object') {
      return { team: parsed.team ?? '', checkpoints: parsed.checkpoints ?? {} };
    }
  } catch {
    // corrupted state — start fresh
  }
  return { team: '', checkpoints: {} };
}

export function saveSubmission(checkpointId, team, data) {
  const state = loadState();
  const next = {
    team,
    checkpoints: {
      ...state.checkpoints,
      [checkpointId]: { ...data, team, submittedAt: new Date().toISOString() },
    },
  };
  localStorage.setItem(KEY, JSON.stringify(next));
  return next;
}
