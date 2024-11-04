package org.iesvdm.tienda;

import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.Locale.filter;
import static java.util.stream.Collectors.toMap;


@SpringBootTest
class TiendaApplicationTests {

	@Autowired
	FabricanteRepository fabRepo;
	
	@Autowired
	ProductoRepository prodRepo;

	@Test
	void testAllFabricante() {
		var listFabs = fabRepo.findAll();
		
		listFabs.forEach(f -> {
			System.out.println(">>"+f+ ":");
			f.getProductos().forEach(System.out::println);
		});
	}
	
	@Test
	void testAllProducto() {
		var listProds = prodRepo.findAll();

		listProds.forEach( p -> {
			System.out.println(">>"+p+":"+"\nProductos mismo fabricante "+ p.getFabricante());
			p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>"+pF));
		});
				
	}

	
	/**
	 * 1. Lista los nombres y los precios de todos los productos de la tabla producto
	 */
	@Test
	void test1() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.collect(toMap(Producto::getNombre, Producto::getPrecio));

		result.forEach((k, v) -> System.out.println(k + " - " + v));
		Assertions.assertEquals(11, result.size());
	}
	
	
	/**
	 * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares.
	 */
	@Test
	void test2() {
		var listProdsPrecioEuro = prodRepo.findAll();
		var listaProdsPrecidoDollar = listProdsPrecioEuro.stream()
									.map(p -> {
										Producto pDollar = new Producto();
										pDollar.setCodigo(p.getCodigo());
										pDollar.setNombre(p.getNombre());
										pDollar.setPrecio(p.getPrecio()*1.00);
										pDollar.setFabricante(p.getFabricante());
                                        return pDollar;
                                    })
									.toList();
		System.out.println("Lista productos con Euros: " );
		listProdsPrecioEuro.forEach(p -> System.out.println(p));

		System.out.println("Lista productos dollar: " );
		listaProdsPrecidoDollar.forEach(p -> System.out.println(p));
	}
	
	/**
	 * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
	 */
	@Test
	void test3() {
		var listProds = prodRepo.findAll();
		listProds.forEach(p -> p.setNombre(p.getNombre().toUpperCase()));
		listProds.forEach(p -> System.out.println("Nombre: " + p.getNombre() + ", Precio: " + p.getPrecio()));

		record NomPrecio(String nombre, double precio) {};
		listProds.stream().map(p -> new NomPrecio(p.getNombre().toUpperCase(), p.getPrecio())).toList();
	}
	
	/**
	 * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros
	 * caracteres del nombre del fabricante.
	 */
	@Test
	void test4() {
		var listFabs = fabRepo.findAll();
		record Tupla(String nombre, String iniciales) {}
		var result = listFabs.stream()
					.map(p -> new Tupla(p.getNombre(), p.getNombre().substring(0,2).toUpperCase()))
					.toList();

		System.out.println(result);
		result.forEach(tupla -> System.out.println("Nombre: " + tupla.nombre() + ", Iniciales: " + tupla.iniciales()));
	}
	
	/**
	 * 5. Lista el código de los fabricantes que tienen productos.
	 */
	@Test
	void test5() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> !f.getProductos().isEmpty())
				.toList();

		result.forEach(reult -> System.out.println("Código: " + reult.getCodigo()));
	}
	
	/**
	 * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
	 */
	@Test
	void test6() {
		var listFabs = fabRepo.findAll();
		var reult = listFabs.stream()
				//.sorted(comparing(Fabricante::getNombre).reversed())
				.sorted((f1,f2) -> f2.getNombre().compareTo(f1.getNombre()))
				.toList();

		reult.forEach(fabricante -> System.out.println(fabricante.getNombre()));
	}
	
	/**
	 * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
	 */
	@Test
	void test7() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(Producto::getNombre).thenComparing(Producto::getPrecio).reversed())
				.toList();

		result.forEach(x -> System.out.println(x.getNombre() + " - " + x.getPrecio()));
	}
	
	/**
	 * 8. Devuelve una lista con los 5 primeros fabricantes.
	 */
	@Test
	void test8() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.limit(5)
				.toList();

		System.out.println("5 primeros fabricantes");
		result.forEach(x -> System.out.println("Fabricante: " + x.getNombre()));
	}
	
	/**
	 * 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante.
	 * El cuarto fabricante también se debe incluir en la respuesta.
	 */
	@Test
	void test9() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.skip(3)
				.limit(2)
				.toList();

		System.out.println(result);

        Assertions.assertEquals(2, result.size());
		Assertions.assertEquals(result.getFirst().getNombre(), "Samsung");
	}
	
	/**
	 * 10. Lista el nombre y el precio del producto más barato
	 */
	@Test
	void test10() {
		var listProds = prodRepo.findAll();
		var productoBartos = listProds.stream()
				.sorted(comparing(Producto::getPrecio))
				.map(producto -> "Nombre: " + producto.getNombre() + ", Precio: " + producto.getPrecio())
				.limit(1)
				.findAny();

		Assertions.assertTrue(productoBartos.orElse("").contains("59.99"));
	}
	
	/**
	 * 11. Lista el nombre y el precio del producto más caro
	 */
	@Test
	void test11() {
		var listProds = prodRepo.findAll();
//		var result = listProds.stream()
//				.sorted(comparing((Producto producto) -> producto.getPrecio()).reversed())
//				.limit(1)
//				.toList();
//		result.forEach(producto -> System.out.println(producto.getNombre() + " - " + producto.getPrecio()));

		listProds.stream()
				.max(comparing(producto -> producto.getPrecio()))
				.ifPresentOrElse(producto -> System.out.println(producto.getNombre() + " - " + producto.getPrecio()),
						() -> System.out.println("Coleccion vacia"));
	}
	
	/**
	 * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
	 * 
	 */
	@Test
	void test12() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getCodigo() == 2)
				.map(Producto::getNombre)
				.toList();

		System.out.println(result);

		result.forEach(p -> System.out.println(p));

		Assertions.assertEquals(2, result.size());
		Assertions.assertTrue(result.contains("Portátil Yoga 520") && result.contains("Portátil Ideapd 320"));
	}
	
	/**
	 * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
	 */
	@Test
	void test13() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() <= 120)
				.map(Producto::getNombre)
				.toList();

		result.forEach(System.out::println);
	}
	
	/**
	 * 14. Lista los productos que tienen un precio mayor o igual a 400€.
	 */
	@Test
	void test14() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() >= 400 )
				.toList();

		System.out.println(result);
		Assertions.assertEquals(3, result.size());
	}
	
	/**
	 * 15. Lista todos los productos que tengan un precio entre 80€ y 300€. 
	 */
	@Test
	void test15() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() >= 80 && p.getPrecio() <= 300)
				.toList();

		System.out.println(result);
		Assertions.assertEquals(7, result.size());
	}
	
	/**
	 * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
	 */
	@Test
	void test16() {
		var listProds = prodRepo.findAll();
		var reult = listProds.stream()
				.filter(p -> p.getPrecio() > 200 && p.getCodigo() == 6)
				.toList();

		reult.forEach(System.out::println);
		Assertions.assertEquals(1, reult.size());
	}
	
	/**
	 * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
	 */
	@Test
	void test17() {
		var listProds = prodRepo.findAll();
		Set<Integer> codigo = Set.of(1, 3, 5);

		var result = listProds.stream()
				/*.filter(p -> p.getFabricante().getCodigo() == 1 ||
						p.getFabricante().getCodigo() == 3 ||
						p.getFabricante().getCodigo() == 5)*/
				.filter(p -> codigo.contains(p.getFabricante().getCodigo()))
				.toList();

		result.forEach(System.out::println);

		/*Assertions.assertTrue(result.get(0).getFabricante().getCodigo() == 1 ||
										result.get(0).getFabricante().getCodigo() == 3 ||
										result.get(0).getFabricante().getCodigo() == 5);*/
		Assertions.assertTrue(codigo.contains(result.getFirst().getFabricante().getCodigo()));
		Assertions.assertEquals(5, result.size());
	}
	
	/**
	 * 18. Lista el nombre y el precio de los productos en céntimos.
	 */
	@Test
	void test18() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.map(p -> p.getNombre() + " " + p.getPrecio() * 100)
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(12, result.size());
	}
	
	
	/**
	 * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
	 */
	@Test
	void test19() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				// .filter(p -> p.getNombre().substring(0, 1).equalsIgnoreCase("s"))
				.filter(p -> p.getNombre().toLowerCase().startsWith("s"))
				.toList();

		result.forEach(p -> System.out.println(p.getNombre()));
		Assertions.assertEquals(2, result.size());
	}
	
	/**
	 * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
	 */
	@Test
	void test20() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getNombre().matches(".*[P|p]ort[a|á]til.*"))
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(2, result.size());
	}
	
	/**
	 * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
	 */
	@Test
	void test21() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getNombre().contains("Monitor") && p.getPrecio() < 215)
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(1, result.size());
	}
	
	/**
	 * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
	 */
	@Test
	void test22() {
		var listProds = prodRepo.findAll();
		var reult = listProds.stream()
				.filter(producto -> producto.getPrecio() >= 180)
				.sorted(comparing(Producto::getPrecio, reverseOrder())
						.thenComparing(Producto::getNombre))
				.toList();

		reult.forEach(System.out::println);
		Assertions.assertEquals(7, reult.size());
	}
	
	/**
	 * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos. 
	 * Ordene el resultado por el nombre del fabricante, por orden alfabético.
	 */
	@Test
	void test23() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(producto -> producto.getFabricante().getNombre()))
				.map(producto -> producto.getNombre() +
					 producto.getPrecio() +
					 producto.getFabricante().getNombre())
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(11, result.size());
	}
	
	/**
	 * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
	 */
	@Test
	void test24() {
		var listProds = prodRepo.findAll();
		Producto p = listProds.stream()
				.sorted(comparing((producto) -> producto.getPrecio(), reverseOrder()))
				.findFirst()
				.orElse(null);

		System.out.println(p);
		Assertions.assertTrue(p.getPrecio() == 755);
	}
	
	/**
	 * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
	 */
	@Test
	void test25() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equalsIgnoreCase("Crucial") && p.getPrecio() >= 200)
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(755, result);
	}
	
	/**
	 * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
	 */
	@Test
	void test26() {
		var listProds = prodRepo.findAll();
		Set <String> lista = Set.of("Asus", "Hewlett-Packard", "Seagate");
		var result = listProds.stream()
				.filter(p -> lista.contains(p.getFabricante().getNombre()))
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(5, result.size());
		//Assertions.assertTrue(result.stream().anyMatch(p -> p.getFabricante().getNombre().equalsIgnoreCase("Seagate")));
		lista.forEach(s -> Assertions.assertTrue(result.stream()
						.anyMatch(p -> p.getFabricante().getNombre().equalsIgnoreCase(s))));
	}
	
	/**
	 * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
	 * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes campos a presentar y compensa mediante la inclusión de espacios en blanco.
	 * La salida debe quedar tabulada como sigue:

	Producto                Precio             Fabricante
	-----------------------------------------------------
	GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
	Portátil Yoga 520      |452.79            |Lenovo
	Portátil Ideapd 320    |359.64000000000004|Lenovo
	Monitor 27 LED Full HD |199.25190000000003|Asus

	 */
	@Test
	void test27() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(Producto::getPrecio, reverseOrder()).thenComparing(Producto::getNombre))
				.map(p -> p.getNombre() + "|" + p.getPrecio() + "|" + p.getFabricante().getNombre())
				.toList();

		System.out.println("Producto    Precio    Fabricante\n--------------------------------");
		result.forEach(System.out::println);

		Assertions.assertEquals(11, result.size());
	}
	
	/**
	 * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos. 
	 * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados. 
	 * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
	 * La salida debe queda como sigue:
Fabricante: Asus

            	Productos:
            	Monitor 27 LED Full HD
            	Monitor 24 LED Full HD

Fabricante: Lenovo

            	Productos:
            	Portátil Ideapd 320
            	Portátil Yoga 520

Fabricante: Hewlett-Packard

            	Productos:
            	Impresora HP Deskjet 3720
            	Impresora HP Laserjet Pro M26nw

Fabricante: Samsung

            	Productos:
            	Disco SSD 1 TB

Fabricante: Seagate

            	Productos:
            	Disco duro SATA3 1TB

Fabricante: Crucial

            	Productos:
            	GeForce GTX 1080 Xtreme
            	Memoria RAM DDR4 8GB

Fabricante: Gigabyte

            	Productos:
            	GeForce GTX 1050Ti

Fabricante: Huawei

            	Productos:


Fabricante: Xiaomi

            	Productos:

	 */
	@Test
	void test28() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.map(fabricante -> "Fabricante: " + fabricante.getNombre() + "\n\n" +
						           "Producto: " + "\n" +
						fabricante.getProductos()
						.stream()
						.map(producto -> producto.getNombre() + "\n")
						.collect(Collectors.joining()))
				.toList();

		result.forEach(System.out::println);
	}
	
	/**
	 * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
	 */
	@Test
	void test29() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> f.getProductos().isEmpty())
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(2, result.size());
	}
	
	/**
	 * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
	 */
	@Test
	void test30() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.count();

		System.out.println(result);
		Assertions.assertEquals(11, listProds.size());
	}

	
	/**
	 * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
	 */
	@Test
	void test31() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.map(f -> f.getFabricante())
				.distinct()
				.count();

		System.out.println(result);
		Assertions.assertEquals(9, listProds.size());
	}
	
	/**
	 * 32. Calcula la media del precio de todos los productos
	 */
	@Test
	void test32() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(p -> p.getPrecio()).average();

		System.out.println(result.orElse(0.0));
		Assertions.assertEquals(0, result.orElse(0.0));
	}
	
	/**
	 * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
	 */
	@Test
	void test33() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(p -> p.getPrecio()).min();

		System.out.println(result.orElse(0.0));
		Assertions.assertEquals(59.99, result.orElse(0.0));
	}
	
	/**
	 * 34. Calcula la suma de los precios de todos los productos.
	 */
	@Test
	void test34() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(p -> p.getPrecio()).sum();

		System.out.println(result);
		Assertions.assertEquals(2988.96, result);
	}
	
	/**
	 * 35. Calcula el número de productos que tiene el fabricante Asus.
	 */
	@Test
	void test35() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equalsIgnoreCase("Asus"))
				.count();

		System.out.println(result);
		Assertions.assertEquals(2, result);
	}
	
	/**
	 * 36. Calcula la media del precio de todos los productos del fabricante Asus.
	 */
	@Test
	void test36() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equalsIgnoreCase("Asus"))
						.mapToDouble(p -> p.getPrecio()).average().orElse(0.0);

		System.out.println(result);
		Assertions.assertEquals(223.995, result);
	}
	
	
	/**
	 * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial. 
	 *  Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 */
	@Test
	void test37() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equalsIgnoreCase("Crucial"))
				.map(p -> new Double[] {
						p.getPrecio(),
						p.getPrecio(),
						p.getPrecio(),
						0.0})
				.reduce((doubles, doubles2) -> new Double[]{
						Math.min(doubles[0], doubles2[0]),
						Math.max(doubles[1], doubles2[1]),
						doubles[2]+doubles2[2],
						doubles[3]+doubles2[3],})
				.orElse(new Double[]{});

		Double media = result[3]>0 ? result[2]/result[3]: 0.0;
		System.out.println("Valor mínimo: " + result[0]);
		System.out.println("Valor máximo: " + result[1]);
		System.out.println("Valor medio: " + media);
		System.out.println("Numero total: " + result[3]);
	}
	
	/**
	 * 38. Muestra el número total de productos que tiene cada uno de los fabricantes. 
	 * El listado también debe incluir los fabricantes que no tienen ningún producto. 
	 * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene. 
	 * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
	 * La salida debe queda como sigue:
	 
     Fabricante     #Productos
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
           Asus              2
         Lenovo              2
Hewlett-Packard              2
        Samsung              1
        Seagate              1
        Crucial              2
       Gigabyte              1
         Huawei              0
         Xiaomi              0

	 */
	@Test
	void test38() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.map(f -> f.getNombre() + " - " + f.getProductos().size())
				.sorted(comparing(s -> Integer.parseInt(s.split(" - ")[1]), reverseOrder()))
				.toList();

		System.out.println("Fabricante     Productos");
		System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
		result.forEach(System.out::println);
	}
	
	/**
	 * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes. 
	 * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 * Deben aparecer los fabricantes que no tienen productos.
	 */
	@Test
	void test39() {
		var listFabs = fabRepo.findAll();
		//TODO

	}
	
	/**
	 * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€. 
	 * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
	 */
	@Test
	void test40() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
	 */
	@Test
	void test41() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> f.getProductos().size() >= 2)
				.map(f -> f.getNombre())
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(4, result.size());
	}
	
	/**
	 * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €. 
	 * Ordenado de mayor a menor número de productos.
	 */
	@Test
	void test42() {
		var listFabs = fabRepo.findAll();
		record numProducto(String nombre, int numP) {
		}
		var result = listFabs.stream()
				.map(f -> new numProducto(f.getNombre(),
						(int) f.getProductos().stream().filter(p -> p.getPrecio() >= 220).count()))

				.filter(f -> f.numP() > 0)
				.sorted(comparingInt(numProducto::numP).reversed())
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(3, result.size());
	}
	
	/**
	 * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 */
	@Test
	void test43() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> f.getProductos().stream().mapToDouble(p -> p.getPrecio()).sum() > 1000)
				.map(f ->f.getNombre())
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(1, result.size());
	}
	
	/**
	 * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 * Ordenado de menor a mayor por cuantía de precio de los productos.
	 */
	@Test
	void test44() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.sorted(comparingDouble(f -> f.getProductos().stream().mapToDouble(p -> p.getPrecio()).sum()))
				.filter(f -> f.getProductos().stream().mapToDouble(p -> p.getPrecio()).sum() > 1000)
				.map(f -> f.getNombre())
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(1, result.size());
	}
	
	/**
	 * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante. 
	 * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante. 
	 * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
	 */
	@Test
	void test45() {
		var listFabs = fabRepo.findAll();
		System.out.println("Nombre Producto			Precio 			Fabricante");
		var result = listFabs.stream()
				.sorted(comparing(f -> f.getNombre()))
				.map(f -> f.getProductos().stream()
						.max(comparingDouble(p -> p.getPrecio()))
						.map(p -> p.getNombre() + "-" + p.getPrecio() + "-" + f.getNombre())
						.orElse(null))
				.toList();

		result.forEach(System.out::println);
		Assertions.assertTrue(result.isEmpty());
	}
	
	/**
	 * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
	 * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
	 */
	@Test
	void test46() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.sorted(comparing(f -> f.getNombre()))
				.flatMap(f ->f.getProductos().stream()
						.filter(p ->p.getPrecio() >= f.getProductos().stream()
								.mapToDouble(pr -> pr.getPrecio()).average().orElse(0.0)
						)
						.sorted(comparing(p -> p.getPrecio(),reverseOrder()))
						.map(producto -> producto.getNombre() + "-" + producto.getPrecio() + "-" + f.getNombre()))
				.toList();

		result.forEach(System.out::println);
		Assertions.assertEquals(7, result.size());
	}

}
