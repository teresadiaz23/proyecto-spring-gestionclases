# Platón App Gestión de Clases
## Información sobre el proyecto
1. Al ejecutar el proyecto se cargan dos csv con los datos del ciclo de DAM, uno con los titulos, cursos, asignaturas y horarios y otro con los alunmos.
2. En la carpeta Archivos CSV hay otros archivos csv para probar la subida y procesamiento de ficheros csv, como son Datos ayf y Datos alumnos ayf con los datos del ciclo de Administración y Finanzas, realizándose la primera en el apartado Escuela, y la otra en Alumnos.
3. Los usuarios creados inicialmente no tienen contraseña, solo un código de invitación general (11111) por si se quiere probar sin necesidad de mandar el correo.
5. Cuando se cree desde la zona de administración un alumno o profesor nuevo, introducir un correo existente ya que es a donde se va a enviar el código de invitación.
4. En el repositorio se encuentra un archivo pdf con la solicitud de convalidación/ exención, para probar la subida de ficheros pdf para las situaciones excepcionales.
5. Además se crean en el CommandLineRunner tres usuarios, uno Jefe de Estudios, otro Profesor y otro Alumno con los siguientes datos para poder acceder a la aplicación:
    - Jefe de Estudios: email: angel@email.com; contraseña: 1234
    - Profesor: email: luismi@email.com; contraseña: 1234
    - Alumno: email: teresa@email.com; contraseña: 1234
6. Se pueden crear horarios nuevos desde la gestión de asignaturas, pero debe haber horarios libres, es decir, asignaturas dadas de baja.
7. En el listado de cursos que ve el profesor o jefe de estudios se pueden ver los alumnos matriculados en ese curso, seleccionando el nombre del curso, donde un jefe de estudios tendrá la opción de decir que un alumno tiene una asignatura aprobada del curso anterior.
8. Cuando un alumno solicita una situación excepcional o una ampliación de matrícula, al jefe de estudios le aparecerá una alerta en la barra de navegación para avisarle de que tiene solicitudes pendientes.
9. El botón de ampliación de matrícula solo se habilitará si el alumno es de 1º y tiene alguna asignatura aprobada, convalidada o exenta.
10. Cuando un alumno tiene confirmada una convalidación o exencion o bien tiene alguna asignatura aprobada del curso anterior, en su horario se pone esa hora gris indicando que la tiene libre, y si amplia matrícula, esta ocupará uno de los huecos.
11. El jefe de estudios confirmará una ampliación de matrícula comparando el horario del alumno con el horario de 2º.
12. Desde la sección de Alumnos se puede obtener el carnet de todos los alumnos. 