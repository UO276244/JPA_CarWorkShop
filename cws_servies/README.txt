UO276244
MARTIN BELTRAN DIAZ
EXERCISE 1: Voucher 25 and Client management
EXTRA EXERCISES: voucher 20, voucher 30, Every annotation to orm.xml

puede que en el proyecto haya WARNINGS, los cuales se deben a que la versión del
JRE usada en el proyecto no es la misma que la instalada.

Los tests dentro de este proyecto pueden fallar si no se resetea la 
BD después de ejecutar los tests de la extensión debido a 
que éstos dejan datos que impiden a los otros tests pasar.


