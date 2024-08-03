CREATE TABLE IF NOT EXISTS user (
                                    id LONG PRIMARY KEY AUTO_INCREMENT,
                                    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS question (
                                        id LONG PRIMARY KEY AUTO_INCREMENT,
                                        ownerId LONG,
                                        body TEXT NOT NULL,
                                        answer0 TEXT,
                                        answer1 TEXT,
                                        answer2 TEXT,
                                        answer3 TEXT,
                                        answer_index INT,
                                        FOREIGN KEY(ownerId) REFERENCES user(id),
    CHECK(answer_index < 4 AND answer_index >= 0)
    );

CREATE TABLE IF NOT EXISTS test (
                                    id LONG PRIMARY KEY AUTO_INCREMENT,
                                    ownerId LONG,
                                    name TEXT NOT NULL,
                                    tag TEXT NOT NULL,
                                    FOREIGN KEY (ownerId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS test_questions (
    testId LONG,
    questionId LONG,
    PRIMARY KEY (testId, questionId),
    FOREIGN KEY (testId) REFERENCES test(id),
    FOREIGN KEY (questionId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS question_tags (
    questionId LONG,
    tag TEXT,
    PRIMARY KEY (questionId, tag),
    FOREIGN KEY(questionId) REFERENCES question(id)
);

CREATE TABLE IF NOT EXISTS test_add_request(
    testId LONG,
    questionId LONG,
    requesterId LONG,
    PRIMARY KEY (testId, questionId, requesterId),
    FOREIGN KEY (testId) REFERENCES test(id),
    FOREIGN KEY (questionId) REFERENCES question(id),
    FOREIGN KEY (requesterId) REFERENCES user(id),
    CHECK (requesterId <> (SELECT ownerId FROM test WHERE test.id = testId))
);
CREATE TABLE IF NOT EXISTS question_tag_request(
    questionId LONG,
    tag TEXT,
    requesterId LONG,
    PRIMARY KEY(questionId, requesterId, tag),
    FOREIGN KEY(questionId) REFERENCES question(id),
    FOREIGN KEY(requesterId) REFERENCES user(id),
    CHECK (requesterId <> (SELECT ownerId FROM question WHERE question.id = questionId))
);

