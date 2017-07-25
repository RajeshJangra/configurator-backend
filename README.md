## Car Configurator Search Utility

Ths utility has a rest api where you can search car by model information. The information is cached first time 
and subsequently fetched from redis cache.

#### Search
The application will search by search parameter in following fields (partial or full)
- Model Id, 
- Name, 
- Class Name, 
- Body Name

All the records thus fetched will be collected and duplicates will be removed

- Api Url: http://<host_name>:<port>/search/<search_param>
e.g. http://localhost:8080/search/205

#### Warming the cache
http://<host_name>:<port>/search/<name>
e.g. http://localhost:8080/search/205066_000

http://<host_name>:<port>/search/<model_id_partial>
e.g. http://localhost:8080/search/5066