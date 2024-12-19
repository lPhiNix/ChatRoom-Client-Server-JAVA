# ChatRoom-ClientServer-JAVA

ChatRoom-ClientServer-JAVA es una aplicación de chat cliente-servidor que utiliza UDP (User Datagram Protocol)   
para la comunicación entre el cliente y el servidor. Esta aplicación está organizada de manera modular y está basada   
en un diseño que sigue el patrón de comandos, lo que permite una gestión flexible y escalable de las acciones de los   
usuarios. A continuación, te haré una descripción detallada de cómo funciona el proyecto y de las clases que lo componen.

## Guía Completa para Utilizar la Aplicación de Chat Cliente-Servidor (UDP)
Esta guía está diseñada para explicar cómo utilizar la aplicación de chat entre clientes y servidores en una red utilizando el protocolo UDP. A continuación, encontrarás los pasos detallados para que puedas conectar, interactuar y desconectarte de la aplicación sin necesidad de entender su funcionamiento interno.

### Requisitos Previos
Antes de comenzar, asegúrate de cumplir con los siguientes requisitos:

- Tener Java instalado en tu máquina.
- Tener acceso a la terminal de comandos (o consola de tu sistema operativo).
- La aplicación debe estar compilada y lista para ejecutarse.
- Asegúrate de que tanto el servidor como el cliente estén en la misma red o puedas realizar conexiones de red entre ellos.

### Pasos para Usar la Aplicación

1. **Iniciar el Servidor**

   El servidor es el encargado de gestionar todas las conexiones de los clientes. Para iniciarlo, sigue estos pasos:

    1. **Ubica el archivo del servidor en tu sistema**  
       (probablemente un archivo .jar si la aplicación ya está compilada).

    2. Abre una terminal o consola en el directorio donde está el archivo del servidor.

    3. Ejecuta el siguiente comando para iniciar el servidor:
       ```bash
       java -jar server.jar  
       ```

    4. Verás mensajes en la consola que indican que el servidor está escuchando en un puerto para aceptar conexiones de los clientes. Asegúrate de que no haya errores al iniciar el servidor. Por ejemplo:

       ```bash
       Servidor iniciado, esperando conexiones...  
       ```  

   El servidor estará ahora en funcionamiento, esperando que los clientes se conecten.

2. **Iniciar el Cliente**
   Una vez que el servidor está en funcionamiento, puedes iniciar la aplicación cliente en una o varias máquinas que se conectarán al servidor para chatear.

    1. Ubica el archivo del cliente en tu sistema (probablemente también un archivo .jar).

    2. Abre una terminal en el directorio donde está el archivo del cliente.

    3. Ejecuta el siguiente comando para iniciar el cliente:

       ```bash
       java -jar client.jar 
       ```

   El cliente pedirá al usuario que ingrese un nombre de usuario para poder conectarse al servidor.

3. **Conectar al Servidor**  
   Cuando el cliente se inicia, el programa solicitará un nombre de usuario. Sigue estos pasos:

   Escribe un nombre de usuario en la terminal. El nombre de usuario debe ser único, ya que no puedes tener dos usuarios con el mismo nombre.


	```css
	Ingrese su nombre de usuario: [TuNombre] 
	```


	Después de ingresar el nombre de usuario, presiona Enter. El cliente intentará conectarse al servidor. Verás un mensaje de confirmación si la conexión es exitosa:  

	```css
	Conectado al servidor en la dirección [Dirección]
	```
 
	Si el nombre de usuario ya está en uso por otro cliente, el servidor enviará un mensaje de error al cliente y te pedirá que elijas otro nombre.  

4. **Enviar Mensajes al Chat**
   Una vez conectado, podrás comenzar a interactuar con otros usuarios en el chat. Aquí tienes las opciones disponibles para comunicarte:

    - Escribir mensajes al chat general: Puedes escribir cualquier mensaje en la consola, y el mensaje será enviado a todos los usuarios conectados al servidor. Solo tienes que escribir el mensaje y presionar Enter:

      ```text
      Hola, ¿cómo están todos? 
      ```

      Los demás usuarios conectados verán tu mensaje en su consola.

- Comandos especiales:

    1. Listar los usuarios conectados:

       Si deseas saber quiénes están en el chat, puedes usar el comando /users.

       Para hacerlo, simplemente escribe en la consola:

       ```bash
       /users
       ```

       El servidor enviará una lista de todos los usuarios conectados al chat y los verás en tu consola.

    2. Enviar un mensaje privado a otro usuario:

       Si deseas enviar un mensaje privado a un usuario en particular, utiliza el comando ``/private [NombreUsuario] [Mensaje]``.  
       Ejemplo: Si deseas enviar un mensaje privado a un usuario llamado "Juan":

       ```bash
       /private Juan Hola Juan, ¿cómo estás?
       ```

       Este mensaje solo será recibido por "Juan", no por los demás usuarios en el chat.

    3. Salir del chat:

       Si deseas terminar la sesión y desconectarte del servidor, puedes usar el comando /exit.  
       Para ello, escribe:

       ```bash
       /exit
       ```

       Esto cerrará la conexión y te desconectará del servidor.


5. Mensajes del Servidor  
   El servidor enviará varios tipos de mensajes que podrás ver en tu consola:

    - **Notificaciones de conexión y desconexión:** El servidor notificará cuando un usuario se conecta o desconecta del chat.
      Ejemplo:

      ```text
      El usuario Juan se ha conectado.  
      El usuario Maria se ha desconectado.  
      ```

      Errores y advertencias: Si escribes un comando incorrecto o si intentas enviar un mensaje a un usuario que no existe, el servidor te enviará un mensaje de error. Por ejemplo:

      ```bash
      Error: Usuario no encontrado. 
      ```


6. **Desconectar del Chat**
   Para finalizar tu sesión y desconectarte del servidor, puedes utilizar el comando ``/exit``. Esto enviará una notificación al servidor que eliminará tu usuario de la lista de usuarios conectados y te desconectará de la red.

   Cuando un cliente se desconecta correctamente, el servidor lo notificará a los demás usuarios. Luego, el cliente puede cerrar la aplicación.

7. **Repetir los Pasos o Iniciar Nuevas Conexiones**  
   Si deseas conectarte de nuevo, simplemente repite los pasos desde el inicio, eligiendo un nuevo nombre de usuario si el anterior ya está en uso.  
   Si el servidor está en ejecución, puedes conectar varios clientes al mismo tiempo y chatear entre ellos.  
   Comandos Disponibles  
   A continuación, un resumen de los comandos disponibles que puedes usar dentro de la aplicación cliente:

### Errores Comunes y Soluciones

- **"El nombre de usuario ya está en uso":** Si el nombre de usuario que elegiste ya está siendo utilizado por otro cliente, el servidor te pedirá que elijas otro nombre. Simplemente ingresa un nombre diferente y vuelve a intentarlo.

- **"Error al conectar al servidor":** Si el cliente no puede conectarse al servidor, puede ser por varios motivos (como problemas de red o el servidor no está en ejecución). Asegúrate de que el servidor esté en marcha y que ambos, servidor y cliente, estén en la misma red.

- **"Usuario no encontrado":** Si intentas enviar un mensaje privado a un usuario que no está conectado, recibirás este error. Verifica que el nombre del usuario sea correcto y que esté conectado al chat.

### Consejos Útiles

- Mantén tu conexión estable: Asegúrate de que tanto el servidor como el cliente estén ejecutándose correctamente durante toda la sesión.

- Usa nombres de usuario únicos: Si estás en una red con múltiples personas, asegúrate de que todos los usuarios utilicen nombres de usuario diferentes para evitar conflictos.  
