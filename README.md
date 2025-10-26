# TALLER DE MICROSERVICIOS

En este taller se implementó un sistema de publicación de posts de hasta 140 caracteres, similar al funcionamiento de Twitter. El sistema está construido utilizando Quarkus, un framework de Java diseñado para aplicaciones nativas en la nube. 
Se ha implementado como un monolito inicialmente, luego dividido en tres microservicios independientes utilizando AWS Lambda y AWS Cognito.

## Diseño de la aplicación 

La aplicación está diseñada para cumplir con los requisitos especificados en el enunciado del taller y proporcionar una experiencia de usuario fluida y satisfactoria.
El primer paso para el desarrollo de este laboratorio, fue la implementación de un monolito haciendo uso del
framework Quarkus.  A continuación, se describen los principales componentes y características de la aplicación:

- Se implementaron tres entidades fundamentales para el sistema: `Post`, `Stream` y `User`. 

- La clase `Post` representa cada publicación realizada en el sistema, con atributos que incluyen el propietario y el contenido del post. 

- La clase `Stream` encapsula una colección de posts, ofreciendo métodos para agregar y obtener posts. 

- La clase `User` proporciona la estructura necesaria para representar a los usuarios del sistema, incluyendo atributos como el nombre de usuario y la contraseña. Estos objetos `User` son fundamentales para identificar a los propietarios de los posts.

- La clase `PostController` es un controlador REST diseñado para manejar las solicitudes relacionadas con la entidad `Post`.
  Dentro de esta clase, se encuentra el método `savePost`,  el cual se encarga de recibir las solicitudes de los usuarios para publicar posts en el sistema y guardar la información en la capa de persistencia correspondiente. 

- La clase `StreamController` es un controlador REST diseñado para manejar las solicitudes relacionadas con la obtención de posts desde el sistema. Esta clase incluye el método `getPosts`, el cual se encarga de recuperar los posts almacenados en el sistema y devolverlos en formato JSON como respuesta a la solicitud.

- La clase `UserController` es un controlador REST diseñado para manejar las solicitudes relacionadas con la gestión de usuarios en el sistema

-  Para el monolito en Quarkus, se implementó la capa de persistencia en las clases Services de cada uno de los controladores. Para el despliegue en AWS, se hace uso de una base de datos Mongo.

## Guía de Inicio

Las siguientes instrucciones le permitirán descargar una copia y ejecutar la aplicación en su máquina local.

### Prerrequisitos

- Java versión 8 OpenJDK
- Maven
- Git

1. Ubíquese sobre el directorio donde desea realizar la descarga y ejecute el siguiente comando:
```shell script
  git clone https://github.com/juliandtrianar/lab7-AREP.git
```
2. Navegue al directorio del proyecto:
```shell script
  cd lab7-AREP
```

3. Ejecute el siguiente comando para compilar el código:

```shell script
  mvn compile
```

4. Para iniciar el servidor, ejecute el siguiente comando:
```shell script
  mvn compile quarkus:dev
```

Debería ver algo así en consola:
<img width="1202" height="186" alt="image" src="https://github.com/user-attachments/assets/ee0e103d-c1c9-42a6-9f34-147d7eb8450d" />


## Probando la Aplicación.

Ingrese a la siguiente URL para ingresar a el cliente: 
```
http://localhost:8080/index.html
```
![image](https://github.com/user-attachments/assets/87c0cff7-afcb-4a3e-95a7-3fa99b2faf49)

Ingrese la mensaje a postear:

![image](https://github.com/user-attachments/assets/9979ca1d-8579-4f31-80a4-fe793918858e)


De clic en el botón `Postear`,  podrá observar que los mensajes se muestran en la parte inferior.
![image](https://github.com/user-attachments/assets/154ad52c-7f07-4e0e-8bd2-17388a43d4b0)

# Despliegue en Amazon Web Services

A continuación, se describe la arquitectura de la aplicación en Amazon Web Services (AWS). La aplicación se despliega en la infraestructura de la nube de AWS para garantizar escalabilidad, disponibilidad y seguridad.

## Arquitectura

![image](https://github.com/user-attachments/assets/a21fa032-da64-4028-8af9-b41b22664249)

## Funciones Lambda

Se desplegaron dos microservicios utilizando funciones Lambda de AWS. Estos microservicios establecen conexión con una base de datos `MongoDB` para almacenar y gestionar los datos de la aplicación.
Para cada uno de los microservicios, se compiló el proyecto ubicado en la rama `cloud` del repositorio para generar un JAR con todas las dependencias necesarias para su despliegue en AWS Lambda.

`post-function`: Esta función Lambda implementa la lógica para almacenar los posts en la base de datos `MongoDB`. Cuando se invoca, recibe un nuevo post como entrada y lo guarda en la base de datos para su posterior recuperación y visualización. Es responsable de asegurar que los posts enviados por los usuarios se almacenen correctamente en la base de datos para su uso posterior. Se mapeó el método `savePost` de la clase `PostController` para este microservicio.

`stream-function`: Esta función Lambda se encarga de proporcionar información sobre los posts almacenados en la base de datos MongoDB. Se mapeó el método `getPosts` de la clase `StreamController` para este microservicio. Al ser invocado, este método recupera los posts de la base de datos y los devuelve como respuesta al cliente que realizó la solicitud.

El uso de funciones Lambda de AWS permite una arquitectura sin servidor, escalable y de alto rendimiento.



## Amazon S3

Utilizamos Amazon S3 para almacenar nuestros archivos estáticos, como HTML, CSS y JavaScript. Esto nos permite distribuir y servir estos archivos de manera eficiente a través de internet para nuestra aplicación web. Creamos un bucket para nuestra aplicación:


## Amazon Cognito

En nuestra aplicación, utilizamos Amazon Cognito para gestionar la autenticación de usuarios antes de que accedan al sitio web estático alojado en Amazon S3. Cuando un usuario intenta acceder al sitio web, lo redirigimos a una página de inicio de sesión vinculada a Amazon Cognito. Allí, el usuario proporciona sus credenciales de inicio de sesión y Amazon Cognito valida estas credenciales. Si son válidas, el usuario recibe un token de acceso que lo identifica. Con este token, el usuario es redirigido de vuelta al sitio web estático, donde el token se utiliza para validar su acceso y permitirle interactuar con la aplicación web estática de acuerdo con sus permisos. 

Luego de autenticarse en un portal integrado por este servicio, se realiza la redirección al sitio web estatico enviando un token JWT en la URL del mismo.



Dicho token es esencial para poder consumir los servicios del back (endpoints del API Gateway). Para consultarlo desde la URL se ha modificado el JavaScript para que lea la URL, extraiga este token y lo almacene de tal forma que lo use cada vez que se consulten los servicios del back.

Con esto se asegura que solo aquellos agentes autorizados sean capaces de consumir los servicios de la solución.

## API Gateway 

En nuestra aplicación, hemos configurado el Gateway de API de AWS para actuar como el punto de entrada principal.


Al definir recursos y métodos dentro de nuestra API, hemos establecido la estructura y el comportamiento de la interfaz que nuestra aplicación ofrece a los usuarios. Luego, hemos asignado nuestras funciones Lambda específicas para manejar estas solicitudes entrantes, asegurándonos de que cada solicitud se dirija al código adecuado para su procesamiento. 

Seguridad de los endpoints:

![image](https://github.com/user-attachments/assets/9e9f9416-4628-45ad-96c9-aa4b2c75f06f)

![image](https://github.com/user-attachments/assets/73ff8759-e38b-4b2f-944e-fa6fc69642a5)


## Video de la aplicacion desplegada y funcionando

https://youtu.be/ys2xsuERCPc

## Autores

- Julián David Triana Roa
- Andres Jacobo Sepulveda Sanchez


## Versión
1.0.0
