-- 1. Count how many parts in NYC have more than 70 parts on hand
SELECT COUNT(*)
FROM part_nyc pnyc
WHERE  pnyc.on_hand > 70 ;

-- 2. Count how many total parts on hand, in both NYC and SFO, are Red
SELECT SUM(totalParts) FROM
(
SELECT SUM(pnyc.on_hand) as totalParts
FROM part_nyc pnyc,color c
WHERE pnyc.color = c.color_id AND c.color_name = 'Red'
UNION 
SELECT SUM(psfo.on_hand) 
FROM part_sfo psfo, color c
WHERE psfo.color = c.color_id AND c.color_name = 'Red' 
) AS Q ;

--3. List all the suppliers that have more total on hand parts in NYC than they do in SFO.
SELECT s.supplier_id , s.supplier_name 
FROM supplier s
WHERE 
(SELECT SUM(pnyc.on_hand)
FROM part_nyc pnyc
WHERE s.supplier_id = pnyc.supplier)
>
(SELECT SUM(psfo.on_hand)
FROM part_sfo psfo
WHERE s.supplier_id  = psfo.supplier )
ORDER BY s.supplier_id;
-- 4. List all suppliers that supply parts in NYC that aren't supplied by anyone in SFO.
SELECT distinct s.supplier_id , s.supplier_name 
FROM supplier s , part_nyc pnyc
WHERE  s.supplier_id = pnyc.supplier AND pnyc.part_number IN 
(
select pnyc1.part_number
from part_nyc pnyc1, supplier s
where s.supplier_id = pnyc1.supplier 
EXCEPT 
select psfo.part_number
from part_sfo psfo , supplier s
where s.supplier_id = psfo.supplier 
) 
ORDER BY s.supplier_id; 

-- 5. Update all of the NYC on hand values to on hand - 10.
UPDATE part_nyc  
SET on_hand = on_hand - 10 
WHERE on_hand >= 10;

-- 6. Delete all parts from NYC which have less than 30 parts on hand.
DELETE 
FROM part_nyc
WHERE on_hand < 30;