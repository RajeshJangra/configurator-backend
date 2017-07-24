# Daimler Car Configurator Search utility

Ths utility has a rest api where you can search car by model information. The information is cached first time 
and subsequently fetched from redis cache.

#### Search for Model Id
http://<host_name>:<port>/search?searchParam=<model_id>
e.g. http://localhost:8080/search?searchParam=205066_000
