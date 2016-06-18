CREATE TABLE `brag`.`tb_user_data` (
  `id` INT NOT NULL,
  `login_type` INT NOT NULL,
  `login_number` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `sex` INT NULL DEFAULT 1,
  `age` INT NULL DEFAULT 1,
  `win_count` INT NULL DEFAULT 1,
  `fail_count` INT NULL DEFAULT 1,
  `equal_count` INT NULL DEFAULT 1,
  `brag_count` INT NULL DEFAULT 1,
  `month_win` INT NULL DEFAULT 1,
  `month_brag` INT NULL DEFAULT 1,
  `month_fail` INT NULL DEFAULT 1,
  `month_equal` INT NULL DEFAULT 1,
  `weak_win` INT NULL DEFAULT 1,
  `weak_brag` INT NULL DEFAULT 1,
  `weak_fail` INT NULL DEFAULT 1,
  `weak_equal` INT NULL DEFAULT 1,
  `name_changed` INT NULL DEFAULT 1,
  `picture_changed` INT NULL DEFAULT 1,
  `last_login_month` INT NULL DEFAULT 1,
  `last_login_week` INT NULL DEFAULT 1,
  PRIMARY KEY (`id`));
