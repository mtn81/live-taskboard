insert into members (member_id) values ('taro');
insert into groups (group_id, owner_member_id, name, description, state) values ('test-group', 'taro', 'テストグループ', 'テスト用のグループ', 'AVAILABLE');
insert into groups_members (group_id, member_id, admin) values ('test-group', 'taro', true);
