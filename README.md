
# mutante

Para ejecutar el api tener en cuenta los siguientes pasos:

LOCAL:

  1. Configurar acceso a base de datos cache de redis con las variables de ambiente
      - spring.redis.host
      - spring.redis.port
      
  2. Ejecutar proyecto spring.
  
  3. Validar urls de la api:
      - localhost:8080/mutant
      - localhost:8080/stats
      - localhost:8080/ok
      
      
NUBE:

  Despliegue automático con GITHUB, AWS ECS y  AWS MEMORYDB REDIS :
  
  1. Solo con hacer commit a la rama master en github es suficiente.
      Realiza las siguientes acciones automáticas:
     - Github compila jar y verifica unitarias, definido en el archivo ./github/workflows/aws.yml 
     - Github compila y sube la imagen docker con la aplicación a AWS ECR Registry, definido en el archivo ./github/workflows/aws.yml 
     - Se crea nueva tarea en el balanceador de cargas de aws con la nueva imagen docker.
  2. Revisar acceso a los endpoint del balanceador de carga en AWS ECS.
