CREATE TABLE IF NOT EXISTS users (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    name    TEXT    NOT NULL,
    avatar  TEXT    DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS journal_entries (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     INTEGER NOT NULL,
    category    TEXT    NOT NULL,
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL,
    trigger     TEXT,
    target      TEXT,
    mood        TEXT    NOT NULL,
    mood_score  INTEGER NOT NULL,
    date        TEXT    NOT NULL,
    time        TEXT    NOT NULL,
    photo_id    INTEGER DEFAULT NULL,
    created_at  TEXT    DEFAULT (datetime('now')),
    updated_at  TEXT    DEFAULT (datetime('now')),
    FOREIGN KEY (user_id)  REFERENCES users(id),
    FOREIGN KEY (photo_id) REFERENCES photos(id)
);

CREATE TABLE IF NOT EXISTS photos (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    file_path   TEXT    NOT NULL,
    entry_id    INTEGER,
    uploaded_at TEXT    DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS self_care_targets (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     INTEGER NOT NULL,
    label       TEXT    NOT NULL,
    is_active   INTEGER DEFAULT 1,
    created_at  TEXT    DEFAULT (datetime('now')),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS self_care_completions (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    target_id   INTEGER NOT NULL,
    user_id     INTEGER NOT NULL,
    date        TEXT    NOT NULL,
    completed   INTEGER DEFAULT 0,
    FOREIGN KEY (target_id) REFERENCES self_care_targets(id),
    FOREIGN KEY (user_id)   REFERENCES users(id),
    UNIQUE(target_id, date)
);
