INSERT INTO users (id, created_at, updated_at, email, password, full_name, role, active)
VALUES (
        'a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        'admin@odonto.com',
        '$2a$12$8KzV4Xm9Yq2w1e3r4t5y6u7i8o9p0a1b2c3d4e5f6g7h8i9j0k1l2',
        'Gabriel Mendes Admin',
        'ADMIN',
        TRUE
        );