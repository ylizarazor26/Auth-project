### Auth Service Versión No.2 — JWT + Roles + Permisos (RBAC)

Microservicio de autenticación desarrollado con Spring Boot que implementa control de acceso basado en roles y permisos (RBAC) usando JWT.
El servicio permite el registro, autenticación y administración básica de usuarios, con seguridad aplicada por endpoint y método HTTP.

#### Tecnologías
* Java 17
* Spring Boot
* Spring Security
* JWT (io.jsonwebtoken)
* PostgreSQL
* JPA / Hibernate

#### Funcionalidades
* Registro de usuarios
* Login con generación de JWT
* Roles y permisos persistidos en base de datos
* Autorización por permisos (CREATE, READ, UPDATE, DELETE)
