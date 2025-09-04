insert into user_tb (username, password, email, roles,created_at) values ('ssar', '$2y$04$0yDwb5VSijD7z8Wj3lFlwu50bcZRkUwqZQWekol9g.h1eCEto02VK', 'ssar@metacoding.com', 'USER',now());
insert into user_tb (username, password, email, roles,created_at) values ('cos', '$2y$04$0yDwb5VSijD7z8Wj3lFlwu50bcZRkUwqZQWekol9g.h1eCEto02VK', 'cos@metacoding.com', 'ADMIN',now());

insert into board_tb (title, content, user_id,created_at) values ('title 1', 'Spring Study 1', 1,now());
insert into board_tb (title, content, user_id,created_at) values ('title 2', 'Spring Study 2', 1,now());
insert into board_tb (title, content, user_id,created_at) values ('title 3', 'Spring Study 3', 1,now());
insert into board_tb (title, content, user_id,created_at) values ('title 4', 'Spring Study 4', 2,now());
insert into board_tb (title, content, user_id,created_at) values ('title 5', 'Spring Study 5', 2,now());

insert into reply_tb (comment, board_id, user_id,created_at) values ('reply 1', 4, 2,now());
insert into reply_tb (comment, board_id, user_id,created_at) values ('reply 2', 4, 2,now());
insert into reply_tb (comment, board_id, user_id,created_at) values ('reply 3', 4, 2,now());
insert into reply_tb (comment, board_id, user_id,created_at) values ('reply 4', 5, 1,now());
insert into reply_tb (comment, board_id, user_id,created_at) values ('reply 5', 5, 1,now());
