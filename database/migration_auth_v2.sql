-- Migración del módulo de autenticación (MySQL 8+)
-- Ejecutar una sola vez sobre BD_Metas_personales después de respaldar la base de datos.
USE BD_Metas_personales;

ALTER TABLE Usuario
    ADD COLUMN failed_login_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN locked_until DATETIME NULL DEFAULT NULL;

CREATE TABLE PasswordResetToken (
    reset_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    token_hash CHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    used_at DATETIME NULL,
    PRIMARY KEY (reset_id),
    UNIQUE KEY uq_password_reset_token_hash (token_hash),
    KEY idx_password_reset_user (user_id),
    CONSTRAINT fk_password_reset_usuario
        FOREIGN KEY (user_id) REFERENCES Usuario(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
