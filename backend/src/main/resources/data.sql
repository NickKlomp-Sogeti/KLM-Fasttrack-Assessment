INSERT INTO employee (employee_id, name) VALUES ('klm012345', 'Nick');
INSERT INTO employee (employee_id, name) VALUES ('klm012346', 'Anna');
INSERT INTO employee (employee_id, name) VALUES ('klm012347', 'John');
INSERT INTO employee (employee_id, name) VALUES ('klm012348', 'Maria');
INSERT INTO employee (employee_id, name) VALUES ('klm012349', 'Sophie');

INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('0f8fad5b-d9cb-469f-a165-70867728950e', 'Summerholidays',
        '2022-08-02T08:00:00+00:00', '2022-08-16T08:00:00+00:00',
        'SCHEDULED', 'klm012345');

INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('1a2b3c4d-1111-2222-3333-444455556666', 'Winter Break',
        '2022-12-20T08:00:00+00:00', '2023-01-05T08:00:00+00:00',
        'DRAFT', 'klm012346');

INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('2b3c4d5e-7777-8888-9999-000011112222', 'Spring Trip',
        '2023-04-10T08:00:00+00:00', '2023-04-17T08:00:00+00:00',
        'REQUESTED', 'klm012347');

INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('3c4d5e6f-aaaa-bbbb-cccc-ddddeeeeffff', 'Autumn Getaway',
        '2023-10-01T08:00:00+00:00', '2023-10-10T08:00:00+00:00',
        'ARCHIVED', 'klm012348');

-- FIXED UUIDs (only 0-9 and a-f)
INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('4d5e6f70-eeee-ffff-aaaa-bbbbbbbbbbbb', 'Short Break',
        '2023-05-15T08:00:00+00:00', '2023-05-18T08:00:00+00:00',
        'DRAFT', 'klm012349');

INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('5e6f7a80-1111-2222-3333-444444444444', 'Family Visit',
        '2024-07-01T08:00:00+00:00', '2024-07-14T08:00:00+00:00',
        'REQUESTED', 'klm012345');

INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('6f7a8b90-aaaa-bbbb-cccc-dddddddddddd', 'Conference',
        '2024-09-10T08:00:00+00:00', '2024-09-12T08:00:00+00:00',
        'SCHEDULED', 'klm012346');

INSERT INTO holiday (holiday_id, holiday_label, start_of_holiday, end_of_holiday, status, employee_employee_id)
VALUES ('7a8b9c00-1234-5678-9abc-def012345678', 'Christmas',
        '2025-12-24T08:00:00+00:00', '2026-01-02T08:00:00+00:00',
        'ARCHIVED', 'klm012347');
