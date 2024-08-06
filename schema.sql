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
                                        difficulty VARCHAR(10),
                                        FOREIGN KEY(ownerId) REFERENCES user(id),
    CHECK(answer_index < 4 AND answer_index >= 0),
    CHECK(difficulty IN ('easy', 'medium', 'hard', 'extreme'))
);

CREATE TABLE IF NOT EXISTS test (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    ownerId INT,
                                    name VARCHAR(20) NOT NULL,
                                    tag VARCHAR(30) NOT NULL,
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
                                             tag VARCHAR(30),
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
    FOREIGN KEY (requesterId) REFERENCES user(id)
    );
CREATE TABLE IF NOT EXISTS question_tag_request(
                                                   questionId INT,
                                                   tag VARCHAR(30),
                                                   requesterId INT,
                                                   PRIMARY KEY(questionId, requesterId, tag),
    FOREIGN KEY(questionId) REFERENCES question(id),
    FOREIGN KEY(requesterId) REFERENCES user(id)
);

delimiter ^;

DROP TRIGGER IF EXISTS release_tag_requests^;
CREATE TRIGGER release_tag_requests
    BEFORE INSERT ON question_tags
    FOR EACH ROW
BEGIN
    DELETE FROM question_tag_request
    WHERE questionId = NEW.questionId AND tag = NEW.tag;
END^;

DROP TRIGGER IF EXISTS release_add_requests^;
CREATE TRIGGER release_add_requests
    BEFORE INSERT ON test_questions
    FOR EACH ROW
BEGIN
    DELETE FROM test_add_request
    WHERE testId = NEW.testId AND questionId = NEW.questionId;
END^;

DROP TRIGGER IF EXISTS del_question_trigger^;
CREATE TRIGGER del_question_trigger
    BEFORE DELETE ON question
    FOR EACH ROW
BEGIN
    DELETE FROM test_questions
    WHERE questionId = OLD.id;

    DELETE FROM question_tags
    WHERE questionId = OLD.id;

    DELETE FROM test_add_request
    WHERE questionId = OLD.id;

    DELETE FROM question_tag_request
    WHERE questionId = OLD.id;
END^;

DROP TRIGGER IF EXISTS del_test_trigger^;
CREATE TRIGGER del_test_trigger
    BEFORE DELETE ON test
    FOR EACH ROW
BEGIN
    DELETE FROM test_questions
    WHERE testId = OLD.id;

    DELETE FROM test_add_request
    WHERE testId = OLD.id;
END^;

CREATE OR REPLACE VIEW difficulty_distributions AS (
    SELECT COUNT(id) AS question_count, difficulty
    FROM question
    GROUP BY difficulty
)^;

CREATE OR REPLACE VIEW question_tag_distributions AS (
       SELECT COUNT(id) AS question_count, tag
       FROM question JOIN question_tags ON (question.id = question_tags.questionId)
       GROUP BY tag
)^;

CREATE OR REPLACE VIEW test_tag_distributions AS (
    SELECT COUNT(id) AS test_count, tag
    FROM test
    GROUP BY tag
)^;

