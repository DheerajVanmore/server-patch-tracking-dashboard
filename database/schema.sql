-- ============================================================
-- Server Patch Tracking Dashboard - Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS server_patch_dashboard
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE server_patch_dashboard;

-- -----------------------------------------------------------
-- Servers
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS servers (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    hostname        VARCHAR(255)    NOT NULL,
    ip_address      VARCHAR(45)     NOT NULL,
    os              VARCHAR(100)    NOT NULL,
    environment     VARCHAR(50)     NOT NULL,
    owner_team      VARCHAR(100)    NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    last_checked    DATETIME        NULL,
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_servers_hostname (hostname),
    INDEX idx_servers_environment (environment),
    INDEX idx_servers_status (status)
) ENGINE=InnoDB;

-- -----------------------------------------------------------
-- Patches
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS patches (
    id                  BIGINT          AUTO_INCREMENT PRIMARY KEY,
    patch_name          VARCHAR(255)    NOT NULL,
    patch_identifier    VARCHAR(100)    NOT NULL,
    version             VARCHAR(50)     NOT NULL,
    release_date        DATE            NOT NULL,
    severity            VARCHAR(20)     NOT NULL,
    description         TEXT            NULL,
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_patches_severity (severity),
    INDEX idx_patches_identifier (patch_identifier)
) ENGINE=InnoDB;

-- -----------------------------------------------------------
-- Patch Events
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS patch_events (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    server_id       BIGINT          NOT NULL,
    patch_id        BIGINT          NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    event_date      DATETIME        NOT NULL,
    failure_reason  TEXT            NULL,
    remarks         TEXT            NULL,
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_patch_events_server
        FOREIGN KEY (server_id) REFERENCES servers(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_patch_events_patch
        FOREIGN KEY (patch_id) REFERENCES patches(id)
        ON DELETE CASCADE,
    INDEX idx_patch_events_status (status),
    INDEX idx_patch_events_server (server_id),
    INDEX idx_patch_events_patch (patch_id)
) ENGINE=InnoDB;
