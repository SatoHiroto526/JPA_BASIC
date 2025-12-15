CREATE TABLE jakartaee.account (
    userid INTEGER NOT NULL,
    username VARCHAR(50) NOT NULL,
    PRIMARY KEY(userid)
);

CREATE TABLE jakartaee.todo (
    id INTEGER NOT NULL,
    todo VARCHAR(50) NOT NULL,
    priority VARCHAR(10),
    detail VARCHAR(200),
    userid INTEGER NOT NULL,
    PRIMARY KEY(id)
);

BEGIN;
TRUNCATE TABLE jakartaee.account;
INSERT INTO jakartaee.account(userid, username) VALUES(1, '田中');
INSERT INTO jakartaee.account(userid, username) VALUES(2, '鈴木');
END;

BEGIN;
TRUNCATE TABLE jakartaee.todo;
INSERT INTO jakartaee.todo(id, todo, priority, detail,userid) VALUES(1, 'JPAの勉強', '高', 'JakartaEEにおけるJPAの実装方法の勉強', 1);
INSERT INTO jakartaee.todo(id, todo, priority, detail,userid) VALUES(2, 'JSFの勉強', '高', 'JakartaEEにおけるJSFの実装方法の勉強', 1);
INSERT INTO jakartaee.todo(id, todo, priority, detail,userid) VALUES(3, 'JAX-RSの勉強', '中', 'JakartaEEにおけるJAX-RSの実装方法の勉強', 1);
INSERT INTO jakartaee.todo(id, todo, priority, detail,userid) VALUES(4, 'コンテナの勉強', '中', 'Docker&Podmanの勉強', 2);
INSERT INTO jakartaee.todo(id, todo, priority, detail,userid) VALUES(5, 'k8sの勉強', '中', 'kubernetesの勉強', 2);
INSERT INTO jakartaee.todo(id, todo, priority, detail,userid) VALUES(6, 'EJB', '低', 'JakartaEEにおけるEJBの実装方法勉強', 2);
END;