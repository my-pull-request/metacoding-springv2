insert into user_tb (username, password, email, roles) values ('ssar', '1234', 'ssar@metacoding.com', 'USER');
insert into user_tb (username, password, email, roles) values ('cos', '1234', 'cos@metacoding.com', 'USER');

insert into board_tb (title, content, user_id) values ('title 1', 'Spring Study 1', 1);
insert into board_tb (title, content, user_id) values ('title 2', 'Spring Study 2', 1);
insert into board_tb (title, content, user_id) values ('title 3', 'Spring Study 3', 1);
insert into board_tb (title, content, user_id) values ('title 4', 'Spring Study 4', 2);
insert into board_tb (title, content, user_id) values ('title 5', 'Spring Study 5', 2);

insert into reply_tb (comment, board_id, user_id) values ('reply 1', 4, 2);
insert into reply_tb (comment, board_id, user_id) values ('reply 2', 4, 2);
insert into reply_tb (comment, board_id, user_id) values ('reply 3', 4, 2);
insert into reply_tb (comment, board_id, user_id) values ('reply 4', 5, 1);
insert into reply_tb (comment, board_id, user_id) values ('reply 5', 5, 1);
