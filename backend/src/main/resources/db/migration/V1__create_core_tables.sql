CREATE TABLE app_user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(191) NOT NULL,
    password_hash VARCHAR(255) NULL,
    display_name VARCHAR(80) NOT NULL,
    avatar_url VARCHAR(500) NULL,
    bio VARCHAR(500) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_app_user_username UNIQUE (username),
    CONSTRAINT uk_app_user_email UNIQUE (email),
    CONSTRAINT chk_app_user_status CHECK (status IN ('ACTIVE', 'DISABLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE activity (
    id BIGINT NOT NULL AUTO_INCREMENT,
    creator_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(40) NOT NULL,
    city VARCHAR(80) NOT NULL,
    location_name VARCHAR(160) NOT NULL,
    location_address VARCHAR(255) NULL,
    cover_url VARCHAR(500) NULL,
    start_at DATETIME(6) NOT NULL,
    end_at DATETIME(6) NOT NULL,
    registration_deadline DATETIME(6) NOT NULL,
    capacity INT UNSIGNED NOT NULL,
    participant_count INT UNSIGNED NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_activity_creator FOREIGN KEY (creator_id) REFERENCES app_user (id),
    CONSTRAINT chk_activity_capacity CHECK (capacity > 0),
    CONSTRAINT chk_activity_participant_count CHECK (participant_count <= capacity),
    CONSTRAINT chk_activity_time CHECK (end_at > start_at),
    CONSTRAINT chk_activity_deadline CHECK (registration_deadline <= start_at),
    CONSTRAINT chk_activity_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'CANCELLED', 'FINISHED')),
    INDEX idx_activity_city_status_start (city, status, start_at),
    INDEX idx_activity_creator_created (creator_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE activity_participant (
    id BIGINT NOT NULL AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'REGISTERED',
    registered_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    withdrawn_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_participant_activity FOREIGN KEY (activity_id) REFERENCES activity (id),
    CONSTRAINT fk_participant_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT uk_participant_activity_user UNIQUE (activity_id, user_id),
    CONSTRAINT chk_participant_status CHECK (status IN ('REGISTERED', 'WITHDRAWN')),
    INDEX idx_participant_user_status (user_id, status),
    INDEX idx_participant_activity_status (activity_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

