import Countdown from './Countdown';
import { formatDeadline } from './deadlines';
import { Checkpoint1Form, Checkpoint2Form, Checkpoint3Form } from './CheckpointForms';

const FORMS = {
  1: Checkpoint1Form,
  2: Checkpoint2Form,
  3: Checkpoint3Form,
};

function SubmittedSummary({ checkpointId, data }) {
  return (
    <div className="hk-summary">
      <p className="hk-summary__confirm">
        Чекпоинт {checkpointId} сдан! Команда {data.team}.
      </p>
      <dl className="hk-summary__list">
        {data.repoUrl && (
          <div>
            <dt>Репозиторий</dt>
            <dd>
              <a href={data.repoUrl} target="_blank" rel="noreferrer">
                {data.repoUrl}
              </a>
            </dd>
          </div>
        )}
        {data.idea && (
          <div>
            <dt>Идея</dt>
            <dd>{data.idea}</dd>
          </div>
        )}
        {data.changes && (
          <div>
            <dt>Изменения</dt>
            <dd>{data.changes}</dd>
          </div>
        )}
        {data.fileUrl && (
          <div>
            <dt>Презентация</dt>
            <dd>
              <a href={data.fileUrl} target="_blank" rel="noreferrer">
                {data.fileName || 'Файл'}
              </a>
            </dd>
          </div>
        )}
        {data.description && (
          <div>
            <dt>Описание</dt>
            <dd>{data.description}</dd>
          </div>
        )}
        {data.demoLink && (
          <div>
            <dt>Демо</dt>
            <dd>
              <a href={data.demoLink} target="_blank" rel="noreferrer">
                {data.demoLink}
              </a>
            </dd>
          </div>
        )}
        {data.submittedAt && (
          <div>
            <dt>Время сдачи</dt>
            <dd>{new Date(data.submittedAt).toLocaleString('ru-RU')}</dd>
          </div>
        )}
      </dl>
    </div>
  );
}

/**
 * status: 'done' | 'active' | 'locked' | 'future'
 */
export default function CheckpointCard({ checkpoint, deadline, status, submission, savedTeam, onSubmitted }) {
  const Form = FORMS[checkpoint.id];

  return (
    <li className={`hk-card hk-card--${status}`}>
      <div className="hk-card__marker" aria-hidden="true">
        {status === 'done' ? '✓' : checkpoint.id}
      </div>

      <div className="hk-card__body">
        <header className="hk-card__header">
          <div>
            <h3 className="hk-card__title">{checkpoint.title}</h3>
            <p className="hk-card__subtitle">{checkpoint.subtitle}</p>
          </div>
          {status === 'done' && <span className="hk-badge hk-badge--done">Сдано ✓</span>}
          {status === 'locked' && <span className="hk-badge hk-badge--locked">Приём закрыт</span>}
          {status === 'active' && <span className="hk-badge hk-badge--active">Открыт</span>}
        </header>

        <p className="hk-card__deadline">
          Дедлайн: {formatDeadline(deadline)} <span className="hk-card__tz">(UTC+5, Алматы)</span>
        </p>

        {status === 'active' && (
          <>
            <Countdown deadline={deadline} />
            <Form savedTeam={savedTeam} onSubmitted={onSubmitted} />
          </>
        )}

        {status === 'done' && <SubmittedSummary checkpointId={checkpoint.id} data={submission} />}

        {status === 'locked' && (
          <p className="hk-card__note">Дедлайн прошёл. Приём работ по этому чекпоинту закрыт.</p>
        )}

        {status === 'future' && (
          <p className="hk-card__note">Откроется после сдачи предыдущего чекпоинта.</p>
        )}
      </div>
    </li>
  );
}
