create database if not exists forum;
use forum;

create table if not exists `user`(
	`id` bigint not null auto_increment,
    `username` varchar(30) not null unique,
    `password` varchar(100) not null,
    `name` nvarchar(50) not null,
    `date_of_birth` date,
    `gender` nchar(10),
    `email` char(50),
    `profile_picture` varchar(250) DEFAULT "/resources/images/default_avatar.png",
    `last_login` bigint DEFAULT 0,
    `online` tinyint not null default 0,
    `create_time` timestamp not null default current_timestamp,
	constraint pk_user primary key(`id`)
);

create table if not exists `role`(
	`id` bigint not null,
    `name` nvarchar(100) not null,
    `code` varchar(50) not null,
	constraint pk_role primary key(`id`)
);

create table if not exists `user_role`(
    `user_id` bigint not null,
    `role_id` bigint not null,
    constraint `u_1` unique (`user_id`, `role_id`),
    constraint `fk_1` foreign key(`user_id`) references `user`(`id`),
    constraint `fk_2` foreign key(`role_id`) references `role`(`id`)
);

create table if not exists `group`(
	`id` bigint not null auto_increment,
    `name` nvarchar(150) not null,
    `description` nvarchar(200) not null,
    `create_time` timestamp not null default current_timestamp,
	constraint pk_group primary key(`id`)
);

create table if not exists `post`(
    `id` bigint not null auto_increment,
    `title` nvarchar(150) not null,
    `content` text not null,
    `user_id` bigint not null,
    `group_id` bigint not null,
    `pin_on_top` bit default 0,
    `create_time` timestamp not null default current_timestamp,
    constraint pk_post primary key(`id`),
    constraint fk_post foreign key(`user_id`) references `user`(`id`) on delete cascade,
    constraint fk_post_group foreign key(`group_id`) references `group`(`id`) on delete cascade
);

create table if not exists `comment`(
    `id` bigint not null auto_increment,
    `content` text not null,
    `user_id` bigint not null,
    `post_id` bigint not null,
    `create_time` timestamp not null default current_timestamp,
	constraint pk_comment primary key(`id`),
    constraint fk_comment_user foreign key(`user_id`) references `user`(`id`) on delete cascade,
    constraint fk_comment_post foreign key(`post_id`) references `post`(`id`) on delete cascade
);

create table if not exists `like`(
    `id` bigint not null auto_increment,
    `of_post` char(1) not null, -- F: binh luan, T: bai viet
    `user_id` bigint not null,
    `post_id` bigint,
    `comment_id` bigint,
    `create_time` timestamp not null default current_timestamp,
	constraint pk_like primary key(`id`),
    constraint fk_like_post foreign key(`post_id`) references `post`(`id`) on delete cascade,
    constraint fk_like_cmt foreign key(`comment_id`) references `comment`(`id`) on delete cascade,
    constraint fk_like_user foreign key(`user_id`) references `user`(`id`) on delete cascade
);

create table if not exists `notify`(
    `id` bigint not null auto_increment,
    `state` bit default 0, -- 0: chua doc, 1: da doc
    `sender` bigint not null,
    `recipient` bigint not null,
    `content` nvarchar(200) not null,
	`url` varchar(200),
    `create_time` timestamp not null default current_timestamp,
	constraint pk_notify primary key(`id`),
    constraint fk_notify_sender foreign key(`sender`) references `user`(`id`) on delete cascade,
    constraint fk_notify_recipient foreign key(`recipient`) references `user`(`id`) on delete cascade
);

create table if not exists `image`(
	`id` bigint not null auto_increment,
    `user_id` bigint not null,
    `of_post` char(1) not null, -- F: binh luan, T: bai viet
	`url` varchar(200) not null,
    `post_id` bigint,
    `comment_id` bigint,
    `create_time` timestamp not null default current_timestamp,
	constraint pk_image primary key(`id`),
    constraint fk_image_owner foreign key(`user_id`) references `user`(`id`) on delete cascade,
    constraint fk_image_post foreign key(`post_id`) references `user`(`id`) on delete cascade,
    constraint fk_image_cmt foreign key(`comment_id`) references `user`(`id`) on delete cascade
);

create table if not exists `message`(
	`id` bigint not null auto_increment,
    `sender` bigint not null,
    `recipient` bigint not null,
    `content` text not null,
    `state` bit not null default 0, -- 0: unread, 1: readed
    `create_time` timestamp not null default current_timestamp,
	constraint pk_message primary key(`id`),
    constraint fk_message_sender foreign key(`sender`) references `user`(`id`) on delete cascade,
    constraint fk_messages_recipient foreign key(`recipient`) references `user`(`id`) on delete cascade
);


create table if not exists `acl_sid`(
	`id` bigint not null auto_increment,
    `principal` tinyint not null,
    `sid` varchar(100) not null,
    primary key(`id`),
    constraint `unique_2` unique (`sid`, `principal`)
);

create table if not exists `acl_class` (
	`id` bigint not null auto_increment,
    `class` varchar(255) not null unique,
    primary key(`id`)
);

create table if not exists `acl_object_identity` (
	`id` bigint not null auto_increment,
    `object_id_class` bigint not null,
    `object_id_identity` bigint not null,
    `parent_object` bigint default null,
    `owner_sid` bigint default null,
    `entries_inheriting` tinyint not null,
    primary key(`id`),
    constraint `unique_3` unique(`object_id_class`, `object_id_identity`),
    constraint `fk_4` foreign key (`parent_object`) references `acl_object_identity`(`id`),
    constraint `fk_5` foreign key (`object_id_class`) references `acl_class`(`id`),
    constraint `fk_6` foreign key (`owner_sid`) references `acl_sid`(`id`)
);


insert into `user`(`id`, `username`, `password`, `name`) values(1, 'admin', '$2a$10$98pCm6cwS7QlOxW1HjuXGufSvI01w8PEL.4dKzQYqvs6OGwFjUGWm', 'Admin');
insert into `user`(`id`, `username`, `password`, `name`) values(2, 'chatbot', '$2a$10$98pCm6cwS7QlOxW1HjuXGufSvI01w8PEL.4dKzQYqvs6OGwFjUGWm', 'Chat Bot');
insert into `user`(`id`, `username`, `password`, `name`) values(3, 'user', '$2a$10$98pCm6cwS7QlOxW1HjuXGufSvI01w8PEL.4dKzQYqvs6OGwFjUGWm', 'User');

insert into `role`(`id`, `name`, `code`) values(1, 'Thành viên', 'ROLE_USER');
insert into `role`(`id`, `name`, `code`) values(2, 'Quản lý nhóm', 'ROLE_GROUP_MANAGER');
insert into `role`(`id`, `name`, `code`) values(3, 'Quản lý', 'ROLE_MANAGER');
insert into `role`(`id`, `name`, `code`) values(4, 'Admin', 'ROLE_ADMIN');

insert into `group`(`id`, `name`, `description`) values(1, 'Tổng hợp', 'Tổng hợp những bài viết về chủ đề phượt.');
insert into `group`(`id`, `name`, `description`) values(2, 'Bắc', 'Những bài viết, hình ảnh về các địa điểm phượt miền Bắc.');
insert into `group`(`id`, `name`, `description`) values(3, 'Trung', 'Miền Trung đầy nắng và gió.');
insert into `group`(`id`, `name`, `description`) values(4, 'Nam', 'Miền Nam thân thương.');
insert into `group`(`id`, `name`, `description`) values(5, 'Tâm sự, chia sẻ kinh nghiệm', 'Cùng chia sẻ chuyến đi, kinh nghiệm phượt.');
insert into `group`(`id`, `name`, `description`) values(6, 'Tìm kiếm đồng đội', 'Bạn không muốn phượt một mình? Hãy kiếm đồng đội.');

-- delimiter $$
-- create function deleteLikes(id bigint) returns boolean
-- begin

-- end

-- $$
-- delimiter ;
-- DELIMITER //
-- create event delete_event
-- on schedule at current_timestamp + interval 1 day
-- on completion preserve
-- do begin 
-- 	delete from forum.notifications where `date` < date_sub(now(), interval 7 day) and readed = true;
-- end//
-- DELIMITER ;
-- set global event_scheduler = on;

-- alter table users add column date timestamp default current_timestamp() after avatar

-- alter table posts add constraint fk_posts foreign key(user_name) references users(user_name);
-- alter table comments add constraint fk_comments_user foreign key(user_name) references users(user_name);
-- alter table comments add constraint fk_comments_post foreign key(post_id) references posts(post_id);
-- alter table likes add constraint fk_likes_post foreign key(post_id) references posts(post_id);
-- alter table likes add constraint fk_likes_cmt foreign key(cmt_id) references comments(comment_id);
-- alter table likes add constraint fk_user foreign key(user_name) references users(user_name);
-- alter table notifications add constraint fk_from foreign key(from_user) references users(user_name);
-- alter table notifications add constraint fk_to foreign key(to_user) references users(user_name);





-- create table if not exists images(
-- 	image_id int not null auto_increment,
--     link text not null,
--     post int not null,
-- 	constraint pk_images primary key(image_id)
-- );


-- alter table users add constraint fk_users_add foreign key(anhdaidien) references images(image_id);
-- alter table images add constraint fk_images_post foreign key(post) references posts(post_id);
-- create table if not exists likes(
--     user_name char(13) not null,
--     post_id int not null,
--     time_like datetime,
--     constraint fk_likes_user foreign key(user_name) references users(user_name),
--     constraint fk_likes_post foreign key(post_id) references posts(post_id)
-- 
-- create table if not exists notifications(
-- 	notif_id int not null auto_increment,
--     content text not null,
--     user_name char(13) not null,
--     time_notif datetime,
--     constraint pk_notifications primary key(notif_id),
--     constraint fk_notifications_user foreign key(user_name) references users(user_name)
-- );
