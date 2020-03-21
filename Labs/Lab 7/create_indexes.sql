-- By default , postgres index uses B-Tree indexing

------ Suppliers indexes ----------
CREATE INDEX supplier_id_index
ON supplier (supplier_id);

CREATE INDEX supplier_name_index
ON supplier (supplier_name);

---------- nyc and sfo Part numbers----------
CREATE INDEX part_nyc_number_index
ON part_nyc (part_number);

CREATE INDEX part_sfo_number_index
ON part_sfo (part_number);
---------- nyc and sfo Suppliers----
CREATE INDEX part_nyc_supplier_index
ON part_nyc (supplier);

CREATE INDEX part_sfo_supplier_index
ON part_sfo (supplier);
---------- nyc and sfo colors ----------
CREATE INDEX part_nyc_color_index
ON part_nyc (color);

CREATE INDEX part_sfo_color_index
ON part_sfo (color);
---------- nyc and sfo On_hand ----------
CREATE INDEX part_nyc_on_hand_index
ON part_nyc (on_hand);

CREATE INDEX part_sfo_on_hand_index
ON part_sfo (on_hand);

---------------- colors -----------------------------------
CREATE INDEX color_id_index
ON color(color_id);

CREATE INDEX color_name_index
ON color(color_name);