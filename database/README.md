# Database

## Overview
MySQL 8.4 database for the Server Patch Tracking Dashboard.

## Database Name
`server_patch_dashboard`

## Files
- `schema.sql` — Creates the database and tables (servers, patches, patch_events)
- `seed.sql` — Inserts realistic sample data

## Setup
```bash
mysql -u root -p < schema.sql
mysql -u root -p < seed.sql
```

## Tables
- **servers** — Tracked server inventory
- **patches** — Known patches to apply
- **patch_events** — Records of patch application per server
