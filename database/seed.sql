-- ============================================================
-- Server Patch Tracking Dashboard - Seed Data
-- ============================================================

USE server_patch_dashboard;

-- -----------------------------------------------------------
-- Servers
-- -----------------------------------------------------------
INSERT INTO servers (hostname, ip_address, os, environment, owner_team, status, last_checked) VALUES
('WEB-PROD-01',   '10.0.1.10',  'Ubuntu 22.04 LTS',       'Production',  'Platform Engineering', 'ACTIVE',      '2026-09-20 08:00:00'),
('WEB-PROD-02',   '10.0.1.11',  'Ubuntu 22.04 LTS',       'Production',  'Platform Engineering', 'ACTIVE',      '2026-09-20 08:05:00'),
('DB-PROD-01',    '10.0.1.20',  'Ubuntu 24.04 LTS',       'Production',  'Database Team',        'ACTIVE',      '2026-09-19 22:00:00'),
('APP-PROD-01',   '10.0.1.30',  'Windows Server 2022',    'Production',  'Application Services', 'ACTIVE',      '2026-09-20 09:00:00'),
('APP-PROD-02',   '10.0.1.31',  'Windows Server 2022',    'Production',  'Application Services', 'MAINTENANCE', '2026-09-18 14:00:00'),
('WEB-STG-01',    '10.0.2.10',  'Ubuntu 22.04 LTS',       'Staging',     'Platform Engineering', 'ACTIVE',      '2026-09-20 07:00:00'),
('DB-STG-01',     '10.0.2.20',  'Ubuntu 24.04 LTS',       'Staging',     'Database Team',        'ACTIVE',      '2026-09-19 21:00:00'),
('APP-STG-01',    '10.0.2.30',  'Windows Server 2022',    'Staging',     'Application Services', 'ACTIVE',      '2026-09-20 06:30:00'),
('DEV-01',        '10.0.3.10',  'Ubuntu 22.04 LTS',       'Development', 'DevOps',               'ACTIVE',      '2026-09-20 10:00:00'),
('DEV-02',        '10.0.3.11',  'Windows Server 2019',    'Development', 'DevOps',               'OFFLINE',     '2026-09-15 12:00:00'),
('CI-BUILD-01',   '10.0.3.50',  'Ubuntu 24.04 LTS',       'Development', 'DevOps',               'ACTIVE',      '2026-09-20 11:00:00'),
('MAIL-PROD-01',  '10.0.1.40',  'Windows Server 2022',    'Production',  'IT Operations',        'ACTIVE',      '2026-09-20 07:30:00');

-- -----------------------------------------------------------
-- Patches
-- -----------------------------------------------------------
INSERT INTO patches (patch_name, patch_identifier, version, release_date, severity, description) VALUES
('OpenSSL Security Update',          'CVE-2026-3456',  '3.1.7',   '2026-09-01', 'CRITICAL', 'Fixes remote code execution vulnerability in OpenSSL library.'),
('Linux Kernel Update',              'KERN-2026-09',   '6.8.12',  '2026-09-05', 'HIGH',     'Kernel update addressing privilege escalation and memory safety issues.'),
('Apache HTTP Server Patch',         'APACHE-2026-02', '2.4.62',  '2026-08-20', 'MEDIUM',   'Patch for HTTP request smuggling vulnerability.'),
('Windows Cumulative Update Sep 26', 'KB5040001',      '2026.09', '2026-09-10', 'HIGH',     'Monthly cumulative security update for Windows Server.'),
('MySQL Security Patch',             'MYSQL-2026-Q3',  '8.4.3',   '2026-08-15', 'HIGH',     'Addresses SQL injection vector and authentication bypass.'),
('Nginx Stability Update',           'NGINX-2026-03',  '1.26.2',  '2026-09-08', 'LOW',      'Minor stability improvements and connection handling fix.'),
('Java Runtime Patch',               'JDK-2026-09',    '21.0.5',  '2026-09-12', 'MEDIUM',   'Security patches for JDK 21 LTS including TLS improvements.'),
('SSH Daemon Hardening',             'SSHD-2026-01',   '9.8p2',   '2026-07-28', 'CRITICAL', 'Fixes authentication bypass in OpenSSH server.');

-- -----------------------------------------------------------
-- Patch Events
-- -----------------------------------------------------------
INSERT INTO patch_events (server_id, patch_id, status, event_date, failure_reason, remarks) VALUES
-- OpenSSL (CRITICAL) - mostly patched, one failed, one pending
(1, 1, 'PATCHED',     '2026-09-10 02:00:00', NULL, 'Applied during maintenance window.'),
(2, 1, 'PATCHED',     '2026-09-10 02:30:00', NULL, 'Applied during maintenance window.'),
(3, 1, 'PATCHED',     '2026-09-11 03:00:00', NULL, 'Applied successfully.'),
(6, 1, 'PATCHED',     '2026-09-09 01:00:00', NULL, 'Staging validated first.'),
(7, 1, 'PATCHED',     '2026-09-09 01:30:00', NULL, 'Staging validated.'),
(9, 1, 'PATCHED',     '2026-09-08 10:00:00', NULL, 'Dev environment patched first.'),
(11,1, 'FAILED',      '2026-09-12 04:00:00', 'Dependency conflict with build toolchain.', 'Needs manual intervention.'),
(5, 1, 'PENDING',     '2026-09-15 00:00:00', NULL, 'Server in maintenance, patch scheduled.'),

-- Kernel Update (HIGH) - mixed statuses
(1, 2, 'PATCHED',     '2026-09-12 03:00:00', NULL, 'Kernel update applied, reboot completed.'),
(2, 2, 'IN_PROGRESS', '2026-09-20 02:00:00', NULL, 'Reboot pending.'),
(3, 2, 'PATCHED',     '2026-09-13 03:30:00', NULL, 'Applied successfully.'),
(6, 2, 'PATCHED',     '2026-09-11 01:00:00', NULL, 'Staging kernel updated.'),
(9, 2, 'PATCHED',     '2026-09-10 09:00:00', NULL, 'Dev patched.'),
(11,2, 'PENDING',     '2026-09-18 00:00:00', NULL, 'Scheduled for next window.'),

-- Apache (MEDIUM) - on web servers
(1, 3, 'PATCHED',     '2026-09-05 02:00:00', NULL, 'Apache updated.'),
(2, 3, 'PATCHED',     '2026-09-05 02:30:00', NULL, 'Apache updated.'),
(6, 3, 'PATCHED',     '2026-09-04 01:00:00', NULL, 'Staging Apache updated.'),

-- Windows Cumulative (HIGH)
(4, 4, 'PATCHED',     '2026-09-15 04:00:00', NULL, 'Windows Update applied.'),
(5, 4, 'FAILED',      '2026-09-16 04:00:00', 'Update failed: insufficient disk space.', 'Disk cleanup required before retry.'),
(8, 4, 'PATCHED',     '2026-09-14 03:00:00', NULL, 'Staging Windows updated.'),
(10,4, 'EXEMPTED',    '2026-09-17 00:00:00', NULL, 'Server scheduled for decommission.'),
(12,4, 'PENDING',     '2026-09-22 00:00:00', NULL, 'Mail server update pending approval.'),

-- MySQL Security (HIGH)
(3, 5, 'PATCHED',     '2026-09-01 03:00:00', NULL, 'MySQL patched during DB maintenance.'),
(7, 5, 'PATCHED',     '2026-09-01 02:00:00', NULL, 'Staging DB patched.'),

-- Nginx (LOW)
(1, 6, 'PATCHED',     '2026-09-15 02:00:00', NULL, 'Nginx updated.'),
(6, 6, 'PENDING',     '2026-09-22 00:00:00', NULL, 'Low priority, scheduled.'),

-- Java Runtime (MEDIUM)
(4, 7, 'PATCHED',     '2026-09-18 04:00:00', NULL, 'JDK updated on app server.'),
(5, 7, 'PENDING',     '2026-09-22 00:00:00', NULL, 'Pending maintenance completion.'),
(8, 7, 'PATCHED',     '2026-09-17 03:00:00', NULL, 'Staging JDK updated.'),

-- SSH Hardening (CRITICAL) - important to patch everywhere
(1, 8, 'PATCHED',     '2026-08-05 02:00:00', NULL, 'SSH hardened.'),
(2, 8, 'PATCHED',     '2026-08-05 02:30:00', NULL, 'SSH hardened.'),
(3, 8, 'PATCHED',     '2026-08-06 03:00:00', NULL, 'SSH hardened.'),
(6, 8, 'PATCHED',     '2026-08-04 01:00:00', NULL, 'Staging SSH hardened.'),
(7, 8, 'PATCHED',     '2026-08-04 01:30:00', NULL, 'Staging SSH hardened.'),
(9, 8, 'PATCHED',     '2026-08-03 10:00:00', NULL, 'Dev SSH hardened.'),
(11,8, 'PATCHED',     '2026-08-07 04:00:00', NULL, 'CI server SSH hardened.'),
(4, 8, 'FAILED',      '2026-08-10 04:00:00', 'Windows SSH service conflict with existing config.', 'Manual SSH configuration needed.'),
(12,8, 'PENDING',     '2026-09-20 00:00:00', NULL, 'Critical - needs urgent attention.');
