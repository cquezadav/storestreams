CREATE KEYSPACE IF NOT EXISTS storestreams WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS storestreams.raw_events (
    year int,
    month int,
    day int,
    hour int,
    minutes int,
    event_id timeuuid,
    message_id bigint,
    timestamp timestamp,
    visit_origin text,
    device_type text,
    os text,
    location text,
    department text,
    product_id int,
    quantity int,
    action text,
    transaction_id bigint,
    payment_type text,
    shipment_type text,
    PRIMARY KEY ((year, month, day, hour), event_id)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_hour (
    year int,
    month int,
    day int,
    hour int,
    location text,
    count counter,
    PRIMARY KEY (year, month, day, hour, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_hour_speed (
    year int,
    month int,
    day int,
    hour int,
    location text,
    count bigint,
    PRIMARY KEY (year, month, day, hour, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_day (
    year int,
    month int,
    day int,
    location text,
    count counter,
    PRIMARY KEY (year, month, day, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_day_speed (
    year int,
    month int,
    day int,
    location text,
    count counter,
    PRIMARY KEY (year, month, day, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_month (
    year int,
    month int,
    location text,
    count counter,
    PRIMARY KEY (year, month, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_month_speed (
    year int,
    month int,
    location text,
    count bigint,
    PRIMARY KEY (year, month, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_year (
    year int,
    location text,
    count counter,
    PRIMARY KEY (year, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_per_year_speed (
    year int,
    location text,
    count bigint,
    PRIMARY KEY (year, location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location (
    location text,
    count counter,
    PRIMARY KEY (location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_location_speed (
    location text,
    count bigint,
    PRIMARY KEY (location)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_year (
    year int,
    count counter,
    PRIMARY KEY (year)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_year_speed (
    year int,
    count bigint,
    PRIMARY KEY (year)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_month (
    year int,
    month int,
    count counter,
    PRIMARY KEY (year, month)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_month_speed (
    year int,
    month int,
    count bigint,
    PRIMARY KEY (year, month)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_day (
    year int,
    month int,
    day int,
    count counter,
    PRIMARY KEY (year, month, day)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_day (
    year int,
    month int,
    day int,
    count counter,
    PRIMARY KEY (year, month, day)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_day_speed (
    year int,
    month int,
    day int,
    count bigint,
    PRIMARY KEY (year, month, day)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_hour (
    year int,
    month int,
    day int,
    hour int,
    count counter,
    PRIMARY KEY (year, month, day, hour)
);

CREATE TABLE IF NOT EXISTS storestreams.events_per_hour_speed (
    year int,
    month int,
    day int,
    hour int,
    count bigint,
    PRIMARY KEY (year, month, day, hour)
);


