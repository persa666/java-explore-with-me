DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS subscriptions CASCADE;

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT pk_categories PRIMARY KEY (id),
  CONSTRAINT uq_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(250) NOT NULL,
  email VARCHAR(254) NOT NULL,
  permission BOOLEAN NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id),
  CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS locations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  CONSTRAINT pk_locations PRIMARY KEY (id),
  CONSTRAINT uq_locations UNIQUE (lat,lon)
);


CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INT NOT NULL,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  request_moderation BOOLEAN NOT NULL,
  state VARCHAR(9) NOT NULL,
  title VARCHAR(120) NOT NULL,
  initiator_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  location_id BIGINT NOT NULL,
  CONSTRAINT pk_events PRIMARY KEY (id),
  CONSTRAINT fk_event_user FOREIGN KEY (initiator_id) REFERENCES users(id),
  CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES categories(id),
  CONSTRAINT fk_event_location FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  event_id BIGINT NOT NULL,
  requester_id BIGINT NOT NULL,
  status VARCHAR(9) NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY (id),
  CONSTRAINT uq_requests UNIQUE (event_id, requester_id),
  CONSTRAINT fk_request_event FOREIGN KEY (event_id) REFERENCES events(id),
  CONSTRAINT fk_request_users FOREIGN KEY (requester_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(50) NOT NULL,
  CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  compilation_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
  CONSTRAINT pk_compilations_events PRIMARY KEY (id),
  CONSTRAINT uq_compilations_events UNIQUE (compilation_id, event_id),
  CONSTRAINT fk_compilations_events FOREIGN KEY (compilation_id) REFERENCES compilations(id),
  CONSTRAINT fk_events_compilations FOREIGN KEY (event_id) REFERENCES events(id)
);

CREATE TABLE IF NOT EXISTS subscriptions (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  author_of_content BIGINT NOT NULL,
  subscriber_id BIGINT NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_subscriptions PRIMARY KEY (id),
  CONSTRAINT uq_subscriptions UNIQUE (author_of_content, subscriber_id),
  CONSTRAINT fk_subscriptions_author_users FOREIGN KEY (author_of_content) REFERENCES users(id),
  CONSTRAINT fk_subscriptions_subscriber_users FOREIGN KEY (subscriber_id) REFERENCES users(id),
  CONSTRAINT chk_author_subscriber_different CHECK (author_of_content <> subscriber_id)
);
