--changeset ovoshchko:001
--runOnChange: true
INSERT INTO banks (name, limit_amount) VALUES
('Тинькофф', 5000),
('Банк Санкт-Петербург', 3000),
('Альфа', 2000);

--changeset ovoshchko:002
--runOnChange: true
INSERT INTO cards (bank_id, name) VALUES
(1, 'Тинькофф МИР'),
(2, 'Банк Санкт-Петербург'),
(3, 'Альфа Кредитка'),
(3, 'Альфа МИР');

--changeset ovoshchko:003
--runOnChange: true
INSERT INTO categories (name) VALUES
('Рестораны'),
('Дом и Ремонт'),
('ЖД билеты'),
('Заправки'),
('Остальное');

--changeset ovoshchko:004
--runOnChange: true
INSERT INTO cashbacks (card_id, category_id, percent, start_date, end_date) VALUES
(1, 1, 0.05, '2024-05-01 00:00:00', '2024-06-01 00:00:00'),
(1, 2, 0.05, '2024-05-01 00:00:00', '2024-06-01 00:00:00'),
(1, 5, 0.01, '2024-05-01 00:00:00', '9999-12-31 23:59:59'),
(2, 3, 0.07, '2024-05-01 00:00:00', '2024-06-01 00:00:00'),
(2, 5, 0.015, '2024-05-01 00:00:00', '9999-12-31 23:59:59'),
(3, 1, 0.05, '2024-05-01 00:00:00', '2024-06-01 00:00:00'),
(3, 4, 0.05, '2024-05-01 00:00:00', '9999-12-31 23:59:59'),
(3, 5, 0.01, '2024-05-01 00:00:00', '9999-12-31 23:59:59'),
(4, 4, 0.05, '2024-05-01 00:00:00', '2024-06-01 00:00:00'),
(4, 3, 0.03, '2024-05-01 00:00:00', '2024-06-01 00:00:00'),
(4, 5, 0.01, '2024-05-01 00:00:00', '9999-12-31 23:59:59');

--changeset ovoshchko:005
--runOnChange: true
INSERT INTO transactions (card_id, category_id, amount, transaction_date) VALUES
(1, 1, 2000, '2024-05-15 12:30:00'),
(1, 2, 1500, '2024-05-20 14:45:00'),
(2, 3, 500, '2024-05-10 10:00:00'),
(2, 5, 300, '2024-05-25 08:00:00'),
(3, 4, 1000, '2024-05-12 16:20:00'),
(3, 5, 700, '2024-05-18 18:30:00'),
(4, 4, 1200, '2024-05-22 20:15:00'),
(4, 3, 800, '2024-05-05 11:10:00'),
(4, 5, 600, '2024-05-30 09:45:00');
