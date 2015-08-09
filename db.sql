create table orders (
    _id integer primary key autoincrement,
    order_id int default -1,
    order_number int default -1,
    create_time integer default 10,
    add_time integer default 10,
    price decimal(6, 2) default 0.00,
    subtotal decimal(6, 2) default 0.00,
    mobile_phone varchar(20) default '0',
    address varchar(255) default '',
    remarks text default '',
    source varchar(100) default '',
    order_code char(13) not null unique default '',
    restaurant_id integer default -1,
    request_type integer default 1
);


create table goods (
    _id integer primary key autoincrement,
    order_id int default -1,
    item_id integer default -1,
    order_code varchar(15) not null unique default '',
    restaurant varchar(60) default '',
    course_name varchar(60) default '',
    price decimal(6, 2) default 0.00,
    quantity int default 0,
    total_price decimal(10, 2) default 0.00,
    event_id int default -1,
    logistics_info text default '',
    deliver_name varchar(20) default '',
    phone varchar(20) default ''
);

create index if not exists orders_index on orders (order_code);
create index if not exists goods_index on goods (order_code);


create table logistics (
    _id integer primary key autoincrement,
    item_id int default -1,
    stage_id int default -1,
    dlv_name varchar(20) default '',
    dlv_time varchar(20) default '',
    event_id int default -1,
    mobile_phone varchar(20) default '',
    real_name varchar(20) default ''
);





create table events (
    _id integer primary key autoincrement,
    title text not null,
    start_time integer not null,
    end_time integer not null,
    repeat integer not null,
    notify integer not null,
    type integer not null
);