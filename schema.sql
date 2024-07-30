CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS question (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        ownerId BIGINT,
                                        body TEXT NOT NULL,
                                        answer0 TEXT,
                                        answer1 TEXT,
                                        answer2 TEXT,
                                        answer3 TEXT,
                                        answer_index INT,
                                        FOREIGN KEY(ownerId) REFERENCES user(id),
    CHECK(answer_index < 4 AND answer_index >= 0)
    );
