ALTER TABLE "user" ADD COLUMN role VARCHAR(50);
ALTER TABLE "user" RENAME COLUMN password_hash TO password;