CREATE TABLE IF NOT EXISTS movie (
    id bigint NOT NULL AUTO_INCREMENT,
    movie_name VARCHAR(255),
    movie_description VARCHAR(255),
    movie_director VARCHAR(255),
    movie_genre VARCHAR(255),
    video_link VARCHAR(255),
    PRIMARY KEY (id)
);

INSERT INTO movie VALUES (1, 'Леон', 'Осиротевшая девочка становится напарницей наемного убийцы. Культовый триллер с Жаном Рено и Натали Портман', 'Люк Бессон', 'боевик', 'https://rutube.ru/video/b9a0e1e3264f557d5cd6544e92adefea/'),
                         (2, 'Дэдпул', 'Уэйд Уилсон — наёмник. Будучи побочным продуктом программы вооружённых сил под названием ...', 'Тим Миллер', 'комедия', 'https://rutube.ru/video/062407889e3870e83ecc800cf78fdea2/'),
                         (3, '1+1', 'Аристократ на коляске нанимает в сиделки бывшего заключенного. Искрометная французская комедия с Омаром Си', 'Оливье Накаш', 'драма', 'https://rutube.ru/video/50d09cd2daa46a98172b8b7bb6846271/'),
                         (4, 'Джон Уик 4', 'Чтобы обрести свободу, киллер-изгой бросает вызов Правлению кланов.', 'Чад Стахелски', 'боевик', 'https://rutube.ru/video/512cdad0fba7c3e72da03e765f996deb/');

CREATE TABLE IF NOT EXISTS user (
                                     id bigint NOT NULL AUTO_INCREMENT,
                                     registration_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     email VARCHAR(255),
                                     first_name VARCHAR(255),
                                     last_name VARCHAR(255),
                                     username VARCHAR(255),
                                     password VARCHAR(255),
                                     role VARCHAR(255),
                                     PRIMARY KEY (id)
);

INSERT INTO user VALUES (1, '2024-11-14 15:39:17', 'user1@gmail.com', 'Иван', 'Иванов', 'user1', '$2a$10$laxheQ1oMKCxxKKpiDYRJ.JgwxlM7fpmBFUdyGwVIz2LBUhAT3gh.', 'USER'),
                         (2, '2024-11-15 15:39:17', 'user2@gmail.com', 'Александра', 'Александрова', 'user2', '$2a$10$1eTDFDGCkrb/TGp.YFfqs.ZZlnyn2jBkUhi8xl0ZtmCYfLV4owckC', 'USER'),
                         (3, '2024-11-16 15:39:17', 'user3@gmail.com', 'Петр', 'Петров', 'user3', '$2a$10$UEAX4Z9NEflcC/ABs3EiGuvyJD6GQu9kMi2vJNOrTlXbn.p4GL2DW', 'USER'),
                         (4, '2024-11-17 15:39:17', 'user4@gmail.com', 'Семен', 'Семенов', 'user4', '$2a$10$4ZXOSRYFRA7YyPF7J9u8sOv40lZmV5Uw.GequrDlmmkHNw3Nri4mm', 'USER');

CREATE TABLE IF NOT EXISTS review (
                                    id bigint NOT NULL AUTO_INCREMENT,
                                    date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    text VARCHAR(255),
                                    user_id bigint,
                                    movie_id bigint,
                                    rating int,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (user_id) REFERENCES user(id),
                                    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
);

INSERT INTO review VALUES (1, '2024-11-14 19:39:17', 'Отличный фильм', 1, 1, 9),
                        (2, '2024-11-15 20:39:17', 'Ужасный фильм', 2, 1, 2),
                        (3, '2024-11-16 21:39:17', 'Скучный фильм', 3, 1, 5),
                        (4, '2024-11-17 22:39:17', 'Понравилось', 4, 2, 10);

CREATE TABLE IF NOT EXISTS favorite (
                                        id bigint NOT NULL AUTO_INCREMENT,
                                        user_id bigint,
                                        movie_id bigint,
                                        PRIMARY KEY (id),
                                        FOREIGN KEY (user_id) REFERENCES user(id),
                                        FOREIGN KEY (movie_id) REFERENCES movie(id)
);

INSERT INTO favorite VALUES (1, 1, 1),
                            (2, 1, 2),
                            (3, 1, 3);

