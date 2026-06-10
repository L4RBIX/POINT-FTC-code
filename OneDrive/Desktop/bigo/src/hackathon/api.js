const APPS_SCRIPT_URL = import.meta.env.VITE_APPS_SCRIPT_URL;
const SUPABASE_URL = import.meta.env.VITE_SUPABASE_URL;
const SUPABASE_ANON_KEY = import.meta.env.VITE_SUPABASE_ANON_KEY;
const BUCKET = import.meta.env.VITE_SUPABASE_BUCKET || 'presentations';

/** POST a checkpoint payload to the Google Apps Script endpoint. */
export async function submitToSheets(payload) {
  if (!APPS_SCRIPT_URL) {
    throw new Error('Эндпоинт не настроен (VITE_APPS_SCRIPT_URL). Сообщите организаторам.');
  }
  // text/plain keeps this a "simple request" — Apps Script can't answer CORS preflights
  const res = await fetch(APPS_SCRIPT_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'text/plain;charset=utf-8' },
    body: JSON.stringify({ ...payload, clientTime: new Date().toISOString() }),
  });
  if (!res.ok) {
    throw new Error('Не удалось отправить данные. Попробуйте ещё раз.');
  }
}

/** Upload a presentation to Supabase Storage; returns the public URL. */
export async function uploadPresentation(file, teamName) {
  if (!SUPABASE_URL || !SUPABASE_ANON_KEY) {
    throw new Error('Загрузка файлов не настроена (VITE_SUPABASE_URL). Сообщите организаторам.');
  }
  const ext = file.name.split('.').pop().toLowerCase();
  const safeTeam =
    teamName
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-+|-+$/g, '') || 'team';
  const path = `${safeTeam}-${Date.now()}.${ext}`;

  const res = await fetch(`${SUPABASE_URL}/storage/v1/object/${BUCKET}/${path}`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${SUPABASE_ANON_KEY}`,
      apikey: SUPABASE_ANON_KEY,
      'Content-Type': file.type || 'application/octet-stream',
    },
    body: file,
  });
  if (!res.ok) {
    throw new Error('Не удалось загрузить файл. Попробуйте ещё раз.');
  }
  return `${SUPABASE_URL}/storage/v1/object/public/${BUCKET}/${path}`;
}
