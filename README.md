
## Build
In project directory

``` mvn package ``` to build jar.

### application properties
Change configuration here. Default app port is 8081

### H2 database console
`localhost:8081/h2-console/`

View database here.

## Run
Example run commad

```cmd
 java -jar fileserver-0.0.1-SNAPSHOT.jar --com.demo.uploads.directory=/tmp/storage 
 ```


## POST /api/file

### ContentType
Use multipart/form-data as content type for sending files

### form data - Postman example
Select "form-data" for body and set:
key: "file"
value: `<selected-file>`

### File descriptior information

Every upload persist file in storage on filesystem. For every user separate directory  is created as private file storage. User email is used for directory name with "@" repalced with "__".
In user directory for every upload system creates directory with name based on random UUID. Uploaded file version is stored in this directory.
This way application can store multile versions of the same file. This versioning is tracked in database usig file-directory UUID name as file unique file-id

```json
{
    "owned": [
        {
            "fileId": "69a45db7-605e-44f2-8eea-b03e1c04f42c",
            "filename": "webinar.txt",
            "fileVersionNr": 1
        },
        {
            "fileId": "150942f3-11e8-45a4-94b4-9172a008ace8",
            "filename": "webinar.txt",
            "fileVersionNr": 2
        },
        {
            "fileId": "9e9d0f64-e33b-4c0c-a9c3-7607a6e46b67",
            "filename": "second.txt",
            "fileVersionNr": 1
        }
    ],
    "shared": [
        {
            "fileId": "c959f21b-e42a-493e-a47b-2113085ad551",
            "filename": "sessija.txt",
            "fileVersionNr": 1
        }
    ]
}
```
