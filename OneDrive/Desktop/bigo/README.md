# BigO Landing Page

Dark, terminal-core landing page for **BigO** — a competitive programming community.

## Stack

- React 18 + Vite
- Vanilla CSS (no UI libraries)
- Google Fonts: JetBrains Mono, IBM Plex Sans

## Run locally

```bash
npm install
npm run dev
```

Open [http://localhost:5173](http://localhost:5173).

## Build

```bash
npm run build
npm run preview
```

## Customize

- **Contact / CTA**: Edit `mailto:` in `src/components/Join.jsx` and social `href`s in `src/components/Footer.jsx`.
- **Logo**: Replace `public/logo.png`.

---

# Haiku Hackathon — Checkpoint Submission System

Available at `#/hackathon` (e.g. `http://localhost:5173/#/hackathon`).

Three checkpoints on a vertical timeline. Each unlocks after the previous one is
submitted; after the deadline the form locks ("Приём закрыт"). Submission state is
stored in `localStorage`, so a refresh doesn't reset progress. All deadlines are
Almaty time (UTC+5) — see `src/hackathon/config.js` (also edit the `TEAMS` list there).

## Setup

Copy `.env.example` to `.env` and fill in:

| Variable | Purpose |
| --- | --- |
| `VITE_APPS_SCRIPT_URL` | Google Apps Script web app URL — receives all submissions |
| `VITE_SUPABASE_URL` | Supabase project URL — checkpoint 3 file uploads |
| `VITE_SUPABASE_ANON_KEY` | Supabase publishable (anon) key |
| `VITE_SUPABASE_BUCKET` | Storage bucket name (default `presentations`) |

### 1. Google Sheets endpoint (Apps Script)

In your Google Sheet: **Extensions → Apps Script**, paste:

```js
function doPost(e) {
  const data = JSON.parse(e.postData.contents);
  const sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName('Лист1'); // your sheet name
  sheet.appendRow([
    new Date(),
    data.checkpoint,
    data.team,
    data.repoUrl || '',
    data.idea || '',
    data.changes || '',
    data.fileUrl || '',
    data.description || '',
    data.demoLink || '',
  ]);
  return ContentService.createTextOutput(JSON.stringify({ ok: true }))
    .setMimeType(ContentService.MimeType.JSON);
}
```

Then **Deploy → New deployment → Web app**, execute as *Me*, access: *Anyone*.
Copy the `/exec` URL into `VITE_APPS_SCRIPT_URL`.

### 2. Supabase Storage bucket

Run in the SQL editor of your Supabase project:

```sql
insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values (
  'presentations', 'presentations', true, 20971520,
  array[
    'application/pdf',
    'application/vnd.openxmlformats-officedocument.presentationml.presentation',
    'application/vnd.apple.keynote',
    'application/octet-stream'
  ]
);

create policy "anon can upload presentations"
on storage.objects for insert to anon
with check (bucket_id = 'presentations');
```

The bucket is public-read; the anon key can only insert (no overwrite/delete).
