-- create database Oscatdb
use Oscatdb;

-- �إ߹q�v(Movie)��ƪ�
CREATE TABLE movie
(
    movie_id       uniqueidentifier PRIMARY KEY,
    movie_name     VARCHAR(255) not null,
    movie_type     VARCHAR(255) not null,
    movie_status   VARCHAR(10)  not null,
    director       VARCHAR(255) not null,
    writer_list    VARCHAR(255),
    actor_list     VARCHAR(255),
    plot_summary   VARCHAR(MAX) not null,
    release_date   DATE         not null,
    duration       INT          not null,
    classification VARCHAR(50)  not null,
    trailer_link   VARCHAR(255),
    poster_image   VARCHAR(1000)
);

-- �@���Ӥ���ƪ�
CREATE TABLE movie_stills
(
    still_id        uniqueidentifier PRIMARY KEY,
    movie_id        uniqueidentifier REFERENCES movie (movie_id),
    still_image_url VARCHAR(1000) NOT NULL
);

-- �إ߷|��(Member)��ƪ�
CREATE TABLE member
(
    member_id   uniqueidentifier PRIMARY KEY,
    member_name VARCHAR(255) not null,
    email       VARCHAR(255) UNIQUE,
    password    VARCHAR(255) not null,
    phone       VARCHAR(20),
    gender      VARCHAR(50),
    birth_date  DATE         not null,
    join_date   DATETIME     not null
);

-- �إ߼v��(Cinema)��ƪ�
CREATE TABLE cinema
(
    cinema_id      INT IDENTITY (1,1) PRIMARY KEY,
    cinema_name    VARCHAR(255) NOT NULL,
    cinema_address VARCHAR(255) NOT NULL,
    contact_phone  VARCHAR(20) NOT NULL,
    opening_hours  VARCHAR(100) NOT NULL,
    facilities     VARCHAR(MAX) NOT NULL,
    base_price     DECIMAL(10, 2) NOT NULL  -- �s�W����¦����
);

-- �إߩ�M�U (ScreeningRoom)��ƪ�
CREATE TABLE screening_room
(
    room_id   INT identity (1,1) PRIMARY KEY,
    room_name VARCHAR(255) not null,
    type      varchar(50)  not null,
    cinema_id INT FOREIGN KEY REFERENCES cinema (cinema_id)
);

-- �إ߮y�� (Seat)��ƪ�
CREATE TABLE seat
(
    seat_id     uniqueidentifier PRIMARY KEY,
    seat_name   varchar(50) not null ,
    room_id     INT FOREIGN KEY REFERENCES screening_room (room_id),
    seat_status VARCHAR(50) not null
);

-- �إ߲��� (TicketType)��ƪ�
CREATE TABLE ticket_type
(
    ticket_type_id             INT IDENTITY (1,1) PRIMARY KEY,
    ticket_type_name           VARCHAR(255) NOT NULL,
    price_difference  DECIMAL(5, 2) NOT NULL  -- �ק�ھڲ��ئ����P�����T
);

-- �إ߳���(ShowTime)��ƪ�
CREATE TABLE showtime
(
    showtime_id        UNIQUEIDENTIFIER PRIMARY KEY,
    movie_id           UNIQUEIDENTIFIER FOREIGN KEY REFERENCES movie (movie_id),
    room_id            INT FOREIGN KEY REFERENCES screening_room (room_id),
    film_type          VARCHAR(255) NOT NULL,
    price              DECIMAL(10, 2) NOT NULL,
    extra_fee          DECIMAL(10, 2) DEFAULT 0,  -- �s�W���S�w��������վ�
    show_date_and_time DATETIME2 NOT NULL
);

-- �إ߭q������(TransOrder)��ƪ�
CREATE TABLE trans_order
(
    order_id              uniqueidentifier PRIMARY KEY,
    showtime_id           uniqueidentifier FOREIGN KEY REFERENCES showtime (showtime_id),
    member_id             uniqueidentifier foreign key references member (member_id),
    payment_method        VARCHAR(50)    not null,
    booking_date_and_time DATETIME       not null,
    total_price           DECIMAL(10, 2) not null
);

-- �إ߹q�v�� (Ticket)��ƪ�
CREATE TABLE ticket
(
    ticket_id uniqueidentifier,
    order_id  uniqueidentifier FOREIGN KEY REFERENCES trans_order (order_id),
    type_id   INT FOREIGN KEY REFERENCES ticket_type (ticket_type_id),
    seat_id   uniqueidentifier FOREIGN KEY REFERENCES seat (seat_id),
    PRIMARY KEY (ticket_id, seat_id)
);


-- �إ߲��~(Product)��ƪ�
CREATE TABLE product
(
    product_id    uniqueidentifier PRIMARY KEY,
    product_name  VARCHAR(255) NOT NULL,
    unit_price    DECIMAL(10, 2) NOT NULL,
    product_type  VARCHAR(50) NOT NULL,
	product_img VARCHAR(255) NOT NULL
);


-- �إ߼v�����~ ��ƪ�
CREATE TABLE cinema_product
(
    cinema_id    INT,
    product_id   UNIQUEIDENTIFIER,
    PRIMARY KEY (cinema_id, product_id),
    FOREIGN KEY (cinema_id) REFERENCES cinema (cinema_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id)
);

