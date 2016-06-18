CREATE TABLE `brag`.`tb_game` (
  `game_id` INT NOT NULL,
  `winner_id` INT NOT NULL,
  `failer_id` INT NOT NULL,
  `game_data` JSON NULL,
  PRIMARY KEY (`game_id`));