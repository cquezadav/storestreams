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

CREATE TABLE storestreams.events_per_location_per_hour_batch (
    year int,
    month int,
    day int,
    hour int,
    location text,
    count bigint,
    PRIMARY KEY (year, month, day, hour, location)
);

CREATE TABLE storestreams.events_per_location_per_hour_speed (
    year int,
    month int,
    day int,
    hour int,
    location text,
    count bigint,
    PRIMARY KEY (year, month, day, hour, location)
);

