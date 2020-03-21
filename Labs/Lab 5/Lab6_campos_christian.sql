select s.sname , count(s.sid) as total_Parts
from catalog c, suppliers s
where s.sid = c.sid
group by s.sname ;

select s.sname , count(s.sid) as total_Parts
from catalog c, suppliers s
where  s.sid = c.sid
group by s.sname 
having count(s.sid) >= 3;

select s.sname , count(s.sid) as total_Parts
from catalog c , suppliers s , parts p
where s.sid = c.sid and p.pid = c.pid 
group by s.sname 
having every(p.color = 'Green');


select s.sname , MAX(c.cost) as Max_cost
from catalog c , suppliers s , parts p
where s.sid = c.sid and p.pid = c.pid and p.color in ('Green','Red') 
group by s.sname  
having count(distinct p.color ) = 2;
