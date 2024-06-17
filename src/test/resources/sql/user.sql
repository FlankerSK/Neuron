-- 添加用户
INSERT INTO users (username, password, role)
VALUES
    ('test1', '$2a$10$9y3amVDM9YPlIvOnLWEujuTTtFLMr4JIRnP/WdA3M3sYDruaBg3Qe', 'ROLE_ADMIN'),
    ('test2', '$2a$10$wphfu8lbfN5ka9JsHyCdtOucaxOPia6nnM0aewC883OTRk7oMuJn2', 'ROLE_USER');

INSERT INTO security_info (username, email)
VALUES
    ('test1', 'communicationGCY@outlook.com'),
    ('test2', 'communicationGCY@outlook.com');

INSERT INTO user_info (username) VALUES ('test1'), ('test2')
