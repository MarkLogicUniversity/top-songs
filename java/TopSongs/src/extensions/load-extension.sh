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
*** Installing a custom resource:
"

# Install custom resource
# Note that metadata (provider, version, title, description, methods, parameters) are not required or validated,
# but provided for the management convenience of developers and administrators.
curl -i -H "Content-type: application/xquery" --digest --user $ADMINAUTH -T ./suggest.xqy "$CONN/v1/config/resources/suggestions?title=Search Suggestions&version=0.1&provider=cw&description=implement Search Suggestions&method=get&get:arg1=xs:string*"

