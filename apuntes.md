# Resumen del Proyecto: Portafolio Spring Boot

Este documento resume los pasos conceptuales y técnicos para crear, configurar y desplegar un proyecto de portafolio usando Java Spring Boot, IntelliJ IDEA, y PostgreSQL.

## 1. Creación del Proyecto (IntelliJ IDEA)

1.  **Herramienta:** Se utilizó IntelliJ IDEA 2025.2.3.
2.  **Asistente:** Se usó el asistente integrado **Spring Initializr**. Este es el método estándar de la industria, ya que configura automáticamente la estructura de Maven y las dependencias base de Spring Boot.
3.  **Configuración del Proyecto:**
    * **Tipo:** Maven (Sistema de gestión de dependencias y construcción del proyecto).
    * **Lenguaje:** Java 17.
    * **Packaging:** Jar (La aplicación se empaquetará como un archivo `.jar` ejecutable que contiene el servidor Tomcat integrado).

## 2. Dependencias Iniciales (pom.xml)

El archivo `pom.xml` es el corazón de un proyecto Maven; define todas las "librerías" externas que el proyecto necesita.

* **Spring Web:** El pilar para crear aplicaciones web. Incluye:
    * **Servidor Tomcat:** Un servidor web integrado para que la aplicación pueda correr por sí misma.
    * **Spring MVC:** El framework para manejar peticiones web (el patrón Modelo-Vista-Controlador).
* **Thymeleaf:** El "motor de plantillas". Su trabajo es conectar Java con HTML. Permite insertar datos dinámicos (variables, listas) desde el código Java en los archivos HTML antes de enviarlos al navegador.
* **Spring Boot DevTools:** Una herramienta de productividad. Su función principal es monitorear cambios en el código y **reiniciar automáticamente** el servidor, agilizando mucho el ciclo de desarrollo.

## 3. Estructura y "Hola Mundo" (Patrón MVC)

Se implementó el patrón Modelo-Vista-Controlador (MVC) para la página de inicio.

1.  **Controlador (`PortafolioController.java`):**
    * Se creó una clase Java anotada con **`@Controller`**. Esta anotación es fundamental; le dice a Spring que esta clase es un componente especial encargado de *recibir peticiones HTTP* (del navegador) y gestionar la respuesta.
2.  **Mapeo de Rutas (`@GetMapping`):**
    * Se creó un método dentro del controlador y se le anotó con **`@GetMapping("/")`**. Esta anotación "mapea" una URL (la raíz `/` del sitio) a ese método específico. Cuando un usuario visita `http://localhost:8080/`, Spring sabe que debe ejecutar *ese* método.
3.  **Vista (`index.html`):**
    * El método del controlador devolvía un `String` (ej. `"index"`). Spring Boot interpreta este String como el **nombre de la Vista** (un archivo HTML) que debe buscar en la carpeta de plantillas: `src/main/resources/templates/index.html`.
4.  **Ejecución:**
    * Se ejecutó el método `main` de la clase `PortafolioApplication.java` para iniciar el servidor Tomcat integrado.

## 4. Conexión Java -> HTML (Modelo Dinámico)

Para que la página no fuera estática, se enviaron datos desde el Controlador a la Vista.

1.  **Pasar Datos Simples (El `Model`):**
    * Se añadió un parámetro `Model model` al método del controlador. El **`Model`** es un objeto (similar a un mapa) que Spring provee para ser el "contenedor" de los datos que viajarán desde Java (Controlador) hasta el HTML (Vista).
    * Se usó `model.addAttribute("titulo", "...")` para añadir un dato (un `String`) a este contenedor.
    * En el HTML, se usó **`th:text="${titulo}"`**. Este es un atributo de Thymeleaf que instruye: "reemplaza el texto de esta etiqueta HTML con el valor de la variable `titulo` que viene en el Modelo".
2.  **Pasar Listas de Objetos (Bucles con `th:each`):**
    * Se creó una clase `Proyecto.java` (un POJO o "molde") para definir la estructura de un proyecto (título, descripción).
    * En el controlador, se creó una `List<Proyecto>` y se añadió al `Model`.
    * En el HTML, se usó **`th:each="p : ${proyectos}"`**. Este es el atributo de bucle de Thymeleaf. Clona la etiqueta HTML (ej. un `<div>`) por cada elemento (`p`) que encuentre en la lista (`${proyectos}`).

## 5. Configuración de Control de Versiones (Git y GitHub)

1.  **Repositorio Remoto:** Se creó un repositorio público en GitHub. Al crearlo, se configuró para que incluyera:
    * `README.md`: La portada del proyecto.
    * `LICENSE`: (Ej. `MIT License`).
    * `.gitignore` (Plantilla `Java`): **Archivo crucial**. Es una lista de archivos y carpetas (como `/target`, `.idea/`, `*.log`) que Git debe *ignorar* y nunca subir al repositorio.
2.  **Conexión Local (Terminal Git):**
    * `git init -b main`: Se inicializó un repositorio Git local en la carpeta del proyecto.
    * `git remote add origin [URL]`: Se "conectó" el repositorio local con la dirección del repositorio remoto en GitHub.
    * `git pull origin main --allow-unrelated-histories`: Se descargaron los archivos (`README`, `.gitignore`) de GitHub y se fusionaron con el código local, usando la bandera `--allow-unrelated-histories` para unir dos líneas de tiempo que no compartían un ancestro común.
    * `git add .`: Se prepararon todos los archivos del proyecto para ser "empaquetados".
    * `git commit -m "Mensaje"`: Se creó el "paquete" (commit) con un mensaje descriptivo.
    * `git push -u origin main`: Se subió el paquete de cambios al repositorio de GitHub.

## 6. Integración de Base de Datos y Seguridad

Para preparar el sistema de login, se instaló una base de datos PostgreSQL y se añadieron las dependencias de seguridad y persistencia.

1.  **Paso 1: Configurar la Base de Datos (PostgreSQL con Docker)**
    * Se instaló **Docker Desktop**, que requirió la activación de **WSL 2 (Subsistema de Windows para Linux)** como su motor de ejecución.
    * Se ejecutó el siguiente comando en la terminal para crear y correr un contenedor de PostgreSQL:
        ```bash
        docker run --name portafolio-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=portafolio_db -p 5432:5432 -d postgres
        ```
    * **Desglose del comando:**
        * `--name portafolio-db`: Nombra el contenedor para fácil identificación.
        * `-e POSTGRES_USER=postgres`: Establece una variable de entorno para crear el usuario `postgres`.
        * `-e POSTGRES_PASSWORD=admin`: Asigna la contraseña `admin` a ese usuario.
        * `-e POSTGRES_DB=portafolio_db`: Crea una base de datos inicial llamada `portafolio_db`.
        * `-p 5432:5432`: Mapea el puerto 5432 de la PC (localhost) al puerto 5432 interno del contenedor.
        * `-d postgres`: Ejecuta el contenedor en modo "detached" (segundo plano) usando la imagen oficial `postgres`.

2.  **Paso 2: Añadir Nuevas Dependencias (pom.xml)**
    * **Spring Security:** El framework principal para la seguridad. Gestiona la autenticación (quién eres / login), autorización (qué puedes hacer / permisos) y la protección de contraseñas (encriptación).
    * **Spring Data JPA:** La librería de persistencia (ORM). Permite "mapear" clases Java (llamadas "Entidades") directamente a tablas en la base de datos, eliminando la necesidad de escribir SQL manualmente.
    * **PostgreSQL Driver:** El conector/traductor específico que permite a Java (JPA) comunicarse con la base de datos PostgreSQL.

3.  **Paso 3: Conectar la Aplicación (`application.properties`)**
    * Se configuraron las propiedades `spring.datasource.url`, `username` y `password` en el archivo `src/main/resources/application.properties` para apuntar al contenedor de Docker.
    * Se añadió **`spring.jpa.hibernate.ddl-auto=update`**. Esta es una propiedad clave de JPA (Hibernate) que le ordena sincronizar la base de datos con el código. Si JPA ve una clase `@Entity` en Java que no existe como tabla en PostgreSQL, la crea automáticamente.