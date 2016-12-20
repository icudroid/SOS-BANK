Configuration
~~~~~~~~~~~~~

Exemple de fichier `application.yml` sur la machine de DEV :

.. code-block:: yaml

    spring:
    jpa:
        open_in_view: true
        show-sql: true
        hibernate:
            ddl-auto: none
        generate-ddl: false
        properties:
            hibernate:
                dialect: org.hibernate.dialect.PostgreSQLDialect
                cache:
                    use_second_level_cache: true
                    provider_class: org.hibernate.cache.EhCacheProvider

    datasource:
        driverClassName: org.postgresql.Driver
        url: jdbc:postgresql://localhost/starter
        username: starter
        password: starter
        maxActive: 100
        maxWait: 1000
        poolPreparedStatements: true
        defaultAutoCommit: true
        validationQuery: SELECT 1


    thymeleaf:
        # en dev à false pour hotswapper les vues
        cache: false

    security:
        basic:
            enabled: false

    starter:
        checksum.key: DesireSecretKeyR
        static.url: http://localhost/statics
        adminEntityProfile: PROFILE_ADMIN_ENTITY
        upload:
            directory: /home/dev/upload
        http:
            proxy:
                host:
                port:  0

            remoteApiBaseUrl: http://10.199.54.110/starter/api/v1
            connectTimeout: 6000


    multipart:
        maxFileSize: 10Mb

