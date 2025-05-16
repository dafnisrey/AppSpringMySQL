# API Spring - Mysql - Sakila

## Descripción:

He realizado esta aplicación con el propósito principal de **aprender y poner en práctica conocimientos de Spring**. Esto no significa que la aplicación no sea plenamente funcional.

La aplicación interactúa con la base de datos **Sakila**. Se trata de una base de datos totalmente libre que se puede usar en diferentes tipos de bases de datos. Para esta APP he decidido usarla en MySQL. La base de datos contiene 16 tablas relacionadas entre sí, lo que la convierte en una buena base de datos para este tipo de práctica.


## Seguridad:

La app usa una tabla de usuarios guardada en la base de datos principal, es decir en Sakila. Al iniciarse la app, de forma automática, inserta en la tabla 'usarios' un total de 3 usuarios y 2 admin, si es que no han sido insertados ya.

**Usuarios Normales:**
* **Acceso exclusivo a endpoints GET**
    * `usuario1`, contraseña: `pass1`
    * `usuario2`, contraseña: `pass2`
    * `usuario3`, contraseña: `pass3`

**Administradores:**
* **Acceso completo a todos los endpoints (GET, POST, PUT/PATCH, DELETE)**
    * `admin1`, contraseña: `adminpass1`
    * `admin2`, contraseña: `adminpass2`

## Instrucciones:

1.  **Clonar el repositorio:** Obtén el código fuente del proyecto desde el repositorio.
2.  **Instalar y activar MySQL Server:** Asegúrate de tener MySQL Server instalado y en ejecución en tu sistema.
3.  **Crear la base de datos Sakila:** Utiliza una herramienta de administración de MySQL para crear una base de datos llamada `sakila`.
4.  **Ejecutar los scripts SQL:** Navega al directorio `src/main/resources` dentro de la carpeta del proyecto y ejecuta los siguientes scripts SQL en la base de datos `sakila` en el orden indicado:
    * `sakila-schema.sql`: Crea la estructura de las tablas de la base de datos Sakila.
    * `sakila-data.sql`: Inserta los datos de ejemplo en las tablas de Sakila.
    * `usuarios-schema.sql`: Crea la tabla `usuarios` utilizada para la autenticación de la aplicación.
    **Nota:** Los scripts originales de Sakila (`sakila-schema.sql` y `sakila-data.sql`) también se pueden descargar directamente desde la página web oficial del proyecto Sakila si lo prefieres. Asegúrate de ejecutar también el script `usuarios-schema.sql` después de crear la base de datos.
5.  **Ejecutar la aplicación:** Puedes ejecutar el proyecto de dos maneras:
    * **Desde un IDE:** Importa el proyecto en tu IDE (IntelliJ IDEA, Eclipse, etc.) y ejecútalo como una aplicación Spring Boot.
    * **Usando Maven:** Abre una terminal en la raíz del directorio de tu proyecto (donde se encuentra el archivo `pom.xml`) y ejecuta el comando: `mvn spring-boot:run`

## Documentación de Endpoints:

### Endpoints GET:


**GET /actor**
+ Devuelve todos los actores de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /actor/{id}**
+ Devuelve la información del actor con ese ID
+ Path variable: El ID del actor

**GET /actor/name/{name}**
+ Devuelve el actor correspondiente a ese nombre
+ Path variable: El nombre del actor

**GET /actorinfo**
+ Devuelve todos los actorinfo de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`
 
**GET /actorinfo/{id}**
+ Devuelve el actorinfo del actor con ese ID
+ Path variable: El ID del actor

**GET /addresses**
+ Devuelve todas las address de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /address/{id}**
+ Devuelve el address con ese id
+ Path variable: El ID del address

**GET /address/{id}/stores**
+ Devuelve los stores asociados a esa address
+ Path variable: El ID del address

**GET /address/{id}/customers**
+ Devuelve los customers asociados a esa address
+ Path variable: El ID del customer

**GET /categories**
+ Devuelve todas las categorias que hay

**GET /cities**
+ Devuelve todas las ciudades de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /city/name/{name}**
+ Devuelve la ciudad con ese nombre
+ Path variable: El nombre de esa ciudad

**GET /city/{id}**
+ Devuelve la ciudad con ese id
+ Path variable: El ID de la ciudad

**GET /city/{id}/addresses**
+ Devuelve las addresses asociadas a esa ciudad
+ Path variable: El ID de la ciudad

**GET /countries**
+ Devuelve todos los países de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`
 
**GET /country/{id}**
+ Devuelve el país con ese ID
+ Path variable: El ID del país

**GET /country/{id}/cities**
+ Devuelve las ciudades asociadas al país con ese ID
+ Path variable: El ID del país

**GET /customers**
+ Devuelve todos los customers de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /customer/{id}**
+ Devuelve el customer con ese ID
+ Path variable: El ID del customer

**GET /film/{id}/actors**
+ Devuelve los actores que participan en esa película
+ Path variable: El ID de la película

**GET /actor/{id}/films**
+ Devuelve las películas en las que participa ese actor
+ Path variable: El ID del actor

**GET /film/{id}/category**
+ Devuelve la categoria de esa película
+ Path variable: El ID de la película

**GET /category/{id}/films**
+ Devuelve las películas que son de esa categoría de forma paginada
+ Path variable: El ID de la categoría
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /films**
+ Devuelve todas las peliculas de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /film/{id}**
+ Devuelve la película con ese ID
+ Path variable: El ID de la película

**GET /actor/{id}/filmactors**
+ Devuelve los filmactors asociados al actor con ese ID
+ Path variable: El ID del actor

**GET /film/{id}/filmactors**
+ Devuelve los filmactors asociados al film con ese ID
+ Path variable: El ID de la película

**GET /inventory**
+ Devuelve todo el inventario de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /film/{id}/inventory**
+ Devuelve el inventario de la película con ese ID
+ Path variable: El ID de la película

**GET /language**
+ Devuelve todos los idiomas

**GET /language/{id}/films**
+ Devuelve las peliculas asociadas al idioma con ese ID
+ Path variable: El ID del idioma
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /payments**
+ Devuelve todos los payments de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /payment/{id}**
+ Devuelve el payment con ese ID
+ Path variable: El ID del payment

**GET /customer/{id}/payments**
+ Devuelve los payments de ese customer de forma paginada
+ Path variable: El ID del customer
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /staff/{id}/payments**
+ Devuelve los payments de ese staff de forma paginada
+ Path variable: El ID del staff
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /rental/{id}/payments**
+ Devuelve los payments asociados a ese rental
+ Path variable: El ID del rental

**GET /rentals**
+ Devuelve todos los rentals de forma paginada
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /rental/{id}**
+ Devuelve el rental con ese ID
+ Path variable: El ID del rental

**GET /customer/{id}/rentals**
+ Devuelve los rental de ese customer de forma paginada
+ Path variable: El ID del customer
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /staff**
+ Devuelve todos los staff

**GET /staff/{id}**
+ Devuelve el staff con ese ID
+ Path variable: El ID del staff

**GET /staff/{id}/stores**
+ Devuelve los stores asociados a ese staff
+ Path variable: El ID del staff

**GET /staff/{id}/rentals**
+ Devuelve los rentals de ese staff de forma paginada
+ Path variable: El ID del staff
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /stores**
+ Devuelve todos los stores

**GET /store/{id}**
+ Devuelve el store con ese ID
+ Path variable: El ID del store

**GET /store/{id}/customers**
+ Devuelve los customers de ese store de forma paginada
+ Path variable: El ID del store
+ Parámetros: `numPagina`, `tamañoPagina`

**GET /store/{id}/staff**
+ Devuelve el staff asociado a ese store
+ Path variable: El ID del store

**GET /store/{id}/inventory**
+ Devuelve los inventory de ese store de forma paginada
+ Path variable: El ID del store
+ Parámetros: `numPagina`, `tamañoPagina`



### Endpoints POST:

**POST /actor**
+ Guarda un actor nuevo
+ Requiere body: `first_name` (obligatorio), `last_name` (obligatorio)

**POST /address**
+ Guarda un address nuevo
+ Requiere body: 
   + Obligatorios: `address`, `district`, `postal_code`, `phone`, `latitud`, `longitud`, `city_id`
   + Opcionales: `address2`

**POST /category**
+ Guarda una categoría nueva
+ Requiere body: `name` (obligatorio)

**POST /city**
+ Guarda una ciudad nueva
+ Requiere body: `name` (obligatorio), `country_id` (obligatorio)

**POST /country**
+ Guarda un país nuevo
+ Requiere body: `name` (obligatorio)

**POST /customer**
+ Guarda un customer nuevo
+ Requiere body:
   + Obligatorios: `first_name`, `last_name`, `email`, `active` (1 o 0), `store_id`, `address_id`

**POST /film**
+ Guarda un film nuevo
+ Requier body:
   + Obligatorios: `title`, `language_id`
   + Opcionales: `description`, `release_year`, `original_language_id`, `rental_duration`, `rental_rate`, `length`, `replacement_cost`, `rating`(G, PG, PG-13, R, NC-17)

**POST /filmactor**
+ Guarda un filmactor nuevo
+ Requiere body: `actor_id` (obligatorio), `film_id` (obligatorio)

**POST /filmcategory**
+ Guarda un filmcategory nuevo
+ Requiere body: `film_id` (obligatorio), `category_id` (obligatorio)

**POST /inventory**
+ Guarda un inventory nuevo
+ Requiere body: `film_id` (obligatorio), `store_id` (obligatorio)

**POST /language**
+ Guarda un idioma nuevo
+ Requiere body: `name` (obligatorio)

**POST /payment**
+ Guarda un payment nuevo
+ Requiere body:
   + Obligatorios: `customer_id`, `staff_id`, `rental_id`, `amount`

**POST /rental**
+ Guarda un rental nuevo
+ Requiere body:
   + Obligatorios: `customer_id`, `staff_id`, `inventory_id`

**POST /staff**
+ Guarda un staff nuevo
+ Requiere body:
   + Obligatorios: `first_name`, `last_name`, `active` (1 o 0), `username`, `address_id`, `store_id`
   + Opcionales: `picture`, `email`, `password`

**POST /store**
+ Guarda un store nuevo
+ Requiere body: `customer_id` (obligatorio), `staff_id` (obligatorio)



### Enpoints PATCH:

**PATCH /actor/{id}**
+ Actualiza el actor con ese ID
+ Path variable: El ID del actor
+ Requiere body: `first_name` (opcional), `last_name` (opcional)

**PATCH /address/{id}**
+ Actualiza el address con ese ID
+ Path variable: El ID del address
+ Requiere body: 
   + Opcionales: `address`, `address2`, `district`, `city_id`, `postal_code`, `phone`

**PATCH /category/{id}**
+ Actualiza la categoría con ese ID
+ Path variable: El ID de la categoría
+ Requiere body: `name` (opcional)

**PATCH /city/{id}**
+ Actualiza la ciudad con ese ID
+ Path variable: El ID de la ciudad
+ Requiere body: `name` (opcional), `country_id` (opcional)

**PATCH /country/{id}**
+ Actualiza el país con ese ID
+ Path variable: El ID del país
+ Requiere body: `name` (opcional)

**PATCH /customer/{id}**
+ Actualiza el customer con ese ID
+ Path variable: El ID del customer
+ Requiere body: 
   + Opcionales: `first_name`, `last_name`, `email`, `active` (1 o 0), `store_id`, `address_id`

**PATCH /filmactor**
+ Actualiza el filmactor que corresponde a esos valores
+ Parámetros: oldFilmId, oldActorId, newFilmId, newActorId

**PATCH /filmcategory**
+ Actualiza el filmcategory que corresponde a esos valores
+ Parámetros: oldFilmId, oldCategoryId, newFilmId, newCategoryId

**PATCH /film/{id}**
+ Actualiza la película con ese ID
+ Path variable: El ID de la película
+ Requiere body: 
   + Opcionales: `title`, `description`, `release_year`, `language_id`, `original_language_id`, `rental_duration`, `rental_rate`, `length`, `replacement_cost`

**PATCH /inventory/{id}**
+ Actualiza el inventory con ese ID
+ Path variable: El ID del inventory
+ Requiere body: `film_id`, `store_id`

**PATCH /payment/{id}**
+ Actualiza el payment con ese ID
+ Path variable: El ID del payment
+ Requiere body: 
   + Opcionales: `amount`, `customer_id`, `staff_id`, `rental_id`

**PATCH /rental/{id}**
+ Actualiza el rental con ese ID
+ Path variable: El ID del rental
+ Requiere body: 
   + Opcionales: `inventory_id`, `customer_id`, `staff_id`, `return_date`

**PATCH /staff/{id}**
+ Actualiza el staff con ese ID
+ Path variable: El ID del staff
+ Requiere body: 
   + Opcionales: `first_name`, `last_name`, `email`, `active` (1 o 0), `store_id`, `address_id`, `username`, `password`

**PATCH /store/{id}**
+ Actualiza el store con ese ID
+ Path variable: El ID del store
+ Requiere body: `staff_id` (opcional), `address_id` (opcional)



## Endpoints DELETE:

**DELETE /actor/{id}**
+ Elimina el actor con ese ID

**DELETE /address/{id}**
+ Elimina el address con ese ID

**DELETE /category/{id}**
+ Elimina el category con ese ID

**DELETE /city/{id}**
+ Elimina el city con ese ID

**DELETE /country/{id}**
+ Elimina el country con ese ID

**DELETE /customer/{id}**
+ Elimina el customer con ese ID

**DELETE /actor/{id}/filmactors**
+ Elimina los filmactors asociados a ese actor_id

**DELETE /film/{id}/filmactors**
+ Elimina los filmactors asociados a ese film_id

**DELETE /category/{id}/filmcategories**
+ Elimina los filmcategories asociados a esa category_id

**DELETE /film/{id}/filmcategories**
+ Elimina los filmcategories asociados a ese film_id

**DELETE /film/{id}**
+ Elimina la película con ese ID

**DELETE /inventory/{id}**
+ Elimina el inventory con ese ID

**DELETE /language/{id}**
+ Elimina el language con ese ID

**DELETE /payment/{id}**
+ Elimina el payment con ese ID

**DELETE /rental/{id}**
+ Elimina el rental con ese ID

**DELETE /staff/{id}**
+ Elimina el staff con ese ID

**DELETE /store/{id}**
+ Elimina el store con ese ID

































































