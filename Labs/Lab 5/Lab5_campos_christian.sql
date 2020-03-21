
 --- Find the pid of parts with cost lower than 10 

SELECT p.pid
From  parts p , catalog c
WHERE p.pid = c.pid AND c.cost < 10;

--- Find the name of parts with cost lower than 10

SELECT p.pname
FROM parts p , catalog c 
WHERE p.pid = c.pid AND c.cost < 10;

---Find the address of the suppliers who supply \Fire Hydrant Cap"

SELECT s.address
FROM suppliers s , parts p , catalog c
WHERE s.sid = c.sid AND c.pid = p.pid AND p.pname = 'Fire Hydrant Cap';

---Find the name of the suppliers who supply green parts

SELECT s.sname 
FROM suppliers s , parts p , catalog c
WHERE s.sid = c.sid AND c.pid = p.pid AND p.color = 'Green';

---For each supplier, list the supplier's name along with all parts' 
---name that it supply

SELECT s.sname , string_agg(DISTINCT p.pname,',') as parts
FROM suppliers s, parts p , catalog c
GROUP BY  s.sname 