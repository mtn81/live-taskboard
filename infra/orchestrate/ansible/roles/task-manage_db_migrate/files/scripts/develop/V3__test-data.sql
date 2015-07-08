insert into members (member_id) values ('taro');
insert into groups (group_id, owner_member_id, name, description) values ('test-group', 'taro', 'テストグループ', 'テスト用のグループ');
insert into groups_members (group_id, member_id) values ('test-group', 'taro');
