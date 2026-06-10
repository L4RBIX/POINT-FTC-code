import { useMemo, useState } from 'react';
import { TEAMS, MAX_FILE_SIZE, ALLOWED_EXTENSIONS } from './config';
import { submitToSheets, uploadPresentation } from './api';

function Field({ label, error, hint, children }) {
  return (
    <label className="hk-field">
      <span className="hk-field__label">{label}</span>
      {children}
      {hint && !error && <span className="hk-field__hint">{hint}</span>}
      {error && <span className="hk-field__error">{error}</span>}
    </label>
  );
}

function SubmitButton({ busy }) {
  return (
    <button type="submit" className="hk-submit" disabled={busy}>
      {busy ? 'Отправка…' : 'Сдать чекпоинт'}
    </button>
  );
}

function TeamSelect({ value, onChange, savedTeam }) {
  const options = useMemo(() => {
    const list = savedTeam && !TEAMS.includes(savedTeam) ? [savedTeam, ...TEAMS] : TEAMS;
    return list;
  }, [savedTeam]);

  return (
    <select className="hk-input" value={value} onChange={(e) => onChange(e.target.value)}>
      <option value="">— Выберите команду —</option>
      {options.map((t) => (
        <option key={t} value={t}>
          {t}
        </option>
      ))}
    </select>
  );
}

export function Checkpoint1Form({ savedTeam, onSubmitted }) {
  const [team, setTeam] = useState(savedTeam || '');
  const [repo, setRepo] = useState('');
  const [idea, setIdea] = useState('');
  const [errors, setErrors] = useState({});
  const [busy, setBusy] = useState(false);
  const [serverError, setServerError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = {};
    if (!team.trim()) errs.team = 'Укажите название команды';
    if (!repo.trim().startsWith('https://github.com/')) {
      errs.repo = 'Ссылка должна начинаться с https://github.com/';
    }
    if (idea.trim().length < 100) {
      errs.idea = `Минимум 100 символов (сейчас ${idea.trim().length})`;
    }
    setErrors(errs);
    if (Object.keys(errs).length > 0) return;

    setBusy(true);
    setServerError('');
    try {
      const data = { team: team.trim(), repoUrl: repo.trim(), idea: idea.trim() };
      await submitToSheets({ checkpoint: 1, ...data });
      onSubmitted(data);
    } catch (err) {
      setServerError(err.message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <form className="hk-form" onSubmit={handleSubmit} noValidate>
      <Field label="Название команды" error={errors.team}>
        <input
          className="hk-input"
          type="text"
          value={team}
          onChange={(e) => setTeam(e.target.value)}
          placeholder="Например: Null Pointers"
        />
      </Field>

      <Field label="Публичный GitHub-репозиторий" error={errors.repo}>
        <input
          className="hk-input"
          type="url"
          value={repo}
          onChange={(e) => setRepo(e.target.value)}
          placeholder="https://github.com/team/project"
        />
      </Field>

      <Field
        label="Описание идеи"
        error={errors.idea}
        hint={`Минимум 100 символов · ${idea.trim().length}`}
      >
        <textarea
          className="hk-input hk-textarea"
          rows={5}
          value={idea}
          onChange={(e) => setIdea(e.target.value)}
          placeholder="Опишите проблему, которую решаете, и ваше решение..."
        />
      </Field>

      {serverError && <p className="hk-server-error">{serverError}</p>}
      <SubmitButton busy={busy} />
    </form>
  );
}

export function Checkpoint2Form({ savedTeam, onSubmitted }) {
  const [team, setTeam] = useState(savedTeam || '');
  const [changes, setChanges] = useState('');
  const [errors, setErrors] = useState({});
  const [busy, setBusy] = useState(false);
  const [serverError, setServerError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = {};
    if (!team) errs.team = 'Выберите команду';
    if (changes.trim().length < 100) {
      errs.changes = `Минимум 100 символов (сейчас ${changes.trim().length})`;
    }
    setErrors(errs);
    if (Object.keys(errs).length > 0) return;

    setBusy(true);
    setServerError('');
    try {
      const data = { team, changes: changes.trim() };
      await submitToSheets({ checkpoint: 2, ...data });
      onSubmitted(data);
    } catch (err) {
      setServerError(err.message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <form className="hk-form" onSubmit={handleSubmit} noValidate>
      <Field label="Команда" error={errors.team}>
        <TeamSelect value={team} onChange={setTeam} savedTeam={savedTeam} />
      </Field>

      <Field
        label="Что изменилось с момента первоначальной идеи"
        error={errors.changes}
        hint={`Минимум 100 символов · ${changes.trim().length}`}
      >
        <textarea
          className="hk-input hk-textarea"
          rows={5}
          value={changes}
          onChange={(e) => setChanges(e.target.value)}
          placeholder="Расскажите, как развивалась идея, что вы убрали или добавили..."
        />
      </Field>

      {serverError && <p className="hk-server-error">{serverError}</p>}
      <SubmitButton busy={busy} />
    </form>
  );
}

const DESC_MAX = 300;

export function Checkpoint3Form({ savedTeam, onSubmitted }) {
  const [team, setTeam] = useState(savedTeam || '');
  const [file, setFile] = useState(null);
  const [description, setDescription] = useState('');
  const [demoLink, setDemoLink] = useState('');
  const [errors, setErrors] = useState({});
  const [busy, setBusy] = useState(false);
  const [busyText, setBusyText] = useState('');
  const [serverError, setServerError] = useState('');

  const handleFile = (e) => {
    const f = e.target.files?.[0] ?? null;
    setFile(f);
    if (!f) return;
    const ext = f.name.split('.').pop().toLowerCase();
    if (!ALLOWED_EXTENSIONS.includes(ext)) {
      setErrors((prev) => ({ ...prev, file: 'Допустимые форматы: .pdf, .pptx, .key' }));
    } else if (f.size > MAX_FILE_SIZE) {
      setErrors((prev) => ({
        ...prev,
        file: `Файл слишком большой (${(f.size / 1024 / 1024).toFixed(1)} МБ). Максимум 20 МБ.`,
      }));
    } else {
      setErrors((prev) => ({ ...prev, file: undefined }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = {};
    if (!team) errs.team = 'Выберите команду';
    if (!file) {
      errs.file = 'Прикрепите презентацию';
    } else {
      const ext = file.name.split('.').pop().toLowerCase();
      if (!ALLOWED_EXTENSIONS.includes(ext)) errs.file = 'Допустимые форматы: .pdf, .pptx, .key';
      else if (file.size > MAX_FILE_SIZE) errs.file = 'Максимальный размер файла — 20 МБ';
    }
    if (!description.trim()) errs.description = 'Добавьте описание проекта';
    if (description.length > DESC_MAX) errs.description = `Максимум ${DESC_MAX} символов`;
    setErrors(errs);
    if (Object.values(errs).some(Boolean)) return;

    setBusy(true);
    setServerError('');
    try {
      setBusyText('Загрузка файла…');
      const fileUrl = await uploadPresentation(file, team);
      setBusyText('Отправка данных…');
      const data = {
        team,
        fileUrl,
        fileName: file.name,
        description: description.trim(),
        demoLink: demoLink.trim(),
      };
      await submitToSheets({ checkpoint: 3, ...data });
      onSubmitted(data);
    } catch (err) {
      setServerError(err.message);
    } finally {
      setBusy(false);
      setBusyText('');
    }
  };

  return (
    <form className="hk-form" onSubmit={handleSubmit} noValidate>
      <Field label="Команда" error={errors.team}>
        <TeamSelect value={team} onChange={setTeam} savedTeam={savedTeam} />
      </Field>

      <Field label="Презентация (.pdf, .pptx, .key — до 20 МБ)" error={errors.file}>
        <input
          className="hk-input hk-input--file"
          type="file"
          accept=".pdf,.pptx,.key"
          onChange={handleFile}
        />
      </Field>

      <Field
        label="Краткое описание проекта для жюри"
        error={errors.description}
        hint={`${description.length}/${DESC_MAX}`}
      >
        <textarea
          className="hk-input hk-textarea"
          rows={4}
          maxLength={DESC_MAX}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Одно-два предложения: что делает проект и чем он хорош"
        />
      </Field>

      <Field label="Ссылка на демо (необязательно)" error={errors.demoLink}>
        <input
          className="hk-input"
          type="url"
          value={demoLink}
          onChange={(e) => setDemoLink(e.target.value)}
          placeholder="YouTube, Figma, задеплоенное приложение…"
        />
      </Field>

      {serverError && <p className="hk-server-error">{serverError}</p>}
      <button type="submit" className="hk-submit" disabled={busy}>
        {busy ? busyText || 'Отправка…' : 'Сдать чекпоинт'}
      </button>
    </form>
  );
}
