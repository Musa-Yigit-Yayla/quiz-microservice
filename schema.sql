CREATE TABLE IF NOT EXISTS user (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS question (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        ownerId INT,
                                        body TEXT NOT NULL,
                                        answer0 TEXT NOT NULL,
                                        answer1 TEXT NOT NULL,
                                        answer2 TEXT NOT NULL,
                                        answer3 TEXT NOT NULL,
                                        answer_index INT,
                                        FOREIGN KEY(ownerId) REFERENCES user(id),
    CHECK(answer_index < 4 AND answer_index >= 0)
    );

CREATE TABLE IF NOT EXISTS test (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    ownerId INT,
                                    name TEXT NOT NULL,
                                    tag TEXT NOT NULL,
                                    UNIQUE(ownerId, name),
                                    FOREIGN KEY (ownerId) REFERENCES user(id)
    );

CREATE TABLE IF NOT EXISTS test_questions (
                                              testId INT,
                                              questionId INT,
                                              PRIMARY KEY (testId, questionId),
    FOREIGN KEY (testId) REFERENCES test(id),
    FOREIGN KEY (questionId) REFERENCES user(id)
    );

CREATE TABLE IF NOT EXISTS question_tags (
                                             questionId INT,
                                             tag TEXT,
                                             PRIMARY KEY (questionId, tag),
    FOREIGN KEY(questionId) REFERENCES question(id)
    );

CREATE TABLE IF NOT EXISTS test_add_request(
                                               testId INT,
                                               questionId INT,
                                               requesterId INT,
                                               PRIMARY KEY (testId, questionId, requesterId),
    FOREIGN KEY (testId) REFERENCES test(id),
    FOREIGN KEY (questionId) REFERENCES question(id),
    FOREIGN KEY (requesterId) REFERENCES user(id),
    CHECK (requesterId <> (SELECT ownerId FROM test WHERE test.id = testId))
    );
CREATE TABLE IF NOT EXISTS question_tag_request(
                                                   questionId INT,
                                                   tag TEXT,
                                                   requesterId INT,
                                                   PRIMARY KEY(questionId, requesterId, tag),
    FOREIGN KEY(questionId) REFERENCES question(id),
    FOREIGN KEY(requesterId) REFERENCES user(id),
    CHECK (requesterId <> (SELECT ownerId FROM question WHERE question.id = questionId))
    );

DROP TRIGGER IF EXISTS release_tag_requests;
CREATE TRIGGER release_tag_requests
    BEFORE DELETE ON question_tag_request
    FOR EACH ROW
BEGIN
    DELETE FROM question_tag_request
    WHERE questionId = OLD.questionId AND tag = OLD.tag
END;

DROP TRIGGER IF EXISTS release_add_requests;
CREATE TRIGGER release_add_requests
    BEFORE DELETE ON test_add_request
    FOR EACH ROW
BEGIN
    DELETE FROM test_add_request
    WHERE testId = OLD.testId AND questionId = OLD.questionId
END;

DROP TRIGGER IF EXISTS disallow_owner_qtr;
CREATE TRIGGER disallow_owner_qtr(
    BEFORE INSERT ON question_tag_request
);
