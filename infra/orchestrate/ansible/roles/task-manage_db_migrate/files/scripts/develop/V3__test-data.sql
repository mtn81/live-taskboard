insert into members (member_id, name, type) values ('taro', 'タスク太郎', 'PROPER');
insert into members (member_id, name, type) values ('jiro', 'タスク次郎', 'PROPER');
insert into groups (group_id, owner_member_id, name, description) values ('test-group', 'taro', 'テストグループ', 'テスト用のグループ');
insert into groups_members (group_id, member_id, admin) values ('test-group', 'taro', true);
insert into groups_members (group_id, member_id, admin) values ('test-group', 'jiro', false);
insert into tasks (task_id, group_id, status, name, deadline, assigned) values ('t01', 'test-group', 'TODO', 'タスクA', '2015-01-02', 'm01');
insert into tasks (task_id, group_id, status, name, deadline, assigned) values ('t02', 'test-group', 'DOING', 'タスクB', '2015-01-02', 'm01');
insert into tasks (task_id, group_id, status, name, deadline, assigned) values ('t03', 'test-group', 'DONE', 'タスクC', '2015-01-02', 'm01');
