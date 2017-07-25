## Configurator Search Utility

This utility has a rest api where you can search car by various parameters. The information is served from redis cache.

#### Search
Application will fetch all records by matching(partial or full) search parameter with following fields
- Model Id, 
- Name, 
- Class Name, 
- Body Name

All records thus fetched will be collected and duplicates will be removed

- Api Url: ** GET ** http://<host_name>:<port>/search/<search_param>
e.g. http://localhost:8080/search/205

#### Warming the cache
Application will fetch data from corpinter service and save information in redis.
- Api Url: ** PUT ** http://<host_name>:<port>/cache
e.g. http://localhost:8080/cache

#### Clearing the cache
Application will clear data from redis.
- Api Url: ** DEL ** http://<host_name>:<port>/cache
e.g. http://localhost:8080/cache
