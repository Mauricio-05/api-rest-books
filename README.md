# API Rest books. 📚
Esta es una API RESTful construida con Spring Boot que permite gestionar libros y autores.
Proporciona endpoints para crear, leer, actualizar y eliminar tanto libros como autores. 
También permite asociar libros con autores.

## Requisitos. ✏️

- Tener Java 17 instalado o una version mayor.
- Tener Docker instalado.

## Tecnologías utilizadas. 💻

- Java 17.
- Spring boot.
- Spring Data JPA.
- Hibernate.
- H2 (Pruebas).
- Maven.
- Github Actions.
- Postgres.
- Docker.

## Clonar repositorio. ⬇️
 ```bash
  git clone https://github.com/Mauricio-05/api-rest-books.git
 ```

## Configuración del proyecto. 🔩

> [!NOTE]
> En Windows, si no tienes Maven instalado globalmente, asegúrate de utilizar `.\mvnw` en lugar de `mvn` 
para todos los comandos de Maven en este proyecto. En Linux y macOS puedes usar `./mvnw` .

1. Navegar al directorio del proyecto.
      ```bash
      cd your_path/book
      ```
2. Instalar las dependencias de Maven.
      ```bash
      mvn clean install
      ```
3. Crear la base de datos con docker compose.
   ```bash
   docker compose up books_db_dev
      ```
4. Ejecutar la aplicación.
   ```bash
   mvn spring-boot:run
      ```

## Endpoints 🌐

- `http://localhost:8080` - URL base luego de la API.
  - `/api/authors` - CRUD para autores.
  - `/api/books` - CRUD para libros.

> [!NOTE]
> Cada ruta admite los siguientes métodos HTTP.

- **POST**: Crea un nuevo recurso.

- **GET**: Obtiene uno o más recursos.

- **PUT**: Reemplaza completamente un recurso existente.

- **PATCH**: Actualiza parcialmente un recurso existente.

- **DELETE**: Elimina un recurso existente.

Cada método HTTP cumple una función específica en la manipulación de los datos. Asegúrate de utilizar el método
adecuado según la acción que desees realizar en la API.


