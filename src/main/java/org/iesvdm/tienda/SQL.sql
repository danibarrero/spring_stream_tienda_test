use tienda;

-- 1
select nombre, precio from producto;

-- 2
select nombre, round(precio * 1.08, 2) from producto;

-- 3
select upper(nombre), precio from producto;

-- 4
select nombre, upper(substring(nombre, 1, 2)) as iniciales from fabricante;

-- 5
select distinct codigo_fabricante from producto;

-- 6
select nombre from fabricante order by nombre desc;

-- 7
select nombre, precio from producto order by nombre asc, precio desc;

-- 8
select nombre from fabricante limit 5;

-- 9
select nombre from fabricante limit 2 offset 3;

-- 10
select nombre, precio from producto order by precio asc limit 1;

-- 11
select nombre, precio from producto order by precio desc limit 1;

-- 12
select nombre from producto where codigo_fabricante = 2;

-- 13
select nombre from producto where precio <= 120;

-- 14
select * from tienda.producto p where p.precio >= 400;

-- 15
select * from tienda.producto p where p.precio >= 80 and p.precio <= 300;

-- 16
select * from tienda.producto p where p.precio > 200 and codigo_fabricante = 6;

-- 17
select * from producto where codigo_fabricante in (1, 3, 5);

-- 18
select p.nombre, p.precio * 100 from producto as p;

-- 19
select nombre from fabricante where nombre like 's%';

-- 20
select * from producto where nombre like '%_ort_til%';

-- 21
select * from producto where producto.nombre like '%_onitor%' and producto.precio < 215;

-- 22
select * from producto where producto.precio >= 180 order by precio desc, nombre asc;

-- 23
select p.nombre, p.precio, f.nombre from producto as p join fabricante as f on
    p.codigo_fabricante = f.codigo order by f.nombre asc;

-- 24
select * from producto order by precio desc limit 1;

-- 25
select p.nombre from producto as p join fabricante as f on p.codigo_fabricante = f.codigo
    where p.precio > 200 and f.nombre like 'Crucial';

-- 26
select p.* from producto as p join fabricante as f on p.codigo_fabricante = f.codigo
    where f.nombre in ('Asus', 'Hewlett-Packard', 'Seagate');

-- 27
select p.nombre, p.precio from producto as p join fabricante as f on p.codigo_fabricante = f.codigo
    where p.precio >= 100 order by p.precio desc, p.codigo asc;

-- 28
select * from fabricante join producto on fabricante.codigo = producto.codigo_fabricante;

-- 29
select f.* from fabricante as f where f.codigo not in (select codigo_fabricante from producto);

-- 30
select count(*) as total_productos from producto;

-- 31
select count(distinct codigo_fabricante) from producto;

-- 32
select avg(producto.precio) from producto;

-- 33
select min(producto.precio) from producto;

-- 34
select sum(producto.precio) from producto;

-- 35
select count(p.codigo_fabricante) from producto p
    join tienda.fabricante f on f.codigo = p.codigo_fabricante
    where lower(f.nombre) = lower('asus');

-- 36
select avg(p.precio) from producto p join tienda.fabricante f on p.codigo_fabricante = f.codigo
    where f.nombre like 'Asus';

-- 37
select * from producto join tienda.fabricante on producto.codigo_fabricante = fabricante.codigo
    where fabricante.nombre = 'Crucial';

-- 38
select fabricante.nombre, count(*) from fabricante
    join producto on producto.codigo_fabricante = fabricante.codigo group by fabricante.codigo;

-- 39

-- 40

-- 41
select f.nombre from fabricante f join producto p on f.codigo = p.codigo_fabricante
    group by f.codigo, f.nombre having count(p.codigo) >= 2;

-- 42
select f.nombre, count(p.codigo_fabricante) as numProd from fabricante f join producto p
    on f.codigo = p.codigo_fabricante where p.precio >= 220 group by f.nombre order by numProd desc;

-- 43
select f.nombre from fabricante f join producto p
    on f.codigo = p.codigo_fabricante group by f.nombre having sum(p.precio) > 1000;

-- 44
select f.nombre from fabricante f join producto p
    on f.codigo = p.codigo_fabricante group by f.nombre having sum(p.precio) > 1000 order by sum(p.precio) asc;

-- 45
select p.nombre, p.precio, f.nombre from producto p join fabricante f
    on p.codigo_fabricante = f.codigo where p.precio = (select max(precio) from producto
    where codigo_fabricante = f.codigo) order by f.nombre asc;

-- 46
select p.nombre, p.precio, f.nombre from producto p join fabricante f
    on p.codigo_fabricante = f.codigo where p.precio >= (select avg(precio) from producto
    where codigo_fabricante = f.codigo) order by f.nombre asc, p.precio desc;
