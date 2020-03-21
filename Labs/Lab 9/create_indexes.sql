CREATE INDEX part_nyci
ON part_nyc
USING BTREE (on_hand);


CREATE INDEX part_nyc_part_number
ON part_nyc
USING BTREE (part_number);