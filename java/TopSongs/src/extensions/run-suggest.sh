#Customize variables to your environment in the "scripts.conf" file

#Customize host, port and credentials to fit your environment 

HOST="http://localhost"

#Port where REST server will be deployed 
RESTPORT="8020"

#Credentials for REST services (see README)
READERAUTH="rest-reader-user:reader"
WRITERAUTH="rest-writer-user:writer"
ADMINAUTH="rest-admin-user:admin"


##Do not modify variables below##
CONN="$HOST:$RESTPORT"
MCONN="$HOST:8002"

echo "
*** invoke resource - suggestions :
"

#Call our custom resource with GET
curl -s -H "Accept: application/xml" --digest --user $READERAUTH "$CONN/v1/resources/suggestions?rs:q=night"

