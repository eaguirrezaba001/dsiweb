
## dsiweb
# Desarrollo de sistemas de información web
__________________________________________________________

## eltenedor

## Base de datos
- Restaurante    Horarios de reserva: 21:00 – 21:15 – 21:30 – 22:00
  - Nombre
  - Longitud, Latitud
  - nºMesas (4 o 3 asientos)
  - Horario
  - Descripcion
  - Foto
- Usuario
  - Email
  - Pass
  - Móvil
  - DNI
  - Nombre
- - Reserva
  - Sin especificar, Foreing Keys??
- Saldo   Para simular la plataforma de pago

### Especificaciones y restricciones de la aplicación
- Un usuario no logueado, puede ver los restaurantes
- Solo el usuario logueado puede reservar
- El usuario logueado podrá ver restaurantes, ver sus reservas (anteriores, pendientes y canceladas), reservar y anular reservas.
- Cada reserva costara 10€
- Si una reserva se cancela antes de 24h se devolverá el importe, si se cancela después nos quedamos el 50%




## PROYECTO RESTAURANTE ANDROID - DSIWEB

- Client: Adndroid UI

- Server: Web Service

- Data Base
 - resevas (anuladas, pasadas, activas, )
 - clientes (email, password, edad, nombre, dni, telefono movil)
 - restaurantes (nombre, longitud, latitud, num mesas-4plazas-)
 - saldo (dni, cantidad)

### ESPECIFICACIONES TECNICAS:
- pantalla reserva: restaurante, fecha, num de personas
- se puede reservar cualquier dia de la semana
- 4 horarios de posibles reservas 9,00 - 9,15 - 9,30 - 10,00
- controlar la ocupacion de las mesas
- una misma persona puede reservar en distintos horarios el mismo dia
- registro obligatorio para realizar reservas
- cliente logeado puede: crear reserva, consultar reservas, anular reservas

- *** opcional: localizacion en google maps


